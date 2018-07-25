/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author chafikgouasmia
 */
@Entity
@Table(name = "IW_LIST_TEST")
@NamedQueries({
    @NamedQuery(name = "IwListTest.findAll", query = "SELECT i FROM IwListTest i"),
    @NamedQuery(name = "IwListTest.findByIdPerson", query = "SELECT i FROM IwListTest i WHERE i.idPerson = :idPerson"),
    @NamedQuery(name = "IwListTest.findByNom", query = "SELECT i FROM IwListTest i WHERE i.nom = :nom"),
    @NamedQuery(name = "IwListTest.findByPrenom", query = "SELECT i FROM IwListTest i WHERE i.prenom = :prenom"),
    @NamedQuery(name = "IwListTest.findByOptionValue", query = "SELECT i FROM IwListTest i WHERE i.optionValue = :optionValue")})
public class IwListTest implements Serializable {

    @JoinColumn(name = "IWLISTID", referencedColumnName = "IW_LIST_ID")
    @ManyToOne
    private IwList iwlistid;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PERSON")
    private Integer idPerson;
    @Size(max = 50)
    @Column(name = "NOM")
    private String nom;
    @Size(max = 50)
    @Column(name = "PRENOM")
    private String prenom;
    @Size(max = 255)
    @Column(name = "OPTION_VALUE")
    private String optionValue;

    public IwListTest() {
    }

    public IwListTest(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerson != null ? idPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwListTest)) {
            return false;
        }
        IwListTest other = (IwListTest) object;
        if ((this.idPerson == null && other.idPerson != null) || (this.idPerson != null && !this.idPerson.equals(other.idPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwListTest[ idPerson=" + idPerson + " ]";
    }

    public IwList getIwlistid() {
        return iwlistid;
    }

    public void setIwlistid(IwList iwlistid) {
        this.iwlistid = iwlistid;
    }

}
