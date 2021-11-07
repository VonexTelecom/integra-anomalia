package com.br.integra.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import com.br.integra.filter.FiltroEstatistica;
import com.br.integra.filter.FiltroEstatisticaErros;
import com.br.integra.filter.FiltroEstatisticaNumeros;

public class FiltroPipeline {


	public static Criteria definirFiltroBuscaEstatisticasValores(FiltroEstatistica filtro) {
		Criteria query = new Criteria();
			if (filtro.getModalidade() != null && !StringUtils.isBlank(filtro.getModalidade())) {
				query.and("modalidade").is(filtro.getModalidade());
			}
			if (filtro.getDiscador() != null && !StringUtils.isBlank(filtro.getDiscador())) {
				query.and("discador").is(filtro.getDiscador());
			}
			if (filtro.getOperadora() != null && !StringUtils.isBlank(filtro.getOperadora())) {
				query.and("operadora").is(filtro.getOperadora());
			}
			if (filtro.getUnidadeAtendimento() != null && !StringUtils.isBlank(filtro.getUnidadeAtendimento())) {
				query.and("unidadeAtendimento").is(filtro.getUnidadeAtendimento());
			}
			if (filtro.getCampanha() != null && !StringUtils.isBlank(filtro.getCampanha())) {
				query.and("campanha").is(filtro.getCampanha());
			}
			if (filtro.getDdd() != null && !StringUtils.isBlank(filtro.getDdd())) {
				query.and("ddd").is(filtro.getDdd());
			}
			if (filtro.getEstado() != null && !StringUtils.isBlank(filtro.getEstado())) {
				query.and("estado").is(filtro.getEstado());
			}
			if (filtro.getRegiao() != null && !StringUtils.isBlank(filtro.getRegiao())) {
				query.and("regiao").is(filtro.getRegiao());
			}
			
			return query;
	}
	public static Criteria definirFiltroBuscaEstatisticasValores(FiltroEstatisticaErros filtro) {
			Criteria query = new Criteria();
			
			if(filtro.getDescricaoErro() != null && !StringUtils.isBlank(filtro.getDescricaoErro())) {
				query.and("descricaoErro").is(filtro.getModalidade());
			}
			if (filtro.getModalidade() != null && !StringUtils.isBlank(filtro.getModalidade())) {
				query.and("modalidade").is(filtro.getModalidade());
			}
			if (filtro.getDiscador() != null && !StringUtils.isBlank(filtro.getDiscador())) {
				query.and("discador").is(filtro.getDiscador());
			}
			if (filtro.getOperadora() != null && !StringUtils.isBlank(filtro.getOperadora())) {
				query.and("operadora").is(filtro.getOperadora());
			}
			if (filtro.getUnidadeAtendimento() != null && !StringUtils.isBlank(filtro.getUnidadeAtendimento())) {
				query.and("unidadeAtendimento").is(filtro.getUnidadeAtendimento());
			}
			if (filtro.getCampanha() != null && !StringUtils.isBlank(filtro.getCampanha())) {
				query.and("campanha").is(filtro.getCampanha());
			}
			if (filtro.getDdd() != null && !StringUtils.isBlank(filtro.getDdd())) {
				query.and("ddd").is(filtro.getDdd());
			}
			if (filtro.getEstado() != null && !StringUtils.isBlank(filtro.getEstado())) {
				query.and("estado").is(filtro.getEstado());
			}
			if (filtro.getRegiao() != null && !StringUtils.isBlank(filtro.getRegiao())) {
				query.and("regiao").is(filtro.getRegiao());
			}
			return query;
		}
	
	public static Criteria definirFiltroBuscaEstatisticasValores(FiltroEstatisticaNumeros filtro) {
		Criteria query = new Criteria();
			if (filtro.getModalidade() != null && !StringUtils.isBlank(filtro.getModalidade())) {
				query.and("modalidade").is(filtro.getModalidade());
			}
			if (filtro.getDiscador() != null && !StringUtils.isBlank(filtro.getDiscador())) {
				query.and("discador").is(filtro.getDiscador());
			}
			if (filtro.getOperadora() != null && !StringUtils.isBlank(filtro.getOperadora())) {
				query.and("operadora").is(filtro.getOperadora());
			}
			if (filtro.getUnidadeAtendimento() != null && !StringUtils.isBlank(filtro.getUnidadeAtendimento())) {
				query.and("unidadeAtendimento").is(filtro.getUnidadeAtendimento());
			}
			if (filtro.getCampanha() != null && !StringUtils.isBlank(filtro.getCampanha())) {
				query.and("campanha").is(filtro.getCampanha());
			}
			if (filtro.getDdd() != null && !StringUtils.isBlank(filtro.getDdd())) {
				query.and("ddd").is(filtro.getDdd());
			}
			if (filtro.getEstado() != null && !StringUtils.isBlank(filtro.getEstado())) {
				query.and("estado").is(filtro.getEstado());
			}
			if (filtro.getRegiao() != null && !StringUtils.isBlank(filtro.getRegiao())) {
				query.and("regiao").is(filtro.getRegiao());
			}
			if(filtro.getNumero() != null && !StringUtils.isBlank(filtro.getNumero())) {
				query.and("numero").is(filtro.getNumero());
			}
			
			return query;
	}
	
	
}

