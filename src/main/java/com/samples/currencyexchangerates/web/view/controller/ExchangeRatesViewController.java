package com.samples.currencyexchangerates.web.view.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExchangeRatesViewController {

    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesViewController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @RequestMapping(value = "/latestRates")
    public String latestRates(Model model) {
        ExchangeRate latestExchangeRates = exchangeRatesService.getLatestExchangeRates(LocalDate.now());
        model.addAttribute("data", latestExchangeRates);
        return "latestRates";
    }

    @RequestMapping(value = "/previousRates")
    public String previousRates(Model model) {
        List<ExchangeRate> latestExchangeRates = exchangeRatesService.getPreviousExchangeRates(LocalDate.now());
        model.addAttribute("data", latestExchangeRates);
        return "previousRates";
    }
}
