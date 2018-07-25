package com.imaginepartners.imagineworkflow.services;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ActivitiService {

	public ProcessInstance startProcessInstanceById(String procDefId);

	public ProcessInstance startProcessInstanceById(String procDefId, Map<String, Object> vars);

	public Task getTask(String taskId);

	public User getUser(String userId);

	public ProcessDefinition getProcessDefinitionByKey(String procDefId);

	public List<Task> getTasksForUser(String userId, List<String> processKeis);

	public List<Task> getStandaloneTasksForUser(String userId);

	public List<Task> getStandaloneTasksForUserByCategory(String userId, String category);

	public List<Task> getTasksForUserByProcKey(String userId, String procDefId, List<String> processKeis);

	public Task getTaskForUserByProcInstId(String userId, String procInstId);

	public void completeTask(String userId, Map<String, Object> varMap);

	public TaskFormData getTaskFormData(String taskId);

	public Map<String, Object> getTaskVariables(String taskId, List<String> varList);

	public Map<String, Object> getTaskVariables(String taskId);

	public List<ProcessDefinition> getLastProcDefList();

	public List<ProcessDefinition> getLastProcDefList(String category);

	public List<ProcessDefinition> getProcDefList();

	public List<ProcessDefinition> getProcDefList(List<String> processKeis);

	public List<Deployment> getDeployementList();

	public List<ProcessInstance> getProcInstList();

	public InputStream getDiagramResource(String deploymentId, String diagramName);

	public void undeploy(String deploymentId, boolean includeInstances);

	public void suspend(String procDefId, boolean includeInstances);

	public void activate(String procDefId, boolean includeInstances);

	public Deployment deploy(String name, InputStream bpmnStream);

	public List<User> getUsersListLike(String matche);

	public Comment addTaskComment(String taskId, String procInstId, String comment);

	public List<Comment> getTaskCommentList(String taskId);

	public void saveUser(User user, Picture avatar);

	public Model getModel(String modelId);

	public void saveUser(User user);

	public Picture getAvatar(String userId);

	public void initAuthenticatedUser();

	public void setAuthenticatedUserIdForActiviti(String userId);

	public void removeAuthenticatedUser();

	public Model newModel();

	public void saveModel(Model model);

	public void addModelEditorSource(String modelId, byte[] editorSource);

	public void addModelEditorSourceExtra(String modelId, byte[] editorSource);

	public List<Model> getModelList();

	public List<Model> getModelListByCategory(String categorie);

	public void deleteModel(String modelId);

	public byte[] getModelEditorSource(String modelId);

	public byte[] getModelEditorSourceExtra(String modelId);

	public Deployment deployModel(String modelId);

	public ProcessDefinition getProcessDefinitionById(String procDefId);

	public List<Group> getGroupsListLike(String matche, String excludGroup);

	public Group getGroup(String groupId);

	public void saveTask(Task task);

	public InputStream getResourceAsStream(String deploymentId, String ressourceName);

	public void setProcessInstanceName(String procInstId, String procInstName);

	public ProcessInstance getProcessInstanceById(String procInstId);

	public void suspendInstance(String procInstId);

	public void deleteInstance(String procInstId);

	public void activateInstance(String procInstId);

	public List<HistoricTaskInstance> getProcessInstanceHistory(String procInstId);

	public User newUser(String userId);

	public void createMembership(String userId, String groupId);

	public List<User> getUserList();

	public Comment addProcInstComment(String procInstId, String comment);

	public List<Comment> getProcInstCommentList(String procInstId);

	public void deleteMembership(String userId, String groupId);

	public List<Group> getUserGroupList(String userId, String excludeGroup);

	public List<IdentityLink> getProcInstIdentityLiks(String procInstId);

	public List<Task> getTaskForUserByProcInstId(String procInstId);

	public Group newGroup(String groupId);

	public void saveGroup(Group group);

	public List<Group> getGroupList(String prefix);

	public List<Group> getGroupList(List<String> excludeList);

	public List<HistoricProcessInstance> getProcInstHistList();

	public List<HistoricProcessInstance> getProcInstHistListForUser(String involvedUser, List<String> processKies);

	public List<ProcessInstance> getProcInstListForUser(String involvedUser, List<String> processKies);

	public long getProcInstNbr(String processKey);

	public long getProcInstHistNbr(String processKey);

	public long getProcInstNbr();

	public long getProcInstHistNbr();

	public HistoricProcessInstance getHistoricProcessInstanceByIdIncludeVars(String histProcId);

	public Long getUsersCount();

	public List<Task> getTaskListForInvolvedUser(String userId, List<String> processKeis);

	public List<HistoricTaskInstance> getTaskArchiveListForUser(String userId, List<String> processKeis);

	public List<Task> getTaskQueueListForUser(String userId, List<String> processKeis);

	public ProcessInstance startProcessInstanceByKey(String procKey, Map<String, Object> vars);

	public void claimTask(String taskId, String userId);

	public boolean isMemberOfGroup(String userId, String groupId);

	public List<User> getMemerbs(String groupId);

	public List<Group> getAllGroupList();

	public List<Group> getHierarchyList(String prefix);

	public Map<String, String> getTaskModelList(String processDefinitionId);

	public Set<String> getProcDefFormKeyList(String processDefinitionId);

	public List<User> getUserListCaseInsensitive(String userId);

	public List<Group> getGroupListCaseInsensitive(String groupeId);

	public List<User> getUserListByEmail(String email);

	public void deleteGroup(String groupId);

	public void deleteUser(String userId);

	public List<Group> getUserGroupList(String userId);

	public Object getVariableProcess(String procInsId, String varName);

	public Map<String, Object> getVariablesProcess(String proceDefKey);

	public List<User> getUserListIn(String[] usersIds);

	public List<User> getUserListIn(List<String> usersIds);

	public String getCategoryModelProcessDefId(String processDefId);

	public List<User> getUserListByEmail(String email, String[] userNotIn);

	public List<Group> getGroupListIn(List<String> groupsIds);

	public List<ProcessDefinition> getLastProcDefList(List<String> processKies);

	public List<Model> getModelList(List<String> precessKes);

	public List<IdentityLink> getIdentityLinkForTask(String taskId);

	public HistoricTaskInstance getTaskHistById(String taskId);

	public Task newStandaloneTask();

	public void saveStandaloneTask(Task task);

	public List<Model> getOrderedModelList();

	public List<HistoricTaskInstance> getStandaloneTaskArchiveListForUser(String userId);

	public List<Task> getTasksByProcDef(ProcessDefinition procDef);

	public List<Task> getTasksByExecId(String execId);

	public Task getTaskByExecIdandAssigne(String execuId, String assignee);

	public void addVariableforGivenInstance(String procInstance, String varName, Object varValue);

	public void changeTaskAssignee(String taskId, String userId);

	public void addGroupCandidateTask(String taskId, String groupId);

	public Map<String, Object> getVariablesTasks(String taskId);

	public void unclaimTask(String taskId);

	public Group getMemberGroup(String userId);

}
