package com.br.integra.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class JsonObject {
	private String name;
	
	private String type;
	
	private List<String> v  = new ArrayList<>();
	
	private String n;
	
	private String m;
	
	
}
