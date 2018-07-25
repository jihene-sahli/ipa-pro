package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.models.business.BizFournisseur;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller("fournisseurController")
public class FournisseurController {

	@Autowired
	BusinessService businessService;

	BizFournisseur selectedFournisseur =null;
	private String destDirPath;
	private List<BizFournisseur> fournisseurList;

	@PostConstruct
	public void init(){
		destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ "formation_template";

		File distDir = new File(destDirPath);

		if (!distDir.exists()) {
			distDir.mkdirs();
		}
	}

	public void createFournisseur(){
		selectedFournisseur = new BizFournisseur();
	}

	public void persistFournisseur(){
		businessService.saveEntite(selectedFournisseur);
	}

	public void deleteFournisseur(){
     businessService.removeEntite(selectedFournisseur);
	}

	public void selectFournisseur(int rowId){
    selectedFournisseur = fournisseurList.get(rowId);
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
			if("offreFinancier".equals(event.getComponent().getAttributes().get("fournisseurUpload") )){
				selectedFournisseur.setOffreFinanciere(iwUpload);
			}
			if("offreTechnique".equals(event.getComponent().getAttributes().get("fournisseurUpload") )){
				selectedFournisseur.setOffreTechnique(iwUpload);
			}
			if("specificationFonctionnelles".equals(event.getComponent().getAttributes().get("fournisseurUpload") )){
				selectedFournisseur.setSpecificationFonctionnelles(iwUpload);
			}

		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	public void removeFile(String operation){
		if("specificationFonctionnelles".equals(operation))
		selectedFournisseur.setSpecificationFonctionnelles(null);
		if("offreFinancier".equals(operation))
			selectedFournisseur.setOffreFinanciere(null);
		if("offreTechnique".equals(operation))
			selectedFournisseur.setOffreFinanciere(null);
	}

	public List<BizFournisseur> getListfournisseurs() {
		fournisseurList=(List<BizFournisseur>)(List<?>)businessService.getEntiteList(BizFournisseur.class.getName());
		return fournisseurList;
	}

	public BizFournisseur getSelectedFournisseur() {
		return selectedFournisseur;
	}

	public void setSelectedFournisseur(BizFournisseur selectedFournisseur) {
		this.selectedFournisseur = selectedFournisseur;
	}
}
