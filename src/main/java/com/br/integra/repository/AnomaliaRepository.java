package com.br.integra.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.br.integra.filter.FiltroEstatistica;
import com.br.integra.filter.FiltroEstatisticaErros;
import com.br.integra.filter.FiltroEstatisticaNumeros;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.model.Numeros;
import com.br.integra.model.OutrosErros;
import com.br.integra.output.dto.AnomaliaOutputDto;
import com.br.integra.output.dto.AnomaliaOutputDtoErros;
import com.br.integra.output.dto.AnomaliaOutputDtoNumeros;

public interface AnomaliaRepository {
	void salvar(List<AnomaliaOutputDto> anomalia, String nomeDaTabela);
	void salvarErros(List<AnomaliaOutputDtoErros> anomalia, String nomeDaTabela);
	void salvarNumeros(List<AnomaliaOutputDtoNumeros> anomalia, String nomeDaTabela);
	
	HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> findTipoEstatistica
			(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipoEstatistica, Integer clienteId, String nomeCollection);
	
	HashMap<FiltroEstatisticaErros, ArrayList<OutrosErros>> findTipoEstatisticaErros
		(LocalDateTime dataInicial, LocalDateTime dataFinal, Integer clienteId, String nomeCollection);
	
	HashMap<FiltroEstatisticaNumeros, ArrayList<Numeros>> findTipoNumeros
	(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipoEstatistica, Integer clienteId, String nomeCollection);
}
