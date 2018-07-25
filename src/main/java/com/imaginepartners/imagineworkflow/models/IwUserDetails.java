package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_USER_DETAILS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"IW_ACT_ID_USER"})
})
@NamedQueries({
    @NamedQuery(name = "IwUserDetails.findAll", query = "SELECT i FROM IwUserDetails i"),
    @NamedQuery(name = "IwUserDetails.findByIwUserDetailsId", query = "SELECT i FROM IwUserDetails i WHERE i.iwUserDetailsId = :iwUserDetailsId"),
    @NamedQuery(name = "IwUserDetails.findByIwActIdUser", query = "SELECT i FROM IwUserDetails i WHERE i.iwActIdUser = :iwActIdUser"),
    @NamedQuery(name = "IwUserDetails.findByIwActive", query = "SELECT i FROM IwUserDetails i WHERE i.iwActive = :iwActive"),
    @NamedQuery(name = "IwUserDetails.findByIwGeneratedPasswordPlain", query = "SELECT i FROM IwUserDetails i WHERE i.iwGeneratedPasswordPlain = :iwGeneratedPasswordPlain"),
    @NamedQuery(name = "IwUserDetails.findByIwPasswordChangeDate", query = "SELECT i FROM IwUserDetails i WHERE i.iwPasswordChangeDate = :iwPasswordChangeDate"),
    @NamedQuery(name = "IwUserDetails.findByIwTypeAuthentification", query = "SELECT i FROM IwUserDetails i WHERE i.iwTypeAuthentification = :iwTypeAuthentification")
})
public class IwUserDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_USER_DETAILS_ID", nullable = false)
    private Long iwUserDetailsId;

    @Size(max = 255)
    @Column(name = "IW_ACT_ID_USER", length = 255)
    private String iwActIdUser;

    @Column(name = "IW_ACTIVE")
    private Boolean iwActive;

    @Size(max = 255)
    @Column(name = "IW_GENERATED_PASSWORD_PLAIN", length = 255)
    private String iwGeneratedPasswordPlain;

    @Column(name = "IW_PASSWORD_CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwPasswordChangeDate;

    @Size(max = 45)
    @Column(name = "IW_TYPE_AUTHENTIFICATION", length = 45)
    private String iwTypeAuthentification;

	@Column(name = "IW_VISIBLE", insertable = false)
	private Boolean iwVisible;

	@Size(max = 255)
	@Column(name = "IW_TIMEZONE", length = 255)
	private String iwTimeZone;

    public IwUserDetails() {

    }

    public IwUserDetails(Long iwUserDetailsId) {
        this.iwUserDetailsId = iwUserDetailsId;
    }

    public Long getIwUserDetailsId() {
        return iwUserDetailsId;
    }

    public void setIwUserDetailsId(Long iwUserDetailsId) {
        this.iwUserDetailsId = iwUserDetailsId;
    }

    public String getIwActIdUser() {
        return iwActIdUser;
    }

    public void setIwActIdUser(String iwActIdUser) {
        this.iwActIdUser = iwActIdUser;
    }

    public Boolean getIwActive() {
        return iwActive;
    }

    public void setIwActive(Boolean iwActive) {
        this.iwActive = iwActive;
    }

    public String getIwGeneratedPasswordPlain() {
        return iwGeneratedPasswordPlain;
    }

    public void setIwGeneratedPasswordPlain(String iwGeneratedPasswordPlain) {
        this.iwGeneratedPasswordPlain = iwGeneratedPasswordPlain;
    }

    public Date getIwPasswordChangeDate() {
        return iwPasswordChangeDate;
    }

    public void setIwPasswordChangeDate(Date iwPasswordChangeDate) {
        this.iwPasswordChangeDate = iwPasswordChangeDate;
    }

    public String getIwTypeAuthentification() {
        return iwTypeAuthentification;
    }

    public void setIwTypeAuthentification(String iwTypeAuthentification) {
        this.iwTypeAuthentification = iwTypeAuthentification;
    }

	public String getIwTimeZone() {
		return iwTimeZone;
	}

	public void setIwTimeZone(String iwTimeZone) {
		this.iwTimeZone = iwTimeZone;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iwUserDetailsId != null ? iwUserDetailsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwUserDetails)) {
            return false;
        }
        IwUserDetails other = (IwUserDetails) object;
        if ((this.iwUserDetailsId == null && other.iwUserDetailsId != null) || (this.iwUserDetailsId != null && !this.iwUserDetailsId.equals(other.iwUserDetailsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwUserDetails[ iwUserDetailsId=" + iwUserDetailsId + " ]";
    }
	public Boolean getIwVisible() {
		return iwVisible;
	}

	public void setIwVisible(Boolean iwVisible) {
		this.iwVisible = iwVisible;
	}
}
