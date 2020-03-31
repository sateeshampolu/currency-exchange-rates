package com.samples.currencyexchangerates.service;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRatesService {
    ExchangeRate getLatestExchangeRates(LocalDate date);
    List<ExchangeRate> getPreviousExchangeRates(LocalDate date);
}
