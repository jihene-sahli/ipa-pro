package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwResourceType;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("resourceTypeConverter")
@Component
public class ResourceTypeConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<IwResourceType> resourceTypeList = (List<IwResourceType>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		if(resourceTypeList != null){
			for (IwResourceType resourceType : resourceTypeList) {
				if (value.equals(String.valueOf(resourceType.getResourceTypeId() ))) {
					return resourceType;
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof IwResourceType) {
			return String.valueOf(((IwResourceType) value).getResourceTypeId());
		}
		return null;
	}
}
