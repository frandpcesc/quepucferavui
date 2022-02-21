package com.qpf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Codi;
import com.qpf.repository.CodiRepository;

@Service
public class CodiServiceImpl implements CodiService{
	
	@Autowired
	private CodiRepository codiRepository;

	@Override
	public List<Codi> findAll() {
		
		return (List<Codi>) codiRepository.findAll();
	}
}
