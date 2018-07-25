package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class CommentController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String procInstId;

	private String procInstIdHist;

	private String taskId;

	private String comment;

	@Autowired
	private ActivitiService activitiService;

	@PostConstruct
	public void init() {
		procInstId = FacesUtil.getUrlParam("instance");
		procInstIdHist = FacesUtil.getUrlParam("instanceHist");
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public CommentController() {
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getProcInstIdHist() {
		return procInstIdHist;
	}

	public void setProcInstIdHist(String procInstIdHist) {
		this.procInstIdHist = procInstIdHist;
	}

	public void submitTaskComment() {
		if (StringUtils.isNotBlank(comment)) {
			activitiService.addTaskComment(taskId, null, comment);
			comment = null;
		}
	}

	public void submitProcInstComment() {
		if (StringUtils.isNotBlank(comment)) {
			activitiService.addProcInstComment(procInstId, comment);
			comment = null;
		}
	}

	public void submitProcInstComment(String procInstId) {
		if (StringUtils.isNotBlank(comment)) {
			activitiService.addProcInstComment(procInstId, comment);
			comment = null;
		}
	}

	public List<Comment> getProcInstCommentList() {
		if(procInstId != null){
			return activitiService.getProcInstCommentList(procInstId);
		}else{
			return activitiService.getProcInstCommentList(procInstIdHist);
		}
	}

	public List<Comment> getProcInstCommentList(String procInstId) {
		return activitiService.getProcInstCommentList(procInstId);
	}

	public List<Comment> getTaskCommentList() {
		return activitiService.getTaskCommentList(taskId);
	}
}
