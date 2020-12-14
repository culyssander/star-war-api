package com.culysoft.starwar.models.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class Planeta extends RepresentationModel<Planeta> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String clima;
	private String terreno;
	private Integer quantidadeDeAparecoesEmFilmes;
	private LocalDateTime dataEHoraDaCriacao;
	private LocalDateTime dataEHoraDeAlteracao;
}
