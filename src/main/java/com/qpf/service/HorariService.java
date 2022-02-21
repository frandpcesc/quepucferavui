package com.qpf.service;

import java.util.List;
import java.util.Optional;

import com.qpf.model.Horari;

public interface HorariService {
	
	List<Horari> findAll();
	
	Optional<Horari> findById(Long id);
	
	Horari save(Horari horari);
	
	String getFranja(Long id);
}
