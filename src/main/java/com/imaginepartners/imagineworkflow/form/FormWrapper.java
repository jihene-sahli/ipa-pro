package com.imaginepartners.imagineworkflow.form;

import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang3.StringUtils;

public abstract class FormWrapper {

	protected final FormProperty form;

	protected final IwInput iwInput;

	protected final Boolean isReadable;

	protected final Boolean isWritable;

	protected Object value;

	protected BusinessService businessService;

	protected FormWrapper(FormProperty form, Object value) {
		this.form = form;
		this.value = value;
		this.iwInput = null;
		this.isReadable = true;
		this.isWritable = true;
	}

	protected FormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		this.iwInput = iwInput;
		this.form = null;
		this.value = value;
		this.isReadable = isReadable;
		this.isWritable = isWritable;
		this.businessService= businessService;
	}

	public abstract Object getValue();

	public abstract void persiste(BusinessService businessService, ActivitiService activitiService);

	/**
	 * Using the end user expression, extract result name, and expression value.
	 * @param multilineEntityInstance Entity Reference Object.
	 */
	public void setArithValue(Object multilineEntityInstance){
		if( !iwInput.getExplastcolumn().isEmpty()){
			for(String exp: iwInput.getExplastcolumn()){
				if(StringUtils.isNotBlank(exp)){
					FacesUtil.EVAL_MULTILINE_ENTITY_COLUMN(multilineEntityInstance, exp);
				}
			}
		}
	}

	/**
	 * Get reseltSet for given request
	 * @param clazz add class criteria to the given request
	 * @return list of tuples for given multiline entity
	 */
	public List<Object> getTuples(Class clazz){
		return businessService.getObjectBySQL(iwInput.getDatabaseRequest(), clazz);
	}

	/**
	 * Get reseltSet for given request
	 * @param dataBaseRequest simple sql request.
	 * @param clazz add class criteria to the given request
	 * @return result as list<clazz>
	 */
	public List<Object> getTuples(String dataBaseRequest, Class clazz){
		return businessService.getObjectBySQL(dataBaseRequest, clazz);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public FormProperty getForm() {
		return form;
	}

	public IwInput getIwInput() {
		return iwInput;
	}

	public Boolean getIsReadable() {
		return isReadable;
	}

	public Boolean getIsWritable() {
		return isWritable;
	}
}
