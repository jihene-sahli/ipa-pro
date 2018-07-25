package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhEmolument;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafik on 17/09/17.
 */
public class Emoluments extends FormTemplate implements Serializable{

	private ArrayList<RhEmolument> rhEmolumentList;
	private RhEmolument currentObj;
	private RhCollaborateur currentCollaborateur ;
	private RhEmolument rhEmolumentTotal = new RhEmolument();

	private final SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyy");

	@Override
	public void putVars() {

	}

	@Override
	public void init() {

		if(varValues.get("rhEmolumentList") != null)
			rhEmolumentList= (ArrayList<RhEmolument>)varValues.get("rhEmolumentList");
		else {
			currentCollaborateur	= businessService.getRhCollaborateurByActIdUser(userService.getLoggedInUser().getId());
			rhEmolumentList = new ArrayList<RhEmolument>();
			Calendar calendar = Calendar.getInstance();
			Calendar calendarRef = Calendar.getInstance();
			calendar.setTime(new Date());
			calendarRef.setTime(new Date());
			calendar.set(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			for(int i = 0; i < calendarRef.get(Calendar.MONTH); i = i + 1) {
				calendar.set(Calendar.MONTH, i);
				RhEmolument rhEmolument = new RhEmolument();
				rhEmolument.setCollaborateurId(currentCollaborateur);
				rhEmolument.setMois(calendar.getTime());
				rhEmolumentList.add(rhEmolument);
			}
			varValues.put("rhEmolumentList", rhEmolumentList);
		}
		calculTotal();
	}

	public void calculTotal(){
		rhEmolumentTotal.setSalaireBase(new BigDecimal(0));
		rhEmolumentTotal.setSecuriteSocialeSalarie(new BigDecimal(0));
		rhEmolumentTotal.setIrg(new BigDecimal(0));
		rhEmolumentTotal.setPrimePanier(new BigDecimal(0));
		rhEmolumentTotal.setIndemniteExpProf(new BigDecimal(0));
		rhEmolumentTotal.setBonusPrimeRappel(new BigDecimal(0));
		rhEmolumentTotal.setRetenuesMaladiesAvances(new BigDecimal(0));
		rhEmolumentTotal.setNetAPayer(new BigDecimal(0));
		rhEmolumentTotal.setSecuriteSocialeEmployeur(new BigDecimal(0));
		for(RhEmolument rhEmolument : rhEmolumentList){
			try {
				rhEmolumentTotal.setSalaireBase(rhEmolumentTotal.getSalaireBase().add(rhEmolument.getSalaireBase()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setSecuriteSocialeSalarie(rhEmolumentTotal.getSecuriteSocialeSalarie().add(rhEmolument.getSecuriteSocialeSalarie()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setIrg(rhEmolumentTotal.getIrg().add(rhEmolument.getIrg()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setPrimePanier(rhEmolumentTotal.getPrimePanier().add(rhEmolument.getPrimePanier()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setIndemniteExpProf(rhEmolumentTotal.getIndemniteExpProf().add(rhEmolument.getIndemniteExpProf()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setBonusPrimeRappel(rhEmolumentTotal.getBonusPrimeRappel().add(rhEmolument.getBonusPrimeRappel()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setRetenuesMaladiesAvances(rhEmolumentTotal.getRetenuesMaladiesAvances().add(rhEmolument.getRetenuesMaladiesAvances()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setNetAPayer(rhEmolumentTotal.getNetAPayer().add(rhEmolument.getNetAPayer()));
			} catch (Exception ex) {}
			try {
				rhEmolumentTotal.setSecuriteSocialeEmployeur(rhEmolumentTotal.getSecuriteSocialeEmployeur().add(rhEmolument.getSecuriteSocialeEmployeur()));
			} catch (Exception ex) {}
		}
	}

	public ArrayList<RhEmolument> getRhEmolumentList() {
		return rhEmolumentList;
	}

	public void setRhEmolumentList(ArrayList<RhEmolument> rhEmolumentList) {
		this.rhEmolumentList = rhEmolumentList;
	}

	public RhEmolument getCurrentObj() {
		return currentObj;
	}

	public void setCurrentObj(RhEmolument currentObj) {
		this.currentObj = currentObj;
	}

	public RhCollaborateur getCurrentCollaborateur() {
		return currentCollaborateur;
	}

	public void setCurrentCollaborateur(RhCollaborateur currentCollaborateur) {
		this.currentCollaborateur = currentCollaborateur;
	}

	public RhEmolument getRhEmolumentTotal() {
		return rhEmolumentTotal;
	}

	public void setRhEmolumentTotal(RhEmolument rhEmolumentTotal) {
		this.rhEmolumentTotal = rhEmolumentTotal;
	}
}
