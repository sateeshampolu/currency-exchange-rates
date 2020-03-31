package com.samples.currencyexchangerates.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ExchangeRate {
    @ApiModelProperty(notes = "Base currency for the exchange rate", example = "EUR")
    private String base;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(notes = "Date for which exchange rate is returned", example = "2020-03-31")
    private LocalDate date;
    @ApiModelProperty(notes = "Map of exchange rates with currency as key", example = "{\n" +
            "    \"GBP\": 1.03,\n" +
            "    \"USD\": 0.96,\n" +
            "    \"HKD\": 0.12\n" +
            "  }")
    private Map<String, BigDecimal> rates;
}
