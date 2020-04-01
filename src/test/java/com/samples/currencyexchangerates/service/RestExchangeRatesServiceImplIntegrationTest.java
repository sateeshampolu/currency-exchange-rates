package com.samples.currencyexchangerates.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestExchangeRatesServiceImplIntegrationTest {

    @Autowired
    private ExchangeRatesService restExchangeRatesServiceImpl;
    private ExchangeRate resultLatestRate;

    @Test
    void testGetLatestRate() {
        whenLatestRateIsRetrieved();
        thenVerifyCorrectResponseIsReturned();
    }

    private void whenLatestRateIsRetrieved() {
        resultLatestRate = restExchangeRatesServiceImpl.getLatestExchangeRates();
    }

    private void thenVerifyCorrectResponseIsReturned() {
        assertThat(resultLatestRate).isNotNull();
    }
}
