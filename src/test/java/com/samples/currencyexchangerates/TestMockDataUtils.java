package com.samples.currencyexchangerates;

import com.samples.currencyexchangerates.service.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMockDataUtils {
    private static final String USD = "USD";
    private static final String HKD = "HKD";
    private static final String GBP = "GBP";
    private static final BigDecimal USD_RATE = BigDecimal.valueOf(0.87);
    private static final BigDecimal GBP_RATE = BigDecimal.valueOf(1.02);
    private static final BigDecimal HKD_RATE = BigDecimal.valueOf(0.12);

    public static ExchangeRate getLatestExchangeRates() {
        Map<String, BigDecimal> rates = new HashMap<String, BigDecimal>(){{
            put(GBP, GBP_RATE);
            put(USD, USD_RATE);
            put(HKD, HKD_RATE);
        }};

        return ExchangeRate.builder()
                .base(USD)
                .rates(rates)
                .date( LocalDate.of(2020, 3, 30))
                .build();
    }

    public static List<ExchangeRate> getPreviousExchangeRates() {
        Map<String, BigDecimal> rates = new HashMap<String, BigDecimal>(){{
            put(GBP, GBP_RATE);
            put(USD, USD_RATE);
            put(HKD, HKD_RATE);
        }};

        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .base(USD)
                .rates(rates)
                .date( LocalDate.of(2020, 2, 29))
                .build();
        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .base(USD)
                .rates(rates)
                .date( LocalDate.of(2020, 1, 31))
                .build();
        return Arrays.asList(exchangeRate1, exchangeRate2);
    }
}
