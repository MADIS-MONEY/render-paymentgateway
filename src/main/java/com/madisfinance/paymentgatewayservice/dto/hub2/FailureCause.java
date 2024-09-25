package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Transient;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FailureCause implements Serializable{
private String code;
private String message;
@Transient
private List<String> params;
}
