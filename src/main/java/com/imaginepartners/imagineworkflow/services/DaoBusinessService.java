package com.imaginepartners.imagineworkflow.services;

import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.business.*;
import com.imaginepartners.imagineworkflow.models.rh.*;
import org.activiti.engine.task.Task;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DaoBusinessService {

	IwConfig getConfig(String configName);

	IwForm newIwForm();

	IwForm saveIwForm(IwForm iwForm);

	IwForm getIwForm(Long iwFormId);

	List<IwForm> getIwFormList();

	List<IwForm> getIwFormListByFormKey(Set<String> keys);

	List<IwInput> getIwInputByFormId(IwForm iwForm);

	IwInput getIwInputById(String id);

	public IwInput getIwInputByRealId(Long realid);

	void deleteIwForm(Long formId);

	void saveIwUserDetails(IwUserDetails userDetails);

	IwUserDetails getIwUserDetails(String userId);

	void saveIwGroupHierarchy(IwGroupHierarchy groupeHierarchy);

	List<IwGroupHierarchy> getIwGroupHierarchyList();

	IwGroupHierarchy getIwGroupHierarchyByGroupAndParent(String group, String parent);

	List<IwGroupHierarchy> getIwGroupHierarchyByParent(String parent);

	IwGroupHierarchy getIwGroupHierarchyByGroup(String group);

	int removeAllIwGroupHierarchy();

	void saveIwUpload(IwUpload iwUpload);

	List<IwFile> getIwFilesForUpload(Long uploadId);

	IwUpload getIwUpload(Long uploadId);

	List<IwEvent> getIwEventForUser(String userId);

	void saveIwEvent(IwEvent iwEvent);

	void deleteIwEvent(IwEvent iwEvent);

	BigDecimal getIwProgress(String procKey, String taskId);

	BigDecimal getIwProgressEnd(String procKey, String taskId);

	List<IwCar> getCarsList();

	void saveIwBooking(IwBooking iwBooking);

	void deleteIwBooking(IwBooking iwBooking);

	List<IwBooking> geIwBookingList();

	List<IwBooking> getIwBookingList();

	List<IwBooking> getIwBookingForGivenResource(IwResource resourceId);

	List<IwBooking> getBookingListForResourceByDate(Integer resourceId, Date startDate, Date endDate);

	IwGroupDetails getGroupDetails(String idGroup);

	void saveIwGroupDetails(IwGroupDetails iwGroupDetails);

	IwProgress getIwProgressProcessTask(String procKey, String taskId);

	void saveIwProgress(IwProgress p);

	void saveIwVariableProcess(IwVariableProcess Vp);

	String getResponsibleForUser(String userId);

	List<IwPhase> getPhaseList();

	void savePhase(IwPhase currentPhase);

	void removePhase(IwPhase phase);

	void removeIwGroupDetail(String groupId);

	void saveIwLicense(IwLicense iwLicense);

	List<IwLicense> getIwLicenseList();

	IwLicense getIwLicense(Integer iwLicenseId);

	void deleteIwLicense(Integer iwLicenseId);

	IwLicense getLastActiveIwLicense();

	List<IwConfig> getConfigList(List<String> configNames);

	IwConfig saveIwConfig(IwConfig iwConfig);

	void deleteIwUserDetails(String userId);

	List<IwApplication> getApplicationList();

	IwApplication getApplicationByKey(String key);

	IwApplication getApplicationByProcess(String processKey);

	IwApplication getApplicationById(Long id);

	void saveIwApplication(IwApplication application);

	void deleteIwApplication(Long applicationId);

	List<IwMultilineEntity> getMultiLineEntityList();

	Object getEntitytById(String className, String idEntity);

	void saveEntityList(List entityList);

	List<IwMultilineEntityField> getFieldByEntityList(String entityName);

	String getDescriptionFiledEntity(String entityName, String fieldName);

	void saveEntite(Object entity);

	void removeEntite(Object entity);

	void removeList(Object entity);

	List<IwMultilineEntityField> getEntityFieldList(String entityId);

	List<IwRight> getIwRightList(String userId, Collection<GrantedAuthority> autorities);

	void saveRights(List<IwRight> rightAddList, List<IwRight> rightRemoveList);

	List<IwRight> getIwRightList(String applicatioKey);

	List<IwRight> getIwRightList(String applicatioKey, String processKey);

	List<IwApplication> getUserApplicaitonList(List<String> appKies);

	List<IwList> getIwListList();

	List<IwListOptions> getIwListOptionsListByIwList(IwList list);

	List<IwVariableProcess> getVariableProcess(String processKey, Boolean isTask, Boolean isForm);

	List<IwVariableProcess> getVariableProcessByProcessKeyAndIwinputs(String processKey, List<IwInput> inputList);

	void deleteObject(Object obj);

	List<IwFormTemplate> getIwFormTemplateList();

	List<BizSecteur> getSectorList();

	List<BizRayon> getRayonList();

	public BizSecteurRayon getSecteurRayonById(BizSecteur secteurId, BizRayon rayonId);

	List<BizFamille> getFamilyList();

	public List<BizSecteurRayon> getRayonListForSector(Integer sectorId);

	public List<BizSecteurRayon> getSecteurRayonList(BizSecteur sectorId);

	List<Object> getEntityListLike(String entity, String field, String query);

	IwList getIwListById(Long listId);

	void deleteEntites(String entityName);

	List<Object> getEntiteList(String entityName);

	public List<Object> getEntiteList(String entityName, List attributes, List values);

	public List<Object> getEntiteConfigList(String entityName, List attributes, List values);

	void saveAplicationProcess(String appKey, String processKey);

	List<String> getProcessKeyByApplication(String appKey);

	void deleteProcessApplication(String appKey);

	List<IwRegistre> getRegistreList();

	List getListResourceByType(Class clazz);

	List<IwResource> allResources();

	IwResource getResourceById(Integer id);

	List<IwResourceType> getAllResourceType();

	void saveIwResource(IwResource resource);

	Object getTupleFromClassById(Class clazz, Integer tupleId);

	List<RhCollaborateur> getRhCollaborateurList();

	RhCollaborateur getRhCollaborateurByCode(Integer code);

	IwModelDetails getIwModelDetailsByModelId(String id);

	public List<IwVariableProcess> getVariableProcess(String processKey);

	public IwMultilineEntity getIwMultiLineEntityByClassName(String className);

	public List<IwStandAloneTask> getStandAloneTaskList();

	public List<IwStandAloneTask> getStandAloneTaskList(String applicationKey);

	public IwStandAloneTask getStandAloneTask(Long id);

	public void remove(IwStandAloneTask iwStandAloneTask);

	public void save(IwStandAloneTask iwStandAloneTask);

	public List<IwRight> getIwRightListByStandaloneTaskId(String applicatioKey, Long standaloneTAskId);

	public List<IwStandAloneTask> getStandAloneTaskListByIwRight(String appKey, List<IwRight> rights);

	public List<OrgEntiteOrganisationnelle> getOrgEntiteOrganisationnelleList();

	public List<OrgEntiteOrganisationnelle> getChildsOfGivenEntiteOrg(OrgEntiteOrganisationnelle parrent);

	void saveRhCollaborateur(RhCollaborateur rhCollaborateur);

	void saveOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle);

	public List<Object> getEntityListLike(String entity, String[] fieldList, String query);

	List<RhPosteOccupe> getRhPosteOccupeList();

	public OrgEntiteOrganisationnelle getOrgEntiteOrganisationnelleById(int id);

	public RhCollaborateur getRhCollaborateurById(int id);

	public RhPosteOccupe getRhPosteOccupeById(int id);

	public void removeOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle);

	public void removeRhCollaborateur(RhCollaborateur rhCollaborateur);

	public IwVariableProcess getVariableProcessById(Long id);

	public List<IwTree> getAllParent();

	public List<IwTree> getChilds(IwTree parent);

	public List<IwTree> getParent(IwTree level);

	public List<IwColumnTask> getColumnTaskList();

	public List<IwVariableProcess> getVariableProcessList(String processKey, Boolean isTask, Boolean isForm);

	public IwRight getIwRightByAppAndUser(String appKey, String userId, String processKey);

	public IwRight getIwRightByAppAndGroup(String appkey, String userId, String processkey);

	public List<IwRight> getIwRightByAppAndUser(String appKey, String userId);

	public List<IwRight> getIwRightByAppAndGroup(String appKey, String groupId);

	public List<String> getGroupIdsByAppKey(String appkey);

	public IwMultilineEntity getEntityByName(String entity);

	List<Object> getEntityItems(Class className, String... criteria);

	Object getEntityItem(IwMultilineEntity multiLineEntity, String id);

	void saveEntity(Class className, Object body);

	void updateEntity(Class className, Object body);

	Boolean deleteEntity(Class className, String id);

	List<Object> getObjectBySQL(String request, Class clazz);

	IwList getIwListByName(String name);

	List<IwVariableTemplate> getVariableTemplateList();

	public RhCollaborateur getRhCollaborateurByActIdUser(String actIdUser);

	public List<RhConge> getAllCongesByUser(int idCollobaratteur);

	public RhCollaborateur getColloborateurByMatricule(int matricule);

	public void saveObjectAsEntity(Object obj);

	public RhCollaborateur getCollaborateurByActIdUser(String actIdUser);

	public RhFormation getRhFormationByAldFormationId(Integer aldFormationId);

	public int getNombreDemandes();
	public int getNombreCommandes();

	public List<String> getTaskByFormBuilderId(String formId);



}
