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
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_CENTRAL_AISLE")
@NamedQueries({
	@NamedQuery(name = "IwCentralAisle.findAll", query = "SELECT i FROM IwCentralAisle i"),
	@NamedQuery(name = "IwCentralAisle.findByIwCentralAisleId", query = "SELECT i FROM IwCentralAisle i WHERE i.iwCentralAisleId = :iwCentralAisleId"),
	@NamedQuery(name = "IwCentralAisle.findByIwIntitule", query = "SELECT i FROM IwCentralAisle i WHERE i.iwIntitule = :iwIntitule"),
	@NamedQuery(name = "IwCentralAisle.findByIwDescription", query = "SELECT i FROM IwCentralAisle i WHERE i.iwDescription = :iwDescription")
})
public class IwCentralAisle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_CENTRAL_AISLE_ID", nullable = false)
	private Integer iwCentralAisleId;

	@Size(max = 45)
	@Column(name = "IW_INTITULE", length = 45)
	private String iwIntitule;

	@Size(max = 45)
	@Column(name = "IW_DESCRIPTION", length = 45)
	private String iwDescription;

	public IwCentralAisle() {
	}

	public IwCentralAisle(Integer iwCentralAisleId) {
		this.iwCentralAisleId = iwCentralAisleId;
	}

	public Integer getIwCentralAisleId() {
		return iwCentralAisleId;
	}

	public void setIwCentralAisleId(Integer iwCentralAisleId) {
		this.iwCentralAisleId = iwCentralAisleId;
	}

	public String getIwIntitule() {
		return iwIntitule;
	}

	public void setIwIntitule(String iwIntitule) {
		this.iwIntitule = iwIntitule;
	}

	public String getIwDescription() {
		return iwDescription;
	}

	public void setIwDescription(String iwDescription) {
		this.iwDescription = iwDescription;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwCentralAisleId != null ? iwCentralAisleId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwCentralAisle)) {
			return false;
		}
		IwCentralAisle other = (IwCentralAisle) object;
		if ((this.iwCentralAisleId == null && other.iwCentralAisleId != null) || (this.iwCentralAisleId != null && !this.iwCentralAisleId.equals(other.iwCentralAisleId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwCentralAisle[ iwCentralAisleId=" + iwCentralAisleId + " ]";
	}
}
