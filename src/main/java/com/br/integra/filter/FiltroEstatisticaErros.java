package com.br.integra.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroEstatisticaErros {
	private String modalidade;
	private String operadora;
	private String discador;
	private String equipamento;
	private String conta;
	private String unidadeAtendimento;
	private String numeroOrigem;
	private String campanha;
	private String descricaoErro;
	private String statusChamada;
	private String ddd;
	private String estado;
	private String regiao;
}