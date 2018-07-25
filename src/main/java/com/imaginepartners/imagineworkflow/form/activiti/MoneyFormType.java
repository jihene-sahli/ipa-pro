package com.imaginepartners.imagineworkflow.form.activiti;

import com.imaginepartners.imagineworkflow.util.FormContants;
import java.math.BigDecimal;
import org.activiti.engine.form.AbstractFormType;

public class MoneyFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		return FormContants.MONEY_FORM_TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		if (propertyValue == null) {
			return null;
		}
		return new BigDecimal(propertyValue);
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue == null) {
			return null;
		}
		return ((BigDecimal) modelValue).toPlainString();
	}
}
