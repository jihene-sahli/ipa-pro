package com.imaginepartners.imagineworkflow.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.annotations.ColumnInfo;
import com.imaginepartners.imagineworkflow.annotations.EntityInfo;
import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.business.*;
import com.imaginepartners.imagineworkflow.models.rh.*;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.DaoBusinessService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional("transactionManager")
public class BusinessServiceImpl implements BusinessService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DaoBusinessService daoDataService;

	@Override
	public IwConfig getConfig(String configName) {
		return daoDataService.getConfig(configName);
	}

	@Override
	public String getConfigValue(String configName) {
		IwConfig config = getConfig(configName);
		if (config != null) {
			return config.getConfigValue();
		} else {
			return "";
		}
	}

	@Override
	public String getConfigValueOrDefault(String configName) {
		IwConfig config = getConfig(configName);
		if (config != null) {
			if (StringUtils.isNoneEmpty(config.getConfigValue())) {
				return config.getConfigValue();
			} else if (StringUtils.isNoneEmpty(config.getConfigValue())) {
				return config.getDefaultValue();
			}
		}
		return "";
	}

	@Override
	public IwForm newIwForm() {
		return daoDataService.newIwForm();
	}

	@Override
	public IwForm saveIwForm(IwForm iwForm) {
		return daoDataService.saveIwForm(iwForm);
	}

	@Override
	public IwForm getIwForm(Long iwFormId) {
		return daoDataService.getIwForm(iwFormId);
	}

	@Override
	public List<IwForm> getIwFormList() {
		return daoDataService.getIwFormList();
	}

	@Override
	public List<IwForm> getIwFormListByFormKey(Set<String> keys) {
		return daoDataService.getIwFormListByFormKey(keys);
	}

	@Override
	public List<IwInput> getIwInputByFormId(IwForm iwForm) {
		return daoDataService.getIwInputByFormId(iwForm);
	}

	@Override
	public IwInput getIwInputById(String id) {
		return daoDataService.getIwInputById(id);
	}

	@Override
	public IwInput getIwInputByRealId(Long realid) {
		return daoDataService.getIwInputByRealId(realid);
	}

	@Override
	public void deleteIwForm(Long formId) {
		daoDataService.deleteIwForm(formId);
	}

	@Override
	public void saveIwUserDetails(IwUserDetails userDetails) {
		daoDataService.saveIwUserDetails(userDetails);
	}

	@Override
	public IwUserDetails getIwUserDetails(String userId) {
		return daoDataService.getIwUserDetails(userId);
	}

	@Override
	public void saveIwGroupHierarchy(IwGroupHierarchy groupeHierarchy) {
		daoDataService.saveIwGroupHierarchy(groupeHierarchy);
	}

	@Override
	public List<IwGroupHierarchy> getIwGroupHierarchyList() {
		return daoDataService.getIwGroupHierarchyList();
	}

	@Override
	public IwGroupHierarchy getIwGroupHierarchyByGroupAndParent(String group, String parent) {
		return daoDataService.getIwGroupHierarchyByGroupAndParent(group, parent);
	}

	@Override
	public List<IwGroupHierarchy> getIwGroupHierarchyByParent(String parent) {
		return daoDataService.getIwGroupHierarchyByParent(parent);
	}

	@Override
	public IwGroupHierarchy getIwGroupHierarchyByGroup(String group) {
		return daoDataService.getIwGroupHierarchyByGroup(group);
	}

	@Override
	public int removeAllIwGroupHierarchy() {
		return daoDataService.removeAllIwGroupHierarchy();
	}

	@Override
	public void saveIwUpload(IwUpload iwUpload) {
		daoDataService.saveIwUpload(iwUpload);
	}

	@Override
	public List<IwFile> getIwFilesForUpload(Long uploadId) {
		return daoDataService.getIwFilesForUpload(uploadId);
	}

	@Override
	public IwUpload getIwUpload(Long uploadId) {
		return daoDataService.getIwUpload(uploadId);
	}

	@Override
	public List<IwEvent> getIwEventForUser(String userId) {

		return daoDataService.getIwEventForUser(userId);
	}

	@Override
	public void saveIwEvent(IwEvent iwEvent) {
		daoDataService.saveIwEvent(iwEvent);
	}

	@Override
	public void deleteIwEvent(IwEvent iwEvent) {
		daoDataService.deleteIwEvent(iwEvent);
	}

	@Override
	public List<IwCar> getCarsList() {
		return daoDataService.getCarsList();
	}

	@Override
	public void saveIwBooking(IwBooking iwBooking) {
		daoDataService.saveIwBooking(iwBooking);
	}

	@Override
	public void deleteIwBooking(IwBooking iwBooking) {
		daoDataService.deleteIwBooking(iwBooking);
	}

	@Override
	public List<IwBooking> geIwBookingList() {
		return daoDataService.geIwBookingList();
	}

	@Override
	public List<IwBooking> getBookingListForResourceByDate(Integer resourceId, Date startDate, Date endDate) {
		return daoDataService.getBookingListForResourceByDate(resourceId, startDate, endDate);
	}

	@Override
	public IwGroupDetails getGroupDetails(String idGroup) {
		return daoDataService.getGroupDetails(idGroup);
	}

	@Override
	public void saveIwGroupDetails(IwGroupDetails iwGroupDetails) {
		daoDataService.saveIwGroupDetails(iwGroupDetails);
	}

	@Override
	public IwProgress getIwProgressProcessTask(String procKey, String taskId) {
		return daoDataService.getIwProgressProcessTask(procKey, taskId);
	}

	@Override
	public void saveIwProgress(IwProgress p) {
		daoDataService.saveIwProgress(p);
	}

	@Override
	public void saveIwVariableProcess(IwVariableProcess Vp) {
		daoDataService.saveIwVariableProcess(Vp);
	}

	@Override
	public String getResponsibleForUser(String userId) {
		return daoDataService.getResponsibleForUser(userId);
	}

	@Override
	public List<IwPhase> getPhaseList() {
		return daoDataService.getPhaseList();
	}

	@Override
	public void savePhase(IwPhase currentPhase) {
		daoDataService.savePhase(currentPhase);
	}

	@Override
	public void removePhase(IwPhase phase) {
		daoDataService.removePhase(phase);
	}

	@Override
	public void removeIwGroupDetail(String groupId) {
		daoDataService.removeIwGroupDetail(groupId);
	}

	@Override
	public void saveIwLicense(IwLicense iwLicense) {
		daoDataService.saveIwLicense(iwLicense);
	}

	@Override
	public List<IwLicense> getIwLicenseList() {
		return daoDataService.getIwLicenseList();
	}

	@Override
	public IwLicense getIwLicense(Integer iwLicenseId) {
		return daoDataService.getIwLicense(iwLicenseId);
	}

	@Override
	public void deleteIwLicense(Integer iwLicenseId) {
		daoDataService.deleteIwLicense(iwLicenseId);
	}

	@Override
	public IwLicense getLastActiveIwLicense() {
		return daoDataService.getLastActiveIwLicense();
	}

	@Override
	public List<IwConfig> getConfigList(List<String> configNames) {
		return daoDataService.getConfigList(configNames);
	}

	@Override
	public IwConfig saveIwConfig(IwConfig iwConfig) {
		daoDataService.saveIwConfig(iwConfig);
		return iwConfig;
	}

	@Override
	public void deleteIwUserDetails(String userId) {
		daoDataService.deleteIwUserDetails(userId);
	}

	@Override
	public List<IwApplication> getApplicaitonList() {
		return daoDataService.getApplicationList();
	}

	@Override
	public List<IwApplication> getUserApplicaitonList(List<String> appKies) {
		return daoDataService.getUserApplicaitonList(appKies);
	}

	@Override
	public IwApplication getApplicationByKey(String key) {
		return daoDataService.getApplicationByKey(key);
	}

	@Override
	public IwApplication getApplicationByProcess(String processKey) {
		return daoDataService.getApplicationByProcess(processKey);
	}

	@Override
	public IwApplication getApplicationById(Long id) {
		return daoDataService.getApplicationById(id);
	}

	@Override
	public void saveIwApplication(IwApplication application) {
		daoDataService.saveIwApplication(application);
	}

	@Override
	public void deleteIwApplication(Long applicationId) {
		daoDataService.deleteIwApplication(applicationId);
	}

	@Override
	public List<IwMultilineEntity> getMultiLineEntityList() {
		return daoDataService.getMultiLineEntityList();
	}

	@Override
	public Object getEntitytById(String className, String idEntity) {
		return daoDataService.getEntitytById(className, idEntity);
	}

	@Override
	public void saveEntityList(List entityList) {
		daoDataService.saveEntityList(entityList);
	}

	@Override
	public List<IwMultilineEntityField> getFieldByEntityList(String entityName) {
		return daoDataService.getFieldByEntityList(entityName);
	}

	@Override
	public String getDescriptionFiledEntity(String entityName, String fieldName) {
		return daoDataService.getDescriptionFiledEntity(entityName, fieldName);
	}

	@Override
	public List<IwRight> getIwRightList(String userId, Collection<GrantedAuthority> autorities) {
		return daoDataService.getIwRightList(userId, autorities);
	}

	@Override
	public void saveRights(List<IwRight> rightAddList, List<IwRight> rightRemoveList) {
		daoDataService.saveRights(rightAddList, rightRemoveList);
	}

	@Override
	public List<IwRight> getIwRightList(String applicatioKey) {
		return daoDataService.getIwRightList(applicatioKey);
	}

	@Override
	public List<IwRight> getIwRightList(String applicatioKey, String processKey) {
		return daoDataService.getIwRightList(applicatioKey, processKey);
	}

	@Override
	public void saveEntite(Object entity) {
		daoDataService.saveEntite(entity);
	}

	@Override
	public void removeEntite(Object entity) {
		daoDataService.removeEntite(entity);
	}

	@Override
	public void removeList(Object entity) {
		daoDataService.removeEntite(entity);
	}

	@Override
	public List<IwMultilineEntityField> getEntityFieldList(String entityId) {
		return daoDataService.getEntityFieldList(entityId);
	}

	@Override
	public List<IwList> getIwListList() {
		return daoDataService.getIwListList();
	}

	@Override
	public List<IwListOptions> getIwListOptionsListByIwList(IwList list) {
		return daoDataService.getIwListOptionsListByIwList(list);
	}

	@Override
	public List<IwVariableProcess> getVariableProcess(String processKey, Boolean isTask, Boolean isForm) {
		return daoDataService.getVariableProcess(processKey, isTask, isForm);
	}

	@Override
	public List<IwVariableProcess> getVariableProcessByProcessKeyAndIwinputs(String processKey, List<IwInput> inputList) {
		return daoDataService.getVariableProcessByProcessKeyAndIwinputs(processKey, inputList);
	}

	@Override

	public void deleteObject(Object obj) {
		daoDataService.deleteObject(obj);
	}

	@Override
	public List<IwFormTemplate> getIwFormTemplateList() {
		return daoDataService.getIwFormTemplateList();
	}

	@Override
	public List<BizSecteur> getSectorList() {
		return daoDataService.getSectorList();
	}

	@Override
	public List<BizRayon> getRayonList() {
		return daoDataService.getRayonList();
	}

	@Override
	public List<BizFamille> getFamilyList() {
		return daoDataService.getFamilyList();
	}

	@Override
	public List<BizSecteurRayon> getRayonListForSector(Integer sectorId) {
		return daoDataService.getRayonListForSector(sectorId);
	}

	@Override
	public List<BizSecteurRayon> getSecteurRayonList(BizSecteur sectorId) {
		return daoDataService.getSecteurRayonList(sectorId);
	}

	@Override
	public List<Object> getEntityListLike(String entity, String field, String query) {
		return daoDataService.getEntityListLike(entity, field, query);
	}

	@Override
	public IwList getIwListById(Long listId) {
		return daoDataService.getIwListById(listId);
	}

	@Override
	public void deleteEntites(String entityName) {
		daoDataService.deleteEntites(entityName);
	}

	@Override
	public List<Object> getEntiteList(String entityName) {
		return daoDataService.getEntiteList(entityName);
	}

	@Override
	public List<Object> getEntiteList(String entityName, List attributes, List values) {
		return daoDataService.getEntiteList(entityName, attributes, values);
	}

	@Override
	public List<Object> getEntiteConfigList(String entityName, List attributes, List values) {
		return daoDataService.getEntiteConfigList(entityName, attributes, values);
	}

	@Override
	public void saveAplicationProcess(String appKey, String processKey) {
		daoDataService.saveAplicationProcess(appKey, processKey);
	}

	@Override
	public List<String> getProcessKeyByApplication(String appKey) {
		return daoDataService.getProcessKeyByApplication(appKey);
	}

	@Override
	public void deleteProcessApplication(String appKey) {
		daoDataService.deleteProcessApplication(appKey);
	}

	@Override
	public List<IwRegistre> getRegistreList() {
		return daoDataService.getRegistreList();
	}

	@Override
	public List getListResourceByType(Class clazz) {
		return daoDataService.getListResourceByType(clazz);
	}

	@Override
	public List<IwResource> allResources() {
		return daoDataService.allResources();
	}

	@Override
	public List<IwBooking> getIwBookingList() {
		return daoDataService.getIwBookingList();
	}

	@Override
	public List<IwBooking> getIwBookingForGivenResource(IwResource resourceId) {
		return daoDataService.getIwBookingForGivenResource(resourceId);
	}

	@Override
	public IwResource getResourceById(Integer id) {
		return daoDataService.getResourceById(id);
	}

	@Override
	public Long getRegistreNextVal(String idEntity) {
		IwRegistre current = (IwRegistre) getEntitytById(IwRegistre.class.getName(), idEntity);
		if (current != null) {
			Long currentValue = current.getIwValue();
			if (currentValue == null) {
				currentValue = 0L;
			}
			current.setIwValue(currentValue + 1);
			saveEntite(current);
			return current.getIwValue();
		}
		return null;
	}

	@Override
	public List<IwResourceType> getAllResourceType() {
		return daoDataService.getAllResourceType();
	}

	@Override
	public void saveIwResource(IwResource resource) {
		daoDataService.saveIwResource(resource);
	}

	@Override
	public Object getTupleFromClassById(Class clazz, Integer tupleId) {
		return daoDataService.getTupleFromClassById(clazz, tupleId);
	}

	@Override
	public List<RhCollaborateur> getRhCollaborateurList() {
		return daoDataService.getRhCollaborateurList();
	}

	@Override
	public RhCollaborateur getRhCollaborateurByCode(Integer code) {
		return daoDataService.getRhCollaborateurByCode(code);
	}

	@Override
	public IwMultilineEntity getIwMultiLineEntityByClassName(String className) {
		return daoDataService.getIwMultiLineEntityByClassName(className);
	}

	@Override
	public IwModelDetails getIwModelDetailsByModelId(String id) {
		return daoDataService.getIwModelDetailsByModelId(id);
	}

	public List<IwVariableProcess> getVariableProcess(String processKey) {
		return daoDataService.getVariableProcess(processKey);
	}

	@Override
	public BizSecteurRayon getSecteurRayonById(BizSecteur secteurId, BizRayon rayonId) {
		return daoDataService.getSecteurRayonById(secteurId, rayonId);
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskList() {
		return daoDataService.getStandAloneTaskList();
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskList(String applicationKey) {
		return daoDataService.getStandAloneTaskList(applicationKey);
	}

	@Override
	public IwStandAloneTask getStandAloneTask(Long id) {
		return daoDataService.getStandAloneTask(id);
	}

	@Override
	public void remove(IwStandAloneTask iwStandAloneTask) {
		daoDataService.remove(iwStandAloneTask);
	}

	@Override
	public void save(IwStandAloneTask iwStandAloneTask) {
		daoDataService.save(iwStandAloneTask);
	}

	@Override
	public List<IwRight> getIwRightListByStandaloneTaskId(String applicatioKey, Long standaloneTAskId) {
		return daoDataService.getIwRightListByStandaloneTaskId(applicatioKey, standaloneTAskId);
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskListByIwRight(String appKey, List<IwRight> rights) {
		return daoDataService.getStandAloneTaskListByIwRight(appKey, rights);
	}

	@Override
	public List<OrgEntiteOrganisationnelle> getOrgEntiteOrganisationnelleList() {
		return daoDataService.getOrgEntiteOrganisationnelleList();
	}

	@Override
	public List<OrgEntiteOrganisationnelle> getChildsOfGivenEntiteOrg(OrgEntiteOrganisationnelle parrent) {
		return daoDataService.getChildsOfGivenEntiteOrg(parrent);
	}

	@Override
	public void saveRhCollaborateur(RhCollaborateur rhCollaborateur) {
		daoDataService.saveRhCollaborateur(rhCollaborateur);
	}

	@Override
	public void saveOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle) {
		daoDataService.saveOrgEntiteOrganisationnelle(orgEntiteOrganisationnelle);
	}

	public List<Object> getEntityListLike(String entity, String[] fieldList, String query) {
		return daoDataService.getEntityListLike(entity, fieldList, query);
	}

	@Override
	public List<RhPosteOccupe> getRhPosteOccupeList() {
		return daoDataService.getRhPosteOccupeList();
	}

	@Override
	public OrgEntiteOrganisationnelle getOrgEntiteOrganisationnelleById(int id) {
		return daoDataService.getOrgEntiteOrganisationnelleById(id);
	}

	@Override
	public RhCollaborateur getRhCollaborateurById(int id) {
		return daoDataService.getRhCollaborateurById(id);
	}

	@Override
	public RhPosteOccupe getRhPosteOccupeById(int id) {
		return daoDataService.getRhPosteOccupeById(id);
	}

	@Override
	public void removeOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle) {
		daoDataService.removeOrgEntiteOrganisationnelle(orgEntiteOrganisationnelle);
	}

	@Override
	public void removeRhCollaborateur(RhCollaborateur rhCollaborateur) {
		daoDataService.removeRhCollaborateur(rhCollaborateur);
	}

	@Override
	public IwVariableProcess getVariableProcessById(Long id) {
		return daoDataService.getVariableProcessById(id);
	}

	@Override
	public List<IwColumnTask> getColumnTaskList() {
		return daoDataService.getColumnTaskList();
	}

	@Override
	public List<IwVariableProcess> getVariableProcessList(String processKey, Boolean isTask, Boolean isForm){
		return daoDataService.getVariableProcessList(processKey,isTask,isForm);
	}

	@Override
	public List<IwTree> getAllParent() {
		return daoDataService.getAllParent();
	}

	@Override
	public List<IwTree> getChilds(IwTree parent) {
		return daoDataService.getChilds(parent);
	}

	@Override
	public List<IwTree> getParent(IwTree level) {
		return daoDataService.getParent(level);
	}

	@Override
	public IwRight getIwRightByAppAndUser(String appKey, String userId, String processKey) {
		return daoDataService.getIwRightByAppAndUser(appKey, userId, processKey);
	}

	@Override
	public IwRight getIwRightByAppAndGroup(String appkey, String userId, String processkey) {
		return daoDataService.getIwRightByAppAndGroup(appkey, userId, processkey);
	}

	@Override
	public List<IwRight> getIwRightByAppAndUser(String appKey, String userId) {
		return daoDataService.getIwRightByAppAndUser(appKey, userId);
	}

	@Override
	public List<IwRight> getIwRightByAppAndGroup(String appKey, String groupId) {
		return daoDataService.getIwRightByAppAndGroup(appKey, groupId);
	}

	@Override
	public List<String> getGroupIdsByAppKey(String appkey) {
		return daoDataService.getGroupIdsByAppKey(appkey);
	}

	/**
	 * Get entity schema from class matadata by entity name
	 * @param entity
	 * @return
	 */
	@Override
	public Object getEntitySchema(String entity) {
		String[] relations = new String[] {"ManyToMany","OneToMany","ManyToOne","OneToOne"};
		IwMultilineEntity multiLineEntity = getEntityByName(entity);
		if(multiLineEntity != null) {
			ObjectNode entityInfo = new ObjectMapper().createObjectNode();
			try {
				Class<?> className = Class.forName(multiLineEntity.getIwClass());
				entityInfo.put("name", multiLineEntity.getIwName());
				entityInfo.put("label", multiLineEntity.getIwName());
				entityInfo.put("gender", String.valueOf(EntityInfo.Gender.MASCULINE));
				/**
				 * Get entity metadata like label, description ...
				 */
				if(className.isAnnotationPresent(EntityInfo.class)) {
					EntityInfo entityInfoAnnotation = className.getAnnotation(EntityInfo.class);
					entityInfo.put("label", entityInfoAnnotation.name());
					entityInfo.put("gender", String.valueOf(entityInfoAnnotation.gender()));
					entityInfo.put("description", entityInfoAnnotation.description());
					entityInfo.put("columns", entityInfoAnnotation.columns());
				}
				/**
				 * Get columns fields
				 */
				ArrayNode fields = entityInfo.putArray("fields");
				for (Field field : className.getDeclaredFields()) {
					/**
					 * Ignore serialVersionUID field
					 */
					if ("serialVersionUID".equals(field.getName()))
						continue;
					ObjectNode columnInfo = new ObjectMapper().createObjectNode();
					columnInfo.put("name", field.getName());
					columnInfo.put("label", field.getName());
					columnInfo.put("type", String.valueOf(field.getType().getName()));
					columnInfo.put("genericType", String.valueOf(field.getGenericType()));
					/**
					 * Check if the field is the primary key of the entity
					 */
					if(field.isAnnotationPresent(Id.class)) {
						columnInfo.put("primaryKey", true);
						entityInfo.put("primaryKey", field.getName());
					}
					/**
					 * Get column's specific annotations
					 */
					Annotation[] declaredAnnotations = field.getDeclaredAnnotations();

					if(field.isAnnotationPresent(ColumnInfo.class)) {
						ColumnInfo fieldAnnotation = field.getAnnotation(ColumnInfo.class);
						columnInfo.put("label", fieldAnnotation.label());
					}
					/**
					 * Handle associations
					 */
					if(field.isAnnotationPresent(ManyToMany.class)) {
						ObjectNode association = new ObjectMapper().createObjectNode();
						association.put("type", "ManyToMany");
						columnInfo.put("association", association);
						continue;
					}
					if(field.isAnnotationPresent(ManyToOne.class)) {
						Class<?> type = field.getType();
						ObjectNode association = new ObjectMapper().createObjectNode();
						association.put("type", "ManyToOne");
						association.put("className", String.valueOf(type.getSimpleName()));
						if(type.isAnnotationPresent(EntityInfo.class)) {
							EntityInfo associationInfoAnnotation = type.getAnnotation(EntityInfo.class);
							association.put("label", associationInfoAnnotation.associationLabel());
						}
						columnInfo.put("type", "association");
						columnInfo.put("association", association);
					}
					if(field.isAnnotationPresent(OneToMany.class)) {
						ObjectNode association = new ObjectMapper().createObjectNode();
						association.put("type", "OneToMany");
						columnInfo.put("association", association);
						continue;
					}
					if(field.isAnnotationPresent(OneToOne.class)) {
						ObjectNode association = new ObjectMapper().createObjectNode();
						association.put("association", "OneToOne");
						columnInfo.put("links", association);
						continue;
					}
					fields.add(columnInfo);
				}
				return entityInfo;
			} catch (ClassNotFoundException ex) {
				LogManager.getLogger(DaoBusinessServiceImpl.class.getName()).error("Java Reflection Error", ex);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get entity by name
	 * @param entity
	 * @return
	 */
	@Override
	public IwMultilineEntity getEntityByName(String entity) {
		return daoDataService.getEntityByName(entity);
	}

	/**
	 *
	 * @param entity
	 * @param criteria
	 * @return
	 */
	@Override
	public List<Object> getEntityItems(String entity, String... criteria) {
		// Check if the MultiLineEntity exists
		IwMultilineEntity multiLineEntity = getEntityByName(entity);
		if(multiLineEntity != null) {
			try {
				Class entityClass = Class.forName(multiLineEntity.getIwClass());
				List<Object> items = daoDataService.getEntityItems(entityClass, criteria);
				return items;
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, "Entity class not found", ex);
			}
		}
		return null;
	}

	/**
	 *
	 * @param entity
	 * @param id
	 * @return
	 */
	@Override
	public Object getEntityItem(String entity, String id) {
		// Check if the MultiLineEntity exists
		IwMultilineEntity multiLineEntity = getEntityByName(entity);
		if(multiLineEntity != null) {
			Object item = daoDataService.getEntityItem(multiLineEntity, id);
			return item;
		}
		return null;
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Object saveEntity(String entity, Object body) {
		IwMultilineEntity multiLineEntity = getEntityByName(entity);
		if(multiLineEntity != null) {
			try {
				Class entityClass = Class.forName(multiLineEntity.getIwClass());
				Object object = new ObjectMapper().convertValue(body, entityClass);
				daoDataService.saveEntity(entityClass, object);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, "Entity class not found", ex);
			}
		}
		return null;
	}

	@Override
	public Boolean deleteEntity(String entity, String id) {
		IwMultilineEntity multiLineEntity = getEntityByName(entity);
		if(multiLineEntity != null) {
			try {
				Class entityClass = Class.forName(multiLineEntity.getIwClass());
				return daoDataService.deleteEntity(entityClass, id);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, "Entity class not found", ex);
			}
		}
		return false;
	}


	@Override
	public List<Object> getObjectBySQL(String request, Class clazz) {
		return daoDataService.getObjectBySQL(request, clazz);
	}

	@Override
	public IwList getIwListByName(String name) {
		IwList iwList = daoDataService.getIwListByName(name);
		return iwList;
	}

	@Override
	public List<IwVariableTemplate> getVariableTemplateList() {
		return daoDataService.getVariableTemplateList();
	}

	@Override
	public RhCollaborateur getRhCollaborateurByActIdUser(String actIdUser){
		return daoDataService.getRhCollaborateurByActIdUser( actIdUser);
	}
	@Override
	public List<RhConge> getCongesByUser(int colloborateur) {
		return daoDataService.getAllCongesByUser(colloborateur);
	}

	@Override
	public RhCollaborateur getColloborateurByMatricule(int matricule) {
		return daoDataService.getColloborateurByMatricule(matricule);
	}

	@Override
	public void saveObjectAsEntity(Object obj){
		daoDataService.saveObjectAsEntity(obj);
	}

	@Override
	public RhCollaborateur getCollaborateurByActIdUser(String actIdUser) {
		return daoDataService.getCollaborateurByActIdUser(actIdUser);
	}

	@Override
	public RhFormation getRhFormationByAldFormationId(Integer aldFormationId) {
		return daoDataService.getRhFormationByAldFormationId(aldFormationId);
	}

	@Override
	public int getNombreDemandes() {
		return  daoDataService.getNombreDemandes();
	}

	@Override
	public int getNombreCommandes() {
		return  daoDataService.getNombreCommandes();
	}

	@Override
	public List<String> getTaskByFormBuilderId(String formId) {
		return daoDataService.getTaskByFormBuilderId(formId);
	}


}
