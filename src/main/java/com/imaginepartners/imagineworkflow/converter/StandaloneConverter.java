package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwStandAloneTask;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("standaloneConverter")
@Component
public class StandaloneConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<IwStandAloneTask> list = (List<IwStandAloneTask>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwStandAloneTask iwStandaloneTask : list) {
			if (value.equals(String.valueOf(iwStandaloneTask.getIwStandAloneTaskId()))) {
				return iwStandaloneTask;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((IwStandAloneTask) value).getIwStandAloneTaskId());
	}
}
