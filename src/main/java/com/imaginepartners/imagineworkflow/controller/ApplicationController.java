package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwApplication;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.ReorderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class ApplicationController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	private String applicationId;

	private IwApplication application;

	private List<Model> modelList;

	private List<IwApplication> appList;

	private Model model;

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<Model> getModelList() {
		List<String> processKes = businessService.getProcessKeyByApplication(application.getIwKey());
		return activitiService.getModelList(processKes);
	}

	public List<IwApplication> getApplicationList() {
		if (appList==null) {
			appList = businessService.getApplicaitonList();
		}
		return appList;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public IwApplication getApplication() {
		return application;
	}

	public void setApplication(IwApplication application) {
		this.application = application;
	}

	@PostConstruct
	public void init() {
		getApplicationList();
		applicationId = FacesUtil.getUrlParam("application");
		if (StringUtils.isNotBlank(applicationId)) {
			application = businessService.getApplicationById(Long.valueOf(applicationId));
		} else {
			application = new IwApplication();
		}
	}

	public void delete() {
		String appKey = FacesUtil.getUrlParam("applicationKey");
		List<Model> modelList = activitiService.getModelListByCategory(appKey);
		for (Model model : modelList) {
			model.setCategory(null);
			businessService.deleteProcessApplication(appKey);
			activitiService.saveModel(model);
		}
		String appId = FacesUtil.getUrlParam("applicationId");
		businessService.deleteIwApplication(Long.valueOf(appId));
		appList = businessService.getApplicaitonList();
	}

	public String saveApplication(boolean newApp) {
		if (!StringUtils.isNotBlank(application.getIwName())) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.application.saisirnomapplication"));
			return null;
		}
		if (application.getIwKey() == null) {
			application.setIwKey(Util.convertToAlphaNumeric(application.getIwName()) + new Date().getTime());
		}
		application.setIwDate(new Date());
		businessService.saveIwApplication(application);
		if (newApp) {
			return "formulaire?faces-redirect=true";
		}
		return "details?application=" + application.getIwApplicationId() + "&faces-redirect=true";
	}

	public String replace(String txt, String regex, String replacement) {
		return txt.replace(regex, replacement);
	}

	public List<Model> getModelListByCategory(String appKey) {
		return activitiService.getModelListByCategory(appKey);
	}

	// Row reorder listener
	public void onRowReorder(ReorderEvent event) {
		for(IwApplication app: appList){
			app.setIwListingIndex(appList.indexOf(app));
		}
		businessService.saveEntityList(appList);

		// Show the info message to user
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Déplacement enregistré", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
