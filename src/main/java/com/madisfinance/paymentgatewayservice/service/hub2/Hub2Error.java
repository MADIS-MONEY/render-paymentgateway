package com.madisfinance.paymentgatewayservice.service.hub2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hub2Error {
    private int statusCode;
    private String message;
    private String error;
}
