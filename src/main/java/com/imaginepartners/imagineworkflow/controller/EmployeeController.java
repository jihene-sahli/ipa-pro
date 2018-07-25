package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhPosteOccupe;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("view")
public class EmployeeController implements Serializable {
	List<RhCollaborateur> listCollaborateurs;
	RhCollaborateur rhCollaborateur;
	private String destDirPath;
	private List<RhPosteOccupe> postes;
	private List<RhPosteOccupe> removedPostes;
	private List<RhFormation> formations;
	private List<RhFormation> removedformations;
	private RhFormation currentObj;
	private Integer posteId;
	private RhPosteOccupe posteOccupe;
	@Autowired
	private BusinessService businessService;

	@PostConstruct
	public void init(){


		String collaboratorKey = FacesUtil.getUrlParam("collaborator");
		if(collaboratorKey != null) {
			loadCollaborateur(collaboratorKey);
			return;
		}
		loadCollaborateursList();
	}



	private void loadCollaborateursList(){
		listCollaborateurs=(List<RhCollaborateur>)(List<?>)businessService.getEntiteList(RhCollaborateur.class.getName());
		if(listCollaborateurs == null)
		listCollaborateurs=new ArrayList<>();
	}
	private void loadCollaborateur(String actIdUser){
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "collaborateur_pj";
		File distDir = new File(destDirPath);

		if (!distDir.exists()) {
			distDir.mkdirs();
		}

		rhCollaborateur=businessService.getRhCollaborateurByActIdUser(actIdUser);
		loadFormationsForCollaborator();
		loadPostesForCollaborator();

	}

	private void loadFormationsForCollaborator(){
		formations = new ArrayList<>();
		removedformations = new ArrayList<>();
		formations.addAll(rhCollaborateur.getListRhFormations());
	}
	private void loadPostesForCollaborator(){
		postes = new ArrayList<>();
		removedPostes = new ArrayList<>();
		postes = businessService.getRhPosteOccupeList();
		posteOccupe = rhCollaborateur.getIdPosteOccupe();
		if(posteOccupe !=null)
		posteId=posteOccupe.getIdPosteOccupe();
	}


	public void handleUploadedPromesse(FileUploadEvent event) throws IOException {
		String fileName = event.getFile().getFileName();
		InputStream input = event.getFile().getInputstream();
		IwUpload iwUpload = new IwUpload();
		String generatedRef = RandomStringUtils.randomAlphanumeric(10);

		OutputStream output = new FileOutputStream(new File(destDirPath + "/", generatedRef + "_" + fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(destDirPath + "/" + generatedRef + "_" + fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList = new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			rhCollaborateur.setPromesseEmbauche(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public void handleUploadedFichePoste(FileUploadEvent event) throws IOException {
		String fileName = event.getFile().getFileName();
		InputStream input = event.getFile().getInputstream();
		IwUpload iwUpload = new IwUpload();
		String generatedRef = RandomStringUtils.randomAlphanumeric(10);

		OutputStream output = new FileOutputStream(new File(destDirPath + "/", generatedRef + "_" + fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(destDirPath + "/" + generatedRef + "_" + fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList = new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			rhCollaborateur.setFichePoste(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public void addRhFormation() {
		RhFormation formation = new RhFormation();
		formation.setCollaborator(rhCollaborateur);
		formations.add(formation);
	}
	public void deleteRhFormation(int rowId) {
		removedformations.add(formations.get(rowId));
		formations.remove(rowId);
	}
	public void selectCurrentObj(int rowId) {

		currentObj = formations.get(rowId);
	}
	public void saveCollaborator() {

		if (rhCollaborateur != null) {
			if (!formations.isEmpty()) {
				rhCollaborateur.setListRhFormations(formations);
			}
			if (posteOccupe != null) {
				businessService.saveEntite(posteOccupe);
				rhCollaborateur.setIdPosteOccupe(posteOccupe);
			}
			businessService.saveRhCollaborateur(rhCollaborateur);
			posteOccupe.setCollaborateur(rhCollaborateur);
			businessService.saveEntite(posteOccupe);

			FacesUtil.setAjaxInfoMessage("Collaborateur est ajouté avec succés ");
		}
	}

	private void removeListEntities(List listObjs){
    for(Object obj: listObjs){
    	businessService.removeEntite(obj);
	}
	}
	public void handleUploadedAttestation(FileUploadEvent event) throws IOException {
		String fileName = event.getFile().getFileName();
		InputStream input = event.getFile().getInputstream();
		IwUpload iwUpload = new IwUpload();
		String generatedRef = RandomStringUtils.randomAlphanumeric(10);

		OutputStream output = new FileOutputStream(new File(destDirPath + "/", generatedRef + "_" + fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(destDirPath + "/" + generatedRef + "_" + fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList = new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			currentObj.setAttestation(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public void downloadFile(IwFile iwFile) {
		File fileItem = new File(iwFile.getIwPath());
		InputStream file = null;
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

	public void removeFile() {

		currentObj.setAttestation(null);
	}

	// Getters and Setters

	public List<RhCollaborateur> getListCollaborateurs() {
		return listCollaborateurs;
	}

	public RhCollaborateur getRhCollaborateur() {
		return rhCollaborateur;
	}

	public List<RhPosteOccupe> getPostes() {
		return postes;
	}
	public List<RhFormation> getFormations() {
		return formations;
	}

	public RhFormation getCurrentObj() {
		return currentObj;
	}

	public void setCurrentObj(RhFormation currentObj) {
		this.currentObj = currentObj;
	}

	public Integer getPosteId() {
		return posteId;
	}

	public void setPosteId(Integer posteId) {
		this.posteId = posteId;
	}

	public RhPosteOccupe getPosteOccupe() {
		return posteOccupe;
	}

	public void setPosteOccupe(RhPosteOccupe posteOccupe) {
		this.posteOccupe = posteOccupe;
	}

	public void onChangePoste() {
		if (posteId != null) {
			RhPosteOccupe poste = businessService.getRhPosteOccupeById(posteId);
			posteOccupe = new RhPosteOccupe();
			posteOccupe.setDescription(poste.getDescription());
		}
	}

	public void handleUploadedContrat(FileUploadEvent event) throws IOException {
		String fileName = event.getFile().getFileName();
		InputStream input = event.getFile().getInputstream();
		IwUpload iwUpload = new IwUpload();
		String generatedRef = RandomStringUtils.randomAlphanumeric(10);

		OutputStream output = new FileOutputStream(new File(destDirPath + "/", generatedRef + "_" + fileName));
		try {
			IOUtils.copy(input, output);
			IwFile iwFile = new IwFile();
			iwFile.setIwPath(destDirPath + "/" + generatedRef + "_" + fileName);
			iwFile.setIwSize(event.getFile().getSize());
			iwFile.setIwMime(event.getFile().getContentType());
			iwFile.setIwName(event.getFile().getFileName());
			iwFile.setIwUpload(iwUpload);
			List<IwFile> iwFileList = new ArrayList<IwFile>();
			iwFileList.add(iwFile);
			iwUpload.setIwFileList(iwFileList);
			businessService.saveEntite(iwUpload);
			businessService.saveEntite(iwFile);
			rhCollaborateur.setContrat(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
}
