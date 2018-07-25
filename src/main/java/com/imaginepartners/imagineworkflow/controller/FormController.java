package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ReorderEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Controller
@Scope("view")
public class FormController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private NavigationController navigationController;

	private List<IwForm> formList;

	public FormController() {
	}

	public void createNewForm() {
		FacesUtil.redirect(navigationController.getNewFormUrl());
	}

	public List<IwForm> getFormList() {
		if (formList==null) {
			formList = businessService.getIwFormList();
		}
		return formList;
	}

	public void delete() {
		businessService.deleteIwForm(Long.valueOf(FacesUtil.getUrlParam("form")));
		formList = businessService.getIwFormList();
	}

	public void copy() {
		IwForm oldForm = businessService.getIwForm(Long.valueOf(FacesUtil.getUrlParam("form")));
		IwForm newForm = businessService.newIwForm();
		BeanUtils.copyProperties(oldForm, newForm);
		newForm.setIwFormId(null);
		businessService.saveIwForm(newForm);
		formList = businessService.getIwFormList();
		FacesUtil.redirect(FacesUtil.getRequest().getContextPath() + navigationController.getFormEditUrl(businessService.saveIwForm(newForm).getIwFormId().toString()));
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public void setFormList(List<IwForm> formList) {
		this.formList = formList;
	}

	public void uploadForm(FileUploadEvent event) {
		InputStream in = null;
		try {
			in = event.getFile().getInputstream();
			ObjectMapper xmlMapper = new XmlMapper();
			IwForm frm = xmlMapper.readValue(in, IwForm.class);
			frm.setIwFormId(null);
			Date date = new Date();
			frm.setIwCreateTime(date);
			frm.setIwLastUpdateTime(date);
			businessService.saveIwForm(frm);
			FacesUtil.setAjaxInfoMessage(FacesUtil.getMessage("iw.formulaire.message.formulaireajoute", frm.getIwName(), String.valueOf(frm.getIwFormId())));
		} catch (Exception ex) {
			LogManager.getLogger(FormController.class).error(ex);
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.formulaire.message.erreurajout", ex.getLocalizedMessage()));
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(FormController.class).error(ex);
			}
		}
	}

	// Row reorder listener
	public void onRowReorder(ReorderEvent event) {
		for(IwForm frm: formList){
			frm.setIwListingIndex(formList.indexOf(frm));
		}
		businessService.saveEntityList(formList);
		// Show the info message to user
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Déplacement enregistré", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
