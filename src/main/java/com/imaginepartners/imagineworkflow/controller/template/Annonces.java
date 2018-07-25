package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.rh.RhAnnonceRecrutement;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rafik on 31/01/17.
 */
public class Annonces extends FormTemplate implements Serializable{

	private ArrayList<RhAnnonceRecrutement> rhAnnonceRecrutementList;
	private RhAnnonceRecrutement currentObj;
	private String taskId;
	private Task currentTask;
	private ProcessInstance procInst;
	private RhCollaborateur currentCollaborateur ;

	private final SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyy");

	@Override
	public void putVars() {

	}

	@Override
	public void init() {

		if(varValues.get("rhAnnonceRecrutementList") != null)
			rhAnnonceRecrutementList= (ArrayList<RhAnnonceRecrutement>)varValues.get("rhAnnonceRecrutementList");
		else
			rhAnnonceRecrutementList	= new ArrayList<RhAnnonceRecrutement>();

		taskId 			= StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
		currentTask 	= activitiService.getTask(taskId);
		procInst 		= activitiService.getProcessInstanceById(currentTask.getProcessInstanceId());
		//currentCollaborateur	= businessService.getRhCollaborateurByActIdUser(userService.getLoggedInUser().getId());

	}

	public ArrayList<RhAnnonceRecrutement> getRhAnnonceRecrutementList() {
		return rhAnnonceRecrutementList;
	}

	public void setRhAnnonceRecrutementList(ArrayList<RhAnnonceRecrutement> rhAnnonceRecrutementList) {
		this.rhAnnonceRecrutementList = rhAnnonceRecrutementList;
	}

	public RhAnnonceRecrutement getCurrentObj() {
		return currentObj;
	}

	public void setCurrentObj(RhAnnonceRecrutement currentObj) {
		this.currentObj = currentObj;
	}

	public void addAnnonce() {

		currentObj= new RhAnnonceRecrutement();
		rhAnnonceRecrutementList.add(currentObj);
		varValues.put("rhAnnonceRecrutementList", rhAnnonceRecrutementList);
	}

	public void deleteAnnonce(int rowId) {

		rhAnnonceRecrutementList.remove(rowId);
		varValues.put("rhAnnonceRecrutementList", rhAnnonceRecrutementList);
	}

	public RhCollaborateur getCurrentCollaborateur() {
		return currentCollaborateur;
	}

	public void setCurrentCollaborateur(RhCollaborateur currentCollaborateur) {
		this.currentCollaborateur = currentCollaborateur;
	}
}
