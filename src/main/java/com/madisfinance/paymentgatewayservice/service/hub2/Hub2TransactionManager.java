package com.madisfinance.paymentgatewayservice.service.hub2;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.madisfinance.paymentgatewayservice.dto.PaymentIntent;
import com.madisfinance.paymentgatewayservice.dto.TransferIntent;
import com.madisfinance.paymentgatewayservice.dto.hub2.CashinResponse;
import com.madisfinance.paymentgatewayservice.dto.hub2.Destination;
import com.madisfinance.paymentgatewayservice.dto.hub2.Error;
import com.madisfinance.paymentgatewayservice.dto.hub2.Hub2PaymentIntentAttemptRequest;
import com.madisfinance.paymentgatewayservice.dto.hub2.Hub2PaymentIntentRequest;
import com.madisfinance.paymentgatewayservice.dto.hub2.Hub2TransferTransactionRequest;
import com.madisfinance.paymentgatewayservice.dto.hub2.Origin;
import com.madisfinance.paymentgatewayservice.entity.Hub2PaymentIntentResponse;
import com.madisfinance.paymentgatewayservice.entity.Hub2TransferTransactionResponse;
import com.madisfinance.paymentgatewayservice.entity.Payment;
import com.madisfinance.paymentgatewayservice.repository.Hub2PaymentIntentResponseRepository;
import com.madisfinance.paymentgatewayservice.repository.Hub2TransferTransactionResponseRepository;
import com.madisfinance.paymentgatewayservice.service.Helper;

import jakarta.transaction.Transactional;

@Service
public class Hub2TransactionManager {
    private static final String SUCCESSFUL = "successful";

    private static final String SERVER_ERROR = "SERVER ERROR";

    private static final String INVALID_CUSTOMER_REQUEST = "INVALID CUSTOMER REQUEST";

    private static final String FAILED = "failed";

    private static final String SUCCEED = "succeed";

    private static final String CREATING_PAYMENT_INTENT = "Creating payment Intent {} : {} ";

    private static final String ATTEMPTING_PAYMENT_INTENT = "Attempting payment Intent {} : {} ";

    private static final String SOMETHING_WENT_WRONG = "Something went wrong : %s";

    private static final Logger logger = LoggerFactory.getLogger(Hub2TransactionManager.class);

    private final Hub2PaymentIntentResponseRepository intentResponseRepository;

    private final Hub2TransferTransactionResponseRepository transferResponseRepository;

    @Value("${header.hub2.apikey}")
    private String apiKey;
    @Value("${header.hub2.merchantid}")
    private String merchantId;
    @Value("${header.hub2.environment}")
    private String environment;
    @Value("${header.hub2.transfer.create}")
    private String createTransfer;
    @Value("${header.hub2.transfer.retrieve}")
    private String retrieveTransfer;
    @Value("${header.hub2.transfer.status}")
    private String checkTransfer;
    @Value("${header.hub2.payment.intent}")
    private String intent;
    @Value("${header.hub2.payment.attempt}")
    private String attempt;
    @Value("${header.hub2.payment.retrieve}")
    private String retrievePayment;
    private final RestTemplate restTemplate;
    @Value("${header.hub2.currency}")
    private String currency;
    @Value("${header.hub2.businessname}")
    private String businessName;

    public Hub2TransactionManager(Hub2PaymentIntentResponseRepository intentResponseRepository,
            Hub2TransferTransactionResponseRepository transferResponseRepository,
            RestTemplate restTemplate) {
        this.intentResponseRepository = intentResponseRepository;
        this.transferResponseRepository = transferResponseRepository;
        this.restTemplate = restTemplate;
    }

    public CashinResponse createPaymentIntents(PaymentIntent paymentIntent) {
        try {
            Hub2PaymentIntentRequest paymentIntentRequest = buildPaymentIntentRequest(paymentIntent.getAmount());
            HttpEntity<Hub2PaymentIntentRequest> request = new HttpEntity<>(paymentIntentRequest, getHeaders());
            ResponseEntity<Hub2PaymentIntentResponse> response = this.restTemplate.postForEntity(this.intent, request,
                    Hub2PaymentIntentResponse.class);

            if (response.getStatusCode().is2xxSuccessful()
                    && (Objects.nonNull(response.getBody()))) {
                Hub2PaymentIntentResponse hub2Response = response.getBody();
                if (Objects.nonNull(hub2Response)) {
                    hub2Response.setCustId(paymentIntent.getCustId());
                    logger.info(CREATING_PAYMENT_INTENT, SUCCEED, hub2Response.getUpdatedAt());
                    hub2Response = this.intentResponseRepository.save(hub2Response);
                    return new CashinResponse(null, hub2Response.getId(), hub2Response.getAmount().toString(), null,
                            201);
                }
            }
            logger.info(CREATING_PAYMENT_INTENT, FAILED, response);
            return new CashinResponse(new Error(response.getStatusCode().value(), new ArrayList<>(), "FAILED"), null,
                    null,
                    null, response.getStatusCode().value());
        } catch (RestClientException e) {
            String error = e.getMessage().substring(e.getMessage().indexOf("{\""), e.getMessage().indexOf("\"}") + 2);
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, error);

            List<String> list = getList(error);
            return new CashinResponse(new Error(Integer.valueOf(e.getMessage().substring(0, 3)), list, error),
                    null, null, null, Integer.valueOf(e.getMessage().substring(0, 3)));
        }
    }

    public CashinResponse attemptPaymentIntents(Long custId, Hub2PaymentIntentAttemptRequest paymentIntentAttemptRequest) {

        try {
            Optional<Hub2PaymentIntentResponse> payment = this.intentResponseRepository.findById(paymentIntentAttemptRequest.getId());
            if (Objects.nonNull(payment) && payment.isEmpty()) {
                return new CashinResponse(new Error(2004, new ArrayList<>(), "NO CONTENT"), null, null, null, 2004);
            }

            if (Objects.nonNull(payment) && payment.get().getCustId().equals(custId)) {
                HttpEntity<Hub2PaymentIntentAttemptRequest> request = new HttpEntity<>(paymentIntentAttemptRequest,
                        getHeaders());
                paymentIntentAttemptRequest.setToken(payment.get().getToken());
                ResponseEntity<Hub2PaymentIntentResponse> hub2PaymentIntentResponse = this.restTemplate.postForEntity(
                        new URI(String.format(this.attempt, paymentIntentAttemptRequest.getId())),
                        request, Hub2PaymentIntentResponse.class);
                if (hub2PaymentIntentResponse.getStatusCode().is2xxSuccessful()
                        && (Objects.nonNull(hub2PaymentIntentResponse.getBody()))) {
                    logger.info(ATTEMPTING_PAYMENT_INTENT, SUCCEED, hub2PaymentIntentResponse.getStatusCode());
                    Hub2PaymentIntentResponse hub2Response = hub2PaymentIntentResponse.getBody();
                    logger.info("BEFORE SAVE {}", payment);
                    if (Objects.nonNull(hub2Response)) {
                        logger.info("FROM HUB {}", hub2Response);
                        hub2Response.setTranxId(payment.get().getTranxId());
                        Payment payments = hub2Response.getPayments().get(0);
                        payments.setFee(payments.getFees().get(0)); 
                        
                        // Check status
                        final String id = hub2Response.getId();

                        try {
                            Thread.sleep(5L * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders());
                        ResponseEntity<Hub2PaymentIntentResponse[]> hub2PaymentIntentResponse2 = this.restTemplate
                                .exchange("https://api.hub2.io/payment-intents", HttpMethod.GET, requestEntity,
                                        Hub2PaymentIntentResponse[].class);
                        Hub2PaymentIntentResponse[] resp2 = hub2PaymentIntentResponse2.getBody();
                        Optional<Hub2PaymentIntentResponse> optResp = java.util.Arrays.asList(resp2).stream()
                                .filter(r -> r.getId().equals(id)).findAny();
                        if (optResp.isPresent()) {
                            Hub2PaymentIntentResponse hub2Response2 = optResp.get();
                            hub2Response2.setTranxId(payment.get().getTranxId());
                            boolean hasSucceed = hub2Response2.getStatus().equals(SUCCESSFUL);
                            Payment payment2 = hub2Response2.getPayments().get(0);
                            payment2.setFee(payment2.getFees().get(0));
                            hub2Response2 = this.intentResponseRepository.save(hub2Response2);
                            logger.info("AFTER GET UPDATE {}", hub2Response2);

                            return new CashinResponse(null, hub2Response2.getId(), hub2Response2.getAmount().toString(),
                                    payments.getFee().getAmount().toString(), hasSucceed ? 201 : 200);
                        }
                        hub2Response = this.intentResponseRepository.save(hub2Response); 
                        logger.info("AFTER SAVE {}", hub2Response);
                        return new CashinResponse(null, hub2Response.getId(), hub2Response.getAmount().toString(),
                                payments.getFee().getAmount().toString(),
                                hub2Response.getStatus().equals(SUCCESSFUL) ? 201 : 200);
                    }
                }
                logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, hub2PaymentIntentResponse);
            }
            return new CashinResponse(new Error(4003, new ArrayList<>(), INVALID_CUSTOMER_REQUEST), null, null, null,
                    4003);
        } catch (RestClientException | URISyntaxException e) {
            String error = e.getMessage().substring(e.getMessage().indexOf("{\""), e.getMessage().indexOf("\"}") + 2);
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, error);

            List<String> list = getList(error);
            return new CashinResponse(new Error(Integer.valueOf(e.getMessage().substring(0, 3)), list, error),
                    null, null, null, Integer.valueOf(e.getMessage().substring(0, 3)));
        }

    }

    @Transactional
    public CashinResponse createTransfer(TransferIntent transferIntent) {
        String reference = "";
        try {
            Hub2TransferTransactionRequest request = this.buildPaymentIntentRequest(transferIntent.getSenderName(),
                    transferIntent.getAmount(), transferIntent.getDescription(), transferIntent.getRecipientName(),
                    transferIntent.getMsisdn(), transferIntent.getProvider());
            reference = request.getReference();
            logger.info(ATTEMPTING_PAYMENT_INTENT, "LOGGED ::", request);
            HttpEntity<Hub2TransferTransactionRequest> requestEntity = new HttpEntity<>(request, getHeaders());
            ResponseEntity<Hub2TransferTransactionResponse> response = this.restTemplate
                    .postForEntity(new URI(this.createTransfer), requestEntity, Hub2TransferTransactionResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && (Objects.nonNull(response.getBody()))) {
                Hub2TransferTransactionResponse hub2Response = response.getBody();
                if (Objects.nonNull(hub2Response)) {
                    hub2Response.setCustId(transferIntent.getCustId());
                    hub2Response.setFee(hub2Response.getFees().get(0));
                    logger.info(CREATING_PAYMENT_INTENT, SUCCEED, hub2Response.getStatus());
                    hub2Response = this.transferResponseRepository.save(hub2Response);
                    CashinResponse reponse = this.checkTransferStatus(hub2Response); // See also the simple call to get
                                                                                     // the status
                    return reponse.getError() == null ? reponse
                            : new CashinResponse(null, hub2Response.getId(),
                                    String.valueOf(hub2Response.getAmount()),
                                    null, 200);
                }
            }
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, response);
            return new CashinResponse(new Error(4003, new ArrayList<>(), INVALID_CUSTOMER_REQUEST), reference,
                    "" + transferIntent.getAmount(), BigInteger.ZERO.toString(),
                    4003);
        } catch (RestClientException | URISyntaxException e) {
            String error = e.getMessage().substring(e.getMessage().indexOf("{\""), e.getMessage().indexOf("\"}") + 2);
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, error);

            List<String> list = getList(error);
            return new CashinResponse(new Error(Integer.valueOf(e.getMessage().substring(0, 3)), list, error),
                    null, null, null, Integer.valueOf(e.getMessage().substring(0, 3)));
        }
    }

    private List<String> getList(String error) {

        Hub2Conflict conflict = getHub2Conflict(error);
        List<String> list = new ArrayList<>();
        list.add(conflict.getCode());
        list.add(conflict.getType());
        list.add(conflict.getMessage());
        return list;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> checkStatus(Set<String> ids) {
        HttpEntity<Set<String>> headers = new HttpEntity<>(ids, getHeaders());
        ResponseEntity<Object> response = this.restTemplate.exchange(this.checkTransfer, HttpMethod.POST, headers,
                Object.class);
        return response.getBody() != null ? (Map<String, String>) response.getBody() : new HashMap<>();
    }

    public Map<String, String> retrievePayment(String id) {
        Map<String, String> retour = new HashMap<>();
        try {
            HttpEntity<String> headers = new HttpEntity<>(getHeaders());
            ResponseEntity<Hub2PaymentIntentResponse> response = this.restTemplate.exchange(
                    String.format(this.retrievePayment, id),
                    HttpMethod.GET, headers,
                    Hub2PaymentIntentResponse.class);
            Hub2PaymentIntentResponse responseBody = response.getBody();
            if (responseBody == null) {
                return new HashMap<>();
            }
            retour.put(id, responseBody.getStatus());
            return retour;
        } catch (RestClientException e) {
            String error = e.getMessage().substring(e.getMessage().indexOf("{\""), e.getMessage().indexOf("\"}") + 2);
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, error);
            return getHub2Error(retour, error);
        }
    }

    private Map<String, String> getHub2Error(Map<String, String> retour, String error) {
        Gson gson = new Gson();
        Hub2Error hub2Error = gson.fromJson(error, Hub2Error.class);
        retour.put("statusCode", "" + hub2Error.getStatusCode());
        retour.put("message", hub2Error.getMessage());
        retour.put("error", "" + hub2Error.getError());
        return retour;
    }

    private Hub2Conflict getHub2Conflict(String error) {
        Gson gson = new Gson();
        return gson.fromJson(error, Hub2Conflict.class);
    }

    public List<String> retrievePaymentByStatus(String status) {
        return this.intentResponseRepository.findAll().stream().filter(i -> i.getStatus().equals(status))
                .map(i -> i.getId()).collect(Collectors.toList());
    }

    public List<String> retrieveTransferByStatus(String status) {
        return this.transferResponseRepository.findAll().stream().filter(i -> i.getStatus().equals(status))
                .map(i -> i.getId()).collect(Collectors.toList());
    }

    public boolean updateTransferStatus(String id, String status) {
        Optional<Hub2TransferTransactionResponse> tranxOptional = this.transferResponseRepository.findById(id);
        return changeStatus(id, status, tranxOptional, null);
    }

    private boolean changeStatus(String id, String status, Optional<Hub2TransferTransactionResponse> tranxOptional,
            Optional<Hub2PaymentIntentResponse> tranxOptional2) {
        if (Objects.nonNull(tranxOptional) && tranxOptional.isPresent()) {
            Hub2TransferTransactionResponse tranx = tranxOptional.get();
            tranx.setStatus(status);
            tranx = this.transferResponseRepository.saveAndFlush(tranx);
            logger.info(CREATING_PAYMENT_INTENT, status, tranx.getStatus());
            return true;
        } else if (Objects.nonNull(tranxOptional2) && tranxOptional2.isPresent()) {
            Hub2PaymentIntentResponse tranx = tranxOptional2.get();
            tranx.setStatus(status);
            tranx.getPayments().get(0).setStatus(status);
            tranx = this.intentResponseRepository.saveAndFlush(tranx);
            logger.info(CREATING_PAYMENT_INTENT, status, tranx.getStatus());
            return true;
        } else {
            logger.info(CREATING_PAYMENT_INTENT, FAILED, id);
            return false;
        }

    }

    public boolean updatePaymentStatus(String id, String status) {
        Optional<Hub2PaymentIntentResponse> tranxOptional = this.intentResponseRepository.findById(id);
        return changeStatus(id, status, null, tranxOptional);

    }

    private CashinResponse checkTransferStatus(Hub2TransferTransactionResponse hub2Response) {
        try {
            HttpEntity<Void> headers = new HttpEntity<>(getHeaders());
            ResponseEntity<Hub2TransferTransactionResponse> response = this.restTemplate.exchange(
                    String.format(this.retrieveTransfer, hub2Response.getId()), HttpMethod.GET, headers,
                    Hub2TransferTransactionResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && (Objects.nonNull(response.getBody()))) {
                Hub2TransferTransactionResponse hub2Response2 = response.getBody();
                if (Objects.nonNull(hub2Response2)) {
                    logger.info(CREATING_PAYMENT_INTENT, SUCCEED, hub2Response2.getStatus());
                    hub2Response.setStatus(hub2Response2.getStatus());
                    hub2Response = this.transferResponseRepository.save(hub2Response);

                    return new CashinResponse(null, hub2Response.getId(),
                            String.valueOf(hub2Response.getAmount()),
                            null, hub2Response.getStatus().equals(SUCCESSFUL) ? 201 : 200);
                }
            }
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, response);
            return new CashinResponse(new Error(4003, new ArrayList<>(), INVALID_CUSTOMER_REQUEST), null, null, null,
                    4003);
        } catch (RestClientException e) {
            logger.info(ATTEMPTING_PAYMENT_INTENT, FAILED, e.getMessage());
            return new CashinResponse(
                    new Error(Integer.valueOf(e.getMessage().substring(0, 3)), new ArrayList<>(), e.getMessage()), null,
                    null, null, Integer.valueOf(e.getMessage().substring(0, 3)));
        }
    }

    private Hub2TransferTransactionRequest buildPaymentIntentRequest(String senderName, Integer amount,
            String description, String recipientName, String msisdn, String provider) {
        String reference = Helper.usingRandomUUID();
        return new Hub2TransferTransactionRequest(Helper.TRAN.concat(reference),
                amount,
                this.currency,
                description,
                new Origin(senderName, "CI"),
                new Destination("mobile_money", "CI", recipientName,msisdn, msisdn, provider),
                this.businessName);
    }

    private Hub2PaymentIntentRequest buildPaymentIntentRequest(Integer amount) {
        String reference = Helper.usingRandomUUID();
        return new Hub2PaymentIntentRequest(Helper.CUST.concat(reference), Helper.PAY.concat(reference),
                amount, this.currency, this.businessName);
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("ApiKey", apiKey);
        headers.add("MerchantId", merchantId);
        headers.add("Environment", environment);
        return headers;
    }

    public Map<String, String> getHeadersAsMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("ApiKey", apiKey);
        headers.put("MerchantId", merchantId);
        headers.put("Environment", environment);
        return headers;
    }

}
