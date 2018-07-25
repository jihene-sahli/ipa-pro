package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.imaginepartners.imagineworkflow.dashboard.ReportRenderer;
import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.models.IwConfig;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.GenerateDocService;
import com.imaginepartners.imagineworkflow.services.ReportService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FileController implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private GenerateDocService generateDocService;

	@Autowired
	transient private ActivitiService activitiService;

	@Autowired
	BusinessService businessService;

	@Autowired
	ReportService reportService;

	private boolean displayVisualisation;

	private StreamedContent content;

	private String currentfilename;

	private Map<String, Object> currentvarMap;

	private IwUpload iwUploadSelected;

	private FormWrapper formWrapperSelected;

	private String columnSelected;

	private String calssName;

	private Object entitySelected;

	private Task currentTask;

	private Long standalonetaskId;

	private Long functionId;

	public Object getEntitySelected() {
		return entitySelected;
	}

	public void setEntitySelected(Object entitySelected) {
		this.entitySelected = entitySelected;
		this.content = PreGetFile((IwFile) entitySelected);
	}

	public String getColumnSelected() {
		return columnSelected;
	}

	public void setColumnSelected(String columnSelected) {
		this.columnSelected = columnSelected;
	}

	public IwUpload getIwUploadSelected() {
		return iwUploadSelected;
	}

	public void setIwUploadSelected(IwUpload iwUploadSelected) {
		this.iwUploadSelected = iwUploadSelected;
	}

	public FormWrapper getFormWrapperSelected() {
		return formWrapperSelected;
	}

	public void setFormWrapperSelected(FormWrapper formWrapperSelected) {
		this.formWrapperSelected = formWrapperSelected;
	}

	public FileController() {

	}

	@PostConstruct
	public void init() {
		displayVisualisation = false;
		this.entitySelected = null;
		this.content = null;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public StreamedContent getProcDiagram() {
		String deployementId = FacesUtil.getUrlParam("deployement");
		String diagramName = FacesUtil.getUrlParam("diagram");
		if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			InputStream diagram = activitiService.getDiagramResource(deployementId, diagramName);
			return new DefaultStreamedContent(diagram, "image/png");
		}
	}

	public StreamedContent getAvatar() {
		String userId = FacesUtil.getUrlParam("user");

		if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			Picture avatar = activitiService.getAvatar(userId);
			if (avatar == null) {
				return null;
			}
			return new DefaultStreamedContent(avatar.getInputStream(), avatar.getMimeType());
		}

	}

	public StreamedContent exportModel(String modelId) throws IOException {
		byte[] bpmnBytes = null;
		String filename = null;
		JsonNode editorNode = new ObjectMapper().readTree(activitiService.getModelEditorSource(modelId));
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
		BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
		filename = model.getMainProcess().getId() + ".bpmn20.xml";
		bpmnBytes = new BpmnXMLConverter().convertToXML(model);
		InputStream bpmnStream = new ByteArrayInputStream(bpmnBytes);
		return new DefaultStreamedContent(bpmnStream, "application/xml", filename);
	}

	public StreamedContent getImage(IwConfig config) {
		if (config == null || config.getConfigValue().equals(config.getDefaultValue())) {
			return null;
		}
		String filePath = config.getConfigValue();
		if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			InputStream image = null;
			try {
				image = new FileInputStream(filePath);
			} catch (FileNotFoundException ex) {
				LogManager.getLogger(FileController.class.getName()).error(null, ex);
			}
			if (image == null) {
				return null;
			}
			return new DefaultStreamedContent(image);
		}
	}

	public StreamedContent getCompanyLogo() {
		return getImage(businessService.getConfig(ConfigConstants.COMPANY_LOGO));
	}

	public StreamedContent getLoginLogo() {
		return getImage(businessService.getConfig(ConfigConstants.LOGIN_LOGO));
	}

	public String getFaviconDataImage() {
		IwConfig config = businessService.getConfig(ConfigConstants.COMPANY_FAVICON);
		if (config == null || config.getConfigValue().equals(config.getDefaultValue())) {
			return null;
		}
		byte[] imgContent = null;
		try {
			File image = new File(config.getConfigValue());
			imgContent = FileUtils.readFileToByteArray(image);
		} catch (Exception ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		if (imgContent != null) {
			return "data:image/png;base64,"
			+ DatatypeConverter.printBase64Binary(imgContent);
		}
		return null;
	}

	//TODO: real security issue here, paths are sent over http resquest
	public StreamedContent getImage() {
		String filePath = FacesUtil.getUrlParam("path");
		String fileName = FacesUtil.getUrlParam("fileName");
		String mime = FacesUtil.getUrlParam("mime");
		if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			InputStream image = null;
			try {
				image = new FileInputStream(filePath);
			} catch (FileNotFoundException ex) {
				LogManager.getLogger(FileController.class.getName()).error(null, ex);
			}
			if (image == null) {
				return null;
			}
			return new DefaultStreamedContent(image, mime, fileName);
		}
	}

	public StreamedContent getIconFile() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = classLoader.getResource("resources/img/excel_icon.png").getPath();
		return null;
	}

	//TODO: fix security issue here
	public StreamedContent getFile() {
		String filePath = FacesUtil.getUrlParam("path");
		String fileName = FacesUtil.getUrlParam("fileName");
		String mime = FacesUtil.getUrlParam("mime");
		InputStream file = null;
		try {
			file = new FileInputStream(filePath);
		} catch (FileNotFoundException ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		if (file == null) {
			return null;
		}
		return new DefaultStreamedContent(file, mime, fileName);
	}

	public StreamedContent getFileDebug() {
		return null;
	}

	public StreamedContent PreGetFile(IwFile iwFile) {
		InputStream file = null;
		try {
			file = new FileInputStream(iwFile.getIwPath());
		} catch (FileNotFoundException ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		if (file == null) {
			return null;
		}
		return new DefaultStreamedContent(file, iwFile.getIwMime(), iwFile.getIwName());
	}

	public StreamedContent getFile(IwFile iwFile) {
		return this.PreGetFile(iwFile);
	}

	public String getIiwFilePath(IwFile iwFile) {
		return iwFile.getIwPath();
	}

	public void downloadFile(IwFile iwFile) {
		InputStream file = null;
		try {
			file = new FileInputStream(iwFile.getIwPath());
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType(iwFile.getIwMime());
			response.addHeader("Content-disposition", "attachment; filename=" + iwFile.getIwName());
			OutputStream servletOutputStream = response.getOutputStream();
			IOUtils.copyLarge(file, servletOutputStream);
		} catch (Exception ex) {
			LogManager.getLogger(FileController.class.getName()).error(null, ex);
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void downloadFileTwo(IwFile iwFile) {
		File file = new File(iwFile.getIwPath());
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=" + iwFile.getIwName());
		response.setContentLength((int) file.length());
		ServletOutputStream out = null;
		try {
			FileInputStream input = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			out = response.getOutputStream();
			int i = 0;
			while ((i = input.read(buffer)) != -1) {
				out.write(buffer);
				out.flush();
			}
			FacesContext.getCurrentInstance().getResponseComplete();
		} catch (IOException err) {
			err.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException err) {
				err.printStackTrace();
			}
		}
	}

	public StreamedContent downloadPDFReport(String fileName,ArrayList<String> options ,Map<String, Object> parameters) throws Exception {

		ByteArrayOutputStream bos = generatePDFReport(parameters, fileName);
		InputStream input = new ByteArrayInputStream(bos.toByteArray());
		ArrayList<String> docTypes =options; System.out.println("Size*****: "+docTypes.size());System.out.println("Type*****: "+docTypes.isEmpty());
		currentfilename = fileName;
		currentvarMap = parameters;
		if ((fileName.endsWith(ReportRenderer.DOCX_FILE_EXTENSION) && !docTypes.isEmpty() && docTypes.get(0).equals("docx")) || (fileName.endsWith(ReportRenderer.DOCX_FILE_EXTENSION) && docTypes.isEmpty())) {
			return new DefaultStreamedContent(input, "application/docx", fileName);
		} else if(fileName.endsWith(ReportRenderer.DOCX_FILE_EXTENSION) && docTypes.get(0).equals("pdf")){
			String out = generateDocService.docxToPdf(input,fileName);
			File initialFile = new File(out);
			InputStream targetStream = new FileInputStream(initialFile);
			return new DefaultStreamedContent(targetStream, "application/pdf", fileName.replace("docx", "pdf"));
		} else {
			this.content = new DefaultStreamedContent(input, "application/pdf", fileName.replace("rptdesign", "pdf").replace("jasper", "pdf"));
			displayVisualisation = true;
			return new DefaultStreamedContent(input, "application/pdf", fileName.replace("rptdesign", "pdf").replace("jasper", "pdf"));
		}
	}

	public ByteArrayOutputStream generatePDFReport(Map<String, Object> parameters, String reportName) {
		try {
			return reportService.runReport(reportName, "pdf", parameters, 1);
		} catch (Exception e) {
			LogManager.getLogger(TaskController.class).error(null, e);
		}
		return null;
	}

	public StreamedContent exportForm(Long formId) {
		try {
			ObjectMapper xmlMapper = new XmlMapper();
			IwForm frm = businessService.getIwForm(formId);
			String xml = xmlMapper.writeValueAsString(frm);
			String filename = frm.getIwName() + ".form.xml";
			byte[] formBytes = xml.getBytes(StandardCharsets.UTF_8);
			InputStream formStream = new ByteArrayInputStream(formBytes);
			return new DefaultStreamedContent(formStream, "application/xml", filename);
		} catch (JsonProcessingException ex) {
			LogManager.getLogger(FormController.class).error(null, ex);
		}
		return null;
	}

	public boolean isDisplayVisualisation() {
		return displayVisualisation;
	}

	public void setDisplayVisualisation(boolean displayVisualisation) {
		this.displayVisualisation = displayVisualisation;
	}

	public StreamedContent getContent() {
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}

	public boolean visualisation(ActionEvent event) throws Exception {
		displayVisualisation = true;
		ArrayList<String> docTypes= new ArrayList<String>();
		docTypes.add("docx");
		content = this.downloadPDFReport(currentfilename,docTypes, currentvarMap);
		return displayVisualisation;
	}

	public void handleEntityUploadedFiles(FileUploadEvent event) {
		InputStream input = null;
		FormWrapper formWrapper = (FormWrapper) event.getComponent().getAttributes().get("formWrapper");
		String columnName = (String) event.getComponent().getAttributes().get("columnName");
		Object object = (Object) event.getComponent().getAttributes().get("iwuploadView");
		currentTask = (Task) event.getComponent().getAttributes().get("currentTask");
		String destDirPath;
		if (currentTask == null || currentTask.getProcessInstanceId() == null) {
			Long standalonetaskId = (Long) event.getComponent().getAttributes().get("standalonetaskId");
			Long functionId = (Long) event.getComponent().getAttributes().get("functionId");
			destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
			+ File.separator
			+ functionId
			+ File.separator
			+ standalonetaskId
			+ File.separator
			+ new Date().getTime();
		} else {
			destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
			+ File.separator
			+ currentTask.getProcessDefinitionId().replace(":", "@")
			+ File.separator
			+ currentTask.getProcessInstanceId()
			+ File.separator
			+ currentTask.getId()
			+ File.separator
			+ new Date().getTime();
		}
		String fileName = destDirPath
		+ File.separator
		+ event.getFile().getFileName();
		try {
			input = event.getFile().getInputstream();
			File distDir = new File(destDirPath);
			File uploadedFile = new File(fileName);
			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			if (!uploadedFile.exists()) {
				uploadedFile.createNewFile();
			}
			OutputStream output = new FileOutputStream(uploadedFile);
			try {
				IOUtils.copy(input, output);
				IwFile iwFile = new IwFile();
				iwFile.setIwPath(fileName);
				iwFile.setIwSize(event.getFile().getSize());
				iwFile.setIwMime(event.getFile().getContentType());
				iwUploadSelected = (IwUpload) Util.getEntityFieldValue(calssName, object, columnName);
				if (iwUploadSelected == null) {
					iwUploadSelected = new IwUpload();
					iwUploadSelected.setIwFileList(new ArrayList<IwFile>());
				}
				Util.setEntityFieldValue(calssName, object, columnName, iwUploadSelected);
				iwFile.setIwName(event.getFile().getFileName());
				iwUploadSelected.getIwFileList().add(iwFile);
				iwFile.setIwUpload(iwUploadSelected);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		} catch (IOException ex) {
			LogManager.getLogger(TaskController.class).error(ex);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(TaskController.class).error(ex);
			}
		}
	}

	public String getCalssName() {
		return calssName;
	}

	public void setCalssName(String calssName) {
		this.calssName = calssName;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public Long getStandalonetaskId() {
		return standalonetaskId;
	}

	public void setStandalonetaskId(Long standalonetaskId) {
		this.standalonetaskId = standalonetaskId;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public void removeEntityFile(int FileIndex) {
		if (iwUploadSelected.getIwFileList() != null && !iwUploadSelected.getIwFileList().isEmpty()) {
			iwUploadSelected.getIwFileList().remove(FileIndex);
		}
	}
}
