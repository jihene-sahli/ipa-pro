package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IW_MAIL")
@NamedQueries({
	@NamedQuery(name = "IwMail.findAll", query = "SELECT i FROM IwMail i"),
	@NamedQuery(name = "IwMail.findByIwMailId", query = "SELECT i FROM IwMail i WHERE i.iwMailId = :iwMailId"),
	@NamedQuery(name = "IwMail.findByIwSubject", query = "SELECT i FROM IwMail i WHERE i.iwSubject = :iwSubject"),
	@NamedQuery(name = "IwMail.findByIwSender", query = "SELECT i FROM IwMail i WHERE i.iwSender = :iwSender"),
	@NamedQuery(name = "IwMail.findByIwReciever", query = "SELECT i FROM IwMail i WHERE i.iwReciever = :iwReciever"),
	@NamedQuery(name = "IwMail.findByIwSendingDate", query = "SELECT i FROM IwMail i WHERE i.iwSendingDate = :iwSendingDate")
})
public class IwMail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "IW_MAIL_ID", nullable = false)
	private Long iwMailId;

	@Column(name = "IW_SUBJECT", length = 50)
	private String iwSubject;

	@Column(name = "IW_SENDER", length = 50)
	private String iwSender;

	@Column(name = "IW_RECIEVER", length = 50)
	private String iwReciever;

	@Lob
	@Column(name = "IW_CONTENT", length = 2147483647)
	private String iwContent;

	@Column(name = "IW_SENDING_DATE")
	@Temporal(TemporalType.DATE)
	private Date iwSendingDate;

	public IwMail() {
	}

	public IwMail(Long iwMailId) {
		this.iwMailId = iwMailId;
	}

	public Long getIwMailId() {
		return iwMailId;
	}

	public void setIwMailId(Long iwMailId) {
		this.iwMailId = iwMailId;
	}

	public String getIwSubject() {
		return iwSubject;
	}

	public void setIwSubject(String iwSubject) {
		this.iwSubject = iwSubject;
	}

	public String getIwSender() {
		return iwSender;
	}

	public void setIwSender(String iwSender) {
		this.iwSender = iwSender;
	}

	public String getIwReciever() {
		return iwReciever;
	}

	public void setIwReciever(String iwReciever) {
		this.iwReciever = iwReciever;
	}

	public String getIwContent() {
		return iwContent;
	}

	public void setIwContent(String iwContent) {
		this.iwContent = iwContent;
	}

	public Date getIwSendingDate() {
		return iwSendingDate;
	}

	public void setIwSendingDate(Date iwSendingDate) {
		this.iwSendingDate = iwSendingDate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwMailId != null ? iwMailId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwMail)) {
			return false;
		}
		IwMail other = (IwMail) object;
		if ((this.iwMailId == null && other.iwMailId != null) || (this.iwMailId != null && !this.iwMailId.equals(other.iwMailId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwMail[ iwMailId=" + iwMailId + " ]";
	}
}
