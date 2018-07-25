package com.imaginepartners.imagineworkflow.form.activiti;

import com.imaginepartners.imagineworkflow.util.FormContants;
import org.activiti.engine.form.AbstractFormType;

public class FileFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		return FormContants.FILE_FORM_TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {

		if (propertyValue == null) {
			return null;
		}
		return Long.valueOf(propertyValue);
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue == null) {
			return null;
		}
		return String.valueOf(modelValue);
	}
}
