package com.imaginepartners.imagineworkflow.form.activiti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.models.IwMultiLine;
import com.imaginepartners.imagineworkflow.util.FormContants;
import java.io.IOException;
import org.activiti.engine.form.AbstractFormType;
import org.apache.log4j.LogManager;

public class MultiLineFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String getName() {
		return FormContants.MULTI_LINE_FORM_TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		if (propertyValue == null) {
			return null;
		}
		try {
			return objectMapper.readValue(propertyValue, IwMultiLine.class);
		} catch (IOException ex) {
			LogManager.getLogger(MultiLineFormType.class).error(null, ex);
		}
		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(modelValue);
		} catch (JsonProcessingException ex) {
			LogManager.getLogger(MultiLineFormType.class).error(null, ex);
		}
		return null;
	}
}
