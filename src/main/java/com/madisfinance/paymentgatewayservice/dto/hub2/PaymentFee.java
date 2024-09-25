package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentFee implements Serializable {
    @Transient
    private String currency;
    @Column(name="fee_id")
    private String id;
    private String label;
    private Integer rate;
    @Transient
    private Integer amount;
    private String rateType;
}
