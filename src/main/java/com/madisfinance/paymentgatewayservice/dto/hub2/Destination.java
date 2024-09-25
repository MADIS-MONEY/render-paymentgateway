package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination implements Serializable {
    private String type;
    @Column(name = "dest_country")
    private String country;
    private String recipientName;
    private String number;
    private String msisdn;
    private String provider;
}
