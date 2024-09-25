package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Hub2PaymentIntentRequest implements Serializable {
    private String customerReference;
    private String purchaseReference;
    private Integer amount;
    private String currency;
    private String overrideBusinessName;

}
