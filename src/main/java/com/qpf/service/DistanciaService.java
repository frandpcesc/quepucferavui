package com.qpf.service;

import java.util.List;
import java.util.Optional;

import com.qpf.model.Distancia;

public interface DistanciaService {

	List<Distancia> findAll();
	
	Optional<Distancia> findById(Long id);
	
	Distancia save(Distancia distancia);
	
	String getDistanciaNom(Long id);
}
