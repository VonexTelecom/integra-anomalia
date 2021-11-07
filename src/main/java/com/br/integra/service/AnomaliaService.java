package com.br.integra.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.integra.filter.FiltroEstatistica;
import com.br.integra.filter.FiltroEstatisticaErros;
import com.br.integra.filter.FiltroEstatisticaNumeros;
import com.br.integra.model.Cliente;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.model.Numeros;
import com.br.integra.model.OutrosErros;
import com.br.integra.model.ProcessamentoEstatisticas;
import com.br.integra.model.VariableObject;
import com.br.integra.output.dto.AnomaliaOutputDto;
import com.br.integra.output.dto.AnomaliaOutputDtoErros;
import com.br.integra.output.dto.AnomaliaOutputDtoNumeros;
import com.br.integra.output.dto.ValorAnomalia;
import com.br.integra.repository.AnomaliaRepository;
import com.br.integra.repository.ClienteRepository;
import com.br.integra.utils.NomeCollectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;


@Service
public class AnomaliaService {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private AnomaliaRepository repository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ProcessamentoEstatisticaService processamentoEstatisticaService;
	
	public void run(ProcessamentoEstatisticas processamentoEstatisticas) throws IOException, URISyntaxException {
		Long startTime = System.currentTimeMillis();
		LocalDateTime fimMinuto = processamentoEstatisticas.getMinuto();
		
		
		LocalDateTime inicioMinuto = fimMinuto.minusHours(3);
		List<Cliente> clienteId = clienteRepository.findAll();
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		
		clienteId.stream().parallel().forEachOrdered(cliente -> executorService.execute(()->{
			try {
					estatisticaPorMinuto(cliente.getId().intValue(), inicioMinuto, fimMinuto);		
					Long endTime = System.currentTimeMillis();
					log.info("Tempo de Execução total cliente " + cliente.getNome() + "  " + (float) (endTime - startTime) / 1000 + "s");
					processamentoEstatisticaService.finalizarProcessamento(processamentoEstatisticas);					
			}catch (Exception e) {
				processamentoEstatisticaService.falhaProcessamento(processamentoEstatisticas);
				e.printStackTrace();
			}
		}));
		try {
			executorService.shutdown();
			executorService.awaitTermination(3000, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void estatisticaPorMinuto(Integer clienteId, LocalDateTime inicioMinuto, LocalDateTime fimMinuto) throws IOException, URISyntaxException {
		String nomeCollectionAnomalia = NomeCollectionUtils.nomeCollectionAnomalia(clienteId);
		String nomeCollectionAnomaliaErros = NomeCollectionUtils.nomeCollectionAnomaliaErros(clienteId);
		String nomeCollectionAnomaliaNumeros = NomeCollectionUtils.nomeCollectionAnomaliaNumeros(clienteId);
		
		
		String nomeCollectionErros = NomeCollectionUtils.nomeCollectionErros(clienteId, fimMinuto.toLocalDate());
		String nomeCollectionNumeros = NomeCollectionUtils.nomeCollectionNumeros(clienteId, fimMinuto.toLocalDate());
		String nomeCollectionEstatistica = NomeCollectionUtils.nomeCollection(clienteId, fimMinuto.toLocalDate());
		
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_discadas", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_completadas", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_desconectadas_discador", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_completadas_com_mais_de_30_segundos_desc_origem", nomeCollectionEstatistica), nomeCollectionAnomalia);
	//	repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_com_segundo_desc_destino", nomeCollectionEstatistica), nomeCollectionAnomalia);
	//	repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_com_segundo_desc_origem", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "max_caps_sainte", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_ddd", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "estatistica_acd", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_mais_3_segundos_desconectadas_pela_origem", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_mais_3_segundos_desconectadas_pela_destino", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_numero_invalido", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_completadas_acd", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "media_caps_sainte", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_recebidas", nomeCollectionEstatistica), nomeCollectionAnomalia);
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_atendidas", nomeCollectionEstatistica), nomeCollectionAnomalia);
		
		repository.salvarErros(obterEstatisticaErros(clienteId, inicioMinuto, fimMinuto,  nomeCollectionErros), nomeCollectionAnomaliaErros);
		
		repository.salvarNumeros(obterEstatisticaNumeros(clienteId, inicioMinuto, fimMinuto, "numeros_discados", nomeCollectionNumeros), nomeCollectionAnomaliaNumeros);
		repository.salvarNumeros(obterEstatisticaNumeros(clienteId, inicioMinuto, fimMinuto, "numeros_completados", nomeCollectionNumeros), nomeCollectionAnomaliaNumeros);
		repository.salvarNumeros(obterEstatisticaNumeros(clienteId, inicioMinuto, fimMinuto, "numero_desc_destino", nomeCollectionNumeros), nomeCollectionAnomaliaNumeros);
		repository.salvarNumeros(obterEstatisticaNumeros(clienteId, inicioMinuto, fimMinuto, "numero_desc_origem", nomeCollectionNumeros), nomeCollectionAnomaliaNumeros);
		
		
	}
	
	
	public List<AnomaliaOutputDto> obterEstatistica(Integer clienteId, LocalDateTime inicioMinuto, LocalDateTime fimMinuto, String tipoEstatistica, String nomeCollection) throws IOException, URISyntaxException {
		HashMap<FiltroEstatistica, ArrayList<EstatisticaDiscador>>estatisticas = repository.findTipoEstatistica(inicioMinuto, fimMinuto, tipoEstatistica, clienteId, nomeCollection);
		List<AnomaliaOutputDto> dto = new ArrayList<>();
		if(estatisticas.size() != 0) {
			estatisticas.forEach((key, value) -> {
			ValorAnomalia anomalia;
			if(value.size() > 10) {
					try {
						int[] values = value.stream().map(e -> e.getQuantidade().intValue()).collect(Collectors.toList()).stream().mapToInt(Integer :: intValue).toArray();
						anomalia = identificadorAnomalias(values);
						if(anomalia != null) {
							AnomaliaOutputDto dado = AnomaliaOutputDto.builder()
									.tipoEstatistica(tipoEstatistica)
									.clienteId(clienteId)
									.data(fimMinuto)
									.quantidade(anomalia.getValor())
									.valorEsperado(anomalia.getValorEsperado())
									.porcentual(anomalia.getPorcentagem())
									.build();
							BeanUtils.copyProperties(key, dado, "tipoEstatistica", "clienteId", "data", "quantidade","valorEsperado", "porcentual");
							dto.add(dado);
							log.info("\nAnomalia Identificada no cliente "+clienteId+" na estatistica: "+tipoEstatistica +"\tValor: "+ dado.getQuantidade()+ "\tValor Esperado: "+ dado.getValorEsperado()+ "\tPorcentagem: "+ dado.getPorcentual());
						}
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}}});
			}
		return dto;
	}
	
	public List<AnomaliaOutputDtoErros> obterEstatisticaErros(Integer clienteId, LocalDateTime inicioMinuto, LocalDateTime fimMinuto, String nomeCollection) throws IOException, URISyntaxException {
		HashMap<FiltroEstatisticaErros, ArrayList<OutrosErros>>estatisticas = repository.findTipoEstatisticaErros(inicioMinuto, fimMinuto, clienteId, nomeCollection);
		List<AnomaliaOutputDtoErros> dto = new ArrayList<>();
		if(estatisticas.size() != 0) {
			estatisticas.forEach((key, value) -> {
				if(value.size() > 10) {
					ValorAnomalia anomalia;
					try {
						int[] values = value.stream().map(e -> e.getQuantidade().intValue()).collect(Collectors.toList()).stream().mapToInt(Integer :: intValue).toArray();
						anomalia = identificadorAnomalias(values);
						if(anomalia != null) {
							AnomaliaOutputDtoErros dado = AnomaliaOutputDtoErros.builder()
									.descricaoErro(key.getDescricaoErro())
									.statusChamada(key.getStatusChamada())
									.clienteId(clienteId)
									.data(fimMinuto)
									.quantidade(anomalia.getValor())
									.valorEsperado(anomalia.getValorEsperado())
									.porcentual(anomalia.getPorcentagem())
									.build();
							BeanUtils.copyProperties(key, dado, "descricaoErro", "statusChamada",  "clienteId", "data", "quantidade","valorEsperado", "porcentual");
							dto.add(dado);
							log.info("\nAnomalia Identificada em erros no cliente "+clienteId+":\t" +"Valor: "+ dado.getQuantidade()+ "\tValor Esperado: "+ dado.getValorEsperado()+ "\tPorcentagem: "+ dado.getPorcentual());
						}
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
			}}});
		}
		return dto;
	}
	
	public List<AnomaliaOutputDtoNumeros> obterEstatisticaNumeros(Integer clienteId, LocalDateTime inicioMinuto, LocalDateTime fimMinuto, String tipoEstatistica, String nomeCollection) throws IOException, URISyntaxException {
		HashMap<FiltroEstatisticaNumeros, ArrayList<Numeros>>estatisticas = repository.findTipoNumeros(inicioMinuto, fimMinuto,  tipoEstatistica, clienteId,nomeCollection);
		List<AnomaliaOutputDtoNumeros> dto = new ArrayList<>();
		if(estatisticas.size() != 0) {
			estatisticas.forEach((key, value) -> {
				if(value.size() > 10) {
					ValorAnomalia anomalia;
					try {
						int[] values = value.stream().map(e -> e.getQuantidade().intValue()).collect(Collectors.toList()).stream().mapToInt(Integer :: intValue).toArray();
						anomalia = identificadorAnomalias(values);
						if(anomalia != null) {
							AnomaliaOutputDtoNumeros dado = AnomaliaOutputDtoNumeros.builder()
									.clienteId(clienteId)
									.data(fimMinuto)
									.tipoEstatistica(tipoEstatistica)
									.numero(key.getNumero())
									.quantidade(anomalia.getValor())
									.valorEsperado(anomalia.getValorEsperado())
									.porcentual(anomalia.getPorcentagem())
									.build();
							BeanUtils.copyProperties(key, dado, "numeros","tipoEstatistica", "clienteId", "data", "quantidade","valorEsperado", "porcentual");
							dto.add(dado);
							log.info("\nAnomalia Identificada em numeros no cliente "+clienteId+" na estatistica: "+tipoEstatistica+"\tValor: "+ dado.getQuantidade()+ "\tValor Esperado: "+ dado.getValorEsperado()+ "\tPorcentagem: "+ dado.getPorcentual());
						}
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
			}}});
		}
		return dto;
	}
	
	
	
	
	
	public ValorAnomalia identificadorAnomalias(int[] values) throws IOException, URISyntaxException {
		ValorAnomalia valorAnomalia = new ValorAnomalia();
		if(values.length != 0) {
			HashMap<String, String> resultado = mean(values);
			
			for (String valor : resultado.keySet()) {
				Integer valorInt = Integer.valueOf(valor);
				if(valorInt == (values.length) && values.length > 10) {
					BigDecimal valorBig = BigDecimal.valueOf(Double.valueOf(values[valorInt.intValue()-1]));
					BigDecimal valorEsperadoBig = BigDecimal.valueOf(Double.valueOf(resultado.get(valor)));
					valorAnomalia = ValorAnomalia.builder()
							.valor(valorBig)
							.valorEsperado(valorEsperadoBig)
							.porcentagem((valorEsperadoBig.multiply(BigDecimal.valueOf(100)).divide(valorBig, 2, RoundingMode.HALF_UP))).build();
					if(valorBig.compareTo(valorEsperadoBig) != 0) {
						return valorAnomalia;												
					}
				}
				
			}
		}
		return null;

	}
	
	
	
	
    public HashMap<String,String> mean(int[] values) throws IOException, URISyntaxException {
    RCode code = RCode.create();
    RCaller caller = RCaller.create();
    
    code.R_require("forecast");
    code.addIntArray("x", values);
    

    code.addRCode("myResult <- tsoutliers(x)");
    caller.setRCode(code);
    
    caller.runAndReturnResult("myResult");
    

    String xml = caller.getParser().getXMLFileAsString();
    
    return  extrairResultado(xml);
    		
    }	
    
    public HashMap<String,String> extrairResultado(String xml) throws IOException {
 
    	XmlMapper xmlMapper = new XmlMapper();
    	JsonNode node = xmlMapper.readTree(xml.getBytes());

    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	
    	jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    	jsonMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    	
    	String json = jsonMapper.writeValueAsString(node);
    	
    	VariableObject object = jsonMapper.readValue(json, VariableObject.class);
    	
    	HashMap<String, String> valor = new HashMap<String, String>();
    	
    	Integer quantidade = object.getVariable().get(0).getV().size();
    	for(int i = 0; i < quantidade; i++) {
    		String ind = object.getVariable().get(0).getV().get(i);
    		String rep = object.getVariable().get(1).getV().get(i);
    		valor.put(ind, rep);
    	}
    	
    	return valor;
    }
}
