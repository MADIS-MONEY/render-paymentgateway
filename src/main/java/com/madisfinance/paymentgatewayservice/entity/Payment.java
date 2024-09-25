package com.madisfinance.paymentgatewayservice.entity;

import java.io.Serializable;
import java.util.List;

import com.madisfinance.paymentgatewayservice.dto.hub2.FailureCause;
import com.madisfinance.paymentgatewayservice.dto.hub2.PaymentFee;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
public class Payment implements Serializable {
    private String id;
    private String intentId;
    private String createdAt;
    private String updatedAt;
    private String status;
    @Transient
    private String amount;
    @Transient
    private String currency;
    private String method;
    @Transient
    private String country;
    @Embedded
    private FailureCause failure;
    @Embedded
    private PaymentFee fee;  
    @Transient
    private List<PaymentFee> fees;
}
