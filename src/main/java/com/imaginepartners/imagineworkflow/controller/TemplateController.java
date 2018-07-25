package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFormTemplate;
import com.imaginepartners.imagineworkflow.models.IwVariableTemplate;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 31/01/2017.
 */
@Controller("templateController")
@Scope("view")
public class TemplateController implements Serializable {

	@Autowired
	private BusinessService businessService;

	private Set<IwFormTemplate> templateList;

	private List<IwVariableTemplate> variableTemplateList;

	private List<IwVariableTemplate> removedVariableList;

	private String templateId;

	private IwFormTemplate selectedTemplate;


	public TemplateController() {
	}

	@PostConstruct
	public void init() {

		if (businessService.getIwFormTemplateList().isEmpty()) {
			templateList = new HashSet<IwFormTemplate>();

		} else {
			templateList = new HashSet<IwFormTemplate>(businessService.getIwFormTemplateList());

		}


		removedVariableList = new ArrayList<IwVariableTemplate>();

	}

	public void onTemplateChange() {
		selectedTemplate = (IwFormTemplate) businessService.getEntitytById(IwFormTemplate.class.getName(), templateId);

		if (selectedTemplate.getIwVarTemplateList().isEmpty()) {
			variableTemplateList = new ArrayList<IwVariableTemplate>();

		} else {
			variableTemplateList = selectedTemplate.getIwVarTemplateList();

		}
	}

	public void addVariableTemplate() {

		variableTemplateList.add(new IwVariableTemplate());
	}

	public void deleteVariableTemplate(int rowId) {
		removedVariableList.add((IwVariableTemplate) variableTemplateList.get(rowId));
		variableTemplateList.remove(rowId);
	}

	public void saveOrDeleteVariable() {

		if (!variableTemplateList.isEmpty()) {

			for (IwVariableTemplate var : variableTemplateList) {

				var.setIwFormTemplate(selectedTemplate);

			}
			businessService.saveEntityList(variableTemplateList);
			FacesUtil.setAjaxInfoMessage("L' ajout des variables est effectué avec succès");
		}
		if (!removedVariableList.isEmpty()) {

			for (IwVariableTemplate var : removedVariableList) {
				if (var.getIwvariablerealid() != null)
					businessService.removeEntite(var);
			}

			FacesUtil.setAjaxInfoMessage("La suppression des variables est effectué avec succès");

		}
		init();
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public Set<IwFormTemplate> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(Set<IwFormTemplate> templateList) {
		this.templateList = templateList;
	}

	public List<IwVariableTemplate> getVariableTemplateList() {
		return variableTemplateList;
	}

	public void setVariableTemplateList(List<IwVariableTemplate> variableTemplateList) {
		this.variableTemplateList = variableTemplateList;
	}

	public List<IwVariableTemplate> getRemovedVariableList() {
		return removedVariableList;
	}

	public void setRemovedVariableList(List<IwVariableTemplate> removedVariableList) {
		this.removedVariableList = removedVariableList;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public IwFormTemplate getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(IwFormTemplate selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	/*public List<IwVariableTemplate> getAddedVariableList() {
		return addedVariableList;
	}

	public void setAddedVariableList(List<IwVariableTemplate> addedVariableList) {
		this.addedVariableList = addedVariableList;
	}*/
}
