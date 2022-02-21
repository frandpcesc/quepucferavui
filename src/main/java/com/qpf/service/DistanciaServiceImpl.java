package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Distancia;
import com.qpf.repository.DistanciaRepository;

@Service
public class DistanciaServiceImpl implements DistanciaService {
	
	@Autowired
	private DistanciaRepository distanciaRepository;
	
	@Override
	public List<Distancia> findAll() {
		return (List<Distancia>) distanciaRepository.findAll();
	}
	
    @Override
    public Optional<Distancia> findById(Long id) {
        return distanciaRepository.findById(id);
    }    
    
	@Override
	public Distancia save (Distancia distancia) {
		return distanciaRepository.save(distancia);
	}

	@Override
	public String getDistanciaNom(Long id) {
		
		return distanciaRepository.getDistanciaNom(id);
	}	

}
