package com.qpf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qpf.model.Codi;

@Repository
public interface CodiRepository extends JpaRepository<Codi, Long> {
	

}
