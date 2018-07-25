package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 09/01/2017.
 */
@Controller("procController")
@Scope("view")
public class ProcController implements Serializable{

	@Autowired
	private ActivitiService activitiService;
	private ProcessDefinition procDef;
	private List <ProcessDefinition> procDefList=new ArrayList<ProcessDefinition>();
    private List<Task> tasksByDefList = new ArrayList<Task>();
    private String procDefId;
    private Task selectedTask;
	private String userOrGroup;
	private Map <String,Object> variablesTaskList;
	private Map <String,Object> filtredVariablesTaskList;
	private String maVariable;
	private Object newValue;
	private List<Group> newGroupsList;
	private User newAssignee;

	public ProcController() {

	}

	@PostConstruct
	public void init() {
		for (ProcessDefinition processDefinition :activitiService.getProcDefList()) {
			if (!activitiService.getTasksByProcDef(processDefinition).isEmpty()){
				procDefList.add(processDefinition);
			}
		}
	}

	public void getSelectedProcDef() {
		procDef =activitiService.getProcessDefinitionById(procDefId);
		tasksByDefList = activitiService.getTasksByProcDef(procDef);

	}

	public void onRowSelect(SelectEvent event) {

		variablesTaskList= new HashMap<String, Object>();
		filtredVariablesTaskList= new HashMap<String, Object>();
		variablesTaskList=activitiService.getVariablesTasks(selectedTask.getId());

		if (!variablesTaskList.isEmpty()){

			for(Map.Entry<String, Object> entry : variablesTaskList.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value == null){
					filtredVariablesTaskList.put(key,value);
				}
			}
		}
		System.out.println("******************** vals: "+variablesTaskList.size());
		System.out.println("******************** filtred: "+filtredVariablesTaskList.size());


	}


	public void doFrocerTache() {

		if (filtredVariablesTaskList.isEmpty()){

			activitiService.completeTask(selectedTask.getId(),variablesTaskList);


		}else {
			filtredVariablesTaskList.put(maVariable,newValue);
			activitiService.completeTask(selectedTask.getId(),filtredVariablesTaskList);

		}
		FacesUtil.setAjaxInfoMessage(selectedTask.getName()+" a été forcer avec succès");

		tasksByDefList = activitiService.getTasksByProcDef(procDef);
	}

	public void doReaffecterTache() {

		if (userOrGroup.equals("Utilisateur") && selectedTask !=null){
			activitiService.changeTaskAssignee(selectedTask.getId(),newAssignee.getId());
			FacesUtil.setAjaxInfoMessage(selectedTask.getName()+" est affecté à  "+newAssignee.getId());


		} else if(userOrGroup.equals("Groupe") && selectedTask !=null){
			if (!selectedTask.getAssignee().isEmpty()){
              activitiService.unclaimTask(selectedTask.getId());
			}
			for (Group g : newGroupsList) {
				activitiService.addGroupCandidateTask(selectedTask.getId(),g.getId());
				FacesUtil.setAjaxInfoMessage(selectedTask.getName()+" est affecté au groupe "+g.getName());
			}

		}else {
			FacesUtil.setAjaxErrorMessage("Veuillez sélectionner une tâche");
		}

		tasksByDefList = activitiService.getTasksByProcDef(procDef);
		}
	public void doRenvoyerTache() {


	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public ProcessDefinition getProcDef() {
		return procDef;
	}

	public void setProcDef(ProcessDefinition procDef) {
		this.procDef = procDef;
	}

	public List<ProcessDefinition> getProcDefList() {
		return procDefList;
	}

	public void setProcDefList(List<ProcessDefinition> procDefList) {
		this.procDefList = procDefList;
	}

	public List<Task> getTasksByDefList() {
		return tasksByDefList;
	}

	public void setTasksByDefList(List<Task> tasksByDefList) {
		this.tasksByDefList = tasksByDefList;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}

	public String getUserOrGroup() {
		return userOrGroup;
	}

	public void setUserOrGroup(String userOrGroup) {
		this.userOrGroup = userOrGroup;
	}

	public Map<String, Object> getVariablesTaskList() {
		return variablesTaskList;
	}

	public void setVariablesTaskList(Map<String, Object> variablesTaskList) {
		this.variablesTaskList = variablesTaskList;
	}

	public Map<String, Object> getFiltredVariablesTaskList() {
		return filtredVariablesTaskList;
	}

	public void setFiltredVariablesTaskList(Map<String, Object> filtredVariablesTaskList) {
		this.filtredVariablesTaskList = filtredVariablesTaskList;
	}

	public String getMaVariable() {
		return maVariable;
	}

	public void setMaVariable(String maVariable) {
		this.maVariable = maVariable;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public List<Group> getNewGroupsList() {
		return newGroupsList;
	}

	public void setNewGroupsList(List<Group> newGroupsList) {
		this.newGroupsList = newGroupsList;
	}

	public User getNewAssignee() {
		return newAssignee;
	}

	public void setNewAssignee(User newAssignee) {
		this.newAssignee = newAssignee;
	}
}
