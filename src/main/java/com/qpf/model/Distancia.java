package com.qpf.model;

import javax.persistence.*;

@Entity
@Table(
	name = "qpft_distanci",
	uniqueConstraints = @UniqueConstraint(columnNames = "dis_pos")
)
public class Distancia {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id")
    private Long id;

	@Column(name="dis_pos")
	private int posicio;
	
	@Column(name = "dis_nom")
    private String nom;

	public Distancia() {
		
	}

	public Distancia(int posicio, String nom) {
		this.posicio = posicio;
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPosicio() {
		return posicio;
	}

	public void setPosicio(int posicio) {
		this.posicio = posicio;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
}
