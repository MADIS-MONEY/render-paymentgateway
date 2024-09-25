package com.madisfinance.paymentgatewayservice.service.hub2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hub2Conflict {
    private String type;
    private String code;
    private String message;
}
