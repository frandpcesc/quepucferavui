package com.qpf.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="qpft_usuari", uniqueConstraints = @UniqueConstraint(columnNames = "usr_email"))
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_nom")
    private String name;
    
    @Column(name = "usr_email")
    private String email;
    
    @Column(name = "usr_pass")
    private String password;
    
    // 1 usuari > 1 rol // 1 rol > n persones
    @ManyToOne
    @JoinColumn(name="usrrol_idrol")
    private Role role;    
    
    @Column(name = "usr_actiu", columnDefinition = "TINYINT(1) default 1") 
    private Boolean accountNonLocked;
    
    // activitats preferides
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(
    		name = "qpft_usract", 
    		joinColumns = @JoinColumn(name = "actusr_idusuari"), 		// id usuari
    		inverseJoinColumns = @JoinColumn(name = "usract_idact")		// id activitat
    )
    //List<Activitat> activitats; 
    List<Activitat> activitats = new ArrayList<>();    
    
    // llistes d'activitats que té assignades l'usuari
	@OneToMany(mappedBy="usuari")
	private List<Dades> Dades;    

   
    // constructors -------
    
    public Usuari() {
    }

    public Usuari(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountNonLocked=true;
    }

    public Usuari(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountNonLocked=true;
    }
    
    // id -----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // nom (complet) --------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // email (correu) -----

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // pasword ------------

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    // rol ------------------

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    // accountNonLocked ----
      
	public Boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	
	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}	

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
    
    // activitat -----------
    
    public List<Activitat> getActivitats() {
		return activitats;
	}

	public void setActivitats(List<Activitat> activitats) {
		this.activitats = activitats;
	}    
	
	// dades --------------
	
	public List<Dades> getDades() {
		return Dades;
	}

	public void setDades(List<Dades> dades) {
		Dades = dades;
	}
	   
    // toString -----------

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", role=" + role +
                '}';
    }
}

