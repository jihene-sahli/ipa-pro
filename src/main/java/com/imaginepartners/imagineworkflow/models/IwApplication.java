/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.validation.constraints.Size;

/**
 *
 * @author ali
 */
@Entity
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "IW_APPLICATION")
@NamedQueries({
    @NamedQuery(name = "IwApplication.findAll", query = "SELECT i FROM IwApplication i"),
    @NamedQuery(name = "IwApplication.findByIwApplicationId", query = "SELECT i FROM IwApplication i WHERE i.iwApplicationId = :iwApplicationId"),
    @NamedQuery(name = "IwApplication.findByIwColor", query = "SELECT i FROM IwApplication i WHERE i.iwColor = :iwColor"),
    @NamedQuery(name = "IwApplication.findByIwDate", query = "SELECT i FROM IwApplication i WHERE i.iwDate = :iwDate"),
    @NamedQuery(name = "IwApplication.findByIwDescription", query = "SELECT i FROM IwApplication i WHERE i.iwDescription = :iwDescription"),
    @NamedQuery(name = "IwApplication.findByIwIcon", query = "SELECT i FROM IwApplication i WHERE i.iwIcon = :iwIcon"),
    @NamedQuery(name = "IwApplication.findByIwKey", query = "SELECT i FROM IwApplication i WHERE i.iwKey = :iwKey"),
    @NamedQuery(name = "IwApplication.findByIwName", query = "SELECT i FROM IwApplication i WHERE i.iwName = :iwName")})
public class IwApplication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IW_APPLICATION_ID")
    private Long iwApplicationId;
    @Size(max = 255)
    @Column(name = "IW_COLOR")
    private String iwColor;
    @Column(name = "IW_DATE")
    @Temporal(TemporalType.DATE)
    private Date iwDate;
    @Size(max = 255)
    @Column(name = "IW_DESCRIPTION")
    private String iwDescription;
    @Size(max = 255)
    @Column(name = "IW_ICON")
    private String iwIcon;
    @Size(max = 255)
    @Column(name = "IW_KEY")
    private String iwKey;
    @Size(max = 255)
    @Column(name = "IW_NAME")
    private String iwName;

	// Add an index for listing vues, so we can implement drag and drop reorder
	@Column(name = "IW_LISTING_INDEX")
	private Integer iwListingIndex;

    public IwApplication() {
    }

    public IwApplication(Long iwApplicationId) {
        this.iwApplicationId = iwApplicationId;
    }

    public Long getIwApplicationId() {
        return iwApplicationId;
    }

    public void setIwApplicationId(Long iwApplicationId) {
        this.iwApplicationId = iwApplicationId;
    }

    public String getIwColor() {
        return iwColor;
    }

    public void setIwColor(String iwColor) {
        this.iwColor = iwColor;
    }

    public Date getIwDate() {
        return iwDate;
    }

    public void setIwDate(Date iwDate) {
        this.iwDate = iwDate;
    }

    public String getIwDescription() {
        return iwDescription;
    }

    public void setIwDescription(String iwDescription) {
        this.iwDescription = iwDescription;
    }

    public String getIwIcon() {
        return iwIcon;
    }

    public void setIwIcon(String iwIcon) {
        this.iwIcon = iwIcon;
    }

    public String getIwKey() {
        return iwKey;
    }

    public void setIwKey(String iwKey) {
        this.iwKey = iwKey;
    }

    public String getIwName() {
        return iwName;
    }

    public void setIwName(String iwName) {
        this.iwName = iwName;
    }

	// Getter and setter for the index in vue needing a drag and drop reorder
	public Integer getIwListingIndex() {
		return iwListingIndex;
	}
	public void setIwListingIndex(Integer iwListingIndex) {
		this.iwListingIndex = iwListingIndex;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwApplicationId != null ? iwApplicationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwApplication)) {
            return false;
        }
        IwApplication other = (IwApplication) object;
        if ((this.iwApplicationId == null && other.iwApplicationId != null) || (this.iwApplicationId != null && !this.iwApplicationId.equals(other.iwApplicationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwApplication[ iwApplicationId=" + iwApplicationId + " ]";
    }

}
