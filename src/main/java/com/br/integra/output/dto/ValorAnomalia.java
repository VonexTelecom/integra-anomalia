package com.br.integra.output.dto;

import java.util.List;

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
	
	@JsonProperty("hora da anomalia")
	private String data;
	
	@JsonProperty("Valor da Anomalia")
	private double valor;
}
