package com.example.usagetranslator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chargeable {
    private Long id;
    private Integer partnerID;
    private String product;
    private String partnerPurchasedPlanID;
    private String plan;
    private Integer usage;

    public Chargeable(Integer partnerID, String product, String partnerPurchasedPlanID, String plan, Integer usage) {
        this(null, partnerID, product, partnerPurchasedPlanID, plan, usage);
    }
}
