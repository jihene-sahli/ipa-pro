package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.controller.FileController;
import com.imaginepartners.imagineworkflow.dashboard.data.DocxData;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.business.BizFournisseur;
import com.imaginepartners.imagineworkflow.models.business.BizLigneCommande;
import com.imaginepartners.imagineworkflow.repositories.EntityRepository;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by medkhelifi on 23/10/16.
 */

//@Component
public class FournisseurTemplate extends FormTemplate implements Serializable {

	private List<BizFournisseur> fournisseurList				= new ArrayList<BizFournisseur>();
	private List<BizFournisseur> allFournisseur					= new ArrayList<BizFournisseur>();
	private List<Map<String,Object>> fournisseurListHash		= new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> oldFournisseurListHash		= new ArrayList<Map<String,Object>>();
	private List<BizLigneCommande> produitsObjects;
	private List<BizFournisseur> fournisseursObjects;
	private BizFournisseur fournisseurAutoSuggest				=  new BizFournisseur();
	private boolean create= false, update= false, delete= false, importer = false;
	private boolean ubdateThisFournisseur=false;
	private BizFournisseur currentFournisseur;
	private int					currentCpt;
	private int totalFournisseur	= 0;
	private List<Integer> totalFournisseurArray	= new ArrayList<Integer>();
	private BigDecimal pr_pu_ht_etm ;
	//ROLE_UTILISATEUR_ACHAT


	private UploadedFile offreTechniqueFile ;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private EntityRepository entityRepository;
	private String taskId;
	private Task currentTask;
	private String executionId;
	private ProcessInstance procInst;
	String loggedUserDestDirPath;
	private String destDirPath;
	private String loggedUser;
	private boolean metierRole;
	private boolean acheteurRole;
	private boolean metierAcheteurRole;

	@Override
	public void init() {
		taskId 			= StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
		currentTask 	= activitiService.getTask(taskId);
		executionId		= currentTask.getExecutionId();
		procInst 		= activitiService.getProcessInstanceById(currentTask.getProcessInstanceId());
		allFournisseur		= (ArrayList<BizFournisseur>) entityRepository.findAll(BizFournisseur.class);
		if(varValues.get("fournisseurList") != null) {
			fournisseurList	= (ArrayList<BizFournisseur>) varValues.get("fournisseurList");
		}
		if(varValues.get("fournisseurListHash") != null){
			fournisseurListHash	= (ArrayList<Map<String,Object>>) varValues.get("fournisseurListHash");
		}
		totalFournisseur	= fournisseurListHash.size();
		totalFournisseurArray	= new ArrayList<Integer>();

		loggedUser			= userService.getLoggedInUser().getId();
		User loggedUserObj	= userService.getLoggedInUser();
		metierRole			= activitiService.isMemberOfGroup(loggedUser,"ROLE_UTILISATEUR_ACHAT");
		acheteurRole		= activitiService.isMemberOfGroup(loggedUser,"ROLE_PROCUREMENT") || activitiService.isMemberOfGroup(loggedUser,"ROLE_ACHETEURS_MGX")
		|| activitiService.isMemberOfGroup(loggedUser,"EQUIPE_DG")|| activitiService.isMemberOfGroup(loggedUser,"ROLE_DG_DAF")||loggedUser.equals("directeur_ressources");
		metierAcheteurRole	= (metierRole || acheteurRole);		metierAcheteurRole	= (metierRole || acheteurRole);

		if("admin".equals(loggedUser)){
			metierRole = acheteurRole = metierAcheteurRole = true;
		}

		createFournisseurFolder();
		persisteValues();
		initializeValues();
	}

	public void createFournisseurFolder(){
		//créer / définir le dossier racine des uploads Réponse aux courriers
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "fournisseur_template";
		File distDir = new File(destDirPath);
		if (!distDir.exists()) {
			distDir.mkdirs();
		}
		loggedUserDestDirPath	= destDirPath+"/";

	}
	@Override
	public void putVars() {
	}
	private void persisteValues(){
		varValues.put("fournisseurList", fournisseurList);
		varValues.put("fournisseurListHash", fournisseurListHash);
		oldFournisseurListHash = new ArrayList<Map<String,Object>>();
		oldFournisseurListHash	= copy((ArrayList<Map<String,Object>>) fournisseurListHash);
		totalFournisseurArray	= new ArrayList<Integer>();
		for (int i = 1; i <= totalFournisseur; i++) {
			totalFournisseurArray.add(i);
		}
	}
	public static ArrayList<Map<String,Object>> copy(ArrayList<Map<String,Object>> original)
	{
		ArrayList<Map<String,Object>> copy = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> item : original){
			Map<String,Object> temp	= new HashMap<String,Object>();
			for (Map.Entry<String, Object> entry : item.entrySet())
			{
				temp.put(entry.getKey(), entry.getValue());
			}
			copy.add(temp);
		}
		return copy;
	}

	public void initializeValues(){
		currentFournisseur	= new BizFournisseur();
		currentCpt			= 0;
		ubdateThisFournisseur = false;
	}


	public List<BizFournisseur> completeFournisseur(String query) {
		List<BizFournisseur> filteredBizFournisseurs = new ArrayList<BizFournisseur>();

		for (int i = 0; i < allFournisseur.size(); i++) {
			BizFournisseur fournisseurItem = allFournisseur.get(i);
			boolean addaddFournisseurItem		= false;
			if(fournisseurItem.getCodeFournisseur()!=null){
				if(fournisseurItem.getCodeFournisseur().toLowerCase().startsWith(query)) {
					addaddFournisseurItem		= true;
				}
			}
			if(fournisseurItem.getRaisonSocial()!=null){
				if(fournisseurItem.getRaisonSocial().toLowerCase().startsWith(query)) {
					addaddFournisseurItem		= true;
				}
			}
			if(addaddFournisseurItem) filteredBizFournisseurs.add(fournisseurItem);
		}
		return filteredBizFournisseurs;
	}
	public void createFournisseur() {
		businessService.saveEntite(currentFournisseur);
		fournisseurList.add(currentFournisseur);

		Map<String,Object> temp	= new HashMap<String,Object>();
		temp.put("fournisseur",currentFournisseur);
		temp.put("newone","oui");
		totalFournisseur++;
		temp.put("classementAcheteur"	,totalFournisseur);
		temp.put("classementMetier"		,totalFournisseur);
		temp.put("classementDefinitif"	,totalFournisseur);
		fournisseurListHash.add(temp);

		persisteValues();
		initializeValues();
	}
	public void updateSelected(Map<String,Object> selected, int index ){
		updateStatus("update");
		currentFournisseur	= (BizFournisseur) selected.get("fournisseur");
		if("oui".equals(selected.get("newone"))){
			ubdateThisFournisseur	= true;
		}
		currentCpt			= index;
	}
	public void editFournisseur(){
		businessService.saveEntite(currentFournisseur);
		fournisseurList.set(currentCpt,currentFournisseur);
		Map<String,Object> temp	= fournisseurListHash.get(currentCpt);
		temp.put("fournisseur",currentFournisseur);
		fournisseurListHash.set(currentCpt,temp);

		persisteValues();
		initializeValues();
	}
	public void deleteFournisseur(){
		fournisseurList.remove(currentCpt);
		persisteValues();
		initializeValues();
	}
	public void addFournisseur(){
		BizFournisseur selectedFournisseur	= fournisseurAutoSuggest;
		fournisseurList.add(selectedFournisseur);

		Map<String,Object> temp	= new HashMap<String,Object>();
		temp.put("fournisseur",selectedFournisseur);
		temp.put("newone","non");
		totalFournisseur++;
		temp.put("classementAcheteur"	,totalFournisseur);
		temp.put("classementMetier"		,totalFournisseur);
		temp.put("classementDefinitif"	,totalFournisseur);
		fournisseurListHash.add(temp);

		persisteValues();
		initializeValues();
	}
	public void handleUploadedFiles(FileUploadEvent event)throws IOException {
		String fileName 	= event.getFile().getFileName();
		InputStream input 	= event.getFile().getInputstream();
		IwUpload iwUpload	= new IwUpload();
		String generatedRef 	= RandomStringUtils.randomAlphanumeric(10);
		String fournisseurUpload = (String) event.getComponent().getAttributes().get("fournisseurUpload");

		OutputStream output = new FileOutputStream(new File(loggedUserDestDirPath+"/", generatedRef+"_"+fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(loggedUserDestDirPath+generatedRef+"_"+fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList	= new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			if("offreFinancier".equals(fournisseurUpload )){
				currentFournisseur.setOffreFinanciere(iwUpload);
			}
			if("offreTechnique".equals(fournisseurUpload )){
				currentFournisseur.setOffreTechnique(iwUpload);
			}
			if("specificationFonctionnelles".equals(fournisseurUpload )){
				currentFournisseur.setSpecificationFonctionnelles(iwUpload);
			}

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	public void onItemSelect(SelectEvent event) {
		Object selected = event.getObject();
	}
	public void updateStatus(String status){
		initializeValues();
		if (status.toLowerCase().equals("create")) {
			create = true;
			delete = false;
			update = false;
			importer = false;
		}
		if (status.toLowerCase().equals("update")) {
			update = true;
			create = false;
			delete = false;
			importer = false;
		}
		if (status.toLowerCase().equals("delete")) {
			delete = true;
			create = false;
			update = false;
			importer = false;
		}
		if (status.toLowerCase().equals("import")) {
			delete = false;
			create = false;
			update = false;
			importer = true;
		}
	}
	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();
		int rowIndex 	= event.getRowIndex();
		String rowKey 	= event.getRowKey();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if(newValue != null && !newValue.equals(oldValue)) {
			DataTable s 			= (DataTable) event.getSource();

			//ReponseCourrierEntity d = (ReponseCourrierEntity) s.getRowData();
		}
	}
	public void onRowCancel(RowEditEvent event) {

		Map<String,Object> updatedFournisseur 	= ((Map<String,Object>) event.getObject());
		int theIndex 							= fournisseurListHash.indexOf(updatedFournisseur);
		for(int i =0 ; i< fournisseurListHash.size(); i++){
			if(i != theIndex){
				String updatedClassementAchat				= updatedFournisseur.get("classementAcheteur").toString();
				String fournisseurListHashClassementAchat	= fournisseurListHash.get(i).get("classementAcheteur").toString();

				String updatedClassementMetier				= updatedFournisseur.get("classementMetier").toString();
				String fournisseurListHashClassementMetier	= fournisseurListHash.get(i).get("classementMetier").toString();

				String updatedClassementDefinitif				= updatedFournisseur.get("classementDefinitif").toString();
				String fournisseurListHashClassementDefinitif	= fournisseurListHash.get(i).get("classementDefinitif").toString();

				if( updatedClassementAchat.equals(fournisseurListHashClassementAchat)){
					fournisseurListHash.get(i).put("classementAcheteur",oldFournisseurListHash.get(theIndex).get("classementAcheteur"));
				}
				if( updatedClassementMetier.equals(fournisseurListHashClassementMetier)){
					fournisseurListHash.get(i).put("classementMetier",oldFournisseurListHash.get(theIndex).get("classementMetier"));
				}
				if( updatedClassementDefinitif.equals(fournisseurListHashClassementDefinitif)){
					fournisseurListHash.get(i).put("classementDefinitif",oldFournisseurListHash.get(theIndex).get("classementDefinitif"));
				}
			}
		}

		persisteValues();
		initializeValues();
	}

	public void downloadFile(IwFile iwFile) {
		File fileItem		= new File(iwFile.getIwPath());
		InputStream file 	= null;
		try {
			file = new FileInputStream(fileItem.getPath());
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType(fileItem.toURL().openConnection().getContentType());
			response.addHeader("Content-disposition", "attachment; filename=" + fileItem.getName());
			OutputStream servletOutputStream = response.getOutputStream();
			IOUtils.copyLarge(file, servletOutputStream);
		} catch (Exception ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public String separerMilliers(Number n){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		String s0 = nf.format(n);
		return s0;
	}
	public Map<String,Object> getVarValuesReport(){
		BizFournisseur fournisseur=new BizFournisseur();
		BizFournisseur fournisseur1=new BizFournisseur();
		Map<String, Object> mapReport = new HashMap<String, Object>();
		if(fournisseurListHash!= null && !fournisseurListHash.isEmpty() ){
			for (Map<String,Object> item :fournisseurListHash){
				if (Integer.parseInt(item.get("classementDefinitif").toString())==1){
					fournisseur=(BizFournisseur) item.get("fournisseur");
					if (fournisseur!=null)fournisseur1=fournisseur;
				}

			}

		}
		if (fournisseur1!=null){
			mapReport.put("demande_achat_fournisseur", fournisseur.getNomHabituel());
			mapReport.put("adresseFournisseur",fournisseur.getAdresse());

		}

		return mapReport;
	}
	public List<BizFournisseur> getFournisseurList() {return fournisseurList;	}
	public void setFournisseurList(List<BizFournisseur> fournisseurList) {	this.fournisseurList = fournisseurList;	}
	public List<BizFournisseur> getAllFournisseur() {		return allFournisseur;	}
	public void setAllFournisseur(List<BizFournisseur> allFournisseur) {		this.allFournisseur = allFournisseur;	}
	public BizFournisseur getCurrentFournisseur() {		return currentFournisseur;	}
	public void setCurrentFournisseur(BizFournisseur currentFournisseur) {		this.currentFournisseur = currentFournisseur;	}
	public boolean isCreate() {		return create;	}
	public void setCreate(boolean create) {		this.create = create;	}
	public boolean isUpdate() {		return update;	}
	public void setUpdate(boolean update) {		this.update = update;	}
	public boolean isDelete() {		return delete;	}
	public void setDelete(boolean delete) {		this.delete = delete;	}
	public boolean isImporter() {		return importer;	}
	public void setImporter(boolean importer) {		this.importer = importer;	}
	public BizFournisseur getFournisseurAutoSuggest() {		return fournisseurAutoSuggest;	}
	public void setFournisseurAutoSuggest(BizFournisseur fournisseurAutoSuggest) {this.fournisseurAutoSuggest = fournisseurAutoSuggest;}
	public List<Map<String, Object>> getFournisseurListHash() {		return fournisseurListHash;	}
	public void setFournisseurListHash(List<Map<String, Object>> fournisseurListHash) {		this.fournisseurListHash = fournisseurListHash;	}
	public boolean isUbdateThisFournisseur() {		return ubdateThisFournisseur;	}
	public void setUbdateThisFournisseur(boolean ubdateThisFournisseur) {		this.ubdateThisFournisseur = ubdateThisFournisseur;	}
	public UploadedFile getOffreTechniqueFile() {		return offreTechniqueFile;	}
	public void setOffreTechniqueFile(UploadedFile offreTechniqueFile) {		this.offreTechniqueFile = offreTechniqueFile;	}
	public int getTotalFournisseur() {		return totalFournisseur;	}
	public void setTotalFournisseur(int totalFournisseur) {		this.totalFournisseur = totalFournisseur;	}
	public List<Integer> getTotalFournisseurArray() {		return totalFournisseurArray;	}
	public void setTotalFournisseurArray(List<Integer> totalFournisseurArray) {		this.totalFournisseurArray = totalFournisseurArray;	}
	public boolean isMetierRole() {		return metierRole;	}
	public void setMetierRole(boolean metierRole) {		this.metierRole = metierRole;	}
	public boolean isAcheteurRole() {		return acheteurRole;	}
	public void setAcheteurRole(boolean acheteurRole) {		this.acheteurRole = acheteurRole;	}
	public boolean isMetierAcheteurRole() {		return metierAcheteurRole;	}
	public void setMetierAcheteurRole(boolean metierAcheteurRole) {		this.metierAcheteurRole = metierAcheteurRole;	}

}
