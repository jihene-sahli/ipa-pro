/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;
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
 * @author ali
 */
@Entity
@Table(name = "IW_PHASE")
@NamedQueries({
    @NamedQuery(name = "IwPhase.findAll", query = "SELECT i FROM IwPhase i"),
    @NamedQuery(name = "IwPhase.findByIwPhaseId", query = "SELECT i FROM IwPhase i WHERE i.iwPhaseId = :iwPhaseId"),
    @NamedQuery(name = "IwPhase.findByIwPhaseName", query = "SELECT i FROM IwPhase i WHERE i.iwPhaseName = :iwPhaseName")})
public class IwPhase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_PHASE_ID", nullable = false)
    private Long iwPhaseId;
    @Size(max = 50)
    @Column(name = "IW_PHASE_NAME", length = 50)
    private String iwPhaseName;
    @OneToMany(mappedBy = "iwPhase")
    private List<IwProgress> iwProgressList;

    public IwPhase() {
    }

    public IwPhase(Long iwPhaseId) {
        this.iwPhaseId = iwPhaseId;
    }

    public Long getIwPhaseId() {
        return iwPhaseId;
    }

    public void setIwPhaseId(Long iwPhaseId) {
        this.iwPhaseId = iwPhaseId;
    }

    public String getIwPhaseName() {
        return iwPhaseName;
    }

    public void setIwPhaseName(String iwPhaseName) {
        this.iwPhaseName = iwPhaseName;
    }

    public List<IwProgress> getIwProgressList() {
        return iwProgressList;
    }

    public void setIwProgressList(List<IwProgress> iwProgressList) {
        this.iwProgressList = iwProgressList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwPhaseId != null ? iwPhaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwPhase)) {
            return false;
        }
        IwPhase other = (IwPhase) object;
        if ((this.iwPhaseId == null && other.iwPhaseId != null) || (this.iwPhaseId != null && !this.iwPhaseId.equals(other.iwPhaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwPhase[ iwPhaseId=" + iwPhaseId + " ]";
    }
    
}
