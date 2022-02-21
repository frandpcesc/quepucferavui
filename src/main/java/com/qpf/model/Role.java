package com.qpf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "qpft_rols")
@Table(
	name = "qpft_rols",
	uniqueConstraints = @UniqueConstraint(columnNames = "rol_nomamigable")
)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;
    
    @Column(name = "rol_nom")
    private String name;
    
    @Column(name = "rol_nomamigable")
    private String friendlyName;

    // constructors ---------
    
    public Role() {
    }
  
    public Role(String name) {
        this.name = name;
    }
    
    // id -------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // nom ------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // nom amgiable ---------

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }    
    
    // toString -----------

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", friendlyName='" + friendlyName + '\'' +
                '}';
    }
}
