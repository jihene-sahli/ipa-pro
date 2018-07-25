/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ali
 */
@Entity
@Table(name = "IW_PROGRESS")
@NamedQueries({
    @NamedQuery(name = "IwProgress.findAll", query = "SELECT i FROM IwProgress i"),
    @NamedQuery(name = "IwProgress.findByIwProgressId", query = "SELECT i FROM IwProgress i WHERE i.iwProgressId = :iwProgressId"),
    @NamedQuery(name = "IwProgress.findByIwProgressEnd", query = "SELECT i FROM IwProgress i WHERE i.iwProgressEnd = :iwProgressEnd"),
    @NamedQuery(name = "IwProgress.findByIwProcKey", query = "SELECT i FROM IwProgress i WHERE i.iwProcKey = :iwProcKey"),
    @NamedQuery(name = "IwProgress.findByIwProgressRate", query = "SELECT i FROM IwProgress i WHERE i.iwProgressRate = :iwProgressRate"),
    @NamedQuery(name = "IwProgress.findByIwTask", query = "SELECT i FROM IwProgress i WHERE i.iwTask = :iwTask")})
public class IwProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_PROGRESS_ID", nullable = false)
    private Long iwProgressId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "IW_PROGRESS_END", nullable = false, precision = 5, scale = 2)
    private BigDecimal iwProgressEnd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "IW_PROC_KEY", nullable = false, length = 64)
    private String iwProcKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IW_PROGRESS_RATE", nullable = false, precision = 5, scale = 2)
    private BigDecimal iwProgressRate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "IW_TASK", nullable = false, length = 64)
    private String iwTask;
    @JoinColumn(name = "IW_PHASE", referencedColumnName = "IW_PHASE_ID")
    @ManyToOne
    private IwPhase iwPhase;

    public IwProgress() {
    }

    public IwProgress(Long iwProgressId) {
        this.iwProgressId = iwProgressId;
    }

    public IwProgress(Long iwProgressId, BigDecimal iwProgressEnd, String iwProcKey, BigDecimal iwProgressRate, String iwTask) {
        this.iwProgressId = iwProgressId;
        this.iwProgressEnd = iwProgressEnd;
        this.iwProcKey = iwProcKey;
        this.iwProgressRate = iwProgressRate;
        this.iwTask = iwTask;
    }

    public Long getIwProgressId() {
        return iwProgressId;
    }

    public void setIwProgressId(Long iwProgressId) {
        this.iwProgressId = iwProgressId;
    }

    public BigDecimal getIwProgressEnd() {
        return iwProgressEnd;
    }

    public void setIwProgressEnd(BigDecimal iwProgressEnd) {
        this.iwProgressEnd = iwProgressEnd;
    }

    public String getIwProcKey() {
        return iwProcKey;
    }

    public void setIwProcKey(String iwProcKey) {
        this.iwProcKey = iwProcKey;
    }

    public BigDecimal getIwProgressRate() {
        return iwProgressRate;
    }

    public void setIwProgressRate(BigDecimal iwProgressRate) {
        this.iwProgressRate = iwProgressRate;
    }

    public String getIwTask() {
        return iwTask;
    }

    public void setIwTask(String iwTask) {
        this.iwTask = iwTask;
    }

    public IwPhase getIwPhase() {
        return iwPhase;
    }

    public void setIwPhase(IwPhase iwPhase) {
        this.iwPhase = iwPhase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwProgressId != null ? iwProgressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwProgress)) {
            return false;
        }
        IwProgress other = (IwProgress) object;
        if ((this.iwProgressId == null && other.iwProgressId != null) || (this.iwProgressId != null && !this.iwProgressId.equals(other.iwProgressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwProgress[ iwProgressId=" + iwProgressId + " ]";
    }
    
}
