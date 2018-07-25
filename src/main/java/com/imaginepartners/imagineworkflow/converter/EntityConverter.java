package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.Util;
import java.lang.reflect.Field;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter("entityConverter")
@Component
public class EntityConverter implements Converter {

	@Autowired
	private transient BusinessService businessService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Class entityClass;
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		try {
			entityClass = Class.forName((String) component.getAttributes().get(AppConstants.ENTITY_NAME_FOR_CONVERTER_ATTRIBUTE));
			Field entityId = Util.getEntityIdField(entityClass);
			List< ?> entityList = (List<?>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
			if (entityList != null && !entityList.isEmpty()) {
				for (Object entity : entityList) {
					entityId.setAccessible(true);
					if (value.equals(String.valueOf(entityId.get(entity)))) {
						return entity;
					}
				}
			} else {
				return businessService.getEntitytById((String) component.getAttributes().get(AppConstants.ENTITY_NAME_FOR_CONVERTER_ATTRIBUTE), value);
			}
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex) {
			LogManager.getLogger(EntityConverter.class).error(ex);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Long || value instanceof Integer || value instanceof String) {
			return String.valueOf(value);
		}
		Class entityClass;
		try {
			entityClass = Class.forName((String) component.getAttributes().get(AppConstants.ENTITY_NAME_FOR_CONVERTER_ATTRIBUTE));
			Field entityId = Util.getEntityIdField(entityClass);
			entityId.setAccessible(true);
			return String.valueOf(entityId.get(value));
		} catch (Exception ex) {
			LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
		}
		return null;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}
}
