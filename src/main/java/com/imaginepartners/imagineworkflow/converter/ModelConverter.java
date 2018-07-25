package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.activiti.engine.repository.Model;
import org.springframework.stereotype.Component;

@FacesConverter("modelConverter")
@Component
public class ModelConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<Model> modelList = (List<Model>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (Model model : modelList) {
			if (value.equals(String.valueOf(model.getId()))) {
				return model;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((Model) value).getId());
	}
}
