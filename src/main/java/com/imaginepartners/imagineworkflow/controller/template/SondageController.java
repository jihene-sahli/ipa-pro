package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.rh.AldSondage;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class SondageController extends FormTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private AldSondage sondage;

	public boolean userIsDRH;

	private String taskId;

	private Task currentTask;

	private List<AldSondage> initSondages;

	private List<AldSondage> sondagesList;

	private List<String> listeReponces;
	private  String reponseSelectionnee;
	private  String reponseEcrite;
	private boolean taskIsAnalyseResultat = false;
	private boolean taskIsRespond = false;





	@Override
	public void init() {
		listeReponces= new ArrayList<String>();
		listeReponces.add("Oui");
		listeReponces.add("Non");
		listeReponces.add("Autre");

		taskId = StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
		currentTask = activitiService.getTask(taskId);
		//taskNumber = Integer.parseInt(currentTask.getName().substring(1, 3));


	/*	if (currentTask.getName().contains("Répondre au Sondage")) {
			List<AldSondage> sondages = new ArrayList();

			for (AldSondage sondage : (ArrayList<AldSondage>) varValues.get("sondagesList")) {
				AldSondage aldSondage = new AldSondage();
				aldSondage.setQuestion(sondage.getQuestion());
				sondages.add(aldSondage);
			}

			varValues.put("sondagesList", sondages);

		}*/
		initSondages = new ArrayList();
		if (currentTask.getName().contains("Répondre au Sondage")) {
			taskIsRespond =true;
			sondagesList = new ArrayList<AldSondage>();

			for (AldSondage sondage : (ArrayList<AldSondage>) varValues.get("sondagesList")) {
				AldSondage aldSondage = new AldSondage();
				aldSondage.setQuestion(sondage.getQuestion());
				aldSondage.setReponse("Oui");
				sondagesList.add(aldSondage);
			}

			/*if (varValues.get("initSondages") != null) {
				sondagesList = (ArrayList<AldSondage>) varValues.get("initSondages");
			} else {
				sondagesList = new ArrayList<AldSondage>();
			}*/

		} else if (currentTask.getName().contains("Analyser les résultats du Sondage")) {
			taskIsAnalyseResultat = true;
			if (varValues.get("allSondages") != null) {
				sondagesList = (ArrayList<AldSondage>) varValues.get("allSondages");
			} else {
				sondagesList = new ArrayList<AldSondage>();
			}

		} else {
			if (varValues.get("sondagesList") != null) {
				sondagesList = (ArrayList<AldSondage>) varValues.get("sondagesList");

			} else {
				sondagesList = new ArrayList<AldSondage>();
			}
		}

		checkUser();

	}

	public void changerReponse(AldSondage s, int index){
	int i = sondagesList.indexOf(s);
		if(reponseSelectionnee != null && !reponseSelectionnee.equals("Autre")){
			s.setReponse(reponseSelectionnee);

			reponseEcrite = "";
		}else {
			s.setReponse("");
			reponseEcrite = "";

		}
	}
	public void ecrireReponse(AldSondage s){
		int i = sondagesList.indexOf(s);
		s.setReponse(reponseEcrite);
	}
	public void replaceElemnt(AldSondage s){
		sondagesList.indexOf(s);

	}

	public void checkUser() {
		String userId = userService.getLoggedInUser().getId();
		if (userId.equals("DRH")) {
			userIsDRH = true;
		} else {
			userIsDRH = false;
		}

	}

	public void addSondage() {

		sondagesList.add(new AldSondage("", ""));

	}

	public void deleteSondage(AldSondage f) {
		sondagesList.remove(f);
		varValues.put("sondagesList", sondagesList);
	}

	@Override
	public void putVars() {

	}

	@Override
	public Map<String, Object> getVarValues() {

		 if (currentTask.getName().contains("Répondre au Sondage")) {

			for (AldSondage sondage : sondagesList) {
				sondage.setCollaborateur(userService.getLoggedInUser().getId());
			}

			if (varValues.get("allSondages") != null) {
				sondagesList.addAll((Collection<? extends AldSondage>) varValues.get("allSondages"));
			}
			varValues.put("allSondages", sondagesList);

			// rowStyleClass="#{sondageController.ShowOrHideRowByCollaborator(aldSondage)}"
		} else {

			varValues.put("sondagesList", sondagesList);
		}
		if (currentTask.getName().contains("Analyser les résultats du Sondage")) {
			businessService.saveEntityList(sondagesList);
		}
		return varValues;
	}

	public String ShowOrHideRowByCollaborator(AldSondage sondage) {
		if (currentTask.getName().contains("Analyser les résultats du Sondage")) {
			String collaborateur = sondage.getCollaborateur();
			String demandeur = (String) activitiService.getTaskVariables(taskId).get("collaborateur");
			Map myMap = activitiService.getTaskVariables(taskId);
			if (collaborateur.equals(demandeur)) {
				return "";
			} else {
				return "ui-helper-hidden";
			}
		}
		return "";
	}

	@Override
	public Map<String, Object> getVarValuesReport() {

		return null;
	}

	public SondageController() {

	}

	public AldSondage getSondage() {
		return sondage;
	}

	public void setSondage(AldSondage sondage) {
		this.sondage = sondage;
	}

	public List<AldSondage> getSondagesList() {
		return sondagesList;
	}

	public void setSondagesList(List<AldSondage> sondagesList) {
		this.sondagesList = sondagesList;
	}

	public boolean isUserIsDRH() {
		return userIsDRH;
	}

	public void setUserIsDRH(boolean userIsDRH) {
		this.userIsDRH = userIsDRH;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public List<AldSondage> getInitSondages() {
		return initSondages;
	}

	public void setInitSondages(List<AldSondage> initSondages) {
		this.initSondages = initSondages;
	}

	public List<String> getListeReponces() {
		return listeReponces;
	}

	public void setListeReponces(List<String> listeReponces) {
		this.listeReponces = listeReponces;
	}

	public String getReponseSelectionnee() {
		return reponseSelectionnee;
	}

	public void setReponseSelectionnee(String reponseSelectionnee) {
		this.reponseSelectionnee = reponseSelectionnee;
	}

	public String getReponseEcrite() {
		return reponseEcrite;
	}

	public void setReponseEcrite(String reponseEcrite) {
		this.reponseEcrite = reponseEcrite;
	}

	public boolean isTaskIsAnalyseResultat() {
		return taskIsAnalyseResultat;
	}

	public void setTaskIsAnalyseResultat(boolean taskIsAnalyseResultat) {
		this.taskIsAnalyseResultat = taskIsAnalyseResultat;
	}

	public boolean isTaskIsRespond() {
		return taskIsRespond;
	}

	public void setTaskIsRespond(boolean taskIsRespond) {
		this.taskIsRespond = taskIsRespond;
	}
}
