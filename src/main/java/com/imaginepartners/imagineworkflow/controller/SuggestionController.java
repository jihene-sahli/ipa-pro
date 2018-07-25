package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class SuggestionController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	BusinessService businessService;

	@Autowired
	transient private ActivitiService activitiService;

	private Map<String, Object> suggestedEntityMap = new HashMap<String, Object>();

	public List<User> completeUsers(String query) {
		return activitiService.getUsersListLike(query);
	}

	public List<Group> completeGroups(String query) {
		return activitiService.getGroupsListLike(query, businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
	}
	public List<Object> completeEntity(String entity) {
		String inputId = FacesUtil.getUrlParam("inputId");
		suggestedEntityMap.put(inputId, businessService.getEntiteList(entity));
		return (List<Object>) suggestedEntityMap.get(inputId);
	}

	public List<Object> completeEntityQuery(String query) {
		String entity = FacesUtil.getUrlParam("entity");
		String field = FacesUtil.getUrlParam("field");
		String[] fieldList = null;
		if (StringUtils.isNotBlank(FacesUtil.getUrlParam("fieldList"))) {
			fieldList = FacesUtil.getUrlParam("fieldList").split(",");
		}
		String inputId = FacesUtil.getUrlParam("inputId");
		if (StringUtils.isNotBlank(field)) {
			suggestedEntityMap.put(inputId, businessService.getEntityListLike(entity, field, query));
		} else {
			suggestedEntityMap.put(inputId, businessService.getEntityListLike(entity, fieldList, query));
		}
		return (List<Object>) suggestedEntityMap.get(inputId);
	}

	public String completeJoinEntity(String query) {
		String entity = FacesUtil.getUrlParam("entity");
		String field = FacesUtil.getUrlParam("field");
		String inputId = FacesUtil.getUrlParam("inputId");
		return entity;
	}

	public String getEntityIdValue(Object value, String className) {
		Class entityClass;
		try {
			entityClass = Class.forName(className);
			Field entityId = getEntityIdField(entityClass);
			entityId.setAccessible(true);
			return String.valueOf(entityId.get(value));
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex) {
			LogManager.getLogger(SuggestionController.class).error(null, ex);
		}
		return null;
	}

	public Field getEntityIdField(Class entityClass) {
		for (Field fld : entityClass.getDeclaredFields()) {
			if (fld.getAnnotation(Id.class) != null) {
				return fld;
			}
		}
		return null;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public Map<String, Object> getSuggestedEntityMap() {
		return suggestedEntityMap;
	}

	public void setSuggestedEntityMap(Map<String, Object> suggestedEntityMap) {
		this.suggestedEntityMap = suggestedEntityMap;
	}

	public Entry<String, Object> createEntryFortSuggestedEntityMap(String entryId) {
		suggestedEntityMap.put(entryId, new ArrayList<Object>());
		for (Entry<String, Object> entry : suggestedEntityMap.entrySet()) {
			if (entry.getKey().equals(entryId)) {
				return entry;
			}
		}
		return null;
	}

	public List<String> getEntityIdValueList(Object value, String className) {
		List<String> sonIdsList = new ArrayList<>();
		List<Object> sonListe = (List<Object>) value;
		Class entityClass;
		try {
			entityClass = Class.forName(className);
			for (Field field : entityClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(OneToMany.class)|| field.isAnnotationPresent(OneToOne.class)) {
					ParameterizedType type = (ParameterizedType) field.getGenericType();
					Class<?> joinClass = (Class<?>) type.getActualTypeArguments()[0];
					for (Object sonObj : sonListe) {
						Field entityId = getEntityIdField(joinClass);
						entityId.setAccessible(true);
						sonIdsList.add(String.valueOf(entityId.get(sonObj)));
					}
					return sonIdsList;
				}
			}
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex) {
			LogManager.getLogger(SuggestionController.class).error(null, ex);
		}
		return null;
	}
}
