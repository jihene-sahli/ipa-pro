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

@Entity
@Table(name = "IW_TREE")
@NamedQueries({
	@NamedQuery(name = "IwTree.findAll", query = "SELECT i FROM IwTree i")
})
public class IwTree implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "CHILD", nullable = false)
	private Integer child;

	@Size(max = 50)
	@Column(name = "TITLE", length = 50)
	private String title;

	@Size(max = 500)
	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@OneToMany(mappedBy = "parent")
	private List<IwTree> iwTreeList;

	@JoinColumn(name = "PARENT", referencedColumnName = "CHILD")
	@ManyToOne
	private IwTree parent;

	public IwTree() {
	}

	public IwTree(Integer child) {
		this.child = child;
	}

	public Integer getChild() {
		return child;
	}

	public void setChild(Integer child) {
		this.child = child;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IwTree> getIwTreeList() {
		return iwTreeList;
	}

	public void setIwTreeList(List<IwTree> iwTreeList) {
		this.iwTreeList = iwTreeList;
	}

	public IwTree getParent() {
		return parent;
	}

	public void setParent(IwTree parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (child != null ? child.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof IwTree)) {
			return false;
		}
		IwTree other = (IwTree) object;
		if ((this.child == null && other.child != null) || (this.child != null && !this.child.equals(other.child))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.imaginepartners.imagineworkflow.models.IwTree[ child=" + child + " ]";
	}
}
