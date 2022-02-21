package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qpf.model.Role;

//@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	// When you write HQL (or JPQL) queries, you use the names of the types, not the tables.
	// (https://stackoverflow.com/questions/14446048/hibernate-table-not-mapped-error-in-hql-query)
	// en aquest cas, l'entitat pel type Role té el nom de la taula (qpft_rols)
	@Query("SELECT r FROM qpft_rols r WHERE r.name=?1")
	public Role findByName(String name);
	
	// llista tots els rols
	List<Role> findAll();
	
	// cerca per id 
	Optional<Role> findById(Long id);
	
	// actualitza rol	
	Role save(Role role);
}
