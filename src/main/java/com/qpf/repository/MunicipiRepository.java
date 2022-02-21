package com.qpf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpf.model.Municipi;

@Repository
public interface MunicipiRepository extends JpaRepository<Municipi, Long> {

	List<Municipi> findAll();

	// filtra municipis per codi postal
	@Query("SELECT m FROM Municipi m JOIN m.codis c WHERE c.codiPostal = ?1 ORDER BY m.nom ASC")
	List<Municipi> findByCodi(String codiPostal);
	
	@Query("SELECT m.provincia.id FROM Municipi m WHERE m.id =?1")
	Long findProvinciaId(Long id_municipi);
	
	@Query("SELECT m.nom FROM Municipi m WHERE m.id= :id")
	String getMunicipiNom(Long id);
}
