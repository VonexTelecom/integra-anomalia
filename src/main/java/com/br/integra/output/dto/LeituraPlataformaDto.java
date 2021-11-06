package com.br.integra.output.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LeituraPlataformaDto {

	private String plataformaNome;
	private LocalDateTime minuto;
	
}
