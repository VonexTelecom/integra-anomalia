package com.br.integra.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.br.integra.model.CountEstatisticaDiscador;

public class CountRowMapper implements RowMapper<CountEstatisticaDiscador>{

	@Override
	
	//mapper para quantidade de tabelas com mesmo nome
	public CountEstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {
		CountEstatisticaDiscador count = new CountEstatisticaDiscador();
		count.setCount(rs.getLong("COUNT(*)"));
		return count;
	}

}
