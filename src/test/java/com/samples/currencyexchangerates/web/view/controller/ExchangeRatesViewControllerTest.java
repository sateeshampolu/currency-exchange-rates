package com.samples.currencyexchangerates.web.view.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesViewControllerTest {
    public static final int DEFAULT_NO_OF_MONTHS = 2;
    @Mock
    private ExchangeRate exchangeRate;
    @Mock
    private ExchangeRatesService exchangeRatesService;
    @Mock
    private Model model;

    private ExchangeRatesViewController exchangeRatesViewController;
    private String result;

    @BeforeEach
    void setUp() {
        exchangeRatesViewController = new ExchangeRatesViewController(DEFAULT_NO_OF_MONTHS, exchangeRatesService);
    }

    @Test
    void testGetLatestExchangeRates() {
        givenLatestExchangeRate();
        whenLatestExchangeRatesAreRetrieved();
        thenVerifyLatestRatesViewIsReturned();
        andVerifyExchangeRateServiceIsCalledForLatestRate();
        andVerifyTheModelIsUpdatedWithCorrectDataForLatestRate();
    }

    @Test
    void testGetPreviousExchangeRates() {
        givenPreviousExchangeRate();
        whenPreviousExchangeRatesAreRetrieved();
        thenVerifyPreviousRatesViewIsReturned();
        andVerifyExchangeRateServiceIsCalledForPreviousRates();
        andVerifyTheModelIsUpdatedWithCorrectDataForPreviousRates();
    }

    private void givenLatestExchangeRate() {
        when(exchangeRatesService.getLatestExchangeRates()).thenReturn(exchangeRate);
    }

    private void givenPreviousExchangeRate() {
        when(exchangeRatesService.getPreviousExchangeRates(LocalDate.now(), DEFAULT_NO_OF_MONTHS)).thenReturn(asList(exchangeRate));
    }

    private void whenLatestExchangeRatesAreRetrieved() {
        result = exchangeRatesViewController.latestRates(model);
    }

    private void whenPreviousExchangeRatesAreRetrieved() {
        result = exchangeRatesViewController.previousRates(model);
    }

    private void thenVerifyLatestRatesViewIsReturned() {
        assertThat(result).isEqualTo("latestRates");
    }

    private void thenVerifyPreviousRatesViewIsReturned() {
        assertThat(result).isEqualTo("previousRates");
    }

    private void andVerifyExchangeRateServiceIsCalledForLatestRate() {
        verify(exchangeRatesService).getLatestExchangeRates();
    }

    private void andVerifyExchangeRateServiceIsCalledForPreviousRates() {
        verify(exchangeRatesService).getPreviousExchangeRates(LocalDate.now(), DEFAULT_NO_OF_MONTHS);
    }

    private void andVerifyTheModelIsUpdatedWithCorrectDataForLatestRate() {
        verify(model).addAttribute("data", exchangeRate);
    }

    private void andVerifyTheModelIsUpdatedWithCorrectDataForPreviousRates() {
        verify(model).addAttribute("data", asList(exchangeRate));
    }

}