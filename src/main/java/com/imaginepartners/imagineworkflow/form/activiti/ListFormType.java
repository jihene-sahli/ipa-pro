package com.imaginepartners.imagineworkflow.form.activiti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.models.IwMultiLine;
import com.imaginepartners.imagineworkflow.util.FormContants;
import java.io.IOException;
import java.util.List;
import org.activiti.engine.form.AbstractFormType;
import org.apache.log4j.LogManager;

public class ListFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String getName() {
		return FormContants.LIST_FORM_TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		if (propertyValue == null) {
			return null;
		}
		try {
			return objectMapper.readValue(propertyValue, List.class);
		} catch (IOException ex) {
			LogManager.getLogger(ListFormType.class).error(null, ex);
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
			LogManager.getLogger(ListFormType.class).error(null, ex);
		}
		return null;
	}
}
