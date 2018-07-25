package com.imaginepartners.imagineworkflow.services;

import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.business.BizFamille;
import com.imaginepartners.imagineworkflow.models.business.BizRayon;
import com.imaginepartners.imagineworkflow.models.business.BizSecteur;
import com.imaginepartners.imagineworkflow.models.business.BizSecteurRayon;
import com.imaginepartners.imagineworkflow.models.rh.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Task;
import org.springframework.security.core.GrantedAuthority;

public interface BusinessService {

	/**
	 * pour changer le logo, le theme de l'app.
	 * les valuers sont enregistrées dans l'entité IWConfig
	 *
	 * @param configName il faut passer le nom de la configuration sur laquel le changement s'appliquer.
	 * @return IwConfig Object.
	 */
	IwConfig getConfig(String configName);

	/**
	 * récupérer la valeur de la config demander.
	 *
	 * @param configName le nom de la configuration comme par exemple: company_logo
	 * @return la valeur de la configuration demander: algeria.png par exemple.
	 */
	String getConfigValue(String configName);


	/**
	 * créer un nouveau IwForm objet.
	 *
	 * @return IwForm Object.
	 */
	IwForm newIwForm();

	/**
	 * @param iwForm sauvegarder cet objet comme iWForm.
	 * @return si le sauvegarde a bien exécuter, le IwForm va retourner avec l'id attribué.
	 */
	IwForm saveIwForm(IwForm iwForm);

	/**
	 * @param iwFormId récupérer un objet iwForm à travert un id
	 * @return iwForm.
	 */
	IwForm getIwForm(Long iwFormId);

	/**
	 * @return une liste de toutes les IwForm
	 */
	List<IwForm> getIwFormList();

	/**
	 * @param keys liste des IDs de l'entité IwForm.
	 * @return retourner une liste des objets de type IwForm.
	 */
	List<IwForm> getIwFormListByFormKey(Set<String> keys);

	/**
	 * @param iwForm un objet de type iwForm.
	 * @return retourner une liste des IwInput qui correspends à IWForm donner.
	 */
	List<IwInput> getIwInputByFormId(IwForm iwForm);

	IwInput getIwInputById(String id);

	/**
	 * @param realid nous avons utilisé un champ de realId,
	 *               pour que nous puissions manupiler les id au niveau de form builder
	 * @return la ligne (row) qui correspond au realId donné.
	 */
	IwInput getIwInputByRealId(Long realid);

	void deleteIwForm(Long formId);

	void saveIwUserDetails(IwUserDetails userDetails);

	IwUserDetails getIwUserDetails(String userId);


	void saveIwGroupHierarchy(IwGroupHierarchy groupeHierarchy);

	List<IwGroupHierarchy> getIwGroupHierarchyList();

	IwGroupHierarchy getIwGroupHierarchyByGroupAndParent(String group, String parent);

	List<IwGroupHierarchy> getIwGroupHierarchyByParent(String parent);

	IwGroupHierarchy getIwGroupHierarchyByGroup(String group);

	int removeAllIwGroupHierarchy();


	/**
	 * sauvegarder un upload
	 *
	 * @param iwUpload
	 */
	void saveIwUpload(IwUpload iwUpload);

	/**
	 * @param uploadId
	 * @return une liste de fichiers associes à un upload.
	 */
	List<IwFile> getIwFilesForUpload(Long uploadId);

	IwUpload getIwUpload(Long uploadId);

	List<IwEvent> getIwEventForUser(String userId);

	/**
	 * lors d'utilisation d'agenda, nous allons utiliser l'entité IwEvent
	 *
	 * @param iwEvent
	 */
	void saveIwEvent(IwEvent iwEvent);

	void deleteIwEvent(IwEvent iwEvent);

	List<IwCar> getCarsList();

	void saveIwBooking(IwBooking iwBooking);

	void deleteIwBooking(IwBooking iwEvent);

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

	List<IwApplication> getApplicaitonList();

	List<IwApplication> getUserApplicaitonList(List<String> appKies);

	IwApplication getApplicationByKey(String key);

	IwApplication getApplicationByProcess(String processKey);

	IwApplication getApplicationById(Long id);

	void saveIwApplication(IwApplication application);

	void deleteIwApplication(Long applicationId);

	List<IwMultilineEntity> getMultiLineEntityList();

	/**
	 * une méthode générique qui nous permet de récupérer un tuple.
	 *
	 * @param className le nom de la classe. full Name.
	 *                  c'est à dire: le nom_de_package.nom_de_classe
	 * @param idEntity  un id de ligne
	 * @return un objet de type @param: className.
	 */
	Object getEntitytById(String className, String idEntity);

	void saveEntityList(List entityList);

	/**
	 * récupérer les colonnes d'une entité donnée.
	 *
	 * @param entityName le nom complet de l'entité.
	 * @return une liste d'objet de type IwMultilineEntityField,
	 * comporte les informations sur une entité donnée.
	 */
	List<IwMultilineEntityField> getFieldByEntityList(String entityName);

	/**
	 * @param entityName le nom complet de l'entité.
	 * @param fieldName  le nom de champs.
	 * @return récupérer que le champs de déscription d'une entité donnée
	 */
	String getDescriptionFiledEntity(String entityName, String fieldName);

	void saveEntite(Object entity);

	void removeEntite(Object entity);

	void removeList(Object entity);

	/**
	 * récupérer les champs d'une entité demandée.
	 * on représente ces champs la sous form d'une entité IwMultilineEntityField.
	 *
	 * @param entityId avec le nom complet d'une entité,
	 * @return une liste de type IwMultilineEntityField
	 */
	List<IwMultilineEntityField> getEntityFieldList(String entityId);

	List<IwRight> getIwRightList(String userId, Collection<GrantedAuthority> autorities);

	void saveRights(List<IwRight> rightAddList, List<IwRight> rightRemoveList);

	List<IwRight> getIwRightList(String applicatioKey);

	List<IwRight> getIwRightList(String applicatioKey, String processKey);

	List<IwList> getIwListList();

	List<IwListOptions> getIwListOptionsListByIwList(IwList list);

	List<IwVariableProcess> getVariableProcess(String processKey, Boolean isTask, Boolean isForm);

	void deleteObject(Object obj);

	List<IwFormTemplate> getIwFormTemplateList();

	List<BizSecteur> getSectorList();

	List<BizRayon> getRayonList();

	public BizSecteurRayon getSecteurRayonById(BizSecteur secteurId, BizRayon rayonId);

	List<IwVariableProcess> getVariableProcessByProcessKeyAndIwinputs(String processKey, List<IwInput> inputList);

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

	String getConfigValueOrDefault(String configName);

	Long getRegistreNextVal(String idEntity);

	List<IwResourceType> getAllResourceType();

	void saveIwResource(IwResource resource);

	Object getTupleFromClassById(Class clazz, Integer tupleId);

	List<RhCollaborateur> getRhCollaborateurList();

	RhCollaborateur getRhCollaborateurByCode(Integer code);

	IwModelDetails getIwModelDetailsByModelId(String id);

	public IwMultilineEntity getIwMultiLineEntityByClassName(String className);

	public List<IwVariableProcess> getVariableProcess(String processKey);

	public List<IwStandAloneTask> getStandAloneTaskList();

	public List<IwStandAloneTask> getStandAloneTaskList(String applicationKey);

	public IwStandAloneTask getStandAloneTask(Long id);

	public void remove(IwStandAloneTask iwStandAloneTask);

	public void save(IwStandAloneTask iwStandAloneTask);

	public List<IwRight> getIwRightListByStandaloneTaskId(String applicatioKey, Long standaloneTAskId);

	public List<IwStandAloneTask> getStandAloneTaskListByIwRight(String appKey, List<IwRight> rights);

	List<OrgEntiteOrganisationnelle> getOrgEntiteOrganisationnelleList();

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

	Object getEntitySchema(String entity);

	IwMultilineEntity getEntityByName(String entity);

	List<Object> getEntityItems(String entity, String... criteria);

	Object getEntityItem(String entity, String id);

	Object saveEntity(String entity, Object body);

	Boolean deleteEntity(String entity, String id);

	List<Object> getObjectBySQL(String request, Class clazz);

	IwList getIwListByName(String name);

	List<IwVariableTemplate> getVariableTemplateList();

	public RhCollaborateur getRhCollaborateurByActIdUser(String actIdUser);

	public List<RhConge> getCongesByUser(int colloborateur);

	public RhCollaborateur getColloborateurByMatricule(int matricule);

	public void saveObjectAsEntity(Object obj);

	public RhCollaborateur getCollaborateurByActIdUser(String actIdUser);

	public RhFormation getRhFormationByAldFormationId(Integer aldFormationId);

	public int getNombreDemandes();
	public int getNombreCommandes();

	public List<String> getTaskByFormBuilderId(String formId);
}
