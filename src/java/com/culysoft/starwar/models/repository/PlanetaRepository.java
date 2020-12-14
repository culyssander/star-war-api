package com.culysoft.starwar.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.starwar.models.domain.Planeta;

@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Long> {
	
	Optional<Planeta> findByNome(String nome);

}
