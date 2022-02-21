package com.qpf.service;

import java.util.List;

import com.qpf.model.Municipi;

public interface MunicipiService {

	List<Municipi> findAll();
	
	List<Municipi> findByCodi(String codiPostal);
	
	Long findProvinciaId(Long id_municipi);
	
	String getMunicipiNom(Long id);
}
