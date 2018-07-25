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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rafik.rebahi
 */
@Entity
@Table(name = "IW_REGISTRE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IwRegistre.findAll", query = "SELECT i FROM IwRegistre i"),
    @NamedQuery(name = "IwRegistre.findByIwRegistreId", query = "SELECT i FROM IwRegistre i WHERE i.iwRegistreId = :iwRegistreId"),
    @NamedQuery(name = "IwRegistre.findByIwName", query = "SELECT i FROM IwRegistre i WHERE i.iwName = :iwName"),
    @NamedQuery(name = "IwRegistre.findByIwValue", query = "SELECT i FROM IwRegistre i WHERE i.iwValue = :iwValue")})
public class IwRegistre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_REGISTRE_ID")
    private Long iwRegistreId;
    @Size(max = 64)
    @Column(name = "IW_NAME")
    private String iwName;
    @Size(max = 64)
    @Column(name = "IW_VALUE")
    private Long iwValue;

    public IwRegistre() {
    }

    public IwRegistre(Long iwRegistreId) {
        this.iwRegistreId = iwRegistreId;
    }

    public Long getIwRegistreId() {
        return iwRegistreId;
    }

    public void setIwRegistreId(Long iwRegistreId) {
        this.iwRegistreId = iwRegistreId;
    }

    public String getIwName() {
        return iwName;
    }

    public void setIwName(String iwName) {
        this.iwName = iwName;
    }

    public Long getIwValue() {
        return iwValue;
    }

    public void setIwValue(Long iwValue) {
        this.iwValue = iwValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwRegistreId != null ? iwRegistreId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwRegistre)) {
            return false;
        }
        IwRegistre other = (IwRegistre) object;
        if ((this.iwRegistreId == null && other.iwRegistreId != null) || (this.iwRegistreId != null && !this.iwRegistreId.equals(other.iwRegistreId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwRegistre[ iwRegistreId=" + iwRegistreId + " ]";
    }

}
