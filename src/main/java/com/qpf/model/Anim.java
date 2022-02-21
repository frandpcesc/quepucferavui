package com.qpf.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(
	name = "qpft_anim",
	uniqueConstraints = @UniqueConstraint(columnNames = "ani_pos")
)
public class Anim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ani_id")
    private Long id;
    
    @Column(name="ani_pos")
	private int posicio;
    
    @Column(name = "ani_estat")
    private String estat;
    
    
    @ManyToMany(mappedBy="anims")
    List<Activitat> activitats;
    

    // constructors ---------
    
    public Anim() {
    }     
    
    public Anim(int posicio, String estat, List<Activitat> activitats) {
		this.posicio = posicio;
		this.estat = estat;
		this.activitats = activitats;
	}

	// toString -----------

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

	public String getEstat() {
		return estat;
	}

	public void setEstat(String estat) {
		this.estat = estat;
	}

	public List<Activitat> getActivitats() {
		return activitats;
	}

	public void setActivitats(List<Activitat> activitats) {
		this.activitats = activitats;
	}

	@Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", estat='" + estat + '\'' +
               // ", activitats=" + activitats +
                '}';
    }
}
