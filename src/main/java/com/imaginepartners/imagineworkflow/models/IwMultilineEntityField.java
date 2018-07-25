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
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_MULTILINE_ENTITY_FIELD")
@NamedQueries({
    @NamedQuery(name = "IwMultilineEntityField.findAll", query = "SELECT i FROM IwMultilineEntityField i")})
public class IwMultilineEntityField implements Serializable {

    @Column(name = "DISPLAY_IN_JOINTABLE")
    private Boolean displayInJointable;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_MULTILINE_ENTITY_FIELD_ID", nullable = false)
    private Long iwMultilineEntityFieldId;
    @Size(max = 64)
    @Column(name = "IW_MULTILINE_ENTITY_FIELD_DESCRIPTION", length = 64)
    private String iwMultilineEntityFieldDescription;
    @Size(max = 64)
    @Column(name = "IW_MULTILINE_ENTITY_FIELD_NAME", length = 64)
    private String iwMultilineEntityFieldName;
    @JoinColumn(name = "IW_MULTILINE_ENTITY", referencedColumnName = "IW_MULTILINE_ENTITY_ID")
    @ManyToOne
    private IwMultilineEntity iwMultilineEntity;
    @JoinColumn(name = "IW_LIST", referencedColumnName = "IW_LIST_ID")
    @ManyToOne
    private IwList iwList;

    public IwMultilineEntityField() {
    }

    public IwMultilineEntityField(Long iwMultilineEntityFieldId) {
        this.iwMultilineEntityFieldId = iwMultilineEntityFieldId;
    }

    public Long getIwMultilineEntityFieldId() {
        return iwMultilineEntityFieldId;
    }

    public void setIwMultilineEntityFieldId(Long iwMultilineEntityFieldId) {
        this.iwMultilineEntityFieldId = iwMultilineEntityFieldId;
    }

    public String getIwMultilineEntityFieldDescription() {
        return iwMultilineEntityFieldDescription;
    }

    public void setIwMultilineEntityFieldDescription(String iwMultilineEntityFieldDescription) {
        this.iwMultilineEntityFieldDescription = iwMultilineEntityFieldDescription;
    }

    public String getIwMultilineEntityFieldName() {
        return iwMultilineEntityFieldName;
    }

    public void setIwMultilineEntityFieldName(String iwMultilineEntityFieldName) {
        this.iwMultilineEntityFieldName = iwMultilineEntityFieldName;
    }

    public IwMultilineEntity getIwMultilineEntity() {
        return iwMultilineEntity;
    }

    public void setIwMultilineEntity(IwMultilineEntity iwMultilineEntity) {
        this.iwMultilineEntity = iwMultilineEntity;
    }

    public IwList getIwList() {
        return iwList;
    }

    public void setIwList(IwList iwList) {
        this.iwList = iwList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwMultilineEntityFieldId != null ? iwMultilineEntityFieldId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwMultilineEntityField)) {
            return false;
        }
        IwMultilineEntityField other = (IwMultilineEntityField) object;
        if ((this.iwMultilineEntityFieldId == null && other.iwMultilineEntityFieldId != null) || (this.iwMultilineEntityFieldId != null && !this.iwMultilineEntityFieldId.equals(other.iwMultilineEntityFieldId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwMultilineEntityField[ iwMultilineEntityFieldId=" + iwMultilineEntityFieldId + " ]";
    }

    public Boolean getDisplayInJointable() {
        return displayInJointable;
    }

    public void setDisplayInJointable(Boolean displayInJointable) {
        this.displayInJointable = displayInJointable;
    }

}
