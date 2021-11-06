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
@Entity
@Table(name = "ProcessamentoAnomaliaMongoDB", catalog="IntegraBI")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessamentoEstatisticas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long processoID;
	private LocalDateTime minuto;
	private String statusTarifacao;
	private LocalDateTime dataInicioTarifacao;
	private LocalDateTime dataFimTarifacao;
	private String statusProcessamento;
	private LocalDateTime dataInicioProcessamento;
	private LocalDateTime dataFimProcessamento;
	private String statusSumarizacao;
	private LocalDateTime dataInicioSumarizacao;
	private LocalDateTime dataFimSumarizacao;
}
