/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author ali.kolai
 */
@Entity
@Table(name = "IW_STANDALONE_TASK")
public class IwStandAloneTask implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IW_STANDALONE_TASK_ID", nullable = false)
	private Long iwStandAloneTaskId;
	@Column(name = "IW_NAME", length = 50)
	private String iwName;
	@Column(name = "IW_DESCRIPTION", length = 500)
	private String iwDescription;
	@Column(name = "IW_FORM")
	private Long iwFormId;
	@Column(name = "IW_LINK", length = 500)
	private String iwLink;
	@Column(name = "IW_APPLICATION_KEY", length = 50)
	private String iwApplicationKey;
	@Column(name = "IW_IS_FORM")
	private Boolean iwIsForm;

	public IwStandAloneTask() {
	}

	public Long getIwStandAloneTaskId() {
		return iwStandAloneTaskId;
	}

	public void setIwStandAloneTaskId(Long iwStandAloneTaskId) {
		this.iwStandAloneTaskId = iwStandAloneTaskId;
	}

	public String getIwName() {
		return iwName;
	}

	public void setIwName(String iwName) {
		this.iwName = iwName;
	}

	public String getIwDescription() {
		return iwDescription;
	}

	public void setIwDescription(String iwDescription) {
		this.iwDescription = iwDescription;
	}

	public void setIwFormId(Long iwFormId) {
		this.iwFormId = iwFormId;
	}

	public String getIwApplicationKey() {
		return iwApplicationKey;
	}

	public void setIwApplicationKey(String iwApplicationKey) {
		this.iwApplicationKey = iwApplicationKey;
	}

	public Boolean getIwIsForm() {
		return iwIsForm;
	}

	public void setIwIsForm(Boolean iwIsForm) {
		this.iwIsForm = iwIsForm;
	}

	public Long getIwFormId() {
		return iwFormId;
	}

	public String getIwLink() {
		return iwLink;
	}

	public void setIwLink(String iwLink) {
		this.iwLink = iwLink;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwStandAloneTaskId != null ? iwStandAloneTaskId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwStandAloneTask)) {
			return false;
		}
		IwStandAloneTask other = (IwStandAloneTask) object;
		if ((this.iwStandAloneTaskId == null && other.iwStandAloneTaskId != null) || (this.iwStandAloneTaskId != null && !this.iwStandAloneTaskId.equals(other.iwStandAloneTaskId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwStandAloneTask[ iwStandAloneTaskId=" + iwStandAloneTaskId + " ]";
	}

}
