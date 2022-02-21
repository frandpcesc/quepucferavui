package com.qpf.service;

import java.util.List;
import java.util.Optional;

import com.qpf.model.Role;

public interface RoleService {

	// llista tots els rols de l'aplicació
	List<Role> findAll();
	
	// cerca per id 
	Optional<Role> findById(Long id);
	
	// cerca per nom
	Role findByName(String name);
	
	// actualitza rol
	Role save (Role role);	
	
}
