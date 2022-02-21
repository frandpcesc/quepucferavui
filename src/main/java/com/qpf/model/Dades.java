package com.qpf.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(
	name = "qpft_dades",
	uniqueConstraints = @UniqueConstraint(columnNames = "dad_nom")
)
public class Dades {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dad_id")
    private Long id;
	
	@Column(name = "dad_nom")
    private String name;
	
    @Column(name = "dad_visible", columnDefinition = "TINYINT(1)")
    private boolean visible;		
	
	@OneToMany(mappedBy="dades", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private List<Activitat> Activitats;
	
//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dadusr_idusr")
    private Usuari usuari;	
	
	public Dades() {
	}

	public Dades(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}	

	public List<Activitat> getActivitats() {
		return Activitats;
	}

	public void setActivitats(List<Activitat> activitats) {
		Activitats = activitats;
	}
	
	public Usuari getUsuari() {
		return usuari;
	}

	public void setUsuari(Usuari usuari) {
		this.usuari = usuari;
	}	
	
}
