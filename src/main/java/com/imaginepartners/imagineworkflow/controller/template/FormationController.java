package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.controller.FileController;
import com.imaginepartners.imagineworkflow.controller.HierarchyController;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.rh.AldFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhFormation;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Med on 06/09/2017.
 */
public class FormationController extends FormTemplate implements Serializable {

	@Autowired
	private BusinessService businessService;
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	HierarchyController hierarchyController;
	private List <AldFormation> formationsList;
	private List <AldFormation> removedFormationsList;
	private String destDirPath;
	private AldFormation currentObj;
	private String taskId;
	private TaskInfo currentTask;
	private int taskNumber;
	private List <AldFormation> allFormationsList;
	private List <User>  collaborateurs;
    private List<AldFormation>  formationsEnAttente;
	private int  attestation;
	private IwUpload CurrentAttestation;
	private  boolean taskIsT08 ;
	Map<String,Object> varsMap;
	public FormationController() {

	}

	@Override
	public void init() {
if(StringUtils.isNotBlank(FacesUtil.getUrlParam("tacheHist"))){
	taskId=	FacesUtil.getUrlParam("tacheHist");
	currentTask=activitiService.getTaskHistById(taskId);
	taskNumber = Integer.parseInt(currentTask.getName().substring(1,3));
	varsMap=currentTask.getProcessVariables();
}else
{
	taskId 			= StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
	currentTask 	= activitiService.getTask(taskId);
	taskNumber = Integer.parseInt(currentTask.getName().substring(1,3));
	varsMap=activitiService.getTaskVariables(taskId,null);
}

		if  (taskNumber == 5 &&  varsMap.get("formation")!=null )
		{
			formationsList= new ArrayList<>();
             formationsList.add( (AldFormation) varsMap.get("formation"));
		}
		else if  (taskNumber == 8 &&  varsMap.get("rejectedList")!=null )
		{
			formationsList= (ArrayList<AldFormation>) varsMap.get("rejectedList");
		}
		else if(varValues.get("formationsList") != null && taskNumber >1) {


				formationsList= (ArrayList<AldFormation>) varValues.get("formationsList");


		}else{
			formationsList = new ArrayList<AldFormation>();
		}
		getCollaboraterList();
		removedFormationsList= new ArrayList<AldFormation>();
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "formation_template";

		File distDir = new File(destDirPath);

		if (!distDir.exists()) {
			distDir.mkdirs();
		}

		taskIsT08 = false;
		if(currentTask.getName().contains("Rensienger les dates de formations en atentes")){
			taskIsT08 = true;
		}
		if(currentTask.getName().equals("T06 - Evaluation a chaud par le collaborateur") || currentTask.getName().equals("T07 - N+1 évaluation a froid du collaborateur")){
			if(currentTask.getExecutionId()!= null && !currentTask.getExecutionId().isEmpty()){
				formationsList= (ArrayList<AldFormation>) varsMap.get("formationsListFor-"+currentTask.getExecutionId());
			}
		}
	}

	@Override
	public void putVars() {

	}
	public void addFormation() {

		formationsList.add(new AldFormation());
	}

	public void deleteFormation(int rowId) {
		removedFormationsList.add((AldFormation) formationsList.get(rowId));
		formationsList.remove(rowId);
	}

	@Override
	public Map<String, Object> getVarValues() {
		if(!formationsList.isEmpty()){
			businessService.saveEntityList(formationsList);

			//sba
			saveCurrentValidRhFormation(formationsList);
		}

		if(!removedFormationsList.isEmpty()){
			for(AldFormation var : removedFormationsList){
				businessService.removeEntite(var);
			}
		}
if (varValues.get("formationsList") !=null && checkTask(1,taskNumber)){
			formationsList.addAll((Collection<? extends AldFormation>) varValues.get("formationsList"));
		}

		if(currentTask.getName().equals("T05 - RH --> Upload de l'attestation") || currentTask.getName().equals("T06 - Evaluation a chaud par le collaborateur")){
			if(currentTask.getExecutionId()!= null && !currentTask.getExecutionId().isEmpty()){
				varValues.put("formationsListFor-"+currentTask.getExecutionId(), formationsList);
			}
		}else

		varValues.put("formationsList", formationsList);
		return varValues;

	}

	public void updateFormationValidation(AldFormation formation) {

		if (formation.getRejected() != null && formation.getRejected()) {
			formation.setRejected(true);
		}
		else {
			formation.setRejected(false);
		}
	}

	public void selectCurrentObj(int rowId, int att ){

		currentObj = formationsList.get(rowId);
		attestation=att;
		if (attestation==1)
		{
			if (currentObj.getAttestation()!=null)
				setCurrentAttestation(currentObj.getAttestation());
			else setCurrentAttestation(null);
		}
		else if (attestation==2)
		{
			if (currentObj.getAttestationChaud()!=null)
				setCurrentAttestation(currentObj.getAttestationChaud());
			else setCurrentAttestation(null);

		}

		else if (attestation==3)
		{
			if (currentObj.getAttestationFroid()!=null)
				setCurrentAttestation(currentObj.getAttestationFroid());
			else setCurrentAttestation(null);

		}

	}

	public void handleUploadedFile(FileUploadEvent event)throws IOException {
		String fileName 	= event.getFile().getFileName();
		InputStream input 	= event.getFile().getInputstream();
		IwUpload iwUpload	= new IwUpload();
		String generatedRef 	= RandomStringUtils.randomAlphanumeric(10);

		OutputStream output = new FileOutputStream(new File(destDirPath+"/", generatedRef+"_"+fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(destDirPath+"/"+generatedRef+"_"+fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList	= new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			if (attestation==1)
			{
				currentObj.setAttestation(iwUpload);
				setCurrentAttestation(currentObj.getAttestation());
			}
			else if (attestation==2)
			{currentObj.setAttestationChaud(iwUpload);
				setCurrentAttestation(currentObj.getAttestationChaud());
			}
			else if (attestation==3)
			{currentObj.setAttestationFroid(iwUpload);
				setCurrentAttestation(currentObj.getAttestationFroid());
			}

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	public Boolean greaterThan(int a, int b){
		if(a > b)
			return true;
		return false;
	}

	public void downloadFile(IwFile iwFile) {
		File fileItem		= new File(iwFile.getIwPath());
		InputStream file 	= null;
		try {
			file = new FileInputStream(fileItem.getPath());
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType(fileItem.toURL().openConnection().getContentType());
			response.addHeader("Content-disposition", "attachment; filename=" + generateRealFileName(fileItem.getName()));
			OutputStream servletOutputStream = response.getOutputStream();
			IOUtils.copyLarge(file, servletOutputStream);
		} catch (Exception ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	private String generateRealFileName(String fileName){
try {
	return fileName.substring(fileName.indexOf("_")+1, fileName.length());
}catch (Exception e){}
		return "";
	}

	public void removeFile(){

		currentObj.setAttestation(null);
	}

	public Boolean checkTask(int a, int b){
		if(a == b)
			return true;
		return false;
	}

	private void getCollaboraterList(){
		Group myTeam=null;
		String idUser=userService.getLoggedInUser().getId();
		if (idUser !=null && checkTask(taskNumber,1)){
			List<Group> groups=hierarchyController.getGroupList();
			for (Group group:groups){
				if (businessService.getGroupDetails(group.getId()).getIwResponsible().equals(idUser))
					myTeam=group;
			}
			if (myTeam!=null)
			collaborateurs=	activitiService.getMemerbs(myTeam.getId());
		}
	}

	public String ShowOrHideRowByCollaborator(AldFormation formation){
		String collaborateur=formation.getCollaborateur();
		String responsable=businessService.getResponsibleForUser(collaborateur);
		String demandeur= (String) varsMap.get("collaborateur");

		if ( taskNumber ==8|| taskNumber < 6 ||currentTask.getAssignee().equals(collaborateur)  ||
		(collaborateur.equals(demandeur)&& currentTask.getAssignee().equals(responsable))){
           return "";
		}else{
			return "ui-helper-hidden";
		}
	}

	public List<AldFormation> getFormationsList() {
		return formationsList;
	}

	public void setFormationsList(List<AldFormation> formationsList) {
		this.formationsList = formationsList;
	}

	public List<AldFormation> getRemovedFormationsList() {
		return removedFormationsList;
	}

	public void setRemovedFormationsList(List<AldFormation> removedFormationsList) {
		this.removedFormationsList = removedFormationsList;
	}

	public String getDestDirPath() {
		return destDirPath;
	}

	public void setDestDirPath(String destDirPath) {
		this.destDirPath = destDirPath;
	}

	public AldFormation getCurrentObj() {
		return currentObj;
	}

	public void setCurrentObj(AldFormation currentObj) {
		this.currentObj = currentObj;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskInfo getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(TaskInfo currentTask) {
		this.currentTask = currentTask;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public List<AldFormation> getAllFormationsList() {
		return allFormationsList;
	}

	public void setAllFormationsList(List<AldFormation> allFormationsList) {
		this.allFormationsList = allFormationsList;
	}

	public List<User> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(List<User> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}

	public List<AldFormation> getFormationsEnAttente() {
		return formationsEnAttente;
	}

	public void setFormationsEnAttente(List<AldFormation> formationsEnAttente) {
		this.formationsEnAttente = formationsEnAttente;
	}
	public int getAttestation() {
		return attestation;
	}

	public void setAttestation(int attestation) {
		this.attestation = attestation;
	}

	public IwUpload getCurrentAttestation() {
		return CurrentAttestation;
	}

	public void setCurrentAttestation(IwUpload currentAttestation) {
		CurrentAttestation = currentAttestation;
	}

	public boolean isTaskIsT08() {
		return taskIsT08;
	}

	public void setTaskIsT08(boolean taskIsT08) {
		this.taskIsT08 = taskIsT08;
	}

	/**
	 * Save valides Formationss
	 * @param list
	 */
	public void saveCurrentValidRhFormation(List<AldFormation> list){

		for(AldFormation f : list){
			if(f.getRejected()== null || !f.getRejected() || f.getDateFormation() == null)
				continue;
			RhFormation rhFormation=businessService.getRhFormationByAldFormationId(f.getFormationId());
			if(rhFormation == null)
				rhFormation = new RhFormation();
			rhFormation.setAldFormationId(f.getFormationId());
			rhFormation.setObjectifFormation(f.getObjectifFormation());
			rhFormation.setIntituleFormation(f.getIntituleFormation());
			rhFormation.setDateFormation(f.getDateFormation());
			rhFormation.setCollaborator(businessService.getCollaborateurByActIdUser(f.getCollaborateur()));
			rhFormation.setAttestation(f.getAttestation());
			businessService.saveObjectAsEntity(rhFormation);
			System.out.println();
		}


	}
}
