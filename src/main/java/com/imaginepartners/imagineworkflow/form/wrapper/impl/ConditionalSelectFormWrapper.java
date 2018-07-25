package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.activiti.engine.form.FormProperty;

public class ConditionalSelectFormWrapper extends FormWrapper {

	private boolean conditionalSelectDefaultValue;

	public ConditionalSelectFormWrapper(FormProperty form, Object value) {
		super(form, value);
		if (this.value == null && iwInput.getOptions2() != null && !iwInput.getOptions2().isEmpty()) {
			this.value = iwInput.getOptions2().get(0);
			conditionalSelectDefaultValue = this.value.equals(iwInput.getOptions2().get(0));
		}
	}

	public ConditionalSelectFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	public boolean getConditionalSelectDefaultValue() {
		return conditionalSelectDefaultValue;
	}

	public void setConditionalSelectDefaultValue(boolean conditionalSelectDefaultValue) {
		if (conditionalSelectDefaultValue) {
			this.value = iwInput.getOptions2().get(0);
		} else {
			this.value = null;
		}
		this.conditionalSelectDefaultValue = conditionalSelectDefaultValue;
	}
}
