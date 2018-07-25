package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.GenerateDocService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.event.AjaxBehaviorEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 11/05/2017.
 */
public class ImageController extends FormTemplate implements Serializable {

	@Autowired
	private GenerateDocService generateDocService;
	private String imgPath;
	private File imgFolder;
	private String selectedDoc;
	private String documentPath;
	private String toFind;
    private List <String> imagesPlaceholders;

	public ImageController() {

	}

	@Override
	public void init() {

		imgFolder = new File(AppConstants.REPORTS_IMAGES_FOLDER);
		if (!imgFolder.exists()) {
			imgFolder.mkdirs();
		}
	}
	public void handleFileUpload(FileUploadEvent e) throws IOException {

		copyFile(e.getFile().getFileName(), e.getFile().getInputstream());
		imgPath= AppConstants.REPORTS_IMAGES_FOLDER + e.getFile().getFileName();

	}

	public void copyFile(String fileName, InputStream in) {
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(AppConstants.REPORTS_IMAGES_FOLDER  + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void onDocSelect( AjaxBehaviorEvent event) throws Exception {

		documentPath=AppConstants.REPORTS_PATH + selectedDoc;
		imagesPlaceholders = generateDocService.getPlaceholderImageList(documentPath);

	}

	public void doAddImageToDocx() throws Exception {

			generateDocService.addImageToDocx(documentPath, imgPath, toFind);

		FacesUtil.setAjaxInfoMessage("Une image est ajoutée dans le document "+selectedDoc);
	}
	@Override
	public void putVars() {

	}

	public GenerateDocService getGenerateDocService() {
		return generateDocService;
	}

	public void setGenerateDocService(GenerateDocService generateDocService) {
		this.generateDocService = generateDocService;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public File getImgFolder() {
		return imgFolder;
	}

	public void setImgFolder(File imgFolder) {
		this.imgFolder = imgFolder;
	}

	public String getSelectedDoc() {
		return selectedDoc;
	}

	public void setSelectedDoc(String selectedDoc) {
		this.selectedDoc = selectedDoc;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getToFind() {
		return toFind;
	}

	public void setToFind(String toFind) {
		this.toFind = toFind;
	}

	public List<String> getImagesPlaceholders() {
		return imagesPlaceholders;
	}

	public void setImagesPlaceholders(List<String> imagesPlaceholders) {
		this.imagesPlaceholders = imagesPlaceholders;
	}
}
