package com.madisfinance.paymentgatewayservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madisfinance.paymentgatewayservice.entity.Hub2TransferTransactionResponse;

public interface Hub2TransferTransactionResponseRepository extends JpaRepository<Hub2TransferTransactionResponse,Long>{
        Optional<Hub2TransferTransactionResponse> findById(String string);

}
