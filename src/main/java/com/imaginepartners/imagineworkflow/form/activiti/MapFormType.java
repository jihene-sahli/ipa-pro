package com.imaginepartners.imagineworkflow.form.activiti;

import com.imaginepartners.imagineworkflow.util.FormContants;
import org.activiti.engine.form.AbstractFormType;

public class MapFormType extends AbstractFormType{

	@Override
	public Object convertFormValueToModelValue(String formValue) {
		if(formValue == null)
			return null;
		return formValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if(modelValue == null)
			return null;
		return (String) modelValue;
	}

	@Override
	public String getName() {
		return FormContants.GMAP_FORM_TYPE_NAME;
	}
}
