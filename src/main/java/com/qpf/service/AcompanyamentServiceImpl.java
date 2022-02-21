package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Acompanyament;
import com.qpf.repository.AcompanyamentRepository;

@Service
public class AcompanyamentServiceImpl implements AcompanyamentService {

	@Autowired
	private AcompanyamentRepository acompanyamentRepository;
	
	@Override
	public List<Acompanyament> findAll() {
		return (List<Acompanyament>) acompanyamentRepository.findAll();
	}
	
    @Override
    public Optional<Acompanyament> findById(Long id) {
        return acompanyamentRepository.findById(id);
    }    
    
	@Override
	public Acompanyament save (Acompanyament acompanyament) {
		return acompanyamentRepository.save(acompanyament);
	}

	@Override
	public String getAmbQui(Long id) {
		
		return acompanyamentRepository.getAmbQui(id);
	}

}
