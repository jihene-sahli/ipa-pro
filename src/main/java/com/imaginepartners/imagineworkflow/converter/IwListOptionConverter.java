package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwList;
import com.imaginepartners.imagineworkflow.models.IwListOptions;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;

@FacesConverter("iwListOptionConverter")
@Component
public class IwListOptionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		List<IwListOptions> iwListOptions = (List<IwListOptions>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwListOptions option : iwListOptions) {
			if (value.equals(String.valueOf(option.getIwListOptionsId()))) {
				return option;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((IwListOptions) value).getIwListOptionsId());
	}
}
