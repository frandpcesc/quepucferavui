package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Dades;
import com.qpf.repository.DadesRepository;

@Service
public class DadesServiceImpl implements DadesService{
	
	@Autowired
	private DadesRepository dadesRepository;

	@Override
	public List<Dades> findAll() {
		return (List<Dades>) dadesRepository.findAll();
	}
	
	// llistes assignades als administradors
	public List<Dades> findAllByRoleAdmin() {
		return (List<Dades>) dadesRepository.findAllByRoleAdmin();
	}	
	
	// llistes assignades a un usuari	
	public List<Dades> findAllByUserId(Long usuari_id) {
		return (List<Dades>) dadesRepository.findAllByUserId(usuari_id);	
	}
	
    @Override
    public Optional<Dades> findById(Long id) {
        return dadesRepository.findById(id);
    }   
       
	@Override
	public Dades save (Dades dades) {
		return dadesRepository.save(dades);
	}
	
	/*
	@Override
	public Dades saveAndFlush (Dades dades) {
		return dadesRepository.saveAndFlush(dades);
	}	
	*/
	
	@Override
	public void deleteById (Long id) {
		dadesRepository.deleteById(id);
	}
	
}
