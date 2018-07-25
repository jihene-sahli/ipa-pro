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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_GROUP_DETAILS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"IW_GROUP"})
})
@NamedQueries({
    @NamedQuery(name = "IwGroupDetails.findAll", query = "SELECT i FROM IwGroupDetails i"),
    @NamedQuery(name = "IwGroupDetails.findByIwGroupDetailsId", query = "SELECT i FROM IwGroupDetails i WHERE i.iwGroupDetailsId = :iwGroupDetailsId"),
    @NamedQuery(name = "IwGroupDetails.findByIwActive", query = "SELECT i FROM IwGroupDetails i WHERE i.iwActive = :iwActive"),
    @NamedQuery(name = "IwGroupDetails.findByIwGroup", query = "SELECT i FROM IwGroupDetails i WHERE i.iwGroup = :iwGroup"),
    @NamedQuery(name = "IwGroupDetails.findByIwResponsible", query = "SELECT i FROM IwGroupDetails i WHERE i.iwResponsible = :iwResponsible"),
    @NamedQuery(name = "IwGroupDetails.findByIwType", query = "SELECT i FROM IwGroupDetails i WHERE i.iwType = :iwType"),
    @NamedQuery(name = "IwGroupDetails.findByIwVisibale", query = "SELECT i FROM IwGroupDetails i WHERE i.iwVisibale = :iwVisibale")
})
public class IwGroupDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_GROUP_DETAILS_ID", nullable = false)
    private Long iwGroupDetailsId;

    @Column(name = "IW_ACTIVE")
    private Boolean iwActive;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "IW_GROUP", nullable = false, length = 64)
    private String iwGroup;

    @Size(max = 64)
    @Column(name = "IW_RESPONSIBLE", length = 64)
    private String iwResponsible;

    @Size(max = 64)
    @Column(name = "IW_TYPE", length = 64)
    private String iwType;

    @Column(name = "IW_VISIBALE", insertable = false)
    private Boolean iwVisibale;

    public IwGroupDetails() {

    }

    public IwGroupDetails(Long iwGroupDetailsId) {
        this.iwGroupDetailsId = iwGroupDetailsId;
    }

    public IwGroupDetails(Long iwGroupDetailsId, String iwGroup) {
        this.iwGroupDetailsId = iwGroupDetailsId;
        this.iwGroup = iwGroup;
    }

    public Long getIwGroupDetailsId() {
        return iwGroupDetailsId;
    }

    public void setIwGroupDetailsId(Long iwGroupDetailsId) {
        this.iwGroupDetailsId = iwGroupDetailsId;
    }

    public Boolean getIwActive() {
        return iwActive;
    }

    public void setIwActive(Boolean iwActive) {
        this.iwActive = iwActive;
    }

    public String getIwGroup() {
        return iwGroup;
    }

    public void setIwGroup(String iwGroup) {
        this.iwGroup = iwGroup;
    }

    public String getIwResponsible() {
        return iwResponsible;
    }

    public void setIwResponsible(String iwResponsible) {
        this.iwResponsible = iwResponsible;
    }

    public String getIwType() {
        return iwType;
    }

    public void setIwType(String iwType) {
        this.iwType = iwType;
    }

    public Boolean getIwVisibale() {
        return iwVisibale;
    }

    public void setIwVisibale(Boolean iwVisibale) {
        this.iwVisibale = iwVisibale;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwGroupDetailsId != null ? iwGroupDetailsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwGroupDetails)) {
            return false;
        }
        IwGroupDetails other = (IwGroupDetails) object;
        if ((this.iwGroupDetailsId == null && other.iwGroupDetailsId != null) || (this.iwGroupDetailsId != null && !this.iwGroupDetailsId.equals(other.iwGroupDetailsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwGroupDetails[ iwGroupDetailsId=" + iwGroupDetailsId + " ]";
    }
}
