package com.example.usagetranslator.service;

import com.example.usagetranslator.config.UsageConfig;
import com.example.usagetranslator.model.Chargeable;
import com.example.usagetranslator.model.CsvRecord;
import com.example.usagetranslator.model.DomainRecord;
import com.example.usagetranslator.utils.CsvUtil;
import com.example.usagetranslator.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UsageServiceImpl implements UsageService {


    private final List<Integer> partnerIdSkipList;
    private final Map<String, Integer> unitReductionMap;

    public UsageServiceImpl(UsageConfig config) {
        this.partnerIdSkipList = config.getSkipPartnerIds();
        this.unitReductionMap = config.getUnitReduction();
    }

    @Override
    public void processUsage(String csvFilePath, String jsonFilePath) {
        List<CsvRecord> csvRecords = CsvUtil.readCsv(csvFilePath);
        Map<String, String> typeMapping = JsonUtil.readJson(jsonFilePath);

        List<Chargeable> chargeableList = new ArrayList<>();
        Set<DomainRecord> domainRecords = new HashSet<>();
        Map<String, Integer> productTotals = new HashMap<>();

        for (CsvRecord record : csvRecords) {
            if (record.getPartNumber() == null || record.getPartNumber().isEmpty() || record.getItemCount() <= 0) {
                log.info("Skipping record with partner guid {} and Item Name {} for reason: Part number is missing or Item count is 0/negative",
                        record.getPartnerGuid(), record.getItemName());
                continue;
            }
            if (partnerIdSkipList.contains(record.getPartnerID())) {
                log.info("Skipping record with partner guid {} and Item Name {} for reason: Partner Id in skip list",
                        record.getPartnerGuid(), record.getItemName());
                continue;
            }
            String product = getProductMapping(record.getPartNumber(), typeMapping);
            String partnerPurchasedPlanID = getCleanedAccountGuid(record.getAccountGuid());
            Integer usage = calculateUsage(record.getPartNumber(), record.getItemCount());
            Chargeable chargeable = new Chargeable(record.getPartnerID(), product, partnerPurchasedPlanID, record.getPlan(), usage);
            chargeableList.add(chargeable);

            if (record.getDomains() != null && !record.getDomains().isEmpty()) {
                DomainRecord domainRecord = new DomainRecord(partnerPurchasedPlanID, record.getDomains());
                domainRecords.add(domainRecord);
            }
            productTotals.merge(product, usage, Integer::sum);
        }

        String chargeableSql = generateChargeableInsert(chargeableList);
        String domainsSql = generateDomainsInsert(new ArrayList<>(domainRecords));

        log.info("SQL for chargeable table:\n{}", chargeableSql);
        log.info("SQL for domains table:\n{}", domainsSql);
        log.info("Running totals for each product:");
        productTotals.forEach((prod, total) -> log.info("Product: {}, Total usage: {}", prod, total));

        System.out.println("SQL for chargeable table:\n" + chargeableSql);
        System.out.println("SQL for domains table:\n" + domainsSql);
        System.out.println("Running totals for each product:");
        productTotals.forEach((prod, total) -> System.out.println("Product: " + prod + ", Total usage: " + total));
    }

    private Integer calculateUsage(String partNumber, int itemCount) {
        if (partNumber == null) {
            return itemCount;
        }
        Integer divisor = unitReductionMap.get(partNumber.toUpperCase());
        return (divisor != null) ? itemCount / divisor : itemCount;
    }

    private String getCleanedAccountGuid(String accountGuid) {
        if (accountGuid == null) {
            return "";
        }
        String cleaned = accountGuid.replaceAll("[^A-Za-z0-9]", "");
        return cleaned.length() > 32 ? cleaned.substring(0, 32) : cleaned;
    }

    private String getProductMapping(String partNumber, Map<String, String> typeMapping) {
        if (typeMapping.containsKey(partNumber)) {
            return typeMapping.get(partNumber);
        } else {
            log.warn("No mapping found for partNumber: {}. Using partNumber as product value.", partNumber);
            return partNumber;
        }
    }

    private String generateChargeableInsert(List<Chargeable> chargeables) {
        if (chargeables.isEmpty()) {
            return "-- No valid chargeable records to insert.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO chargeable (partnerID, product, partnerPurchasedPlanID, plan, usage) VALUES ");

        for (int i = 0; i < chargeables.size(); i++) {
            Chargeable c = chargeables.get(i);
            String productEscaped = escapeSql(c.getProduct());
            String planEscaped = escapeSql(c.getPlan());
            String partnerPlanIDEscaped = escapeSql(c.getPartnerPurchasedPlanID());

            String tuple = String.format("(%d, '%s', '%s', '%s', %d)",
                    c.getPartnerID(),
                    productEscaped,
                    partnerPlanIDEscaped,
                    planEscaped,
                    c.getUsage());
            sb.append(tuple);

            if (i < chargeables.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(";");
        return sb.toString();
    }

    private String generateDomainsInsert(List<DomainRecord> domainRecords) {
        if (domainRecords.isEmpty()) {
            return "-- No domain records to insert.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO domains (partnerPurchasedPlanID, domain) VALUES ");

        for (int i = 0; i < domainRecords.size(); i++) {
            DomainRecord d = domainRecords.get(i);
            String domainEscaped = escapeSql(d.getDomain());
            String partnerPlanIDEscaped = escapeSql(d.getPartnerPurchasedPlanID());
            String tuple = String.format("('%s', '%s')", partnerPlanIDEscaped, domainEscaped);
            sb.append(tuple);

            if (i < domainRecords.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(";");
        return sb.toString();
    }

    private String escapeSql(String input) {
        return (input == null) ? "" : input.replace("'", "''");
    }
}
