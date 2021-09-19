package com.br.integra.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstatisticaDiscador {
	private Long clienteId;
	private LocalTime data;
	private String tipoEstatistica;
	private BigDecimal quantidade;
	private Long tipoEstatisticaValor;
}
