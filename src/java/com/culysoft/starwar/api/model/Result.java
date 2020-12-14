package com.culysoft.starwar.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Result implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String [] films;
	
}


