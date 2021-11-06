package com.br.integra.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.br.integra.utils.NomeCollectionUtils;

@Repository
public class AnomaliaEstatisticaRepository implements AnomaliaRepository{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> findTipoEstatisticaSumarizada(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipoEstatistica, Integer clienteId) {

		String nomeDaTabelaData = NomeCollectionUtils.nomeCollectionSumarizada(clienteId,dataInicial.toLocalDate());
		Query query = new Query();
		
		query.addCriteria(new Criteria("tipoEstatistica").is(tipoEstatistica).and("clienteId").is(clienteId)
				.and("data").gte(dataInicial).lte(dataFinal)).with(Sort.by(Sort.Direction.DESC, "data"));
		List<EstatisticaDiscador> estatisticas = mongoTemplate.find(query,EstatisticaDiscador.class ,nomeDaTabelaData);
		
		HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>> dados = new HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>>(); 
		for(EstatisticaDiscador e : estatisticas) {
			EstatisticaDiscador a = e;
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
			List<EstatisticaDiscador> estatistica = Arrays.asList(e); 
			if (dados.containsKey(filtro)) {
				dados.get(filtro).add(a);
			}else {
				ArrayList<EstatisticaDiscador> t = new ArrayList<>();
				t.add(e);
				dados.put(filtro, t);
			}
		}
		
		
		return dados;
	 
    }


	@Override
	public void salvar(List<AnomaliaOutputDto> anomalia, String nomeDaTabela) {
		if(anomalia != null && anomalia.size() == 0) {
			anomalia.forEach( a -> {
				mongoTemplate.save(a, nomeDaTabela);			
			});
		}
		
	}
}
