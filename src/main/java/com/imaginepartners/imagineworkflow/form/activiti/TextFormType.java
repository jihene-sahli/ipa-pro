package com.imaginepartners.imagineworkflow.form.activiti;

import com.imaginepartners.imagineworkflow.util.FormContants;
import org.activiti.engine.form.AbstractFormType;

public class TextFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		return FormContants.TEXTAREA_FORM_TYPE_NAME;
	}

	public String getMimeType() {
		return FormContants.TEXTAREA_FORM_TYPE_MIME_TYPE;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		if (propertyValue == null) {
			return null;
		}
		return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue == null) {
			return null;
		}
		return (String) modelValue;
	}
}
