package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter("groupConverter")
@Component
public class GroupConverter implements Converter {

	@Autowired
	private transient ActivitiService activitiService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return activitiService.getGroup(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((Group) value).getId();
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}
}
