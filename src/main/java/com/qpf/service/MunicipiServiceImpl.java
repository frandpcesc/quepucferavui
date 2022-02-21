package com.qpf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Municipi;
import com.qpf.repository.MunicipiRepository;

@Service
public class MunicipiServiceImpl implements MunicipiService{
	
	@Autowired
	private MunicipiRepository municipiRepository;

	@Override
	public List<Municipi> findAll() {
		
		return (List<Municipi>) municipiRepository.findAll();
	}
	
	@Override
	public List<Municipi> findByCodi(String codiPostal) {
		
		return (List<Municipi>) municipiRepository.findByCodi(codiPostal);
	}

	@Override
	public Long findProvinciaId(Long id_municipi) {
		return (Long) municipiRepository.findProvinciaId(id_municipi);
	}

	@Override
	public String getMunicipiNom(Long id) {
		
		return municipiRepository.getMunicipiNom(id);
	}	
	
}
