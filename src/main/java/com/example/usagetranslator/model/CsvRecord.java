package com.example.usagetranslator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvRecord {
    private int partnerID;
    private String partnerGuid;
    private int accountId;
    private String accountGuid;
    private String username;
    private String domains;
    private String itemName;
    private String plan;
    private String itemType;
    private String partNumber;
    private int itemCount;
}
