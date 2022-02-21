package com.qpf.service;

import java.util.List;
import java.util.Optional;

import com.qpf.model.Acompanyament;


public interface AcompanyamentService {
	
	List<Acompanyament> findAll();
	
	Optional<Acompanyament> findById(Long id);
	
	Acompanyament save (Acompanyament acompanyament);
	
	String getAmbQui(Long id);
}
