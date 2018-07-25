package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("pickListConverter")
@Component
public class PickListConverter implements Converter {

	@Autowired
	private ActivitiService activitiService;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		Group group = activitiService.getGroup(s);
		return group;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
			System.out.print(o);
			return (String)o;

	}
}
