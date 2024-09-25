package com.madisfinance.paymentgatewayservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferIntent  implements Serializable {
    Long custId;
    String senderName;
    Integer amount;
    String description;
    String recipientName;
    String msisdn;
    String provider;
}
