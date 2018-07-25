/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

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
import javax.validation.constraints.Size;

/**
 *
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_EVENT_TYPE")
@NamedQueries({
    @NamedQuery(name = "IwEventType.findAll", query = "SELECT i FROM IwEventType i"),
    @NamedQuery(name = "IwEventType.findByIwEventTypeId", query = "SELECT i FROM IwEventType i WHERE i.iwEventTypeId = :iwEventTypeId"),
    @NamedQuery(name = "IwEventType.findByIwName", query = "SELECT i FROM IwEventType i WHERE i.iwName = :iwName")})
public class IwEventType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_EVENT_TYPE_ID", nullable = false)
    private Long iwEventTypeId;
    @Size(max = 255)
    @Column(name = "IW_NAME", length = 255)
    private String iwName;
    @OneToMany(mappedBy = "iwEventType")
    private Collection<IwEvent> iwEventCollection;

    public IwEventType() {
    }

    public IwEventType(Long iwEventTypeId) {
        this.iwEventTypeId = iwEventTypeId;
    }

    public Long getIwEventTypeId() {
        return iwEventTypeId;
    }

    public void setIwEventTypeId(Long iwEventTypeId) {
        this.iwEventTypeId = iwEventTypeId;
    }

    public String getIwName() {
        return iwName;
    }

    public void setIwName(String iwName) {
        this.iwName = iwName;
    }

    public Collection<IwEvent> getIwEventCollection() {
        return iwEventCollection;
    }

    public void setIwEventCollection(Collection<IwEvent> iwEventCollection) {
        this.iwEventCollection = iwEventCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwEventTypeId != null ? iwEventTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwEventType)) {
            return false;
        }
        IwEventType other = (IwEventType) object;
        if ((this.iwEventTypeId == null && other.iwEventTypeId != null) || (this.iwEventTypeId != null && !this.iwEventTypeId.equals(other.iwEventTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwEventType[ iwEventTypeId=" + iwEventTypeId + " ]";
    }

}
