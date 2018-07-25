package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwListOptions;
import com.imaginepartners.imagineworkflow.models.IwMultilineEntityField;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;

public class MultilineEntityFormWrapper extends FormWrapper {

	private List<String> multilineEntityEditableColumns;

	private List<String> multilineEntityColumns;

	private final Map<String, List<String>> iwListMap = new HashMap<String, List<String>>();

	private final Map<String, List<String>> joinsMap = new HashMap<String, List<String>>();

	private final Map<String, String> joinsClasses = new HashMap<String, String>();

	private List<Object> entityList;

	private List<Object> filtredData;

	private List<IwInput> entityColumnsNames;

	private UIComponent component;

	private String taskID;

	private String procInstID;

	private Task currentTask;

	private ProcessInstance currentProcess;

	private String taskDefKey;

	private String procDefKey;

	public MultilineEntityFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public MultilineEntityFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {

		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		/**
		 * Intialize "taskID" and "procInstID" to be used later by "displayByTaskProcess()"
		 */
		taskID = taskId;
		currentTask = activitiService.getTask(taskId);
		procInstID = procInstId;
		currentProcess = activitiService.getProcessInstanceById(procInstID);
		if (currentProcess != null) {
			procDefKey = currentProcess.getProcessDefinitionKey();
			taskDefKey = currentTask.getTaskDefinitionKey();
			try {
				List<IwMultilineEntityField> listField = businessService.getFieldByEntityList(iwInput.getValue().toString());
				Class cls = Class.forName(iwInput.getValue());
				for (Field field : cls.getDeclaredFields()) {
					Class<?> joinClass = null;
					if (field.isAnnotationPresent(OneToMany.class)) {
						ParameterizedType type = (ParameterizedType) field.getGenericType();
						joinClass = (Class<?>) type.getActualTypeArguments()[0];
					}else if(field.isAnnotationPresent(OneToOne.class)){
						joinClass = field.getType();
					}
					if(joinClass != null){
						List<IwMultilineEntityField> displayableJoinFields = businessService.getFieldByEntityList(joinClass.getName());
						joinsClasses.put(field.getName(), joinClass.getName());
						List<String> fields = new ArrayList<>();
						for (IwMultilineEntityField currentField : displayableJoinFields) {
							if (currentField.getDisplayInJointable() != null && currentField.getDisplayInJointable()) {
								fields.add(currentField.getIwMultilineEntityFieldName());
							}
						}
						joinsMap.put(field.getName(), fields);
					}
				}
				if (listField != null && !listField.isEmpty()) {
					for (IwMultilineEntityField item : listField) {
						if (item.getIwList() != null) {
							List<IwListOptions> options = businessService.getIwListOptionsListByIwList(item.getIwList());
							List<String> optionNameList = new ArrayList<String>();
							// Set names of iwOptions in optionNameList
							if (options != null && !options.isEmpty()) {
								for (IwListOptions opt : options) {
									optionNameList.add(opt.getIwName());
								}
							}
							iwListMap.put(item.getIwMultilineEntityFieldName(), optionNameList);
						}
					}
				}
				entityList = new ArrayList<Object>();
				List entitys = (List) this.value;
				if (entitys != null) {
					if (!entitys.isEmpty()) {
						for (Object idEntity : entitys) {
							Object entity = businessService.getEntitytById(iwInput.getValue(), String.valueOf(idEntity));
							if (entity != null) {
								entityList.add(entity);
							}
						}
					}
				} else {
					if (iwInput.getAllowFilter() != null && iwInput.getAllowFilter() == true) {
						if (iwInput.getEntityFilter() != null && !iwInput.getEntityFilter().isEmpty()) {
							iwInput.setEntityFilter((ArrayList<String>) FacesUtil.evalAsString(iwInput.getEntityFilter()));
							if (iwInput.getValue() != null && iwInput.getValue().length() > 1) {
								entityList = businessService.getEntiteList(iwInput.getValue(), iwInput.getOptions2(), iwInput.getEntityFilter());
							}
						}
					}
					if (iwInput != null) {
						try {
							if(iwInput.getUseRequest() &&  StringUtils.isNotBlank(iwInput.getDatabaseRequest())) {
								entityList= this.getTuples(cls);
							}
						} catch(NullPointerException exp) {
							LogManager.getLogger(FormWrapper.class).error(exp);
						}
					}
				}
			} catch (ClassNotFoundException exp) {
				LogManager.getLogger(FormWrapper.class).error(exp);
			}
		}
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	public String getSubEntityLabel(Object subEntity, String field) {
		if (subEntity == null || joinsMap == null || joinsMap.get(field) == null || joinsClasses.get(field) == null) {
			return "";
		}
		String label = "";
		for (String subField : joinsMap.get(field)) {
			label += Util.getEntityFieldValue(joinsClasses.get(field), subEntity, subField) + " ";
		}
		return label;
	}

	public String getSubEntityFields(String field) {
		if (joinsMap == null || joinsMap.get(field) == null) {
			return "";
		}
		String label = "";
		for (String subField : joinsMap.get(field)) {
			label += subField + ",";
		}
		return label;
	}

	public List<Object> getEntityList() {
		return entityList;
	}

	public List<IwInput> getEntityColumnsNames() {
		return entityColumnsNames;
	}

	public void setEntityColumnsNames(List<IwInput> entityColumnsNames) {
		this.entityColumnsNames = entityColumnsNames;
	}

	public List<Object> getFiltredData() {
		return filtredData;
	}

	public void setFiltredData(List<Object> filtredData) {
		this.filtredData = filtredData;
	}

	public List<String> getMultilineEntityEditableColumns() {
		return multilineEntityEditableColumns;
	}

	public void setMultilineEntityEditableColumns(List<String> multilineEntityEditableColumns) {
		this.multilineEntityEditableColumns = multilineEntityEditableColumns;
	}

	public Map<String, List<String>> getIwListMap() {
		return iwListMap;
	}

	public Map<String, List<String>> getJoinsMap() {
		return joinsMap;
	}

	public Map<String, String> getJoinsClasses() {
		return joinsClasses;
	}

	public Field getEntityIdField(Class entityClass) {
		for (Field fld : entityClass.getDeclaredFields()) {
			if (fld.getAnnotation(Id.class) != null) {
				return fld;
			}
		}
		return null;
	}

	public List<String> complete(String query) {
		List<String> results = new ArrayList<String>();
		for (String name : joinsMap.keySet()) {
			String key = name.toString();
			String valeur = joinsMap.get(name).toString();
		}
		for (String name : joinsClasses.keySet()) {
			String key = name.toString();
			String sonClassName = joinsClasses.get(name).toString();
		}
		Class entityClass;
		try {
			entityClass = Class.forName(((String) component.getAttributes().get(AppConstants.ENTITY_NAME_FOR_CONVERTER_ATTRIBUTE)));
		} catch (ClassNotFoundException exp) {
			LogManager.getLogger(MultilineEntityFormWrapper.class).error(exp);
		}
		return results;
	}

	public UIComponent getComponent() {
		return component;
	}

	public void setComponent(UIComponent component) {
		this.component = component;
	}

	public String resizeMultilignColomns(IwInput iwInput, int index) {
		if (iwInput.getOptions3() != null && iwInput.getOptions3().size() > index) {
			return iwInput.getOptions3().get(index);
		}
		return null;
	}

	public void setEntityList(List<Object> entityList) {
		this.entityList = entityList;
	}

	/**
	 * getMultilineEntityColumns
	 *
	 * Verify which column has rights to be rendered in multilineEntity.xhtml
	 * The method calls verifyEmptiness() with a column index (given by
	 * iwInput.getOptions2()), which calls verifyProcessPresence() to verify the
	 * actual rights, given that the rights list provided by FormBuilder is not
	 * empty.
	 *
	 * @return List<String>
	 */
	public List<String> getMultilineEntityColumns() {
		multilineEntityColumns = new ArrayList<String>();
		for (int i = 0; i < iwInput.getOptions2().size(); i++) {
			if (verifyEmptiness(i)) {
				multilineEntityColumns.add(iwInput.getOptions2().get(i));
			}
		}
		return multilineEntityColumns;
	}

	/**
	 * userHasWriteRights
	 *
	 * @param processListInColumn the list of process key and it's values
	 *
	 * Compare the current process key with those in processListInColumn, if it
	 * shows in the list continue with the verification of the existance of the
	 * current task key inside the list of values if it's there, then the column
	 * will be given the right to be rendered for only those process and task.
	 * Else, no right to be rendered is given.
	 *
	 * @return Boolean
	 */
	public Boolean verifyProcessPresence(LinkedHashMap<String, List<String>> processListInColumn) {
		Boolean result = false;
		if (currentProcess == null) {
			return false;
		}
		for (Map.Entry<String, List<String>> process : processListInColumn.entrySet()) {
			String key = process.getKey();
			List<String> value = process.getValue();
			if (!key.equals(procDefKey)) {
				continue;
			} else if (key.equals(procDefKey)) {
				for (String task : value) {
					if (!task.equals(taskDefKey)) {
						continue;
					} else if (task.equals(taskDefKey)) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * userHasWriteRights
	 *
	 * @param index Index of the column for which we need to inspect the render
	 * right
	 *
	 * Verify if the rights list by column provided by FormBuilder is not empty.
	 * If it's empty it return TRUE which means this column's will added to
	 * multilineEntityColumns and it will be rendred. Else, it calls
	 * verifyProcessPresence() for a further inspection the rights.
	 *
	 * @return Boolean
	 */
	public Boolean verifyEmptiness(Integer index) {
		Boolean result = false;
		List<LinkedHashMap<String, List<String>>> colTasksByProcess = iwInput.getColTachesbyprocess();
		try {
			if(!colTasksByProcess.isEmpty()) {
				LinkedHashMap<String, List<String>> processListInColumn = colTasksByProcess.get(index);
				if (processListInColumn.isEmpty())
					result = true;
				else
					result = verifyProcessPresence(processListInColumn);
			} else {
				result = true;
			}
		} catch(IndexOutOfBoundsException ex) {
			LogManager.getLogger(MultilineEntityFormWrapper.class).error("Index out of bound exception", ex);
		} finally {
			return result;
		}
	}
}
