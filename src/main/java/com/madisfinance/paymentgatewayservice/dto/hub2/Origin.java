package com.madisfinance.paymentgatewayservice.dto.hub2;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Origin implements Serializable{
    private String name; 
    private String country;
}
