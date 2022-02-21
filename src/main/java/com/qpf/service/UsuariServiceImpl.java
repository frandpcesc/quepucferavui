package com.qpf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qpf.model.Role;
import com.qpf.model.Usuari;
import com.qpf.repository.RoleRepository;
import com.qpf.repository.UsuariRepository;

@Service
public class UsuariServiceImpl implements UsuariService {

    @Autowired
    private UsuariRepository usuariRepository;
    
    @Autowired
    private RoleRepository roleRepository;    

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
	// llista els usuaris amb rol ROLE_ADMIN o ROLE_PROMOTOR
	public List<Usuari> findByRolesAdminOrPromotor() {
        return usuariRepository.findByRolesAdminOrPromotor();		
	}
	
	// llista els usuaris amb rol ROLE_ADMIN
	public List<Usuari> findByRoleAdmin() {
        return usuariRepository.findByRoleAdmin();		
	}	

    // cerca usuari per correu (login)
    public Usuari findByEmail(String email){
        return usuariRepository.findByEmail(email);
    }
    
    // cerca usuari per correu i actiu (login)
    public Usuari findByEmailAndNonLocked(String email){
        return usuariRepository.findByEmailAndNonLocked(email);
    }    
    
    // cerca usuari per id
    public Optional<Usuari> findById(Long id){
        return usuariRepository.findById(id);
    }
    
    // llista tots els usuaris
	@Override
	public List<Usuari> findAll() {
		return usuariRepository.findAll();
	}    
        
	@Override
	public Usuari save (Usuari usuari) {
		return usuariRepository.save(usuari);
	}    
    
    // modifica usuari
    public void update(Long usuariId, Optional<Role> role, Boolean accountNonLocked) {
    	usuariRepository.update(usuariId, role, accountNonLocked);
    }    
    
	// recupera l'usuari loguejat
	public Usuari findLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;		
		UserDetails ud = null;
		if (principal instanceof UserDetails) {
			ud = (UserDetails)principal;
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		Usuari usuari = this.findByEmail(username);
		return usuari;
	}

    // carrega dades de l'usuari loguejat
    // aquest mètode fa el login de l'usuari i sobreescriu el mètode UserDetailsService.loadUserByUsername()
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuari user = usuariRepository.findByEmailAndNonLocked(email);
        if (user == null){
            throw new UsernameNotFoundException("Nom d'usuari o contrasenya incorrectes");
        }
        return new org.springframework.security.core.userdetails.User(
        		user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole()));
    }

    // rol de l'usuari loguejat
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role){
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        authorities.add(authority);
        return authorities;
    }
    
	@Override
	public void deleteById (Long id) {
		usuariRepository.deleteById(id);
	}

}
