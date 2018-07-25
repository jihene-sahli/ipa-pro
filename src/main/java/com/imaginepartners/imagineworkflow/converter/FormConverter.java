package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter("formConverter")
@Component
public class FormConverter implements Converter{

	@Autowired
	private BusinessService businessService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (!StringUtils.isBlank(value)){
			return businessService.getIwForm(Long.valueOf( value) );
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			if(value instanceof IwForm){
				IwForm form= (IwForm) value;
				return String.valueOf(((IwForm)value).getIwFormId());
			}
			if(value instanceof Long) {
				Long form= (Long) value;
				return String.valueOf(((Long)value));
			}
		}
		return null;
	}
}
