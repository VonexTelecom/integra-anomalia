package com.br.integra.output.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
	private Integer clienteId;
	private LocalDateTime data;
	private BigDecimal porcentual;
	private BigDecimal quantidade;
	private BigDecimal valorEsperado;
	private String modalidade;
	private String operadora;
	private String discador;
	private String equipamento;
	private String conta;
	private String unidadeAtendimento;
	private String numeroOrigem;
	private String campanha;
	private String ddd;
	private String estado;
	private String regiao;
	
}
