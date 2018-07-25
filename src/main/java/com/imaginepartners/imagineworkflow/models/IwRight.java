package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_RIGHT")
@NamedQueries({
	@NamedQuery(name = "IwRight.findAll", query = "SELECT i FROM IwRight i")
})
public class IwRight implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_RIGHT_ID", nullable = false)
	private Long iwRightId;

	@Size(max = 254)
	@Column(name = "IW_APPLICATION_KEY", length = 254)
	private String iwApplicationKey;

	@Size(max = 64)
	@Column(name = "IW_GROUP", length = 64)
	private String iwGroup;

	@Size(max = 254)
	@Column(name = "IW_PROCESS_KEY", length = 254)
	private String iwProcessKey;

	@Size(max = 64)
	@Column(name = "IW_USER", length = 64)
	private String iwUser;

	@Column(name = "IW_STANDALONE_ID")
	private Long iwStandaloneId;

	@Column(name = "IW_RIGHT_TO_LAUNCH")
	private Boolean iwRightToLaunch;

	public IwRight() {
	}

	public IwRight(Long iwRightId) {
		this.iwRightId = iwRightId;
	}

	public Long getIwRightId() {
		return iwRightId;
	}

	public void setIwRightId(Long iwRightId) {
		this.iwRightId = iwRightId;
	}

	public String getIwApplicationKey() {
		return iwApplicationKey;
	}

	public void setIwApplicationKey(String iwApplicationKey) {
		this.iwApplicationKey = iwApplicationKey;
	}

	public String getIwGroup() {
		return iwGroup;
	}

	public void setIwGroup(String iwGroup) {
		this.iwGroup = iwGroup;
	}

	public String getIwProcessKey() {
		return iwProcessKey;
	}

	public void setIwProcessKey(String iwProcessKey) {
		this.iwProcessKey = iwProcessKey;
	}

	public String getIwUser() {
		return iwUser;
	}

	public void setIwUser(String iwUser) {
		this.iwUser = iwUser;
	}

	public Long getIwStandaloneId() {
		return iwStandaloneId;
	}

	public void setIwStandaloneId(Long iwStandaloneId) {
		this.iwStandaloneId = iwStandaloneId;
	}

	public Boolean getIwRightToLaunch() {
		return iwRightToLaunch;
	}

	public void setIwRightToLaunch(Boolean iwRightToLaunch) {
		this.iwRightToLaunch = iwRightToLaunch;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwRightId != null ? iwRightId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwRight)) {
			return false;
		}
		IwRight other = (IwRight) object;
		if ((this.iwRightId == null && other.iwRightId != null) || (this.iwRightId != null && !this.iwRightId.equals(other.iwRightId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwRight[ iwRightId=" + iwRightId + " ]";
	}
}
