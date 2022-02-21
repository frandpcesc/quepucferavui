package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.qpf.model.Role;
import com.qpf.model.Usuari;

public interface UsuariService extends UserDetailsService{

	// llista els usuaris amb rol ROLE_ADMIN o ROLE_PROMOTOR
	List<Usuari> findByRolesAdminOrPromotor();
	
	// llista els usuaris amb rol ROLE_ADMIN
	List<Usuari> findByRoleAdmin();	
	
	// cerca usuari per correu (login)
	Usuari findByEmail(String email);
	
	// cerca usuari per correu i actiu (login)
	Usuari findByEmailAndNonLocked(String email);	
	
    // cerca usuari per id	
	Optional<Usuari> findById(Long id);
	
	// llista tots els usuaris
	List<Usuari> findAll();
	
	Usuari save (Usuari usuari);
	
	// modifica usuari
	void update(Long usuariId, Optional<Role> role, Boolean accountNonLocked);
	
	// recupera l'usuari loguejat
	Usuari findLoggedUser();
	
	void deleteById (Long id);
	
}
