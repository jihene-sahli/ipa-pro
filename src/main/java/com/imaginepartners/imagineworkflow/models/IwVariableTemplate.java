package com.imaginepartners.imagineworkflow.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "IW_VARIABLE_TEMPLATE")
public class IwVariableTemplate implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@Basic(optional = false)
	@Column(name = "IW_VARIABLE_REAL_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long iwvariablerealid;

    @Column(name = "IW_VARIABLE_ID", nullable = false)
    private String id;

	@JoinColumn(name = "IW_FORM_TEMPLATE", referencedColumnName = "IW_FORM_TEMPLATE_ID", nullable = false)
	@ManyToOne(optional = false)
	private IwFormTemplate iwFormTemplate;


    @Size(min = 1, max = 250)
    @Column(name = "IW_LABEL", length = 250)
    private String label;

    @Size(min = 1, max = 512)
    @Column(name = "IW_DESCRIPTION", length = 512)
    private String description;



    public IwVariableTemplate() {
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwvariablerealid != null ? iwvariablerealid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwVariableTemplate)) {
            return false;
        }
        IwVariableTemplate other = (IwVariableTemplate) object;
        if ((this.iwvariablerealid == null && other.iwvariablerealid != null) || (this.iwvariablerealid != null && !this.iwvariablerealid.equals(other.iwvariablerealid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwVariableTemplate[ id=" + iwvariablerealid + " ]";
    }

	public Long getIwvariablerealid() {
		return iwvariablerealid;
	}

	public void setIwvariablerealid(Long iwvariablerealid) {
		this.iwvariablerealid = iwvariablerealid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IwFormTemplate getIwFormTemplate() {
		return iwFormTemplate;
	}

	public void setIwFormTemplate(IwFormTemplate iwFormTemplate) {
		this.iwFormTemplate = iwFormTemplate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
