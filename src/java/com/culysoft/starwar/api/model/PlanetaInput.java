package com.culysoft.starwar.api.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanetaInput {

	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String clima;
	
	@NotBlank
	private String terreno;
}
