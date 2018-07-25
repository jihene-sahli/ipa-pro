/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_FORM_TEMPLATE")
@NamedQueries({
    @NamedQuery(name = "IwFormTemplate.findAll", query = "SELECT i FROM IwFormTemplate i"),
    @NamedQuery(name = "IwFormTemplate.findByIwFormTemplateId", query = "SELECT i FROM IwFormTemplate i WHERE i.iwFormTemplateId = :iwFormTemplateId"),
    @NamedQuery(name = "IwFormTemplate.findByIwName", query = "SELECT i FROM IwFormTemplate i WHERE i.iwName = :iwName"),
    @NamedQuery(name = "IwFormTemplate.findByIwDescription", query = "SELECT i FROM IwFormTemplate i WHERE i.iwDescription = :iwDescription"),
    @NamedQuery(name = "IwFormTemplate.findByIwValue", query = "SELECT i FROM IwFormTemplate i WHERE i.iwValue = :iwValue")})
public class IwFormTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_FORM_TEMPLATE_ID", nullable = false)
    private Integer iwFormTemplateId;
    @Size(max = 254)
    @Column(name = "IW_NAME", length = 254)
    private String iwName;
    @Size(max = 254)
    @Column(name = "IW_DESCRIPTION", length = 254)
    private String iwDescription;
    @Size(max = 254)
    @Column(name = "IW_VALUE", length = 254)
    private String iwValue;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "iwFormTemplate", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<IwVariableTemplate> iwVarTemplateList;
    public IwFormTemplate() {
    }

    public IwFormTemplate(Integer iwFormTemplateId) {
        this.iwFormTemplateId = iwFormTemplateId;
    }

    public Integer getIwFormTemplateId() {
        return iwFormTemplateId;
    }

    public void setIwFormTemplateId(Integer iwFormTemplateId) {
        this.iwFormTemplateId = iwFormTemplateId;
    }

    public String getIwName() {
        return iwName;
    }

    public void setIwName(String iwName) {
        this.iwName = iwName;
    }

    public String getIwDescription() {
        return iwDescription;
    }

    public void setIwDescription(String iwDescription) {
        this.iwDescription = iwDescription;
    }

    public String getIwValue() {
        return iwValue;
    }

    public void setIwValue(String iwValue) {
        this.iwValue = iwValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwFormTemplateId != null ? iwFormTemplateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwFormTemplate)) {
            return false;
        }
        IwFormTemplate other = (IwFormTemplate) object;
        if ((this.iwFormTemplateId == null && other.iwFormTemplateId != null) || (this.iwFormTemplateId != null && !this.iwFormTemplateId.equals(other.iwFormTemplateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwFormTemplate[ iwFormTemplateId=" + iwFormTemplateId + " ]";
    }

	public List<IwVariableTemplate> getIwVarTemplateList() {
		return iwVarTemplateList;
	}

	public void setIwVarTemplateList(List<IwVariableTemplate> iwVarTemplateList) {
		this.iwVarTemplateList = iwVarTemplateList;
	}
}
