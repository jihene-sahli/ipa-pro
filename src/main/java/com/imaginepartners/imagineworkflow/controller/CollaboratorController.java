package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhPosteOccupe;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.User;
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
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import org.activiti.engine.identity.Group;
@Controller
@Scope("view")
public class CollaboratorController implements Serializable {

	@Autowired
	ActivitiService activitiService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private UserService userService;
	@Autowired
	HierarchyController hierarchyController;
	private List<User> users;
	private User user;
	private RhCollaborateur collaborateur;
	private String userId;
	private String destDirPath;
	private List<RhFormation> formations;
	private List<RhFormation> removedformations;
	private RhFormation currentObj;
	private List<RhPosteOccupe> postes;
	private List<RhPosteOccupe> removedPostes;
	private Integer posteId;
	private RhPosteOccupe posteOccupe;
	private Boolean showOrHide = false;
	private User loggedInUser;
	private Group myTeam;
	private RhCollaborateur connectedCollaborateur;
	public CollaboratorController() {
	}

	@PostConstruct
	public void init() {
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "collaborateur_pj";
		File distDir = new File(destDirPath);

		if (!distDir.exists()) {
			distDir.mkdirs();
		}
		loggedInUser=userService.getLoggedInUser();
		connectedCollaborateur=businessService.getRhCollaborateurByActIdUser(loggedInUser.getId());
		if (connectedCollaborateur==null){
			users = activitiService.getUserList();
		}
		else{
			List<Group> groups=hierarchyController.getGroupList();
			for (Group group:groups){
				if (businessService.getGroupDetails(group.getId()).getIwResponsible().equals(connectedCollaborateur.getActIdUser()))
					myTeam=group;
			}
			if (myTeam!=null)
				users=activitiService.getMemerbs(myTeam.getId());

		    }
		postes = businessService.getRhPosteOccupeList();
		initCollections();
	}

	private void initCollections() {
		formations = new ArrayList<RhFormation>();
		removedformations = new ArrayList<RhFormation>();
	}

	public void onChangeCollaborator() {
		initCollections();
		user = activitiService.getUser(userId);
		RhCollaborateur rhCollaborateur = businessService.getCollaborateurByActIdUser(userId);

		if (rhCollaborateur != null) {
			collaborateur = rhCollaborateur;
			formations.addAll(rhCollaborateur.getListRhFormations());
			posteOccupe = rhCollaborateur.getIdPosteOccupe();
		} else {
			if (user != null) {
				collaborateur = new RhCollaborateur();
				collaborateur.setActIdUser(user.getId());
				collaborateur.setEmail(user.getEmail());
				collaborateur.setNom(user.getLastName());
				collaborateur.setPrenom(user.getFirstName());
				posteOccupe= new RhPosteOccupe();
			}
		}
	}

	public void onChangePoste() {
		if (posteId != null) {
			RhPosteOccupe poste = businessService.getRhPosteOccupeById(posteId);
			posteOccupe = new RhPosteOccupe();
			posteOccupe.setDescription(poste.getDescription());

			showOrHide=true;
		}
	}

	public void saveCollaborator() {

		if (collaborateur != null) {
			if (!formations.isEmpty()) {
				collaborateur.setListRhFormations(formations);
			}
			if (posteOccupe != null) {
				businessService.saveEntite(posteOccupe);
				collaborateur.setIdPosteOccupe(posteOccupe);
			}
			businessService.saveRhCollaborateur(collaborateur);
			posteOccupe.setCollaborateur(collaborateur);
			businessService.saveEntite(posteOccupe);

			FacesUtil.setAjaxInfoMessage("Collaborateur est ajouté avec succés ");
		}
	}

	public void addRhFormation() {
		RhFormation formation = new RhFormation();
		formation.setCollaborator(collaborateur);
		formations.add(formation);
	}

	public void deleteRhFormation(int rowId) {
		removedformations.add((RhFormation) formations.get(rowId));
		formations.remove(rowId);
	}

	public void addRhPoste() {
		RhPosteOccupe posteOccupe = new RhPosteOccupe();
		posteOccupe.setCollaborateur(collaborateur);
		postes.add(posteOccupe);
	}

	public void deleteRhPoste(int rowId) {
		removedPostes.add((RhPosteOccupe) postes.get(rowId));
		postes.remove(rowId);
	}

	public void selectCurrentObj(int rowId) {

		currentObj = formations.get(rowId);
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
			collaborateur.setPromesseEmbauche(iwUpload);

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
			collaborateur.setFichePoste(iwUpload);

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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RhCollaborateur getCollaborateur() {
		return collaborateur;
	}

	public void setCollaborateur(RhCollaborateur collaborateur) {
		this.collaborateur = collaborateur;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<RhFormation> getFormations() {
		return formations;
	}

	public void setFormations(List<RhFormation> formations) {
		this.formations = formations;
	}

	public List<RhFormation> getRemovedformations() {
		return removedformations;
	}

	public void setRemovedformations(List<RhFormation> removedformations) {
		this.removedformations = removedformations;
	}

	public List<RhPosteOccupe> getPostes() {
		return postes;
	}

	public void setPostes(List<RhPosteOccupe> postes) {
		this.postes = postes;
	}

	public List<RhPosteOccupe> getRemovedPostes() {
		return removedPostes;
	}

	public void setRemovedPostes(List<RhPosteOccupe> removedPostes) {
		this.removedPostes = removedPostes;
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

	public Boolean getShowOrHide() {
		return showOrHide;
	}

	public void setShowOrHide(Boolean showOrhide) {
		this.showOrHide = showOrhide;
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
			collaborateur.setContrat(iwUpload);

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
}
