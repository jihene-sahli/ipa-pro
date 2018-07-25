package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhConge;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

@Component("congeController")
public class CongeController extends FormTemplate implements Serializable {
	private Date dateDeb;
	private Date dateFin;
	private Date dateFinLimit;
	private int soldeRest;
	private int soldeAnterieur;

	private String dateDebId="dateDebTemplateConge";
	private String dateFinId="dateFinTemplateConge";
	private String soldeRestId="soldRestTemplateConge";
	private String soldeAnterieurId="soldAntTemplateConge";

	@Override
	public void init() {
		if(varValues.get(soldeAnterieurId) == null)
			intiCPAnterieur();
		else
			soldeAnterieur= (int) varValues.get(soldeAnterieurId);

		if(varValues.get(soldeRestId) == null){
			soldeRest = soldeAnterieur;
			varValues.put(soldeRestId,soldeRest);
		}
		else
			soldeRest= (int) varValues.get(soldeRestId);


	}


	@Override
	public void putVars() {

	}

	private void intiCPAnterieur(){
		String taskId=FacesUtil.getUrlParam("task");
		RhCollaborateur selectedCollab =businessService.getCollaborateurByActIdUser((String)activitiService.getVariablesTasks(taskId).get("Initiator"));
		if(selectedCollab == null)
			return;
		List<RhConge> list = businessService.getCongesByUser(selectedCollab.getId());
		int nbrJoursCongesUtilise  =  0 ;
		for (RhConge conge:list ) {
			nbrJoursCongesUtilise += conge.getNbrJour() ;

		}
		soldeAnterieur= 30-nbrJoursCongesUtilise ;
		varValues.put(soldeAnterieurId,soldeAnterieur);
	}

	// get nombres jours except friday
	private int nbrjoursValidConj(Object datedeb,Object DateFin){
		if(datedeb == null || DateFin == null)
			return 0;
		Date date1=(Date)datedeb;
		Date date2=(Date)DateFin;
		Calendar cal1=Calendar.getInstance();
		Calendar cal2=Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int allJours = 0;
		while(cal1.before(cal2)){
			if(cal1.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				cal1.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			allJours++;
			cal1.add(Calendar.DAY_OF_YEAR, 1);
		}

		return allJours;

	}

	public void updateSoldeRest(){
		soldeRest =  soldeAnterieur - nbrjoursValidConj(dateDeb,dateFin);
		varValues.put(soldeRestId,soldeRest);
	}

	private Date addJoursToDate(Date date,int nbrJours){
		Calendar cl=Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.DAY_OF_YEAR, nbrJours);
		return cl.getTime();
	}
	// Getters and Setters

	public Date getDateFin() {
		dateFin = (Date)  varValues.get(dateFinId);

		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
		varValues.put(dateFinId,dateFin);
	}

	public int getSoldeRest() {
		return soldeRest;
	}

	public void setSoldeRest(int soldeRest) {
		this.soldeRest = soldeRest;
	}

	public Date getDateDeb() {
		dateDeb = (Date)  varValues.get(dateDebId);
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
		varValues.put(dateDebId,dateDeb);
	}

	public int getSoldeAnterieur() {
		return soldeAnterieur;
	}

	public Date getDateFinLimit() {
		if(dateDeb != null)
			return addJoursToDate(dateDeb,soldeRest+(soldeRest/6)-1);
		return dateFinLimit;
	}

	@Override
	public Map<String,Object> getVarValuesReport(){
		Map<String, Object> mapReport = new HashMap<String, Object>();

		mapReport.put("dateDebTemplateConge",varValues.get(dateDebId) );
		mapReport.put("dateFinTemplateConge", varValues.get(dateFinId));
		mapReport.put("soldAntTemplateConge", String.valueOf(varValues.get(soldeAnterieurId)));
		mapReport.put("soldRestTemplateConge",String.valueOf(varValues.get(soldeRestId)) );

		return mapReport;
	}


}
