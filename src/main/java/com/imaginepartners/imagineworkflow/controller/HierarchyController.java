package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.models.IwGroupDetails;
import com.imaginepartners.imagineworkflow.models.IwGroupHierarchy;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.constant.GroupHierarchyConstant;
import com.imaginepartners.imagineworkflow.util.tree.GenericTree;
import com.imaginepartners.imagineworkflow.util.tree.GenericTreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class HierarchyController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupId;

	private String groupLinks = "";

	private Group group;

	private User currentUser;

	private IwGroupDetails groupDetails;

	private DefaultDiagramModel groupsModel;

	private boolean suspendEvent;

	private List<User> newMembersList;

	private List<User> memberList;

	private Boolean activeGroup;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	public HierarchyController() {
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	@PostConstruct
	public void init() {

		groupId = FacesUtil.getUrlParam("group");
		activeGroup = true;
		if (StringUtils.isNotBlank(groupId)) {
			group = activitiService.getGroup(groupId);
			groupDetails = businessService.getGroupDetails(groupId);
			if (groupDetails != null && groupDetails.getIwResponsible() != null) {
				currentUser = activitiService.getUser(groupDetails.getIwResponsible());
			}
			memberList = activitiService.getMemerbs(group.getId());
		} else {
			group = activitiService.newGroup("");
			groupDetails = new IwGroupDetails();
			groupDetails.setIwActive(true);
			group.setType(AppConstants.GROUP_USER_NAME);
			memberList = new ArrayList<User>();
		}
	}

	public String getUserId() {
		return groupId;
	}

	public void setUserId(String groupId) {
		this.groupId = groupId;
	}

	public String saveGroup(Boolean newForm) {
		String prefix = businessService.getConfigValue(ConfigConstants.GROUP_ID_DEFAULT_PREFIX);

		group.setId(group.getId().toUpperCase());
		if (!activitiService.getGroupListCaseInsensitive(group.getId()).isEmpty() && !StringUtils.isNotBlank(groupId)) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.groupe.groupeexistedeja", group.getId()));
			return null;
		}

		activitiService.saveGroup(group);
		if (currentUser != null) {
			groupDetails.setIwResponsible(currentUser.getId());
			if (!activitiService.isMemberOfGroup(currentUser.getId(), group.getId())) {
				activitiService.createMembership(currentUser.getId(), group.getId());
			}
		}
		groupDetails.setIwGroup(group.getId());
		businessService.saveIwGroupDetails(groupDetails);
		if (newForm) {
			return "formulaire?faces-redirect=true";
		} else {
			return "details?group=" + group.getId() + "&faces-redirect=true";
		}
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<User> getUserList() {
		return activitiService.getUserList();
	}

	public List<Group> completeGroups(String query) {
		return activitiService.getGroupsListLike(query, businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
	}

	public boolean containsGroup(List<Group> grpList, Group grp) {
		for (Group loopGroup : grpList) {
			if (loopGroup.getId().equals(grp.getId())) {
				return true;
			}
		}
		return false;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Group> getGroupList() {
		List<Group> groups;
		List<Group> groupsFilter = new ArrayList<Group>();
		groups = activitiService.getHierarchyList(businessService.getConfigValue(ConfigConstants.GROUP_ID_DEFAULT_PREFIX));
		// Filter groups by activation
		if (groups != null && !groups.isEmpty()) {
			for (Group group : groups) {
				if (isActiveGroup(group.getId()) == activeGroup) {
					groupsFilter.add(group);
				}
			}
		}
		return groupsFilter;
	}

	public DiagramModel getGroupsModel() {
		return groupsModel;
	}

	public Boolean getActiveGroup() {
		return activeGroup;
	}

	public void setActiveGroup(Boolean activeGroup) {
		this.activeGroup = activeGroup;
	}

	public String initHierarchyModel() {
		StringBuilder membersJs = new StringBuilder();
		int i = 0;
		for (User user : memberList) {
			if (user.getId().equals(groupDetails.getIwResponsible())) {
				membersJs.append(getMemberJs(activitiService.getUser(groupDetails.getIwResponsible()), GroupHierarchyConstant.HIERARCHY_INIT_X, GroupHierarchyConstant.HIERARCHY_MIDDLE_Y + "(" + memberList.size() + ")", GroupHierarchyConstant.COLORS + "[0]", GroupHierarchyConstant.BG_COLORS + "[0]", getAvatarAsDataImage(user.getId())));
			} else {
				membersJs.append(getMemberJs(user, GroupHierarchyConstant.HIERARCHY_GET_X + "(" + i + ")", GroupHierarchyConstant.HIERARCHY_GET_Y + "(" + i + ")", GroupHierarchyConstant.COLORS + "[1]", GroupHierarchyConstant.BG_COLORS + "[1]", getAvatarAsDataImage(user.getId())));
				i++;
			}

		}
		i = 0;
		String firstPoint = "{x:" + GroupHierarchyConstant.HIERARCHY_MIDDLE_LINK_X + "(" + memberList.size() + ")"
		+ ",y:"	+ GroupHierarchyConstant.HIERARCHY_MIDDLE_LINK_Y + "(" + memberList.size() + ")}";
		for (User user : memberList) {
			if (!user.getId().equals(groupDetails.getIwResponsible())) {
				String point;
				String secondPoint = "{x:"
				+ GroupHierarchyConstant.HIERARCHY_MIDDLE_LINK_X + "(" + memberList.size() + ")"
				+ ",y:"
				+ GroupHierarchyConstant.HIERARCHY_GET_LINK_Y + "(" + i + ")"
				+ "}";
				point = "[" + firstPoint + "," + secondPoint + "]";
				membersJs.append(getLinkJs(user, groupDetails.getIwResponsible(), 0, point));
				i++;
			}
		}
		return membersJs.toString();
	}

	public String getLinkJs(User user, String responsible, int index, String points) {
		String linkJs = "iw.group.linkList.link_{0}_{3}=iw.group.link(iw.group.groupList.group_{0},iw.group.groupList.group_{3},{2});\n";
		return MessageFormat.format(linkJs, responsible.replaceAll("[^A-Za-z0-9 ]", "_"), user.getId(), points, user.getId().replaceAll("[^A-Za-z0-9 ]", "_"));
	}

	public String initGroupsModel() {
		List<Group> groupList = activitiService.getGroupList(Arrays.asList(
		businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP), businessService.getConfigValue(ConfigConstants.USERS_ADMIN_GROUP), businessService.getConfigValue(ConfigConstants.USERS_SUPER_ADMIN_GROUP)));
		Map<String, Group> groupMap = new HashMap<String, Group>();
		for (Group grp : groupList) {
			groupMap.put(grp.getId(), grp);
		}
		Group adminGroup = activitiService.getGroup(businessService.getConfigValue(ConfigConstants.USERS_ADMIN_GROUP));
		List<IwGroupHierarchy> groupHierarchy = businessService.getIwGroupHierarchyList();
		GenericTree<String> groupTree = new GenericTree<String>();
		GenericTreeNode<String> root = new GenericTreeNode<String>(GroupHierarchyConstant.ROOT_NAME);
		groupTree.setRoot(root);
		for (IwGroupHierarchy grpH : groupHierarchy) {
			GenericTreeNode<String> parentNode = groupTree.find(grpH.getIwParant());
			GenericTreeNode<String> groupNode = groupTree.find(grpH.getIwGroup());
			if (parentNode == null) {
				parentNode = new GenericTreeNode<String>(grpH.getIwParant());
				groupTree.getRoot().addChild(parentNode);
			}
			if (groupNode == null) {
				groupNode = new GenericTreeNode<String>(grpH.getIwGroup());
			} else {
				if (groupNode.getParent().getData().equals(GroupHierarchyConstant.ROOT_NAME)) {
					root.removeChildAt(root.getChildren().indexOf(groupNode));
				}
			}
			parentNode.addChild(groupNode);
		}
		StringBuilder membersJs = new StringBuilder();
		membersJs.append(getMemberJs(adminGroup, GroupHierarchyConstant.INIT_X, GroupHierarchyConstant.INIT_Y, GroupHierarchyConstant.COLORS + "[0]", GroupHierarchyConstant.BG_COLORS + "[0]"));
		membersJs.append(getMembersJs(groupTree.getRoot(), groupMap, "", 300, 100));
		for (IwGroupHierarchy grpH : groupHierarchy) {
			membersJs.append(getLinkJs(grpH));
		}

		String x = GroupHierarchyConstant.SPACING_X;
		int y = 100;
		int i = 0;
		for (Group grp : groupMap.values()) {
			membersJs.append(getMemberJs(grp, GroupHierarchyConstant.GET_X + "(" + i + ")", GroupHierarchyConstant.GET_Y + "(" + (i + 1) + ")", GroupHierarchyConstant.COLORS + "[1]", GroupHierarchyConstant.BG_COLORS + "[1]"));
			i++;
		}
		return membersJs.toString();
	}

	private String getMembersJs(GenericTreeNode<String> currentNode, Map<String, Group> groupMap, String color, int x, int y) {
		if (currentNode == null) {
			return "";
		}
		String membersJs = "";
		if (currentNode.getData() != null && groupMap.get(currentNode.getData()) != null) {
			membersJs += getMemberJs(groupMap.get(currentNode.getData()), String.valueOf(x), String.valueOf(y), "'#3498DB'", "'#008e09'");
			groupMap.remove(currentNode.getData());
		}
		int newX = 100;
		int newY = y + 100;
		for (GenericTreeNode<String> child : currentNode.getChildren()) {
			color = "";
			membersJs += getMembersJs(child, groupMap, color, newX, newY);
			newX = (newX + 300);
		}
		return membersJs;
	}

	public String initGroupsLinks() {
		return "";
	}

	public String getLinkJs(IwGroupHierarchy grpH) {
		String linkJs = "iw.group.linkList.link_{0}_{1}=iw.group.link(iw.group.groupList.group_{0},iw.group.groupList.group_{1},[]);\n";
		return MessageFormat.format(linkJs, grpH.getIwParant(), grpH.getIwGroup());
	}

	public String getMemberJs(User user, String x, String y, String bgColor, String borderColor, String image) {
		String memberJs = "iw.group.groupList.group_{7}=iw.group.member({1}, {2}, ''{0}'', ''{3}'', ''{6}'', {4}, {5});\n";
		return MessageFormat.format(memberJs, user.getId(), String.valueOf(x), String.valueOf(y), user.getFirstName() + " " + user.getLastName(), bgColor, borderColor, image, user.getId().replaceAll("[^A-Za-z0-9 ]", "_"));
	}

	public String getMemberJs(Group grp, String x, String y, String bgColor, String borderColor) {
		String memberJs = "iw.group.groupList.group_{0}=iw.group.member({1}, {2}, ''{0}'', ''{3}'', ''member1.png'', {4}, {5});\n";
		return MessageFormat.format(memberJs, grp.getId(), String.valueOf(x), String.valueOf(y), grp.getName(), bgColor, borderColor);
	}

	public String getGroupLinks() {
		return groupLinks;
	}

	public void setGroupLinks(String groupLinks) {
		this.groupLinks = groupLinks;
	}

	public void setGroupsModel(DefaultDiagramModel groupsModel) {
		this.groupsModel = groupsModel;
	}

	public boolean isSuspendEvent() {
		return suspendEvent;
	}

	public void setSuspendEvent(boolean suspendEvent) {
		this.suspendEvent = suspendEvent;
	}

	public void saveLinks() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			businessService.removeAllIwGroupHierarchy();
			for (JsonNode node : mapper.readTree(groupLinks)) {
				node.get("source");
				node.get("destination");
				node.get("type");
				IwGroupHierarchy groupHierarchy = new IwGroupHierarchy();
				groupHierarchy.setIwGroup(node.get("target").asText());
				groupHierarchy.setIwParant(node.get("source").asText());
				groupHierarchy.setIwHierarchyType(node.get("type").asText());
				businessService.saveIwGroupHierarchy(groupHierarchy);
			}
			FacesUtil.setSessionErrorMessage(groupLinks);
		} catch (IOException ex) {
			LogManager.getLogger(HierarchyController.class).error(ex);
		}
	}

	public void addMembership() {
		for (User user : newMembersList) {
			activitiService.createMembership(user.getId(), group.getId());
		}
		newMembersList = new ArrayList<User>();
		memberList = activitiService.getMemerbs(group.getId());
	}

	public IwGroupDetails getGroupDetails() {
		return groupDetails;
	}

	public void setGroupDetails(IwGroupDetails groupDetails) {
		this.groupDetails = groupDetails;
	}

	public List<User> completeUsers(String query) {
		return activitiService.getUsersListLike(query);
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public List<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<User> memberList) {
		this.memberList = memberList;
	}

	public List<User> getNewMembersList() {
		return newMembersList;
	}

	public void setNewMembersList(List<User> newMembersList) {
		this.newMembersList = newMembersList;
	}

	public String getAvatarAsDataImage(String userId) {
		Picture avatar = activitiService.getAvatar(userId);
		if (avatar != null) {
			return "data:image/png;base64,"
			+ DatatypeConverter.printBase64Binary(avatar.getBytes());
		} else {
			return FacesUtil.getContextPath() + AppConstants.DEFAULT_AVATATR;
		}
	}

	public void removeGroup(String groupId) {
		activitiService.deleteGroup(groupId);
		businessService.removeIwGroupDetail(groupId);
	}

	public Boolean isActiveGroup(String groupId) {
		return businessService.getGroupDetails(groupId).getIwActive();
	}

	public void activateGroup(String groupId, Boolean activate) {
		IwGroupDetails groupDetail = businessService.getGroupDetails(groupId);
		groupDetail.setIwActive(activate);
		businessService.saveIwGroupDetails(groupDetail);
	}
}
