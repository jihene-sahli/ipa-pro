package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("userConverter")
@Component
public class UserConverter implements Converter {

	@Autowired
	private transient ActivitiService activitiService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return activitiService.getUser(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((User) value).getId();
	}

	public ActivitiService getActivitiService() {

		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}
}
