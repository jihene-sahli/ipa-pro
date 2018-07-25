package com.imaginepartners.imagineworkflow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_MULTILINE_ENTITY")
@NamedQueries({
	@NamedQuery(name = "IwMultilineEntity.findAll", query = "SELECT i FROM IwMultilineEntity i")
})
public class IwMultilineEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_MULTILINE_ENTITY_ID", nullable = false)
	private Long iwMultilineEntityId;

	@Size(max = 255)
	@Column(name = "IW_CLASS")
	private String iwClass;

	@Size(max = 255)
	@Column(name = "IW_NAME")
	private String iwName;

	@JsonIgnore
	@OneToMany(mappedBy = "iwMultilineEntity")
	private Set<IwMultilineEntityField> iwMultilineEntityFieldList;

	public IwMultilineEntity() {
	}

	public IwMultilineEntity(Long iwMultilineEntityId) {
		this.iwMultilineEntityId = iwMultilineEntityId;
	}

	public Long getIwMultilineEntityId() {
		return iwMultilineEntityId;
	}

	public void setIwMultilineEntityId(Long iwMultilineEntityId) {
		this.iwMultilineEntityId = iwMultilineEntityId;
	}

	public String getIwClass() {
		return iwClass;
	}

	public void setIwClass(String iwClass) {
		this.iwClass = iwClass;
	}

	public String getIwName() {
		return iwName;
	}

	public void setIwName(String iwName) {
		this.iwName = iwName;
	}

	public Set<IwMultilineEntityField> getIwMultilineEntityFieldList() {
		return iwMultilineEntityFieldList;
	}

	public void setIwMultilineEntityFieldList(Set<IwMultilineEntityField> iwMultilineEntityFieldList) {
		this.iwMultilineEntityFieldList = iwMultilineEntityFieldList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwMultilineEntityId != null ? iwMultilineEntityId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwMultilineEntity)) {
			return false;
		}
		IwMultilineEntity other = (IwMultilineEntity) object;
		if ((this.iwMultilineEntityId == null && other.iwMultilineEntityId != null) || (this.iwMultilineEntityId != null && !this.iwMultilineEntityId.equals(other.iwMultilineEntityId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwMultilineEntity[ iwMultilineEntityId=" + iwMultilineEntityId + " ]";
	}
}
