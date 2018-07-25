package com.imaginepartners.imagineworkflow.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginepartners.imagineworkflow.controller.ClasseToJasper.Item;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.business.BizFournisseur;
import com.imaginepartners.imagineworkflow.models.rh.AldFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhConge;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.HashedMap;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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

/**
 * Created by Med on 11/12/17.
 */
@Controller
@Scope("view")
public class DashAchatsController implements Serializable {

	//** components Keys from form Builder*******************
	private String formKey             ="47";
	private String formKeyHS             ="45";
	private String formKeyConv             ="46";
	private String achatDateDemandeKey ="form_1504450806061";
	private String bonCmdSigneKey      ="form_1520506798977";
	private String bonLivKey           ="form_1504777521045";
	private String familleKey          ="form_1521553951632";
	private String btnValidKey		   ="valider";
	private String factureInstKey	   ="form_1504777562715";
	//*******************************************************

	//** components Keys from form Builder for xhtml page*******
	private String demandeurKey        ="form_1504450740586";
	private String directionKey        ="form_1521550327348";
	private String descriptionKey      ="form_1504450816689";
	private String montantDAKey		   ="valeur";
	private String fournisseurKey      ="fournisseurList";
	//*******************************************************

	@Autowired
	ActivitiService activitiService;
	@Autowired
	BusinessService businessService;

	private int activeIndex;
	private List<String> listTaskId;
	private List<String> listTaskIdHS;
	private List<String> listTaskIdConv;
	private int selectedYear;
	private List<SelectItem> listYears;
	Calendar calender=Calendar.getInstance();
	private Map<Integer,Integer> nbrAchatMois=new HashedMap();
	private Map<String,Integer> nbrAchatFamille=new HashedMap();
	private Map<String,Integer> daBcRejected=new HashedMap();
	private Map<String,Integer> factureInst=new HashedMap();
	private Map<String,List<Task>> AllAchatsTasksMap = new HashedMap();
	private Map<String,List<Task>> AllAchatsHSTasksMap = new HashedMap();
	private Map<String,List<Task>> AllAchatsConvTasksMap = new HashedMap();
	@PostConstruct
	public void init() {
		// get list of taskIds from active processes
		listTaskId =businessService.getTaskByFormBuilderId(formKey);
		listTaskIdHS =businessService.getTaskByFormBuilderId(formKeyHS);
		listTaskIdConv =businessService.getTaskByFormBuilderId(formKeyConv);

		// int years/famille for active processes
		initMaps();
	}

	/**
	 * Nombre de bon de commande livrés et qui sont en attentes de Bon de livraison
	 * @return nbr
	 */
	public String getNombreBonCommande(){
		int bonCmdAttenteLiv =0;
		int BonCmdLiv=0;

		for(String taskId:listTaskId){
			Map<String, Object> variablesTask=	activitiService.getVariablesTasks(taskId);

			if(    variablesTask.get(bonCmdSigneKey)!=null){  // id input bon de Commandes signé
				if(hasUploadedFile((Long) variablesTask.get(bonLivKey))) // id input bon de livraison
					bonCmdAttenteLiv++;
				if(hasUploadedFile((Long) variablesTask.get(bonCmdSigneKey)))
					BonCmdLiv++;

			}

		}

		JsonObject node = new JsonObject();
		node.addProperty("nonlivrer", bonCmdAttenteLiv);
		node.addProperty("livrer", BonCmdLiv);

		return String.valueOf(node);
	}

	/**
	 * Nombre de demandes d'achat hors système par status
	 * @return nbr
	 */
	public String getDaHSByState(){
		int factureRecue =0;
		int dpEnCours=0;
		int dpValidee =0;
		int FactureReglee=0;
		for(String taskId:listTaskIdHS){
			Task selectedTask = activitiService.getTask(taskId);
			List<Task> TasksList;
			String taskNum = selectedTask.getName().substring(0,3);
			if(    taskNum!=null && taskNum.equals("T01")) factureRecue++;
			else if (taskNum!=null && ( taskNum.equals("T03") || taskNum.equals("T04")  || taskNum.equals("T05"))) dpEnCours++;
			else if (taskNum!=null && taskNum.equals("T06")) dpValidee++;
			else if (taskNum!=null && taskNum.equals("T07")) FactureReglee++;

		}

		JsonObject node = new JsonObject();
		node.addProperty("factRecue", factureRecue);
		node.addProperty("dpEnCours", dpEnCours);
		node.addProperty("dpValidee", dpValidee);
		node.addProperty("FactureReglee", FactureReglee);

		return String.valueOf(node);
	}
	/**
	 * Nombre de demandes d'achat conventionné par status
	 * @return nbr
	 */
	public String getDaConvByState(){
		int factureRecue =0;
		int dpEnCours=0;
		int dpValidee =0;
		int FactureReglee=0;
		for(String taskId:listTaskIdConv){
			Task selectedTask = activitiService.getTask(taskId);
			List<Task> TasksList;
			String taskNum = selectedTask.getName().substring(0,3);
			if(    taskNum!=null && taskNum.equals("T01")) factureRecue++;
			else if (taskNum!=null && ( taskNum.equals("T03") || taskNum.equals("T04")  || taskNum.equals("T02"))) dpEnCours++;
			else if (taskNum!=null && taskNum.equals("T05")) dpValidee++;
			else if (taskNum!=null && taskNum.equals("T06")) FactureReglee++;

		}

		JsonObject node = new JsonObject();
		node.addProperty("factRecue", factureRecue);
		node.addProperty("dpEnCours", dpEnCours);
		node.addProperty("dpValidee", dpValidee);
		node.addProperty("FactureReglee", FactureReglee);

		return String.valueOf(node);
	}


	// test if exist uploaded doc
	private Boolean hasUploadedFile(Long iwUpload){

		try {
			List<?> list = businessService.getIwFilesForUpload(iwUpload);
			return list.size() > 0;
		}catch (Exception e){
			return false;
		}
	}




	public int getSelectedYear() {
		if(selectedYear ==0)
			selectedYear=calender.get(Calendar.YEAR);
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<SelectItem> getListYears() {
		return listYears;
	}

	public void setListYears(List<SelectItem> listYears) {
		this.listYears = listYears;
	}

	// retrieve nbrAchat/years,nbrAchat/famille,Nbr DC -> (BC,Rejected) from active processes
	private void initMaps(){

		// Achat
		listYears=new ArrayList<>();

		nbrAchatFamille.put("Autres", 0);

		daBcRejected.put("DA",0);
		daBcRejected.put("DA_rejected", 0);
		daBcRejected.put("BC", 0);

		factureInst.put("da",0);
		factureInst.put("factInst",0);

		for(String taskId:listTaskId) {
			Map<String, Object> variablesTask = activitiService.getVariablesTasks(taskId);
			Calendar cal=Calendar.getInstance();
			try {
				cal.setTime((Date) variablesTask.get(achatDateDemandeKey));
				int yearFromProcess=cal.get(Calendar.YEAR);

				//Map for year/nbrAchats
				if(nbrAchatMois.get(yearFromProcess) !=null)
					nbrAchatMois.put(yearFromProcess,nbrAchatMois.get(yearFromProcess)+1);
				else
					nbrAchatMois.put(yearFromProcess,1);

				//Map for famille/nbrAchats
				if(nbrAchatFamille.get(variablesTask.get(familleKey)) != null)
					nbrAchatFamille.put(String.valueOf(variablesTask.get(familleKey)),nbrAchatFamille.get(String.valueOf(variablesTask.get(familleKey)))+1);
				else
				{
					if(variablesTask.get(familleKey) == null) {
						if (nbrAchatFamille.get("Autres") != null)
							nbrAchatFamille.put("Autres", nbrAchatFamille.get("Autres")+ 1);

					}else{
						nbrAchatFamille.put(String.valueOf(variablesTask.get(familleKey)),1);
					}
				}


				//Map for DA -> (BC,Rejected)
				if(daBcRejected.get("DA")!=null)
					daBcRejected.put("DA",daBcRejected.get("DA")+1);

				if("non".equals(variablesTask.get(btnValidKey)))
					if (daBcRejected.get("DA_rejected") != null)
						daBcRejected.put("DA_rejected", daBcRejected.get("DA_rejected") + 1);

				if(hasUploadedFile((Long) variablesTask.get(bonCmdSigneKey)))
					if (daBcRejected.get("BC") != null)
						daBcRejected.put("BC", daBcRejected.get("BC") + 1);


				//Map for facture instance

				if(factureInst.get("da")!=null)
					factureInst.put("da",factureInst.get("da")+1);

				if(hasUploadedFile((Long)variablesTask.get(factureInstKey)))
					factureInst.put("factInst",factureInst.get("factInst")+1);



				//Map for AllAchatsTasks instance

					Task selectedTask = activitiService.getTask(taskId);
					List<Task> TasksList;
					String taskNum = selectedTask.getName().substring(0,3);
					if(AllAchatsTasksMap.get(taskNum) == null){

						TasksList= new ArrayList<>();
						TasksList.add(selectedTask);
						AllAchatsTasksMap.put(taskNum,TasksList);
					}else {
						TasksList = AllAchatsTasksMap.get(taskNum);
						TasksList.add(selectedTask);
						AllAchatsTasksMap.put(taskNum,TasksList);
					}


			}catch(Exception e){e.printStackTrace();}
		}

		for (Map.Entry<Integer, Integer> entry : nbrAchatMois.entrySet())
			listYears.add(new SelectItem(Integer.toString(entry.getKey()),Integer.toString(entry.getKey())));

		// Achat Hors Système

		for(String taskId:listTaskIdHS) {
			try {
				//Map for AllAchatsTasks instance

				Task selectedTask = activitiService.getTask(taskId);

				List<Task> TasksList;
				String taskNum = selectedTask.getName().substring(0,3);
				if(AllAchatsHSTasksMap.get(taskNum) == null){

					TasksList= new ArrayList<>();
					TasksList.add(selectedTask);
					AllAchatsHSTasksMap.put(taskNum,TasksList);
				}else {
					TasksList = AllAchatsHSTasksMap.get(taskNum);
					TasksList.add(selectedTask);
					AllAchatsHSTasksMap.put(taskNum,TasksList);
				}


			}catch(Exception e){e.printStackTrace();}
		}
		// Achat Conventionné
		for(String taskId:listTaskIdConv) {
			try {
				//Map for AllAchatsTasks instance

				Task selectedTask = activitiService.getTask(taskId);

				List<Task> TasksList;
				String taskNum = selectedTask.getName().substring(0,3);
				if(AllAchatsConvTasksMap.get(taskNum) == null){

					TasksList= new ArrayList<>();
					TasksList.add(selectedTask);
					AllAchatsConvTasksMap.put(taskNum,TasksList);
				}else {
					TasksList = AllAchatsConvTasksMap.get(taskNum);
					TasksList.add(selectedTask);
					AllAchatsConvTasksMap.put(taskNum,TasksList);
				}


			}catch(Exception e){e.printStackTrace();}


		}
	}

	// get nbr Achats From Map(year,NbrAchats)
	public String getNbrAchatSelectedYear(){
		JsonObject node = new JsonObject();
		node.addProperty("year", this.selectedYear);
		node.addProperty("nbrAchats", nbrAchatMois.get(this.selectedYear));
		return String.valueOf(node);
	}

	// get nbr Achats From Map(Famille,NbrAchats)
	public String getNbrAchatFamille(){

		JsonArray array=new JsonArray();

		for (Map.Entry<String, Integer> entry : nbrAchatFamille.entrySet()) {
			JsonObject node = new JsonObject();
			node.addProperty("id",entry.getKey());
			node.addProperty("value",entry.getValue());
			array.add(node);
		}
		return String.valueOf(array);
	}

	// get nbr Achats From Map(Famille,NbrAchats)
	public String getNbrDaBcRejected(){
		JsonObject node = new JsonObject();
		node.addProperty("da",daBcRejected.get("DA"));
		node.addProperty("bc",daBcRejected.get("BC"));
		node.addProperty("dA_rejected",daBcRejected.get("DA_rejected"));

		return String.valueOf(node);
	}

	// get nbr Facture en instance
	public String getNbrFactureInstance(){
		JsonObject node = new JsonObject();
		node.addProperty("da",factureInst.get("da"));
		node.addProperty("factureInst",factureInst.get("factInst"));

		return String.valueOf(node);
	}

	public Object getVarProcessByExecuionId(String excutionId, String varName){

		return activitiService.getVariableProcess(excutionId, varName);
	}

	public List<BizFournisseur> getVarProcessFournisseursByExecuionId(String excutionId, String varName){

		List<BizFournisseur> multilineentityFournisseur;

			multilineentityFournisseur = (List<BizFournisseur>) activitiService.getVariableProcess(excutionId, varName);

		if(multilineentityFournisseur != null && !multilineentityFournisseur.isEmpty()) {
		return multilineentityFournisseur;
		}
		return new ArrayList<>();
	}

	public List<Task> getTasksbyName(String formKey, String... taskNums){

		List<Task> result=new ArrayList<>();
		if (formKey.equals("achat")){
		for(String taskNum:taskNums){
			if(AllAchatsTasksMap.get(taskNum) != null)
			result.addAll(AllAchatsTasksMap.get(taskNum));
		}
		}
		else if (formKey.equals("HS")){
			for(String taskNum:taskNums){
				if(AllAchatsHSTasksMap.get(taskNum) != null)
					result.addAll(AllAchatsHSTasksMap.get(taskNum));
			}
		}
		if (formKey.equals("conv")){
			for(String taskNum:taskNums){
				if(AllAchatsConvTasksMap.get(taskNum) != null)
					result.addAll(AllAchatsConvTasksMap.get(taskNum));
			}
		}
		return result ;
	}


	public int getActiveIndex() {return activeIndex;}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<String> getListTaskId() {
		return listTaskId;
	}

	public void setListTaskId(List<String> listTaskId) {
		this.listTaskId = listTaskId;
	}

	public String getDemandeurKey() {return demandeurKey;}

	public String getDirectionKey() {return directionKey;}

	public String getDescriptionKey() {return descriptionKey;}

	public String getMontantDAKey() {return montantDAKey;}

	public String getFournisseurKey() {return fournisseurKey;}

	public String getDateDAKey() {return achatDateDemandeKey;}
}
