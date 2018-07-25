package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IW_AGENDA")
@NamedQueries({
	@NamedQuery(name = "IwAgenda.findAll", query = "SELECT i FROM IwAgenda i"),
	@NamedQuery(name = "IwAgenda.findByIwAgendaId", query = "SELECT i FROM IwAgenda i WHERE i.iwAgendaId = :iwAgendaId"),
	@NamedQuery(name = "IwAgenda.findByIwTitle", query = "SELECT i FROM IwAgenda i WHERE i.iwTitle = :iwTitle"),
	@NamedQuery(name = "IwAgenda.findByIwStartDate", query = "SELECT i FROM IwAgenda i WHERE i.iwStartDate = :iwStartDate"),
	@NamedQuery(name = "IwAgenda.findByIwEndDate", query = "SELECT i FROM IwAgenda i WHERE i.iwEndDate = :iwEndDate"),
	@NamedQuery(name = "IwAgenda.findByIwDescription", query = "SELECT i FROM IwAgenda i WHERE i.iwDescription = :iwDescription"),
	@NamedQuery(name = "IwAgenda.findByIwColor", query = "SELECT i FROM IwAgenda i WHERE i.iwColor = :iwColor")
})
public class IwAgenda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IW_AGENDA_ID", nullable = false)
	private Long iwAgendaId;

	@Column(name = "IW_TITLE", length = 50)
	private String iwTitle;

	@Column(name = "IW_START_DATE")
	@Temporal(TemporalType.DATE)
	private Date iwStartDate;

	@Column(name = "IW_END_DATE")
	@Temporal(TemporalType.DATE)
	private Date iwEndDate;

	@Column(name = "IW_DESCRIPTION", length = 500)
	private String iwDescription;

	@Column(name = "IW_COLOR", length = 50)
	private String iwColor;

	public IwAgenda() {
	}

	public IwAgenda(Long iwAgendaId) {
		this.iwAgendaId = iwAgendaId;
	}

	public Long getIwAgendaId() {
		return iwAgendaId;
	}

	public void setIwAgendaId(Long iwAgendaId) {
		this.iwAgendaId = iwAgendaId;
	}

	public String getIwTitle() {
		return iwTitle;
	}

	public void setIwTitle(String iwTitle) {
		this.iwTitle = iwTitle;
	}

	public Date getIwStartDate() {
		return iwStartDate;
	}

	public void setIwStartDate(Date iwStartDate) {
		this.iwStartDate = iwStartDate;
	}

	public Date getIwEndDate() {
		return iwEndDate;
	}

	public void setIwEndDate(Date iwEndDate) {
		this.iwEndDate = iwEndDate;
	}

	public String getIwDescription() {
		return iwDescription;
	}

	public void setIwDescription(String iwDescription) {
		this.iwDescription = iwDescription;
	}

	public String getIwColor() {
		return iwColor;
	}

	public void setIwColor(String iwColor) {
		this.iwColor = iwColor;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwAgendaId != null ? iwAgendaId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwAgenda)) {
			return false;
		}
		IwAgenda other = (IwAgenda) object;
		if ((this.iwAgendaId == null && other.iwAgendaId != null) || (this.iwAgendaId != null && !this.iwAgendaId.equals(other.iwAgendaId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwAgenda[ iwAgendaId=" + iwAgendaId + " ]";
	}
}
