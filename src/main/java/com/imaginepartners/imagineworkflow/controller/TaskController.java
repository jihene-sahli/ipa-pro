package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphbuilder.math.VarMap;
import com.imaginepartners.imagineworkflow.dashboard.data.DocxData;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.form.template.impl.DefaultFormTemplate;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.MultilineEntityConfigFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.MultilineEntityFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.UploadFormWrapper;
import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.rh.AldFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhConge;
import com.imaginepartners.imagineworkflow.models.rh.RhTypeConge;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.FormWrapperFactory;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import com.imaginepartners.imagineworkflow.util.XlsxReader;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.LogManager;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.joda.time.DateTime;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.slider.Slider;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@Scope("view")
public class TaskController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String procDefKey;

	private String taskId;

	private String taskAutonomeId;

	private String functionId;

	private Map<String, FormWrapper> formList;

	private Map<String, FormWrapper> allFormList = new LinkedHashMap<String, FormWrapper>();

	private String selectedFormId;

	private List<Map<String, Object>> globalFormList;

	private Map<String, Object> globalFormListForCol;

	private String procTitle;

	private int priority;

	private List<Task> taskList;

	private Map<String, Map<String, Object>> procInstVars;

	private Task currentTask;

	private Task standaloneTask;

	private User assignee;

	private User owner;

	private List<User> candidatUserList;

	private List<Group> candidatGroupList;

	private IwForm currentIwForm;

	private String processInstanceName;

	private ProcessDefinition procDef;

	private ProcessInstance procInst;

	private List<HistoricTaskInstance> historicTaskList;

	private Map<String, Object> taskVars;

	private String view;

	private Map<String, HtmlInputHidden> sliderValues = new HashMap<String, HtmlInputHidden>();

	private IwProgress currentProgress;

	private FormWrapper selectedMultiLine;

	private String newRow;

	private List<Group> currentUserGroups;

	private List<String> currentUserGroupsIds;

	private boolean displayVisualisation = false;

	@Autowired
	private UserService userService;
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private FormWrapperFactory formWrapperFactory;

	private List<String> templateBeans;

	private IwUpload iwUpload;

	private String taskHistId;

	private HistoricTaskInstance taskHist;

	private List<HistoricTaskInstance> procInstHist;

	private Map<String, FormWrapper> formListHist;

	private Map<String, Object> taskVarsHist;

	private List<String> configEntityColumnsSeleted;

	private Object configEntityValueSeleted;

	private String configEntityClassSelected;

	private List<Object> configEntityListSelected;

	private List<Object> configEntityListDeleted;

	private boolean isInstantiate;

	private FormWrapper configEntityFormWrapperSeleted;

	private TabView tabViewComponent;

	private Map<String, Map<String, List<Task>>> tasksMap;

	private Map<String, List<Task>> taskApp;

	private String selectProcessId;

	private ArrayList<String> optionsList;

	private ArrayList<String> checkedOptionsList;

	@Autowired
	NavigationController navigationController;

	private List<Object> sonFieldsObjectValueList = new ArrayList<Object>();

	public List<Object> getSonFieldsObjectValueList() {
		return sonFieldsObjectValueList;
	}

	public void setSonFieldsObjectValueList(List<Object> sonFieldsObjectValueList) {
		this.sonFieldsObjectValueList = sonFieldsObjectValueList;
	}

	public List<String> getSonFieldNameList() {
		return sonFieldNameList;
	}

	public void setSonFieldNameList(List<String> sonFieldNameList) {
		this.sonFieldNameList = sonFieldNameList;
	}

	private List<String> sonFieldNameList = new ArrayList<String>();

	public IwUpload getIwUpload() {
		if (iwUpload == null) {
			iwUpload = new IwUpload();
			iwUpload.setIwFileList(new ArrayList<IwFile>());
		}
		return iwUpload;
	}

	public void setIwUpload(IwUpload iwUpload) {
		this.iwUpload = iwUpload;
	}

	private ObjectMapper objectMapper = new ObjectMapper();

	public TaskController() {

	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	@PostConstruct
	public void init() {
		procDefKey = FacesUtil.getUrlParam("process");
		taskId = StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
		view = FacesUtil.getUrlParam("view");
		taskAutonomeId = FacesUtil.getUrlParam("taskAutonome");
		functionId = FacesUtil.getUrlParam("functionId");
		taskHistId = FacesUtil.getUrlParam("tacheHist");
		initTasksMap();
		if (iwUpload == null) {
			iwUpload = new IwUpload();
			iwUpload.setIwFileList(new ArrayList<IwFile>());
		}
		if (StringUtils.isNotBlank(taskId)) {
			initTask();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			currentUserGroups = activitiService.getUserGroupList(userService.getLoggedInUser().getId());
			currentUserGroupsIds = new ArrayList<String>();
			for (Group grp : currentUserGroups) {
				currentUserGroupsIds.add(grp.getId());
			}
			initFormList();
			initGlobalForm();
			templateBeans = new ArrayList<String>();
			currentProgress = businessService.getIwProgressProcessTask(procInst != null ? procInst.getProcessDefinitionKey() : currentTask.getId(), currentTask.getTaskDefinitionKey());
		}
		if (StringUtils.isNotBlank(taskHistId)) {
			initTaskHist();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			currentUserGroups = activitiService.getUserGroupList(userService.getLoggedInUser().getId());
			currentUserGroupsIds = new ArrayList<String>();
			for (Group grp : currentUserGroups) {
				currentUserGroupsIds.add(grp.getId());
			}
			initFormHistList();
			initGlobalFormHist();
			templateBeans = new ArrayList<String>();
			currentProgress = businessService.getIwProgressProcessTask(procInstHist.get(0).getTaskDefinitionKey(), taskHist.getTaskDefinitionKey());
		}
		if (StringUtils.isNotBlank(procDefKey)) {
			initProcDef();
		}
	}

	public void initTasksMap() {
		tasksMap = new HashMap<String, Map<String, List<Task>>>();
		taskApp = new HashMap<String, List<Task>>();
		Map<String, List<Task>> taskByProcessMap;
		List<IwApplication> appList = navigationController.getUserApplicationList();
		List<String> processByAppList;
		List<Task> tasks;
		if (appList != null && !appList.isEmpty()) {
			for (IwApplication app : appList) {
				processByAppList = getAutorisedProcessKeis(app.getIwKey());
				taskByProcessMap = new HashMap<String, List<Task>>();
				if (processByAppList != null && !processByAppList.isEmpty()) {
					for (String pressesKey : processByAppList) {
						taskByProcessMap.put(pressesKey, getTaskListByProssesKeis(Arrays.asList(pressesKey)));
					}
					tasksMap.put(app.getIwKey(), taskByProcessMap);
					taskApp.put(app.getIwKey(), getTaskListByProssesKeis(processByAppList));
				}
			}
		}
		if (navigationController.getIndexActiveApp() > tasksMap.size()) {
			navigationController.setIndexActiveApp(0);
			navigationController.setIndexActiveProcess(0);
		}
		if (navigationController.getIndexActiveApp() > 0) {
			Map mapSelected = tasksMap.get((String) tasksMap.keySet().toArray()[navigationController.getIndexActiveApp() - 1]);
			if (navigationController.getIndexActiveProcess() > mapSelected.size()) {
				navigationController.setIndexActiveProcess(0);
			}
		}
	}

	public List<String> getAutorisedProcessKeis(String appKey) {
		List<String> result = new ArrayList<String>();
		List<String> processKeisList = businessService.getProcessKeyByApplication(appKey);
		List<String> processKeisIwRight = userService.getUserProcessKeyRights();
		if (processKeisList != null && !processKeisList.isEmpty()
		&& processKeisIwRight != null && !processKeisIwRight.isEmpty()) {
			for (String key : processKeisList) {
				if (processKeisIwRight.contains(key)) {
					result.add(key);
				}
			}
		}
		return result;
	}

	public List<Task> getTaskListByProssesKeis(List procKeis) {
		List<Task> tasks = new ArrayList<Task>();
		if ("my".equals(view) || StringUtils.isBlank(view)) {
			tasks = activitiService.getTasksForUser(userService.getLoggedInUser().getId(), procKeis);
		} else if ("involved".equals(view)) {
			tasks = activitiService.getTaskListForInvolvedUser(userService.getLoggedInUser().getId(), procKeis);
		} else if ("queue".equals(view)) {
			tasks = activitiService.getTaskQueueListForUser(userService.getLoggedInUser().getId(), procKeis);
		} else if ("standalone".equals(view)) {
			tasks = activitiService.getStandaloneTasksForUser(userService.getLoggedInUser().getId());
		}
		return tasks;
	}

	public void initStandaloneTask() {
		standaloneTask = activitiService.getTask(taskAutonomeId);
		currentTask = standaloneTask;
		taskAutonomeId = taskAutonomeId;
		if (taskAutonomeId != null) {
			if (StringUtils.isNotBlank(standaloneTask.getAssignee())) {
				assignee = activitiService.getUser(standaloneTask.getAssignee());
			}
			if (StringUtils.isNotBlank(standaloneTask.getOwner())) {
				owner = activitiService.getUser(standaloneTask.getOwner());
			}
			if (StringUtils.isNotBlank(standaloneTask.getFormKey())) {
				try {
					currentIwForm = businessService.getIwForm(Long.valueOf(standaloneTask.getFormKey()));
				} catch (Exception exp) {

					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
					LogManager
					.getLogger(TaskController.class
					).error(exp);
				}
			}
		}
	}

	public void initTaskHist() {
		taskHist = activitiService.getTaskHistById(taskHistId);
		if (taskHist != null) {
			procInstHist = activitiService.getProcessInstanceHistory(taskHist.getProcessInstanceId());
			if (procInstHist.size() > 0) {
				procTitle = procInstHist.get(0).getName();
				processInstanceName = procInstHist.get(0).getName();
			}
			if (StringUtils.isNotBlank(taskHist.getAssignee())) {
				assignee = activitiService.getUser(taskHist.getAssignee());
			}
			if (StringUtils.isNotBlank(taskHist.getOwner())) {
				owner = activitiService.getUser(taskHist.getOwner());
			}
			if (StringUtils.isNotBlank(taskHist.getFormKey())) {
				try {
					currentIwForm = businessService.getIwForm(Long.valueOf(taskHist.getFormKey()));
				} catch (Exception exp) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
					LogManager
					.getLogger(TaskController.class
					).error(exp);
				}
			}
		}
	}

	public void addFormHistWrapper(IwInput iwInput) {
		addFormHistWrapper(iwInput, iwInput);
	}

	public String startProcess() {
		ProcessInstance processInstance = activitiService.startProcessInstanceById(procDef.getId());
		if (processInstance != null) {
			activitiService.setProcessInstanceName(processInstance.getId(), processInstanceName);
			procTitle = processInstanceName;
			FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.message.processcreeavecsucces", procTitle));
			return getNextTask(processInstance.getId(), processInstance.getProcessDefinitionId(), true);
		} else {
			return getNextTask(null, procDefKey, true);
		}
	}

	public String startProcess(String procDefId) {
		ProcessInstance processInstance = activitiService.startProcessInstanceById(procDefId);
		if (processInstance != null) {
			activitiService.setProcessInstanceName(processInstance.getId(), processInstanceName);
			procTitle = processInstanceName;
			FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.message.processcreeavecsucces", procTitle));
			return getNextTask(processInstance.getId(), processInstance.getProcessDefinitionId(), true);
		} else {
			return getNextTask(null, procDefKey, true);
		}
	}

	public String startProcess(boolean disPlay) {
		if (!disPlay) {
			ProcessInstance processInstance = activitiService.startProcessInstanceById(procDef.getId());
			if (processInstance != null) {
				activitiService.setProcessInstanceName(processInstance.getId(), processInstanceName);
				procTitle = processInstanceName;
				FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.message.processcreeavecsucces", procTitle));
				return getNextTask(processInstance.getId(), processInstance.getProcessDefinitionId(), true);
			} else {
				return getNextTask(null, procDefKey, true);
			}
		}
		return null;
	}

	public Task getTask() {
		return activitiService.getTask(taskId);
	}

	public List<Deployment> getDeployementList() {
		return activitiService.getDeployementList();
	}

	public void initTaskList() {
		List<String> processKeyList = userService.getUserProcessKeyRights();
		if ("my".equals(view) || StringUtils.isBlank(view)) {
			taskList = activitiService.getTasksForUser(userService.getLoggedInUser().getId(), processKeyList);
		} else if ("involved".equals(view)) {
			taskList = activitiService.getTaskListForInvolvedUser(userService.getLoggedInUser().getId(), processKeyList);
		} else if ("queue".equals(view)) {
			taskList = activitiService.getTaskQueueListForUser(userService.getLoggedInUser().getId(), processKeyList);
		} else if ("standalone".equals(view)) {
			taskList = activitiService.getStandaloneTasksForUser(userService.getLoggedInUser().getId());
		} else {
			historicTaskList = activitiService.getTaskArchiveListForUser(userService.getLoggedInUser().getId(), processKeyList);
		}
		String appkey = FacesUtil.getUrlParam("applicationKey");
		if (StringUtils.isNotBlank(appkey)) {
			filterTaskByApplication(appkey);
		}
	}

	public void filterTaskByApplication(String appkey) {
		List<Task> tasksApp = new ArrayList<Task>();
		List<Model> modelsApp = activitiService.getModelListByCategory(appkey);
		for (Task task : taskList) {
			for (Model model : modelsApp) {
				if (model.getCategory() != null && model.getCategory().equals(activitiService.getCategoryModelProcessDefId(task.getProcessDefinitionId()))) {
					tasksApp.add(task);
					break;
				}
			}
		}
		taskList = tasksApp;
	}

	public void initProcDef() {
		procDef = activitiService.getProcessDefinitionByKey(procDefKey);
		if (procDef != null) {
			processInstanceName = procDef.getName();
		}
	}

	public void initTask() {
		currentTask = activitiService.getTask(taskId);
		if (currentTask != null) {
			procInst = activitiService.getProcessInstanceById(currentTask.getProcessInstanceId());
			if (procInst != null) {
				procTitle = procInst.getName();
				processInstanceName = procInst.getName();
			}
			if (StringUtils.isNotBlank(currentTask.getAssignee())) {
				assignee = activitiService.getUser(currentTask.getAssignee());
			}
			if (StringUtils.isNotBlank(currentTask.getOwner())) {
				owner = activitiService.getUser(currentTask.getOwner());
			}
			if (StringUtils.isNotBlank(currentTask.getFormKey())) {
				try {
					currentIwForm = businessService.getIwForm(Long.valueOf(currentTask.getFormKey()));
				} catch (Exception exp) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
					LogManager
					.getLogger(TaskController.class
					).error(exp);
				}
			}
		}
	}

	public void addFormWrapper(IwInput iwInput, IwInput parentIw) {
		boolean canView = false;
		boolean canEdit = false;
		boolean isDisplayable = false;
		if ((parentIw.getReadusers() == null || parentIw.getReadusers().isEmpty()) && (parentIw.getReadgroups() == null || parentIw.getReadgroups().isEmpty())) {
			canView = true;
		} else if (parentIw.getReadusers().contains(userService.getLoggedInUser().getId())) {
			canView = true;
		} else if (CollectionUtils.containsAny(parentIw.getReadgroups(), currentUserGroupsIds)) {
			canView = true;
		}
		if ((parentIw.getTachesbyprocess() == null || parentIw.getTachesbyprocess().isEmpty()) || StringUtils.isNotBlank(taskAutonomeId)) {
			isDisplayable = true;
		} else {
			for (Entry<String, List<String>> item : parentIw.getTachesbyprocess().entrySet()) {
				if (item != null
				&& StringUtils.isNotEmpty(item.getKey())
				&& item.getValue() != null && !item.getValue().isEmpty()) {
					if (procInst.getProcessDefinitionKey().equals(item.getKey())) {
						for (String subItem : item.getValue()) {
							if (currentTask.getTaskDefinitionKey().equals(subItem)) {
								isDisplayable = true;
								break;
							}
						}
					}
				}
			}
		}
		if ((parentIw.getWriteusers() == null || parentIw.getWriteusers().isEmpty()) && (parentIw.getWritegroups() == null || parentIw.getWritegroups().isEmpty())) {
			canEdit = true;
		} else if (parentIw.getWriteusers().contains(userService.getLoggedInUser().getId())) {
			canEdit = true;
		} else if (CollectionUtils.containsAny(parentIw.getWritegroups(), currentUserGroupsIds)) {
			canEdit = true;
		}

		if (iwInput.getDisabledfortaches()!= null && !iwInput.getDisabledfortaches().isEmpty()) {
			for (String s : iwInput.getDisabledfortaches()) {
				if (currentTask.getTaskDefinitionKey().contains(s.substring(s.indexOf("-")+1))) {
					canEdit = false;
				}
			}
		}
		Iterator<String> columns = iwInput.getOptions2().iterator();
		int index = 0;
		while (columns.hasNext()) {
			boolean remove = false;
			String column = columns.next();
			if (!((iwInput.getEntityUserRights() == null
			|| iwInput.getEntityUserRights().isEmpty())
			&& (iwInput.getEntityGroupRights() == null
			|| iwInput.getEntityGroupRights().isEmpty()))) {
				if (iwInput.getEntityUserRights().size() >= index) {
					if (iwInput.getEntityUserRights().get(index) != null
					&& !iwInput.getEntityUserRights().get(index).isEmpty()
					&& !iwInput.getEntityUserRights().get(index).contains(userService.getLoggedInUser().getId())) {
						remove = true;
					}
				}
				if (!remove && iwInput.getEntityGroupRights().size() >= index) {
					if (iwInput.getEntityGroupRights().get(index) != null
					&& !iwInput.getEntityGroupRights().get(index).isEmpty()
					&& !CollectionUtils.containsAny(iwInput.getEntityGroupRights().get(index), currentUserGroupsIds)) {
						remove = true;
					}
				}
				if (remove) {
					columns.remove();
				}
			}
			index++;
		}
		if (taskVars.keySet().contains(iwInput.getId())) {
			FormWrapper frm = formWrapperFactory.createFormWrapper(iwInput, taskVars.get(iwInput.getId()), canView, canEdit, businessService, activitiService, taskId, procInst.getId());
			if ("multilineentity".equals(frm.getIwInput().getComponent())) {
				List entityList = new ArrayList<Object>();
				Object entity;
				List entitys = (List) taskVars.get(iwInput.getId());
				if (entitys != null && !entitys.isEmpty()) {
					for (Object idEntity : entitys) {
						entity = businessService.getEntitytById(iwInput.getValue(), String.valueOf(idEntity));
						if (entity != null) {
							entityList.add(entity);
						}
					}
					((MultilineEntityFormWrapper) frm).setEntityList(entityList);
				}
			}
			allFormList.put(frm.getIwInput().getId(), frm);
			if (canView && isDisplayable)
				formList.put(frm.getIwInput().getId(), frm);
		} else {
			String wrapperProcInstId = null;
			if (procInst != null) {
				wrapperProcInstId = procInst.getId();
			}
			FormWrapper formWrapper = formWrapperFactory.createFormWrapper(iwInput, null, canView, canEdit, businessService, activitiService, taskId, wrapperProcInstId);
			allFormList.put(iwInput.getId(), formWrapper);
			if (canView && isDisplayable)
				formList.put(iwInput.getId(), formWrapper);
		}
	}

	public void addFormHistWrapper(IwInput iwInput, IwInput parentIw) {
		boolean canView = false;
		boolean canEdit = false;
		boolean isDisplayable = false;
		if ((parentIw.getReadusers() == null || parentIw.getReadusers().isEmpty()) && (parentIw.getReadgroups() == null || parentIw.getReadgroups().isEmpty())) {
			canView = true;
		} else if (parentIw.getReadusers().contains(userService.getLoggedInUser().getId())) {
			canView = true;
		} else if (CollectionUtils.containsAny(parentIw.getReadgroups(), currentUserGroupsIds)) {
			canView = true;
		}
		if ((parentIw.getTachesbyprocess() == null || parentIw.getTachesbyprocess().isEmpty())) {
			isDisplayable = true;
		} else {
			for (Entry<String, List<String>> item : parentIw.getTachesbyprocess().entrySet()) {
				if (item != null
				&& StringUtils.isNotEmpty(item.getKey())
				&& item.getValue() != null && !item.getValue().isEmpty()) {

					if (taskHist.getProcessDefinitionId().split(":")[0].equals(item.getKey())) {
						for (String subItem : item.getValue()) {
							if (taskHist.getTaskDefinitionKey().equals(subItem)) {
								isDisplayable = true;
								break;
							}
						}
					}
				}
			}
		}
		if ((parentIw.getWriteusers() == null || parentIw.getWriteusers().isEmpty()) && (parentIw.getWritegroups() == null || parentIw.getWritegroups().isEmpty())) {
			canEdit = true;
		} else if (parentIw.getWriteusers().contains(userService.getLoggedInUser().getId())) {
			canEdit = true;
		} else if (CollectionUtils.containsAny(parentIw.getWritegroups(), currentUserGroupsIds)) {
			canEdit = true;
		}
		Iterator<String> columns = iwInput.getOptions2().iterator();
		int index = 0;
		while (columns.hasNext()) {
			boolean remove = false;
			String column = columns.next();
			if (!((iwInput.getEntityUserRights() == null
			|| iwInput.getEntityUserRights().isEmpty())
			&& (iwInput.getEntityGroupRights() == null
			|| iwInput.getEntityGroupRights().isEmpty()))) {
				boolean tobedifferent = true;

				if (iwInput.getEntityUserRights().size() >= index) {
					if (iwInput.getEntityUserRights().get(index) != null
					&& !iwInput.getEntityUserRights().get(index).isEmpty()
					&& !iwInput.getEntityUserRights().get(index).contains(userService.getLoggedInUser().getId())) {
						remove = true;
					}
				}
				if (!remove && iwInput.getEntityGroupRights().size() >= index) {
					if (iwInput.getEntityGroupRights().get(index) != null
					&& !iwInput.getEntityGroupRights().get(index).isEmpty()
					&& !CollectionUtils.containsAny(iwInput.getEntityGroupRights().get(index), currentUserGroupsIds)) {
						remove = true;
					}
				}
				if (remove) {
					columns.remove();
				}
			}
			index++;
		}
		if (taskVarsHist.keySet().contains(iwInput.getId())) {
			if (canView && isDisplayable) {
				FormWrapper frm = formWrapperFactory.createFormWrapper(iwInput, taskVarsHist.get(iwInput.getId()), canView, canEdit, businessService, activitiService, taskHistId, procInstHist.get(0).getId());
				if ("multilineentity".equals(frm.getIwInput().getComponent())) {
					List entityList = new ArrayList<Object>();
					Object entity;
					List entitys = (List) taskVarsHist.get(iwInput.getId());
					if (entitys != null && !entitys.isEmpty()) {
						for (Object idEntity : entitys) {
							entity = businessService.getEntitytById(iwInput.getValue(), String.valueOf(idEntity));
							if (entity != null) {
								entityList.add(entity);
							}
						}
						((MultilineEntityFormWrapper) frm).setEntityList(entityList);
					}
				}
				formListHist.put(iwInput.getId(), frm);
			}
		} else if (canView && isDisplayable) {
			String wrapperProcInstId = null;
			if (procInst != null) {
				wrapperProcInstId = procInst.getId();
			}
			FormWrapper formWrapper = formWrapperFactory.createFormWrapper(iwInput, null, canView, canEdit, businessService, activitiService, taskId, wrapperProcInstId);
			formListHist.put(iwInput.getId(), formWrapper);
		}
	}

	public void initGlobalFormHist() {
		if (procInstHist.size() > 0 && taskHist != null) {
			String processKey = procInstHist.get(0).getTaskDefinitionKey();
			List<IwVariableProcess> variableList = businessService.getVariableProcess(processKey, null, Boolean.TRUE);
			globalFormList = new ArrayList<Map<String, Object>>();
			for (IwVariableProcess var : variableList) {
				Map<String, Object> val = new HashMap<String, Object>();
				val.put("var", var);
				val.put("val", activitiService.getVariableProcess(taskHist.getProcessInstanceId(), var.getIwInput().getId()));
				globalFormList.add(val);
			}
		}
	}

	public void addFormWrapper(IwInput iwInput) {
		addFormWrapper(iwInput, iwInput);
	}

	public void initFormList() {
		if (formList == null) {
			formList = new LinkedHashMap<String, FormWrapper>();
		}
		List<String> varList = new ArrayList<String>();
		taskVars = activitiService.getTaskVariables(taskId);
		if (currentTask != null && StringUtils.isNotBlank(currentTask.getFormKey())) {
			try {
				if (currentIwForm != null) {
					List<IwInput> iwInputListBis = objectMapper.readValue(currentIwForm.getIwFormSourceJson(), new TypeReference<List<IwInput>>() {
					});
					for (IwInput iwInput : iwInputListBis) {
						if ("fform".equals(iwInput.getComponent())) {
							IwForm subForm;
							List<IwInput> subIwInputList = new ArrayList<IwInput>();
							if (StringUtils.isNotEmpty(iwInput.getId())) {
								subForm = businessService.getIwForm(Long.valueOf(iwInput.getValue()));
								if (subForm != null) {
									subIwInputList = objectMapper.readValue(subForm.getIwFormSourceJson(), new TypeReference<List<IwInput>>() {
									});
								}
								if (iwInput.getOptions3() != null
								&& iwInput.getOptions3().size() > 0
								&& Boolean.valueOf(iwInput.getOptions3().get(0))) {
									for (IwInput subInput : subIwInputList) {
										addFormWrapper(subInput, iwInput);
									}
								} else {
									for (IwInput subInput : subIwInputList) {
										addFormWrapper(subInput);
									}
								}
							}
						} else {
							addFormWrapper(iwInput);
						}
					}
				} else {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
				}
			} catch (IOException ex) {
				LogManager.getLogger(TaskController.class).error(ex);
			}
		} else {
			TaskFormData taskFormData = activitiService.getTaskFormData(taskId);
			for (FormProperty frm : taskFormData.getFormProperties()) {
				varList.add(frm.getId());
			}
			taskVars = activitiService.getTaskVariables(taskId, varList);
			for (FormProperty frm : taskFormData.getFormProperties()) {
				if (taskVars.keySet().contains(frm.getId())) {
					formList.put(taskVars.get(frm.getId()).toString(), formWrapperFactory.createFormWrapper(frm, taskVars.get(frm.getId())));
				} else {
					formList.put(frm.getId(), formWrapperFactory.createFormWrapper(frm, frm.getValue()));
				}
			}
		}
	}

	public void initFormHistList() {
		if (formListHist == null) {
			formListHist = new LinkedHashMap<String, FormWrapper>();
		}
		Set<String> formKey = new HashSet<String>();
		List<String> varList = new ArrayList<String>();
		taskVarsHist = taskHist.getProcessVariables();
		if (StringUtils.isNotBlank(taskHist.getFormKey())) {
			try {
				if (currentIwForm != null) {
					List<IwInput> iwInputListBis = objectMapper.readValue(currentIwForm.getIwFormSourceJson(), new TypeReference<List<IwInput>>() {
					});
					formKey.add(taskHist.getFormKey());
					List<IwForm> formListBis = businessService.getIwFormListByFormKey(formKey);
					List<IwInput> listInputs = businessService.getIwInputByFormId(formListBis.get(0));
					for (IwInput iwInput : iwInputListBis) {
						if ("fform".equals(iwInput.getComponent())) {
							IwForm subForm;
							List<IwInput> subIwInputList;

							if (StringUtils.isNotEmpty(iwInput.getId())) {
								subForm = businessService.getIwForm(Long.valueOf(iwInput.getValue()));
								subIwInputList = objectMapper.readValue(subForm.getIwFormSourceJson(), new TypeReference<List<IwInput>>() {
								});
								if (iwInput.getOptions3() != null
								&& iwInput.getOptions3().size() > 0
								&& Boolean.valueOf(iwInput.getOptions3().get(0))) {
									for (IwInput subInput : subIwInputList) {
										addFormHistWrapper(subInput, iwInput);
									}
								} else {
									for (IwInput subInput : subIwInputList) {
										addFormHistWrapper(subInput);
									}
								}
							}
						} else {
							addFormHistWrapper(iwInput);
						}
					}
				} else {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
				}
			} catch (IOException ex) {
				LogManager.getLogger(TaskController.class).error(ex);
			}
		} else {
			TaskFormData taskFormData = activitiService.getTaskFormData(taskHistId);
			for (FormProperty frm : taskFormData.getFormProperties()) {
				varList.add(frm.getId());
			}
			taskVarsHist = taskHist.getProcessVariables();
			for (FormProperty frm : taskFormData.getFormProperties()) {
				if (taskVarsHist.keySet().contains(frm.getId())) {
					formListHist.put(taskVarsHist.get(frm.getId()).toString(), formWrapperFactory.createFormWrapper(frm, taskVarsHist.get(frm.getId())));
				} else {
					formListHist.put(taskVarsHist.get(frm.getId()).toString(), formWrapperFactory.createFormWrapper(frm, frm.getValue()));
				}
			}
		}
	}

	public void initFormListListner(ComponentSystemEvent event) {
		initFormList();
	}

	public void initGlobalForm() {
		if (procInst != null && currentTask != null) {
			String processKey = procInst.getProcessDefinitionKey();
			List<IwVariableProcess> variableList = businessService.getVariableProcess(processKey, null, Boolean.TRUE);
			globalFormList = new ArrayList<Map<String, Object>>();
			for (IwVariableProcess var : variableList) {
				Map<String, Object> val = new HashMap<String, Object>();
				val.put("var", var);
				val.put("val", activitiService.getVariableProcess(currentTask.getProcessInstanceId(), var.getIwInput().getId()));
				globalFormList.add(val);
			}
		}
	}

	public List<IwVariableProcess> getVariableProcessList() {
		List<IwVariableProcess> variableList = businessService.getVariableProcess(procDefKey, Boolean.TRUE, null);
		return variableList;
	}

	public List<IwVariableProcess> getVariableProcessByKeyList(String procDefKey) {
		List<IwVariableProcess> variableList = businessService.getVariableProcess(procDefKey, Boolean.TRUE, null);
		return variableList;
	}

	public List<Map<String, Object>> getGlobalFormList() {
		return globalFormList;
	}

	public void setGlobalFormList(List<Map<String, Object>> globalFormList) {
		this.globalFormList = globalFormList;
	}

	public Map<String, Object> getVarMap() {
		Map<String, Object> varMap = new HashMap<String, Object>();
		Iterator formWrapperIterator = formList.keySet().iterator();
		if (StringUtils.isNotBlank(currentTask.getFormKey())) {
			for (Map.Entry<String, FormWrapper> entry : formList.entrySet()) {
				String key = entry.getKey();
				FormWrapper frm = entry.getValue();
				frm.persiste(businessService, activitiService);
				if ("multilineentity".equals(frm.getIwInput().getComponent())) {
					businessService.saveEntityList(((MultilineEntityFormWrapper) frm).getEntityList());
					List entityIdList = null;
					if (((MultilineEntityFormWrapper) frm).getEntityList() != null && !((MultilineEntityFormWrapper) frm).getEntityList().isEmpty()) {
						entityIdList = new ArrayList();
						for (Object entity : ((MultilineEntityFormWrapper) frm).getEntityList()) {
							entityIdList.add(getIdEntity(entity, frm.getIwInput().getValue()));
						}
					}
					frm.setValue(entityIdList);
					varMap.put(frm.getIwInput().getId(), frm.getValue());
				} else if ("multilineentityconfig".equals(frm.getIwInput().getComponent())) {
					if (((MultilineEntityConfigFormWrapper) frm).getEntityList() != null
					&& !((MultilineEntityConfigFormWrapper) frm).getEntityList().isEmpty()) {
						Long idEntity;
						for (Object obj : ((MultilineEntityConfigFormWrapper) frm).getEntityList()) {
							idEntity = getLongValue(getIdEntity(obj, frm.getIwInput().getValue()));
							if (idEntity != null && idEntity.longValue() < 0) {
								setIdEntity(obj, frm.getIwInput().getValue(), null);
							}
						}
						businessService.saveEntityList(((MultilineEntityConfigFormWrapper) frm).getEntityList());
					}
					if (((MultilineEntityConfigFormWrapper) frm).getConfigEntityListDeleted() != null
					&& !((MultilineEntityConfigFormWrapper) frm).getConfigEntityListDeleted().isEmpty()) {
						Long idEntity;
						for (Object entity : ((MultilineEntityConfigFormWrapper) frm).getConfigEntityListDeleted()) {
							idEntity = getLongValue(getIdEntity(entity, frm.getIwInput().getValue()));
							if (idEntity == null || idEntity.longValue() > 0) {
								businessService.removeEntite(entity);
							}
						}
					}
				} else if (!"staticText".equals(frm.getIwInput().getComponent())) {
					varMap.put(frm.getIwInput().getId(), frm.getValue());
				}
			}
		} else {
			for (Map.Entry<String, FormWrapper> entry : formList.entrySet()) {
				String key = entry.getKey();
				FormWrapper frm = entry.getValue();
				if (frm.getForm().isReadable() && frm.getForm().isWritable()) {
					varMap.put(frm.getForm().getId(), frm.getValue());
					frm.persiste(businessService, activitiService);
					if ("multilineentity".equals(frm.getIwInput().getComponent())) {
						businessService.saveEntityList(((MultilineEntityFormWrapper) frm).getEntityList());
						List entityIdList = null;
						if (frm.getValue() != null && !((MultilineEntityFormWrapper) frm).getEntityList().isEmpty()) {
							entityIdList = new ArrayList();
							for (Object entity : ((MultilineEntityFormWrapper) frm).getEntityList()) {
								entityIdList.add(getIdEntity(entity, frm.getIwInput().getValue()));
							}
						}
						frm.setValue(entityIdList);
						varMap.put(frm.getIwInput().getId(), frm.getValue());
					}
				}
			}
		}
		return varMap;
	}

	public DocxData getDocxDataFromMultilineEnity(MultilineEntityFormWrapper frm) {
		DocxData data = new DocxData();
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		Map row;
		List<String> placeholders = new ArrayList<String>();
		Class classEntity;
		try {
			// Get all columns
			classEntity = Class.forName(frm.getIwInput().getValue());
			for (Field field : classEntity.getDeclaredFields()) {
				field.setAccessible(true);
				placeholders.add(field.getName());
			}
			if (frm.getEntityList() != null) {
				for (Object entity : frm.getEntityList()) {
					row = new HashMap<String, String>();
					for (Field field : classEntity.getDeclaredFields()) {
						field.setAccessible(true);
						try {
							row.put(field.getName(), field.get(entity).toString());
						} catch (Exception ex) {
							row.put(field.getName(), "");
						}
					}
					rows.add(row);
				}
			}
			data.setRows(rows);
			data.setColumns(placeholders);
		} catch (ClassNotFoundException ex) {

		}
		return data;
	}
	private DocxData getDoxDataFromChecklist( ArrayList<String> optionsList, ArrayList<String>checkedOptionsList){
		DocxData data = new DocxData();
		List<String> placeholders = new ArrayList<String>();
		placeholders.add("value");
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		Map<String, String> row;

		for (String option:optionsList
		) {
			row = new HashMap<>();
			String c = "";
			if (existe(option,checkedOptionsList)){
				c ="\u2611 - "+ option;
			}
			else {
				c ="\u2610 - "+ option;
			}
			row = new HashMap<>();
			row.put("value" , c);
			rows.add(row);
		}
		data.setColumns(placeholders);
		data.setRows(rows);
		return data;
	}

	public boolean existe(String s,ArrayList<String> list){
		if(list != null ){
			for (String c :list
			) {
				if(s.equals(c)){
					return true;
				}
			}
			return false;
		}else return false;

	}

	private String getDoxDataFromCheckbox(FormWrapper frm){
		String c = "";
		if((Boolean) frm.getValue()){
			c = "\u2611 - "+frm.getIwInput().getOptions().get(0);
		}else {
			c = "\u2610 - "+frm.getIwInput().getOptions().get(0);
		}
		return c;
	}

	public Map<String, Object> getVarMapReport() {
		Map<String, Object> varMap = new HashMap<String, Object>();
		Iterator formListIterator = allFormList.keySet().iterator();
		if (StringUtils.isNotBlank(currentTask.getFormKey())) {
			for (Map.Entry<String, FormWrapper> entry : allFormList.entrySet()) {
				String key = entry.getKey();
				FormWrapper frm = entry.getValue();
				if ("multilineentity".equals(frm.getIwInput().getComponent())) {
					varMap.put(frm.getIwInput().getId(), getDocxDataFromMultilineEnity((MultilineEntityFormWrapper) frm));
				}else if ("checklist".equals(frm.getIwInput().getComponent())) {
					optionsList = frm.getIwInput().getOptions();
					if (frm.getValue() != null){
						checkedOptionsList = new ArrayList(Arrays.asList((String []) frm.getValue()));
					}
					varMap.put(frm.getIwInput().getId(), getDoxDataFromChecklist(optionsList,checkedOptionsList));

				}else if ("checkbox".equals(frm.getIwInput().getComponent())) {

					varMap.put(frm.getIwInput().getId(), getDoxDataFromCheckbox(frm));

				} else if (!"staticText".equals(frm.getIwInput().getComponent())/*"textInput".equals(frm.getIwInput().getComponent()) || "select".equals(frm.getIwInput().getComponent()) || "textArea".equals(frm.getIwInput().getComponent()) || "date".equals(frm.getIwInput().getComponent())*/) {
					varMap.put(frm.getIwInput().getId(), convertForDocx(frm.getValue()));
				}
			}

			for (String bean : templateBeans) {
				if (((FormTemplate) appContext.getBean(bean)).getVarValuesReport() != null)
					varMap.putAll(((FormTemplate) appContext.getBean(bean)).getVarValuesReport());
			}

		} else {
			for (Map.Entry<String, FormWrapper> entry : allFormList.entrySet()) {
				String key = entry.getKey();
				FormWrapper frm = entry.getValue();
				if ("multilineentity".equals(frm.getIwInput().getComponent())) {
					varMap.put(frm.getIwInput().getId(), getDocxDataFromMultilineEnity((MultilineEntityFormWrapper) frm));
				} else if ("textInput".equals(frm.getIwInput().getComponent()) || "select".equals(frm.getIwInput().getComponent()) || "textArea".equals(frm.getIwInput().getComponent())) {
					varMap.put(frm.getIwInput().getId(), convertForDocx(frm.getValue()));
				}
			}
		}
		return varMap;
	}

	public String completeTask() {
		if (StringUtils.isNotBlank(currentTask.getFormKey())) {
			if (currentIwForm == null) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.tache.formulairenontrouve"));
				return "formulaire?task=" + currentTask.getId() + "&faces-redirect=true";
			}
		}

		// TODO :save Valide Congé
		if (!saveCurrentValidConge(this.formList)) {
			return "formulaire?task=" + currentTask.getId() + "&faces-redirect=true";
		}
		String procInstId = null;
		String procDefKey = null;
		if (currentTask
		!= null) {
			procInstId = currentTask.getProcessInstanceId();
			procDefKey = currentTask.getProcessDefinitionId();
		}
		try {
			Map<String, Object> varsVals = getVarMap();
			for (String bean : templateBeans) {
				varsVals.putAll(((FormTemplate) appContext.getBean(bean)).getVarValues());
			}

			List<AldFormation> validateList =  new ArrayList<>();
			List<AldFormation> formations  ;
			List<AldFormation> formationList =  new ArrayList<>();

				if (currentTask.getName().equals("T04 - Rensienger les dates de formations  et inscription de la personne RH") ||
				currentTask.getName().equals("T08 - Rensienger les dates de formations en atentes"))
			{formations = (ArrayList<AldFormation>) varsVals.get("formationsList");
				for (int i=0 ;  i<formations.size();i++)
				{
					try
					{if (formations.get(i).getRejected() == true && !formations.get(i).getDateFormation().equals(null))

						validateList.add(formations.get(i)) ;
				else if(formations.get(i).getRejected() == true)
						formationList.add(formations.get(i)) ;
					}catch (Exception e)
					{
						formationList.add(formations.get(i)) ;
					}
				}


				taskVars.remove("formationsList");
				taskVars.put("formationsList",formationList);
				taskVars.put("rejectedList",formationList);
				taskVars.put("validateList",validateList) ;
				varsVals.remove("formationsList");
				varsVals.put("formationsList",formationList);
				varsVals.put("rejectedList",formationList);
			    varsVals.put("validateList",validateList) ;



			}
			if (currentTask.getName().equals("T08 - Rensienger les dates de formations en atentes"))
			{//taskVars.replace("validateList",(ArrayList<AldFormation>) taskVars.get("validateList"),(ArrayList<AldFormation>)  varsVals.get("formationsList")) ;
				//varsVals.replace("validateList",(ArrayList<AldFormation>) varsVals.get("validateList"),(ArrayList<AldFormation>) varsVals.get("formationsList")) ;
			}
			activitiService.completeTask(taskId, varsVals);


			FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.message.tachetermineavecsucces", currentTask.getName()));
			for(Object obj : businessService.getEntiteList(IwNotification.class.getName())){
				IwNotification iwNotification = (IwNotification)obj;
				if (iwNotification.getActive()){
					String tas=iwNotification.getTasks();
					List<String> allTasks=new ArrayList<String>();
					String[] partstask = iwNotification.getTasks().split(";");
					for (String s:partstask) {
						String[] partstasks = s.split("--");
						allTasks.add(partstasks[1]);
					}
					for (String ss:allTasks){
						if(currentTask !=null &&  currentTask.getName().equals(ss)){
							// Create the email message
							MultiPartEmail email = new MultiPartEmail();

							email.setHostName("smtp.gmail.com");
							email.setAuthentication("mailsender2@imaginepartners.com", "a&n3WBuN");
							email.setStartTLSEnabled(true);
							email.setSmtpPort(465);
							email.setFrom("mailsender2@imaginepartners.com", iwNotification.getFromName());
							email.setSubject(iwNotification.getSubject());
							List<String> finalList=new ArrayList<String>();
							String[] partsUser = iwNotification.getToUser().split(";");
							String[] partsGroup = iwNotification.getToRole().split(";");
							// add receptients
							for(String receptient : partsUser)
							{User usr=activitiService.getUser(receptient);
								finalList.add(usr.getEmail());
							}
							for(String receptient : partsGroup)
							{List<User> listUser=activitiService.getMemerbs(receptient);
								for (User user:listUser)
									finalList.add(user.getEmail());
							}
							for (String to:finalList){
								email.addTo(to,to.toString());}
							email.addTo(iwNotification.getToEmail(),"");
							if (iwNotification.getNotifierResponsable()){
								String responsible=businessService.getResponsibleForUser(userService.getLoggedInUser().getId());
								User user=activitiService.getUser(responsible);
								email.addTo(user.getEmail());
							}
							//add email's body
							email.setMsg(iwNotification.getBody());
							if(iwNotification.getAttachment() != null){
								// add the attachment
								EmailAttachment attachment = new EmailAttachment();
								// Initialize the uploadID variable
								String[] attachments = iwNotification.getAttachment() .split(";");
								for (String s:attachments){
									Long uploadID = (Long) varsVals.get(s);

									// Get the IwFile file corresponding to the given uploadID
									List<IwFile> iwFileList = businessService.getIwFilesForUpload(uploadID);
									for(IwFile iwFile : iwFileList){
										// Initialize the email attachment object so that it corresponds to our IwFile
										attachment.setPath(iwFile.getIwPath());
										attachment.setDisposition(EmailAttachment.ATTACHMENT);
										attachment.setName(iwFile.getIwName());
										email.attach(attachment);
									}}
							}
							// send the email
							email.setSSLOnConnect(true);
							email.send();
						}}
				}
			}
		} catch (Exception exp) {
			FacesUtil.setAjaxInfoMessage(exp.getLocalizedMessage());
		}
		try {
			return getNextTask(procInstId, procDefKey, false);
		} catch (Exception exp) {
			FacesUtil.setAjaxInfoMessage(exp.getLocalizedMessage());
		}
		return "?includeViewParams=true";
	}

	public String getNextTask(String procInstId, String procDefKey, boolean processNav) {
		if (procInstId != null) {
			Task nextTask = activitiService.getTaskForUserByProcInstId(userService.getLoggedInUser().getId(), procInstId);
			if (nextTask != null && procDefKey != null )  {
           try {
			   if (!currentTask.getName().equals("T05 - RH --> Upload de l'attestation"))
				   return "formulaire?task=" + nextTask.getId() + "&faces-redirect=true";
		   }  catch (NullPointerException e )
		   {
			   return "formulaire?task=" + nextTask.getId() + "&faces-redirect=true";
		   }
			} else {
				List<Task> procTasks;
				if (currentTask != null)
					procTasks = activitiService.getTasksByExecId(currentTask.getExecutionId());
				else
					procTasks = activitiService.getTaskForUserByProcInstId(procInstId);

				if (!procTasks.isEmpty()) {
					usersList(procTasks);
				} else {
					procTasks = activitiService.getTaskForUserByProcInstId(procInstId);
					if (!procTasks.isEmpty()) {
						usersList(procTasks);
					}
				}
				if (processNav) {
					return "liste?process=" + procDefKey + "&faces-redirect=true";
				}
			}
		}
		return "liste?faces-redirect=true";
	}

	private void usersList(List<Task> tasks) {
		String assignee = "";
		String taskName = "";
		for (Task tsk : tasks) {



			 if (tsk.getName().equals("T06 - Evaluation a chaud par le collaborateur")) {
				 Map<String, Object> varsMap = activitiService.getTaskVariables(tsk.getId(), null);
				 AldFormation formation = (AldFormation) varsMap.get("formation");
				 try { if (formation.getRejected() && (!formation.getDateFormation().equals(null)))
					 assignee += tsk.getAssignee() + ", "; }
					 catch(Exception e)
				 {

				 }
			 } else {
				 assignee += tsk.getAssignee() + ", ";
			 }

			taskName = tsk.getName();
			if (StringUtils.isBlank(tsk.getAssignee())) {
				List<String> candUsers = new ArrayList<String>();
				List<IdentityLink> ilList = activitiService.getIdentityLinkForTask(tsk.getId());
				for (IdentityLink il : ilList) {
					if (StringUtils.isNotBlank(il.getUserId())) {
						candUsers.add(il.getUserId());
					}
				}
				if (candUsers.isEmpty()) {
					for (IdentityLink il : ilList) {
						if (StringUtils.isNotBlank(il.getGroupId())) {
							candUsers.add(il.getGroupId());
						}
					}
				}
				if (!candUsers.isEmpty()) {
					assignee = StringUtils.join(candUsers, ",");
				}
			}
		}
		if (StringUtils.isNotBlank(assignee)) {
			FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.message.tacheassigne", taskName, assignee));
		} else {
			FacesUtil.setSessionWarnMessage(FacesUtil.getMessage("iw.message.prochainetachenonassigne"));
		}
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map<String, FormWrapper> getFormList() {
		return formList;
	}

	public void setFormList(Map<String, FormWrapper> formList) {
		this.formList = formList;
	}

	public List<Task> getTaskList() {
		procInstVars = new HashMap<String, Map<String, Object>>();
		initTaskList();
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public String getProcTitle() {
		return procTitle;
	}

	public void setProcTitle(String procTitle) {
		this.procTitle = procTitle;
	}

	public String getProcessName(String procDefKey) {
		ProcessDefinition procDef = activitiService.getProcessDefinitionById(procDefKey);
		if (procDef != null) {
			return procDef.getName();
		}
		return "";
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public String getUserName(String userId) {
		User user = activitiService.getUser(userId);
		if (user == null) {
			return "";
		}
		String userName = "";
		if (StringUtils.isNotBlank(user.getLastName())) {
			userName = userService.getLoggedInUser().getLastName();
		}
		if (StringUtils.isNotBlank(user.getFirstName())) {
			userName += " " + userService.getLoggedInUser().getFirstName();
		}
		if (StringUtils.isNotBlank(userName)) {
			userName = user.getId();
		} else {
			userName += " ( "
			+ user.getId()
			+ " )";
		}
		return userName;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public List<User> getCandidatUserList() {
		return candidatUserList;
	}

	public void setCandidatUserList(List<User> candidatUserList) {
		this.candidatUserList = candidatUserList;
	}

	public List<Group> getCandidatGroupList() {
		return candidatGroupList;
	}

	public void setCandidatGroupList(List<Group> candidatGroupList) {
		this.candidatGroupList = candidatGroupList;
	}

	public void updateDueDatedisplay(SelectEvent event) {
		RequestContext.getCurrentInstance().update(":manageTask:dueDateContainer");
	}

	public void registerConfig() {
		if (assignee != null) {
			currentTask.setAssignee(assignee.getId());
		}
		if (owner != null) {
			currentTask.setOwner(owner.getId());
		}
		activitiService.saveTask(currentTask);
		FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.tache.parmetresenregistrersucces"));
	}

	public IwForm getCurrentIwForm() {
		return currentIwForm;
	}

	public void setCurrentIwForm(IwForm currentIwForm) {
		this.currentIwForm = currentIwForm;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

	public String getProcessInstanceName(String procInstanceId) {
		if (procInst != null) {
			return procInst.getName();

		} else {
			return null;
		}
	}

	public void handleUploadedFiles(FileUploadEvent event) {
		InputStream input = null;
		UploadFormWrapper formWrapper = (UploadFormWrapper) event.getComponent().getAttributes().get("formWrapper");
		String destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ currentTask.getProcessDefinitionId().replace(":", "@")
		+ File.separator
		+ currentTask.getProcessInstanceId()
		+ File.separator
		+ currentTask.getId()
		+ File.separator
		+ new Date().getTime();
		String fileName = destDirPath
		+ File.separator
		+ event.getFile().getFileName();
		try {
			input = event.getFile().getInputstream();
			File distDir = new File(destDirPath);
			File uploadedFile = new File(fileName);
			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			if (!uploadedFile.exists()) {
				uploadedFile.createNewFile();
			}
			OutputStream output = new FileOutputStream(uploadedFile);
			try {
				IOUtils.copy(input, output);
				IwFile iwFile = new IwFile();
				iwFile.setIwPath(fileName);
				iwFile.setIwSize(event.getFile().getSize());
				iwFile.setIwMime(event.getFile().getContentType());
				iwFile.setIwUpload(formWrapper.getIwUpload());
				iwFile.setIwName(event.getFile().getFileName());
				formWrapper.getIwUpload().getIwFileList().add(iwFile);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		} catch (IOException ex) {
			LogManager.getLogger(TaskController.class).error(ex);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(TaskController.class).error(ex);
			}
		}
	}

	public void handleEntityUploadedFiles(FileUploadEvent event) {
		InputStream input = null;
		FormWrapper formWrapper = (FormWrapper) event.getComponent().getAttributes().get("formWrapper");
		String columnName = (String) event.getComponent().getAttributes().get("columnName");
		Object object = event.getComponent().getAttributes().get("iwuploadView");
		String calssName = (String) event.getComponent().getAttributes().get("calssName");
		String destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ currentTask.getProcessDefinitionId().replace(":", "@")
		+ File.separator
		+ currentTask.getProcessInstanceId()
		+ File.separator
		+ currentTask.getId()
		+ File.separator
		+ new Date().getTime();
		String fileName = destDirPath
		+ File.separator
		+ event.getFile().getFileName();
		try {
			input = event.getFile().getInputstream();
			File distDir = new File(destDirPath);
			File uploadedFile = new File(fileName);
			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			if (!uploadedFile.exists()) {
				uploadedFile.createNewFile();
			}
			OutputStream output = new FileOutputStream(uploadedFile);
			try {
				IOUtils.copy(input, output);
				IwFile iwFile = new IwFile();
				iwFile.setIwPath(fileName);
				iwFile.setIwSize(event.getFile().getSize());
				iwFile.setIwMime(event.getFile().getContentType());
				iwUpload = (IwUpload) Util.getEntityFieldValue(calssName, object, columnName);
				if (iwUpload == null) {
					iwUpload = new IwUpload();
					iwUpload.setIwFileList(new ArrayList<IwFile>());
				}
				Util.setEntityFieldValue(calssName, object, columnName, iwUpload);
				iwFile.setIwName(event.getFile().getFileName());
				iwUpload.getIwFileList().add(iwFile);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		} catch (IOException ex) {
			LogManager.getLogger(TaskController.class).error(ex);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(TaskController.class).error(ex);
			}
		}
	}

	public List<IwFile> getFormFileList(UploadFormWrapper formWrapper) {
		if (formWrapper == null || formWrapper.getIwUpload() == null) {
			return null;
		}
		return formWrapper.getIwUpload().getIwFileList();
	}

	public List<IwFile> getIwuploadFileList(Object object, UploadFormWrapper formWrapper) {
		if (object == null) {
			return null;
		}
		IwUpload iwUpload = (IwUpload) object;
		if (formWrapper == null || formWrapper.getIwUpload() == null) {
			return null;
		}
		return formWrapper.getIwUpload().getIwFileList();
	}

	public List<IwFile> getIwuploadEntityFileList(Object object, UploadFormWrapper formWrapper, String calssName, String columnName) {
		if (object == null) {
			return null;
		}
		iwUpload = (IwUpload) Util.getEntityFieldValue(calssName, object, columnName);
		if (iwUpload == null) {
			iwUpload = new IwUpload();
			iwUpload.setIwFileList(new ArrayList<IwFile>());
		}
		if (formWrapper == null || formWrapper.getIwUpload() == null) {
			return null;
		}
		return iwUpload.getIwFileList();
	}

	public void removeFile(UploadFormWrapper formWrapper, IwFile iwFile) {
		formWrapper.getIwUpload().getIwFileList().remove(iwFile);
	}

	public void removeEntityFile(IwFile iwFile) {
		iwUpload.getIwFileList().remove(iwFile);
	}

	public Long getProgress() {
		if (currentProgress == null) {
			return null;
		}
		if (currentProgress.getIwProgressRate() == null) {
			return null;
		}
		return currentProgress.getIwProgressRate().longValue();
	}

	public Long getProgressEnd() {
		if (currentProgress == null) {
			return null;
		}
		if (currentProgress.getIwProgressEnd() == null) {
			return null;
		}
		return currentProgress.getIwProgressEnd().longValue();
	}

	public List<HistoricTaskInstance> getArchivedTaskList() {
		return activitiService.getTaskArchiveListForUser(userService.getLoggedInUser().getId(), userService.getUserProcessKeyRights());
	}

	public String claim(String taskId) {
		activitiService.claimTask(taskId, userService.getLoggedInUser().getId());
		return "formulaire.xhtml?task=" + taskId + "&faces-redirect=true";
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public Map<String, Object> getTaskVars() {
		return taskVars;
	}

	public void setTaskVars(Map<String, Object> taskVars) {
		this.taskVars = taskVars;
	}

	public HtmlInputHidden createSliderValueInput(String hiddenInputId) {
		HtmlInputHidden sliderValue;
		Application app = FacesContext.getCurrentInstance().getApplication();
		sliderValue = (HtmlInputHidden) app.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		sliderValue.setId(hiddenInputId);
		return sliderValue;
	}

	public Slider createSlider(String hiddenInputId) {
		Slider slider;
		Application app = FacesContext.getCurrentInstance().getApplication();
		slider = (Slider) app.createComponent(Slider.COMPONENT_TYPE);
		slider.setFor(hiddenInputId);
		return slider;
	}

	public ProcessDefinition getProcDef() {
		return procDef;
	}

	public void setProcDef(ProcessDefinition procDef) {
		this.procDef = procDef;
	}

	public ProcessInstance getProcInst() {
		return procInst;
	}

	public void setProcInst(ProcessInstance procInst) {
		this.procInst = procInst;
	}

	public List<HistoricTaskInstance> getHistoricTaskList() {
		return historicTaskList;
	}

	public void setHistoricTaskList(List<HistoricTaskInstance> historicTaskList) {
		this.historicTaskList = historicTaskList;
	}

	public Map<String, HtmlInputHidden> getSliderValues() {
		return sliderValues;
	}

	public void setSliderValues(Map<String, HtmlInputHidden> sliderValues) {
		this.sliderValues = sliderValues;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void addRow(IwMultiLine selectedMultiLine) {
		if (selectedMultiLine != null) {
			selectedMultiLine.addRow();
		}
	}

	public String getNewRow() {
		return newRow;
	}

	public void setNewRow(String newRow) {
		this.newRow = newRow;
	}

	public FormWrapper getSelectedMultiLine() {
		return selectedMultiLine;
	}

	public void setSelectedMultiLine(FormWrapper selectedMultiLine) {
		this.selectedMultiLine = selectedMultiLine;
	}

	public Integer getColSpan(IwInput iwInput, String colName) {
		int index = iwInput.getOptions2().indexOf(colName);
		return iwInput.getNumbersubcolumns().get(index);
	}

	public String getRowName(IwInput iwInput, String subRow) {
		int index = iwInput.getSubrows().indexOf(subRow);
		int i = 0;
		int rowCount = 0;
		for (Integer rowNbr : iwInput.getNumbersubrows()) {
			rowCount += rowNbr;
			if (index < rowCount) {
				return iwInput.getOptions().get(i);
			}
			i++;
		}
		return "";
	}

	public IwProgress getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(IwProgress currentProgress) {
		this.currentProgress = currentProgress;
	}

	public List<Group> getCurrentUserGroups() {
		return currentUserGroups;
	}

	public void setCurrentUserGroups(List<Group> currentUserGroups) {
		this.currentUserGroups = currentUserGroups;
	}

	public List<String> getCurrentUserGroupsIds() {
		return currentUserGroupsIds;
	}

	public void setCurrentUserGroupsIds(List<String> currentUserGroupsIds) {
		this.currentUserGroupsIds = currentUserGroupsIds;
	}

	public List getMultilineEntity(String id) {
		MultilineEntityFormWrapper frm = (MultilineEntityFormWrapper) getFormById(id);
		if (frm != null) {
			return frm.getEntityList();
		}
		return new ArrayList();
	}

	public FormWrapper getFormById(String formId) {
		Iterator formListIterator = formList.keySet().iterator();
		for (Map.Entry<String, FormWrapper> entry : formList.entrySet()) {
			String key = entry.getKey();
			FormWrapper frm = entry.getValue();
			if (frm.getIwInput().getId().equals(formId)) {
				return frm;
			}
		}
		return null;
	}

	public void addEntity(MultilineEntityFormWrapper frm) {
		List<Object> list;
		list = frm.getEntityList();
		if (list == null) {
			list = new ArrayList<Object>();
		}
		try {
			try {
				list.add(Class.forName(frm.getIwInput().getValue()).newInstance());
			} catch (InstantiationException ex) {

			} catch (IllegalAccessException ex) {

			}
		} catch (ClassNotFoundException ex) {

		}
		frm.setEntityList(list);
	}

	public void removeEntity(MultilineEntityFormWrapper frm, int index) {
		if (frm != null) {
			List list = frm.getEntityList();
			if (list != null && !list.isEmpty()) {
				list.remove(index);
			}
		}
	}

	public Object getIdEntity(Object object, String className) {
		try {
			Class classEntity = Class.forName(className);
			String idName = null;
			for (Field field : classEntity.getDeclaredFields()) {
				Class type = field.getType();
				String name = field.getName();
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					try {
						field.setAccessible(true);
						return field.get(object);
					} catch (IllegalArgumentException ex) {

					} catch (IllegalAccessException ex) {

					}
				}
			}
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(TaskController.class.getName()).error(null, ex);
		}
		return null;
	}

	public Object valueField(Object object, String className, String fieldName) {
		Class classEntity = null;
		try {
			classEntity = Class.forName(className);
			String nameField = Util.convertToAlphaNumeric(fieldName);
			Field currentField = null;
			for (Field field : classEntity.getDeclaredFields()) {
				Class type = field.getType();
				if (field.getName().contains(nameField)) {
					currentField = field;
					break;
				}
			}
			if (currentField != null) {
				currentField.setAccessible(true);
				try {
					return currentField.get(object);
				} catch (IllegalArgumentException ex) {
					LogManager.getLogger(TaskController.class.getName()).error(null, ex);
				} catch (IllegalAccessException ex) {
					LogManager.getLogger(TaskController.class.getName()).error(null, ex);
				}
			}
		} catch (Exception ex) {
			LogManager.getLogger(TaskController.class.getName()).error(null, ex);
		}
		return null;
	}

	public List<IwMultilineEntityField> getFieldEntityList(String entityName, List<String> filterFields) {
		List<IwMultilineEntityField> fields = businessService.getFieldByEntityList(entityName);
		// Filter columns
		if (fields != null && !fields.isEmpty()) {
			if (filterFields != null && !filterFields.isEmpty()) {
				List<IwMultilineEntityField> result = new ArrayList<IwMultilineEntityField>();
				for (IwMultilineEntityField field : fields) {
					if (filterFields.contains(field.getIwMultilineEntityFieldName())) {
						result.add(field);
					}
				}
				return result;
			}
		}
		return fields;
	}

	public String getDescriptionField(String entityName, String fieldName) {
		return businessService.getDescriptionFiledEntity(entityName, fieldName);
	}

	public boolean containsGivenText(List option2, String columnValue, String clazzName) {
		Iterator option2Iterator = option2.iterator();
		while (option2Iterator.hasNext()) {
			String option2Value = option2Iterator.next().toString();

			if (option2Value.toLowerCase().trim().equals(columnValue.toLowerCase().trim())) {
				return true;
			}
		}
		return false;
	}

	public List<String> getColumnList(String className) {
		List list = new ArrayList<String>();
		try {
			Class classEntity;
			classEntity = Class.forName(className);
			for (Field field : classEntity.getDeclaredFields()) {
				Id id = field.getAnnotation(Id.class);
				if (!"serialVersionUID".equals(field.getName()) && field.getAnnotation(Id.class) == null) {
					list.add(field.getName());
				}
			}
		} catch (ClassNotFoundException ex) {

		}
		return list;
	}

	public Boolean isDateInput(String calssName, String fieldName) {
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
			if (type != null && type.getName().equals("java.util.Date")) {
				return true;
			}
		} catch (ClassNotFoundException ex) {

		} catch (SecurityException ex) {

		}
		return false;
	}

	public Boolean isTimeInput(String calssName, String fieldName) {
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
			if (type != null && type.getName().equals("java.sql.Time")) {
				return true;
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, "Class not found", ex);
		} catch (SecurityException ex) {
			Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, "Security violation", ex);
		}
		return false;
	}

	public Boolean isBooleanSelectInput(String calssName, String fieldName) {
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
			if (type != null && type.getName().equals("java.lang.boolean") || type.getName().equals("java.lang.Boolean")) {
				return true;
			}
		} catch (ClassNotFoundException ex) {

		} catch (SecurityException ex) {

		}
		return false;
	}

	public Boolean isIwUploadFile(String calssName, String fieldName) {
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
			if (type != null && type.getName().equals("com.imaginepartners.imagineworkflow.models.IwUpload")) {
				return true;
			}
		} catch (ClassNotFoundException ex) {

		} catch (SecurityException ex) {

		}
		return false;
	}

	public int getAttributeMaxSize(String calssName, String fieldName) {
		try {
			Class cls = Class.forName(calssName);
			for (Field field : cls.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					Size s = field.getAnnotation(Size.class);
					return s.max();
				}
			}

		} catch (ClassNotFoundException ex) {

		} catch (SecurityException ex) {

		}
		return 255;
	}

	public Boolean reuiredField(String calssName, String fieldName) {
		try {
			Class cls = Class.forName(calssName);
			for (Field field : cls.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					if (field.getName().equals(fieldName) && field.getAnnotation(NotNull.class) != null) {
						return true;
					}
				}
			}

		} catch (ClassNotFoundException ex) {

		} catch (SecurityException ex) {

		}
		return false;
	}

	public Map<String, Map<String, Object>> getProcInstVars() {
		return procInstVars;
	}

	public void setProcInstVars(Map<String, Map<String, Object>> procInstVars) {
		this.procInstVars = procInstVars;
	}

	public void registerBean(String beanName, Class<?> beanClass, String[] varNames) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
		builder.setScope("view");
		if (varNames != null && varNames.length > 0 && taskId != null) {
			builder.addPropertyValue("varValues", activitiService.getTaskVariables(taskId, new ArrayList<String>(Arrays.asList(varNames))));
		} else if (varNames != null && varNames.length > 0 && taskHistId != null) {
			Map<String, Object> newTaskVarsHist = new HashMap<String, Object>();
			List<String> varNamesList = new ArrayList<String>(Arrays.asList(varNames));
			for (Entry<String, Object> taskVarsHistItem : taskVarsHist.entrySet()) {
				if (varNamesList.contains(taskVarsHistItem.getKey())) {
					newTaskVarsHist.put(taskVarsHistItem.getKey(), taskVarsHistItem.getValue());
				}
			}
			builder.addPropertyValue("varValues", newTaskVarsHist);
		}
		builder.addPropertyValue("businessService", businessService);
		builder.addPropertyValue("activitiService", activitiService);
		builder.addPropertyValue("userService", userService);
		builder.setInitMethodName("init");
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) ((XmlWebApplicationContext) appContext).getBeanFactory();
		try {
			factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
			templateBeans.add(beanName);
		} catch (BeanDefinitionStoreException bdse) {
			LogManager.getLogger(TaskController.class).error("can't ovveride bean definition or beanDefinition is invalide.", bdse);
		}

	}

	public void registerTemplate(String beanName, String beanClassName, String[] varNames) {
		try {
			String templatePackage = "com.imaginepartners.imagineworkflow.controller.template";
			Class beanClass = Class.forName(templatePackage + "." + beanClassName);
			if (!FormTemplate.class.isAssignableFrom(FormTemplate.class)) {
				throw new ClassCastException("beanClassName: " + templatePackage + "." + beanClassName + " doesn't extend FormTemplate");
			}
			registerBean(beanName, beanClass, varNames);
		} catch (ClassNotFoundException ex) {

		}
	}

	/**
	 * @param beanName           simple bean name.
	 * @param bean               object instance.
	 * @param persisteValuesName names of variables to be persisted.
	 */
	public void registerTemplateByBeanInstance(String beanName, String[] persisteValuesName, Object bean) {
		Class<?> clazz = bean.getClass();
		try {
			Field field = clazz.getSuperclass().getDeclaredField("businessService");
			Field field2 = clazz.getSuperclass().getDeclaredField("activitiService");
			Field field3 = clazz.getSuperclass().getDeclaredField("userService");
			field.setAccessible(true);
			field2.setAccessible(true);
			field3.setAccessible(true);
			field.set(bean, this.businessService);
			field2.set(bean, this.activitiService);
			field3.set(bean, this.userService);
			Field field4 = clazz.getSuperclass().getDeclaredField("varValues");
			field4.setAccessible(true);
			if (persisteValuesName != null && persisteValuesName.length > 0 && taskId != null)
				field4.set(bean, activitiService.getTaskVariables(taskId, Arrays.asList(persisteValuesName)));
			else if (persisteValuesName != null && persisteValuesName.length > 0 && taskHistId != null) {
				Map<String, Object> newTaskVarsHist = new HashMap<String, Object>();
				List<String> varNamesList = new ArrayList<String>(Arrays.asList(persisteValuesName));
				for (Map.Entry<String, Object> taskVarsHistItem : taskVarsHist.entrySet()) {
					if (varNamesList.contains(taskVarsHistItem.getKey())) {
						newTaskVarsHist.put(taskVarsHistItem.getKey(), taskVarsHistItem.getValue());
					}
				}
				field4.set(bean, newTaskVarsHist);
			}

		} catch (NoSuchFieldException ex) {
			LogManager.getLogger(TaskController.class).error("No such field exception", ex);
		} catch (SecurityException ex) {
			LogManager.getLogger(TaskController.class).error("Security exception", ex);
		} catch (IllegalArgumentException ex) {
			LogManager.getLogger(TaskController.class).error("IllegalArgumentException", ex);
		} catch (IllegalAccessException ex) {
			LogManager.getLogger(TaskController.class).error("IllegalAccessException", ex);
		}
		this.getTemplateBeans().add(beanName);
	}

	public void registerTemplate(String beanName, String[] varNames) {
		registerBean(beanName, DefaultFormTemplate.class, varNames);
	}

	public Object getVariableProcess(String processInstanceId, String iwInputId) {
		return activitiService.getVariableProcess(processInstanceId, iwInputId);
	}

	public List<IwListOptions> getListOptions(String listId) {
		IwList iwList = businessService.getIwListById(Long.valueOf(listId));
		if (iwList != null) {
			return iwList.getIwListOptionsList();
		} else {
			return null;
		}
	}

	public void updateCondtionSelectMultiSelect(AjaxBehaviorEvent event) {
		SelectBooleanCheckbox checkBox = (SelectBooleanCheckbox) event.getComponent();
	}

	public Map<String, Object> getGlobalFormListForCol() {
		return globalFormListForCol;
	}

	public void setGlobalFormListForCol(Map<String, Object> globalFormListForCol) {
		this.globalFormListForCol = globalFormListForCol;
	}

	public boolean filterColumns(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}
		if ((value instanceof Boolean)) {
			String val = ((Boolean) value == false) ? "non" : "oui";
			return val.contains(filterText.toLowerCase());
		}
		if ((value instanceof Date)) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String val = formatter.format(value);
			return val.contains(filterText);
		}
		if ((value instanceof String)) {
			String val = (String) value;
			return val.contains(filterText);
		}
		return false;
	}

	public String getTaskHistId() {
		return taskHistId;
	}

	public void setTaskHistId(String taskHistId) {
		this.taskHistId = taskHistId;
	}

	public HistoricTaskInstance getTaskHist() {
		return taskHist;
	}

	public void setTaskHist(HistoricTaskInstance taskHist) {
		this.taskHist = taskHist;
	}

	public List<HistoricTaskInstance> getProcInstHist() {
		return procInstHist;
	}

	public void setProcInstHist(List<HistoricTaskInstance> procInstHist) {
		this.procInstHist = procInstHist;
	}

	public Map<String, FormWrapper> getFormListHist() {
		return formListHist;
	}

	public void setFormListHist(Map<String, FormWrapper> formListHist) {
		this.formListHist = formListHist;
	}

	public Map<String, Object> getTaskVarsHist() {
		return taskVarsHist;
	}

	public void setTaskVarsHist(Map<String, Object> taskVarsHist) {
		this.taskVarsHist = taskVarsHist;
	}

	public String getSelectedFormId() {
		return selectedFormId;
	}

	public void setSelectedFormId(String selectedFormId) {
		this.selectedFormId = selectedFormId;
	}

	public void setIwUploadSelected(List<IwFile> listIwfile) {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", true);
		options.put("Height", 400);
		options.put("Width", 1000);
		options.put("contentHeight", 400);
		options.put("contentWidth", 1000);
		RequestContext.getCurrentInstance().openDialog("downloadFile", options, null);
	}

	public String getTaskAutonomeId() {
		return taskAutonomeId;
	}

	public void setTaskAutonomeId(String taskAutonomeId) {
		this.taskAutonomeId = taskAutonomeId;
	}

	public Task getStandaloneTask() {
		return standaloneTask;
	}

	public void setStandaloneTask(Task standaloneTask) {
		this.standaloneTask = standaloneTask;
	}

	/**
	 * userHasWriteRights
	 *
	 * @param colIndex               the index of the list element inside
	 *                               entityUserWriteRights or entityGroupWriteRights, containing the write
	 *                               rights for the relative input element in the front.
	 * @param userID                 the ID of the current logged-in user.
	 * @param entityUserWriteRights  the list of the write rights by input
	 *                               element (a list of lists, every list contains the rights for an input
	 *                               element).
	 * @param userGroups             the list of groups the current user belongs to.
	 * @param entityGroupWriteRights the list of the write rights by input
	 *                               element (a list of lists, every list contains the rights for an input
	 *                               element).
	 * @return Boolean
	 */
	public Boolean userHasWriteRights(Integer colIndex, String userID, List<List> entityUserWriteRights,
									  List<String> userGroups, List<List> entityGroupWriteRights) throws NullPointerException {
		List<String> userList = entityUserWriteRights.get(colIndex);
		List<String> groupList = entityGroupWriteRights.get(colIndex);
		/*
		 * Verify user rights list
		 * If the list is not empty
		 */
		if (!userList.isEmpty()) {
			return !userList.contains(userID);
		} else if (userList.isEmpty()) {
			if (!groupList.isEmpty()) {
				return ListUtils.intersection(userGroups, groupList).isEmpty();
			} else if (groupList.isEmpty()) {
				return false;
			}
		}
		return false;
	}

	public String getTaskTitel() {
		return (processInstanceName != null) ? (getTask().getName() + " - " + processInstanceName) : getTask().getName();
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public void setIdEntity(Object obj, String className, Object value) {
		try {
			Class cls = Class.forName(className);
			for (Field field : cls.getDeclaredFields()) {
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					field.setAccessible(true);
					field.set(obj, value);
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Long getLongValue(Object obj) {
		try {
			return new Long(obj.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void addSubEntity(SelectEvent event) {

	}

	/**
	 * @param event
	 * @throws IOException
	 * @throws Docx4JException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void importXlsx(FileUploadEvent event) throws IOException, Docx4JException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		MultilineEntityFormWrapper frm = (MultilineEntityFormWrapper) getFormById((String) event.getComponent().getAttributes().get("formId"));
		UploadedFile excelFile = event.getFile();
		List<Object> list = new XlsxReader(excelFile.getInputstream()).saveXlxs(frm.getIwInput().getValue(), frm.getIwInput().getOptions2(), businessService);
		frm.setEntityList(list);
	}

	public Map<String, Map<String, List<Task>>> getTasksMap() {
		return tasksMap;
	}

	public void setTasksMap(Map<String, Map<String, List<Task>>> tasksMap) {
		this.tasksMap = tasksMap;
	}

	public Map<String, List<Task>> getTaskApp() {
		return taskApp;
	}

	public void setTaskApp(Map<String, List<Task>> taskApp) {
		this.taskApp = taskApp;
	}

	public void selectProcDef(String procDefId) {
		selectProcessId = procDefId;
	}

	public String getSelectProcessId() {
		return selectProcessId;
	}

	public void setSelectProcessId(String selectProcessId) {
		this.selectProcessId = selectProcessId;
	}


	/**
	 * Register template's name.
	 *
	 * @return list of templates associated with th current form.
	 */
	public List<String> getTemplateBeans() {
		return templateBeans;
	}

	public void setTemplateBeans(List<String> templateBeans) {
		this.templateBeans = templateBeans;
	}

	public Object convertForDocx(Object obj) {
		Object result = obj;
		if (!(obj instanceof DocxData) && !(obj instanceof Date) && !(obj instanceof DateTime) && !(obj instanceof String) && obj != null)
			result = obj.toString();
		return result;
	}



	//  calculer  les  nombres de jour de congés restés
	public int nbrJoursRestentConge()
	{
		List<RhConge> list = businessService.getCongesByUser(getIdColloborateur());
		int nbrJoursCongesUtilise  =  0 ;
		for (RhConge conge:
		list ) {
			nbrJoursCongesUtilise += conge.getNbrJour() ;

		}
		return 30-nbrJoursCongesUtilise ;
	}
	public int getIdColloborateur()
	{     Map<String,Object> varsMap=activitiService.getTaskVariables(taskId,null);

		BigDecimal matricule = (BigDecimal) varsMap.get("form_1512726219178");
		if(matricule == null)
			return 1;
		int m = matricule.intValueExact() ;
		RhCollaborateur collaborateur  = businessService.getColloborateurByMatricule(m);
		return collaborateur.getId() ;
	}

	// calculer  le nombre de jours de congés  en  eliminant le vendredi  s'il existe
	public int getNbrJourConge ()
	{
		Map<String,Object> varsMap=activitiService.getTaskVariables(taskId,null);


		Date dateDeb , dateFin ;
		int nbrJourConge = 0 ;

		dateDeb = (Date) varsMap.get("dateDebTemplateConge");
		dateFin= (Date) varsMap.get("dateFinTemplateConge");

		Calendar cStart = Calendar.getInstance(); cStart.setTime(dateDeb);
		Calendar cEnd = Calendar.getInstance(); cEnd.setTime(dateFin);
		if(dateDeb.compareTo(dateFin)<0)

			while (cStart.before(cEnd)) {

				if ( cStart.get(Calendar.DAY_OF_WEEK)!=6)
					nbrJourConge ++ ;
				cStart.add(Calendar.DAY_OF_MONTH, 1);

			}
		return nbrJourConge ;
	}
	// save Current Valid conge
	private boolean saveCurrentValidConge(Map<String, FormWrapper> f) {
		if ("3".equals(currentTask.getFormKey())) {
			RhCollaborateur selectedCollab=businessService.getRhCollaborateurByActIdUser((String)taskVars.get("Initiator"));
			String DateDebFormId = "dateDebTemplateConge";
			String DateFinFormId = "dateFinTemplateConge";
			String slectValidFormID = "valider";

			if (selectedCollab == null) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("rh.matriculeObligatoire"));
				return false;
			}
			Object o=f.get(slectValidFormID);
			if (o != null && !valideDates(taskVars.get(DateDebFormId), taskVars.get(DateFinFormId))) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.agneda.datefinsuperieurdatedebut"));
				return false;
			}
			int nbrJoursValidCong=nbrjoursValidConj(taskVars.get(DateDebFormId),taskVars.get(DateFinFormId));

			if (f.get(slectValidFormID) != null && "oui".equals(f.get(slectValidFormID).getValue())) {
				RhConge conge = new RhConge();
				conge.setDateDebut((Date) taskVars.get(DateDebFormId));
				conge.setDateFin((Date) taskVars.get(DateFinFormId));
				conge.setIdPosteOccupe(selectedCollab.getIdPosteOccupe());
				conge.setNbrJour(nbrJoursValidCong);
				String query = "SELECT R.ID,R.TYPECONGE FROM RH_TYPECONGE R WHERE R.TYPECONGE = 'Annuel'";
				List listRhTypeConge = businessService.getObjectBySQL(query, RhTypeConge.class);
				if (listRhTypeConge != null && listRhTypeConge.size() > 0)
					conge.setIdTypeConge((RhTypeConge) listRhTypeConge.get(0));
				conge.setCollaborateur(selectedCollab);
				businessService.saveEntite(conge);
			}
		}
		return true;
	}

	// test if date1 is greater then date2
	private boolean valideDates(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null)
			return false;
		return ((Date) obj1).getTime() <= ((Date) obj2).getTime();
	}

	// get nombres jours except friday
	private int nbrjoursValidConj(Object datedeb,Object DateFin){
		if(datedeb == null || DateFin == null)
			return 0;
		Date date1= (Date)datedeb;
		Date date2=(Date)DateFin;
		Calendar cal1=Calendar.getInstance();
		Calendar cal2=Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int allJours = 0;

		while(!cal1.after(cal2)){
			if(cal1.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				cal1.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			allJours++;
			cal1.add(Calendar.DAY_OF_YEAR, 1);
		}


		return allJours;

	}

}
