package com.qpf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="qpft_acti")
public class Activitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_id")
    private Long id;
    
    @Lob
	@Column(name = "act_imatge")
    private byte[] imatge;
    
    @Column(name = "act_nom")
    private String name;
    
    @NotNull
    @Lob
    @Column(name = "act_etiqueta")
    private String etiqueta;
    
    @NotNull
    @Lob
    @Column(name = "act_descrip")
    private String descripcio;
    
    @NotNull
    @Column(name = "act_dataini")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date dataInici;
    
    @NotNull
    @Column(name = "act_datafi")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date dataFi;
    
    @Lob
    @Column(name = "act_dies")
    private String dies;
    
    @Column(name = "act_url")
    private String url;
       
    // activitat visible segons l'usuari administrador
    @Column(name = "act_visiblea", columnDefinition = "TINYINT(1)")
    private boolean visiblea;

    // activitat visible segons l'usuari promotor
    @Column(name = "act_visiblep", columnDefinition = "TINYINT(1)")
    private boolean visiblep;    
    
    @NotNull
    @Column(name = "act_destaca")
    @ColumnDefault("0")
    private Integer destacat;    
    
    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
    		name = "qpft_animact", 
    		joinColumns = @JoinColumn(name = "aniact_idact"), 			// id activitat
    		inverseJoinColumns = @JoinColumn(name = "actani_idani")		// id estat d'ànim
    )
    List<Anim> anims;

    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
    		name = "qpft_horaact", 
    		joinColumns = @JoinColumn(name = "horact_idact"), 			// id activitat
    		inverseJoinColumns = @JoinColumn(name = "acthor_idhorari")	// id horari
    )
    List<Horari> horaris;
    
    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
    		name = "qpft_acompact", 
    		joinColumns = @JoinColumn(name = "acoact_idact"), 			// id activitat
    		inverseJoinColumns = @JoinColumn(name = "actaco_idaco")		// id acompanyament
    )
    List<Acompanyament> companys;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "actmun_idmun")
    private Municipi municipi;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY ,cascade=CascadeType.ALL)
    @JoinColumn(name = "actcod_idcod")
    private Codi codi;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "actdad_iddad")
    private Dades dades;
    
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST,CascadeType.MERGE},
    mappedBy = "activitats")	    
    //List<Usuari> usuaris;
    private List<Usuari> usuaris = new ArrayList<>();
    
    // constructors ---------

	public Activitat() {
		
    }

    public Activitat(byte[] imatge, String name, @NotNull String etiqueta, @NotNull String descripcio,
			@NotNull Date dataInici, @NotNull Date dataFi, String dies, String url, List<Anim> anims,
			List<Horari> horaris, List<Acompanyament> companys, @NotNull Municipi municipi, @NotNull Codi codi, Dades dades) {
		this.imatge = imatge;
		this.name = name;
		this.etiqueta = etiqueta;
		this.descripcio = descripcio;
		this.dataInici = dataInici;
		this.dataFi = dataFi;
		this.dies = dies;
		this.url = url;
		this.anims = anims;
		this.horaris = horaris;
		this.companys = companys;
		this.municipi = municipi;
		this.codi = codi;
	}

 
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public byte[] getImatge() {
		return imatge;
	}



	public void setImatge(byte[] imatge) {
		this.imatge = imatge;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEtiqueta() {
		return etiqueta;
	}



	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}



	public String getDescripcio() {
		return descripcio;
	}



	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}



	public Date getDataInici() {
		return dataInici;
	}



	public void setDataInici(Date dataInici) {
		this.dataInici = dataInici;
	}



	public Date getDataFi() {
		return dataFi;
	}



	public void setDataFi(Date dataFi) {
		this.dataFi = dataFi;
	}



	public String getDies() {
		return dies;
	}



	public void setDies(String dies) {
		this.dies = dies;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}


	
	public boolean isVisiblea() {
		return visiblea;
	}


	
	public void setVisiblea(boolean visiblea) {
		this.visiblea = visiblea;
	}



	public boolean isVisiblep() {
		return visiblep;
	}


	
	public void setVisiblep(boolean visiblep) {
		this.visiblep = visiblep;
	}
	

	
	public Integer getDestacat() {
		return destacat;
	}



	public void setDestacat(Integer destacat) {
		this.destacat = destacat;
	}
	
	
	
	public List<Anim> getAnims() {
		return anims;
	}



	public void setAnims(List<Anim> anims) {
		this.anims = anims;
	}



	public List<Horari> getHoraris() {
		return horaris;
	}



	public void setHoraris(List<Horari> horaris) {
		this.horaris = horaris;
	}



	public List<Acompanyament> getCompanys() {
		return companys;
	}



	public void setCompanys(List<Acompanyament> companys) {
		this.companys = companys;
	}



	public Municipi getMunicipi() {
		return municipi;
	}



	public void setMunicipi(Municipi municipi) {
		this.municipi = municipi;
	}



	public Codi getCodi() {
		return codi;
	}



	public void setCodi(Codi codi) {
		this.codi = codi;
	}

	
	
	public Dades getDades() {
		return dades;
	}

	
	
	public void setDades(Dades dades) {
		this.dades = dades;
	}
	
	
	
	public List<Usuari> getUsuaris() {
		return usuaris;
	}

	
	
	public void setUsuaris(List<Usuari> usuaris) {
		this.usuaris = usuaris;
	}	

	
	
	@Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nom='" + name + '\'' +
                ", anims=" + anims +
                '}';
    }
}
