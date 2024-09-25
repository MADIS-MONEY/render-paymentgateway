package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private Integer statusCode;
    private List<String> message;
    private String error;
}
