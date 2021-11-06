package com.br.integra.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="LeituraEstatistica", catalog="IntegraBI")
public class LeituraEstatistica {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long processoID;
		private Long plataformaID;
		private LocalDateTime dataInicio; 
		private LocalDateTime dataFim;
		private LocalDateTime dataLeitura; 
		private String status; 
		private LocalDateTime dataHoraFinalizacao;
		private Integer quantidadeDeRegistrosGravados;
		private Integer quantidadeDeRegistrosLidos; 
		private Boolean processado;
		private Boolean processando;
}

