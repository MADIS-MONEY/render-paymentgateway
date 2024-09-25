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
public class Hub2PaymentIntentAttemptRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String id;
    private String token;
    private String paymentMethod;
    private String country;
    private String provider;
    private MobileMoney mobileMoney;
}
