package com.br.integra.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.br.integra.enums.PeriodoEstatisticaEnum;

public class DateUtils {
	
	public static String formatarData(ZonedDateTime date) {
		
		List<String> dataSeparada = new ArrayList<>();
		String dataString =	date.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString();
		String dataFormatada = "";
		dataString = dataString.concat(" ");
		dataString = dataString.replace(",", "");
		String d = "";
		for (char letra : dataString.toCharArray()) {
			if(letra == ' ') {
				
				dataSeparada.add(d);
				d = "";
			}else{
				d = d.concat(Character.toString(letra));
			}
		}
		dataFormatada = dataSeparada.get(0)+" "+dataSeparada.get(2)+" "+dataSeparada.get(1)+" "+dataSeparada.get(3)+" "+dataSeparada.get(4)+" "+date.getZone();
		return dataFormatada;
	} 
}
