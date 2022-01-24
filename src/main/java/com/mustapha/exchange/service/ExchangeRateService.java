package com.mustapha.exchange.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Set;

import com.mustapha.exchange.domain.AmountConversion;
import org.springframework.stereotype.Service;

import com.mustapha.exchange.domain.ExchangeRate;

@Service
public class ExchangeRateService {


	private CurrencyRatesRepository dataSource;
	private ExchangeRateHttpClient exchangeRateHttpClient;
	public ExchangeRateService(CurrencyRatesRepository dataSource, ExchangeRateHttpClient exchangeRateHttpClient) {
		this.dataSource = dataSource;
		this.exchangeRateHttpClient = exchangeRateHttpClient;
	}

	public Set<String> getSupportedCurrencies() {
		return dataSource.getCurrencies();
	}
	
	public BigDecimal getExchangeRate(String currency) {
		return dataSource.getCurrencyExchangeRate(currency.toUpperCase());
	}
	
	public ExchangeRate getExchangeRate(String baseCurrency, String currency) {
		BigDecimal rate = getExchangeRate(currency.toUpperCase());
		BigDecimal baseCurrencyRate = getExchangeRate(baseCurrency.toUpperCase());
		return new ExchangeRate(getPublishDate(), baseCurrency, currency, rate.divide(baseCurrencyRate, MathContext.DECIMAL64));
	}

	
	public AmountConversion convert(Double amount, String baseCurrency, String currency) {
		ExchangeRate rate = getExchangeRate(baseCurrency, currency);
		BigDecimal preciseAmount = new BigDecimal(amount);
		return new AmountConversion(getPublishDate(), baseCurrency, currency, preciseAmount, rate.getResult().multiply(preciseAmount));
	}
	
	public LocalDate getPublishDate () {
		return dataSource.getPublishedAt();
	}
}
