package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.Util;
import org.activiti.engine.form.FormProperty;
import org.apache.log4j.LogManager;

public class AddEntityFormWrapper extends FormWrapper {

	private Object entity;

	public AddEntityFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public AddEntityFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (this.value != null) {
			entity = businessService.getEntitytById(this.iwInput.getValue(), String.valueOf(this.value));
		} else {
			Class cls;
			try {
				cls = Class.forName(this.iwInput.getValue());
				entity = cls.newInstance();
			} catch (Exception ex) {
				LogManager.getLogger(FormWrapper.class.getName()).debug(null, ex);
			}
		}
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
		businessService.saveEntite(entity);
		value = Util.getEntityIdValue(iwInput.getValue(), entity);
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}
}
