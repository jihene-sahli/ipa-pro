package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ReportService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class BirtReportController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	ReportService reportService;

	private String reportName;

	@PostConstruct
	public void init() {

		reportName = FacesUtil.getUrlParam("rapport");
	}

	public String runReport(String fileName) {
		return reportService.runReport(fileName, new HashMap<String, Object>());
	}

	public String runReport(String fileName, Map<String, Object> params) {
		return reportService.runReport(fileName, params);
	}

	public String displayReport() {
		return reportService.runReport(reportName, new HashMap<String, Object>());
	}
}
