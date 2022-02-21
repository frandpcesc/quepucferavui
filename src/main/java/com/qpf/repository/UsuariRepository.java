package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qpf.model.Role;
import com.qpf.model.Usuari;

@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Long> {
	
	// llista tots els usuaris
	List<Usuari> findAll();
	
	// llista els usuaris actius amb rol ROLE_ADMIN o ROLE_PROMOTOR
	@Query("SELECT u FROM Usuari u WHERE u.accountNonLocked=1 AND (u.role.name='ROLE_ADMIN' OR u.role.name='ROLE_PROMOTOR')")  
	List<Usuari> findByRolesAdminOrPromotor();
	
	// llista els usuaris actius amb rol ROLE_ADMIN
	@Query("SELECT u FROM Usuari u WHERE u.accountNonLocked=1 AND (u.role.name='ROLE_ADMIN')")  
	List<Usuari> findByRoleAdmin();
	
    // cerca usuari per id	
	Optional<Usuari> findById(Long id);
	
	// cerca usuari per la seva adreça de correu
	Usuari findByEmail(String email);
	
	// cerca usuari per la seva adreça de correu i actiu
	@Query("SELECT u FROM Usuari u WHERE u.email= :email and u.accountNonLocked=1")
	Usuari findByEmailAndNonLocked(String email);
	
	// modifica rol i actiu
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Usuari u SET u.role= :role, u.accountNonLocked= :accountNonLocked WHERE u.id= :usuariId")
    int update(@Param("usuariId") Long usuariId, @Param("role") Optional<Role> role, @Param("accountNonLocked") Boolean accountNonLocked);
	
	void deleteById(Long id);
	
}
