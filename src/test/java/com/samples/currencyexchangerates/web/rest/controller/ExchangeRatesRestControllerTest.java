package com.samples.currencyexchangerates.web.rest.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesRestControllerTest {
    @Mock
    private ExchangeRate exchangeRate;
    @Mock
    private ExchangeRatesService exchangeRatesService;

    private ExchangeRatesRestController exchangeRatesRestController;
    private ResponseEntity<ExchangeRate> resultExchangeRate;
    private ResponseEntity<List<ExchangeRate>> resultPreviousExchangeRates;

    @BeforeEach
    void setUp() {
        exchangeRatesRestController = new ExchangeRatesRestController(exchangeRatesService);
    }

    @Test
    void testGetLatestExchangeRates() {
        givenExchangeRatesForToday();
        whenGetLatestExchangeRates();
        thenVerifyExchangeRatesServiceIsCalled();
        andVerifyResponseIsReturned();
    }

    @Test
    void testGetPreviousExchangeRates() {
        givenPreviousExchangeRates();
        whenGetPreviousExchangeRates();
        thenVerifyExchangeRatesServiceIsCalledForPreviousExchangeRates();
        andVerifyResponseIsReturnedForPreviousExchangeRates();
    }

    private void givenExchangeRatesForToday() {
        doReturn(exchangeRate).when(exchangeRatesService).getLatestExchangeRates(any());
    }

    private void givenPreviousExchangeRates() {
        doReturn(Arrays.asList(exchangeRate)).when(exchangeRatesService).getPreviousExchangeRates(any());
    }

    private void whenGetLatestExchangeRates() {
        resultExchangeRate = exchangeRatesRestController.getLatestExchangeRates();
    }

    private void whenGetPreviousExchangeRates() {
        resultPreviousExchangeRates = exchangeRatesRestController.getPreviousExchangeRates(LocalDate.now());
    }

    private void thenVerifyExchangeRatesServiceIsCalled() {
        verify(exchangeRatesService).getLatestExchangeRates(LocalDate.now());
    }

    private void thenVerifyExchangeRatesServiceIsCalledForPreviousExchangeRates() {
        verify(exchangeRatesService).getPreviousExchangeRates(LocalDate.now());
    }

    private void andVerifyResponseIsReturned() {
        assertThat(resultExchangeRate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultExchangeRate.getBody()).isEqualTo(exchangeRate);
    }

    private void andVerifyResponseIsReturnedForPreviousExchangeRates() {
        assertThat(resultPreviousExchangeRates.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultPreviousExchangeRates.getBody()).isEqualTo(Arrays.asList(exchangeRate));
    }
}