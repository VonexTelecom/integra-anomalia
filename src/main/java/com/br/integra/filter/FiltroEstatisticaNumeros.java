package com.br.integra.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroEstatisticaNumeros {
	private String modalidade;
	private String operadora;
	private String discador;
	private String equipamento;
	private String conta;
	private String unidadeAtendimento;
	private String numero;
	private String campanha;
	private String ddd;
	private String estado;
	private String regiao;
}