package com.madisfinance.paymentgatewayservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madisfinance.paymentgatewayservice.entity.Hub2PaymentIntentResponse;

public interface Hub2PaymentIntentResponseRepository extends JpaRepository<Hub2PaymentIntentResponse, Long> {
    Optional<Hub2PaymentIntentResponse> findById(String string);

}
