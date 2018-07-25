package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwList;
import com.imaginepartners.imagineworkflow.models.IwMultilineEntity;
import com.imaginepartners.imagineworkflow.models.IwMultilineEntityField;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class AdvencedController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BusinessService businessService;

	private List<IwMultilineEntity> multiLineEntityList;

	private List<IwMultilineEntityField> multilineEntityFieldList;

	private IwMultilineEntity multiLineEntity;

	private List<IwList> allLists;

	private IwList selectedList;
	@PostConstruct

	public void init() {

		multiLineEntityList = businessService.getMultiLineEntityList();

		allLists = businessService.getIwListList();

		String entityId = FacesUtil.getUrlParam("entity");

		if (StringUtils.isNotBlank(entityId)) {
			multilineEntityFieldList = businessService.getEntityFieldList(entityId);
			multiLineEntity = (IwMultilineEntity) businessService.getEntitytById(IwMultilineEntity.class.getName(), entityId);
		}
	}

	public IwList getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(IwList selectedList) {
		this.selectedList = selectedList;
	}

	public List<IwList> getAllLists() {
		return allLists;
	}

	public void setAllLists(List<IwList> allLists) {
		this.allLists = allLists;
	}

	public List<IwMultilineEntity> getMultiLineEntityList() {
		return multiLineEntityList;
	}

	public void setMultiLineEntityList(List<IwMultilineEntity> multiLineEntityList) {
		this.multiLineEntityList = multiLineEntityList;
	}

	public void addMultiLineEntity() {
		IwMultilineEntity multiLineEntity = new IwMultilineEntity();
		businessService.saveEntite(multiLineEntity);
		multiLineEntityList = businessService.getMultiLineEntityList();
	}

	public void addMultiLineEntityField() {
		if (multiLineEntity != null) {
			IwMultilineEntityField multiLineEntityField = new IwMultilineEntityField();
			multiLineEntityField.setIwMultilineEntity(multiLineEntity);
			businessService.saveEntite(multiLineEntityField);
			multilineEntityFieldList = businessService.getEntityFieldList(multiLineEntity.getIwMultilineEntityId() + "");
		}
	}

	public void removeEntite(int index) {
		IwMultilineEntity entity = multiLineEntityList.get(index);
		businessService.removeEntite(entity);
		multiLineEntityList = businessService.getMultiLineEntityList();
	}

	public void removeEntiteField(int index) {
		if (multiLineEntity != null) {
			IwMultilineEntityField entityField = multilineEntityFieldList.get(index);
			businessService.removeEntite(entityField);
			multilineEntityFieldList = businessService.getEntityFieldList(multiLineEntity.getIwMultilineEntityId() + "");
		}
	}

	public void saveMultiLineEntity(IwMultilineEntity entity) {
		businessService.saveEntite(entity);
	}

	public void saveMultiLineEntityField(IwMultilineEntityField entity) {
		businessService.saveEntite(entity);
	}

	public List<IwMultilineEntityField> getMultilineEntityFieldList() {
		return multilineEntityFieldList;
	}

	public void setMultilineEntityFieldList(List<IwMultilineEntityField> multilineEntityFieldList) {
		this.multilineEntityFieldList = multilineEntityFieldList;
	}

	public IwMultilineEntity getMultiLineEntity() {
		return multiLineEntity;
	}

	public void setMultiLineEntity(IwMultilineEntity multiLineEntity) {
		this.multiLineEntity = multiLineEntity;
	}

	public void updateEntityFields() {
		if (multiLineEntity != null && multiLineEntity.getIwMultilineEntityId() != null) {
			multilineEntityFieldList = businessService.getEntityFieldList(multiLineEntity.getIwMultilineEntityId().toString());
			try {
				Class cls = Class.forName(multiLineEntity.getIwClass());
				for (Field fld : cls.getDeclaredFields()) {
					if (!"serialVersionUID".equals(fld.getName())) {
						if (!isEnityContainsField(fld.getName())) {
							IwMultilineEntityField entityField = new IwMultilineEntityField();
							entityField.setIwMultilineEntityFieldName(fld.getName());
							entityField.setIwMultilineEntityFieldDescription(fld.getName());
							entityField.setIwMultilineEntity(multiLineEntity);
							businessService.saveEntite(entityField);
						}
					}
				}
				if (multilineEntityFieldList != null && !multilineEntityFieldList.isEmpty()) {
					Field fied;
					for (IwMultilineEntityField entityField : multilineEntityFieldList) {
						if (entityField != null && entityField.getIwMultilineEntityFieldName() != null) {
							try {
								cls.getDeclaredField(entityField.getIwMultilineEntityFieldName());
							} catch (Exception ex) {
								businessService.removeEntite(entityField);
							}
						}
					}
				}
				multilineEntityFieldList = businessService.getEntityFieldList(multiLineEntity.getIwMultilineEntityId() + "");
			} catch (ClassNotFoundException ex) {
				LogManager.getLogger(AdvencedController.class.getName()).debug("Entity not found", ex);
			}
		}
	}

	public boolean isEnityContainsField(String fieldName) {
		if (multilineEntityFieldList != null && !multilineEntityFieldList.isEmpty()) {
			for (IwMultilineEntityField entityField : multilineEntityFieldList) {
				if (entityField != null && entityField.getIwMultilineEntityFieldName() != null) {
					if (fieldName.equals(entityField.getIwMultilineEntityFieldName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getEntityFieldType(String calssName, String fieldName) {
		try {
			Class cls = Class.forName(calssName);
			Class type = null;
			for (Field field : cls.getDeclaredFields()) {
				type = field.getType();
				if (field.getName().equals(fieldName)) {
					type = field.getType();
					break;
				}
			}
			if (type != null) {
				return type.getSimpleName();
			}
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(TaskController.class).error(null, ex);
		} catch (SecurityException ex) {
			LogManager.getLogger(TaskController.class).error(null, ex);
		}
		return "";
	}
}
