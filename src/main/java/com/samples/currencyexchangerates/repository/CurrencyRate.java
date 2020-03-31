package com.samples.currencyexchangerates.repository;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CurrencyRate {
    private String currency;
    private BigDecimal rate;

}
