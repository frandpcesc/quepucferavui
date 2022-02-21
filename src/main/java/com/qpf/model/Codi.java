package com.qpf.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(
	name = "qpft_codi",
	uniqueConstraints = @UniqueConstraint(columnNames = "cod_codipost")
)
public class Codi {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cod_id")
    private Long id;
	
	@NotNull
    @Column(name = "cod_codipost")
    private String codiPostal;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
    		name = "qpft_codimuni", 
    		joinColumns = @JoinColumn(name = "muncod_idcod"), 			// id codi postal
    		inverseJoinColumns = @JoinColumn(name = "codmun_idmun")		// id municipi
    )    	
    List<Municipi> municipis;
	
	@OneToMany(mappedBy="codi")
	private List<Activitat> Activitat;
	
	public Codi() {

	}

	public Codi(@NotNull String codiPostal, @NotNull List<Municipi> municipis) {
		this.codiPostal = codiPostal;
		this.municipis = municipis;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiPostal() {
		return codiPostal;
	}

	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}

	public List<Municipi> getMunicipis() {
		return municipis;
	}

	public void setMunicipis(List<Municipi> municipis) {
		this.municipis = municipis;
	}
	
	
}
