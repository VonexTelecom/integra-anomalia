package com.br.integra;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntegraAnomaliaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegraAnomaliaApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
	}

}
