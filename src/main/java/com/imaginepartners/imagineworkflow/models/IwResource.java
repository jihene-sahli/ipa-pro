package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "IW_RESOURCE", uniqueConstraints = {
@UniqueConstraint(columnNames = {"IW_ENTITY_NAME", "IW_ENTITY_ID"})})
@NamedQueries({
	@NamedQuery(name = "IwResource.findAll", query = "SELECT i FROM IwResource i"),
	@NamedQuery(name = "IwResource.findByIdResource", query = "SELECT i FROM IwResource i WHERE i.idResource = :idResource"),
	@NamedQuery(name = "IwResource.findByIwEntityId", query = "SELECT i FROM IwResource i WHERE i.iwEntityId = :iwEntityId")
})
public class IwResource implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ID_RESOURCE", nullable = false, unique = true)
	private Integer idResource;

	@Column(name = "IW_ENTITY_ID")
	private Integer iwEntityId;

	@JoinColumn(name = "IW_ENTITY_NAME", referencedColumnName = "RESOURCE_TYPE_ID")
	@ManyToOne
	private IwResourceType iwEntityName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "iwResourceId")
	private List<IwBooking> iwBookingList;

	public IwResource() {
	}

	public IwResource(Integer idResource) {
		this.idResource = idResource;
	}

	public Integer getIdResource() {
		return idResource;
	}

	public void setIdResource(Integer idResource) {
		this.idResource = idResource;
	}

	public Integer getIwEntityId() {
		return iwEntityId;
	}

	public void setIwEntityId(Integer iwEntityId) {
		this.iwEntityId = iwEntityId;
	}

	public IwResourceType getIwEntityName() {
		return iwEntityName;
	}

	public void setIwEntityName(IwResourceType iwEntityName) {
		this.iwEntityName = iwEntityName;
	}

	public List<IwBooking> getIwBookingList() {
		return iwBookingList;
	}

	public void setIwBookingList(List<IwBooking> iwBookingList) {
		this.iwBookingList = iwBookingList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idResource != null ? idResource.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwResource)) {
			return false;
		}
		IwResource other = (IwResource) object;
		if ((this.idResource == null && other.idResource != null) || (this.idResource != null && !this.idResource.equals(other.idResource))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwResource[ idResource=" + idResource + " ]";
	}
}
