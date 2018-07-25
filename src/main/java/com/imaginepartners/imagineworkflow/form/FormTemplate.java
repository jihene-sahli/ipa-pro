package com.imaginepartners.imagineworkflow.form;

import com.imaginepartners.imagineworkflow.repositories.EntityRepository;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FormTemplate {

	/**
	 * In this Map we will bind activiti variables, that are used in the
	 * template the key will hold the variable name and the value will hold the
	 * variable value
	 */
	protected Map<String, Object> varValues;

	protected BusinessService businessService;

	protected ActivitiService activitiService;

	protected UserService userService;

	protected EntityRepository entityRepository;

	/**
	 * Acts as postContruct
	 */
	public abstract void init();

	/**
	 * Initialize bean properties using Activiti framework.
	 */
	public abstract void putVars();

	public FormTemplate() {

	}

	public Map<String, Object> getVarValues() {
		return varValues;
	}

	public void setVarValues(Map<String, Object> varValues) {
		this.varValues = varValues;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Map<String,Object> getVarValuesReport(){ return null; }

	public EntityRepository getEntityRepository() {
		return entityRepository;
	}

	public void setEntityRepository(EntityRepository entityRepository) {
		this.entityRepository = entityRepository;
	}
}
