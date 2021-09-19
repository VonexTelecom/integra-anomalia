package com.br.integra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.integra.config.security.CheckSecurity;
import com.br.integra.config.security.IntegraSecurity;
import com.br.integra.controller.swagger.AnomaliaControllerSwagger;
import com.br.integra.filter.EstatisticaFilter;
import com.br.integra.service.AnomaliaService;



@RestController
@RequestMapping("/anomalia-estatistica")
public class AnomaliaController implements AnomaliaControllerSwagger{

	@Autowired
	private AnomaliaService service;
	
	@Autowired
	private IntegraSecurity integraSecurity;
	
	@Override
	@GetMapping("/teste")
	@CheckSecurity.DadosSumarizados.PodeAcessar
	public ResponseEntity<?> findAllPeriodo(EstatisticaFilter filter) {
		try {
			return ResponseEntity.ok(service.Anomalias(filter, integraSecurity.getClienteId()));
		}catch(Exception e) {
			return null;
		}
	}

}
