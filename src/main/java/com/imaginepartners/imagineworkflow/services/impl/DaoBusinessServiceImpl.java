package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.business.*;
import com.imaginepartners.imagineworkflow.models.rh.*;
import com.imaginepartners.imagineworkflow.services.DaoBusinessService;
import com.imaginepartners.imagineworkflow.util.Util;
import org.activiti.engine.task.Task;
import org.apache.log4j.LogManager;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class DaoBusinessServiceImpl implements DaoBusinessService, Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		if (sessionFactory.getCurrentSession() == null) {
			sessionFactory.openSession();
		}
		return sessionFactory.getCurrentSession();
	}

	@Override
	public IwConfig getConfig(String configName) {
		List<IwConfig> result = (List<IwConfig>) getCurrentSession().getNamedQuery("IwConfig.findByConfigName").setString("configName", configName).setMaxResults(1).list();
		if (result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}

	@Override
	public IwForm newIwForm() {
		IwForm iwForm = new IwForm();
		return iwForm;
	}

	@Override
	public IwForm saveIwForm(IwForm iwForm) {
		getCurrentSession().saveOrUpdate(iwForm);
		getCurrentSession().flush();
		return iwForm;
	}

	@Override
	public IwForm getIwForm(Long iwFormId) {
		IwForm form = (IwForm) getCurrentSession().get(IwForm.class, iwFormId);
		LogManager.getLogger(DaoBusinessServiceImpl.class).debug(DaoBusinessServiceImpl.class);
		return form;
	}

	@Override
	public List<IwForm> getIwFormList() {
		return (List<IwForm>) getCurrentSession()
		.createCriteria(IwForm.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.addOrder(Order.asc("iwListingIndex"))
		.addOrder(Order.desc("iwLastUpdateTime"))
		.list();
	}

	@Override
	public List<IwForm> getIwFormListByFormKey(Set<String> keys) {
		String formKey = "";
		IwForm iwform = new IwForm();
		Long formKeyLong;
		List<IwForm> lisToreturn = new ArrayList<IwForm>();
		Iterator i = keys.iterator();
		while (i.hasNext()) {
			formKey = (String) i.next();

			formKeyLong = Long.valueOf(formKey);
			iwform = (IwForm) getCurrentSession().get(IwForm.class, formKeyLong);
			lisToreturn.add(iwform);
		}
		return lisToreturn;
	}

	@Override
	public List<IwInput> getIwInputByFormId(IwForm iwForm) {
		List<IwInput> inputlist = new ArrayList<IwInput>();
		inputlist = getCurrentSession().createCriteria(IwInput.class).add(Restrictions.eq("iwForm", iwForm)).list();
		return inputlist;
	}

	@Override
	public IwInput getIwInputById(String id) {
		return (IwInput) getCurrentSession().get(IwInput.class, id);
	}

	@Override
	public IwInput getIwInputByRealId(Long realid) {
		return (IwInput) getCurrentSession().get(IwInput.class, realid);
	}

	@Override
	public void deleteIwForm(Long formId) {
		getCurrentSession().delete(getCurrentSession().load(IwForm.class, formId));
	}

	public void saveIwUserDetails(IwUserDetails userDetails) {
		getCurrentSession().saveOrUpdate(userDetails);
		getCurrentSession().flush();
	}

	@Override
	public IwUserDetails getIwUserDetails(String userId) {
		List<IwUserDetails> detailsList = (List<IwUserDetails>) getCurrentSession()
		.getNamedQuery("IwUserDetails.findByIwActIdUser")
		.setMaxResults(1)
		.setString("iwActIdUser", userId).list();
		if (detailsList.isEmpty()) {
			return null;
		} else {
			return detailsList.get(0);
		}
	}

	@Override
	public void saveIwGroupHierarchy(IwGroupHierarchy groupeHierarchy) {
		getCurrentSession().saveOrUpdate(groupeHierarchy);
		getCurrentSession().flush();
	}

	@Override
	public List<IwGroupHierarchy> getIwGroupHierarchyList() {
		return (List<IwGroupHierarchy>) getCurrentSession().getNamedQuery("IwGroupHierarchy.findAll").list();
	}

	@Override
	public IwGroupHierarchy getIwGroupHierarchyByGroupAndParent(String group, String parent) {
		List<IwGroupHierarchy> list = (List<IwGroupHierarchy>) getCurrentSession()
		.getNamedQuery("IwGroupHierarchy.findByIwGroupAndIwParant")
		.setString("iwGroup", group)
		.setString("iwParant", parent).setMaxResults(1).list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<IwGroupHierarchy> getIwGroupHierarchyByParent(String parent) {
		List<IwGroupHierarchy> list = (List<IwGroupHierarchy>) getCurrentSession()
		.getNamedQuery("IwGroupHierarchy.findByIwParant")
		.setString("iwParant", parent).list();

		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}

	@Override
	public IwGroupHierarchy getIwGroupHierarchyByGroup(String group) {
		List<IwGroupHierarchy> list = (List<IwGroupHierarchy>) getCurrentSession()
		.getNamedQuery("IwGroupHierarchy.findByIwGroup")
		.setString("iwGroup", group).setMaxResults(1).list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int removeAllIwGroupHierarchy() {
		Class<?> c = IwGroupHierarchy.class;
		String hql = "delete from " + IwGroupHierarchy.class.getSimpleName();
		Query query = getCurrentSession().createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public void saveIwUpload(IwUpload iwUpload) {
		getCurrentSession().saveOrUpdate(iwUpload);
		getCurrentSession().flush();
	}

	@Override
	public List<IwFile> getIwFilesForUpload(Long uploadId) {
		return (List<IwFile>) getCurrentSession().createCriteria(IwFile.class)
		.createAlias("iwUpload", "iwUpload")
		.add(Restrictions.eq("iwUpload.iwUploadId", uploadId))
		.list();
	}

	@Override
	public IwUpload getIwUpload(Long uploadId) {
		List<IwUpload> uploadList = (List<IwUpload>) getCurrentSession()
		.getNamedQuery("IwUpload.findByIwUploadId")
		.setLong("iwUploadId", uploadId)
		.setMaxResults(1)
		.list();
		if (!uploadList.isEmpty()) {
			IwUpload iwUpload = uploadList.get(0);
			return iwUpload;
		} else {
			return null;
		}
	}

	@Override
	public List<IwEvent> getIwEventForUser(String userId) {
		return getCurrentSession()
		.createCriteria(IwEvent.class)
		.add(
		Restrictions
		.or(Restrictions.eq("iwUser", userId), Restrictions.and(Restrictions.isNull("iwUser"), Restrictions.isNull("iwGroup")))).list();
	}

	@Override
	public void saveIwEvent(IwEvent iwEvent) {
		getCurrentSession().saveOrUpdate(iwEvent);
		getCurrentSession().flush();
	}

	@Override
	public void deleteIwEvent(IwEvent iwEvent) {
		getCurrentSession().delete(getCurrentSession().load(IwEvent.class, iwEvent.getIwEventId()));
	}

	@Override
	public BigDecimal getIwProgress(String procKey, String taskId) {
		List<IwProgress> iwProgressList = getCurrentSession().createCriteria(IwProgress.class)
		.add(Restrictions.eq("iwProcKey", procKey))
		.add(Restrictions.eq("iwTask", taskId))
		.list();
		if (iwProgressList.isEmpty()) {
			return BigDecimal.ZERO;
		}
		return iwProgressList.get(0).getIwProgressRate();
	}

	@Override
	public BigDecimal getIwProgressEnd(String procKey, String taskId) {
		List<IwProgress> iwProgressList = getCurrentSession().createCriteria(IwProgress.class)
		.add(Restrictions.eq("iwProcKey", procKey))
		.add(Restrictions.eq("iwTask", taskId))
		.list();
		if (iwProgressList.isEmpty()) {
			return BigDecimal.ZERO;
		}
		return iwProgressList.get(0).getIwProgressEnd();
	}

	@Override
	public List<IwCar> getCarsList() {
		return getCurrentSession().createCriteria(IwCar.class).list();
	}

	@Override
	public void saveIwBooking(IwBooking iwBooking) {
		getCurrentSession().saveOrUpdate(iwBooking);
		getCurrentSession().flush();
	}

	@Override
	public void saveIwGroupDetails(IwGroupDetails iwGroupDetails) {
		getCurrentSession().saveOrUpdate(iwGroupDetails);
		getCurrentSession().flush();
	}

	@Override
	public void deleteIwBooking(IwBooking iwBooking) {
		getCurrentSession().delete(getCurrentSession().load(IwBooking.class, iwBooking.getIwBookingId()));
	}

	@Override
	public List<IwBooking> geIwBookingList() {
		return getCurrentSession().createCriteria(IwBooking.class).list();
	}

	@Override
	public List<IwBooking> getBookingListForResourceByDate(Integer resourceId, Date startDate, Date endDate) {
		return getCurrentSession()
		.createCriteria(IwBooking.class)
		.add(Restrictions.between("iwStartTime", startDate, endDate))
		.createAlias("iwResourceId", "iwResourceId")
		.add(Restrictions.eq("iwResourceId.idResource", resourceId))
		.list();
	}

	@Override
	public IwGroupDetails getGroupDetails(String idGroup) {
		List<IwGroupDetails> list = getCurrentSession().createCriteria(IwGroupDetails.class)
		.add(Restrictions.eq("iwGroup", idGroup))
		.list();

		if (!list.isEmpty()) {
			return list.get(0);
		}
		// Create a new DetailGroup if don't exist
		IwGroupDetails groupDetails = new IwGroupDetails();
		groupDetails.setIwGroup(idGroup);
		groupDetails.setIwActive(true);
		getCurrentSession().save(groupDetails);
		return groupDetails;
	}

	@Override
	public IwProgress getIwProgressProcessTask(String procKey, String taskId) {
		List<IwProgress> iwProgressList = getCurrentSession().createCriteria(IwProgress.class)
		.add(Restrictions.eq("iwProcKey", procKey))
		.add(Restrictions.eq("iwTask", taskId))
		.list();
		if (iwProgressList.isEmpty()) {
			return null;
		}
		return iwProgressList.get(0);
	}

	@Override
	public void saveIwProgress(IwProgress p) {
		getCurrentSession().saveOrUpdate(p);
	}

	@Override
	public void saveIwVariableProcess(IwVariableProcess Vp) {
		getCurrentSession().saveOrUpdate(Vp);
		getCurrentSession().flush();
	}

	@Override
	public String getResponsibleForUser(String userId) {
		Query query = getCurrentSession()
		.createSQLQuery("SELECT detailGroup.IW_RESPONSIBLE "
		+ "FROM "
		+ "IW_GROUP_DETAILS detailGroup , ACT_ID_MEMBERSHIP memberShip "
		+ "WHERE memberShip.USER_ID_ =:userId and "
		+ "detailGroup.IW_GROUP=memberShip.GROUP_ID_"
		+ " and detailGroup.IW_RESPONSIBLE is not null"
		+ " and detailGroup.IW_RESPONSIBLE <>:userId")
		.setParameter("userId", userId);
		List<String> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<IwPhase> getPhaseList() {
		return getCurrentSession().createCriteria(IwPhase.class).list();
	}

	@Override
	public void savePhase(IwPhase currentPhase) {
		getCurrentSession().saveOrUpdate(currentPhase);
		getCurrentSession().flush();
	}

	@Override
	public void removePhase(IwPhase phase) {
		getCurrentSession().delete(phase);
	}

	@Override
	public void removeIwGroupDetail(String groupId) {
		IwGroupDetails groupDetail = (IwGroupDetails) getCurrentSession().createCriteria(IwGroupDetails.class).add(Restrictions.eq("iwGroup", groupId)).list().get(0);
		if (groupDetail != null) {
			getCurrentSession().delete(groupDetail);
		}
	}

	@Override
	public void saveIwLicense(IwLicense iwLicense) {
		getCurrentSession().saveOrUpdate(iwLicense);
		getCurrentSession().flush();
	}

	@Override
	public List<IwLicense> getIwLicenseList() {
		return getCurrentSession().createCriteria(IwLicense.class).list();
	}

	@Override
	public IwLicense getLastActiveIwLicense() {
		List<IwLicense> iwLicenseList = getCurrentSession()
		.createCriteria(IwLicense.class)
		.add(Restrictions.eq("iwActive", true))
		.addOrder(Order.desc("iwDate"))
		.setMaxResults(1)
		.list();
		if (!iwLicenseList.isEmpty()) {
			return iwLicenseList.get(0);
		}
		return null;
	}

	@Override
	public IwLicense getIwLicense(Integer iwLicenseId) {
		IwLicense iwLicense = (IwLicense) getCurrentSession().get(IwLicense.class, iwLicenseId);
		return iwLicense;
	}

	@Override
	public void deleteIwLicense(Integer iwLicenseId) {
		getCurrentSession().delete(getCurrentSession().load(IwLicense.class, iwLicenseId));
	}

	@Override
	public List<IwConfig> getConfigList(List<String> configNames) {
		return getCurrentSession().createCriteria(IwConfig.class)
		.add(Restrictions.in("configName", configNames))
		.list();
	}

	@Override
	public IwConfig saveIwConfig(IwConfig iwConfig) {
		getCurrentSession().saveOrUpdate(iwConfig);
		getCurrentSession().flush();
		return iwConfig;
	}

	public IwMultiLine getIwMultiLine(Long id) {
		return (IwMultiLine) getCurrentSession().get(IwMultiLine.class, id);
	}

	@Override
	public void deleteIwUserDetails(String userId) {
		List<IwUserDetails> userDetailsList = getCurrentSession()
		.createCriteria(IwUserDetails.class)
		.add(Restrictions.eq("iwActIdUser", userId)).list();
		if (!userDetailsList.isEmpty()) {
			getCurrentSession().delete(userDetailsList.get(0));
		}
	}

	@Override
	public List<IwApplication> getApplicationList() {
		return getCurrentSession()
		.createCriteria(IwApplication.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.addOrder(Order.asc("iwListingIndex"))
		.list();
	}

	@Override
	public List<IwApplication> getUserApplicaitonList(List<String> appKies) {
		return getCurrentSession()
		.createCriteria(IwApplication.class)
		.add(Restrictions.in("iwKey", appKies))
		.list();
	}

	@Override
	public IwApplication getApplicationByKey(String key) {
		return (IwApplication) getCurrentSession().createCriteria(IwApplication.class).add(Restrictions.eq("iwKey", key)).uniqueResult();
	}

	@Override
	public IwApplication getApplicationByProcess(String processKey) {
		List<IwApplicationProcess> list = getCurrentSession().createCriteria(IwApplicationProcess.class).add(Restrictions.eq("iwProcessKey", processKey)).list();
		if (list != null && !list.isEmpty()) {
			return getApplicationByKey(list.get(0).getIwApplicationKey());
		}
		return null;
	}

	@Override
	public IwApplication getApplicationById(Long id) {
		return (IwApplication) getCurrentSession().createCriteria(IwApplication.class).add(Restrictions.eq("iwApplicationId", id)).uniqueResult();
	}

	@Override
	public void saveIwApplication(IwApplication application) {
		getCurrentSession().saveOrUpdate(application);
		getCurrentSession().flush();
	}

	@Override
	public void deleteIwApplication(Long applicationId) {
		IwApplication application = getApplicationById(applicationId);
		if (application != null) {
			getCurrentSession().delete(application);
		}
	}

	@Override
	public List<IwMultilineEntity> getMultiLineEntityList() {
		return getCurrentSession().createCriteria(IwMultilineEntity.class).list();
	}

	@Override
	public Object getEntitytById(String className, String idEntity) {
		try {
			Class classEntity = Class.forName(className);
			String idName = null;
			Class idType = null;
			for (Field field : classEntity.getDeclaredFields()) {
				Class type = field.getType();
				String name = field.getName();
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					idName = field.getName();
				}
			}
			if (idEntity != null && idName != null) {
				return getCurrentSession().createQuery("select a from " + className + " a where a." + idName + "=" + idEntity).uniqueResult();
			}
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(DaoBusinessServiceImpl.class).error(null, ex);
		}
		return null;
	}

	@Override
	public void saveEntityList(List entityList) {
		if (entityList != null && !entityList.isEmpty()) {
			for (Object entity : entityList) {
				getCurrentSession().saveOrUpdate(entity);
			}
			getCurrentSession().flush();
		}
	}

	@Override
	public List<IwMultilineEntityField> getFieldByEntityList(String entityName) {
		return getCurrentSession()
		.createCriteria(IwMultilineEntityField.class)
		.createAlias("iwMultilineEntity", "iwMultilineEntity")
		.add(Restrictions.eq("iwMultilineEntity.iwClass", entityName))
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
	}

	@Override
	public String getDescriptionFiledEntity(String entityName, String fieldName) {
		IwMultilineEntityField entityField
		= (IwMultilineEntityField) getCurrentSession()
		.createCriteria(IwMultilineEntityField.class)
		.createAlias("iwMultilineEntity", "iwMultilineEntity")
		.add(Restrictions.eq("iwMultilineEntity.iwClass", entityName))
		.add(Restrictions.eq("iwMultilineEntityFieldName", fieldName))
		.uniqueResult();

		if (entityField != null) {
			return entityField.getIwMultilineEntityFieldDescription();
		} else {
			return fieldName;
		}
	}

	@Override
	public List<IwRight> getIwRightList(String userId, Collection<GrantedAuthority> autorities) {
		Collection<String> groups = new ArrayList<String>();
		for (GrantedAuthority auth : autorities) {
			groups.add(auth.getAuthority());
		}
		if (!groups.isEmpty()) {
			return getCurrentSession()
			.createCriteria(IwRight.class)
			.add(Restrictions.or(Restrictions.eq("iwUser", userId), Restrictions.in("iwGroup", groups)))
			.list();
		} else {
			return getCurrentSession()
			.createCriteria(IwRight.class)
			.add(Restrictions.eq("iwUser", userId))
			.list();
		}
	}

	@Override
	public void saveRights(List<IwRight> rightAddList, List<IwRight> rightRemoveList) {
		if ((rightAddList == null || rightAddList.isEmpty()) && (rightRemoveList == null || rightRemoveList.isEmpty())) {
			return;
		}
		for (IwRight right : rightAddList) {
			getCurrentSession().saveOrUpdate(right);
		}
		for (IwRight right : rightRemoveList) {
			getCurrentSession().delete(right);
		}
		getCurrentSession().flush();
	}

	@Override
	public List<IwRight> getIwRightList(String applicatioKey) {
		return (List<IwRight>) getCurrentSession()
		.createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", applicatioKey))
		.add(Restrictions.or(Restrictions.isNull("iwProcessKey"),
		Restrictions.eq("iwProcessKey", "")))
		.list();
	}

	@Override
	public List<IwRight> getIwRightList(String applicatioKey, String processKey) {
		return (List<IwRight>) getCurrentSession()
		.createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", applicatioKey))
		.add(Restrictions.eq("iwProcessKey", processKey))
		.list();
	}

	@Override
	public void saveEntite(Object entity) {
		getCurrentSession().saveOrUpdate(entity);
		getCurrentSession().flush();
	}

	@Override
	public void removeEntite(Object entity) {
		getCurrentSession().delete(entity);
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	@Override
	public void removeList(Object entity) {
		IwList iwlist = (IwList) entity;
		for (IwMultilineEntityField iwMultilineEntityField : iwlist.getIwMultilineEntityFieldList()) {
			iwMultilineEntityField.setIwMultilineEntityFieldId(null);
		}
		getCurrentSession().delete(iwlist);
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	@Override
	public List<IwMultilineEntityField> getEntityFieldList(String entityId) {
		return getCurrentSession()
		.createCriteria(IwMultilineEntityField.class)
		.createAlias("iwMultilineEntity", "iwMultilineEntity")
		.add(Restrictions.eq("iwMultilineEntity.iwMultilineEntityId", Long.valueOf(entityId)))
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
	}

	@Override
	public List<IwList> getIwListList() {
		List<IwList> iwlist = getCurrentSession().createCriteria(IwList.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		for (IwList currentIwlist : iwlist) {
			Hibernate.initialize(currentIwlist.getIwListOptionsList());
		}
		return iwlist;
	}

	@Override
	public List<IwListOptions> getIwListOptionsListByIwList(IwList list) {
		return getCurrentSession().createCriteria(IwListOptions.class).add(Restrictions.eq("iwListId", list)).list();
	}

	@Override
	public List<IwVariableProcess> getVariableProcess(String processKey, Boolean isTask, Boolean isForm) {
		Criteria crt = getCurrentSession().createCriteria(IwVariableProcess.class);
		crt.add(Restrictions.eq("iwProcessKey", processKey));
		if (isTask != null && isTask) {
			crt = crt.add(Restrictions.eq("iwShowTaskList", isTask)).addOrder(Order.asc("iwIndexColonne"));
		}
		if (isForm != null && isForm) {
			crt = crt.add(Restrictions.eq("iwShowTaskForm", isForm)).addOrder(Order.asc("iwIndex"));
		}
		return crt.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@Override
	public void deleteObject(Object obj) {
		getCurrentSession().delete(obj);
	}

	@Override
	public List<IwFormTemplate> getIwFormTemplateList() {
		return (List<IwFormTemplate>) getCurrentSession()
		.createCriteria(IwFormTemplate.class)
		.list();
	}

	@Override
	public List<BizSecteur> getSectorList() {
		return getCurrentSession()
		.createCriteria(BizSecteur.class)
		.list();
	}

	@Override
	public List<BizRayon> getRayonList() {
		return getCurrentSession().createCriteria(BizRayon.class).list();
	}

	@Override
	public BizSecteurRayon getSecteurRayonById(BizSecteur secteurId, BizRayon rayonId) {
		return (BizSecteurRayon) getCurrentSession().createCriteria(BizSecteurRayon.class).add(Restrictions.eq("secteur", secteurId)).add(Restrictions.eq("rayon", rayonId)).uniqueResult();
	}

	@Override
	public List<IwVariableProcess> getVariableProcessByProcessKeyAndIwinputs(String processKey, List<IwInput> inputList) {
		Criteria crt = getCurrentSession().createCriteria(IwVariableProcess.class);
		crt.add(Restrictions.eq("iwProcessKey", processKey));
		crt = crt.add(Restrictions.in("iwInput", inputList));
		return crt.list();
	}

	@Override
	public List<BizFamille> getFamilyList() {
		return getCurrentSession().createCriteria(BizFamille.class).list();
	}

	@Override
	public List<BizSecteurRayon> getRayonListForSector(Integer sectorId) {
		BizSecteur secteur = (BizSecteur) getCurrentSession().get(BizSecteur.class, sectorId);
		if (secteur != null) {
			Hibernate.initialize(secteur.getBizSecteurRayonList());
			return secteur.getBizSecteurRayonList();
		}
		return null;
	}

	@Override
	public List<BizSecteurRayon> getSecteurRayonList(BizSecteur sectorId) {
		return getCurrentSession().createCriteria(BizSecteurRayon.class).add(Restrictions.eq("secteur", sectorId)).list();
	}

	@Override
	public List<Object> getEntityListLike(String entity, String field, String query) {
		try {
			return getCurrentSession()
			.createCriteria(Class.forName(entity))
			.add(Restrictions.ilike(field, "%" + query + "%"))
			.list();
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(DaoBusinessService.class).error(null, ex);
		}
		return null;
	}

	@Override
	public IwList getIwListById(Long listId) {
		IwList iwList = (IwList) getCurrentSession().get(IwList.class, listId);
		Hibernate.initialize(iwList.getIwListOptionsList());
		return iwList;
	}

	@Override
	public void deleteEntites(String entityName) {
		try {
			String tableName = Class.forName(entityName).getAnnotation(Table.class).name();
			getCurrentSession().createQuery("delete from " + tableName).executeUpdate();
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public List<Object> getEntiteList(String entityName) {
		try {
			Class entityClass = Class.forName(entityName);
			return getCurrentSession().createCriteria(entityClass).list();
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public List<Object> getEntiteList(String entityName, List attributes, List values) {
		try {
			if (!entityName.toLowerCase().contains("competence")) {
				Class entityClass = Class.forName(entityName);
				Criteria crt = getCurrentSession().createCriteria(entityClass);
				boolean isNull = true;
				for (Object obj : values) {
					if (obj.toString().trim().length() > 0 && !attributes.isEmpty()) {
						crt = crt.add(Restrictions.eq(attributes.get(values.indexOf(obj)).toString(), obj.toString()));
						isNull = false;
					}
				}
				if (!isNull) {
					return crt.list();
				}
			} else {
				List<Object> listCompetence = new ArrayList<Object>();
				for (Object obj : values) {
					if (obj.toString().trim().length() > 0 && !attributes.isEmpty()) {
						RhCollaborateur colla = (RhCollaborateur) getCurrentSession().createCriteria(RhCollaborateur.class).add(Restrictions.eq("actIdUser", obj.toString())).uniqueResult();
						RhPosteOccupe posteOccupe = colla.getIdPosteOccupe();
						List<RhFicheDePoste> fichePosteList = getCurrentSession().createCriteria(RhFicheDePoste.class).add(Restrictions.eq("idPosteOccupe", posteOccupe)).list();
						for (RhFicheDePoste fichePoste : fichePosteList) {
							listCompetence.add(fichePoste.getIdCompetence());
						}
						break;
					}
				}
				return listCompetence;
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public void saveAplicationProcess(String appkey, String processKey) {
		List<IwApplicationProcess> list = getCurrentSession().createCriteria(IwApplicationProcess.class).add(Restrictions.eq("iwProcessKey", processKey)).list();
		if (list != null && !list.isEmpty()) {
			for (IwApplicationProcess iwAppProcess : list) {
				iwAppProcess.setIwApplicationKey(appkey);
				getCurrentSession().save(iwAppProcess);
			}
		} else {
			IwApplicationProcess iwApplicationProcess = new IwApplicationProcess();
			iwApplicationProcess.setIwApplicationKey(appkey);
			iwApplicationProcess.setIwProcessKey(processKey);
			getCurrentSession().save(iwApplicationProcess);
		}
	}

	@Override
	public List<String> getProcessKeyByApplication(String appKey) {
		if (appKey != null && !appKey.isEmpty()) {
			return getCurrentSession().createQuery("select p.iwProcessKey from IwApplicationProcess p where p.iwApplicationKey = '" + appKey + "'").list();
		}
		return null;
	}

	@Override
	public void deleteProcessApplication(String appKey) {
		if (appKey != null && !appKey.isEmpty()) {
			getCurrentSession().createQuery("delete from IwApplicationProcess p where p.iwApplicationKey = '" + appKey + "'").executeUpdate();
		}
	}

	@Override
	public List<IwRegistre> getRegistreList() {
		return getCurrentSession().createCriteria(IwRegistre.class).list();
	}

	@Override
	public List getListResourceByType(Class clazz) {
		return getCurrentSession().createCriteria(clazz).list();
	}

	@Override
	public List<IwResource> allResources() {
		return getCurrentSession().createCriteria(IwResource.class).list();
	}

	@Override
	public List<IwBooking> getIwBookingList() {
		return getCurrentSession().createCriteria(IwBooking.class).list();
	}

	@Override
	public List<IwBooking> getIwBookingForGivenResource(IwResource resourceId) {
		return getCurrentSession().createCriteria(IwBooking.class).add(Restrictions.eq("iwResourceId", resourceId)).list();
	}

	@Override
	public IwResource getResourceById(Integer id) {
		return (IwResource) getCurrentSession().createCriteria(IwResource.class).add(Restrictions.eq("idResource", id)).uniqueResult();
	}

	@Override
	public List<IwResourceType> getAllResourceType() {
		return getCurrentSession().createCriteria(IwResourceType.class).list();
	}

	@Override
	public void saveIwResource(IwResource resource) {
		getCurrentSession().saveOrUpdate(resource);
		getCurrentSession().flush();
	}

	@Override
	public Object getTupleFromClassById(Class clazz, Integer tupleId) {
		List<Field> classFields = Arrays.asList(clazz.getDeclaredFields());
		for (Field field : classFields) {
			field.setAccessible(true);
			if (field.getName().toLowerCase().contains("id") && !field.getName().toLowerCase().contains("serial")) {
				return getCurrentSession().createCriteria(clazz).add(Restrictions.eq(field.getName(), tupleId)).uniqueResult();
			}
		}
		return null;
	}

	@Override
	public List<RhCollaborateur> getRhCollaborateurList() {
		return getCurrentSession().createCriteria(RhCollaborateur.class).list();
	}

	@Override
	public RhCollaborateur getRhCollaborateurByCode(Integer code) {
		List<RhCollaborateur> list = getCurrentSession()
		.createCriteria(RhCollaborateur.class)
		.add(Restrictions.eq("id", code))
		.list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public IwModelDetails getIwModelDetailsByModelId(String id) {
		List<IwModelDetails> modelDetailsList = getCurrentSession()
		.createQuery("FROM IwModelDetails md WHERE md.iwActModelId=:iwActModelId")
		.setString("iwActModelId", id)
		.list();
		if (modelDetailsList.isEmpty()) {
			return null;
		}
		return modelDetailsList.get(0);
	}

	@Override
	public List<IwVariableProcess> getVariableProcess(String processKey) {
		Criteria crt = getCurrentSession().createCriteria(IwVariableProcess.class);
		return crt.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@Override
	public IwMultilineEntity getIwMultiLineEntityByClassName(String className) {
		List<IwMultilineEntity> list = getCurrentSession().createCriteria(IwMultilineEntity.class)
		.add(Restrictions.eq("iwClass", className))
		.setMaxResults(1)
		.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskList() {
		return getCurrentSession().createCriteria(IwStandAloneTask.class).list();
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskList(String applicationKey) {
		return getCurrentSession().createCriteria(IwStandAloneTask.class).add(Restrictions.eq("iwApplicationKey", applicationKey)).list();
	}

	@Override
	public IwStandAloneTask getStandAloneTask(Long id) {
		return (IwStandAloneTask) getCurrentSession().createCriteria(IwStandAloneTask.class).add(Restrictions.eq("iwStandAloneTaskId", id)).uniqueResult();
	}

	@Override
	public void remove(IwStandAloneTask iwStandAloneTask) {
		getCurrentSession().delete(iwStandAloneTask);
	}

	@Override
	public void save(IwStandAloneTask iwStandAloneTask) {
		getCurrentSession().saveOrUpdate(iwStandAloneTask);
	}

	@Override
	public List<IwRight> getIwRightListByStandaloneTaskId(String applicationKey, Long standaloneTAskId) {
		return getCurrentSession().createCriteria(IwRight.class).add(Restrictions.eq("iwApplicationKey", applicationKey
		)).add(Restrictions.eq("iwStandaloneId", standaloneTAskId)).list();
	}

	@Override
	public List<IwStandAloneTask> getStandAloneTaskListByIwRight(String appKey, List<IwRight> rights) {
		if (rights != null && !rights.isEmpty() && appKey != null) {
			List<Long> isd = new ArrayList<Long>();
			for (IwRight right : rights) {
				if (right.getIwStandaloneId() != null) {
					isd.add(right.getIwStandaloneId());
				}
			}
			if (!isd.isEmpty()) {
				Criteria criteria = getCurrentSession().createCriteria(IwStandAloneTask.class);
				criteria.add(Restrictions.eq("iwApplicationKey", appKey));
				criteria.add(Restrictions.in("iwStandAloneTaskId", isd.toArray()));
				return criteria.list();
			}
		}
		return null;
	}

	@Override
	public List<OrgEntiteOrganisationnelle> getOrgEntiteOrganisationnelleList() {
		return getCurrentSession().createCriteria(OrgEntiteOrganisationnelle.class).list();
	}

	@Override
	public List<OrgEntiteOrganisationnelle> getChildsOfGivenEntiteOrg(OrgEntiteOrganisationnelle parrent) {
		return getCurrentSession().createCriteria(OrgEntiteOrganisationnelle.class).add(Restrictions.eq("idEntiteOrgaSuiv", parrent)).list();
	}

	@Override
	public void saveRhCollaborateur(RhCollaborateur rhCollaborateur) {
		getCurrentSession().saveOrUpdate(rhCollaborateur);
		getCurrentSession().flush();
	}

	@Override
	public void saveOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle) {
		getCurrentSession().saveOrUpdate(orgEntiteOrganisationnelle);
		getCurrentSession().flush();
	}

	@Override
	public List<Object> getEntiteConfigList(String entityName, List attributes, List values) {
		try {
			Class entityClass = Class.forName(entityName);
			Criteria crt = getCurrentSession().createCriteria(entityClass);
			for (Object obj : values) {
				if (obj.toString().trim().length() > 0 && !attributes.isEmpty()) {
					crt.add(Restrictions.eq(attributes.get(values.indexOf(obj)).toString(), obj.toString()));
				}
			}
			return crt.list();
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DaoBusinessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public List<Object> getEntityListLike(String entity, String[] fieldList, String query) {
		try {
			Criteria ctr = getCurrentSession().createCriteria(Class.forName(entity));
			if (fieldList.length != 0) {
				ctr.add(getRecOrCriterion(entity, fieldList, query, 0));
			}
			return ctr.list();
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(DaoBusinessService.class).error(null, ex);
		}
		return null;
	}

	public Criterion getRecOrCriterion(String entity, String[] fieldList, String query, int index) {
		if (index == fieldList.length - 1) {
			return Restrictions.ilike(fieldList[index], "%" + query + "%");
		} else {
			return Restrictions.or(Restrictions.ilike(fieldList[index], "%" + query + "%"), getRecOrCriterion(entity, fieldList, query, index + 1));
		}
	}

	@Override
	public List<RhPosteOccupe> getRhPosteOccupeList() {
		return getCurrentSession().createCriteria(RhPosteOccupe.class).list();
	}

	@Override
	public OrgEntiteOrganisationnelle getOrgEntiteOrganisationnelleById(int id) {
		return (OrgEntiteOrganisationnelle) getCurrentSession().get(OrgEntiteOrganisationnelle.class, id);
	}

	@Override
	public RhCollaborateur getRhCollaborateurById(int id) {
		return (RhCollaborateur) getCurrentSession().get(RhCollaborateur.class, id);
	}

	@Override
	public RhPosteOccupe getRhPosteOccupeById(int id) {
		return (RhPosteOccupe) getCurrentSession().get(RhPosteOccupe.class, id);
	}

	@Override
	public void removeOrgEntiteOrganisationnelle(OrgEntiteOrganisationnelle orgEntiteOrganisationnelle) {
		getCurrentSession().delete(orgEntiteOrganisationnelle);
	}

	@Override
	public void removeRhCollaborateur(RhCollaborateur rhCollaborateur) {
		getCurrentSession().delete(rhCollaborateur);
	}

	@Override
	public IwVariableProcess getVariableProcessById(Long id) {
		return (IwVariableProcess) getCurrentSession().createCriteria(IwVariableProcess.class).add(Restrictions.eq("iwVariableProcessId", id)).uniqueResult();
	}

	@Override
	public List<IwTree> getAllParent() {
		List<IwTree> allParent = getCurrentSession().createCriteria(IwTree.class).add(Restrictions.isNull("parent")).list();
		for (IwTree tree : allParent) {
			Hibernate.initialize(tree.getIwTreeList());
		}
		return allParent;
	}

	@Override
	public List<IwTree> getChilds(IwTree parent) {
		List<IwTree> childs = getCurrentSession().createCriteria(IwTree.class).add(Restrictions.eq("parent", parent)).list();
		for (IwTree tree : childs) {
			Hibernate.initialize(tree.getIwTreeList());
		}
		return childs;
	}

	/**
	 * @param child get list of twins of this child
	 * @return list of children of given parent
	 */
	@Override
	public List<IwTree> getParent(IwTree child) {
		if (child.getParent() == null)
			return getAllParent();
		else {
			List<IwTree> parents = getCurrentSession().createCriteria(IwTree.class).add(Restrictions.eq("parent", child.getParent())).list();
			for (IwTree tree : parents) {
				Hibernate.initialize(tree.getIwTreeList());
			}
			return parents;
		}
	}

	@Override
	public List<IwColumnTask> getColumnTaskList() {
		return (List<IwColumnTask>) getCurrentSession().createCriteria(IwColumnTask.class).list();
	}

	@Override
	public List<IwVariableProcess> getVariableProcessList(String processKey, Boolean isTask, Boolean isForm) {
		Criteria crt = getCurrentSession().createCriteria(IwVariableProcess.class);
		crt.add(Restrictions.eq("iwProcessKey", processKey));
		if (isTask != null && isTask) {
			crt = crt.addOrder(Order.asc("iwIndexColonne"));
		}
		if (isForm != null && isForm) {
			crt = crt.addOrder(Order.asc("iwIndex"));
		}
		return crt.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@Override
	public IwRight getIwRightByAppAndUser(String appKey, String userId, String processKey) {
		return (IwRight) getCurrentSession().createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", appKey))
		.add(Restrictions.eq("iwProcessKey", processKey))
		.add(Restrictions.eq("iwUser", userId))
		.uniqueResult();
	}

	@Override
	public IwRight getIwRightByAppAndGroup(String appKey, String groupId, String processKey) {
		return (IwRight) getCurrentSession().createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", appKey))
		.add(Restrictions.eq("iwProcessKey", processKey))
		.add(Restrictions.eq("iwGroup", groupId))
		.uniqueResult();
	}

	@Override
	public List<IwRight> getIwRightByAppAndUser(String appKey, String userId) {
		return getCurrentSession().createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", appKey))
		.add(Restrictions.eq("iwUser", userId))
		.list();
	}

	@Override
	public List<IwRight> getIwRightByAppAndGroup(String appKey, String groupId) {
		return getCurrentSession().createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", appKey))
		.add(Restrictions.eq("iwUser", groupId))
		.list();
	}

	@Override
	public List<String> getGroupIdsByAppKey(String appkey) {
		List<IwRight> rights = getCurrentSession().createCriteria(IwRight.class)
		.add(Restrictions.eq("iwApplicationKey", appkey))
		.add(Restrictions.isNotNull("iwGroup"))
		.list();
		List<String> list = new ArrayList<String>();
		for (IwRight right : rights) {
			list.add(right.getIwGroup());
		}
		return list;
	}

	@Override
	public IwMultilineEntity getEntityByName(String entity) {
		List<IwMultilineEntity> list = getCurrentSession().createCriteria(IwMultilineEntity.class)
		.add(Restrictions.eq("iwName", entity))
		.setMaxResults(1)
		.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * @param className
	 * @param criteria
	 * @return
	 */
	@Override
	public List<Object> getEntityItems(Class className, String... criteria) {
		return getCurrentSession().createCriteria(className).list();
	}

	/**
	 * @param multiLineEntity
	 * @param id
	 * @return
	 */
	@Override
	public Object getEntityItem(IwMultilineEntity multiLineEntity, String id) {
		String idProperty = getCurrentSession().getSessionFactory()
		.getClassMetadata(multiLineEntity.getIwClass())
		.getIdentifierPropertyName();
		return getCurrentSession().createQuery("SELECT a FROM " + multiLineEntity.getIwName() + " a WHERE a." + idProperty + "=" + id).uniqueResult();
	}

	/**
	 * @param className
	 * @param body
	 * @return
	 */
	@Override
	public void saveEntity(Class className, Object body) {
		getCurrentSession().saveOrUpdate(className.getName(), body);
	}

	/**
	 * @param className
	 * @param body
	 * @return
	 */
	@Override
	public void updateEntity(Class className, Object body) {
		getCurrentSession().update(className.getName(), body);
	}

	/**
	 * @param className
	 * @param id
	 */
	@Override
	public Boolean deleteEntity(Class className, String id) {
		Class<?> typeClass = Util.getEntityIdField(className).getType();
		Object persistentInstance;
		if (typeClass == Long.class) {
			persistentInstance = getCurrentSession().load(className, new Long(id));
		} else {
			persistentInstance = getCurrentSession().load(className, new Integer(id));
		}
		if (persistentInstance != null) {
			getCurrentSession().delete(persistentInstance);
			return true;
		}
		return false;
	}

	@Override
	public List<Object> getObjectBySQL(String request, Class clazz) {
		SQLQuery query = getCurrentSession().createSQLQuery(request);
		query.addEntity(clazz);
		List list = query.list();
		return list;
	}

	@Override
	public IwList getIwListByName(String name) {
		IwList iwList = (IwList) getCurrentSession().createCriteria(IwList.class)
		.add(Restrictions.eq("iwName", name))
		.uniqueResult();
		Hibernate.initialize(iwList.getIwListOptionsList());
		return iwList;
	}

	@Override
	public List<IwVariableTemplate> getVariableTemplateList() {
		return (List<IwVariableTemplate>) getCurrentSession()
		.createCriteria(IwVariableTemplate.class)
		.list();
	}

	@Override
	public RhCollaborateur getRhCollaborateurByActIdUser(String actIdUser) {
		RhCollaborateur rhColaborateur = (RhCollaborateur) getCurrentSession().createCriteria(RhCollaborateur.class).add(Restrictions.eq("actIdUser", actIdUser)).uniqueResult();
		if (rhColaborateur != null) {
			return rhColaborateur;
		}
		return null;
	}

	@Override
	public List<RhConge> getAllCongesByUser(int idCollobaratteur) {
		String query = "SELECT * FROM RH_CONGE  WHERE RH_CONGE.COLLABORATEUR=" + idCollobaratteur + " AND  YEAR(RH_CONGE.DATEDEBUT) = YEAR(CURDATE()) ";
		List rhCongeList = getObjectBySQL(query, RhConge.class);
		//(List<RhConge>) getCurrentSession().createCriteria(RhConge.class)   .add(Restrictions.eq("COLLABORATEUR",idCollobaratteur )).list();
		if (rhCongeList != null) {
			return rhCongeList;
		}
		return null;
	}

	@Override
	public RhCollaborateur getColloborateurByMatricule(int matricule) {
		String query = "SELECT * FROM RH_COLLABORATEUR  WHERE RH_COLLABORATEUR.MATRICULE=" + matricule;

		List rhCollorateur = getObjectBySQL(query, RhCollaborateur.class);
		if (rhCollorateur != null) {
			return (RhCollaborateur) rhCollorateur.get(0);
		}
		return null;
	}

	@Override
	public void saveObjectAsEntity(Object obj) {
		getCurrentSession().saveOrUpdate(obj);
		getCurrentSession().flush();
	}

	@Override
	public RhCollaborateur getCollaborateurByActIdUser(String actIdUser) {
		List<RhCollaborateur> rhColaborateur = ((List<RhCollaborateur>) (List<?>) getCurrentSession().createCriteria(RhCollaborateur.class).add(Restrictions.eq("actIdUser", actIdUser)).list());
		if (rhColaborateur != null && !rhColaborateur.isEmpty()) {
			return rhColaborateur.get(rhColaborateur.size() - 1);
		}
		return null;
	}

	@Override
	public RhFormation getRhFormationByAldFormationId(Integer aldFormationId) {
		RhFormation rhFormation = (RhFormation) getCurrentSession().createCriteria(RhFormation.class).add(Restrictions.eq("aldFormationId", aldFormationId)).uniqueResult();
		return rhFormation;
	}

	@Override
	public int getNombreDemandes() {
		String query = "SELECT max(ID) FROM BIZ_DEMANDE_ACHAT";

		int n = 0;
		try {
			n =(Integer) getObjectBySQL(query, BizDemandeAchat.class).get(0);
		}catch (Exception e){

		}
		return  n;
	}

	@Override
	public int getNombreCommandes() {
		String query = "SELECT max(ID) FROM BIZ_LIGNE_COMMANDE";

		int n = 0;
		try {
			n =(Integer) getObjectBySQL(query, BizLigneCommande.class).get(0);
		}catch (Exception e){

		}
		return  n;
	}

	public List<String> getTaskByFormBuilderId(String formId){
		String query = "SELECT ID_ FROM ACT_RU_TASK WHERE FORM_KEY_ = "+formId;
		try {
			return getCurrentSession().createSQLQuery(query).list();
		}catch (Exception e){}
		return  new ArrayList<>();
	}

}
