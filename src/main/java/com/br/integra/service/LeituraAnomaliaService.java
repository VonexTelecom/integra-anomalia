package com.br.integra.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.integra.model.LeituraEstatistica;
import com.br.integra.model.Plataforma;
import com.br.integra.model.ProcessamentoEstatisticas;
import com.br.integra.output.dto.LeituraPlataformaDto;
import com.br.integra.repository.LeituraEstatisticaRepository;
import com.br.integra.repository.PlataformaRepository;

@Service
public class LeituraAnomaliaService {

	@Autowired
	private LeituraEstatisticaRepository leituraEstatisticaRepository;

	@Autowired
	private PlataformaRepository plataformaRepository;

	public List<LeituraPlataformaDto> verificaPlataformaProcessamento(ProcessamentoEstatisticas processamentoEstatisticas) {
		List<Plataforma> plataformas = plataformaRepository.findByAtivo(true);
		LocalDateTime proximoMinuto = processamentoEstatisticas.getMinuto().plusMinutes(1);
		List<LeituraPlataformaDto> minutos = new ArrayList<>();
		for (Plataforma plataforma : plataformas) {
			Long plataformaId = plataforma.getId();
			List<LeituraEstatistica> list = leituraEstatisticaRepository.findByPlataformaIDDataInicio(plataformaId, proximoMinuto);
			if (list.size() != 0) {
				LeituraPlataformaDto leituraPlataformaDto = LeituraPlataformaDto.builder().
						plataformaNome(plataforma.getNome()).
						minuto(proximoMinuto).
						build();
				minutos.add(leituraPlataformaDto);
			}
		}
		return minutos;
	}
}