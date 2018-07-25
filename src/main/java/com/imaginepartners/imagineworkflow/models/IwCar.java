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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_CAR")
@NamedQueries({
	@NamedQuery(name = "IwCar.findAll", query = "SELECT i FROM IwCar i"),
	@NamedQuery(name = "IwCar.findByIwCarId", query = "SELECT i FROM IwCar i WHERE i.iwCarId = :iwCarId"),
	@NamedQuery(name = "IwCar.findByIwIntitule", query = "SELECT i FROM IwCar i WHERE i.iwIntitule = :iwIntitule"),
	@NamedQuery(name = "IwCar.findByIwDescription", query = "SELECT i FROM IwCar i WHERE i.iwDescription = :iwDescription")
})
public class IwCar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_CAR_ID", nullable = false)
	private Integer iwCarId;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "IW_INTITULE", nullable = false, length = 255)
	private String iwIntitule;

	@Size(max = 45)
	@Column(name = "IW_DESCRIPTION", length = 45)
	private String iwDescription;

	public IwCar() {
	}

	public IwCar(Integer iwCarId) {
		this.iwCarId = iwCarId;
	}

	public IwCar(Integer iwCarId, String iwIntitule) {
		this.iwCarId = iwCarId;
		this.iwIntitule = iwIntitule;
	}

	public Integer getIwCarId() {
		return iwCarId;
	}

	public void setIwCarId(Integer iwCarId) {
		this.iwCarId = iwCarId;
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
		hash += (iwCarId != null ? iwCarId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwCar)) {
			return false;
		}
		IwCar other = (IwCar) object;
		if ((this.iwCarId == null && other.iwCarId != null) || (this.iwCarId != null && !this.iwCarId.equals(other.iwCarId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwCar[ iwCarId=" + iwCarId + " ]";
	}
}
