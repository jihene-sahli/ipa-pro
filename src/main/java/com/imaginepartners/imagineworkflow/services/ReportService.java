package com.imaginepartners.imagineworkflow.services;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public interface ReportService {

	public void initBirtEngin();

	public String runReport(String fileName, Map<String, Object> parms);

	public ByteArrayOutputStream runReport(String fileName, String reportFormat, Map<String, Object> params, Integer pageNumber);

	public String runJasperHtmlReport(String fileName, Map<String, Object> parms);
}
