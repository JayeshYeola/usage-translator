package com.example.usagetranslator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainRecord {
    private Long id;
    private String partnerPurchasedPlanID;
    private String domain;

    public DomainRecord(String partnerPurchasedPlanID, String domain) {
        this(null, partnerPurchasedPlanID, domain);
    }
}
