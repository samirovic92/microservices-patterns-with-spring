package com.samic.commonService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    private String name;
    private String cardNumber;
    private int validUntilMonth;
    private int validUntilYear;
    private String cvv;
}
