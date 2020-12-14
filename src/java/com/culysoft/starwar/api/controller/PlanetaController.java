package com.culysoft.starwar.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.culysoft.starwar.api.model.PlanetaInput;
import com.culysoft.starwar.models.domain.Planeta;
import com.culysoft.starwar.models.service.PlanetaService;

@RestController
@RequestMapping("planetas")
public class PlanetaController {

	@Autowired
	private PlanetaService planetaService;
	
	@GetMapping
	public List<Planeta> findAll() {
		return planetaService.findAll();
	}
	
	@GetMapping("/{id}")
	public Planeta findById(@PathVariable Long id) {
		return planetaService.findById(id);
	}
	
	@GetMapping("/nome/{nome}")
	public Planeta findByNome(@PathVariable String nome) {
		return planetaService.findByNome(nome);
	}
	
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public Planeta insert(@Validated @RequestBody PlanetaInput planetaInput) {
		planetaInput.setId(null);
		return planetaService.save(planetaInput);
	}
	
	@PutMapping("/{id}")
	public Planeta update(@Validated @PathVariable Long id, @RequestBody PlanetaInput planetaInput) {
		planetaInput.setId(id);
		return planetaService.save(planetaInput);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		if(planetaService.deleteById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
}
