package com.br.integra.output.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnomaliaOutputDtoErros {
	
	private Integer clienteId;
	private LocalDateTime data;
	private BigDecimal porcentual;
	private BigDecimal quantidade;
	private BigDecimal valorEsperado;
	private String tipoEstatistica;
	private String descricaoErro;
	private String statusChamada;
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
