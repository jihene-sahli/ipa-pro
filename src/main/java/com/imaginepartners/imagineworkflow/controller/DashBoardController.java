package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class DashBoardController implements Serializable {

	private PieChartModel processPie;

	private BarChartModel processBar;

	private LineChartModel processDateLine;

	private ProcessDefinition currentProcDef;

	String processKey;

	@Autowired
	ActivitiService activitiService;

	@PostConstruct
	public void init() {
		processKey              = FacesUtil.getUrlParam("process");
		if (StringUtils.isNotBlank(processKey)) {
			currentProcDef      = activitiService.getProcessDefinitionByKey(processKey);
		}
		processPie              = new PieChartModel();
		processBar              = new BarChartModel();
		ChartSeries completed   = new ChartSeries();
		ChartSeries current     = new ChartSeries();

		if (currentProcDef != null) {
			processPie.set(FacesUtil.getMessage("iw.dashboard.processencours"), activitiService.getProcInstNbr(processKey));
			processPie.set(FacesUtil.getMessage("iw.dashboard.processcomplete"), activitiService.getProcInstHistNbr(processKey));
			processPie.setTitle(FacesUtil.getMessage("iw.dashboard.process", currentProcDef.getName()));
			processBar.setTitle(FacesUtil.getMessage("iw.dashboard.process", currentProcDef.getName()));
			current.setLabel(FacesUtil.getMessage("iw.dashboard.processencours"));
			current.set(" ", activitiService.getProcInstNbr(processKey));
			completed.setLabel(FacesUtil.getMessage("iw.dashboard.processcomplete"));
			completed.set(" ", activitiService.getProcInstHistNbr(processKey));
		} else {
			processPie.set(FacesUtil.getMessage("iw.dashboard.processencours"), activitiService.getProcInstNbr());
			processPie.set(FacesUtil.getMessage("iw.dashboard.processcomplete"), activitiService.getProcInstHistNbr());
			processPie.setTitle(FacesUtil.getMessage("iw.dashboard.toutesprocess"));
			processBar.setTitle(FacesUtil.getMessage("iw.dashboard.toutesprocess"));
			current.setLabel(FacesUtil.getMessage("iw.dashboard.processencours"));
			current.set(" ", activitiService.getProcInstNbr());
			completed.setLabel(FacesUtil.getMessage("iw.dashboard.processcomplete"));
			completed.set(" ", activitiService.getProcInstHistNbr());
		}
		processPie.setLegendPosition("ne");
		processPie.setShowDataLabels(true);
		processPie.setDiameter(270);
		processPie.setExtender("extLegend");
		processBar.addSeries(current);
		processBar.addSeries(completed);
		Axis xAxis = processBar.getAxis(AxisType.X);
		xAxis.setLabel(FacesUtil.getMessage("iw.process"));
		Axis yAxis = processBar.getAxis(AxisType.Y);
		yAxis.setLabel(FacesUtil.getMessage("iw.nombre"));
		processBar.setLegendPosition("ne");
		processBar.setAnimate(true);
		processBar.setExtender("extLegend");
	}

	public PieChartModel getProcessPie() {
		return processPie;
	}

	public void setProcessPie(PieChartModel processPie) {
		this.processPie = processPie;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public LineChartModel getProcessDateLine() {
		return processDateLine;
	}

	public void setProcessDateLine(LineChartModel processDateLine) {
		this.processDateLine = processDateLine;
	}

	public BarChartModel getProcessBar() {
		return processBar;
	}

	public void setProcessBar(BarChartModel processBar) {
		this.processBar = processBar;
	}
}
