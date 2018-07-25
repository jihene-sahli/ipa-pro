/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author ali
 */
@Entity
@Table(name = "IW_COLUMN_TASK")
public class IwColumnTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_COLUMN_TASK_ID", nullable = false)
    private Long iwColumnTaskId;

    @Size(max = 45)
    @Column(name = "IW_NAME", length = 45)
    private String iwName;

	@Size(max = 45)
	@Column(name = "IW_COLUMN_KEY", length = 45)
	private String iwColumnKey;

	@Column(name = "IW_IS_COLUMN_HISTORIQUE")
    private Boolean iwIsColumnHistorique;

    @OneToMany(mappedBy = "iwColumnTask")
    private List<IwVariableProcess> iwVariableProcessList;

    public IwColumnTask() {
    }

    public IwColumnTask(Long iwColumnTaskId) {
        this.iwColumnTaskId = iwColumnTaskId;
    }

    public Long getIwColumnTaskId() {
        return iwColumnTaskId;
    }

    public void setIwColumnTaskId(Long iwColumnTaskId) {
        this.iwColumnTaskId = iwColumnTaskId;
    }

    public String getIwName() {
        return iwName;
    }

	public String getIwColumnKey() {
		return iwColumnKey;
	}

	public void setIwColumnKey(String iwColumnKey) {
		this.iwColumnKey = iwColumnKey;
	}

	public void setIwName(String iwName) {
        this.iwName = iwName;
    }

    public List<IwVariableProcess> getIwVariableProcessList() {
        return iwVariableProcessList;
    }

    public void setIwVariableProcessList(List<IwVariableProcess> iwVariableProcessList) {
        this.iwVariableProcessList = iwVariableProcessList;
    }

	public Boolean getIwIsColumnHistorique() {
		return iwIsColumnHistorique;
	}

	public void setIwIsColumnHistorique(Boolean iwIsColumnHistorique) {
		this.iwIsColumnHistorique = iwIsColumnHistorique;
	}

    @Override
    public int hashCode() {
        int hash = 0;
		hash += (iwColumnTaskId != null ? iwColumnTaskId.hashCode() : 0);
		return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwColumnTask)) {
            return false;
        }
        IwColumnTask other = (IwColumnTask) object;
        if ((this.iwColumnTaskId == null && other.iwColumnTaskId != null) || (this.iwColumnTaskId != null && !this.iwColumnTaskId.equals(other.iwColumnTaskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwColumnTask[ iwColumnTaskId=" + iwColumnTaskId + " ]";
    }

}
