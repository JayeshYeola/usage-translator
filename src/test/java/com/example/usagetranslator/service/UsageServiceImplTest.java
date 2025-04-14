package com.example.usagetranslator.service;

import com.example.usagetranslator.config.UsageConfig;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class UsageServiceImplTest {

    private UsageServiceImpl createServiceWithTestConfig() {
        UsageConfig config = new UsageConfig();
        config.setSkipPartnerIds(List.of(26392));
        Map<String, Integer> reductionMap = new HashMap<>();
        reductionMap.put("EA000001GB0O", 1000);
        reductionMap.put("PMQ00005GB0R", 5000);
        reductionMap.put("SSX006NR", 1000);
        reductionMap.put("SPQ00001MB0R", 2000);
        config.setUnitReduction(reductionMap);
        return new UsageServiceImpl(config);
    }

    @Test
    public void testCalculateUsage() throws Exception {
        UsageServiceImpl service = createServiceWithTestConfig();
        Method calculateUsage = UsageServiceImpl.class.getDeclaredMethod("calculateUsage", String.class, int.class);
        calculateUsage.setAccessible(true);
        int usageEA = (int) calculateUsage.invoke(service, "EA000001GB0O", 1000);
        assertEquals(1, usageEA);
        int usagePMQ = (int) calculateUsage.invoke(service, "PMQ00005GB0R", 5000);
        assertEquals(1, usagePMQ);
        int usageUnknown = (int) calculateUsage.invoke(service, "UNKNOWN", 3000);
        assertEquals(3000, usageUnknown);
    }

    @Test
    public void testGetCleanedAccountGuid() throws Exception {
        UsageServiceImpl service = createServiceWithTestConfig();
        Method getCleanedAccountGuid = UsageServiceImpl.class.getDeclaredMethod("getCleanedAccountGuid", String.class);
        getCleanedAccountGuid.setAccessible(true);
        String guid = "799ef0ab-4438-4157-8afc-f6fc4dfe9253";
        String expected = "799ef0ab443841578afcf6fc4dfe9253";
        String cleaned = (String) getCleanedAccountGuid.invoke(service, guid);
        assertEquals(expected, cleaned);
        String alreadyClean = "ABCDEF1234567890ABCDEF1234567890";
        String cleaned2 = (String) getCleanedAccountGuid.invoke(service, alreadyClean);
        assertEquals(alreadyClean, cleaned2);
    }

    @Test
    public void testGetProductMapping() throws Exception {
        UsageServiceImpl service = createServiceWithTestConfig();
        Method getProductMapping = UsageServiceImpl.class.getDeclaredMethod("getProductMapping", String.class, Map.class);
        getProductMapping.setAccessible(true);
        Map<String, String> typeMapping = new HashMap<>();
        typeMapping.put("ADS000010U0R", "core.chargeable.adsync");
        typeMapping.put("SSX006NR", "core.chargeable.addsharesyncspace");
        String mappedValue = (String) getProductMapping.invoke(service, "ADS000010U0R", typeMapping);
        assertEquals("core.chargeable.adsync", mappedValue);
        String fallback = (String) getProductMapping.invoke(service, "UNKNOWN", typeMapping);
        assertEquals("UNKNOWN", fallback);
    }
}
