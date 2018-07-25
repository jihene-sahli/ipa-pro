package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwApplication;
import com.imaginepartners.imagineworkflow.models.IwRight;
import com.imaginepartners.imagineworkflow.models.IwStandAloneTask;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Model;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class RightController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserService userService;

	private List<User> userList = new ArrayList<User>();

	private List<Group> groupList = new ArrayList<Group>();

	private DualListModel<Group> groups= new DualListModel<Group>();

	private DualListModel<User> users= new DualListModel<User>();

	private List<String> userIdList = new ArrayList<String>();

	private List<String> groupIdList = new ArrayList<String>();

	private IwApplication currentApplication;

	private List<IwApplication> applicationList;

	private List<IwRight> rightList;

	private List<IwRight> rightAddList;

	private List<IwRight> rightRemoveList;

	private List<Model> modelList = new ArrayList<Model>();

	private Model currentModel;

	private Boolean rightProcess;

	private List<IwStandAloneTask> standaloneTaskList;

	private IwStandAloneTask standaloneTaskSelected;

	public void updateApp() {
		userList = new ArrayList<User>();
		groupList = new ArrayList<Group>();
		userIdList = new ArrayList<String>();
		groupIdList = new ArrayList<String>();
		modelList = new ArrayList<Model>();
		standaloneTaskList = new ArrayList<IwStandAloneTask>();
		standaloneTaskSelected = null;
		if (currentApplication == null) {
			rightList = new ArrayList<IwRight>();
			return;
		}
		if (rightProcess) {
			modelList = activitiService.getModelList(businessService.getProcessKeyByApplication(currentApplication.getIwKey()));
		} else {
			standaloneTaskList = businessService.getStandAloneTaskList(currentApplication.getIwKey());
		}
	}

	public void updateModel() {
		if ((rightProcess == true && currentModel == null) || (rightProcess == false && standaloneTaskSelected == null)) {
			return;
		}
		userList = new ArrayList<User>();
		groupList = new ArrayList<Group>();
		userIdList = new ArrayList<String>();
		groupIdList = new ArrayList<String>();
		if (rightProcess) {
			rightList = businessService.getIwRightList(currentApplication.getIwKey(), currentModel.getKey());
		} else {
			rightList = businessService.getIwRightListByStandaloneTaskId(currentApplication.getIwKey(), standaloneTaskSelected.getIwStandAloneTaskId());
		}
		if (rightList.isEmpty()) {
			return;
		}
		for (IwRight right : rightList) {
			if (StringUtils.isNotBlank(right.getIwUser())) {
				userIdList.add(right.getIwUser());
				if (!userIdList.isEmpty())
					userList = activitiService.getUserListIn(userIdList);
			} else if (StringUtils.isNotBlank(right.getIwGroup())) {
				groupIdList.add(right.getIwGroup());
				if (!groupIdList.isEmpty())
					groupList = activitiService.getGroupListIn(groupIdList);
			}
		}
		if(rightProcess) {
			groups= new DualListModel<Group>();
			users= new DualListModel<User>();
			for(IwRight right: rightList) {
				for(Group group: groupList) {
					if( right.getIwGroup() != null && right.getIwGroup().equals( group.getId() )  ) {
						try{
							if(right.getIwRightToLaunch())
								groups.getTarget().add(group);
							else
								groups.getSource().add(group);
							break;
						}catch (NullPointerException ex) {
							LogManager.getLogger(RightController.class.getSimpleName())
							.warn("this is a null pointer exception caused by null in the entity "
							+ "Iw_Right, in  column IW_RIGHT_TO_LAUNCH", ex);
							groups.getTarget().add(group);
							break;
						}
					}
				}
				for(User user: userList) {
					if( right.getIwUser() != null && right.getIwUser().equals( user.getId() ) ) {
						try{
							if(right.getIwRightToLaunch())
								users.getTarget().add(user);
							else
								users.getSource().add(user);
							break;
						}catch (NullPointerException ex) {
							LogManager.getLogger(RightController.class.getSimpleName())
							.warn("this is a null pointer exception caused by null in the entity "
							+ "Iw_Right, in  column IW_RIGHT_TO_LAUNCH", ex);
							users.getTarget().add(user);
							break;
						}
					}
				}
			}
		}
	}

	public List<IwApplication> getApplicationList() {
		if (applicationList == null) {
			applicationList = businessService.getApplicaitonList();
		}
		return applicationList;
	}

	public void save() {
		rightAddList = new ArrayList<IwRight>();
		rightRemoveList = new ArrayList<IwRight>();
		if (userList != null && !userList.isEmpty()) {
			for (User usr : userList) {
				boolean userExist = false;
				for (IwRight right : rightList) {
					if (currentApplication.getIwKey().equals(right.getIwApplicationKey())) {
						if (usr.getId().equals(right.getIwUser())) {
							userExist = true;
							break;
						}
					}
				}
				if (!userExist) {
					IwRight right = new IwRight();
					right.setIwUser(usr.getId());
					right.setIwApplicationKey(currentApplication.getIwKey());
					if (rightProcess && currentModel != null) {
						right.setIwProcessKey(currentModel.getKey());
					} else if (!rightProcess && standaloneTaskSelected != null) {
						right.setIwStandaloneId(standaloneTaskSelected.getIwStandAloneTaskId());
					}
					rightAddList.add(right);
				}
			}
		} else {
			for (IwRight right : rightList) {
				if (currentApplication.getIwKey().equals(right.getIwApplicationKey())) {
					if (StringUtils.isNoneBlank(right.getIwUser())) {
						rightRemoveList.add(right);
					}
				}
			}
		}
		if (groupList != null && !groupList.isEmpty()) {
			for (Group grp : groupList) {
				boolean grpExist = false;
				for (IwRight right : rightList) {
					if (currentApplication.getIwKey().equals(right.getIwApplicationKey())) {
						if (grp.getId().equals(right.getIwGroup())) {
							grpExist = true;
							break;
						}
					}
				}
				if (!grpExist) {
					IwRight right = new IwRight();
					right.setIwGroup(grp.getId());
					right.setIwApplicationKey(currentApplication.getIwKey());
					if (rightProcess && currentModel != null) {
						right.setIwProcessKey(currentModel.getKey());
					} else if (!rightProcess && standaloneTaskSelected != null) {
						right.setIwStandaloneId(standaloneTaskSelected.getIwStandAloneTaskId());
					}
					rightAddList.add(right);
				}
			}
		} else {
			for (IwRight right : rightList) {
				if (currentApplication.getIwKey().equals(right.getIwApplicationKey())) {
					if (StringUtils.isNoneBlank(right.getIwGroup())) {
						rightRemoveList.add(right);
					}
				}
			}
		}
		for (IwRight right : rightList) {
			if (currentApplication.getIwKey().equals(right.getIwApplicationKey())) {
				if (StringUtils.isNotBlank(right.getIwUser())) {
					if (userList != null && !userList.isEmpty()) {
						boolean rightExist = false;
						for (User usr : userList) {
							if (usr.getId().equals(right.getIwUser())) {
								rightExist = true;
								break;
							}
						}
						if (!rightExist) {
							rightRemoveList.add(right);
						}
					}
				} else if (StringUtils.isNotBlank(right.getIwGroup())) {
					if (groupList != null && !groupList.isEmpty()) {
						boolean rightExist = false;
						for (Group grp : groupList) {
							if (grp.getId().equals(right.getIwGroup())) {
								rightExist = true;
								break;
							}
						}
						if (!rightExist) {
							rightRemoveList.add(right);
						}
					}
				}
			}
		}
		businessService.saveRights(rightAddList, rightRemoveList);
		if (rightProcess) {
			rightList = businessService.getIwRightList(currentApplication.getIwKey(), currentModel.getKey());
		} else {
			rightList = businessService.getIwRightListByStandaloneTaskId(currentApplication.getIwKey(), standaloneTaskSelected.getIwStandAloneTaskId());
		}
		for (IwRight right : rightRemoveList) {
			rightList.remove(right);
		}
		updateRightToLaunchProcesses();
	}

	private void updateRightToLaunchProcesses() {
		for(User user: users.getTarget()) {
			IwRight right= businessService.getIwRightByAppAndUser(currentApplication.getIwKey(), user.getId(), currentModel.getKey());
			if(right != null) {
				right.setIwRightToLaunch(true);
				businessService.saveEntite(right);
			}
		}
		for(User user: users.getSource()) {
			IwRight right= businessService.getIwRightByAppAndUser(currentApplication.getIwKey(), user.getId(), currentModel.getKey());
			if(right != null) {
				right.setIwRightToLaunch(false);
				businessService.saveEntite(right);
			}
		}
		for(Group group: groups.getTarget()) {
			IwRight right= businessService.getIwRightByAppAndGroup(currentApplication.getIwKey(), group.getId(), currentModel.getKey());
			if(right != null) {
				right.setIwRightToLaunch(true);
				businessService.saveEntite(right);
			}
		}
		for(Group group: groups.getSource()) {
			IwRight right= businessService.getIwRightByAppAndGroup(currentApplication.getIwKey(), group.getId(), currentModel.getKey());
			if(right != null) {
				right.setIwRightToLaunch(false);
				businessService.saveEntite(right);
			}
		}
	}
	public void onItemSelect(SelectEvent event) {
		if(event.getObject() instanceof Group) {
			if( !containsGr((Group) event.getObject(), groups.getTarget() )  &&
			!containsGr((Group) event.getObject(), groups.getSource() )   )
				groups.getTarget().add( (Group) event.getObject() );
		}else{
			if( !containsUs( (User) event.getObject(),  users.getTarget() ) &&
			!containsUs( (User) event.getObject() , users.getSource())  )
				users.getTarget().add(  (User) event.getObject() );
		}
	}

	private boolean containsGr(Group gr, List<Group> list) {
		for(Group group: list) {
			if(group.getId().equals(gr.getId()))
				return true;
		}
		return false;
	}

	private boolean containsUs(User us, List<User> list) {
		for(User user: list) {
			if(us.getId().equals(user.getId()))
				return true;
		}
		return false;
	}

	private boolean removeGr(List<Group> list, Group gr) {
		for( Iterator<Group> iterator = list.iterator(); iterator.hasNext(); ) {
			if(iterator.next().getId().equals(gr.getId())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	private boolean removeUs(List<User> list, User us) {
		for(Iterator<User> iterator= list.iterator(); iterator.hasNext();) {
			if(iterator.next().getId().equals(us.getId())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public void onItemUnselect(UnselectEvent event) {
		if(event.getObject() instanceof Group) {
			if(containsGr((Group) event.getObject(), groups.getTarget()))
				removeGr(groups.getTarget(), (Group) event.getObject());
			else
				removeGr(groups.getSource(), (Group) event.getObject());
		}else{
			if(containsUs(  (User) event.getObject() , users.getTarget()  )  )
				removeUs(users.getTarget(), (User) event.getObject());
			else
				removeUs(users.getSource(), (User) event.getObject());
		}
	}

	public void onQuery(AjaxBehaviorEvent event) {

	}

	public void onSelectGr(SelectEvent event) {

	}

	public void onUnselectGr(UnselectEvent unselectEvent) {

	}

	public void onReorderGr(ReorderEvent reorderEvent) {

	}

	public void onTransferGr(TransferEvent transferEvent) {

	}


	public void onSelectUs(SelectEvent event) {

	}

	public void onUnselectUs(UnselectEvent unselectEvent) {

	}

	public void onReorderUs(ReorderEvent reorderEvent) {

	}

	public void onTransferUs(TransferEvent transferEvent) {

	}


	public void setApplicationList(List<IwApplication> applicationList) {
		this.applicationList = applicationList;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public IwApplication getCurrentApplication() {
		return currentApplication;
	}

	public void setCurrentApplication(IwApplication currentApplication) {
		this.currentApplication = currentApplication;
	}

	public List<Model> getModelList() {
		return modelList;
	}

	public void setModelList(List<Model> modelList) {
		this.modelList = modelList;
	}

	public Model getCurrentModel() {
		return currentModel;
	}

	public void setCurrentModel(Model currentModel) {
		this.currentModel = currentModel;
	}

	public Boolean getRightProcess() {
		return rightProcess;
	}

	public void setRightProcess(Boolean rightProcess) {
		this.rightProcess = rightProcess;
	}

	public List<IwStandAloneTask> getStandaloneTaskList() {
		return standaloneTaskList;
	}

	public void setStandaloneTaskList(List<IwStandAloneTask> standaloneTaskList) {
		this.standaloneTaskList = standaloneTaskList;
	}

	public IwStandAloneTask getStandaloneTaskSelected() {
		return standaloneTaskSelected;
	}

	public void setStandaloneTaskSelected(IwStandAloneTask standaloneTaskSelected) {
		this.standaloneTaskSelected = standaloneTaskSelected;
	}

	public DualListModel<Group> getGroups() {
		return groups;
	}

	public void setGroups(DualListModel<Group> groups) {
		this.groups = groups;
	}

	public DualListModel<User> getUsers() {
		return users;
	}

	public void setUsers(DualListModel<User> users) {
		this.users = users;
	}

	public void reset() {
		currentModel = null;
		standaloneTaskSelected = null;
		userList = null;
		groupList = null;
		userIdList = null;
		groupIdList = null;
		modelList = null;
		standaloneTaskList = null;
		currentApplication = null;
	}
}
