package com.br.integra.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.br.integra.filter.EstatisticaFilter;
import com.br.integra.model.EstatisticaDiscador;

import com.br.integra.mapper.EstatisticaDiscadorRowMapper;
import com.br.integra.utils.FiltroEstatisticaUtils;

@Repository
public class AnomaliaEstatisticaRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private CountRepository countRepository;
	  
	public List<EstatisticaDiscador> findtipoEstatisticaInicial(LocalDate date, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);
			
		String dataInicialFormatada = formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
		
		String dataFinalFormatada = formatarData(dataFinal.toLocalTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}

		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();

		}
		

		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, filter, clienteId, dataInicialFormatada, dataFinalFormatada);
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	

	public List<EstatisticaDiscador> findtipoEstatisticaFinal(LocalDate date, EstatisticaFilter filter, Long clienteId) {
		LocalDateTime dataInicial =filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		String dataFormatada = formatarData(date);
		
		//verifição de data do enum (de 8 às 18)
		if(filter.getDataInicial() != null &&
			filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().compareTo(LocalTime.of(8, 00)) == 0){
				dataInicial = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 00);
		}
		
		
		String dataInicialFormatada = formatarData(dataInicial.toLocalTime());
		
		String dataFinalFormatada = formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		
		//validação de filtros para query na tabela
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}

		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
			
		}
		
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, filter, clienteId, dataInicialFormatada, dataFinalFormatada);		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	
	public List<EstatisticaDiscador> findtipoEstatistica(LocalDate date, EstatisticaFilter filter, Long clienteId) {
		
		String dataFormatada = formatarData(date);
		
		
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, filter, clienteId, null, null);
		
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	
	
	//método para conversão de LocalDate(yyyy-MM-dd) para string(yyyyMMdd)
	public String formatarData(LocalDate date) {

		return date.format(DateTimeFormatter.BASIC_ISO_DATE).toString();
	}
	//método para conversão de LocalTime para String
	public String formatarData(LocalTime date) {	
		return date.format(DateTimeFormatter.ISO_TIME).toString();
	}
}
