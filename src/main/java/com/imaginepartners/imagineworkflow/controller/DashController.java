package com.imaginepartners.imagineworkflow.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginepartners.imagineworkflow.controller.ClasseToJasper.Item;
import com.imaginepartners.imagineworkflow.models.rh.AldFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhConge;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import org.primefaces.model.StreamedContent;

/**
 * Created by Med on 11/12/17.
 */
@Controller
@Scope("view")
public class DashController implements Serializable {

	@Autowired
	ActivitiService activitiService;
	@Autowired
	BusinessService businessService;
	@Autowired
	HierarchyController hierarchyController;
	@Autowired
	private FileController fileController;
	@Autowired
	private UserService userService;
	private String teamId;
	private List<Group> teamList;
	private List<User> memberList;
	private List<RhCollaborateur> collaborateurList;
	private Integer ageMin;
	private Integer ageMax;
	private List<RhCollaborateur> allCollaborateurList;
	private Date dateMin;
	private Date dateMax;
	private Double taux;
	private List<AldFormation> allFormationsList;
	private String dayOrYear;
	private List<RhCollaborateur> collaborateurs;
	private RhCollaborateur collaborateur;
	private int activeIndex;
	private  float moyen;
	private float moyenEvalchaud ;
	Map<String, List<AldFormation>> aldformations=new HashMap<String, List<AldFormation>>();

	@PostConstruct
	public void init() {


		if (!hierarchyController.getGroupList().isEmpty()) {
			teamList = hierarchyController.getGroupList();
			teamList.get(0);
		} else {
			teamList = new ArrayList<Group>();
		}

		allCollaborateurList = new ArrayList<RhCollaborateur>();
		allFormationsList = new ArrayList<AldFormation>();
		businessService.getEntiteList(AldFormation.class.getName());

		allCollaborateurList.addAll(businessService.getRhCollaborateurList());
		allFormationsList.addAll((List<AldFormation>) (List<?>) businessService.getEntiteList(AldFormation.class.getName()));
		loggedInUser = userService.getLoggedInUser();
		collaborateur=businessService.getRhCollaborateurByActIdUser(loggedInUser.getId());
		setAldformations(getRecapitulatipFormation());
	}
    public void buttonAction(){
		setActiveIndex(9);
}
	public void teamChanged() {
		collaborateurs = new ArrayList<RhCollaborateur>();
		if (teamId != null && "tous".equals(teamId.toLowerCase()) == false) {
			collaborateurs.addAll(getListCollaboratorParEquipe(teamId));
		}
		if (teamId != null && "tous".equals(teamId.toLowerCase()) == true) {
			for (Group group : teamList) {
				collaborateurs.addAll(getListCollaboratorParEquipe(group.getName()));
			}
		}
	}

	public int getSoldeConge(int collaborateur) {
		List<RhConge> conges = businessService.getCongesByUser(collaborateur);
		int soldeConge = 30;
		if (conges != null && !conges.isEmpty()) {
			for (RhConge conge : conges) {
				soldeConge -= conge.getNbrJour();
			}
			return soldeConge;
		}
		return 0;
	}

	public Boolean getAvatar(RhCollaborateur collaborateur) {
		Picture avatar = activitiService.getAvatar(collaborateur.getActIdUser());
		if (avatar != null) {
			return true;
		}
		return false;
	}

	public String getMoyenneAgeAsJson(String id) {
		JsonArray ages = new JsonArray();
		List<RhCollaborateur> rhCollaborateurs;
		if (id != null) {

			if (id.toLowerCase().equals("tous")) {
				for (Group group : teamList) {
					rhCollaborateurs = new ArrayList<RhCollaborateur>();
					rhCollaborateurs.addAll(getListCollaboratorParEquipe(group.getName()));
					JsonObject node = new JsonObject();
					node.addProperty("team", group.getId());
					node.addProperty("avgAge", getMoyenneAgeParEquipe(group.getName()));
					node.addProperty("nbMembers", rhCollaborateurs.size());
					ages.add(node);
				}
			} else {
				rhCollaborateurs = new ArrayList<RhCollaborateur>();
				rhCollaborateurs.addAll(getListCollaboratorParEquipe(id));
				JsonObject node = new JsonObject();
				node.addProperty("team", id);
				node.addProperty("avgAge", getMoyenneAgeParEquipe(id));
				node.addProperty("nbMembers", rhCollaborateurs.size());
				ages.add(node);
			}

			return ages.toString();
		}
		return null;
	}

	public String getAgeMinMaxAsJson() {
		JsonArray ages = new JsonArray();
		int age;
		final int i = 1;
		Map<Integer, Integer> ageMinMax = new HashMap<Integer, Integer>();
		for (RhCollaborateur collaborateur : allCollaborateurList) {
			if (collaborateur.getDateNaissance() != null) {
				age = getAgeParCollaborateur(collaborateur.getDateNaissance());
				if (ageMin != null && ageMax != null && ageMin <= age && age <= ageMax) {
					if (ageMinMax.get(age) != null) {
						ageMinMax.put(age, ageMinMax.get(age) + i);
					} else {
						ageMinMax.put(age, i);
					}
				}
			}
		}
		for (Map.Entry<Integer, Integer> entry : ageMinMax.entrySet()) {
			JsonObject node = new JsonObject();
			node.addProperty("age", entry.getKey());
			node.addProperty("nbMembers", entry.getValue());
			ages.add(node);
		}
		return ages.toString();

	}

	public String getManWomanAsJson(String id) {
		JsonArray data = new JsonArray();
		List<RhCollaborateur> womanList;
		List<RhCollaborateur> manList;

		if (id.toLowerCase().equals("tous")) {
			if (!allCollaborateurList.isEmpty()) {
				JsonObject node = new JsonObject();
				womanList = new ArrayList<RhCollaborateur>();
				manList = new ArrayList<RhCollaborateur>();
				for (RhCollaborateur collaborateur : allCollaborateurList) {
					if (collaborateur.getSexe() != null && "F".equals(collaborateur.getSexe())) {
						womanList.add(collaborateur);
					}
					if (collaborateur.getSexe() != null && "M".equals(collaborateur.getSexe())) {
						manList.add(collaborateur);
					}
				}
				node.addProperty("man", manList.size());
				node.addProperty("woman", womanList.size());
				data.add(node);
			}

		} else {
			womanList = new ArrayList<RhCollaborateur>();
			manList = new ArrayList<RhCollaborateur>();
			womanList.addAll(getListCollaboratorParSexe(id, "F"));
			manList.addAll(getListCollaboratorParSexe(id, "M"));
			JsonObject node = new JsonObject();
			node.addProperty("man", manList.size());
			node.addProperty("woman", womanList.size());
			data.add(node);
		}
		return data.toString();
	}

	public String getTauxSatisfactionAsJson() {
		JsonArray data = new JsonArray();
		List<AldFormation> formations = new ArrayList<AldFormation>();
		Map<String, Integer> formationsMap = new HashMap<String, Integer>();
		int nbreFormation = 0;
		float sommeEval = 0;

		final int i = 1;
		if (dateMin != null && dateMax != null && taux != null) {
			formations.addAll(getFormationTauxSatisfaction(dateMin, dateMax, taux / 10));
			if(formations != null){
				nbreFormation = formations.size();
			}

			for (AldFormation formation : formations) {
				String dateFormation = formation.getDateFormation().toString();
				sommeEval = sommeEval +formation.getEvalChaud();
				if (formationsMap.get(dateFormation) != null) {
					formationsMap.put(dateFormation, formationsMap.get(dateFormation) + i);
				} else {
					formationsMap.put(formation.getDateFormation().toString(), i);
				}
			}
			moyenEvalchaud = (float)sommeEval/nbreFormation;

			for (Map.Entry<String, Integer> entry : formationsMap.entrySet()) {
				LocalDate date = LocalDate.parse(entry.getKey());
				int day = date.getDayOfMonth();
				int month = date.getMonthValue();
				int year = date.getYear();
				JsonObject node = new JsonObject();
				node.addProperty("day", day);
				node.addProperty("month", month);
				node.addProperty("year", year);
				node.addProperty("nbFormations", entry.getValue());
				data.add(node);
			}
			return data.toString();
		}
		return "";
	}

	public String getFormationDayYearAsJson() {
		JsonArray data = new JsonArray();
		List<AldFormation> formations = new ArrayList<AldFormation>();
		Map<String, Integer> formationsMap = new HashMap<String, Integer>();
		final int i = 1;
		if (dateMin != null && dateMax != null && dayOrYear != null) {
			if ("jour".equals(dayOrYear.toLowerCase())) {
				formations.addAll(getFormationBetweenMinMax(dateMin, dateMax));
			}
			if ("année".equals(dayOrYear.toLowerCase())) {
				formations.addAll(getFormationBetweenMinMaxYear(dateMin, dateMax));
			}
			for (AldFormation formation : formations) {
				if ("jour".equals(dayOrYear.toLowerCase())) {
					String dateFormation = formation.getDateFormation().toString();
					if (formationsMap.get(dateFormation) != null) {
						formationsMap.put(dateFormation, formationsMap.get(dateFormation) + i);
					} else {
						formationsMap.put(dateFormation, i);
					}
				}
				if ("année".equals(dayOrYear.toLowerCase())) {
					LocalDate dateFormation = LocalDate.parse(formation.getDateFormation().toString());
					String yearFormation = Integer.toString(dateFormation.getYear());
					if (formationsMap.get(yearFormation) != null) {
						formationsMap.put(yearFormation, formationsMap.get(yearFormation) + i);
					} else {
						formationsMap.put(yearFormation, i);
					}
				}
			}
			for (Map.Entry<String, Integer> entry : formationsMap.entrySet()) {

				if ("jour".equals(dayOrYear.toLowerCase())) {
					LocalDate date = LocalDate.parse(entry.getKey());
					int day = date.getDayOfMonth();
					int month = date.getMonthValue();
					int year = date.getYear();
					JsonObject node = new JsonObject();
					node.addProperty("day", day);
					node.addProperty("month", month);
					node.addProperty("year", year);
					node.addProperty("nbFormations", entry.getValue());
					data.add(node);
				}
				if ("année".equals(dayOrYear.toLowerCase())) {

					String year = entry.getKey();
					JsonObject node = new JsonObject();
					node.addProperty("year", year);
					node.addProperty("nbFormations", entry.getValue());
					data.add(node);
				}
			}
			return data.toString();
		}
		return "";
	}

	public String getFormationStatusAsJson() {
		JsonArray data = new JsonArray();
		List<AldFormation> formations = new ArrayList<AldFormation>();
		Map<String, Integer> formationsMap = new HashMap<String, Integer>();
		LocalDate now = LocalDate.now();
		String exprime = "Exprimée(s)";
		String valide = "Validée(s)";
		String rejete = "Rejetée(s)";
		String realise = "Réalisée(s)";
		String restant = "Restante(s)";

		final int i = 1;
		if (dateMin != null && dateMax != null) {
			formations.addAll(getFormationBetweenMinMax(dateMin, dateMax));
			for (AldFormation formation : formations) {
				LocalDate dateFormation = LocalDate.parse(formation.getDateFormation().toString());
				// Formation Besoin exprimée
				if (formation.getRejected() == null) {
					if (formationsMap.get(exprime) != null) {
						formationsMap.put(exprime, formationsMap.get(exprime) + i);
					} else {
						formationsMap.put(exprime, i);
					}
				}
				// Formation validée
				if (formation.getRejected() != null && formation.getRejected() == false) {
					if (formationsMap.get(valide) != null) {
						formationsMap.put(valide, formationsMap.get(valide) + i);
					} else {
						formationsMap.put(valide, i);
					}
				}

				// Formation rejetée
				if (formation.getRejected() != null && formation.getRejected() == true) {
					if (formationsMap.get(rejete) != null) {
						formationsMap.put(rejete, formationsMap.get(rejete) + i);
					} else {
						formationsMap.put(rejete, i);
					}
				}
				// Formation réalisée
				if (dateFormation != null && dateFormation.isBefore(now)) {
					if (formationsMap.get(realise) != null) {
						formationsMap.put(realise, formationsMap.get(realise) + i);
					} else {
						formationsMap.put(realise, i);
					}
				}

				// Formation restante
				if (dateFormation != null && dateFormation.isAfter(now)) {
					if (formationsMap.get(restant) != null) {
						formationsMap.put(restant, formationsMap.get(restant) + i);
					} else {
						formationsMap.put(restant, i);
					}
				}
			}

			for (Map.Entry<String, Integer> entry : formationsMap.entrySet()) {
				JsonObject node = new JsonObject();

				node.addProperty("status", entry.getKey());
				node.addProperty("nbFormations", (formationsMap.get(entry.getKey()) != null) ? formationsMap.get(entry.getKey()) : 0);
				data.add(node);
			}
			return data.toString();
		}
		return "";
	}

	public String getFormationAFroidAsJson() {
		JsonArray data = new JsonArray();
		List<AldFormation> formations = new ArrayList<AldFormation>();
		Map<String, Float> formationsMap = new HashMap<String, Float>();
		String _0 = "0";
		String _1 = "1";
		String _2 = "2";
		String _3 = "3";
		String _4 = "4";
		String _5 = "5";
		String _6 = "6";
		String _7 = "7";
		String _8 = "8";
		String _9 = "9";
		String _10= "10";
		String _de0A3= "de0A3";
		String _de4A7= "de4A7";
		String _de8A10= "de8A10";
		String moyenneEvaluationKey= "Moyenne";

		int de0A3 =0;
		int de4A7 =0;
		int de8A10 =0;
		int nbreTotalFormation =0;
		int sommeTotalEvaluation =0;
		float  moyendEvaluation =0;


		final float i = 1;
		if (dateMin != null && dateMax != null) {
			formations.addAll(getFormationBetweenMinMax(dateMin, dateMax));
			for (AldFormation formation : formations) {

				if (formation.getEvalFroid() != null && _0.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					if (formationsMap.get(_0) != null) {
						formationsMap.put(_0, formationsMap.get(_0) + i);
					} else {
						formationsMap.put(_0, i);
					}

					de0A3 ++;
				}
				//
				if (formation.getEvalFroid() != null && _1.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +1;
					if (formationsMap.get(_1) != null) {
						formationsMap.put(_1, formationsMap.get(_1) + i);
					} else {
						formationsMap.put(_1, i);
					}

					de0A3 ++;
				}

				//
				if (formation.getEvalFroid() != null && _2.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {
					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +2;
					if (formationsMap.get(_2) != null) {
						formationsMap.put(_2, formationsMap.get(_2) + i);
					} else {
						formationsMap.put(_2, i);
					}

					de0A3 ++;
				}

				//
				if (formation.getEvalFroid() != null && _3.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {
					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +3;
					if (formationsMap.get(_3) != null) {
						formationsMap.put(_3, formationsMap.get(_3) + i);
					} else {
						formationsMap.put(_3, i);
					}

					de0A3 ++;
				}

				//
				if (formation.getEvalFroid() != null && _4.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +4;
					if (formationsMap.get(_4) != null) {
						formationsMap.put(_4, formationsMap.get(_4) + i);
					} else {
						formationsMap.put(_4, i);
					}

					de4A7 ++;
				}

				//
				if (formation.getEvalFroid() != null && _5.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {
					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +5;
					if (formationsMap.get(_5) != null) {
						formationsMap.put(_5, formationsMap.get(_5) + i);
					} else {
						formationsMap.put(_5, i);
					}

					de4A7 ++;
				}

				//
				if (formation.getEvalFroid() != null && _6.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +6;
					if (formationsMap.get(_6) != null) {
						formationsMap.put(_6, formationsMap.get(_6) + i);
					} else {
						formationsMap.put(_6, i);
					}

					de4A7 ++;
				}

				//
				if (formation.getEvalFroid() != null && _7.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {
					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +7;
					if (formationsMap.get(_7) != null) {
						formationsMap.put(_7, formationsMap.get(_7) + i);
					} else {
						formationsMap.put(_7, i);
					}

					de4A7 ++;
				}

				//
				if (formation.getEvalFroid() != null && _8.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +8;
					if (formationsMap.get(_8) != null) {
						formationsMap.put(_8, formationsMap.get(_8) + i);
					} else {
						formationsMap.put(_8, i);
					}


					de8A10 ++;
				}

				//
				if (formation.getEvalFroid() != null && _9.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +9;
					if (formationsMap.get(_9) != null) {
						formationsMap.put(_9, formationsMap.get(_9) + i);
					} else {
						formationsMap.put(_9, i);
					}

					de8A10 ++;
				}

				//
				if (formation.getEvalFroid() != null && _10.toLowerCase().equals(formation.getEvalFroid().toLowerCase())) {

					nbreTotalFormation ++;
					sommeTotalEvaluation = sommeTotalEvaluation +10;
					if (formationsMap.get(_10) != null) {
						formationsMap.put(_10, formationsMap.get(_10) + i);
					} else {
						formationsMap.put(_10, i);
					}

					de8A10 ++;
				}

			}
			moyendEvaluation =(float) sommeTotalEvaluation/nbreTotalFormation;
			moyen = moyendEvaluation;
			//formationsMap.put(moyenneEvaluationKey,moyendEvaluation );
			for (Map.Entry<String, Float> entry : formationsMap.entrySet()) {
				JsonObject node = new JsonObject();
				//node.addProperty("description", "formations");
				node.addProperty("evalFroid", entry.getKey());
				node.addProperty("nbFormations", (formationsMap.get(entry.getKey()) != null) ? formationsMap.get(entry.getKey()) : 0);
				data.add(node);
			}
			//JsonObject node = new JsonObject();
			//node.addProperty("evalFroid", moyenneEvaluationKey);
			JsonObject node = new JsonObject();
			node.addProperty("de0A3", de0A3);
			node.addProperty("de4A7", de4A7);
			node.addProperty("de8A10", de8A10);
			node.addProperty("moyen", moyendEvaluation);
			data.add(node);
			return data.toString();
		}
		return "";

	}

	public String getMoyenneAncienneteAsJson(String id) {
		JsonArray ages = new JsonArray();
		List<RhCollaborateur> rhCollaborateurs;
		if (id != null) {
			if (id.toLowerCase().equals("tous")) {
				for (Group group : teamList) {
					rhCollaborateurs = new ArrayList<RhCollaborateur>();
					rhCollaborateurs.addAll(getListCollaboratorParEquipe(group.getName()));
					JsonObject node = new JsonObject();
					long a=calculMoyenneAnciennete(group.getName());

					//long nbmois = (long) Math.IEEEremainder(a,12);
					long nbmoisTotal = a/30;
					long nbanne = a/365;
					long nbJrestant = a%365;
					long nbmoisRestant = nbJrestant/30;

					node.addProperty("team", group.getId());
					//node.addProperty("anciennete", calculMoyenneAnciennete(group.getName()));
					node.addProperty("anciennete",nbmoisTotal );
					node.addProperty("nbAnne", nbanne);
					node.addProperty("nbMois", nbmoisRestant);
					node.addProperty("nbMembers", rhCollaborateurs.size());
					ages.add(node);
				}
			} else {
				rhCollaborateurs = new ArrayList<RhCollaborateur>();
				rhCollaborateurs.addAll(getListCollaboratorParEquipe(id));
				JsonObject node = new JsonObject();
				long a =calculMoyenneAnciennete(id);


				//long nbmois = (long) Math.IEEEremainder(a,12);
				long nbmoisTotal = a/30;
				long nbanne = a/365;
				long nbJrestant = a%365;
				long nbmoisRestant = nbJrestant/30;


				node.addProperty("team", id);
				node.addProperty("anciennete",nbmoisTotal);
				node.addProperty("nbAnne", nbanne);
				node.addProperty("nbMois", nbmoisRestant);
				node.addProperty("nbMembers", rhCollaborateurs.size());
				ages.add(node);
			}

			return ages.toString();
		}
		return null;
	}

	public String getFormatedDate(Date date) {
		if (date != null) {
			LocalDateTime dateDebut = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			return formatter.format(dateDebut);
		}
		return null;
	}

	private List<RhCollaborateur> getListCollaboratorParEquipe(String groupId) {

		memberList = new ArrayList<User>();
		collaborateurList = new ArrayList<RhCollaborateur>();
		if (groupId != null) {
			memberList.addAll(activitiService.getMemerbs(groupId));
			if (!memberList.isEmpty()) {
				for (User user : memberList) {
					RhCollaborateur collaborateur = businessService.getRhCollaborateurByActIdUser(user.getId());
					if (collaborateur != null) {
						collaborateurList.add(collaborateur);

					}
				}

			}
			return collaborateurList;
		}
		return null;
	}

	private long getMoyenneAgeParEquipe(String groupId) {
		Double avgAge = 0.0;
		List<RhCollaborateur> rhCollaborateurs = new ArrayList<RhCollaborateur>();
		rhCollaborateurs.addAll(getListCollaboratorParEquipe(groupId));

		if (rhCollaborateurs != null && !rhCollaborateurs.isEmpty()) {
			for (RhCollaborateur collaborateur : rhCollaborateurs) {
				if (collaborateur.getDateNaissance() != null) {
					avgAge += getAgeParCollaborateur(collaborateur.getDateNaissance());
				}
			}
			return Math.round(avgAge / rhCollaborateurs.size());
		}

		return 0;
	}

	private int getAgeParCollaborateur(Date date) {

		LocalDate dateNaissance = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate now = java.time.LocalDate.now();
		return Period.between(dateNaissance, now).getYears();
	}

	private List<RhCollaborateur> getListCollaboratorParSexe(String groupId, String sexe) {
		List<RhCollaborateur> rhCollaborateurs = new ArrayList<RhCollaborateur>();
		List<RhCollaborateur> rhManOrWoman = new ArrayList<RhCollaborateur>();
		if (groupId != null) {
			rhCollaborateurs.addAll(getListCollaboratorParEquipe(groupId));
			if (!rhCollaborateurs.isEmpty()) {
				for (RhCollaborateur collaborateur : rhCollaborateurs) {
					if (collaborateur.getSexe() != null && sexe.equals(collaborateur.getSexe())) {
						rhManOrWoman.add(collaborateur);
					}
				}
			}
			return rhManOrWoman;

		}
		return null;
	}

	private List<AldFormation> getFormationTauxSatisfaction(Date min, Date max, Double taux) {
		List<AldFormation> formationsList = new ArrayList<AldFormation>();
		List<AldFormation> formationsMinMax = new ArrayList<AldFormation>();
		LocalDate dateMin = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateMax = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		formationsMinMax.addAll(getFormationBetweenMinMax(min, max));
		if (!formationsMinMax.isEmpty()) {

			for (AldFormation formation : formationsMinMax) {
				//	LocalDate date = formation.getDateFormation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate date = LocalDate.parse(formation.getDateFormation().toString());

				if (formation.getEvalChaud() != null && taux.intValue() <= formation.getEvalChaud()) {
					formationsList.add(formation);
				}
			}
			return formationsList;
		}
		return null;

	}

	private List<AldFormation> getFormationBetweenMinMax(Date min, Date max) {
		List<AldFormation> formationsList = new ArrayList<AldFormation>();
		LocalDate dateMin = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateMax = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (!allFormationsList.isEmpty()) {

			for (AldFormation formation : allFormationsList) {
				if (formation.getDateFormation() != null) {
					LocalDate date = LocalDate.parse(formation.getDateFormation().toString());

					if ((dateMin.isBefore(date) || dateMin.equals(date)) &&
					(dateMax.isAfter(date) || dateMax.equals(date))) {
						formationsList.add(formation);
					}
				}
			}
			return formationsList;
		}
		return null;

	}

	private List<AldFormation> getFormationBetweenMinMaxYear(Date min, Date max) {
		List<AldFormation> formationsList = new ArrayList<AldFormation>();
		int yearDebut = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
		int yearFin = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();

		if (!allFormationsList.isEmpty()) {

			for (AldFormation formation : allFormationsList) {
				if (formation.getDateFormation() != null) {
					int year = LocalDate.parse(formation.getDateFormation().toString()).getYear();

					if (yearDebut <= year && year <= yearFin) {
						formationsList.add(formation);
					}
				}
			}
			return formationsList;
		}
		return null;
	}

	private long calculMoyenneAnciennete(String groupId) {
		LocalDate now = LocalDate.now();
		long somme = 0;
		List<RhCollaborateur> rhCollaborateurs = new ArrayList<RhCollaborateur>();
		rhCollaborateurs.addAll(getListCollaboratorParEquipe(groupId));

		if (rhCollaborateurs != null && !rhCollaborateurs.isEmpty()) {
			for (RhCollaborateur collaborateur : rhCollaborateurs) {

				if (collaborateur.getDateEmbauche() != null) {
					LocalDate dateEmbauche = collaborateur.getDateEmbauche().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					somme += ChronoUnit.DAYS.between(dateEmbauche, now);
				}
			}
			return Math.round(somme / rhCollaborateurs.size());
		}

		return 0;
	}
	private Map<String,List<AldFormation>> getRecapitulatipFormation() {
		JsonArray data = new JsonArray();
		List<AldFormation> formations = new ArrayList<AldFormation>();
		List<AldFormation> formationsExprime = new ArrayList<AldFormation>();
		List<AldFormation> formationsValide = new ArrayList<AldFormation>();
		List<AldFormation> formationsRejete = new ArrayList<AldFormation>();
		List<AldFormation> formationsRealise = new ArrayList<AldFormation>();
		List<AldFormation> formationsRestante = new ArrayList<AldFormation>();
		Map<String,List<AldFormation>> formationsCollab=new HashMap<String,List<AldFormation>>();

		LocalDate now = LocalDate.now();
		final int i = 1;
		for (AldFormation formation : allFormationsList) {
			// Formation Besoin exprimée
			if (formation.getRejected() == null) {
				formationsExprime.add(formation);
			}
			// Formation validée
			if (formation.getRejected() != null && formation.getRejected() == true && formation.getAttestation()==null) {
				formationsValide.add(formation);
			}
			// Formation rejetée
			if (formation.getRejected() != null && formation.getRejected() == false) {
				formationsRejete.add(formation);
			}
			if (formation.getDateFormation()!=null){
				// Formation réalisée
				if (formation.getAttestation()!=null) {
					formationsRealise.add(formation);
				}

			}

		}
		formationsCollab.put("Formations demandées",formationsExprime);
		formationsCollab.put("Formations Validées",formationsValide);
		formationsCollab.put("Formations Rejetées",formationsRejete);
		formationsCollab.put("Formations Réalisées",formationsRealise);


		return formationsCollab;}
	private StreamedContent iwFile(AldFormation formation, int type){
		Long uploadId=null;
		if (type==1){
			if (formation.getAttestation()!=null)
				uploadId=formation.getAttestation().getIwUploadId();}
			else if (type==2){
				if (formation.getAttestationChaud()!=null)
					uploadId=formation.getAttestationChaud().getIwUploadId();}
				else if (type==3){
					if (formation.getAttestationFroid()!=null)
						uploadId=formation.getAttestationFroid().getIwUploadId();}
		List<IwFile> l = null;
		if (uploadId != null) {
			IwUpload iwUpload = businessService.getIwUpload(uploadId);
			if (iwUpload != null) {
				l = iwUpload.getIwFileList();
			}
		}
		IwFile iwf = null;
		if (l != null && !l.isEmpty()) {
			iwf = l.get(0);
			return fileController.getFile(iwf);
		} else {
			return null;
		}


	}
	public boolean visibleRecap(){
		List<Group>userGroupList = activitiService.getUserGroupList(loggedInUser.getId(), null);
		for (Group group:userGroupList){
		String idGroup=group.getId();
		if (idGroup!=null &&(idGroup.equals("ROLE_DRH") || idGroup.equals("ROLE_RRH")|| idGroup.equals("ROLE_ADMIN")))
			return true;
		}
		return false;
	}

	public StreamedContent getiwFileForForamtion(AldFormation formation,int type){

		return iwFile(formation,type);
	}
	public String getRecapitulatifFormationAsJson() {
		return null;
	}


	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Date getDateMin() {
		return dateMin;
	}

	public void setDateMin(Date dateMin) {
		this.dateMin = dateMin;
	}

	public Date getDateMax() {
		return dateMax;
	}

	public void setDateMax(Date dateMax) {
		this.dateMax = dateMax;
	}

	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double taux) {
		this.taux = taux;
	}

	public List<AldFormation> getAllFormationsList() {
		return allFormationsList;
	}

	public void setAllFormationsList(List<AldFormation> allFormationsList) {
		this.allFormationsList = allFormationsList;
	}

	public String getDayOrYear() {
		return dayOrYear;
	}

	public void setDayOrYear(String dayOrYear) {
		this.dayOrYear = dayOrYear;
	}


	public List<RhCollaborateur> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(List<RhCollaborateur> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}
	private User loggedInUser;

	public List<AldFormation> getFormationsCollabList() {
		return formationsCollabList;
	}

	public void setFormationsCollabList(List<AldFormation> formationsCollabList) {
		this.formationsCollabList = formationsCollabList;
	}

	private List<AldFormation> formationsCollabList;


	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	public Map<String, List<AldFormation>> getAldformations() {
		return aldformations;
	}

	public void setAldformations(Map<String, List<AldFormation>> aldformations) {
		this.aldformations = aldformations;
	}

	public String productPdf() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			InputStream jasperStream = this.getClass().getResourceAsStream("/report/report1.jrxml");

			JasperDesign jasperDesign = JRXmlLoader.load(jasperStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String, Object> parametresmap = new HashMap<>();

			JRDataSource jrDatasource = new JRBeanCollectionDataSource(getListSoldeConge()) ;
			parametresmap.put("datasource", jrDatasource);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametresmap, jrDatasource);
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=soldeConges.pdf");
			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
			FacesContext.getCurrentInstance().responseComplete();


		} catch (JRException e) {
			// TODO Auto-generated catch block
			System.out.println("e  "+ e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("e ");
		}
		return null;

	}
	private  List<Item> getListSoldeConge()
	{
		List<Item> items =  new ArrayList<>() ;

		for (int j=0; j<collaborateurs.size();j++)
		{
			Item i = new Item();
		i.setNom(collaborateurs.get(j).getNom()) ;
         i.setPrenom(collaborateurs.get(j).getPrenom()) ;
         i.setSoldeConge(getSoldeConge(collaborateurs.get(j).getId())) ;
         items.add(i) ;
         }
         return items ;
         }

	public float getMoyen() {
		return moyen;
	}

	public void setMoyen(float moyen) {
		this.moyen = moyen;
	}

	public float getMoyenEvalchaud() {
		return moyenEvalchaud;
	}

	public void setMoyenEvalchaud(float moyenEvalchaud) {
		this.moyenEvalchaud = moyenEvalchaud;
	}
  }
