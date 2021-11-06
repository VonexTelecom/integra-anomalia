package com.br.integra.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstatisticaDiscador {
	private LocalDateTime data;
	private String tipoEstatistica;
	private Double quantidade;
	private String numeroOrigem;
	private String equipamento;
	private String conta;
	private Integer clienteId;
	private String modalidade;
	private String tipoEstatisticaValor;
	private String operadora;
	private String unidadeAtendimento;
	private String discador;
	private String campanha;
	private String ddd;
	private String estado;
	private String regiao;
}
