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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

/**
 *
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_GROUP_HIERARCHY", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"IW_GROUP_HIERARCHY_ID"})})
@NamedQueries({
    @NamedQuery(name = "IwGroupHierarchy.findAll", query = "SELECT i FROM IwGroupHierarchy i"),
    @NamedQuery(name = "IwGroupHierarchy.findByIwGroupHierarchyId", query = "SELECT i FROM IwGroupHierarchy i WHERE i.iwGroupHierarchyId = :iwGroupHierarchyId"),
    @NamedQuery(name = "IwGroupHierarchy.findByIwGroup", query = "SELECT i FROM IwGroupHierarchy i WHERE i.iwGroup = :iwGroup"),
    @NamedQuery(name = "IwGroupHierarchy.findByIwGroupAndIwParant", query = "SELECT i FROM IwGroupHierarchy i WHERE i.iwGroup = :iwGroup and i.iwParant = :iwParant"),
    @NamedQuery(name = "IwGroupHierarchy.findByIwParant", query = "SELECT i FROM IwGroupHierarchy i WHERE i.iwParant = :iwParant"),
    @NamedQuery(name = "IwGroupHierarchy.findByIwHierarchyType", query = "SELECT i FROM IwGroupHierarchy i WHERE i.iwHierarchyType = :iwHierarchyType")})
public class IwGroupHierarchy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_GROUP_HIERARCHY_ID", nullable = false)
    private Long iwGroupHierarchyId;
    @Size(max = 64)
    @Column(name = "IW_GROUP", length = 64)
    private String iwGroup;
    @Size(max = 64)
    @Column(name = "IW_PARANT", length = 64)
    private String iwParant;
    @Size(max = 45)
    @Column(name = "IW_HIERARCHY_TYPE", length = 45)
    private String iwHierarchyType;

    public IwGroupHierarchy() {
    }

    public IwGroupHierarchy(Long iwGroupHierarchyId) {
        this.iwGroupHierarchyId = iwGroupHierarchyId;
    }

    public Long getIwGroupHierarchyId() {
        return iwGroupHierarchyId;
    }

    public void setIwGroupHierarchyId(Long iwGroupHierarchyId) {
        this.iwGroupHierarchyId = iwGroupHierarchyId;
    }

    public String getIwGroup() {
        return iwGroup;
    }

    public void setIwGroup(String iwGroup) {
        this.iwGroup = iwGroup;
    }

    public String getIwParant() {
        return iwParant;
    }

    public void setIwParant(String iwParant) {
        this.iwParant = iwParant;
    }

    public String getIwHierarchyType() {
        return iwHierarchyType;
    }

    public void setIwHierarchyType(String iwHierarchyType) {
        this.iwHierarchyType = iwHierarchyType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwGroupHierarchyId != null ? iwGroupHierarchyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwGroupHierarchy)) {
            return false;
        }
        IwGroupHierarchy other = (IwGroupHierarchy) object;
        if ((this.iwGroupHierarchyId == null && other.iwGroupHierarchyId != null) || (this.iwGroupHierarchyId != null && !this.iwGroupHierarchyId.equals(other.iwGroupHierarchyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwGroupHierarchy[ iwGroupHierarchyId=" + iwGroupHierarchyId + " ]";
    }

}
