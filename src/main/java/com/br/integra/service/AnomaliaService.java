package com.br.integra.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.br.integra.model.Cliente;
import com.br.integra.model.EstatisticaDiscador;
import com.br.integra.model.ProcessamentoEstatisticas;
import com.br.integra.model.VariableObject;
import com.br.integra.output.dto.AnomaliaOutputDto;
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
		String nomeCollectionEstatistica = NomeCollectionUtils.nomeCollection(clienteId, fimMinuto.toLocalDate());
		
		repository.salvar(obterEstatistica(clienteId, inicioMinuto, fimMinuto, "chamadas_discadas", nomeCollectionEstatistica), nomeCollectionAnomalia);
		
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
		
	
	public ValorAnomalia identificadorAnomalias(int[] values) throws IOException, URISyntaxException {
		ValorAnomalia valorAnomalia = new ValorAnomalia();
		if(values.length != 0) {
			HashMap<String, String> resultado = mean(values);
			
			for (String valor : resultado.keySet()) {
				Integer valorInt = Integer.valueOf(valor);
				if(valorInt == (values.length) && values.length > 90) {
					BigDecimal valorBig = BigDecimal.valueOf(Double.valueOf(values[valorInt.intValue()-1]));
					BigDecimal valorEsperadoBig = BigDecimal.valueOf(Double.valueOf(resultado.get(valor)));
					BigDecimal diferenca = valorBig.subtract(valorEsperadoBig);
					if(diferenca.compareTo(BigDecimal.ZERO) < 0) {
						diferenca = diferenca.negate();
					}else if(diferenca.compareTo(BigDecimal.ZERO) != 0) {
						valorAnomalia = ValorAnomalia.builder()
								.valor(valorBig)
								.valorEsperado(valorEsperadoBig)
								.porcentagem((diferenca.multiply(BigDecimal.valueOf(100)).divide(valorBig, 2, RoundingMode.HALF_UP))).build();
						
						if(valorBig.compareTo(valorEsperadoBig) < 0 ) {
							valorAnomalia.setPorcentagem(valorAnomalia.getPorcentagem().negate());
						}
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
