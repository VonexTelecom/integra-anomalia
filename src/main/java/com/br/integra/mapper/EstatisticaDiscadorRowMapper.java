package com.br.integra.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.br.integra.model.EstatisticaDiscador;

public class EstatisticaDiscadorRowMapper implements RowMapper<EstatisticaDiscador>{

	/**
	 *Classe para conversão de resultado de query para model
	 */
	@Override
	public EstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		EstatisticaDiscador estatistica = EstatisticaDiscador.builder()
		.clienteId(rs.getLong("clienteId"))
		.data(rs.getTime("data").toLocalTime())
		.quantidade(rs.getBigDecimal("quantidade"))
		.tipoEstatistica(rs.getString("tipoEstatistica"))
		.tipoEstatisticaValor(Long.getLong(rs.getString("tipoEstatisticaValor")))
		.build();
		return estatistica;
			

	}

}