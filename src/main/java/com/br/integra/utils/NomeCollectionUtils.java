package com.br.integra.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NomeCollectionUtils {
	public static String nomeCollectionSumarizada(Integer clienteId, LocalDate dia) {
		String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscador"+clienteId+"Dia" + diaTabela;
		return nomeTabela;
	}
	
	public static String nomeCollectionAnomalia(Integer clienteId) {
		//String diaTabela = dia.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "");
		String nomeTabela = "EstatisticaDiscadorAnomalia"+clienteId;
		return nomeTabela;
	}
}
