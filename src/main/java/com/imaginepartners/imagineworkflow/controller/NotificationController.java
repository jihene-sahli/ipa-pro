package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwNotification;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author rafik
 */
@Controller
@Scope("view")
public class NotificationController implements Serializable {
	@Autowired
	private ActivitiService activitiService;
	private static final long serialVersionUID = 1L;
	private List<IwNotification> notificationList = new ArrayList<IwNotification>();
	List<String> fields=new ArrayList<String>();
	Map<String,List<String>> allTasks=new HashMap<String,List<String>>();
	DualListModel<String> tasks;
	private List<User> userList = new ArrayList<User>();
	private List<Group> groupList = new ArrayList<Group>();
	private List<User> memberList = new ArrayList<User>();

	public UploadedFile getDoc() {
		return doc;
	}

	public void setDoc(UploadedFile doc) {
		this.doc = doc;
	}

	private List<User> userFinal = new ArrayList<User>();
	private UploadedFile doc;


	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<String> getProcessList() {
		return processList;

	}

	public void setProcessList(List<String> processList) {
		this.processList = processList;
	}

	public void setNotificationList(List<IwNotification> notificationList) {
		this.notificationList = notificationList;
	}

	public DualListModel<String> getTasks() {
		return tasks;
	}

	public void setTasks(DualListModel<String> tasks) {
		this.tasks = tasks;
	}

	List<String> processList=new ArrayList<String>();
	private String notificationId;

	private IwNotification iwNotification;

	@Autowired
	private BusinessService businessService;

	public NotificationController() {

	}

	@PostConstruct
	public void init() {
		notificationId = FacesUtil.getUrlParam("notificationId");
		if (StringUtils.isNotBlank(notificationId)) {
			iwNotification = (IwNotification) businessService.getEntitytById(IwNotification.class.getName(), notificationId);

		}else {
			iwNotification = new IwNotification();
			iwNotification.setActive(true);
			iwNotification.setNotifierResponsable(false);
		}
		List<String> taskTarget=new ArrayList<String>();

		tasks=new DualListModel<String>(getListOfTasksByProcess(),taskTarget);

	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public Map<String, List<String>> getAllTasks() {
		return allTasks;
	}

	public void setAllTasks(Map<String, List<String>> allTasks) {
		this.allTasks = allTasks;
	}

	public List<String> getListOfTasksByProcess() {
		for (ProcessDefinition procDef : activitiService.getLastProcDefList()) {

			if (!procDef.isSuspended()) {
				String procName = StringUtils.isNotBlank(procDef.getName()) ? procDef.getName() : procDef.getKey();
				if (StringUtils.isBlank(procName)) {
					procName = procDef.getId();
				}
				processList.add(procName);
				Set<Map.Entry<String,String>> l=activitiService.getTaskModelList(procDef.getId()).entrySet() ;
				for (Map.Entry<String, String> task : l) {

					if (!fields.contains(task.getValue()))
						fields.add(procName+" --"+task.getValue());
				}

			}
		}
		return fields;
	}
	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<IwNotification> getNotificationList(){
		notificationList.clear();
		for (Object obj : businessService.getEntiteList(IwNotification.class.getName()))
			notificationList.add((IwNotification) obj);
		return notificationList;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public IwNotification getIwNotification() {
		return iwNotification;
	}

	public void setIwNotification(IwNotification iwNotification) {
		this.iwNotification = iwNotification;
	}

	public List<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<User> memberList) {
		this.memberList = memberList;
	}

	public List<User> getUserFinal() {
		return userFinal;
	}

	public void setUserFinal(List<User> userFinal) {
		this.userFinal = userFinal;
	}

	public String saveNotification(Boolean newForm) {

		iwNotification
		.setName(iwNotification.getName());
		iwNotification.setFromName(iwNotification.getFromName());
		String user=new String();
		if (userList !=null &&  !userList.isEmpty()) {
			for (User usr : userList) {
				user +=usr.getId()+";";
			}
		}
		iwNotification.setToUser(user);
		String grpe=new String();
		if ( groupList!=null && !groupList.isEmpty() ){

			for (Group grp :groupList){
				grpe+=grp.getId()+";";

			}}


		iwNotification.setToRole(grpe);
		String rle =new String();
		List<String> task=new ArrayList<String>();
		task=tasks.getTarget();
		if (task !=null &&  !task.isEmpty()){
			for (String ss :task){
				rle=rle+ss+";";
			}}
		iwNotification.setTasks(rle);

		businessService.saveEntite(iwNotification);
		if (newForm)
			return "formulaire?faces-redirect=true";
		else
			return "liste?faces-redirect=true";
	}

	public void remove() {
		String notificationId = FacesUtil.getUrlParam("notificationId");
		if (StringUtils.isNotBlank(notificationId)) {
			IwNotification iwNotification = (IwNotification)businessService.getEntitytById(IwNotification.class.getName(), notificationId);
			businessService.removeEntite(iwNotification);
		}
	}

	public void disable() {
		String notificationId = FacesUtil.getUrlParam("notificationId");
		if (StringUtils.isNotBlank(notificationId)) {
			IwNotification iwNotification = (IwNotification)businessService.getEntitytById(IwNotification.class.getName(), notificationId);
			iwNotification.setActive(false);
			businessService.saveEntite(iwNotification);
		}
	}

	public void enable() {
		String notificationId = FacesUtil.getUrlParam("notificationId");
		if (StringUtils.isNotBlank(notificationId)) {
			IwNotification iwNotification = (IwNotification)businessService.getEntitytById(IwNotification.class.getName(), notificationId);
			iwNotification.setActive(true);
			businessService.saveEntite(iwNotification);
		}
	}

	public void onSelectGr(SelectEvent event) {

	}

	public void onUnselectGr(UnselectEvent unselectEvent) {

	}

	public void onReorderGr(ReorderEvent reorderEvent) {

	}

	public void onTransferGr(TransferEvent transferEvent) {
		List<String> target = tasks.getTarget();

	}

}
