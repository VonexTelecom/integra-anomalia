package com.br.integra.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Numeros {
	private LocalDateTime data;
	private Long clienteId;
	private String numero;
	private String tipoEstatistica;
	private Double quantidade;
	private Long tipoEstatisticaValor;
	private String modalidade;
	private String operadora;
	private String unidadeAtendimento;
	private String discador;
	private String equipamento;
	private String conta;
	private String campanha;
	private String ddd;
	private String estado;
	private String regiao;
}
