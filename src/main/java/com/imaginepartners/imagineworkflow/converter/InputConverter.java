package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter("inputConverter")
@Component
public class InputConverter implements Converter {

	@Autowired
	private BusinessService businessService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (!StringUtils.isBlank(value)) {
			return businessService.getIwInputByRealId(Long.valueOf(value));
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (value instanceof IwInput) {
				IwInput iwInput = (IwInput) value;
				return String.valueOf(((IwInput) value).getIwinputrealid());
			}
			if (value instanceof Long) {
				Long iwInput = (Long) value;
				return String.valueOf(((Long) value));
			}
		}
		return null;
	}
}
