package com.mustapha.exchange.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class DoubleJsonSerializer extends JsonSerializer<BigDecimal> {
	
	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.setScale(4, RoundingMode.CEILING));
	}
}
