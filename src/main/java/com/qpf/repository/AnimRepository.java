package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Anim;

@Repository
public interface AnimRepository extends JpaRepository<Anim, Long> {
	
	// llista tots els estats d'ànim
	List<Anim> findAll();
	
	List<Anim> findAllById(Long id);
	
	// mostra estat d'ànim segons id
	Optional<Anim> findById(Long id);
	
	// insereix/actualitza estat d'ànim	
	Anim save(Anim anim);
	
	@Query("SELECT anim.estat FROM Anim anim WHERE anim.id= :id")
	String estatAnim(Long id);
	
}