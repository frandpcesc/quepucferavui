package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Acompanyament;

@Repository
public interface AcompanyamentRepository extends JpaRepository<Acompanyament, Long> {
	
	List<Acompanyament> findAll();
	
	// mostra acompanyament segons id
	Optional<Acompanyament> findById(Long id);
	
	// insereix/actualitza acompanyament
	Acompanyament save(Acompanyament acompanyament);
	
	@Query("SELECT ac.companys FROM Acompanyament ac WHERE ac.id= :id")
	String getAmbQui(Long id);
}
