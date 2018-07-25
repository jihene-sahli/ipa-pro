package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_LIST_OPTIONS")
@NamedQueries({
	@NamedQuery(name = "IwListOptions.findAll", query = "SELECT i FROM IwListOptions i"),
	@NamedQuery(name = "IwListOptions.findByIwListOptionsId", query = "SELECT i FROM IwListOptions i WHERE i.iwListOptionsId = :iwListOptionsId"),
	@NamedQuery(name = "IwListOptions.findByIwName", query = "SELECT i FROM IwListOptions i WHERE i.iwName = :iwName"),
	@NamedQuery(name = "IwListOptions.findByIwDescirption", query = "SELECT i FROM IwListOptions i WHERE i.iwDescirption = :iwDescirption")
})
public class IwListOptions implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_LIST_OPTIONS_ID", nullable = false)
	private Integer iwListOptionsId;

	@Size(max = 50)
	@Column(name = "IW_NAME", length = 50)
	private String iwName;

	@Size(max = 500)
	@Column(name = "IW_DESCIRPTION", length = 500)
	private String iwDescirption;

	@JoinColumn(name = "IW_LIST_ID", referencedColumnName = "IW_LIST_ID")
	@ManyToOne
	private IwList iwListId;

	public IwListOptions() {
	}

	public IwListOptions(Integer iwListOptionsId) {
		this.iwListOptionsId = iwListOptionsId;
	}

	public Integer getIwListOptionsId() {
		return iwListOptionsId;
	}

	public void setIwListOptionsId(Integer iwListOptionsId) {
		this.iwListOptionsId = iwListOptionsId;
	}

	public String getIwName() {
		return iwName;
	}

	public void setIwName(String iwName) {
		this.iwName = iwName;
	}

	public String getIwDescirption() {
		return iwDescirption;
	}

	public void setIwDescirption(String iwDescirption) {
		this.iwDescirption = iwDescirption;
	}

	public IwList getIwListId() {
		return iwListId;
	}

	public void setIwListId(IwList iwListId) {
		this.iwListId = iwListId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwListOptionsId != null ? iwListOptionsId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwListOptions)) {
			return false;
		}
		IwListOptions other = (IwListOptions) object;
		if ((this.iwListOptionsId == null && other.iwListOptionsId != null) || (this.iwListOptionsId != null && !this.iwListOptionsId.equals(other.iwListOptionsId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwListOptions[ iwListOptionsId=" + iwListOptionsId + " ]";
	}
}
