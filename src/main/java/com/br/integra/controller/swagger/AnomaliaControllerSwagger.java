package com.br.integra.controller.swagger;

import org.springframework.http.ResponseEntity;

import com.br.integra.exception.handler.Problem;
import com.br.integra.filter.EstatisticaFilter;
import com.br.integra.output.dto.AnomaliaOutputDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Controller da Estatistica")
public interface AnomaliaControllerSwagger {
		@ApiOperation(value = "Busca as Estatísticas Sumarizadas por periodo", httpMethod = "GET")
		@ApiResponses({
			@ApiResponse(code = 200, response = AnomaliaOutputDto.class, message = "Requisição com sucesso"),
			@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
		})
		ResponseEntity<?>findAllPeriodo(EstatisticaFilter filter);
}
