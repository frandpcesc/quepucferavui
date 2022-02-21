package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Dades;

@Repository
public interface DadesRepository extends JpaRepository<Dades, Long> {
	
	List<Dades> findAll();
	
	// llistes assignades als administradors
	@Query("SELECT d FROM Dades d WHERE d.usuari.role.name='ROLE_ADMIN'")
	List<Dades> findAllByRoleAdmin();
	
	// llistes assignades a un usuari
	@Query("SELECT d FROM Dades d WHERE d.usuari.id= :usuari_id")	
	List<Dades> findAllByUserId(Long usuari_id);
	
	Optional<Dades> findById(Long id);
	
	Dades save(Dades dades);
	
	//Dades saveAndFlush(Dades dades);	
	
	void deleteById(Long id);
	
}
