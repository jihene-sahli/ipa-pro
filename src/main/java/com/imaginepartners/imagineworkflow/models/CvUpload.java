package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CV_UPLOAD")
@NamedQueries({
	@NamedQuery(name = "CvUpload.findAll", query = "SELECT c FROM CvUpload c"),
	@NamedQuery(name = "CvUpload.findByIdPerson", query = "SELECT c FROM CvUpload c WHERE c.idPerson = :idPerson"),
	@NamedQuery(name = "CvUpload.findByNom", query = "SELECT c FROM CvUpload c WHERE c.nom = :nom"),
	@NamedQuery(name = "CvUpload.findByPrenom", query = "SELECT c FROM CvUpload c WHERE c.prenom = :prenom"),
	@NamedQuery(name = "CvUpload.findByIwuploadid", query = "SELECT c FROM CvUpload c WHERE c.iwuploadid = :iwuploadid")
})
public class CvUpload implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ID_PERSON", nullable = false)
	private Integer idPerson;

	@Size(max = 50)
	@Column(name = "NOM", length = 50)
	private String nom;

	@Size(max = 50)
	@Column(name = "PRENOM", length = 50)
	private String prenom;

	@JoinColumn(name = "IWUPLOADID", referencedColumnName = "IW_UPLOAD_ID")
	@OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)

	private IwUpload iwuploadid;

	public CvUpload() {
	}

	public CvUpload(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public Integer getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public IwUpload getIwuploadid() {
		return iwuploadid;
	}

	public void setIwuploadid(IwUpload iwuploadid) {
		this.iwuploadid = iwuploadid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPerson != null ? idPerson.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CvUpload)) {
			return false;
		}
		CvUpload other = (CvUpload) object;
		if ((this.idPerson == null && other.idPerson != null) || (this.idPerson != null && !this.idPerson.equals(other.idPerson))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.CvUpload[ idPerson=" + idPerson + " ]";
	}
}
