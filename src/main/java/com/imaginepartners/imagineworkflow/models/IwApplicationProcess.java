/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ali.kolai
 */
@Entity
@Table(name = "IW_APPLICATION_PROCESS")
public class IwApplicationProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IW_APPlICATION_PROCESS_ID", nullable = false)
    private Long iwApplicationProcessId;

    @Column(name = "IW_APPlICATION_KEY", length = 50)
    private String iwApplicationKey;

    @Column(name = "IW_PROCESS_KEY", length = 50)
    private String iwProcessKey;

    public IwApplicationProcess() {
    }

    public Long getIwApplicationProcessId() {
        return iwApplicationProcessId;
    }

    public void setIwApplicationProcessId(Long iwApplicationProcessId) {
        this.iwApplicationProcessId = iwApplicationProcessId;
    }

    public String getIwApplicationKey() {
        return iwApplicationKey;
    }

    public void setIwApplicationKey(String iwApplicationKey) {
        this.iwApplicationKey = iwApplicationKey;
    }

    public String getIwProcessKey() {
        return iwProcessKey;
    }

    public void setIwProcessKey(String iwProcessKey) {
        this.iwProcessKey = iwProcessKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwApplicationProcessId != null ? iwApplicationProcessId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwApplicationProcess)) {
            return false;
        }
        IwApplicationProcess other = (IwApplicationProcess) object;
        if ((this.iwApplicationProcessId == null && other.iwApplicationProcessId != null) || (this.iwApplicationProcessId != null && !this.iwApplicationProcessId.equals(other.iwApplicationProcessId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwApplicationProcess[ iwApplicationProcessId=" + iwApplicationProcessId + " ]";
    }

}
