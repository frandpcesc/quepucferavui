package com.qpf.service;

import com.qpf.model.Anim;
import java.util.List;
import java.util.Optional;

public interface AnimService {

	// llista tots els estats d'ànim
	List<Anim> findAll();
	
	List<Anim> findAllById(Long id);
	
	Optional<Anim> findById(Long id);
	
	Anim save(Anim anim);
	
	String estatAnim(Long id);
}
