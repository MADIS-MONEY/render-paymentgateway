package com.madisfinance.paymentgatewayservice.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
public class Hub2PaymentIntentResponse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tranxId;
    private Integer amount;
    private String createdAt;
    private String currency;
    @Column(unique = true, nullable = false, updatable = false)
    private String customerReference;
    @Column(unique = true, nullable = false, updatable = false)
    private String id;
    private String lastPaymentFailure;
    private String merchantId;
    private String mode;
    @ElementCollection
    private List<Payment> payments;
    @Column(unique = true, nullable = false, updatable = false)
    private String purchaseReference;
    private String status;
    @Column(unique = true, nullable = false, updatable = false)
    private String token;
    private String updatedAt;
    private String overrideBusinessName;
    @Column(nullable = false, updatable = false)
    private Long custId;
}
