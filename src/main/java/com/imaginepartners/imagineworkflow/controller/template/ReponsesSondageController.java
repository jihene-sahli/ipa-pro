package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.rh.AldSondage;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ReponsesSondageController extends FormTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<AldSondage> listAllSondages;


	@Override
	public void init() {


	//	taskId = StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
	//	currentTask = activitiService.getTask(taskId);

		if (varValues.get("allSondages") != null) {
			listAllSondages = (ArrayList<AldSondage>) varValues.get("allSondages");
		} else {
			listAllSondages = new ArrayList<AldSondage>();
		}

	}


	@Override
	public void putVars() {

	}

	/*@Override
	public Map<String, Object> getVarValues() {

		return varValues;
	}*/


	@Override
	public Map<String, Object> getVarValuesReport() {

		return null;
	}

	public ReponsesSondageController() {

	}

	public List<AldSondage> getListAllSondages() {
		return listAllSondages;
	}

	public void setListAllSondages(List<AldSondage> listAllSondages) {
		this.listAllSondages = listAllSondages;
	}
}
