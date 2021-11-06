package com.br.integra.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.integra.exception.BusinessException;
import com.br.integra.model.ProcessamentoEstatisticas;
import com.br.integra.repository.ProcessamentoEstatisticasRepository;

@Service
@Transactional
public class ProcessamentoEstatisticaService {

	@Autowired
	private ProcessamentoEstatisticasRepository processamentoEstatisticasRepository;

	public ProcessamentoEstatisticas criarProcessamento() {
		ProcessamentoEstatisticas processamentoEstatisticas = obterUltimoProcessamento();
		LocalDateTime dataInicioProximaLeitura = processamentoEstatisticas.getMinuto().plusMinutes(1);
		if (dataInicioProximaLeitura.isBefore(LocalDateTime.now().minusMinutes(3))) {
			ProcessamentoEstatisticas novaProcessoLeitura = new ProcessamentoEstatisticas();
			novaProcessoLeitura.setProcessoID(null);
			novaProcessoLeitura.setMinuto(dataInicioProximaLeitura);
			novaProcessoLeitura.setStatusTarifacao("Processando");
			novaProcessoLeitura.setStatusProcessamento("Processando");
			novaProcessoLeitura.setDataInicioTarifacao(LocalDateTime.now());
			novaProcessoLeitura.setDataInicioProcessamento(LocalDateTime.now());
			processamentoEstatisticasRepository.saveAndFlush(novaProcessoLeitura);
			return novaProcessoLeitura;
		} else {
			return null;
		}
	}

	public ProcessamentoEstatisticas obterProcessoEstatisticasComFalha() {
		ProcessamentoEstatisticas processamentoEstatisticas = obterFalhaLeitura();
		if (processamentoEstatisticas != null) {
			processamentoEstatisticas.setStatusTarifacao("Processando");
			processamentoEstatisticas.setStatusProcessamento("Processando");
			processamentoEstatisticas.setDataFimTarifacao(null);
			processamentoEstatisticas.setDataFimProcessamento(null);
			processamentoEstatisticas.setDataInicioTarifacao(LocalDateTime.now());
			processamentoEstatisticas.setDataInicioProcessamento(LocalDateTime.now());
			processamentoEstatisticasRepository.saveAndFlush(processamentoEstatisticas);
		}
		return processamentoEstatisticas;
	}

	public ProcessamentoEstatisticas obterUltimoProcessamento() {
		Optional<ProcessamentoEstatisticas> leituraC = processamentoEstatisticasRepository.obterUltimaLeitura();
		if (leituraC.isPresent()) {
			return leituraC.get();
		} else {
			throw new BusinessException("Não foi possível encontrar o último processo de leitura");
		}
	}

	private ProcessamentoEstatisticas obterFalhaLeitura() {
		Optional<ProcessamentoEstatisticas> leituraC = processamentoEstatisticasRepository.obterUltimaLeitura();
		if (leituraC.isPresent()) {
			LocalDateTime dataLeitura = leituraC.get().getMinuto().minusMinutes(3);
			leituraC = processamentoEstatisticasRepository.obterLeituraFalha(dataLeitura);
			if (leituraC.isPresent()) {
				return leituraC.get();
			} else {
				return null;
			}
		} else {
			throw new BusinessException("Não foi possível encontrar o último processo de leitura");
		}
	}

	public void finalizarProcessamento(ProcessamentoEstatisticas processamentoEstatisticas) {
		processamentoEstatisticas.setDataFimTarifacao(LocalDateTime.now());
		processamentoEstatisticas.setDataFimProcessamento(LocalDateTime.now());
		processamentoEstatisticas.setStatusTarifacao("Finalizado");
		processamentoEstatisticas.setStatusProcessamento("Finalizado");
		processamentoEstatisticasRepository.saveAndFlush(processamentoEstatisticas);
	}

	public void falhaProcessamento(ProcessamentoEstatisticas processamentoEstatisticas) {
		processamentoEstatisticas.setStatusTarifacao("Falha");
		processamentoEstatisticas.setStatusProcessamento("Falha");
		processamentoEstatisticasRepository.saveAndFlush(processamentoEstatisticas);
	}
}