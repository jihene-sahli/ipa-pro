package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.controller.FileController;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.rh.RhCandidat;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafik on 03/09/17.
 */
public class Candidats extends FormTemplate implements Serializable{

	private ArrayList<RhCandidat> rhCandidatList;
	private RhCandidat currentObj;
	private String taskId;
	private Task currentTask;
	private int taskNumber;
	private ProcessInstance procInst;
	private RhCollaborateur currentCollaborateur ;
	private String destDirPath;

	private final SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyy");

	@Override
	public void putVars() {

	}

	@Override
	public void init() {

		if(varValues.get("rhCandidatList") != null)
			rhCandidatList= (ArrayList<RhCandidat>)varValues.get("rhCandidatList");
		else
			rhCandidatList	= new ArrayList<RhCandidat>();

		taskId 			= StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
		currentTask 	= activitiService.getTask(taskId);
		taskNumber = Integer.parseInt(currentTask.getName().substring(1,3));
		procInst 		= activitiService.getProcessInstanceById(currentTask.getProcessInstanceId());
		//currentCollaborateur	= businessService.getRhCollaborateurByActIdUser(userService.getLoggedInUser().getId());
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "candidat_template";
		File distDir = new File(destDirPath);
		if (!distDir.exists()) {
			distDir.mkdirs();
		}

	}

	public ArrayList<RhCandidat> getRhCandidatList() {
		return rhCandidatList;
	}

	public void setRhCandidatList(ArrayList<RhCandidat> rhCandidatList) {
		this.rhCandidatList = rhCandidatList;
	}

	public RhCandidat getCurrentObj() {
		return currentObj;
	}

	public void setCurrentObj(RhCandidat currentObj) {
		this.currentObj = currentObj;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public void addCandidat() {

		currentObj= new RhCandidat();
		currentObj.setClassementCandidat(1);
		rhCandidatList.add(currentObj);
		varValues.put("rhCandidatList", rhCandidatList);
	}

	public void deleteCandidat(int rowId) {

		rhCandidatList.remove(rowId);
		varValues.put("rhCandidatList", rhCandidatList);
	}

	public RhCollaborateur getCurrentCollaborateur() {
		return currentCollaborateur;
	}

	public void setCurrentCollaborateur(RhCollaborateur currentCollaborateur) {
		this.currentCollaborateur = currentCollaborateur;
	}

	public void selectCurrentObj(int rowId ){
		currentObj = rhCandidatList.get(rowId);
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
			currentObj.setCv(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
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

	public void removeFile(){
		currentObj.setCv(null);
	}

	public Boolean greaterThan(int a, int b){
		if(a > b)
			return true;
		return false;
	}
}
