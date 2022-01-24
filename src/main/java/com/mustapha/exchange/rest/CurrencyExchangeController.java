package com.mustapha.exchange.rest;

import com.mustapha.exchange.domain.AmountConversion;
import com.mustapha.exchange.rest.dto.ApiError;
import com.mustapha.exchange.service.CurrencyRatesRepository;
import com.mustapha.exchange.service.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "currency")
public class CurrencyExchangeController {
	
	private final ExchangeRateService exchangeRateService;

	public CurrencyExchangeController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSupportedCurrencies () {
		return new ResponseEntity<>(exchangeRateService.getSupportedCurrencies(), HttpStatus.OK);
	}

	
	@GetMapping(path = "/convert")
	public ResponseEntity<?> convert (
			@RequestParam final double amount,
			@RequestParam final String from,
			@RequestParam(defaultValue = CurrencyRatesRepository.EURO_CURRENCY_SYMBOL) final String to) {
		
		AmountConversion amountConversion = exchangeRateService.convert(amount, from, to);
		return new ResponseEntity<>(
				amountConversion,
				HttpStatus.OK);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	protected ApiError handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.name(), ex.getMessage());
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		return new ApiError(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
	}
	
	/**
	 * Binds a property editor, that transforms string parameters (in our case currency symbols) to upper case
	 * This applies to this Rest Controller only
	 * @param dataBinder used for end-points in this controller
	 */
	@InitBinder
	public void initBinder( WebDataBinder dataBinder) {
	    dataBinder.registerCustomEditor( String.class, new UpperCaseEditor());
	}
}
