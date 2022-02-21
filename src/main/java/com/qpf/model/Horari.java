package com.qpf.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(
	name="qpft_horari",
	uniqueConstraints = @UniqueConstraint(columnNames = "hor_pos")
)
public class Horari {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hor_id")
    private Long id;
	
	@Column(name="hor_pos")
	private int posicio;
    
    @Column(name = "hor_franja")
    private String franja;
    
    
    @ManyToMany(mappedBy="horaris")
    List<Activitat> activitats;


	public Horari() {
		
	}


	public Horari(int posicio, String franja, List<Activitat> activitats) {
		this.posicio = posicio;
		this.franja = franja;
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


	public String getFranja() {
		return franja;
	}


	public void setFranja(String franja) {
		this.franja = franja;
	}


	public List<Activitat> getActivitats() {
		return activitats;
	}


	public void setActivitats(List<Activitat> activitats) {
		this.activitats = activitats;
	}
		

}
