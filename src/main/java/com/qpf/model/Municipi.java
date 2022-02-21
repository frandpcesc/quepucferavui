package com.qpf.model;


import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
	name = "qpft_municipi",
	uniqueConstraints = @UniqueConstraint(columnNames = "mun_nom")
)
public class Municipi {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mun_id")
    private Long id;
	
	@NotNull
    @Column(name = "mun_nom")
    private String nom;
	
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "munpro_idpro")
    private Provincia provincia;
	
	@OneToMany(mappedBy="municipi")
	private List<Activitat> Activitats;
	
	@ManyToMany(mappedBy="municipis")
    List<Codi> codis;

	public Municipi() {
		
	}
	
	public Municipi(@NotNull String nom, Provincia provincia) {
		this.nom = nom;
		this.provincia = provincia;
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

	
	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	/*public List<Codi> getCodis() {
		return codis;
	}

	public void setCodis(List<Codi> codis) {
		this.codis = codis;
	}
	*/
	
}
