package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_RESOURCE_TYPE")
@NamedQueries({
	@NamedQuery(name = "IwResourceType.findAll", query = "SELECT i FROM IwResourceType i"),
	@NamedQuery(name = "IwResourceType.findByResourceTypeId", query = "SELECT i FROM IwResourceType i WHERE i.resourceTypeId = :resourceTypeId"),
	@NamedQuery(name = "IwResourceType.findByIwEntityName", query = "SELECT i FROM IwResourceType i WHERE i.iwEntityName = :iwEntityName"),
	@NamedQuery(name = "IwResourceType.findByIwDescription", query = "SELECT i FROM IwResourceType i WHERE i.iwDescription = :iwDescription")
})
public class IwResourceType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "RESOURCE_TYPE_ID", nullable = false)
	private Integer resourceTypeId;

	@Size(max = 50)
	@Column(name = "IW_ENTITY_NAME", length = 50)
	private String iwEntityName;

	@Size(max = 50)
	@Column(name = "IW_DESCRIPTION", length = 50)
	private String iwDescription;

	@OneToMany(mappedBy = "iwEntityName")
	private List<IwResource> iwResourceList;

	public IwResourceType() {
	}

	public IwResourceType(Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Integer getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getIwEntityName() {
		return iwEntityName;
	}

	public void setIwEntityName(String iwEntityName) {
		this.iwEntityName = iwEntityName;
	}

	public String getIwDescription() {
		return iwDescription;
	}

	public void setIwDescription(String iwDescription) {
		this.iwDescription = iwDescription;
	}

	public List<IwResource> getIwResourceList() {
		return iwResourceList;
	}

	public void setIwResourceList(List<IwResource> iwResourceList) {
		this.iwResourceList = iwResourceList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (resourceTypeId != null ? resourceTypeId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwResourceType)) {
			return false;
		}
		IwResourceType other = (IwResourceType) object;
		if ((this.resourceTypeId == null && other.resourceTypeId != null) || (this.resourceTypeId != null && !this.resourceTypeId.equals(other.resourceTypeId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwResourceType[ resourceTypeId=" + resourceTypeId + " ]";
	}
}
