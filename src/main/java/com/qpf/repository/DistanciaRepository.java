package com.qpf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Distancia;

@Repository
public interface DistanciaRepository extends JpaRepository<Distancia, Long> {
	
	// mostra distància segons id
	Optional<Distancia> findById(Long id);
	
	// insereix/actualitza distància
	Distancia save(Distancia distancia);
	
	@Query("SELECT d.nom FROM Distancia d WHERE d.id= :id")
	String getDistanciaNom(Long id);
	
}
