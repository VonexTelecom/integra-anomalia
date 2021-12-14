package com.br.integra.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.br.integra.filter.FiltroEstatistica;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.output.dto.AnomaliaOutputDto;
import com.br.integra.utils.FiltroPipeline;

@Repository
public class AnomaliaEstatisticaRepository implements AnomaliaRepository{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private final Long minimo = 30L; 
	
	@Override
	public HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> findTipoEstatistica(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipoEstatistica, Integer clienteId, String nomeDaTabelaData) {
		Query query = new Query();
		
		query.addCriteria(new Criteria("tipoEstatistica").is(tipoEstatistica).and("clienteId").is(clienteId)
				.and("data").is(dataFinal)).with(Sort.by(Sort.Direction.DESC, "data"));
		List<EstatisticaDiscador> dado = mongoTemplate.find(query,EstatisticaDiscador.class ,nomeDaTabelaData);
		HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> dados = new HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>>(); 
		for(EstatisticaDiscador e : dado) {
			FiltroEstatistica filtro = FiltroEstatistica.builder()
					.campanha(e.getCampanha())
					.conta(e.getConta())
					.ddd(e.getDdd())
					.discador(e.getDiscador())
					.equipamento(e.getEquipamento())
					.estado(e.getEstado())
					.modalidade(e.getModalidade())
					.numeroOrigem(e.getNumeroOrigem())
					.operadora(e.getOperadora())
					.regiao(e.getRegiao())
					.unidadeAtendimento(e.getUnidadeAtendimento()).build();
				
			dados.put(filtro, new ArrayList<>());
			
			Query queryTotal = new Query();
			queryTotal.addCriteria(new Criteria("tipoEstatistica").is(tipoEstatistica).and("clienteId").is(clienteId)
					.and("data").lte(dataFinal)).with(Sort.by(Sort.Direction.DESC, "data")).limit(100);
			queryTotal.addCriteria(FiltroPipeline.definirFiltroBuscaEstatisticasValores(filtro));
			List<EstatisticaDiscador> estatisticas = mongoTemplate.find(queryTotal,EstatisticaDiscador.class ,nomeDaTabelaData);
			Collections.reverse(estatisticas);
			dados.get(filtro).addAll(estatisticas);
		}
		return dados;
    }

	@Override
	public void salvar(List<AnomaliaOutputDto> anomalia, String nomeDaTabela) {
		if(anomalia != null && anomalia.size() != 0) {
			anomalia.forEach( a -> {
			BigDecimal valor =	a.getQuantidade().subtract(a.getValorEsperado());
			if(valor.compareTo(BigDecimal.ZERO) <0) {
				valor = valor.negate();
			}
				if(a.getQuantidade().compareTo(a.getValorEsperado()) <0 &&
						  valor.compareTo(BigDecimal.valueOf(minimo))>0){
					mongoTemplate.save(a, nomeDaTabela);		
				}
			});
		}
		
	}
}
