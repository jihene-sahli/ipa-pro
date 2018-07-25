package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwApplication;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwStandAloneTask;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class StandaloneTaskController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Task task;

	private User assignee;

	private User owner;

	private List<Task> taskList;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserService userService;

	private IwStandAloneTask iwStandAloneTask;

	private List<IwStandAloneTask> standAloneTaskList;

	private List<IwForm> formList;

	private List<IwApplication> applicationList;

	private String functionId;

	private Boolean archive;

	private List<HistoricTaskInstance> taskHistList;

	private Map<String,Map<Long,List<Task>>> tasksMap;

	private Map<String,List<Task>> taskApp;

	private Long functionIdSelected;

	@Autowired
	NavigationController navigationController;

	@PostConstruct
	public void init() {
		task = activitiService.newStandaloneTask();
		initTasksMap();
		functionId = FacesUtil.getUrlParam("fonctionId");
		if (StringUtils.isNoneBlank(FacesUtil.getUrlParam("archive"))) {
			archive = Boolean.valueOf(FacesUtil.getUrlParam("archive"));
			if (archive) {
				taskHistList = activitiService.getStandaloneTaskArchiveListForUser(userService.getLoggedInUser().getId());
			}
		}
		if (functionId != null) {
			if (StringUtils.isNotBlank(functionId)) {
				iwStandAloneTask = businessService.getStandAloneTask(Long.valueOf(functionId));
			} else {
				iwStandAloneTask = new IwStandAloneTask();
			}
			formList = businessService.getIwFormList();
			applicationList = businessService.getApplicaitonList();
		}
	}

	public  void initTasksMap(){
		tasksMap = new HashMap<String, Map<Long, List<Task>>>();
		taskApp = new HashMap<String,List<Task>>();
		Map<Long, List<Task>>  taskByfunctionMap;
		List<IwApplication> appList = navigationController.getUserApplicationList();
		List<IwStandAloneTask>  functionByAppList;
		List<Task> tasks;
		for (IwApplication app :appList){
			functionByAppList = businessService.getStandAloneTaskListByIwRight(app.getIwKey(), userService.getUserRights());
			taskByfunctionMap = new HashMap<Long, List<Task>>();
			if(functionByAppList != null && !functionByAppList.isEmpty()){
				for(IwStandAloneTask function :functionByAppList){
					taskByfunctionMap.put(function.getIwStandAloneTaskId(),getTaskListByFunctions(Arrays.asList(function)));
				}
				tasksMap.put(app.getIwKey(),taskByfunctionMap);
				taskApp.put(app.getIwKey(),getTaskListByFunctions(functionByAppList));
			}
		}
		if(navigationController.getIndexActiveAppStd() > tasksMap.size()) {
			navigationController.setIndexActiveAppStd(0);
			navigationController.setIndexActiveFunction(0);
		}
		if(navigationController.getIndexActiveAppStd()>0) {
			Map mapSelected = tasksMap.get((String) tasksMap.keySet().toArray()[navigationController.getIndexActiveAppStd() - 1]);
			if (navigationController.getIndexActiveFunction() > mapSelected.size()) {
				navigationController.setIndexActiveFunction(0);
			}
		}
	}

	public List<Task> getTaskListByFunctions(List<IwStandAloneTask> funcs){
		List<Task> tasks = new ArrayList<Task>();
		if(funcs!= null && !funcs.isEmpty()){
			List<Task> task;
			for(IwStandAloneTask funcId :funcs){
				task = activitiService.getStandaloneTasksForUserByCategory(userService.getLoggedInUser().getId(), funcId.getIwStandAloneTaskId().toString());
				if(task != null && !task.isEmpty())
					tasks.addAll(task);
			}
		}
		return tasks;
	}

	public String save() {
		businessService.save(iwStandAloneTask);
		return "liste?&faces-redirect=true";
	}

	public void delete(IwStandAloneTask standAloneTask) {
		businessService.remove(standAloneTask);
	}

	public String saveTask() {
		task.setName(iwStandAloneTask.getIwName());
		task.setDescription(iwStandAloneTask.getIwDescription());
		task.setCategory(String.valueOf(iwStandAloneTask.getIwStandAloneTaskId()));
		task.setAssignee(userService.getLoggedInUser().getId());
		task.setOwner(userService.getLoggedInUser().getId());
		task.setFormKey(String.valueOf(iwStandAloneTask.getIwFormId()));
		activitiService.saveStandaloneTask(task);
		return "/pages/tache/formulaire?&faces-redirect=true&taskAutonome=" + task.getId();
	}
	public String saveTask(Long functionId) {
		IwStandAloneTask iwStandAloneTask = businessService.getStandAloneTask(functionId);
		task.setName(iwStandAloneTask.getIwName());
		task.setDescription(iwStandAloneTask.getIwDescription());
		task.setCategory(String.valueOf(iwStandAloneTask.getIwStandAloneTaskId()));
		task.setAssignee(userService.getLoggedInUser().getId());
		task.setOwner(userService.getLoggedInUser().getId());
		task.setFormKey(String.valueOf(iwStandAloneTask.getIwFormId()));
		activitiService.saveStandaloneTask(task);
		return "/pages/tache/formulaire?&faces-redirect=true&taskAutonome=" + task.getId();
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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

	public List<IwForm> getFormList() {
		return formList;
	}

	public void setFormList(List<IwForm> formList) {
		this.formList = formList;
	}

	public List<Task> getTaskList() {
		if (taskList == null) {
			taskList = activitiService.getStandaloneTasksForUser(userService.getLoggedInUser().getId());
		}
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public List<IwApplication> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<IwApplication> applicationList) {
		this.applicationList = applicationList;
	}

	public IwStandAloneTask getIwStandAloneTask() {
		return iwStandAloneTask;
	}

	public void setIwStandAloneTask(IwStandAloneTask iwStandAloneTask) {
		this.iwStandAloneTask = iwStandAloneTask;
	}

	public List<IwStandAloneTask> getStandAloneTaskList() {
		standAloneTaskList = businessService.getStandAloneTaskList();
		return standAloneTaskList;
	}

	public void setStandAloneTaskList(List<IwStandAloneTask> standAloneTaskList) {
		this.standAloneTaskList = standAloneTaskList;
	}

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public List<HistoricTaskInstance> getTaskHistList() {
		return taskHistList;
	}

	public void setTaskHistList(List<HistoricTaskInstance> taskHistList) {
		this.taskHistList = taskHistList;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public Map<String, List<Task>> getTaskApp() {
		return taskApp;
	}

	public void setTaskApp(Map<String, List<Task>> taskApp) {
		this.taskApp = taskApp;
	}

	public Map<String, Map<Long, List<Task>>> getTasksMap() {
		return tasksMap;
	}

	public void setTasksMap(Map<String, Map<Long, List<Task>>> tasksMap) {
		this.tasksMap = tasksMap;
	}

	public Long getFunctionIdSelected() {
		return functionIdSelected;
	}

	public void setFunctionIdSelected(Long functionIdSelected) {
		this.functionIdSelected = functionIdSelected;
	}

	public void selectFunctionId(Long funcId){
		functionIdSelected = funcId;
		iwStandAloneTask = businessService.getStandAloneTask(functionIdSelected);
	}
}
