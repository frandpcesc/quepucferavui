package com.qpf.service;

import java.util.List;
import java.util.Optional;

import com.qpf.model.Dades;

public interface DadesService {

	List<Dades> findAll();
	
	// llistes assignades als administradors
	List<Dades> findAllByRoleAdmin();
	
	// llistes assignades a un usuari	
	List<Dades> findAllByUserId(Long usuari_id);

	Optional<Dades> findById(Long id);
	
	Dades save (Dades dades);
	
	//Dades saveAndFlush (Dades dades);	
	
	void deleteById(Long id);
	
}
