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
@Table(name = "IW_LOAN")
@NamedQueries({
	@NamedQuery(name = "IwLoan.findAll", query = "SELECT i FROM IwLoan i"),
	@NamedQuery(name = "IwLoan.findByIwLoanId", query = "SELECT i FROM IwLoan i WHERE i.iwLoanId = :iwLoanId"),
	@NamedQuery(name = "IwLoan.findByIwAllDay", query = "SELECT i FROM IwLoan i WHERE i.iwAllDay = :iwAllDay"),
	@NamedQuery(name = "IwLoan.findByIwEndTime", query = "SELECT i FROM IwLoan i WHERE i.iwEndTime = :iwEndTime"),
	@NamedQuery(name = "IwLoan.findByIwStartTime", query = "SELECT i FROM IwLoan i WHERE i.iwStartTime = :iwStartTime"),
	@NamedQuery(name = "IwLoan.findByIwStatus", query = "SELECT i FROM IwLoan i WHERE i.iwStatus = :iwStatus"),
	@NamedQuery(name = "IwLoan.findByIwTenant", query = "SELECT i FROM IwLoan i WHERE i.iwTenant = :iwTenant")
})
public class IwLoan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_LOAN_ID", nullable = false)
	private Long iwLoanId;

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

	@JoinColumn(name = "IW_CAR", referencedColumnName = "IW_CAR_ID", nullable = false)
	@ManyToOne(optional = false)
	private IwCar iwCar;

	public IwLoan() {
	}

	public IwLoan(Long iwLoanId) {
		this.iwLoanId = iwLoanId;
	}

	public IwLoan(Long iwLoanId, boolean iwAllDay, Date iwEndTime, Date iwStartTime, String iwStatus, String iwTenant) {
		this.iwLoanId = iwLoanId;
		this.iwAllDay = iwAllDay;
		this.iwEndTime = iwEndTime;
		this.iwStartTime = iwStartTime;
		this.iwStatus = iwStatus;
		this.iwTenant = iwTenant;
	}

	public Long getIwLoanId() {
		return iwLoanId;
	}

	public void setIwLoanId(Long iwLoanId) {
		this.iwLoanId = iwLoanId;
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

	public IwCar getIwCar() {
		return iwCar;
	}

	public void setIwCar(IwCar iwCar) {
		this.iwCar = iwCar;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (iwLoanId != null ? iwLoanId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwLoan)) {
			return false;
		}
		IwLoan other = (IwLoan) object;
		if ((this.iwLoanId == null && other.iwLoanId != null) || (this.iwLoanId != null && !this.iwLoanId.equals(other.iwLoanId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwLoan[ iwLoanId=" + iwLoanId + " ]";
	}
}
