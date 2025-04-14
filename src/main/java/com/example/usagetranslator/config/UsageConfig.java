package com.example.usagetranslator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "usagetranslator")
public class UsageConfig {

    private List<Integer> skipPartnerIds;

    private Map<String, Integer> unitReduction;

    public List<Integer> getSkipPartnerIds() {
        return skipPartnerIds;
    }

    public void setSkipPartnerIds(List<Integer> skipPartnerIds) {
        this.skipPartnerIds = skipPartnerIds;
    }

    public Map<String, Integer> getUnitReduction() {
        return unitReduction;
    }

    public void setUnitReduction(Map<String, Integer> unitReduction) {
        this.unitReduction = unitReduction;
    }
}