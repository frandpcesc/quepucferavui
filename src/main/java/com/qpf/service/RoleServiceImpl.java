package com.qpf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.model.Role;
import com.qpf.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> findAll() {
		return (List<Role>) roleRepository.findAll();
	}
	
    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }  
    
    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }     
	
	@Override
	public Role save (Role role) {
		return roleRepository.save(role);
	}
}
