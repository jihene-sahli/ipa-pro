package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.IwVariableProcess;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.FormContants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class InstanceController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instanceId;

	private ProcessInstance currentInstance;

	private String instanceHistId;

	private String instanceTaskHisId;

	private HistoricProcessInstance currentInstanceHist;

	private ProcessDefinition currentProcDefHist;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private UserService userService;

	@Autowired
	private BusinessService businessService;

	private List<Map<String, Object>> globalFormList;

	private List<Map<String, Object>> globalHistFormList;

	private List<IwInput> uploadInputs;

	private HistoricTaskInstance histTask;

	private TaskController Task;

	private List<IwForm> formList;

	private Set<String> formKey;

	private List<IwInput> listInputs;

	public InstanceController() {

	}

	@PostConstruct
	public void init() {
		formKey = new HashSet<String>();
		listInputs = new ArrayList<IwInput>();
		instanceId = FacesUtil.getUrlParam("instance");
		instanceHistId = FacesUtil.getUrlParam("instanceHist");
		instanceTaskHisId = FacesUtil.getUrlParam("tache");
		if (StringUtils.isNotBlank(instanceId)) {
			currentInstance = activitiService.getProcessInstanceById(instanceId);
			initGlobalForm();
			initUploadedDocuments(currentInstance.getProcessDefinitionId());
		}
		if (StringUtils.isNotBlank(instanceHistId)) {
			currentInstanceHist = activitiService.getHistoricProcessInstanceByIdIncludeVars(instanceHistId);
			currentProcDefHist = activitiService.getProcessDefinitionById(currentInstanceHist.getProcessDefinitionId());
			initGlobalHistForm();
			initUploadedDocuments(currentInstanceHist.getProcessDefinitionId());
		}
		if(StringUtils.isNoneBlank(instanceTaskHisId)) {
			histTask = activitiService.getTaskHistById(instanceTaskHisId);
			formKey.add(histTask.getFormKey());
			formList = businessService.getIwFormListByFormKey(formKey);
			Iterator iterator = formList.iterator();
			while (iterator.hasNext()) {
				IwForm form = (IwForm) iterator.next();
				listInputs = businessService.getIwInputByFormId(form);
			}
		}
	}

	public List<ProcessInstance> getProcInstList() {
		List<String> processKeis = userService.getUserProcessKeyRights();
		return activitiService.getProcInstListForUser(userService.getLoggedInUser().getId(), processKeis);
	}

	public List<HistoricProcessInstance> getProcInstHistList() {
		List<String> processKeis = userService.getUserProcessKeyRights();
		return activitiService.getProcInstHistListForUser(userService.getLoggedInUser().getId(), processKeis);
	}

	public void suspend() {
		String instanceId = FacesUtil.getUrlParam("instance");
		if (StringUtils.isNotBlank(instanceId)) {
			activitiService.suspendInstance(instanceId);
		}
	}

	public void delete() {
		String instanceId = FacesUtil.getUrlParam("instance");
		if (StringUtils.isNotBlank(instanceId)) {
			activitiService.deleteInstance(instanceId);
		}
	}

	public void activate() {
		String instanceId = FacesUtil.getUrlParam("instance");
		if (StringUtils.isNotBlank(instanceId)) {
			activitiService.activateInstance(instanceId);
		}
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public ProcessInstance getCurrentInstance() {
		return currentInstance;
	}

	public void setCurrentInstance(ProcessInstance currentInstance) {
		this.currentInstance = currentInstance;
	}

	public List<HistoricTaskInstance> getProcessInstanceHistory() {
		if (currentInstance != null) {
			return activitiService.getProcessInstanceHistory(instanceId);
		} else if (currentInstanceHist != null) {
			return activitiService.getProcessInstanceHistory(instanceHistId);
		}
		return null;
	}

	public String getDuration(Long milis, Date dateAjout) {
		Duration duration;
		if(milis == null)
			duration = Duration.millis(0L);
		else
		 duration = Duration.millis(milis);
		DateTime dt = new DateTime(dateAjout.getTime());
		Period period = duration.toPeriodFrom(dt, PeriodType.yearMonthDayTime());
		PeriodFormatter formatter = new PeriodFormatterBuilder()
		.appendYears()
		.appendSuffix(" year", " years")
		.appendSeparator(", ")
		.appendMonths()
		.appendSuffix(" month ", " months")
		.appendSeparator(", ")
		.appendDays()
		.appendSuffix(" day", " days")
		.appendSeparator(", ")
		.appendHours()
		.appendSuffix(" hour", " hours")
		.appendSeparator(", ")
		.appendMinutes()
		.appendSuffix(" minute ", " minutes")
		.appendSeparator(", ")
		.appendSeconds()
		.appendSuffix(" second ", " seconds")
		.printZeroNever()
		.toFormatter();
		return formatter.print(period);
	}

	public String getInstanceHistId() {
		return instanceHistId;
	}

	public void setInstanceHistId(String instanceHistId) {
		this.instanceHistId = instanceHistId;
	}

	public HistoricProcessInstance getCurrentInstanceHist() {
		return currentInstanceHist;
	}

	public void setCurrentInstanceHist(HistoricProcessInstance currentInstanceHist) {
		this.currentInstanceHist = currentInstanceHist;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Map<String, Object>> getGlobalFormList() {
		return globalFormList;
	}

	public void setGlobalFormList(List<Map<String, Object>> globalFormList) {
		this.globalFormList = globalFormList;
	}

	public void initGlobalForm() {
		String processKey = currentInstance.getProcessDefinitionKey();
		//List<IwVariableProcess> variableList = businessService.getVariableProcess(processKey, Boolean.TRUE, null);
		List<IwVariableProcess> variableList = businessService.getVariableProcess(processKey,null, Boolean.TRUE);

		globalFormList = new ArrayList<Map<String, Object>>();
		for (IwVariableProcess var : variableList) {
			Map<String, Object> val = new HashMap<String, Object>();
			val.put("var", var);
			val.put("val", activitiService.getVariableProcess(instanceId, var.getIwInput().getId()));
			globalFormList.add(val);
		}
	}

	public void initGlobalHistForm() {
		List<IwVariableProcess> variableList = businessService.getVariableProcess(currentProcDefHist.getKey(), Boolean.TRUE, null);
		LogManager.getLogger(InstanceController.class).debug("variableList : " + variableList);
		Map<String, Object> varsMap = currentInstanceHist.getProcessVariables();
		LogManager.getLogger(InstanceController.class).debug("varsMap : " + varsMap);
		globalHistFormList = new ArrayList<Map<String, Object>>();
		for (IwVariableProcess var : variableList) {
			Map<String, Object> val = new HashMap<String, Object>();
			val.put("var", var);
			val.put("val", varsMap.get(var.getIwInput().getId()));
			globalHistFormList.add(val);
		}
	}

	public List<IwInput> getUploadInputs() {
		return uploadInputs;
	}

	public void setUploadInputs(List<IwInput> uploadInputs) {
		this.uploadInputs = uploadInputs;
	}

	public void initUploadedDocuments(String processDefId) {
		Set<String> formkeys = activitiService.getProcDefFormKeyList(processDefId);
		List<IwForm> formList = businessService.getIwFormListByFormKey(formkeys);
		uploadInputs = new ArrayList<IwInput>();
		for (IwForm form : formList) {
			for (IwInput input : form.getIwInputList()) {
				if (FormContants.FILE_INPUT_FORM_TYPE_NAME.equals(input.getComponent())) {
					uploadInputs.add(input);
				}
			}
		}
	}

	public List<IwFile> getFormFileList(IwInput iwInput) {
		if (iwInput == null) {
			return null;
		}
		Long uploadId;
		if (currentInstance != null) {
			uploadId = (Long) activitiService.getVariableProcess(instanceId, iwInput.getId());
		} else {
			uploadId = (Long) currentInstanceHist.getProcessVariables().get(iwInput.getId());
		}
		if (uploadId != null) {
			IwUpload iwUpload = businessService.getIwUpload(uploadId);
			if(iwUpload != null)
			return iwUpload.getIwFileList();
		}
		return null;
	}

	public List<Map<String, Object>> getGlobalHistFormList() {
		return globalHistFormList;
	}

	public void setGlobalHistFormList(List<Map<String, Object>> globalHistFormList) {
		this.globalHistFormList = globalHistFormList;
	}

	public HistoricTaskInstance getHistTask() {
		return histTask;
	}

	public void setHistTask(HistoricTaskInstance histTask) {
		this.histTask = histTask;
	}

	public TaskController getTask() {
		return Task;
	}

	public void setTask(TaskController Task) {
		this.Task = Task;
	}

	public List<IwForm> getFormList() {
		return formList;
	}

	public void setFormList(List<IwForm> formList) {
		this.formList = formList;
	}

	public List<IwInput> getListInputs() {
		return listInputs;
	}

	public void setListInputs(List<IwInput> listInputs) {
		this.listInputs = listInputs;
	}
}
