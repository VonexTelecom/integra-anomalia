package com.br.integra.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.integra.exception.BusinessException;
import com.br.integra.filter.EstatisticaFilter;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.model.VariableObject;
import com.br.integra.output.dto.AnomaliaOutputDto;
import com.br.integra.output.dto.ValorAnomalia;
import com.br.integra.repository.AnomaliaEstatisticaRepository;
import com.br.integra.utils.DateUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;


@Service
public class AnomaliaService {
	
	@Autowired
	private AnomaliaEstatisticaRepository repository;
	
	
	
	
	public AnomaliaOutputDto Anomalias(EstatisticaFilter filter, Long clienteId) throws IOException, URISyntaxException {
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
		if((filter.getDataInicial()!=null && filter.getDataFinal()!=null) && filter.getDataInicial().after(filter.getDataFinal())) {
			throw new BusinessException("A data Inicial não pode ser maior que a final");
		}	
		else if(filter.getPeriodoEnum() != null) {
			List<LocalDateTime> datas = DateUtils.converterEnumToData(filter.getPeriodoEnum());
			dataInicial = datas.get(0);
			dataFinal = datas.get(1);
			
		}else if(filter.getDataInicial()!=null && filter.getDataFinal()!=null) {
			 dataInicial = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 dataFinal = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 
		}else if(filter.getTipoEstatistica() == null) {
			throw new BusinessException("Selecione um tipo de estatistica");
		}
		else {
			throw new BusinessException("Selecione um periodo ou uma data incial e final.");
		}
		
		LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
		AnomaliaOutputDto dto = new AnomaliaOutputDto();
		List<ValorAnomalia> valores = new ArrayList<>();
		List<EstatisticaDiscador> estatisticaBruto = new ArrayList<>();
		List<Integer> quantidadeAnomalias = new ArrayList<>();
		
		dataIntervalo.stream().forEachOrdered(dataAtual -> {
			try {
			
			if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.tipoEstatistica(filter.getTipoEstatistica())
						.build();

				estatisticaBruto.addAll(repository.findtipoEstatisticaInicial(dataAtual, filtro,clienteId));
				
			}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.tipoEstatistica(filter.getTipoEstatistica())
						.build();
				
				estatisticaBruto.addAll(repository.findtipoEstatistica(dataAtual, filtro,clienteId));
				
			}else {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.tipoEstatistica(filter.getTipoEstatistica())
						.build();
	
				estatisticaBruto.addAll(repository.findtipoEstatisticaFinal(dataAtual, filtro,clienteId));
			} 
			
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
			
			if(estatisticaBruto != null) {
				try {
					valores.addAll(identificadorAnomalias(estatisticaBruto, dataAtual));
					quantidadeAnomalias.add(quantidadeAnomalias(estatisticaBruto));
					System.out.println(quantidadeAnomalias);
					estatisticaBruto.clear();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		
		dto = AnomaliaOutputDto.builder()
			.tipoEstatistica(filter.getTipoEstatistica().getValor())
			.quantidade(quantidadeAnomalias.stream().reduce(0, (a, b) -> a+b))
			.valor(valores)
			.build();
		System.out.println(dto);
		return dto;
	}
	
	
	public List<ValorAnomalia> identificadorAnomalias(List<EstatisticaDiscador> estatisticas, LocalDate dataAtual) throws IOException, URISyntaxException {
		int[] values = estatisticas.stream().map(e -> e.getQuantidade().intValue()).collect(Collectors.toList()).stream().mapToInt(Integer :: intValue).toArray();
		List<ValorAnomalia> valorAnomalias = new ArrayList<>();
		if(values.length != 0) {
			List<Integer> resultado = mean(values).stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList());	
			for (Integer valor : resultado) {
				LocalDateTime dataAnomalia;
				dataAnomalia = LocalDateTime.of(dataAtual, estatisticas.get(valor-1).getData());
				ValorAnomalia valorAnomalia = ValorAnomalia.builder()
						.data(dataAnomalia.toString())
						.valor(values[valor.intValue()-1]).build();
				valorAnomalias.add(valorAnomalia);
				
			}
		}
		return valorAnomalias;

	}
	public Integer quantidadeAnomalias(List<EstatisticaDiscador> estatisticas)  {
		int[] values = estatisticas.stream().map(e -> e.getQuantidade().intValue()).collect(Collectors.toList()).stream().mapToInt(Integer :: intValue).toArray();
		List<Integer> resultado = new ArrayList<>();
		try {
			resultado.addAll(mean(values).stream().map(v -> Integer.parseInt(v)).collect(Collectors.toList()));
		} catch (IOException | URISyntaxException e1) {
			e1.printStackTrace();
		}
		return resultado.size();

	}
    public List<String> mean(int[] values) throws IOException, URISyntaxException {
    RCode code = RCode.create();
    RCaller caller = RCaller.create();
    
    code.addIntArray("x", values);
    code.R_require("forecast");
    

    code.addRCode("myResult <- tsoutliers(x)");

    caller.setRCode(code);
    caller.runAndReturnResult("myResult");
    

    String xml = caller.getParser().getXMLFileAsString();
    
    return  extrairResultado(xml);
    		
    }	
    
    public List<String> extrairResultado(String xml) throws IOException {
 
    	XmlMapper xmlMapper = new XmlMapper();
    	JsonNode node = xmlMapper.readTree(xml.getBytes());

    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	
    	jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    	jsonMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    	
    	String json = jsonMapper.writeValueAsString(node);
    	
    	VariableObject object = jsonMapper.readValue(json, VariableObject.class);
    	List<String> v = object.getVariable().get(0).getV();
    	return v;
    }
}
