package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.models.IwApplication;
import com.imaginepartners.imagineworkflow.models.IwModelDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_REVISION;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ReorderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class ModelController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private NavigationController navigationController;

	@Autowired
	private BusinessService businessService;

	private String modelName = "";

	private String modelDescription = "";

	private String modelId = "";

	private Model model = null;

	private IwApplication application;

	private Boolean isClone = false;

	private List<Model> modelList;

	public ModelController() {

	}

	@PostConstruct
	public void init() {
		modelId = FacesUtil.getUrlParam("model");
		isClone = Boolean.valueOf(FacesUtil.getUrlParam("copy"));
		if (modelId != null && !modelId.isEmpty()) {
			model = activitiService.getModel(modelId);
			if(isClone) {
				modelName = new StringBuilder().append(model.getName()).append(" (Copie)").toString();
			} else {
				modelName = model.getName();
			}
			try {
				JsonNode node = new ObjectMapper().readValue(model.getMetaInfo(), JsonNode.class).get(MODEL_DESCRIPTION);
				if (node != null) {
					modelDescription = node.textValue();
				}
			} catch (IOException ex) {
				modelDescription = "";
			}
			application = businessService.getApplicationByProcess(model.getKey());
		} else {
			String appId = FacesUtil.getUrlParam("application");
			if (StringUtils.isNotBlank(appId)) {
				application = businessService.getApplicationById(Long.valueOf(appId));
			} else {
				application = new IwApplication();
			}
		}
	}

	public IwApplication getApplication() {
		return application;
	}

	public void setApplication(IwApplication application) {
		this.application = application;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public void createModel() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put(AppConstants.MODEL_ID_NAME, AppConstants.MODEL_ID_VALUE);
		editorNode.put(AppConstants.MODEL_RESSOURCE_ID_NAME, AppConstants.MODEL_RESSOURCE_ID_VALUE);
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put(AppConstants.MODEL_NAMESPACE_NAME, AppConstants.MODEL_NAMESPACE_VALUE);
		editorNode.put(AppConstants.MODEL_STENCILSET_NAME, stencilSetNode);
		Model modelData = activitiService.newModel();
		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(MODEL_NAME, modelName);
		modelObjectNode.put(MODEL_REVISION, 1);
		modelObjectNode.put(MODEL_DESCRIPTION, modelDescription);
		modelData.setMetaInfo(modelObjectNode.toString());
		modelData.setName(modelName);
		modelData.setKey(Util.convertToAlphaNumeric(modelData.getName()));
		if (application != null && application.getIwKey() != null) {
			businessService.saveAplicationProcess(application.getIwKey(), modelData.getKey());
		}
		activitiService.saveModel(modelData);
		activitiService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
		FacesUtil.redirect(FacesUtil.getRequest().getContextPath() + navigationController.getModelEditUrl(modelData.getId()));
		modelList = activitiService.getOrderedModelList();
	}

	public void saveModel() {
		Model modelSave;
		if (isClone) {
			// Duplicate model from existing one
			modelSave = activitiService.newModel();
			modelSave.setKey(Util.convertToAlphaNumeric(model.getName()) + new Date().getTime());
		} else {
			modelSave = model;
		}

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(MODEL_NAME, modelName);
		modelObjectNode.put(MODEL_DESCRIPTION, modelDescription);
		modelSave.setMetaInfo(modelObjectNode.toString());
		modelSave.setName(modelName);
		if (modelSave.getKey() == null || modelSave.getKey().isEmpty()) {
			modelSave.setKey(Util.convertToAlphaNumeric(modelSave.getName()) + new Date().getTime());
		}
		if (application != null && application.getIwKey() != null) {
			businessService.saveAplicationProcess(application.getIwKey(), modelSave.getKey());
		}
		activitiService.saveModel(modelSave);
		activitiService.addModelEditorSource(modelSave.getId(), activitiService.getModelEditorSource(modelId));
		activitiService.addModelEditorSourceExtra(modelSave.getId(), activitiService.getModelEditorSourceExtra(modelId));
		FacesUtil.redirect(FacesUtil.getRequest().getContextPath() + navigationController.getModelEditUrl(modelSave.getId()));
	}

	public void deployModel() {
		String modelId = FacesUtil.getUrlParam("model");
		try {
			Deployment deployment = activitiService.deployModel(modelId);
			FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.modele.deploimentresussi"));
		} catch (Exception exp) {
			FacesUtil.setAjaxInfoMessage(exp.getLocalizedMessage());
			LogManager.getLogger(ModelController.class).error(exp);
		}
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public List<Model> getModelList() {
		modelList = activitiService.getOrderedModelList();
		return modelList;
	}

	public void delete() {
		activitiService.deleteModel(FacesUtil.getUrlParam("model"));
		modelList = activitiService.getOrderedModelList();
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void uploadModel(FileUploadEvent event) {
		InputStream in = null;
		try {
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
			in = event.getFile().getInputstream();
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
			xmlConverter.convertToBpmnModel(xtr);
			if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurbpmn"));
			} else if (bpmnModel.getLocationMap().isEmpty()) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurbpmn"));
			} else {
				String processName;
				if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
					processName = bpmnModel.getMainProcess().getName();
				} else {
					processName = bpmnModel.getMainProcess().getId();
				}
				Model modelData = activitiService.newModel();
				ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
				modelObjectNode.put(MODEL_NAME, processName);
				modelObjectNode.put(MODEL_REVISION, 1);
				modelData.setMetaInfo(modelObjectNode.toString());
				modelData.setName(processName);
				activitiService.saveModel(modelData);
				BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
				ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
				activitiService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
				FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.model.message.modelajouteavecsucces"));
			}
		} catch (IOException ex) {
			LogManager.getLogger(ModelController.class).debug(ex);
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurupload"));
		} catch (XMLStreamException ex) {
			LogManager.getLogger(ModelController.class).debug(ex);
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurxml"));
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(ModelController.class).debug(ex);
			}
		}
	}

	// Row reorder listener
	public void onRowReorder(ReorderEvent event) {
		for(Model model: modelList) {
			IwModelDetails mdl = businessService.getIwModelDetailsByModelId(model.getId());
			if (mdl == null) mdl = new IwModelDetails();
			mdl.setIwActModelId(model.getId());
			mdl.setIwListingIndex(modelList.indexOf(model));
			businessService.saveEntite(mdl);
		}
		// Show the info message to user
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Déplacement enregistré", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
