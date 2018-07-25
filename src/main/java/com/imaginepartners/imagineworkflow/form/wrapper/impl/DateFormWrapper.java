package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.Date;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.apache.log4j.LogManager;

public class DateFormWrapper extends FormWrapper {

	private List<Object> entityList;

	public DateFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public DateFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (this.value == null && iwInput.getOptions3() != null
		&& iwInput.getOptions3().size() > 0
		&& Boolean.valueOf(iwInput.getOptions3().get(0))) {
			this.value = new Date();
		}
		entityList = null;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	public List<Object> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Object> entityList) {
		this.entityList = entityList;
	}
}
