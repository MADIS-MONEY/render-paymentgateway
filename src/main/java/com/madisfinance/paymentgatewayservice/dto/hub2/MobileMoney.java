package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MobileMoney implements Serializable {

    private static final long serialVersionUID = 2005122041950251207L;

    private String msisdn;
    private String otp;
    private String onSuccessRedirectionUrl;
    private String onFailedRedirectionUrl;
    private String onCancelRedirectionUrl;
    private String onFinishRedirectionUrl;
    private String workflow;

    public MobileMoney(String msisdn) {
        this.msisdn = msisdn;
    }

    public MobileMoney(String msisdn, String otp, String onCancelRedirectionUrl, String onFinishRedirectionUrl) {
        this.msisdn = msisdn;
        this.otp = otp;
        this.onCancelRedirectionUrl = onCancelRedirectionUrl;
        this.onFinishRedirectionUrl = onFinishRedirectionUrl;
    }

    public MobileMoney(String msisdn, String onSuccessRedirectionUrl, String onFailedRedirectionUrl) {
        this.msisdn = msisdn;
        this.onSuccessRedirectionUrl = onSuccessRedirectionUrl;
        this.onFailedRedirectionUrl = onFailedRedirectionUrl;
    }

}
