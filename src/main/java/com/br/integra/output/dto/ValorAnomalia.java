package com.br.integra.output.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValorAnomalia {
	
	@JsonProperty("Valor da Anomalia")
	private BigDecimal valor;
	
	private BigDecimal porcentagem;
	
	private BigDecimal valorEsperado;
	
}
