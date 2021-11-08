package com.br.integra.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NomeCollectionUtils {
	public static String nomeCollection(Integer clienteId, LocalDate dia) {
		String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscador"+clienteId+"Dia" + diaTabela;
		return nomeTabela;
	}
	public static String nomeCollectionNumeros(Integer clienteId, LocalDate dia) {
		String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "OutrosErros"+clienteId+"Dia" + diaTabela;
		return nomeTabela;
	}
	public static String nomeCollectionErros(Integer clienteId, LocalDate dia) {
		String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "OutrosErros"+clienteId+"Dia" + diaTabela;
		return nomeTabela;
	}
	
	public static String nomeCollectionAnomalia(Integer clienteId) {
		//String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscadorAnomalia"+clienteId;
		return nomeTabela;
	}
	
	public static String nomeCollectionAnomaliaErros(Integer clienteId) {
		//String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscadorAnomaliaErros"+clienteId;
		return nomeTabela;
	}
	public static String nomeCollectionAnomaliaNumeros(Integer clienteId) {
		//String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscadorAnomaliaNumeros"+clienteId;
		return nomeTabela;
	}
}
