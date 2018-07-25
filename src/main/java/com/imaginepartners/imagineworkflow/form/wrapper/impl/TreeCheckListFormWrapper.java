package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class TreeCheckListFormWrapper extends FormWrapper {

	private TreeNode treeCheckList;

	private TreeNode[] selection;

	public TreeCheckListFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public TreeCheckListFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		selection = new TreeNode[]{new DefaultTreeNode()};
		treeCheckList = new DefaultTreeNode(this.iwInput.getLabel(), null);
		for (LinkedHashMap<String, Object> item : this.iwInput.getTreechecklistoptions()) {
			TreeNode itemNode = new DefaultTreeNode(item.get("title"), treeCheckList);
			itemNode.setSelectable(false);
			if (item.get("nodes") != null) {
				for (LinkedHashMap<String, Object> subItem : (List<LinkedHashMap<String, Object>>) item.get("nodes")) {
					TreeNode subItemNode = new DefaultTreeNode(((LinkedHashMap<String, Object>) subItem).get("title"), itemNode);
					if (this.value != null) {
						if (this.iwInput.getMultple()) {
							if (((ArrayList<String>) this.value).contains(subItem.get("title").toString())) {
								subItemNode.setSelected(true);
								subItemNode.setExpanded(true);
								itemNode.setExpanded(true);
							}
						} else if (this.value.equals(subItem.get("title").toString())) {
							subItemNode.setSelected(true);
							subItemNode.setExpanded(true);
							itemNode.setExpanded(true);
						}
					}
				}
			}
		}
	}

	@Override
	public Object getValue() {
		if (selection != null && selection.length != 0) {
			if (this.iwInput.getMultple()) {
				this.value = new ArrayList<String>();
				for (TreeNode item : selection) {
					if (item != null && item.getData() != null) {
						((ArrayList<String>) this.value).add(item.getData().toString());
					}
				}
			} else {
				this.value = String.valueOf(selection[0].getData());
			}
		}
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {

	}

	public TreeNode getTreeCheckList() {
		return treeCheckList;
	}

	public void setTreeCheckList(TreeNode treeCheckList) {
		this.treeCheckList = treeCheckList;
	}

	public TreeNode[] getSelection() {
		return selection;
	}

	public void setSelection(TreeNode[] selection) {
		this.selection = selection;
	}
}
