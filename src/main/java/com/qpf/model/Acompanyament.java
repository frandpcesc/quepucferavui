package com.qpf.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(
	name="qpft_acompany",
	uniqueConstraints = @UniqueConstraint(columnNames = "aco_pos")
)
public class Acompanyament {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aco_id")
    private Long id;
	
	@Column(name="aco_pos")
	private int posicio;
	
	@Column(name="aco_companys")
	private String companys;
	
	@ManyToMany(mappedBy="companys")
    List<Activitat> activitats;

	public Acompanyament() {

	}

	public Acompanyament(int posicio, String companys, List<Activitat> activitats) {
		this.posicio = posicio;
		this.companys = companys;
		this.activitats = activitats;
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

	public String getCompanys() {
		return companys;
	}

	public void setCompanys(String companys) {
		this.companys = companys;
	}

	public List<Activitat> getActivitats() {
		return activitats;
	}

	public void setActivitats(List<Activitat> activitats) {
		this.activitats = activitats;
	}

	
	
	
}
