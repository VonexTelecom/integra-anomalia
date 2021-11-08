package com.br.integra.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OutrosErros {
	
	private Long clienteId;  
	private Double quantidade; 	
	private LocalDateTime data;
	@Field("status_chamada")
	private String statusChamada;
	private String modalidade; 
	private String operadora;
	private String unidadeAtendimento; 
	private String discador;
	private String numeroOrigem; 
	private String equipamento;
	private String tipo;
	private String conta;
	private String campanha;
	private String ddd;
	private String estado;
	private String regiao;
	private String descricaoErro;
	
}