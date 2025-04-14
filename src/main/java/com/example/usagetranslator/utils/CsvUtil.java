package com.example.usagetranslator.utils;

import com.example.usagetranslator.model.CsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtil {

    public static List<CsvRecord> readCsv(String filePath) {
        List<CsvRecord> records = new ArrayList<>();

        try (InputStream is = CsvUtil.class.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .build();

            try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    try {
                        int partnerID = Integer.parseInt(csvRecord.get("PartnerID").trim());
                        String partnerGuid = csvRecord.get("partnerGuid").trim();
                        int accountId = Integer.parseInt(csvRecord.get("accountid").trim());
                        String accountGuid = csvRecord.get("accountGuid").trim();
                        String username = csvRecord.get("username").trim();
                        String domains = csvRecord.get("domains").trim();
                        String itemName = csvRecord.get("itemname").trim();
                        String plan = csvRecord.get("plan").trim();
                        String itemType = csvRecord.get("itemType").trim();
                        String partNumber = csvRecord.get("PartNumber").trim();
                        int itemCount = Integer.parseInt(csvRecord.get("itemCount").trim());

                        CsvRecord record = new CsvRecord(
                                partnerID, partnerGuid, accountId, accountGuid,
                                username, domains, itemName, plan, itemType,
                                partNumber, itemCount);
                        records.add(record);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error parsing numeric field: " + nfe.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to read or parse Csv file from: {}", filePath, e);
        }
        return records;
    }
}
