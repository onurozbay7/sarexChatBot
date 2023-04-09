package com.ozzie.sarexchatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleSheetsDto {

    private String pol;
    private String pod;
    private String carrier;
    private String containerType;
    private String podLocalCharges;
    private String currency;
    private String validity;
}
