package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_HEAD_GONDOLA")
@NamedQueries({
	@NamedQuery(name = "IwHeadGondola.findAll", query = "SELECT i FROM IwHeadGondola i"),
	@NamedQuery(name = "IwHeadGondola.findByIwHeadGondolaId", query = "SELECT i FROM IwHeadGondola i WHERE i.iwHeadGondolaId = :iwHeadGondolaId"),
	@NamedQuery(name = "IwHeadGondola.findByIwIntitule", query = "SELECT i FROM IwHeadGondola i WHERE i.iwIntitule = :iwIntitule"),
	@NamedQuery(name = "IwHeadGondola.findByIwDescription", query = "SELECT i FROM IwHeadGondola i WHERE i.iwDescription = :iwDescription")
})
public class IwHeadGondola implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "IW_HEAD_GONDOLA_ID", nullable = false)
	private Integer iwHeadGondolaId;

	@Size(max = 45)
	@Column(name = "IW_INTITULE", length = 45)
	private String iwIntitule;

	@Size(max = 45)
	@Column(name = "IW_DESCRIPTION", length = 45)
	private String iwDescription;

	public IwHeadGondola() {
	}

	public IwHeadGondola(Integer iwHeadGondolaId) {
		this.iwHeadGondolaId = iwHeadGondolaId;
	}

	public Integer getIwHeadGondolaId() {
		return iwHeadGondolaId;
	}

	public void setIwHeadGondolaId(Integer iwHeadGondolaId) {
		this.iwHeadGondolaId = iwHeadGondolaId;
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
		hash += (iwHeadGondolaId != null ? iwHeadGondolaId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwHeadGondola)) {
			return false;
		}
		IwHeadGondola other = (IwHeadGondola) object;
		if ((this.iwHeadGondolaId == null && other.iwHeadGondolaId != null) || (this.iwHeadGondolaId != null && !this.iwHeadGondolaId.equals(other.iwHeadGondolaId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwHeadGondola[ iwHeadGondolaId=" + iwHeadGondolaId + " ]";
	}
}
