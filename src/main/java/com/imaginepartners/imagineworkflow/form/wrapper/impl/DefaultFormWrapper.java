package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.activiti.engine.form.FormProperty;
import org.apache.log4j.LogManager;

public class DefaultFormWrapper extends FormWrapper {

	public DefaultFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public DefaultFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}
}
