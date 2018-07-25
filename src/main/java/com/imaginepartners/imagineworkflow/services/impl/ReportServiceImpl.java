package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.dashboard.ReportProcessor;
import com.imaginepartners.imagineworkflow.dashboard.ReportRenderer;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.services.ReportService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private LicenseService licenseService;

	private String reportPath;

	private String ressourcePath;

	private String imageFolder;

	private String imageUrl;

	private ReportProcessor processor;

	@Override
	@PostConstruct
	public void initBirtEngin() {
		try {
			//reportPath = this.getClass().getClassLoader().getResource(AppConstants.REPORTS_PATH).toURI().getPath();
			//ressourcePath = this.getClass().getClassLoader().getResource(AppConstants.REPORTS_RESSOURCES_PATH).toURI().getPath();
			reportPath=AppConstants.REPORTS_PATH;
			ressourcePath=AppConstants.REPORTS_RESSOURCES_PATH;
			imageFolder = FacesUtil.getRealPath(AppConstants.REPORTS_IMAGES_FOLDER);
			imageUrl = FacesUtil.getContextPath() + AppConstants.REPORTS_IMAGES_FOLDER;
			processor = ReportProcessor.getReportProcessor();
			processor.initilizeReportEngine(reportPath, ressourcePath, imageFolder, imageUrl, appContext);
		} /*catch (URISyntaxException ex) {
			LogManager.getLogger(ReportServiceImpl.class).error(null, ex);
		}*/ catch (NullPointerException npe) {
			LogManager.getLogger(ReportServiceImpl.class).error("can't instantiate imageFolder object, see the folder /raports/ if existe.", npe);
		}
	}

	@Override
	public String runReport(String fileName, Map<String, Object> parms) {
		try {
			if (licenseService.getHasModuleDashboard()) {
				return processor.runReport(fileName, parms);
			} else {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.license.paslicensepourmoduledashboard"));
				return FacesUtil.getMessage("iw.license.paslicensepourmodule", "dashboard");
			}
		} catch (Exception ex) {
			LogManager.getLogger(ReportServiceImpl.class).error(null, ex);
			FacesUtil.setAjaxErrorMessage(ex.getLocalizedMessage());
			return ex.getLocalizedMessage();
		}
	}

	@Override
	public String runJasperHtmlReport(String fileName, Map<String, Object> parms) {
		try {
			if (licenseService.getHasModuleDashboard()) {
				return processor.runJasperHtmlReport(fileName, parms);
			} else {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.license.paslicensepourmoduledashboard"));
				return FacesUtil.getMessage("iw.license.paslicensepourmodule", "dashboard");
			}
		} catch (Exception ex) {
			LogManager.getLogger(ReportServiceImpl.class).error(null, ex);
			FacesUtil.setAjaxErrorMessage(ex.getLocalizedMessage());
			return ex.getLocalizedMessage();
		}
	}

	@Override
	public ByteArrayOutputStream runReport(String fileName, String reportFormat, Map<String, Object> params, Integer pageNumber) {
		try {
			if (licenseService.getHasModuleDashboard()) {
				if (fileName.endsWith(ReportRenderer.DOCX_FILE_EXTENSION)) {
					LogManager.getLogger(ReportServiceImpl.class.getName()).info("file nameAttestation** " + fileName);
					LogManager.getLogger(ReportServiceImpl.class.getName()).info("file paramsparams** " + params);
					return processor.generateDocx(params, fileName);
				} else if (fileName.endsWith(ReportRenderer.DOCUMENT_FILE_EXTENSION) || fileName.endsWith(ReportRenderer.REPORT_FILE_EXTENSION)) {
					return processor.runReport(fileName, reportFormat, params, pageNumber);
				} else if (fileName.endsWith(ReportRenderer.JRREPORT_FILE_EXTENSION)) {
					return processor.runJRReport(fileName, reportFormat, params, pageNumber);
				}
			} else {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.license.paslicensepourmoduledocument"));
			}
		} catch (Exception ex) {
			LogManager.getLogger(ReportServiceImpl.class).error(null, ex);
			FacesUtil.setAjaxErrorMessage(ex.getLocalizedMessage());
		}
		return null;
	}
}
