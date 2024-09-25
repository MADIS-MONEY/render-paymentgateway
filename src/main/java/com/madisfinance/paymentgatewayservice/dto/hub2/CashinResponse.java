package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashinResponse implements Serializable {  
    private Error error;
    private String token;
    private String amount;
    private String fees;
    private Integer status;
}
