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
@Table(name = "IW_RECORDING_STUDIO")
@NamedQueries({
	@NamedQuery(name = "IwRecordingStudio.findAll", query = "SELECT i FROM IwRecordingStudio i"),
	@NamedQuery(name = "IwRecordingStudio.findByIwRecordingStudioId", query = "SELECT i FROM IwRecordingStudio i WHERE i.iwRecordingStudioId = :iwRecordingStudioId"),
	@NamedQuery(name = "IwRecordingStudio.findByIwIntitule", query = "SELECT i FROM IwRecordingStudio i WHERE i.iwIntitule = :iwIntitule"),
	@NamedQuery(name = "IwRecordingStudio.findByIwDescription", query = "SELECT i FROM IwRecordingStudio i WHERE i.iwDescription = :iwDescription")
})
public class IwRecordingStudio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_RECORDING_STUDIO_ID", nullable = false)
	private Integer iwRecordingStudioId;

	@Size(max = 45)
	@Column(name = "IW_INTITULE", length = 45)
	private String iwIntitule;

	@Size(max = 45)
	@Column(name = "IW_DESCRIPTION", length = 45)
	private String iwDescription;

	public IwRecordingStudio() {
	}

	public IwRecordingStudio(Integer iwRecordingStudioId) {
		this.iwRecordingStudioId = iwRecordingStudioId;
	}

	public Integer getIwRecordingStudioId() {
		return iwRecordingStudioId;
	}

	public void setIwRecordingStudioId(Integer iwRecordingStudioId) {
		this.iwRecordingStudioId = iwRecordingStudioId;
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
		hash += (iwRecordingStudioId != null ? iwRecordingStudioId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwRecordingStudio)) {
			return false;
		}
		IwRecordingStudio other = (IwRecordingStudio) object;
		if ((this.iwRecordingStudioId == null && other.iwRecordingStudioId != null) || (this.iwRecordingStudioId != null && !this.iwRecordingStudioId.equals(other.iwRecordingStudioId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwRecordingStudio[ iwRecordingStudioId=" + iwRecordingStudioId + " ]";
	}
}
