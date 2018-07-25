/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author ibrahimhammani
 */
@Entity
@Table(name = "IW_EVENT")
@NamedQueries({
    @NamedQuery(name = "IwEvent.findAll", query = "SELECT i FROM IwEvent i"),
    @NamedQuery(name = "IwEvent.findByIwEventId", query = "SELECT i FROM IwEvent i WHERE i.iwEventId = :iwEventId"),
    @NamedQuery(name = "IwEvent.findByIwAllDay", query = "SELECT i FROM IwEvent i WHERE i.iwAllDay = :iwAllDay"),
    @NamedQuery(name = "IwEvent.findByIwColor", query = "SELECT i FROM IwEvent i WHERE i.iwColor = :iwColor"),
    @NamedQuery(name = "IwEvent.findByIwDescription", query = "SELECT i FROM IwEvent i WHERE i.iwDescription = :iwDescription"),
    @NamedQuery(name = "IwEvent.findByIwEndDate", query = "SELECT i FROM IwEvent i WHERE i.iwEndDate = :iwEndDate"),
    @NamedQuery(name = "IwEvent.findByIwGroup", query = "SELECT i FROM IwEvent i WHERE i.iwGroup = :iwGroup"),
    @NamedQuery(name = "IwEvent.findByIwUser", query = "SELECT i FROM IwEvent i WHERE i.iwUser = :iwUser"),
    @NamedQuery(name = "IwEvent.findByIwStartDate", query = "SELECT i FROM IwEvent i WHERE i.iwStartDate = :iwStartDate"),
    @NamedQuery(name = "IwEvent.findByIwTitre", query = "SELECT i FROM IwEvent i WHERE i.iwTitre = :iwTitre")})
public class IwEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_EVENT_ID", nullable = false)
    private Long iwEventId;
    @Column(name = "IW_ALL_DAY")
    private Boolean iwAllDay;
    @Size(max = 255)
    @Column(name = "IW_COLOR", length = 255)
    private String iwColor;
    @Size(max = 255)
    @Column(name = "IW_DESCRIPTION", length = 255)
    private String iwDescription;
    @Column(name = "IW_END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwEndDate;
    @Size(max = 255)
    @Column(name = "IW_GROUP", length = 255)
    private String iwGroup;
    @Size(max = 255)
    @Column(name = "IW_USER", length = 255)
    private String iwUser;
    @Column(name = "IW_START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwStartDate;
    @Size(max = 255)
    @Column(name = "IW_TITRE", length = 255)
    private String iwTitre;
    @JoinColumn(name = "IW_EVENT_TYPE", referencedColumnName = "IW_EVENT_TYPE_ID")
    @ManyToOne
    private IwEventType iwEventType;

    public IwEvent() {
    }

    public IwEvent(Long iwEventId) {
        this.iwEventId = iwEventId;
    }

    public Long getIwEventId() {
        return iwEventId;
    }

    public void setIwEventId(Long iwEventId) {
        this.iwEventId = iwEventId;
    }

    public Boolean getIwAllDay() {
        return iwAllDay;
    }

    public void setIwAllDay(Boolean iwAllDay) {
        this.iwAllDay = iwAllDay;
    }

    public String getIwColor() {
        return iwColor;
    }

    public void setIwColor(String iwColor) {
        this.iwColor = iwColor;
    }

    public String getIwDescription() {
        return iwDescription;
    }

    public void setIwDescription(String iwDescription) {
        this.iwDescription = iwDescription;
    }

    public Date getIwEndDate() {
        return iwEndDate;
    }

    public void setIwEndDate(Date iwEndDate) {
        this.iwEndDate = iwEndDate;
    }

    public String getIwGroup() {
        return iwGroup;
    }

    public void setIwGroup(String iwGroup) {
        this.iwGroup = iwGroup;
    }

    public String getIwUser() {
        return iwUser;
    }

    public void setIwUser(String iwUser) {
        this.iwUser = iwUser;
    }

    public Date getIwStartDate() {
        return iwStartDate;
    }

    public void setIwStartDate(Date iwStartDate) {
        this.iwStartDate = iwStartDate;
    }

    public String getIwTitre() {
        return iwTitre;
    }

    public void setIwTitre(String iwTitre) {
        this.iwTitre = iwTitre;
    }

    public IwEventType getIwEventType() {
        return iwEventType;
    }

    public void setIwEventType(IwEventType iwEventType) {
        this.iwEventType = iwEventType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwEventId != null ? iwEventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwEvent)) {
            return false;
        }
        IwEvent other = (IwEvent) object;
        if ((this.iwEventId == null && other.iwEventId != null) || (this.iwEventId != null && !this.iwEventId.equals(other.iwEventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwEvent[ iwEventId=" + iwEventId + " ]";
    }

}
