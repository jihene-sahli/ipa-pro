/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author rafik
 */
@Entity
@Table(name = "IW_NOTIFICATION", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"})})
public class IwNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Size(max = 255)
	@Column(name = "NAME")
	private String name;

	@Size(max = 255)
	@Column(name = "FROM_NAME")
	private String fromName;

	@Lob
	@Size(max = 65535)
	@Column(name = "TO_USER", length = 65535)
	private String toUser;

	@Lob
	@Size(max = 65535)
	@Column(name = "TO_ROLE", length = 65535)
	private String toRole;

	@Lob
	@Size(max = 65535)
	@Column(name = "TO_EMAIL", length = 65535)
	private String toEmail;
	@Size(max = 255)
	@Column(name = "SUBJECT")
	private String subject;

	@Lob
	@Size(max = 65535)
	@Column(name = "BODY", length = 65535)
	private String body;

	@Size(max = 65535)
	@Column(name = "TASKS", length = 65535)
	private String tasks;

	@Lob
	@Size(max = 65535)
	@Column(name = "ATTACHMENT", length = 65535)
	private String attachment;

	@Column(name = "ACTIVE")
	private Boolean active;

	@Column(name = "NOTIFIER_RESPONSABLE")
	private Boolean notifierResponsable;



	public IwNotification() {
    }

    public IwNotification(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	public Boolean getNotifierResponsable() {
		return notifierResponsable;
	}

	public void setNotifierResponsable(Boolean notifierResponsable) {
		this.notifierResponsable = notifierResponsable;
	}


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwNotification)) {
            return false;
        }
        IwNotification other = (IwNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwNotification[ id=" + id + " ]";
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getToRole() {
		return toRole;
	}

	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTasks() {
		return tasks;
	}

	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
