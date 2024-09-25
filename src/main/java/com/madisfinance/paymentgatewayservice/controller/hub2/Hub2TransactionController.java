package com.madisfinance.paymentgatewayservice.controller.hub2;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madisfinance.paymentgatewayservice.dto.PaymentIntent;
import com.madisfinance.paymentgatewayservice.dto.TransferIntent;
import com.madisfinance.paymentgatewayservice.dto.hub2.CashinResponse;
import com.madisfinance.paymentgatewayservice.dto.hub2.Hub2PaymentIntentAttemptRequest;
import com.madisfinance.paymentgatewayservice.service.hub2.Hub2TransactionManager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@RestController
@RequestMapping("/hub2")
@OpenAPIDefinition(info = @Info(title = "Madis Transaction Operation & Proxy Hub2 API", version = "1.0", description = "Madis Wallet : Cashin/Cashout"))
public class Hub2TransactionController {
    private final Hub2TransactionManager hub2TransactionManager;

    public Hub2TransactionController(Hub2TransactionManager hub2TransactionManager) {
        this.hub2TransactionManager = hub2TransactionManager;
    }

    @PostMapping("/cashin-intents")
    public CashinResponse createPaymentIntent(@RequestBody PaymentIntent paymentIntentRequest) {
        return hub2TransactionManager.createPaymentIntents(paymentIntentRequest);
    }

    @PostMapping("/cashin-attempts/{id}")
    public CashinResponse createPaymentIntent(@PathVariable("id") Long id,
            @RequestBody Hub2PaymentIntentAttemptRequest paymentIntentAttemptRequest) {
        return hub2TransactionManager.attemptPaymentIntents(id, paymentIntentAttemptRequest);
    }

    @PutMapping("/cashin-attempts/{transactionId}/{status}")
    public Boolean createPaymentIntent(@PathVariable("transactionId") String transactionId,
            @PathVariable("status") String status) {
        return hub2TransactionManager.updatePaymentStatus(transactionId, status);
    }

    @GetMapping("/cashin-attempts/{id}")
    public  Map<String,String> getPaymentIntent(@PathVariable("id") String id) {
        return hub2TransactionManager.retrievePayment(id);
    }

    @GetMapping("/cashin-attempts/all/{status}")
    public List<String> getPaymentIntentByStatus(@PathVariable("status") String status) {
        return hub2TransactionManager.retrievePaymentByStatus(status);
    }

    @PostMapping("/cashout-transfer")
    public CashinResponse createTransfer(@RequestBody TransferIntent transferIntent) {
        return hub2TransactionManager.createTransfer(transferIntent);
    }

    @PostMapping("/cashout-transfer/check")
    public Map<String,String> checkStatus(@RequestBody Set<String> ids) {
        return hub2TransactionManager.checkStatus(ids);
    }

    @PutMapping("/cashout-transfer/{transactionId}/{status}")
    public Boolean updateTransfer(@PathVariable("transactionId") String transactionId,
            @PathVariable("status") String status) {
        return hub2TransactionManager.updateTransferStatus(transactionId, status);
    }

    @GetMapping("/cashout-transfer/all/{status}")
    public List<String> getTransferIntentByStatus(@PathVariable("status") String status) {
        return hub2TransactionManager.retrieveTransferByStatus(status);
    }
}
