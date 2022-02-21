package com.qpf.service;

import com.qpf.model.Anim;
import com.qpf.repository.AnimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimServiceImpl implements AnimService {
	
    @Autowired
    private AnimRepository animRepository;

    @Override
    public List<Anim> findAll() {
        return (List<Anim>) animRepository.findAll();
    }

	@Override
	public List<Anim> findAllById(Long id) {
		return (List<Anim>) animRepository.findAllById(id);
	}
	
    @Override
    public Optional<Anim> findById(Long id) {
        return animRepository.findById(id);
    }    
    
	@Override
	public Anim save (Anim anim) {
		return animRepository.save(anim);
	}

	@Override
	public String estatAnim(Long id) {
		
		return animRepository.estatAnim(id);
	}	

}
