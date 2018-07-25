package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.text.DecimalFormat;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang.NumberUtils;
import org.apache.log4j.LogManager;

public class DynamicTextFormWrapper extends FormWrapper {

	public DynamicTextFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public DynamicTextFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
	}

	public Object getValue() {
		if (iwInput.getValue() != null) {
			this.value = FacesUtil.evalAsString(iwInput.getValue());
		}
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	public boolean isNumeric() {
		this.value = this.getValue();
		if (value != null && NumberUtils.isNumber(value.toString())) {
			return true;
		}
		return false;
	}

	public String getFormatedValue() {
		if (iwInput.getValue() != null) {
			this.value = FacesUtil.evalAsString(iwInput.getValue());
			try {
				String valueToFormat = (String) this.value;
				if (this.value != null && NumberUtils.isNumber((String) this.value)) {
					double doubleValue = Double.parseDouble(valueToFormat);
					DecimalFormat df = new DecimalFormat("0.00##");
					String result;
					result = df.format(doubleValue);
					return result;
				}
			} catch (Exception exp) {
				LogManager.getLogger(DynamicTextFormWrapper.class).error(exp);
			}
		}
		return "";
	}
}
