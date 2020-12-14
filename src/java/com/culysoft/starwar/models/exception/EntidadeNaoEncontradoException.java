package com.culysoft.starwar.models.exception;

public class EntidadeNaoEncontradoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
