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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "IW_VARIABLE_PROCESS")
@NamedQueries({
    @NamedQuery(name = "IwVariableProcess.findAll", query = "SELECT i FROM IwVariableProcess i")})
public class IwVariableProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_VARIABLE_PROCESS_ID", nullable = false)
    private Long iwVariableProcessId;
    @Column(name = "IW_INDEX")
    private Integer iwIndex;
    @Column(name = "IW_INDEX_COLONNE")
    private Integer iwIndexColonne;
    @Size(max = 255)
    @Column(name = "IW_PROCESS_KEY", length = 255)
    private String iwProcessKey;
    @Column(name = "IW_SHOW_TASK_FORM")
    private Boolean iwShowTaskForm;
    @Column(name = "IW_SHOW_TASK_LIST")
    private Boolean iwShowTaskList;
	@Column(name = "IW_COLUMN_SIZE")
	private Long iwColumnSize;
    @JoinColumn(name = "IW_COLUMN_TASK", referencedColumnName = "IW_COLUMN_TASK_ID")
    @ManyToOne
    private IwColumnTask iwColumnTask;
    @JoinColumn(name = "IW_INPUT", referencedColumnName = "IW_INPUT_REAL_ID")
    @ManyToOne
    private IwInput iwInput;
    @OneToMany(mappedBy = "iwVariableProcess")
    private List<IwInput> iwInputList;

    public IwVariableProcess() {
    }

    public IwVariableProcess(Long iwVariableProcessId) {
        this.iwVariableProcessId = iwVariableProcessId;
    }

    public Long getIwVariableProcessId() {
        return iwVariableProcessId;
    }

    public void setIwVariableProcessId(Long iwVariableProcessId) {
        this.iwVariableProcessId = iwVariableProcessId;
    }

    public Integer getIwIndex() {
        return iwIndex;
    }

    public void setIwIndex(Integer iwIndex) {
        this.iwIndex = iwIndex;
    }

    public Integer getIwIndexColonne() {
        return iwIndexColonne;
    }

    public void setIwIndexColonne(Integer iwIndexColonne) {
        this.iwIndexColonne = iwIndexColonne;
    }

    public String getIwProcessKey() {
        return iwProcessKey;
    }

    public void setIwProcessKey(String iwProcessKey) {
        this.iwProcessKey = iwProcessKey;
    }

    public Boolean getIwShowTaskForm() {
        return iwShowTaskForm;
    }

    public void setIwShowTaskForm(Boolean iwShowTaskForm) {
        this.iwShowTaskForm = iwShowTaskForm;
    }

    public Boolean getIwShowTaskList() {
        return iwShowTaskList;
    }

    public void setIwShowTaskList(Boolean iwShowTaskList) {
        this.iwShowTaskList = iwShowTaskList;
    }

    public IwColumnTask getIwColumnTask() {
        return iwColumnTask;
    }

    public void setIwColumnTask(IwColumnTask iwColumnTask) {
        this.iwColumnTask = iwColumnTask;
    }

    public IwInput getIwInput() {
        return iwInput;
    }

    public void setIwInput(IwInput iwInput) {
        this.iwInput = iwInput;
    }

    public List<IwInput> getIwInputList() {
        return iwInputList;
    }

    public void setIwInputList(List<IwInput> iwInputList) {
        this.iwInputList = iwInputList;
    }

	public Long getIwColumnSize() {
		return iwColumnSize;
	}

	public void setIwColumnSize(Long iwColumnSize) {
		this.iwColumnSize = iwColumnSize;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (iwVariableProcessId != null ? iwVariableProcessId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwVariableProcess)) {
            return false;
        }
        IwVariableProcess other = (IwVariableProcess) object;
        if ((this.iwVariableProcessId == null && other.iwVariableProcessId != null) || (this.iwVariableProcessId != null && !this.iwVariableProcessId.equals(other.iwVariableProcessId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwVariableProcess[ iwVariableProcessId=" + iwVariableProcessId + " ]";
    }

}
