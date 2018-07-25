package com.imaginepartners.imagineworkflow.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table(name = "IW_CONFIG", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CONFIG_NAME"})
})
@NamedQueries({
    @NamedQuery(name = "IwConfig.findAll", query = "SELECT i FROM IwConfig i"),
    @NamedQuery(name = "IwConfig.findByConfigName", query = "SELECT i FROM IwConfig i WHERE i.configName = :configName"),
    @NamedQuery(name = "IwConfig.findByConfigValue", query = "SELECT i FROM IwConfig i WHERE i.configValue = :configValue"),
    @NamedQuery(name = "IwConfig.findByConfigDescription", query = "SELECT i FROM IwConfig i WHERE i.configDescription = :configDescription")
})
public class IwConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	//----------------------------------------------------------------------
	// ENTITY PRIMARY KEY
	//----------------------------------------------------------------------
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Long configId;


	//----------------------------------------------------------------------
	// ENTITY DATA FIELDS
	//----------------------------------------------------------------------
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "CONFIG_NAME", nullable = false, length = 250)
    private String configName;

	@Size(max = 500)
	@Column(name = "CONFIG_DESCRIPTION", length = 500)
	private String configDescription;

    @Size(max = 500)
    @Column(name = "CONFIG_VALUE", length = 500)
    private String configValue;

	@Size(max = 500)
	@Column(name = "DEFAULT_VALUE", length = 500)
	private String defaultValue;

    @Column(name = "CONFIG_EDITABLE")
    private Boolean configEditable;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	//----------------------------------------------------------------------
	// CONSTRUCTOR(S)
	//----------------------------------------------------------------------
    public IwConfig() {

    }

    public IwConfig(String configName) {
        this.configName = configName;
    }

	//----------------------------------------------------------------------
	// GETTERS & SETTERS FOR FIELDS
	//----------------------------------------------------------------------
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public Boolean getConfigEditable() {
        return configEditable;
    }

    public void setConfigEditable(Boolean configEditable) {
        this.configEditable = configEditable;
    }

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	//----------------------------------------------------------------------
	// HOOKS
	//----------------------------------------------------------------------
	@PrePersist
	protected void onCreate() {
		updatedAt = createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configName != null ? configName.hashCode() : 0);
        return hash;
    }

    @Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (!(object instanceof IwConfig))
			return false;
		IwConfig other = (IwConfig) object;
		if (configId == null) {
			if (other.configId != null)
				return false;
		} else if (!configId.equals(other.configId))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwConfig[ configName=" + configName + " ]";
    }
}
