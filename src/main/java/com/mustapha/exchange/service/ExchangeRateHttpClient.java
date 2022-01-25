package com.mustapha.exchange.service;

import com.mustapha.exchange.rest.dto.exchangeRateAPI.ExchangeRatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExchangeRateHttpClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateHttpClient.class);

	@Value("${exchange.api.base}")
	private String baseURL;

	@Value("${exchange.api.latest}")
	private String latest;


	@Value("${exchange.api.key}")
	private String accessKey;

	private RestTemplate restTemplate;
	private CurrencyRatesRepository currencyRatesRepository;

	public ExchangeRateHttpClient(RestTemplate restTemplate, CurrencyRatesRepository currencyRatesRepository) {
		this.restTemplate = restTemplate;
		this.currencyRatesRepository = currencyRatesRepository;
	}

	@Scheduled(cron = "@hourly")
	public void exchangeRates() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		Map<String, String> vars = new HashMap<>();
		vars.put("accessKey",accessKey);
		vars.put("from","EUR");
		try{
			ExchangeRatesResponse rateConversionResponse = restTemplate.getForObject(baseURL + latest + "?access_key={accessKey}&base={from}", ExchangeRatesResponse.class, vars);
			if(rateConversionResponse.isSuccess()) {
				LOGGER.info("Fetching supported currency symbols was successful");
				currencyRatesRepository.updateCurrencyRates(rateConversionResponse.getRates(),rateConversionResponse.getDate());
			}
		} catch (Exception e) {
			LOGGER.error("An exception occured while getting Supported currencies , message : {}", e.getLocalizedMessage());
		}

	}


}
