package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwApplication;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("applicationConverter")
@Component
public class ApplicationConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<IwApplication> appList = (List<IwApplication>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwApplication app : appList) {
			if (value.equals(String.valueOf(app.getIwApplicationId()))) {
				return app;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((IwApplication) value).getIwApplicationId());
	}

}
