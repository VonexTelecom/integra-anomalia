package com.br.integra;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.br.integra.model.ProcessamentoEstatisticas;
import com.br.integra.output.dto.LeituraPlataformaDto;
import com.br.integra.service.AnomaliaService;
import com.br.integra.service.LeituraAnomaliaService;
import com.br.integra.service.ProcessamentoEstatisticaService;

@SpringBootApplication
public class IntegraAnomaliaApplication implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private LeituraAnomaliaService leituraCDRService;

	@Autowired
	private ProcessamentoEstatisticaService processamentoEstatisticaService;
	
	@Autowired
	private AnomaliaService estatisticasService;
	
	public static void main(String[] args) {
		SpringApplication.run(IntegraAnomaliaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		int threadId = 3;
		boolean aguardando = true;
		while (true) {
			ProcessamentoEstatisticas processamentoEstatisticas = new ProcessamentoEstatisticas();
			processamentoEstatisticas = ProcessamentoEstatisticas.builder().minuto(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).build();
			switch (threadId % 2) {
			case 1:
				ProcessamentoEstatisticas ultimoProcessamento = processamentoEstatisticaService.obterUltimoProcessamento();
				List<LeituraPlataformaDto> minutos = leituraCDRService.verificaPlataformaProcessamento(ultimoProcessamento);
				if (minutos.size() == 0) {
					processamentoEstatisticas = processamentoEstatisticaService.criarProcessamento();
				} else {
					log.info("Minuto sem extração das Estatisticas ");
					log.info(minutos.toString());
					processamentoEstatisticas = null;
				}
				break;
			case 0:
				processamentoEstatisticas = processamentoEstatisticaService.obterProcessoEstatisticasComFalha();
				break;
			}
			if (processamentoEstatisticas != null) {
				estatisticasService.run(processamentoEstatisticas);
				aguardando = true;
			} else {
				if (aguardando) {
					log.info("Aguardando proximo minuto....");
				}	
				aguardando = false;
			}
			threadId++;
			if (threadId == 200) {
				threadId = 3;
			}
			Thread.sleep(100);
		}
	}

}
