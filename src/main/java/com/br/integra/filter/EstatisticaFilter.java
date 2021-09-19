package com.br.integra.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.br.integra.enums.DiscadorEnum;
import com.br.integra.enums.ModalidadeEnum;
import com.br.integra.enums.OperadoraEnum;
import com.br.integra.enums.PeriodoEstatisticaEnum;
import com.br.integra.enums.TipoEstatisticaEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaFilter {

	@ApiModelProperty(name = "periodoEnum", value = "Periodo da Estatística", dataType = "Enum", example = "OitoAsDezoito")
	private PeriodoEstatisticaEnum periodoEnum;	
	 
	@ApiModelProperty(name = "dataInicial", value = "Data Início", required = false, position = 3, dataType = "DateTime", example = "2021-01-31T00:00")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date dataInicial;
	
	
	@ApiModelProperty(name = "dataFinal", value = "Data Final", required = false, position = 3, dataType = "DateTime", example = "2021-01-31T23:59")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date dataFinal;
	
	@ApiModelProperty(name = "modalidade", value = "Modalidade", dataType = "Enum", example = "fixo")
	private List<ModalidadeEnum> modalidade = new ArrayList<>();
	
	@ApiModelProperty(name = "operadora", value = "Operadora", dataType = "Enum", example = "oi")
	private List<OperadoraEnum> operadora = new ArrayList<>();
	
	@ApiModelProperty(name = "discador", value = "Discador", dataType = "Enum", example = "atto")
	private List<DiscadorEnum> discador = new ArrayList<>();
	
	@ApiModelProperty(name = "unidadeAtendimento", value = "Unidade de Atendimento", dataType = "String")
	private List<String> unidadeAtendimento = new ArrayList<>();
	
	@ApiModelProperty(name = "tipoEstatistica", value = "Tipo de estatistica", dataType = "Enum", example = "chamadas_discadas")
	private TipoEstatisticaEnum tipoEstatistica;
	
}