package com.imaginepartners.imagineworkflow.controller;

/**
 * Created by wafa on 12/07/17.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import org.docx4j.samples.AbstractSample;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


@Controller("manualController")
@Scope("view")
public class ManualController extends AbstractSample implements Serializable{
	private UploadedFile doc;
	private String inputfilepath;
	private String path;
	private List<String> docList;
	private File repertoire;
	private ObjectMapper mapper;

	@PostConstruct
	public void init() {

		path= AppConstants.DEFAULT_REPORTS_PATH;
		mapper = new ObjectMapper();
		repertoire = new File(path);
		if (!repertoire.exists()) {
			repertoire.mkdirs();
			doGetDocList();
		}else {
			doGetDocList();
		}

	}

	// download file
	public void download() throws IOException {
		FacesContext fc=FacesContext.getCurrentInstance();
		ExternalContext ec=fc.getExternalContext();
		File file = new File(System.getProperty("user.dir")+"/webapps/ROOT/report/manuel_d'utilisation.docx");
		String fileName = file.getName();
		String contentType = ec.getMimeType(fileName);
		int contentLength = (int) file.length();
		ec.responseReset();
		ec.setResponseContentType("application/docx");
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		OutputStream output = ec.getResponseOutputStream();
		Files.copy(file.toPath(), output);

		fc.responseComplete();
	}
	public void handleFileUpload(FileUploadEvent e) throws IOException {

		// Get uploaded file from the FileUploadEvent
		this.doc = e.getFile();
		copyFile("manuel_d'utilisation.docx", e.getFile().getInputstream());
		inputfilepath=path+doc.getFileName();
		doGetDocList();
	}

	public void copyFile(String fileName, InputStream in) {
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(path + fileName));

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
	public void doGetDocList(){
		docList= new ArrayList<String>();

		for (String doc : repertoire.list()) {
			if (doc.endsWith(".docx")  && !doc.startsWith("~")) {
				docList.add(doc);
			}
		}
	}
	public UploadedFile getDoc() {
		return doc;
	}

	public void setDoc(UploadedFile doc) {
		this.doc = doc;
	}

	public String getInputfilepath() {
		return inputfilepath;
	}

	public void setInputfilepath(String inputfilepath) {
		this.inputfilepath = inputfilepath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getDocList() {
		return docList;
	}

	public void setDocList(List<String> docList) {
		this.docList = docList;
	}

	public File getRepertoire() {
		return repertoire;
	}

	public void setRepertoire(File repertoire) {
		this.repertoire = repertoire;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

}
