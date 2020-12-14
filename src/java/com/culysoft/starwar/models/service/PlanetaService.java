package com.culysoft.starwar.models.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.culysoft.starwar.api.model.ApiRequest;
import com.culysoft.starwar.api.model.PlanetaInput;
import com.culysoft.starwar.api.model.Result;
import com.culysoft.starwar.models.domain.Planeta;
import com.culysoft.starwar.models.exception.EntidadeNaoEncontradoException;
import com.culysoft.starwar.models.exception.NegocioException;
import com.culysoft.starwar.models.repository.PlanetaRepository;

import lombok.Data;

@Data
@Service
@ConfigurationProperties("starwarapi")
public class PlanetaService {

	@Autowired
	private PlanetaRepository planetaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String urlApiRequest;
	
	private String urlPlaneta;
	
	public List<Planeta> findAll() {
		
		List<Planeta> planetas = new ArrayList<>();
		
		planetaRepository.findAll().forEach(p -> {
			planetas.add(getPlanetaComLink(p));
		});
		
		return planetas;
	}
	
	public Planeta findById(Long id) {
		Planeta planeta = planetaRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradoException(String.format("Planeta de id %d nao encontrado.", id)));
		return getPlanetaComLink(planeta);
	}
	
	public Planeta findByNome(String nome) {
		Planeta planeta = planetaRepository.findByNome(nome)
				.orElseThrow(() -> new EntidadeNaoEncontradoException(String.format("Planeta de nome %s nao encontrado.", nome)));
		return getPlanetaComLink(planeta);
	}
	
	private Planeta getPlanetaComLink(Planeta planeta) {
		planeta.add(new Link(urlPlaneta));
		planeta.add(new Link(urlPlaneta + planeta.getId()));
		planeta.add(new Link(urlPlaneta + "nome/" + planeta.getNome()));
		return planeta;
	}
	
	public Planeta save(PlanetaInput planetaInput) {
		Planeta planeta = toEntity(planetaInput);
		
		planeta = planeta.getId() == null ? add(planeta) : edit(planeta);
		
		planeta = planetaRepository.save(planeta);
		
		return planeta;
	}

	private Planeta add(Planeta planeta) {
		validarExistenciaDoPlaneta(planeta);
		
		planeta.setQuantidadeDeAparecoesEmFilmes(findByQuantidadeDeAparecoesEmFilmes(planeta.getNome()));
		planeta.setDataEHoraDaCriacao(LocalDateTime.now());
		
		return planeta;
	}
	
	private void validarExistenciaDoPlaneta(Planeta planeta) {
		Optional<Planeta> planetaExistente = planetaRepository.findByNome(planeta.getNome());
		
		if(planetaExistente.isPresent()) {
			throw new NegocioException(String.format("Planeta %s ja encontra-se cadastrado.", planeta.getNome()));
		}
	}

	private Integer findByQuantidadeDeAparecoesEmFilmes(String nome) {
		try {
			// URL foi Movido permanentemente - Status 301
			final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build(); 
			factory.setHttpClient(httpClient);
			restTemplate.setRequestFactory(factory);
			
			ResponseEntity<ApiRequest> results = restTemplate.getForEntity(urlApiRequest, ApiRequest.class);
			
			if(results.hasBody()) {
				for(Result result : results.getBody().getResults()) {
					if(result.getName().equalsIgnoreCase(nome)) {
						return result.getFilms().length;
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return 0;
	}

	private Planeta edit(Planeta planeta) {
		Planeta planetaExistente = findById(planeta.getId());
		
		planetaExistente.setNome(planeta.getNome());
		planetaExistente.setClima(planeta.getClima());
		planetaExistente.setTerreno(planeta.getTerreno());
		planetaExistente.setDataEHoraDeAlteracao(LocalDateTime.now());
		planetaExistente.setQuantidadeDeAparecoesEmFilmes(findByQuantidadeDeAparecoesEmFilmes(planeta.getNome()));
		
		return planetaExistente;
	}

	private Planeta toEntity(PlanetaInput planetaInput) {
		return modelMapper.map(planetaInput, Planeta.class);
	}
	
	public boolean deleteById(Long id) {
		if(planetaRepository.existsById(id)) {
			planetaRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
