package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component("salaireController")
public class SalaireController extends FormTemplate implements Serializable {

	private int mensuel ;
	private int annuel ;
	private int part;
	private int ETAnnuel;


	private static final long serialVersionUID = 1L;


	@Override
	public void init() {
		/**/if(varValues.get("FMNTP") == null){
			varValues.put("FMNTP",0);
		}
		if(varValues.get("FANTP") == null){
			varValues.put("FANTP",0);
		}
		if(varValues.get("PVA") == null){
			varValues.put("PVA",0);
		}
		if(varValues.get("EAT") == null){
			varValues.put("EAT",0);
		}

		mensuel=(Integer) varValues.get("FMNTP");
		annuel=(Integer) varValues.get("FANTP");
		part=(Integer) varValues.get("PVA");
		ETAnnuel=(Integer) varValues.get("EAT");
	}

	public void calculeFixeAnnuelEtEnveloppeTotale(){
		annuel = mensuel * 12;
		ETAnnuel = annuel + part;

		varValues.put("FMNTP",mensuel);
		varValues.put("FANTP",annuel);
		varValues.put("PVA",part);
		varValues.put("EAT",ETAnnuel);
	}

	public void calculeEnveloppeTotale(){
		ETAnnuel = annuel + part;

		varValues.put("FMNTP",mensuel);
		varValues.put("FANTP",annuel);
		varValues.put("PVA",part);
		varValues.put("EAT",ETAnnuel);
	}

	@Override
	public void putVars() {

	}

	@Override
	public Map<String, Object> getVarValuesReport() {

		Map<String, Object> mapReport = new HashMap<String, Object>();

		mapReport.put("FMNTP", ((Integer)getMensuel()).toString());
		mapReport.put("FANTP", ((Integer)getAnnuel()).toString());
		mapReport.put("PVA",   ((Integer)getPart()).toString());
		mapReport.put("EAT",   ((Integer)getETAnnuel()).toString());
		return mapReport;
	}

	public SalaireController() {

	}


	public void persist(){
		varValues.put("FMNTP",mensuel);
		varValues.put("FANTP",annuel);
		varValues.put("PVA",part);
		varValues.put("EAT",ETAnnuel);
	}

	public int getMensuel() {
		return mensuel;
	}

	public void setMensuel(int mensuel) {
		this.mensuel = mensuel;
	}

	public int getAnnuel() {
		return annuel;
	}

	public void setAnnuel(int annuel) {
		this.annuel = annuel;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getETAnnuel() {
		return ETAnnuel;
	}

	public void setETAnnuel(int ETAnnuel) {
		this.ETAnnuel = ETAnnuel;
	}
}
