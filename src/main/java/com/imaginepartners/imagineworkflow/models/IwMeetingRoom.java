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
@Table(name = "IW_MEETING_ROOM")
@NamedQueries({
	@NamedQuery(name = "IwMeetingRoom.findAll", query = "SELECT i FROM IwMeetingRoom i"),
	@NamedQuery(name = "IwMeetingRoom.findByIwMeetingRoomId", query = "SELECT i FROM IwMeetingRoom i WHERE i.iwMeetingRoomId = :iwMeetingRoomId"),
	@NamedQuery(name = "IwMeetingRoom.findByIwIntitule", query = "SELECT i FROM IwMeetingRoom i WHERE i.iwIntitule = :iwIntitule"),
	@NamedQuery(name = "IwMeetingRoom.findByIwDescription", query = "SELECT i FROM IwMeetingRoom i WHERE i.iwDescription = :iwDescription")
})
public class IwMeetingRoom implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_MEETING_ROOM_ID", nullable = false)
	private Integer iwMeetingRoomId;

	@Size(max = 45)
	@Column(name = "IW_INTITULE", length = 45)
	private String iwIntitule;

	@Size(max = 45)
	@Column(name = "IW_DESCRIPTION", length = 45)
	private String iwDescription;

	public IwMeetingRoom() {
	}

	public IwMeetingRoom(Integer iwMeetingRoomId) {
		this.iwMeetingRoomId = iwMeetingRoomId;
	}

	public Integer getIwMeetingRoomId() {
		return iwMeetingRoomId;
	}

	public void setIwMeetingRoomId(Integer iwMeetingRoomId) {
		this.iwMeetingRoomId = iwMeetingRoomId;
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
		hash += (iwMeetingRoomId != null ? iwMeetingRoomId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwMeetingRoom)) {
			return false;
		}
		IwMeetingRoom other = (IwMeetingRoom) object;
		if ((this.iwMeetingRoomId == null && other.iwMeetingRoomId != null) || (this.iwMeetingRoomId != null && !this.iwMeetingRoomId.equals(other.iwMeetingRoomId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwMeetingRoom[ iwMeetingRoomId=" + iwMeetingRoomId + " ]";
	}
}
