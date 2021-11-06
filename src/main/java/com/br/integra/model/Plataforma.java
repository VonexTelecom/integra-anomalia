package com.br.integra.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Plataforma", catalog = "Integra")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plataforma {

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "ativo")
	private Boolean ativo;
	
	@Column(name = "dataInicioProcessamento")
	private LocalDateTime dataInicioProcessamento;
}
