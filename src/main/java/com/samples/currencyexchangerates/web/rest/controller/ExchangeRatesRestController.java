package com.samples.currencyexchangerates.web.rest.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Api("Api for providing exchange rates")
@RestController
@RequestMapping("/api")
public class ExchangeRatesRestController {

    private int defaultNoOfMonths;
    private final ExchangeRatesService restExchangeRatesServiceImpl;

    public ExchangeRatesRestController(@Value("${previousRates.defaultNoOfMonths:6}") int defaultNoOfMonths,
                                       ExchangeRatesService restExchangeRatesServiceImpl) {
        this.defaultNoOfMonths = defaultNoOfMonths;
        this.restExchangeRatesServiceImpl = restExchangeRatesServiceImpl;
    }

    @ApiOperation(value = "Provides exchange rates for USD,GBP,HKD against EUR for current date")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Exchange rate for today against EURO"),
            @ApiResponse(code = 404, message = "Exchange rate not found")
    })
    @GetMapping("/latest")
    public ResponseEntity<ExchangeRate> getLatestExchangeRates() {
        ExchangeRate exchangeRate = restExchangeRatesServiceImpl.getLatestExchangeRates();
        return ResponseEntity.ok(exchangeRate);
    }

    @ApiOperation(value = "Provides exchange rates for USD,GBP,HKD against EUR for previous six months for the same day as given day")
    @ApiParam(name = "date", value = "day for which exchange rates are returned for last 6 months")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of Exchange rates against EUR for last 6 months for the given day"),
            @ApiResponse(code = 404, message = "Exchange rate not found")
    })
    @GetMapping("/previous/{date}")
    public ResponseEntity<List<ExchangeRate>> getPreviousExchangeRates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ExchangeRate> previousExchangeRates = restExchangeRatesServiceImpl.getPreviousExchangeRates(date, defaultNoOfMonths);
        return ResponseEntity.ok(previousExchangeRates);
    }
}
