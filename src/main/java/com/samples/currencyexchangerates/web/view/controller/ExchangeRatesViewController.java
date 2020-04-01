package com.samples.currencyexchangerates.web.view.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExchangeRatesViewController {

    private int defaultNoOfMonths;
    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesViewController(@Value("${previousRates.defaultNoOfMonths:6}") int defaultNoOfMonths,
            ExchangeRatesService restExchangeRatesServiceImpl) {
        this.defaultNoOfMonths = defaultNoOfMonths;
        this.exchangeRatesService = restExchangeRatesServiceImpl;
    }

    @RequestMapping(value = "/latestRates")
    public String latestRates(Model model) {
        ExchangeRate latestExchangeRates = exchangeRatesService.getLatestExchangeRates();
        model.addAttribute("data", latestExchangeRates);
        return "latestRates";
    }

    @RequestMapping(value = "/previousRates")
    public String previousRates(Model model) {
        List<ExchangeRate> latestExchangeRates = exchangeRatesService.getPreviousExchangeRates(LocalDate.now(), defaultNoOfMonths);
        model.addAttribute("data", latestExchangeRates);
        return "previousRates";
    }
}
