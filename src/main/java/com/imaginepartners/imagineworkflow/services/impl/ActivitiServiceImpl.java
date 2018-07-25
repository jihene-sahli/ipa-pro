package com.imaginepartners.imagineworkflow.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.models.IwGroupDetails;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Table;

import java.util.*;

@Service
public class ActivitiServiceImpl implements ActivitiService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private IdentityService identityService;
	@Autowired
	private FormService formService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	transient private HistoryService historyService;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private DynamicBpmnService dynamicBpmnService;

	@Autowired
	private ProcessEngineFactoryBean processEngine;

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	public DynamicBpmnService getDynamicBpmnService() {
		return dynamicBpmnService;
	}

	public void setDynamicBpmnService(DynamicBpmnService dynamicBpmnService) {
		this.dynamicBpmnService = dynamicBpmnService;
	}

	@Override
	public ProcessInstance startProcessInstanceById(String procDefId) {
		try {
			initAuthenticatedUser();
			return runtimeService.startProcessInstanceById(procDefId);
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.error("startProcessInstanceById", exp);
			return null;
		} finally {
			removeAuthenticatedUser();
		}
	}

	@Override
	public ProcessInstance startProcessInstanceById(String procDefId, Map<String, Object> vars) {
		try {
			initAuthenticatedUser();
			return runtimeService.startProcessInstanceById(procDefId, vars);
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("startProcessInstanceById", exp);
			return null;
		}
	}

	@Override
	public ProcessInstance startProcessInstanceByKey(String procKey, Map<String, Object> vars) {
		try {
			initAuthenticatedUser();
			return runtimeService.startProcessInstanceByKey(procKey, vars);
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("startProcessInstanceByKey", exp);
			return null;
		} finally {
			removeAuthenticatedUser();
		}
	}

	@Override
	public Task getTask(String taskId) {
		try {
			List<Task> list = taskService.createTaskQuery().taskId(taskId).list();
			if (!list.isEmpty()) {
				return list.get(0);
			}
		} catch (ActivitiException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("getTask", exp);
		}
		return null;
	}

	@Override
	public User getUser(String userId) {
		List<User> list = identityService.createUserQuery().userId(userId).list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Group getGroup(String groupId) {
		List<Group> list = identityService.createGroupQuery().groupId(groupId).list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public ProcessDefinition getProcessDefinitionByKey(String procDefKey) {
		try {
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
			.processDefinitionKey(procDefKey)
			.latestVersion()
			.list();
			if (!list.isEmpty()) {
				return list.get(0);
			}
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("getProcessDefinition", exp);
		}
		return null;
	}

	@Override
	public ProcessInstance getProcessInstanceById(String procInstId) {
		if (procInstId != null) {
			try {
				List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).list();
				if (!list.isEmpty()) {
					return list.get(0);
				}
			} catch (ActivitiObjectNotFoundException exp) {
				LogManager.getLogger(ActivitiServiceImpl.class)
				.debug("getProcessDefinition", exp);
			}
		}
		return null;
	}

	@Override
	public ProcessDefinition getProcessDefinitionById(String procDefId) {
		try {
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
			.processDefinitionId(procDefId)
			.list();
			if (!list.isEmpty()) {
				return list.get(0);
			}
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("getProcessDefinition", exp);
		}
		return null;
	}

	@Override
	public List<Task> getTasksForUser(String userId, List<String> processKeis) {
		if (processKeis != null && !processKeis.isEmpty()) {
			return taskService.createTaskQuery()
			.taskAssignee(userId)
			.processDefinitionKeyIn(processKeis)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		} else if (processKeis.isEmpty()) {
			return taskService.createTaskQuery()
			.taskAssignee(userId)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		}
		return null;
	}

	@Override
	public List<Task> getStandaloneTasksForUser(String userId) {
		return taskService.createNativeTaskQuery()
		.sql("SELECT * FROM " + managementService.getTableName(Task.class)
		+ " tsk "
		+ " WHERE "
		+ " tsk.ASSIGNEE_=#{assignee} "
		+ " AND "
		+ " tsk.PROC_INST_ID_ is null")
		.parameter("assignee", userId)
		.list();
	}

	@Override
	public List<Task> getStandaloneTasksForUserByCategory(String userId, String category) {
		return taskService.createNativeTaskQuery()
		.sql("SELECT * FROM " + managementService.getTableName(Task.class)
		+ " tsk "
		+ " WHERE "
		+ " tsk.ASSIGNEE_=#{assignee}"
		+ " AND "
		+ " tsk.PROC_INST_ID_ is null"
		+ " AND "
		+ "tsk.CATEGORY_=#{category}")
		.parameter("assignee", userId)
		.parameter("category", category)
		.list();
	}

	@Override
	public List<Task> getTaskListForInvolvedUser(String userId, List<String> processKeis) {
		if (processKeis != null && !processKeis.isEmpty()) {
			return taskService.createTaskQuery()
			.taskInvolvedUser(userId)
			.processDefinitionKeyIn(processKeis)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		}
		return null;
	}

	@Override
	public List<Task> getTaskQueueListForUser(String userId, List<String> processKeis) {
		if (processKeis != null && !processKeis.isEmpty()) {
			List<Group> grpList = identityService.createGroupQuery().groupMember(userId).list();
			List<String> grpListStr = new ArrayList<String>();
			for (Group grp : grpList) {
				grpListStr.add(grp.getId());
			}
			return taskService.createTaskQuery()
			.processDefinitionKeyIn(processKeis)
			.taskUnassigned()
			.taskCandidateUser(userId)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		} else {
			return null;
		}
	}

	@Override
	public List<HistoricTaskInstance> getTaskArchiveListForUser(String userId, List<String> processKeis) {
		if (processKeis != null && !processKeis.isEmpty()) {
			return historyService.createHistoricTaskInstanceQuery()
			.taskAssignee(userId)
			.processDefinitionKeyIn(processKeis)
			.finished()
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		} else {
			return null;
		}
	}

	@Override
	public List<Task> getTasksForUserByProcKey(String userId, String procDefId, List<String> processKeis) {
		if (processKeis != null && !processKeis.isEmpty()) {
			return taskService.createTaskQuery()
			.taskAssignee(userId)
			.processDefinitionKeyIn(processKeis)
			.processDefinitionKey(procDefId)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime().
			desc().orderByTaskName()
			.asc()
			.list();
		} else {
			return null;
		}
	}

	@Override
	public Task getTaskForUserByProcInstId(String userId, String procInstId) {
		try {
			List<Task> list = taskService.createTaskQuery()
			.processInstanceId(procInstId)
			.taskAssignee(userId)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc().list();
			if (!list.isEmpty()) {
				return list.get(0);
			}
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("getTaskForUserByProcInstId", exp);
		}
		return null;
	}

	@Override
	public List<Task> getTaskForUserByProcInstId(String procInstId) {
		try {
			return taskService.createTaskQuery()
			.processInstanceId(procInstId)
			.orderByDueDateNullsLast()
			.desc()
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc().list();
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("getTaskForUserByProcInstId", exp);
			return null;
		}
	}

	@Override
	public void completeTask(String taskId, Map<String, Object> varMap) {
		try {
			taskService.complete(taskId, varMap);
		} catch (ActivitiObjectNotFoundException exp) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.debug("completeTask", exp);
		}
	}

	@Override
	public TaskFormData getTaskFormData(String taskId) {
		if (StringUtils.isBlank(taskId)) {
			return null;
		}
		return formService.getTaskFormData(taskId);
	}

	@Override
	public Map<String, Object> getTaskVariables(String taskId, List<String> varList) {
		return taskService.getVariables(taskId, varList);
	}

	@Override
	public Map<String, Object> getTaskVariables(String taskId) {
		if (StringUtils.isEmpty(taskId)) {
			return null;
		}
		return taskService.getVariables(taskId);
	}

	@Override
	public List<ProcessDefinition> getLastProcDefList() {
		return repositoryService.createProcessDefinitionQuery().latestVersion().list();
	}

	@Override
	public List<ProcessDefinition> getLastProcDefList(String category) {
		LogManager.getLogger(ActivitiServiceImpl.class).debug("getLastProcDefList category:" + category);
		return repositoryService.createProcessDefinitionQuery()
		.processDefinitionCategory(category)
		.latestVersion()
		.list();
	}

	@Override
	public List<ProcessDefinition> getLastProcDefList(List<String> processKies) {
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
		.latestVersion()
		.list();
		return getFilterProcessDefinitionByKeis(processDefinitionList, processKies);

	}

	public List<ProcessDefinition> getFilterProcessDefinitionByKeis(List<ProcessDefinition> processDefinitionList, List<String> processKies) {
		List<ProcessDefinition> processDefRight = new ArrayList<ProcessDefinition>();
		if (processDefinitionList != null && !processDefinitionList.isEmpty()
		&& processKies != null && !processKies.isEmpty()) {
			for (ProcessDefinition definition : processDefinitionList) {
				if (processKies.contains(definition.getKey())) {
					processDefRight.add(definition);
				}
			}
		}
		return processDefRight;
	}

	@Override
	public List<ProcessDefinition> getProcDefList() {
		return repositoryService.createProcessDefinitionQuery().list();
	}

	@Override
	public List<ProcessDefinition> getProcDefList(List<String> processKeis) {
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
		.list();
		return getFilterProcessDefinitionByKeis(processDefinitionList, processKeis);
	}

	@Override
	public List<Deployment> getDeployementList() {
		return repositoryService.createDeploymentQuery().list();
	}

	@Override
	public List<ProcessInstance> getProcInstList() {
		return runtimeService.createProcessInstanceQuery().list();
	}

	@Override
	public List<HistoricProcessInstance> getProcInstHistListForUser(String involvedUser, List<String> processKies) {
		if (processKies != null && !processKies.isEmpty()) {
			return historyService.createHistoricProcessInstanceQuery().processDefinitionKeyIn(processKies).finished().involvedUser(involvedUser).list();
		} else {
			return null;
		}
	}

	@Override
	public List<ProcessInstance> getProcInstListForUser(String involvedUser, List<String> processKies) {
		if (processKies != null && !processKies.isEmpty()) {
			Set<String> processKeyList = new HashSet<String>(processKies);
			return runtimeService.createProcessInstanceQuery().processDefinitionKeys(processKeyList).involvedUser(involvedUser).list();
		} else {
			return null;
		}
	}

	@Override
	public List<HistoricProcessInstance> getProcInstHistList() {
		return historyService.createHistoricProcessInstanceQuery().list();
	}

	@Override
	public InputStream getDiagramResource(String deploymentId, String diagramName) {
		return repositoryService.getResourceAsStream(deploymentId, diagramName);
	}

	@Override
	public void undeploy(String deploymentId, boolean includeInstances) {
		repositoryService.deleteDeployment(deploymentId, includeInstances);
	}

	@Override
	public void suspend(String procDefId, boolean includeInstances) {
		repositoryService.suspendProcessDefinitionById(procDefId, includeInstances, null);
	}

	@Override
	public void activate(String procDefId, boolean includeInstances) {
		repositoryService.activateProcessDefinitionById(procDefId, includeInstances, null);
	}

	@Override
	public Deployment deploy(String name, InputStream bpmnStream) {
		return repositoryService.createDeployment().addInputStream(name, bpmnStream).name(name).deploy();
	}

	@Override
	public List<User> getUsersListLike(String matche) {
		IwUserDetails userDetails 			= new IwUserDetails();
		String userDetailTableName			= "";
		try {
			userDetailTableName 			= Class.forName(userDetails.getClass().getName()).getAnnotation(Table.class).name();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return identityService.createNativeUserQuery()
		.sql(	"	SELECT " +
		"		* " +
		"	FROM " +
		managementService.getTableName(User.class) + " usr" +
		"		INNER JOIN " +userDetailTableName +" details ON (usr.ID_	= details.IW_ACT_ID_USER)"+
		"	WHERE " +
		"	details.IW_ACTIVE	=1 " +
		"	AND details.IW_VISIBLE != 0" +
		"	and (upper(usr.ID_) like upper(#{matche}) or upper(usr.FIRST_) like upper(#{matche}) or upper(usr.LAST_) like upper(#{matche}))" +
		"")
		.parameter("matche", "%" + matche + "%")
		.list();
	}

	@Override
	public List<Group> getGroupsListLike(String matche, String excludGroup) {
		IwGroupDetails groupDetail 			= new IwGroupDetails();
		String groupDetailTableName			= "";
		try {
			groupDetailTableName 			= Class.forName(groupDetail.getClass().getName()).getAnnotation(Table.class).name();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return identityService.createNativeGroupQuery()
		.sql(		"SELECT " +
		"* " +
		"FROM " +
		managementService.getTableName(Group.class) +" grp " +
		"	INNER JOIN " +groupDetailTableName +" grpdtl ON (grp.ID_	= grpdtl.IW_GROUP)"+
		"WHERE " +
		"		grp.ID_<> #{excludGroup} " +
		"	and (upper(grp.ID_) like upper(#{matche}) or upper(grp.NAME_) like upper(#{matche}))" +
		"	AND	grpdtl.IW_VISIBALE != 0"+
		"	")
		.parameter("excludGroup", excludGroup)
		.parameter("matche", "%" + matche + "%")
		.list();
	}

	@Override
	public Comment addTaskComment(String taskId, String procInstId, String comment) {
		initAuthenticatedUser();
		Comment cmt = taskService.addComment(taskId, procInstId, comment);
		removeAuthenticatedUser();
		return cmt;
	}

	@Override
	public Comment addProcInstComment(String procInstId, String comment) {
		initAuthenticatedUser();
		Comment cmt = taskService.addComment(null, procInstId, comment);
		removeAuthenticatedUser();
		return cmt;
	}

	@Override
	public List<Comment> getTaskCommentList(String taskId) {
		return taskService.getTaskComments(taskId);
	}

	@Override
	public User newUser(String userId) {
		return identityService.newUser(userId);
	}

	@Override
	public void saveUser(User user, Picture avatar) {
		saveUser(user);
		identityService.setUserPicture(user.getId(), avatar);
	}

	@Override
	public void saveUser(User user) {
		identityService.saveUser(user);
	}

	@Override
	public void createMembership(String userId, String groupId) {
		identityService.createMembership(userId, groupId);
	}

	@Override
	public void deleteMembership(String userId, String groupId) {
		identityService.deleteMembership(userId, groupId);
	}

	@Override
	public Picture getAvatar(String userId) {
		return identityService.getUserPicture(userId);
	}

	@Override
	public void initAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		identityService.setAuthenticatedUserId(auth.getName());
	}

	@Override
	public void setAuthenticatedUserIdForActiviti(String userId) {
		identityService.setAuthenticatedUserId(userId);
	}

	@Override
	public void removeAuthenticatedUser() {
		identityService.setAuthenticatedUserId(null);
	}

	@Override
	public Model newModel() {
		return repositoryService.newModel();
	}

	@Override
	public Model getModel(String modelId) {
		return repositoryService.getModel(modelId);
	}

	@Override
	public void saveModel(Model model) {
		repositoryService.saveModel(model);
	}

	@Override
	public void addModelEditorSource(String modelId, byte[] editorSource) {
		repositoryService.addModelEditorSource(modelId, editorSource);
	}

	@Override
	public void addModelEditorSourceExtra(String modelId, byte[] editorSource) {
		repositoryService.addModelEditorSourceExtra(modelId, editorSource);
	}

	@Override
	public List<Model> getModelList() {
		return repositoryService.createModelQuery().list();
	}

	@Override
	public List<Model> getModelListByCategory(String categorie) {
		return repositoryService.createModelQuery().modelCategory(categorie).list();
	}

	@Override
	public void deleteModel(String modelId) {
		repositoryService.deleteModel(modelId);
	}

	@Override
	public byte[] getModelEditorSource(String modelId) {
		return repositoryService.getModelEditorSource(modelId);
	}

	@Override
	public byte[] getModelEditorSourceExtra(String modelId) {
		return repositoryService.getModelEditorSourceExtra(modelId);
	}

	@Override
	public Deployment deployModel(String modelId) {
		try {
			Model model = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
			BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			if (model.getKey() == null || model.getKey().isEmpty()) {
				model.setKey(Util.convertToAlphaNumeric(model.getName()));
				repositoryService.saveModel(model);
			}
			Process process = bpmnModel.getProcesses().get(0);
			process.setId(model.getKey());
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel, StandardCharsets.UTF_8.name());
			Deployment deployment = repositoryService.createDeployment().name(model.getName())
			.addString(model.getName() + ".bpmn20.xml", new String(bpmnBytes, StandardCharsets.UTF_8)).deploy();
			return deployment;
		} catch (IOException ex) {
			LogManager.getLogger(ActivitiServiceImpl.class).debug("deployModel", ex);
		}
		return null;
	}

	@Override
	public void saveTask(Task task) {
		taskService.saveTask(task);
	}

	@Override
	public InputStream getResourceAsStream(String deploymentId, String ressourceName) {
		return repositoryService.getResourceAsStream(deploymentId, ressourceName);
	}

	@Override
	public void setProcessInstanceName(String procInstId, String procInstName) {
		runtimeService.setProcessInstanceName(procInstId, procInstName);
	}

	@Override
	public void suspendInstance(String procInstId) {
		runtimeService.suspendProcessInstanceById(procInstId);
	}

	@Override
	public void deleteInstance(String procInstId) {
		runtimeService.deleteProcessInstance(procInstId, "");
	}

	@Override
	public void activateInstance(String procInstId) {
		runtimeService.activateProcessInstanceById(procInstId);
	}

	@Override
	public List<HistoricTaskInstance> getProcessInstanceHistory(String procInstId) {
		return historyService.createHistoricTaskInstanceQuery()
		.processInstanceId(procInstId)
		.orderByTaskCreateTime()
		.asc()
		.orderByDueDateNullsLast()
		.asc()
		.list();
	}

	@Override
	public List<User> getUserList() {
		IwUserDetails userDetails = new IwUserDetails();
		String userDetailTableName = "";
		try {
			userDetailTableName = Class.forName(userDetails.getClass().getName()).getAnnotation(Table.class).name();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return identityService.createNativeUserQuery()
		.sql("SELECT  * FROM " + managementService.getTableName(User.class)+" usr " +
		" INNER JOIN " +userDetailTableName + " usrDtl ON (usr.ID_	= usrDtl.IW_ACT_ID_USER)" +
		" WHERE usrDtl.IW_VISIBLE != 0").list();
	}

	@Override
	public List<Comment> getProcInstCommentList(String procInstId) {
		return taskService.getProcessInstanceComments(procInstId);
	}

	@Override
	public List<Group> getUserGroupList(String userId, String excludeGroup) {
		List<Group> list = identityService.createGroupQuery().groupMember(userId).list();
		Iterator<Group> listIterator = list.iterator();
		while (listIterator.hasNext()) {
			Group grp = listIterator.next();
			if (grp.getId().equals(excludeGroup)) {
				listIterator.remove();
			}
		}
		return list;
	}

	@Override
	public List<Group> getUserGroupList(String userId) {
		List<Group> list = identityService.createGroupQuery().groupMember(userId).list();
		return list;
	}

	@Override
	public List<IdentityLink> getProcInstIdentityLiks(String procInstId) {
		return runtimeService.getIdentityLinksForProcessInstance(procInstId);
	}

	@Override
	public Group newGroup(String groupId) {
		return identityService.newGroup(groupId);
	}

	@Override
	public void saveGroup(Group group) {
		if (this.getGroup(group.getId())==null) {
			identityService.saveGroup(group);
		}

	}

	@Override
	public List<Group> getAllGroupList() {
		return identityService.createGroupQuery().list();
	}

	@Override
	public List<Group> getGroupList(String prefix) {
		IwGroupDetails groupDetail = new IwGroupDetails();
		String groupDetailTableName = "";
		try {
			groupDetailTableName = Class.forName(groupDetail.getClass().getName()).getAnnotation(Table.class).name();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return identityService.createNativeGroupQuery()
		.sql("SELECT " + " * FROM " + managementService.getTableName(Group.class)+" grp " +
		" INNER JOIN " + groupDetailTableName + " grpdtl ON (grp.ID_ = grpdtl.IW_GROUP)" +
		" WHERE grp.ID_ like #{groupeLike} AND grpdtl.IW_VISIBALE != 0").parameter("groupeLike", prefix + "%")
		.list();
	}

	@Override
	public List<Group> getHierarchyList(String prefix) {
		return identityService.createNativeGroupQuery()
		.sql("SELECT * FROM " + managementService.getTableName(Group.class)
		+ " grp WHERE grp.ID_ not like #{groupeLike}")
		.parameter("groupeLike", prefix + "%")
		.list();
	}

	@Override
	public List<Group> getGroupList(List<String> excludeList) {
		String excludeStr = "";
		for (String grp : excludeList) {
			excludeStr += " , '" + grp + "'";
		}
		return identityService.createNativeGroupQuery()
		.sql("SELECT * FROM " + managementService.getTableName(Group.class)
		+ " grp WHERE grp.ID_ not in (" + excludeStr.replaceFirst(",", "") + ")")
		.list();
	}

	@Override
	public long getProcInstNbr(String processKey) {
		return runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).count();
	}

	@Override
	public long getProcInstHistNbr(String processKey) {
		return historyService.createHistoricProcessInstanceQuery().finished().processDefinitionKey(processKey).count();
	}

	@Override
	public long getProcInstNbr() {
		return runtimeService.createProcessInstanceQuery().count();
	}

	@Override
	public long getProcInstHistNbr() {
		return historyService.createHistoricProcessInstanceQuery().finished().count();
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstanceByIdIncludeVars(String histProcId) {
		List<HistoricProcessInstance> list = historyService
		.createHistoricProcessInstanceQuery()
		.processInstanceId(histProcId)
		.includeProcessVariables()
		.list();
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Long getUsersCount() {
		return identityService.createUserQuery().count();
	}

	@Override
	public void claimTask(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	@Override
	public boolean isMemberOfGroup(String userId, String groupId) {
		List<Group> list = identityService.createGroupQuery().groupMember(userId).groupId(groupId).list();
		return !list.isEmpty();
	}

	@Override
	public List<User> getMemerbs(String groupId) {
		return identityService.createUserQuery().memberOfGroup(groupId).list();
	}

	@Override
	public Map<String, String> getTaskModelList(String processDefinitionId) {
		Map<String, String> tasks = new HashMap<String, String>();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
		if (processDefinitionEntity != null) {
			for (ActivityImpl activity : processDefinitionEntity.getActivities()) {
				getTaskFromActivity(activity, tasks);
			}
		}
		return Util.sortByValue(tasks);
	}

	public Map<String, String> getTaskFromActivity(ActivityImpl activity, Map<String, String> tasks) {
		if ("userTask".equals(activity.getProperties().get("type"))) {
			tasks.put(activity.getId(), (String) activity.getProperties().get("name"));
		} else if ("subProcess".equals(activity.getProperties().get("type"))) {
			for (ActivityImpl subActivity : activity.getActivities()) {
				getTaskFromActivity(subActivity, tasks);
			}
		}
		return tasks;
	}

	@Override
	public Set<String> getProcDefFormKeyList(String processDefinitionId) {
		Set<String> kies = new HashSet<String>();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
		if (processDefinitionEntity != null) {
			for (ActivityImpl activity : processDefinitionEntity.getActivities()) {
				if (activity.getProperties().containsKey("taskDefinition")) {
					TaskDefinition taskDef = (TaskDefinition) activity.getProperty("taskDefinition");
					if (taskDef.getFormKeyExpression() != null) {
						kies.add(taskDef.getFormKeyExpression().getExpressionText());
					}
				}
			}
		}
		return kies;
	}

	@Override
	public List<User> getUserListCaseInsensitive(String userId) {
		return identityService.createNativeUserQuery()
		.sql("SELECT * FROM " + managementService.getTableName(User.class)
		+ " usr WHERE UPPER(usr.ID_)=UPPER(#{userId})")
		.parameter("userId", userId)
		.list();
	}

	@Override
	public List<User> getUserListByEmail(String email) {
		return identityService.createNativeUserQuery()
		.sql("SELECT * FROM " + managementService.getTableName(User.class)
		+ " usr WHERE UPPER(usr.EMAIL_)=UPPER(#{email})")
		.parameter("email", email)
		.list();
	}

	@Override
	public List<User> getUserListByEmail(String email, String[] userNotIn) {
		String userNotInStr = "";
		for (int i = 0; i < userNotIn.length; i++) {
			userNotInStr += ",'" + userNotIn[i] + "'";
		}
		userNotInStr = userNotInStr.replaceFirst(",", "");
		return identityService.createNativeUserQuery()
		.sql("SELECT * FROM " + managementService.getTableName(User.class)
		+ " usr WHERE UPPER(usr.EMAIL_)=UPPER(#{email}) and usr.ID_ not in (" + userNotInStr + ")")
		.parameter("email", email)
		.list();
	}

	@Override
	public List<Group> getGroupListCaseInsensitive(String groupeId) {
		return identityService.createNativeGroupQuery()
		.sql("SELECT * FROM " + managementService.getTableName(Group.class)
		+ " grp WHERE UPPER(grp.ID_)=UPPER(#{groupeId})")
		.parameter("groupeId", groupeId)
		.list();
	}

	@Override
	public void deleteGroup(String groupId) {
		identityService.deleteGroup(groupId);
	}

	@Override
	public void deleteUser(String userId) {
		identityService.deleteUser(userId);
	}

	@Override
	public Object getVariableProcess(String procInsId, String varName) {
		return runtimeService.getVariable(procInsId, varName);
	}

	@Override
	public Map<String, Object> getVariablesProcess(String proceDefKey) {
		ProcessInstance procinst= runtimeService.createProcessInstanceQuery().processDefinitionKey(proceDefKey).includeProcessVariables().listPage(0, 1).get(0);
		return procinst.getProcessVariables();
	}

	@Override
	public List<User> getUserListIn(String[] usersIds) {
		if (usersIds == null) {
			return new ArrayList<User>();
		}
		String usersIdsStr = "";
		for (int i = 0; i < usersIds.length; i++) {
			usersIdsStr += ",'" + usersIds[i] + "'";
		}
		usersIdsStr = usersIdsStr.replaceFirst(",", "");
		return identityService.createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ in (" + usersIdsStr + ");")
		.list();
	}

	@Override
	public List<User> getUserListIn(List<String> usersIds) {
		if (usersIds == null) {
			return new ArrayList<User>();
		}
		String usersIdsStr = "";
		for (String user : usersIds) {
			usersIdsStr += ",'" + user + "'";
		}
		usersIdsStr = usersIdsStr.replaceFirst(",", "");
		return identityService.createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ in (" + usersIdsStr + ");")
		.list();
	}

	@Override
	public List<Group> getGroupListIn(List<String> groupsIds) {
		if (groupsIds == null) {
			return new ArrayList<Group>();
		}
		String groupIdsStr = "";
		for (String group : groupsIds) {
			groupIdsStr += ",'" + group + "'";
		}
		groupIdsStr = groupIdsStr.replaceFirst(",", "");
		return identityService.createNativeGroupQuery().sql("SELECT * FROM ACT_ID_GROUP WHERE ID_ in (" + groupIdsStr + ");")
		.list();
	}

	@Override
	public String getCategoryModelProcessDefId(String processDefId) {
		return repositoryService.createDeploymentQuery().deploymentId(getProcessDefinitionById(processDefId).getDeploymentId()).singleResult().getCategory();
	}

	@Override
	public List<Model> getModelList(List<String> precessKes) {
		List<Model> list = new ArrayList<Model>();
		for (String key : precessKes) {
			list.addAll(repositoryService.createModelQuery().modelKey(key).list());
		}
		return list;
	}

	@Override
	public List<IdentityLink> getIdentityLinkForTask(String taskId) {
		return taskService.getIdentityLinksForTask(taskId);
	}

	@Override
	public HistoricTaskInstance getTaskHistById(String taskId) {
		List<HistoricTaskInstance> result = historyService.createHistoricTaskInstanceQuery()
		.includeProcessVariables()
		.taskId(taskId).list();
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Task newStandaloneTask() {
		return taskService.newTask();
	}

	@Override
	public void saveStandaloneTask(Task task) {
		taskService.saveTask(task);
	}

	@Override
	public List<Model> getOrderedModelList() {
		return repositoryService
		.createNativeModelQuery()
		.sql("SELECT * from "
		+ managementService.getTableName(Model.class)
		+ " mdl LEFT JOIN IW_MODEL_DETAILS dtls"
		+ " ON (mdl.ID_= dtls.IW_ACT_MODEL_ID) "
		+ "ORDER BY dtls.IW_LISTING_INDEX "
		+ "ASC").list();
	}

	@Override
	public List<HistoricTaskInstance> getStandaloneTaskArchiveListForUser(String userId) {
		if (userId != null) {
			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM ");
			query.append(managementService.getTableName(HistoricTaskInstance.class));
			query.append(" WHERE ");
			query.append("PROC_DEF_ID_ IS NULL");
			return historyService.createNativeHistoricTaskInstanceQuery().sql(query.toString()).list();
		} else {
			return null;
		}
	}

	@Override
	public List<Task> getTasksByProcDef(ProcessDefinition procDef) {
		return taskService.createTaskQuery()
		.processDefinitionId(procDef.getId())
		.orderByDueDateNullsLast()
		.desc()
		.orderByTaskCreateTime().desc()
		.orderByTaskName().asc().list();
	}

	@Override
	public List<Task> getTasksByExecId(String execId) {
		try {
			return taskService.createTaskQuery()
			.executionId(execId)
			.orderByTaskCreateTime()
			.desc()
			.orderByTaskName()
			.asc()
			.list();
		} catch (ActivitiObjectNotFoundException ex) {
			LogManager.getLogger(ActivitiServiceImpl.class)
			.error("activiti object not found exception", ex);
			return null;
		}
	}

	@Override
	public Task getTaskByExecIdandAssigne(String execuId, String assignee) {
		return taskService.createTaskQuery()
		.executionId(execuId)
		.excludeSubtasks()
		.taskAssignee(assignee)
		.orderByTaskCreateTime()
		.desc()
		.orderByTaskName()
		.asc()
		.singleResult();
	}

	@Override
	public void addVariableforGivenInstance(String procInstance, String varName, Object varValue) {
		runtimeService.setVariable(procInstance, varName, varValue);
	}

	@Override
	public void changeTaskAssignee(String taskId, String userId) {
		taskService.setAssignee(taskId, userId);
	}

	@Override
	public void addGroupCandidateTask(String taskId, String groupId) {
		taskService.addCandidateGroup(taskId, groupId);
	}

	@Override
	public Map<String, Object> getVariablesTasks(String taskId) {
		return taskService.getVariables(taskId);	}

	@Override
	public void unclaimTask(String taskId) {
		taskService.unclaim(taskId);
	}

	@Override
	public Group getMemberGroup(String userId) {
		List<Group> list = identityService.createGroupQuery().groupMember(userId).list();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;

	}

}
