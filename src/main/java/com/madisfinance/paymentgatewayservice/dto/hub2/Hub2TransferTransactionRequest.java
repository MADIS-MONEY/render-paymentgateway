package com.madisfinance.paymentgatewayservice.dto.hub2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hub2TransferTransactionRequest {
    private String reference;
    private int amount;
    private String currency;
    private String description;
    private Origin origin;
    private Destination destination;
    private String overrideBusinessName;
}
