package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.controller.TaskController;
import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwListOptions;
import com.imaginepartners.imagineworkflow.models.IwMultilineEntityField;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.persistence.Id;
import org.activiti.engine.form.FormProperty;
import org.apache.log4j.LogManager;
import static org.apache.log4j.LogManager.getLogger;

public class MultilineEntityConfigFormWrapper extends FormWrapper {

	private List<String> multilineEntityEditableColumns;

	private final Map<String, List<String>> iwListMap = new HashMap<String, List<String>>();

	private final Map<String, List<String>> joinsMap = new HashMap<String, List<String>>();

	private final Map<String, String> joinsClasses = new HashMap<String, String>();

	private List<Object> entityList;

	private List<Object> filtredData;

	private List<IwInput> entityColumnsNames;

	private UIComponent component;

	private Object configEntityValueSeleted;

	private List<Object> configEntityListDeleted;

	private boolean isInstantiate;

	private int lastIndex;

	public MultilineEntityConfigFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public MultilineEntityConfigFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if ("multilineentityconfig".equals(iwInput.getComponent())) {
			List<IwMultilineEntityField> listField = businessService.getFieldByEntityList(iwInput.getValue().toString());
			if (listField != null && !listField.isEmpty()) {
				for (IwMultilineEntityField item : listField) {
					if (item.getIwList() != null) {
						List<IwListOptions> options = businessService.getIwListOptionsListByIwList(item.getIwList());
						List<String> optionNameList = new ArrayList<String>();
						if (options != null && !options.isEmpty()) {
							for (IwListOptions opt : options) {

								optionNameList.add(opt.getIwName());
							}
						}
						iwListMap.put(item.getIwMultilineEntityFieldName(), optionNameList);
					}
				}
			}
			if (iwInput.getEntityFilter() != null && !iwInput.getEntityFilter().isEmpty()) {
				iwInput.setEntityFilter((ArrayList<String>) FacesUtil.evalAsString(iwInput.getEntityFilter()));
				entityList = businessService.getEntiteConfigList(iwInput.getValue(), iwInput.getOptions2(), iwInput.getEntityFilter());
			} else {
				entityList = businessService.getEntiteList(iwInput.getValue());
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
		LogManager.getLogger("subEntity").info("subEntity " + subEntity);
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
		System.out.println("filtredData" + filtredData.size());
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
			LogManager.getLogger(MultilineEntityConfigFormWrapper.class).error(exp);
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

	public Object getConfigEntityValueSeleted() {
		return configEntityValueSeleted;
	}

	public void setConfigEntityValueSeleted(Object configEntityValueSeleted) {
		this.configEntityValueSeleted = configEntityValueSeleted;
	}

	public List<Object> getConfigEntityListDeleted() {
		return configEntityListDeleted;
	}

	public void setConfigEntityListDeleted(List<Object> configEntityListDeleted) {
		this.configEntityListDeleted = configEntityListDeleted;
	}

	public void initDialogEntityConfig(int indexEntity, Boolean isInstantiate) {
		this.isInstantiate = isInstantiate;
		if (isInstantiate) {
			try {
				Class cls = Class.forName(iwInput.getValue());
				configEntityValueSeleted = cls.newInstance();
				for (Field field : cls.getDeclaredFields()) {
					Id id = field.getAnnotation(Id.class);
					if (id != null) {
						field.setAccessible(true);
						if (field.getType().getSimpleName().equals("Long")) {
							field.set(configEntityValueSeleted, new Long(--lastIndex));
						} else if (field.getType().getSimpleName().equals("Integer")) {
							field.set(configEntityValueSeleted, new Integer(--lastIndex));
						}
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				configEntityValueSeleted = null;
				Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InstantiationException ex) {
				configEntityValueSeleted = null;
				Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				configEntityValueSeleted = null;
				Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			Object object = ((filtredData != null && !filtredData.isEmpty())) ? filtredData.get(indexEntity) : entityList.get(indexEntity);
			configEntityValueSeleted = object;
		}
	}

	public void confirmEntityChange() {
		if (isInstantiate) {
			if (entityList == null) {
				entityList = new ArrayList<Object>();
			}
			entityList.add(configEntityValueSeleted);
		}
		resetEntityConfig();
	}

	public void resetEntityConfig() {
		configEntityValueSeleted = null;
		isInstantiate = false;
	}

	public void deleteEntity(int indexEntity) {
		if (configEntityListDeleted == null) {
			configEntityListDeleted = new ArrayList<Object>();
		}
		Object obj;
		if ((filtredData != null && !filtredData.isEmpty())) {
			obj = filtredData.get(indexEntity);
			filtredData.remove(indexEntity);
		} else {
			obj = entityList.get(indexEntity);
		}
		configEntityListDeleted.add(obj);
		entityList.remove(obj);
	}
}
