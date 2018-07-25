package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

@FacesConverter("processDefinitionByKeyConverter")
@Component
public class ProcessDefinitionByKeyConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List< ProcessDefinition> procDefList = (List< ProcessDefinition>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (ProcessDefinition procDef : procDefList) {
			if (value.equals(String.valueOf(procDef.getKey()))) {
				return procDef;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((ProcessDefinition) value).getKey());
	}
}
