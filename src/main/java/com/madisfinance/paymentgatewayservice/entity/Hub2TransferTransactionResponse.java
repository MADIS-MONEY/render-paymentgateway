package com.madisfinance.paymentgatewayservice.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.madisfinance.paymentgatewayservice.dto.hub2.Destination;
import com.madisfinance.paymentgatewayservice.dto.hub2.FailureCause;
import com.madisfinance.paymentgatewayservice.dto.hub2.Origin;
import com.madisfinance.paymentgatewayservice.dto.hub2.PaymentFee;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Hub2TransferTransactionResponse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tranxId;
    @Column(unique = true, nullable = false, updatable = false)
    private String id;
    private String merchantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String updatedAt;
    @Column(unique = true, nullable = false, updatable = false)
    private String reference;
    private String description;
    private String status;
    private double amount;
    private String currency;
    private String mode;
    private Destination destination;
    @Embedded
    private PaymentFee fee;
    @Transient
    private List<PaymentFee> fees;
    private FailureCause failureCause;
    private Origin origin;
    private String overrideBusinessName;
    private boolean isIrt;
    private String providerReference;
    private double balanceBefore;
    private double balanceAfter;
    @Transient
    private Object providerData;
    @Column(nullable = false, updatable = false)
    private Long custId;
}
