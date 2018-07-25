/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_FORM", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"IW_FORM_ID"})})
@NamedQueries({
    @NamedQuery(name = "IwForm.findAll", query = "SELECT i FROM IwForm i"),
    @NamedQuery(name = "IwForm.findByIwFormId", query = "SELECT i FROM IwForm i WHERE i.iwFormId = :iwFormId"),
    @NamedQuery(name = "IwForm.findByIwName", query = "SELECT i FROM IwForm i WHERE i.iwName = :iwName"),
    @NamedQuery(name = "IwForm.findByIwCreateTime", query = "SELECT i FROM IwForm i WHERE i.iwCreateTime = :iwCreateTime"),
    @NamedQuery(name = "IwForm.findByIwLastUpdateTime", query = "SELECT i FROM IwForm i WHERE i.iwLastUpdateTime = :iwLastUpdateTime"),
    @NamedQuery(name = "IwForm.findByIwDescription", query = "SELECT i FROM IwForm i WHERE i.iwDescription = :iwDescription")})
public class IwForm implements Serializable {

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iwForm", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<IwInput> iwInputList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IW_FORM_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iwFormId;
    @Size(max = 255)
    @Column(name = "IW_NAME", length = 255)
    private String iwName;
    @Column(name = "IW_CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwCreateTime;
    @Column(name = "IW_LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwLastUpdateTime;
    @Size(max = 255)
    @Column(name = "IW_DESCRIPTION", length = 255)
    private String iwDescription;
    @Lob
    @Size(max = 65535)
    @Column(name = "IW_FORM_SOURCE_JSON", length = 65535)
    private String iwFormSourceJson;

    // Add an index for listing vues, so we can implement drag and drop reorder
    @Column(name = "IW_LISTING_INDEX")
    private Integer iwListingIndex;

    public IwForm() {
    }

    public IwForm(Long iwFormId) {
        this.iwFormId = iwFormId;
    }

    public Long getIwFormId() {
        return iwFormId;
    }

    public void setIwFormId(Long iwFormId) {
        this.iwFormId = iwFormId;
    }

    public String getIwName() {
        return iwName;
    }

    public void setIwName(String iwName) {
        this.iwName = iwName;
    }

    public Date getIwCreateTime() {
        return iwCreateTime;
    }

    public void setIwCreateTime(Date iwCreateTime) {
        this.iwCreateTime = iwCreateTime;
    }

    public Date getIwLastUpdateTime() {
        return iwLastUpdateTime;
    }

    public void setIwLastUpdateTime(Date iwLastUpdateTime) {
        this.iwLastUpdateTime = iwLastUpdateTime;
    }

    public String getIwDescription() {
        return iwDescription;
    }

    public void setIwDescription(String iwDescription) {
        this.iwDescription = iwDescription;
    }

    public String getIwFormSourceJson() {
        return iwFormSourceJson;
    }

    public void setIwFormSourceJson(String iwFormSourceJson) {
        this.iwFormSourceJson = iwFormSourceJson;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwFormId != null ? iwFormId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwForm)) {
            return false;
        }
        IwForm other = (IwForm) object;
        if ((this.iwFormId == null && other.iwFormId != null) || (this.iwFormId != null && !this.iwFormId.equals(other.iwFormId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwForm[ iwFormId=" + iwFormId + " ]";
    }

    public List<IwInput> getIwInputList() {
        return iwInputList;
    }

    public void setIwInputList(List<IwInput> iwInputList) {
        this.iwInputList = iwInputList;
    }

    // Getter and setter for the index in vue needing a drag and drop reorder
    public Integer getIwListingIndex() {
        return iwListingIndex;
    }

    public void setIwListingIndex(Integer iwListingIndex) {
        this.iwListingIndex = iwListingIndex;
    }
}
