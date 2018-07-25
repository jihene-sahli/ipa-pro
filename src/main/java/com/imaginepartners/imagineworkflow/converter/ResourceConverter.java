package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwCar;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.Util;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter("resourceConverter")
@Component
public class ResourceConverter implements Converter {

	@Autowired
	private BusinessService businessService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List resourceList = (List) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		if(value != null && value.length()>0){
			if(resourceList != null){
				Object obj= businessService.getTupleFromClassById(resourceList.get(0).getClass(), Integer.valueOf(value.toString()));
				Integer intger= ((IwCar)obj).getIwCarId();
				return intger;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		List resourceList = (List) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		if (value == null) {
			return null;
		}
		if(value instanceof Integer){
			Object obj= businessService.getTupleFromClassById(resourceList.get(0).getClass(), Integer.valueOf(value.toString()));
			return Util.getEntityIdValue(obj.getClass().getName(), obj).toString();
		} else {
			List<Field> myList= Arrays.asList(value.getClass().getDeclaredFields());
			for(Field field: myList){
				field.setAccessible(true);
				if(field.getName().toLowerCase().contains("id") && !field.getName().toLowerCase().contains("serial")) {
					try {
						return field.get(value).toString();
					} catch (IllegalArgumentException ex) {
						Logger.getLogger(ResourceConverter.class).error("cant handle that", ex);
					} catch (IllegalAccessException ex) {
						Logger.getLogger(ResourceConverter.class).error("cant handle that", ex);
					}
				}
			}
		}
		return null;
	}
}
