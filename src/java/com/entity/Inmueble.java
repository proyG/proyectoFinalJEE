/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author KMILO
 */
@Entity
@Table(name = "inmueble")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inmueble.findAll", query = "SELECT i FROM Inmueble i"),
    @NamedQuery(name = "Inmueble.findByBarrio", query = "SELECT i FROM Inmueble i WHERE i.barrio = :barrio"),
    @NamedQuery(name = "Inmueble.findByDireccion", query = "SELECT i FROM Inmueble i WHERE i.direccion = :direccion"),
    @NamedQuery(name = "Inmueble.findByTelefono", query = "SELECT i FROM Inmueble i WHERE i.telefono = :telefono"),
    @NamedQuery(name = "Inmueble.findByTipo", query = "SELECT i FROM Inmueble i WHERE i.tipo = :tipo"),
    @NamedQuery(name = "Inmueble.findByTama\u00c3o", query = "SELECT i FROM Inmueble i WHERE i.tama\u00c3o = :tama\u00c3o"),
    @NamedQuery(name = "Inmueble.findByPrecio", query = "SELECT i FROM Inmueble i WHERE i.precio = :precio"),
    @NamedQuery(name = "Inmueble.findByImg", query = "SELECT i FROM Inmueble i WHERE i.img = :img"),
    @NamedQuery(name = "Inmueble.findByIdInmueble", query = "SELECT i FROM Inmueble i WHERE i.idInmueble = :idInmueble")})
public class Inmueble implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 50)
    @Column(name = "barrio")
    private String barrio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 10)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 50)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 50)
    @Column(name = "tama\u00c3\u00b1o")
    private String tamaÃo;
    @Size(max = 50)
    @Column(name = "precio")
    private String precio;
    @Size(max = 50)
    @Column(name = "img")
    private String img;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_inmueble")
    private Integer idInmueble;
    @OneToMany(mappedBy = "inmueble")
    private Collection<Transaccion> transaccionCollection;

    public Inmueble() {
    }

    public Inmueble(Integer idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inmueble(Integer idInmueble, String direccion) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamaÃo() {
        return tamaÃo;
    }

    public void setTamaÃo(String tamaÃo) {
        this.tamaÃo = tamaÃo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(Integer idInmueble) {
        this.idInmueble = idInmueble;
    }

    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInmueble != null ? idInmueble.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inmueble)) {
            return false;
        }
        Inmueble other = (Inmueble) object;
        if ((this.idInmueble == null && other.idInmueble != null) || (this.idInmueble != null && !this.idInmueble.equals(other.idInmueble))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Inmueble[ idInmueble=" + idInmueble + " ]";
    }
    
}
