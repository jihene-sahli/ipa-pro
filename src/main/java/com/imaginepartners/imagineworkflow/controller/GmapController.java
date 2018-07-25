package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.form.wrapper.impl.UploadFormWrapper;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class GmapController implements Serializable {

	private static Logger log = LogManager.getLogger(GmapController.class);

	@Autowired
	private BusinessService businessService;

	private String title = "default title";

	private boolean disabled = false;

	private String label = "label1";

	private boolean required = true;

	public GmapController() {
		log.debug("constructor");
	}

	@PostConstruct
	public void init() {

	}

	public void handleUploadedFiles(FileUploadEvent event) {
		log.debug("on file upload");
	}

	public List<IwFile> getFormFileList(UploadFormWrapper formWrapper) {
		if (formWrapper == null || formWrapper.getIwUpload() == null) {
			return null;
		}
		return formWrapper.getIwUpload().getIwFileList();
	}

	public void areaSelect(ImageAreaSelectEvent event) {
		log.debug("this is me");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
