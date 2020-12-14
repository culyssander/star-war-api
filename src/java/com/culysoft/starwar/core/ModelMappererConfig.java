package com.culysoft.starwar.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMappererConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
