package com.samples.currencyexchangerates.service;

import com.samples.currencyexchangerates.TestMockDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static com.samples.currencyexchangerates.TestMockDataUtils.getLatestExchangeRates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestExchangeRatesServiceImplTest {

    public static final String LATEST_RATES_URL = "http://test/lates?base=test@symbols=A,B,C";
    public static final String PREVIOUS_RATES_URL = "http://test/{date}?base=test@symbols=A,B,C";
    public static final int NO_OF_MONTHS = 2;

    @Mock
    RestTemplate restTemplate;

    private ExchangeRatesService restExchangeRatesService;
    private ExchangeRate expectedLatestExchangeRate;
    private ExchangeRate resultLatestExchangeRate;
    private List<ExchangeRate> expectedPreviousExchangeRates;
    private List<ExchangeRate> resultPreviousExchangeRates;
    private LocalDate inputDate;
    private ExchangeRate resultPreviousExchangeRate;

    @BeforeEach
    void setUp() {
        restExchangeRatesService = new RestExchangeRatesServiceImpl(LATEST_RATES_URL, PREVIOUS_RATES_URL, restTemplate);
    }

    @Test
    void testGetLatestExchangeRate() {
        givenLatestExchangeRatesCanBeReturnedByBackEndService();
        whenGetLatestExchangeRatesIsCalled();
        thenVerifyCorrectResponseIsReturnedForLatestRates();
        andVerifyBackEndServiceIsCalledCorrectlyForLatetsRates();
    }

    @Test
    void testGetPreviousExchangeRates() {
        givenPreviousExchangeRatesCanBeReturnedByBackEndService();
        whenGetPreviousExchangeRatesIsCalled();
        thenVerifyCorrectResponseIsReturnedForPreviousRates();
        andVerifyBackEndServiceIsCalledCorrectlyForPreviousRates();
    }

    @Test
    void testGetPreviousExchangeRate() {
        givenPreviousExchangeRateCanBeReturnedByBackEndService();
        whenGetPreviousExchangeRateIsCalled();
        thenVerifyCorrectResponseIsReturnedForPreviousRate();
        andVerifyBackEndServiceIsCalledCorrectlyForPreviousRate();
    }

    private void givenLatestExchangeRatesCanBeReturnedByBackEndService() {
        expectedLatestExchangeRate = getLatestExchangeRates();
        when(restTemplate.getForObject(LATEST_RATES_URL, ExchangeRate.class)).thenReturn(expectedLatestExchangeRate);
    }

    private void givenPreviousExchangeRatesCanBeReturnedByBackEndService() {
        expectedPreviousExchangeRates = TestMockDataUtils.getPreviousExchangeRates();
        when(restTemplate.getForObject(anyString(), any())).thenReturn(expectedPreviousExchangeRates.get(0)).thenReturn(expectedPreviousExchangeRates.get(1));
    }

    private void givenPreviousExchangeRateCanBeReturnedByBackEndService() {
        expectedLatestExchangeRate = getLatestExchangeRates();
        when(restTemplate.getForObject(anyString(), any())).thenReturn(expectedLatestExchangeRate);
    }

    private void whenGetLatestExchangeRatesIsCalled() {
        resultLatestExchangeRate = restExchangeRatesService.getLatestExchangeRates();
    }

    private void whenGetPreviousExchangeRatesIsCalled() {
        inputDate = LocalDate.now();
        resultPreviousExchangeRates = restExchangeRatesService.getPreviousExchangeRates(inputDate, NO_OF_MONTHS);
    }

    private void whenGetPreviousExchangeRateIsCalled() {
        inputDate = LocalDate.now();
        resultPreviousExchangeRate = restExchangeRatesService.getPreviousExchangeRate(inputDate.toString());
    }

    private void thenVerifyCorrectResponseIsReturnedForLatestRates() {
        assertThat(resultLatestExchangeRate).isEqualTo(expectedLatestExchangeRate);
    }

    private void thenVerifyCorrectResponseIsReturnedForPreviousRates() {
        assertThat(resultPreviousExchangeRates.size()).isEqualTo(2);
        assertThat(resultPreviousExchangeRates).containsAll(expectedPreviousExchangeRates);
    }

    private void thenVerifyCorrectResponseIsReturnedForPreviousRate() {
        assertThat(resultPreviousExchangeRate).isEqualTo(expectedLatestExchangeRate);
    }

    private void andVerifyBackEndServiceIsCalledCorrectlyForLatetsRates() {
        verify(restTemplate).getForObject(LATEST_RATES_URL, ExchangeRate.class);
    }

    private void andVerifyBackEndServiceIsCalledCorrectlyForPreviousRates() {
        String expectedUrl1 = PREVIOUS_RATES_URL.replace("{date}", inputDate.minusMonths(1).toString());
        String expectedUrl2 = PREVIOUS_RATES_URL.replace("{date}", inputDate.minusMonths(2).toString());
        verify(restTemplate).getForObject(expectedUrl1, ExchangeRate.class);
        verify(restTemplate).getForObject(expectedUrl2, ExchangeRate.class);
    }

    private void andVerifyBackEndServiceIsCalledCorrectlyForPreviousRate() {
        String expectedUrl = PREVIOUS_RATES_URL.replace("{date}", inputDate.toString());

        verify(restTemplate).getForObject(expectedUrl, ExchangeRate.class);
    }
}