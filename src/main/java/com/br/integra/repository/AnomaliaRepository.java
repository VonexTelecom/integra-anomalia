package com.br.integra.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.br.integra.filter.FiltroEstatistica;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.output.dto.AnomaliaOutputDto;

public interface AnomaliaRepository {
	void salvar(List<AnomaliaOutputDto> anomalia, String nomeDaTabela);
	
	HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> findTipoEstatistica
			(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipoEstatistica, Integer clienteId, String nomeCollection);
}
