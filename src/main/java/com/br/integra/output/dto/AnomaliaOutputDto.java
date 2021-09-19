package com.br.integra.output.dto;

import java.math.BigDecimal;
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
public class AnomaliaOutputDto {
	
	@JsonProperty("Tipo da estatística")
	private String tipoEstatistica;
	
	private Integer quantidade;
	
	private List<ValorAnomalia> valor;
}
