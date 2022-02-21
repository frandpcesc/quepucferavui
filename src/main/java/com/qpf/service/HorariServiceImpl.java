package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Horari;
import com.qpf.repository.HorariRepository;

@Service
public class HorariServiceImpl implements HorariService {

	@Autowired
	private HorariRepository horariRepository;
	
	@Override
	public List<Horari> findAll() {
		return (List<Horari>) horariRepository.findAll();
	}
	
    @Override
    public Optional<Horari> findById(Long id) {
        return horariRepository.findById(id);
    }    
    
	@Override
	public Horari save (Horari horari) {
		return horariRepository.save(horari);
	}

	@Override
	public String getFranja(Long id) {
		
		return horariRepository.getFranja(id);
	}			
}
