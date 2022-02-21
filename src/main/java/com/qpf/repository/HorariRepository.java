package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Horari;

@Repository
public interface HorariRepository extends JpaRepository<Horari, Long>{
	
	// llista tots els horaris
	List<Horari> findAll();
	
	// mostra horari segons id
	Optional<Horari> findById(Long id);
	
	// insereix/actualitza horari
	Horari save(Horari horari);
	
	@Query("SELECT horari.franja FROM Horari horari WHERE horari.id= :id")
	String getFranja(Long id);
	
}
