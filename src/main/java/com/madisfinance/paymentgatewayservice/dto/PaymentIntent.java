package com.madisfinance.paymentgatewayservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentIntent implements Serializable {
    private Long custId;
    private Integer amount;
}
