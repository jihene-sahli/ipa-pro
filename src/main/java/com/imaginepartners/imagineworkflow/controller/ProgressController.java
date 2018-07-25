package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwPhase;
import com.imaginepartners.imagineworkflow.models.IwProgress;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class ProgressController {

	private List<ProcessDefinition> processDefList;

	private String currentProcessDef;

	private Map<String, String> taskModelList;

	private List<IwProgress> taskProgsList;

	private List<IwPhase> phaseList;

	private IwPhase currentPhase;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	BusinessService businessService;

	public List<ProcessDefinition> getProcessDefList() {
		return processDefList;
	}

	public void setProcessDefList(List<ProcessDefinition> processDefList) {
		this.processDefList = processDefList;
	}

	public String getCurrentProcessDef() {
		return currentProcessDef;
	}

	public void setCurrentProcessDef(String currentProcessDef) {
		this.currentProcessDef = currentProcessDef;
	}

	public Map<String, String> getTaskModelList() {
		return taskModelList;
	}

	public void setTaskModelList(Map<String, String> taskModelList) {
		this.taskModelList = taskModelList;
	}

	public List<IwProgress> getTaskProgsList() {
		return taskProgsList;
	}

	public void setTaskProgsList(List<IwProgress> taskProgsList) {
		this.taskProgsList = taskProgsList;
	}

	public List<IwPhase> getPhaseList() {
		phaseList = businessService.getPhaseList();
		return phaseList;
	}

	public void setPhaseList(List<IwPhase> phaseList) {
		this.phaseList = phaseList;
	}

	public IwPhase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(IwPhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	@PostConstruct
	public void init() {
		// Get all process definition
		processDefList = activitiService.getLastProcDefList();
	}

	public void loadProgressTasks(AjaxBehaviorEvent event) {
		ProcessDefinition processDef = getProcessDef(currentProcessDef);
		if (processDef != null) {
			taskModelList = activitiService.getTaskModelList(processDef.getId());
			if (taskProgsList == null) {
				taskProgsList = new ArrayList<IwProgress>();
			} else {
				taskProgsList.clear();
			}
			for (String taskId : taskModelList.keySet()) {
				IwProgress progress = businessService.getIwProgressProcessTask(processDef.getKey(), taskId);
				if (progress == null) {
					progress = new IwProgress();
					progress.setIwProcKey(processDef.getKey());
					progress.setIwTask(taskId);
					progress.setIwProgressRate(BigDecimal.ZERO);
					progress.setIwProgressEnd(BigDecimal.valueOf(100));
				}
				taskProgsList.add(progress);
			}
		}
	}

	private ProcessDefinition getProcessDef(String id) {
		for (ProcessDefinition p : processDefList) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public void saveProgression() {
		for (IwProgress p : taskProgsList) {
			businessService.saveIwProgress(p);
		}
	}

	public void initPhase(ActionEvent actionEvent) {
		currentPhase = new IwPhase();
	}

	public void removePhase(IwPhase phase) {
		businessService.removePhase(phase);
	}

	public void saveCurrentPhase(ActionEvent actionEvent) {
		businessService.savePhase(currentPhase);
	}
}
