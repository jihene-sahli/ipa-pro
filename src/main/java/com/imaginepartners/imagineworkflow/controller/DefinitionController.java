package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_REVISION;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class DefinitionController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String procDefId;

	private ProcessDefinition procDef;

	private String procTitle;

	private UploadedFile bpmnFile;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private NavigationController navigationController;

	@Autowired
	private UserService userService;

	public DefinitionController() {
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	@PostConstruct
	public void init() {
		procDefId = FacesUtil.getUrlParam("process");
		if (StringUtils.isNotBlank(procDefId)) {
			procDef = activitiService.getProcessDefinitionById(procDefId);
			if (procDef != null) {
				procTitle = procDef.getName();
			}
		}
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getProcTitle() {
		return procTitle;
	}

	public void setProcTitle(String procTitle) {
		this.procTitle = procTitle;
	}

	public ProcessDefinition getProcDef() {
		return procDef;
	}

	public void setProcDef(ProcessDefinition procDef) {
		this.procDef = procDef;
	}

	public List<ProcessDefinition> getLastProcDefList() {
		return activitiService.getLastProcDefList();
	}

	public List<ProcessDefinition> getProcDefList() {
		List<String> processKeis = userService.getUserProcessKeyRights();
		return activitiService.getProcDefList(processKeis);
	}

	public void undeploy() {
		String deploymentId = FacesUtil.getUrlParam("deploymentId");
		boolean includeInstances = Boolean.valueOf(FacesUtil.getUrlParam("includeInstances"));
		activitiService.undeploy(deploymentId, includeInstances);
	}

	public void suspend() {
		String procDefIdParam = FacesUtil.getUrlParam("procDefId");
		boolean includeInstances = Boolean.valueOf(FacesUtil.getUrlParam("includeInstances"));
		activitiService.suspend(procDefIdParam, includeInstances);
	}

	public void activate() {
		String procDefIdParam = FacesUtil.getUrlParam("procDefId");
		boolean includeInstances = Boolean.valueOf(FacesUtil.getUrlParam("includeInstances"));
		activitiService.activate(procDefIdParam, includeInstances);
	}

	public void convertToModel() {
		String procDefIdParam = FacesUtil.getUrlParam("procDefId");
		Boolean redirect = Boolean.valueOf(FacesUtil.getUrlParam("redirect"));
		ProcessDefinition processDefinition = activitiService.getProcessDefinitionById(procDefIdParam);
		try {
			InputStream bpmnStream = activitiService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
			XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
			InputStreamReader in = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
			if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurbpmn"));
			} else {
				if (bpmnModel.getLocationMap().isEmpty()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.model.message.erreurbpmn"));
				} else {
					BpmnJsonConverter converter = new BpmnJsonConverter();
					ObjectNode modelNode = converter.convertToJson(bpmnModel);
					Model modelData = activitiService.newModel();
					ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
					modelObjectNode.put(MODEL_NAME, processDefinition.getName());
					modelObjectNode.put(MODEL_REVISION, 1);
					modelObjectNode.put(MODEL_DESCRIPTION, processDefinition.getDescription());
					modelData.setMetaInfo(modelObjectNode.toString());
					modelData.setName(processDefinition.getName());
					activitiService.saveModel(modelData);
					activitiService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes(StandardCharsets.UTF_8));
					if (redirect) {
						FacesUtil.redirect(FacesUtil.getRequest().getContextPath() + navigationController.getModelEditUrl(modelData.getId()));
					} else {
						FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.model.message.conversionsuccess"));
					}
				}
			}
		} catch (Exception exp) {
			FacesUtil.setAjaxErrorMessage(exp.getLocalizedMessage());
		}
	}

	public UploadedFile getBpmnFile() {
		return bpmnFile;
	}

	public void setBpmnFile(UploadedFile bpmnFile) {
		this.bpmnFile = bpmnFile;
	}

	public void uploadProcess(FileUploadEvent event) {
		LogManager.getLogger(DefinitionController.class).debug("bpmnFile : " + bpmnFile);
		bpmnFile = event.getFile();
		if (bpmnFile != null) {
			try {
				Deployment deploy = activitiService.deploy(bpmnFile.getFileName(), bpmnFile.getInputstream());
				if (deploy == null) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.process.echecdeploiment", bpmnFile.getFileName()));
				} else {
					FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.process.fichierdeploiyersuccess", bpmnFile.getFileName()));
				}
			} catch (Exception ex) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.process.echecdeploiment", bpmnFile.getFileName()));
				FacesUtil.setAjaxErrorMessage(ex.getLocalizedMessage());
			}
		}
	}
}
