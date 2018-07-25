package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component("salaireController2")
public class SalaireController2 extends FormTemplate implements Serializable {

	private int mensuel ;
	private int annuel ;
	private int part;
	private int ETAnnuel;


	private static final long serialVersionUID = 1L;


	@Override
	public void init() {
		/**/if(varValues.get("FMNTP2") == null){
			varValues.put("FMNTP2",0);
		}
		if(varValues.get("FANTP2") == null){
			varValues.put("FANTP2",0);
		}
		if(varValues.get("PVA2") == null){
			varValues.put("PVA2",0);
		}
		if(varValues.get("EAT2") == null){
			varValues.put("EAT2",0);
		}

		mensuel=(Integer) varValues.get("FMNTP2");
		annuel=(Integer) varValues.get("FANTP2");
		part=(Integer) varValues.get("PVA2");
		ETAnnuel=(Integer) varValues.get("EAT2");
	}

	public void calculeFixeAnnuelEtEnveloppeTotale(){
		annuel = mensuel * 12;
		ETAnnuel = annuel + part;

		varValues.put("FMNTP2",mensuel);
		varValues.put("FANTP2",annuel);
		varValues.put("PVA2",part);
		varValues.put("EAT2",ETAnnuel);
	}

	public void calculeEnveloppeTotale(){
		ETAnnuel = annuel + part;

		varValues.put("FMNTP2",mensuel);
		varValues.put("FANTP2",annuel);
		varValues.put("PVA2",part);
		varValues.put("EAT2",ETAnnuel);
	}

	@Override
	public void putVars() {

	}

	@Override
	public Map<String, Object> getVarValuesReport() {

		Map<String, Object> mapReport = new HashMap<String, Object>();

		mapReport.put("FNMPT_2", ((Integer)getMensuel()).toString());
		mapReport.put("FANPT_2", ((Integer)getAnnuel()).toString());
		mapReport.put("PART_VAR",   ((Integer)getPart()).toString());
		mapReport.put("ENV_TOT",   ((Integer)getETAnnuel()).toString());
	return mapReport;
	}

	public SalaireController2() {

	}


	public void persist(){
		varValues.put("FMNTP2",mensuel);
		varValues.put("FANTP2",annuel);
		varValues.put("PVA2",part);
		varValues.put("EAT2",ETAnnuel);
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
