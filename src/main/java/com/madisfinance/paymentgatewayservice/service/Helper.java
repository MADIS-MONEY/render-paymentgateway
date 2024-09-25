package com.madisfinance.paymentgatewayservice.service;

import java.util.UUID;

public class Helper {
    public static final String CUST = "CUST-";
    public static final String PAY = "PAY-";
     public static final String TRAN = "TRAN-";

    private Helper() {
        // nothing to do
    }

    public Helper getInstance() {
        return new Helper();
    }

    public static String usingRandomUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replace("-", "");
    }
}
