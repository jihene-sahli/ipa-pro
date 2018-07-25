package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwMultiLine;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.activiti.engine.form.FormProperty;

public class MultilineFormWrapper extends FormWrapper {

	public MultilineFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public MultilineFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		this.value = new IwMultiLine(iwInput.getMultilinecols(), iwInput.getMultilinerows(), iwInput.getOptions());
		if (value != null) {
			((IwMultiLine) this.value).setRows(((IwMultiLine) value).getRows());
		}
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}
}
