package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.identity.Group;

public class GroupFormWrapper extends FormWrapper {

	private List<Group> groups;

	public GroupFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public GroupFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (this.value != null) {
			this.groups = activitiService.getGroupListIn((List< String>) value);
		}
	}

	@Override
	public Object getValue() {
		if (groups != null && !groups.isEmpty()) {
			value = new ArrayList<String>();
			for (Group grp : groups) {
				((ArrayList<String>) value).add(grp.getId());
			}
		}
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
