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
import javax.persistence.Lob;
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
@Table(name = "IW_LICENSE")
@NamedQueries({
    @NamedQuery(name = "IwLicense.findAll", query = "SELECT i FROM IwLicense i"),
    @NamedQuery(name = "IwLicense.findByIwLicenseId", query = "SELECT i FROM IwLicense i WHERE i.iwLicenseId = :iwLicenseId"),
    @NamedQuery(name = "IwLicense.findByIwFileName", query = "SELECT i FROM IwLicense i WHERE i.iwFileName = :iwFileName"),
    @NamedQuery(name = "IwLicense.findByIwDate", query = "SELECT i FROM IwLicense i WHERE i.iwDate = :iwDate"),
    @NamedQuery(name = "IwLicense.findByIwActive", query = "SELECT i FROM IwLicense i WHERE i.iwActive = :iwActive")})
public class IwLicense implements Serializable {
    @Lob
    @Column(name = "IW_LICENSE_BYTES")
    private byte[] iwLicenseBytes;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_LICENSE_ID", nullable = false)
    private Integer iwLicenseId;
    @Size(max = 255)
    @Column(name = "IW_FILE_NAME", length = 255)
    private String iwFileName;
    @Column(name = "IW_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwDate;
    @Column(name = "IW_ACTIVE")
    private Boolean iwActive;

    public IwLicense() {
    }

    public IwLicense(Integer iwLicenseId) {
        this.iwLicenseId = iwLicenseId;
    }

    public Integer getIwLicenseId() {
        return iwLicenseId;
    }

    public void setIwLicenseId(Integer iwLicenseId) {
        this.iwLicenseId = iwLicenseId;
    }

    public byte[] getIwLicenseBytes() {
        return iwLicenseBytes;
    }

    public void setIwLicenseBytes(byte[] iwLicenseBytes) {
        this.iwLicenseBytes = iwLicenseBytes;
    }

    public String getIwFileName() {
        return iwFileName;
    }

    public void setIwFileName(String iwFileName) {
        this.iwFileName = iwFileName;
    }

    public Date getIwDate() {
        return iwDate;
    }

    public void setIwDate(Date iwDate) {
        this.iwDate = iwDate;
    }

    public Boolean getIwActive() {
        return iwActive;
    }

    public void setIwActive(Boolean iwActive) {
        this.iwActive = iwActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwLicenseId != null ? iwLicenseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwLicense)) {
            return false;
        }
        IwLicense other = (IwLicense) object;
        if ((this.iwLicenseId == null && other.iwLicenseId != null) || (this.iwLicenseId != null && !this.iwLicenseId.equals(other.iwLicenseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwLicense[ iwLicenseId=" + iwLicenseId + " ]";
    }


}
