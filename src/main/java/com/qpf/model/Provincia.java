package com.qpf.model;


import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
	name = "qpft_provin",
	uniqueConstraints = @UniqueConstraint(columnNames = "pro_nom")
)
public class Provincia {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pro_id")
    private Long id;
	
	@NotNull
    @Column(name = "pro_nom")
    private String nom;
	
	@OneToMany(mappedBy="provincia")
	private List<Municipi> Municipis;

	public Provincia() {
		
	}
	
	public Provincia(@NotNull String nom) {
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
}
