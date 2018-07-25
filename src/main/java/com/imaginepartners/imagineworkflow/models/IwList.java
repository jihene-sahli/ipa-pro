package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_LIST")
@NamedQueries({
	@NamedQuery(name = "IwList.findAll", query = "SELECT i FROM IwList i"),
	@NamedQuery(name = "IwList.findByIwListId", query = "SELECT i FROM IwList i WHERE i.iwListId = :iwListId"),
	@NamedQuery(name = "IwList.findByIwName", query = "SELECT i FROM IwList i WHERE i.iwName = :iwName"),
	@NamedQuery(name = "IwList.findByIwTitle", query = "SELECT i FROM IwList i WHERE i.iwTitle = :iwTitle"),
	@NamedQuery(name = "IwList.findByIwDescirption", query = "SELECT i FROM IwList i WHERE i.iwDescirption = :iwDescirption"),
	@NamedQuery(name = "IwList.findByIwPostedDate", query = "SELECT i FROM IwList i WHERE i.iwPostedDate = :iwPostedDate")
})
public class IwList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_LIST_ID", nullable = false)
	private Long iwListId;

	@Size(max = 50)
	@Column(name = "IW_NAME", length = 50)
	private String iwName;

	@Size(max = 45)
	@Column(name = "IW_TITLE", length = 45)
	private String iwTitle;

	@Size(max = 45)
	@Column(name = "IW_DESCIRPTION", length = 45)
	private String iwDescirption;

	@Column(name = "IW_POSTED_DATE")
	@Temporal(TemporalType.DATE)
	private Date iwPostedDate;

	@OneToMany(mappedBy = "iwListId", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<IwListOptions> iwListOptionsList;

	@OneToMany(mappedBy = "iwList")
	private List<IwMultilineEntityField> iwMultilineEntityFieldList;

	public IwList() {
	}

	public IwList(Long iwListId) {
		this.iwListId = iwListId;
	}

	public Long getIwListId() {
		return iwListId;
	}

	public void setIwListId(Long iwListId) {
		this.iwListId = iwListId;
	}

	public String getIwName() {
		return iwName;
	}

	public void setIwName(String iwName) {
		this.iwName = iwName;
	}

	public String getIwTitle() {
		return iwTitle;
	}

	public void setIwTitle(String iwTitle) {
		this.iwTitle = iwTitle;
	}

	public String getIwDescirption() {
		return iwDescirption;
	}

	public void setIwDescirption(String iwDescirption) {
		this.iwDescirption = iwDescirption;
	}

	public Date getIwPostedDate() {
		return iwPostedDate;
	}

	public void setIwPostedDate(Date iwPostedDate) {
		this.iwPostedDate = iwPostedDate;
	}

	public List<IwListOptions> getIwListOptionsList() {

		return iwListOptionsList;
	}

	public void setIwListOptionsList(List<IwListOptions> iwListOptionsList) {
		this.iwListOptionsList = iwListOptionsList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwListId != null ? iwListId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwList)) {
			return false;
		}
		IwList other = (IwList) object;
		if ((this.iwListId == null && other.iwListId != null) || (this.iwListId != null && !this.iwListId.equals(other.iwListId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwList[ iwListId=" + iwListId + " ]";
	}

	public List<IwMultilineEntityField> getIwMultilineEntityFieldList() {
		return iwMultilineEntityFieldList;
	}

	public void setIwMultilineEntityFieldList(List<IwMultilineEntityField> iwMultilineEntityFieldList) {
		this.iwMultilineEntityFieldList = iwMultilineEntityFieldList;
	}
}
