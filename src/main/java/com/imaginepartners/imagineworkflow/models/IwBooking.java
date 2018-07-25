package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_BOOKING")
@NamedQueries({
	@NamedQuery(name = "IwBooking.findAll", query = "SELECT i FROM IwBooking i"),
	@NamedQuery(name = "IwBooking.findByIwBookingId", query = "SELECT i FROM IwBooking i WHERE i.iwBookingId = :iwBookingId"),
	@NamedQuery(name = "IwBooking.findByIwAllDay", query = "SELECT i FROM IwBooking i WHERE i.iwAllDay = :iwAllDay"),
	@NamedQuery(name = "IwBooking.findByIwEndTime", query = "SELECT i FROM IwBooking i WHERE i.iwEndTime = :iwEndTime"),
	@NamedQuery(name = "IwBooking.findByIwStartTime", query = "SELECT i FROM IwBooking i WHERE i.iwStartTime = :iwStartTime"),
	@NamedQuery(name = "IwBooking.findByIwStatus", query = "SELECT i FROM IwBooking i WHERE i.iwStatus = :iwStatus"),
	@NamedQuery(name = "IwBooking.findByIwTenant", query = "SELECT i FROM IwBooking i WHERE i.iwTenant = :iwTenant")
})
public class IwBooking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_BOOKING_ID", nullable = false, unique = true)
	private Long iwBookingId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "IW_ALL_DAY", nullable = false)
	private boolean iwAllDay;

	@Basic(optional = false)
	@NotNull
	@Column(name = "IW_END_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date iwEndTime;

	@Basic(optional = false)
	@NotNull
	@Column(name = "IW_START_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date iwStartTime;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "IW_STATUS", nullable = false, length = 64)
	private String iwStatus;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "IW_TENANT", nullable = false, length = 255)
	private String iwTenant;

	@JoinColumn(name = "IW_RESOURCE_ID", referencedColumnName = "ID_RESOURCE", nullable = false)
	@ManyToOne(optional = false)
	private IwResource iwResourceId;

	public IwBooking() {
	}

	public IwBooking(Long iwBookingId) {
		this.iwBookingId = iwBookingId;
	}

	public IwBooking(Long iwBookingId, boolean iwAllDay, Date iwEndTime, Date iwStartTime, String iwStatus, String iwTenant) {
		this.iwBookingId = iwBookingId;
		this.iwAllDay = iwAllDay;
		this.iwEndTime = iwEndTime;
		this.iwStartTime = iwStartTime;
		this.iwStatus = iwStatus;
		this.iwTenant = iwTenant;
	}

	public Long getIwBookingId() {
		return iwBookingId;
	}

	public void setIwBookingId(Long iwBookingId) {
		this.iwBookingId = iwBookingId;
	}

	public boolean getIwAllDay() {
		return iwAllDay;
	}

	public void setIwAllDay(boolean iwAllDay) {
		this.iwAllDay = iwAllDay;
	}

	public Date getIwEndTime() {
		return iwEndTime;
	}

	public void setIwEndTime(Date iwEndTime) {
		this.iwEndTime = iwEndTime;
	}

	public Date getIwStartTime() {
		return iwStartTime;
	}

	public void setIwStartTime(Date iwStartTime) {
		this.iwStartTime = iwStartTime;
	}

	public String getIwStatus() {
		return iwStatus;
	}

	public void setIwStatus(String iwStatus) {
		this.iwStatus = iwStatus;
	}

	public String getIwTenant() {
		return iwTenant;
	}

	public void setIwTenant(String iwTenant) {
		this.iwTenant = iwTenant;
	}

	public IwResource getIwResourceId() {
		return iwResourceId;
	}

	public void setIwResourceId(IwResource iwResourceId) {
		this.iwResourceId = iwResourceId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwBookingId != null ? iwBookingId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwBooking)) {
			return false;
		}
		IwBooking other = (IwBooking) object;
		if ((this.iwBookingId == null && other.iwBookingId != null) || (this.iwBookingId != null && !this.iwBookingId.equals(other.iwBookingId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwBooking[ iwBookingId=" + iwBookingId + " ]";
	}
}
