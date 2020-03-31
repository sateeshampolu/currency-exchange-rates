package com.samples.currencyexchangerates.web.rest.controller;


import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.samples.currencyexchangerates.TestMockDataUtils.getLatestExchangeRates;
import static com.samples.currencyexchangerates.TestMockDataUtils.getPreviousExchangeRates;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeRatesRestController.class)
class ExchangeRatesRestControllerIntegrationTest {
    private static final String LATEST_END_POINT = "/api/latest";
    private static final String PREVIOUS_END_POINT = "/api/previous";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRatesService exchangeRatesService;
    private LocalDate currentDate;

    @BeforeEach
    void setUp() {
        currentDate = LocalDate.now();
    }

    @WithMockUser("USER")
    @Test
    void testGetLatestExchangeRates() throws Exception {
        givenLatestExchangeRates();
        whenLatestExchangeRatesEndPointIsAccessed();
        thenVerifyCorrectResponseIsReturned();
    }

    @WithMockUser("USER")
    @Test
    void testGetPreviousExchangeRates() throws Exception {
        givenPreviousExchangeRates();
        whenPreviousExchangeRatesEndPointIsAccessed();
        thenVerifyCorrectResponseIsReturned();
    }

    private void givenLatestExchangeRates() {
        doReturn(getLatestExchangeRates()).when(exchangeRatesService).getLatestExchangeRates(currentDate);
    }

    private void givenPreviousExchangeRates() {
        doReturn(getPreviousExchangeRates()).when(exchangeRatesService).getPreviousExchangeRates(currentDate);
    }

    private void whenLatestExchangeRatesEndPointIsAccessed() throws Exception {
        mockMvc.perform(get(LATEST_END_POINT))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"base\":\"USD\",\"date\":\"2020-03-30\",\"rates\":{\"HKD\":0.12,\"GBP\":1.02,\"USD\":0.87}}"))
                .andDo(print());
    }

    private void whenPreviousExchangeRatesEndPointIsAccessed() throws Exception {
        mockMvc.perform(get(PREVIOUS_END_POINT+"/"+currentDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[{\"base\":\"USD\",\"date\":\"2020-02-29\",\"rates\":{\"HKD\":0.12,\"GBP\":1.02,\"USD\":0.87}},{\"base\":\"USD\",\"date\":\"2020-01-31\",\"rates\":{\"HKD\":0.12,\"GBP\":1.02,\"USD\":0.87}}]"))
                .andDo(print());
    }

    private void thenVerifyCorrectResponseIsReturned() {
        // Nothing to verify as it is done in the when with mockMvc. Just for readability
    }
}