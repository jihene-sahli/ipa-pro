package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.models.IwGroupDetails;
import com.imaginepartners.imagineworkflow.models.IwGroupHierarchy;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.models.rh.OrgEntiteOrganisationnelle;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhPosteOccupe;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.constant.GroupHierarchyConstant;
import com.imaginepartners.imagineworkflow.util.tree.GenericTreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class GroupController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupId;

	private String groupLinks = "";

	private String entiteOrganisationnelLinks = "";

	private String collaborateurLinks = "";

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

	public GroupController() {
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
		if (!group.getId().startsWith(prefix) && groupId == null) {
			group.setId(prefix + group.getId().toUpperCase());
		}
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
		groups = activitiService.getGroupList(businessService.getConfigValue(ConfigConstants.GROUP_ID_DEFAULT_PREFIX));
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
		String firstPoint = "{x:"
		+ GroupHierarchyConstant.HIERARCHY_MIDDLE_LINK_X + "(" + memberList.size() + ")"
		+ ",y:"
		+ GroupHierarchyConstant.HIERARCHY_MIDDLE_LINK_Y + "(" + memberList.size() + ")"
		+ "}";

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
		String linkJs = "iw.group.linkList.link_{0}_{1}=iw.group.link(iw.group.groupList.group_{0},iw.group.groupList.group_{1},{2});\n";
		return MessageFormat.format(linkJs, responsible, user.getId(), points);
	}

	public ArrayNode getOrgEntiteOrganisationnelle() {
		List<OrgEntiteOrganisationnelle> orgEntiteOrganisationnelleList = businessService.getOrgEntiteOrganisationnelleList();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for (OrgEntiteOrganisationnelle orgEntiteOrganisationnelle : orgEntiteOrganisationnelleList) {
			ObjectNode node = array.addObject();
			node.put("id", orgEntiteOrganisationnelle.getEntiteOrganisationnelleId());
			node.put("name", orgEntiteOrganisationnelle.getNomEntite());
			OrgEntiteOrganisationnelle entiteOrgaSuiv = orgEntiteOrganisationnelle.getIdEntiteOrgaSuiv();
			Boolean root = false;
			if (entiteOrgaSuiv != null) {
				node.put("parent", entiteOrgaSuiv.getEntiteOrganisationnelleId());
			} else {
				root = true;
				node.put("parent", "");
			}
			node.put("root", root);
		}
		return array;
	}

	public ArrayNode getRhCollaborateur() {
		List<RhCollaborateur> rhCollaborateurList = businessService.getRhCollaborateurList();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for (RhCollaborateur rhCollaborateur : rhCollaborateurList) {
			ObjectNode node = array.addObject();
			node.put("id", rhCollaborateur.getId());
			OrgEntiteOrganisationnelle entiteOrganisationnelle = rhCollaborateur.getIdEntiteOrganisationnelle();
			if (entiteOrganisationnelle != null) {
				node.put("direction", entiteOrganisationnelle.getEntiteOrganisationnelleId());
				if (Objects.equals(entiteOrganisationnelle.getIdResponsable(), rhCollaborateur.getId())) {
					node.put("responsable", true);
				} else {
					node.put("responsable", false);
				}
			} else {
				node.put("direction", "");
			}
			RhPosteOccupe posteOccupe = rhCollaborateur.getIdPosteOccupe();
			if (posteOccupe != null) {
				node.put("poste", posteOccupe.getIdPosteOccupe());
			} else {
				node.put("poste", "");
			}
			node.put("firstName", rhCollaborateur.getNom());
			node.put("lastName", rhCollaborateur.getPrenom());
			if(rhCollaborateur.getActIdUser() == null){
				node.put("image", "../../resources/img/avatar.png");
				node.put("actIdUser", "");
			}else{
				User user = activitiService.getUser(rhCollaborateur.getActIdUser());
				node.put("image", this.getAvatarAsDataImage(rhCollaborateur.getActIdUser()));
				node.put("actIdUser", rhCollaborateur.getActIdUser());
			}
		}
		return array;
	}

	public ArrayNode getRhPosteOccupe() {
		List<RhPosteOccupe> rhPosteOccupeList = businessService.getRhPosteOccupeList();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for (RhPosteOccupe rhPosteOccupe : rhPosteOccupeList) {
			ObjectNode node = array.addObject();
			node.put("id", rhPosteOccupe.getIdPosteOccupe());
			node.put("description", rhPosteOccupe.getDescription());
		}
		return array;
	}

	public ArrayNode getUser() {
		List<User> userList = activitiService.getUserList();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for (User user : userList) {
			ObjectNode node = array.addObject();
			node.put("id", user.getId());
			node.put("firstName", user.getFirstName());
			node.put("lastName", user.getLastName());
		}
		return array;
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
		String memberJs = "iw.group.groupList.group_{0}=iw.group.member({1}, {2}, ''{0}'', ''{3}'', ''{6}'', {4}, {5});\n";
		return MessageFormat.format(memberJs, user.getId(), String.valueOf(x), String.valueOf(y), user.getFirstName() + " " + user.getLastName(), bgColor, borderColor, image);
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

	public String getEntiteOrganisationnelLinks() {
		return entiteOrganisationnelLinks;
	}

	public void setEntiteOrganisationnelLinks(String entiteOrganisationnelLinks) {
		this.entiteOrganisationnelLinks = entiteOrganisationnelLinks;
	}

	public String getCollaborateurLinks() {
		return collaborateurLinks;
	}

	public void setCollaborateurLinks(String collaborateurLinks) {
		this.collaborateurLinks = collaborateurLinks;
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
			List<RhCollaborateur> rhCollaborateurList = businessService.getRhCollaborateurList();
			List<OrgEntiteOrganisationnelle> orgEntiteOrganisationnelleList = businessService.getOrgEntiteOrganisationnelleList();
			List<Integer> newListCollaborateur=new ArrayList<>();
			List<Integer> newListEntity=new ArrayList<>();
			Map<String, Integer> d = new HashMap<>();
			for (JsonNode node : mapper.readTree(entiteOrganisationnelLinks)) {
				OrgEntiteOrganisationnelle orgEntiteOrganisationnelle;
				if(node.get("id").asText().startsWith("new"))
					orgEntiteOrganisationnelle = new OrgEntiteOrganisationnelle();
				else
					orgEntiteOrganisationnelle = businessService.getOrgEntiteOrganisationnelleById(node.get("id").asInt());
				orgEntiteOrganisationnelle.setNomEntite(node.get("name").asText());
				if(node.get("parent").asText().startsWith("new"))
					orgEntiteOrganisationnelle.setIdEntiteOrgaSuiv(businessService.getOrgEntiteOrganisationnelleById(d.get(node.get("parent").asText())));
				else
					orgEntiteOrganisationnelle.setIdEntiteOrgaSuiv(businessService.getOrgEntiteOrganisationnelleById(node.get("parent").asInt()));
				orgEntiteOrganisationnelle.setIdResponsable(null);
				businessService.saveOrgEntiteOrganisationnelle(orgEntiteOrganisationnelle);
				newListEntity.add(orgEntiteOrganisationnelle.getEntiteOrganisationnelleId());
				if(node.get("id").asText().startsWith("new"))
					d.put(node.get("id").asText(), orgEntiteOrganisationnelle.getEntiteOrganisationnelleId());
			}
			List<String> act_id_users = new ArrayList<>();
			Boolean error_act_id_users = false;
			for (JsonNode node : mapper.readTree(collaborateurLinks)) {
				RhCollaborateur rhCollaborateur;
				if(node.get("id").asText().startsWith("new"))
					rhCollaborateur = new RhCollaborateur();
				else
					rhCollaborateur = businessService.getRhCollaborateurById(node.get("id").asInt());
				rhCollaborateur.setNom(node.get("firstName").asText());
				rhCollaborateur.setPrenom(node.get("lastName").asText());
				if(node.get("post") != null)
					rhCollaborateur.setIdPosteOccupe(businessService.getRhPosteOccupeById(node.get("post").asInt()));
				String actIdUser = node.get("actIdUser").asText();
				if(actIdUser.length() > 0){
					if(act_id_users.contains(actIdUser)){
						rhCollaborateur.setActIdUser(null);
						error_act_id_users = true;
					}else{
						act_id_users.add(actIdUser);
						rhCollaborateur.setActIdUser(actIdUser);
					}
				}else{
					rhCollaborateur.setActIdUser(null);
					if (node.get("createActIdUser").asBoolean()){
						String userId = node.get("firstName").asText().trim() + "." + node.get("lastName").asText().trim();
						User userExiste = activitiService.getUser(userId);
						Integer cnt = 1;
						String cUserId = userId;
						while(userExiste != null){
							userId = cUserId + cnt;
							cnt ++;
							userExiste = activitiService.getUser(userId);
						}
						User user = activitiService.newUser(userId);
						user.setFirstName(node.get("firstName").asText());
						user.setLastName(node.get("lastName").asText());
						user.setPassword(userId);
						activitiService.saveUser(user);
						IwUserDetails userDetails = new IwUserDetails();
						userDetails.setIwActIdUser(userId);
						userDetails.setIwActive(true);
						userDetails.setIwVisible(true);
						userDetails.setIwTypeAuthentification("db");
						businessService.saveIwUserDetails(userDetails);
						rhCollaborateur.setActIdUser(userId);
					}
				}
				OrgEntiteOrganisationnelle orgEntiteOrganisationnelleParent;
				if(node.get("direction").asText().startsWith("new"))
					orgEntiteOrganisationnelleParent = businessService.getOrgEntiteOrganisationnelleById(d.get(node.get("direction").asText()));
				else
					orgEntiteOrganisationnelleParent = businessService.getOrgEntiteOrganisationnelleById(node.get("direction").asInt());
				rhCollaborateur.setIdEntiteOrganisationnelle(orgEntiteOrganisationnelleParent);
				businessService.saveRhCollaborateur(rhCollaborateur);
				newListCollaborateur.add(rhCollaborateur.getId());
				if (node.get("responsable").asBoolean())
					orgEntiteOrganisationnelleParent.setIdResponsable(rhCollaborateur.getId());
				businessService.saveOrgEntiteOrganisationnelle(orgEntiteOrganisationnelleParent);
			}

			for (RhCollaborateur collaborateur : rhCollaborateurList) {
				if(!newListCollaborateur.contains(collaborateur.getId()))
					businessService.removeRhCollaborateur(collaborateur);
			}
			for (OrgEntiteOrganisationnelle entity : orgEntiteOrganisationnelleList) {
				if(!newListEntity.contains(entity.getEntiteOrganisationnelleId())){
					entity.setIdEntiteOrgaSuiv(null);
					businessService.saveOrgEntiteOrganisationnelle(entity);
				}
			}
			for (OrgEntiteOrganisationnelle entity : orgEntiteOrganisationnelleList) {
				if(!newListEntity.contains(entity.getEntiteOrganisationnelleId())){
					OrgEntiteOrganisationnelle e = businessService.getOrgEntiteOrganisationnelleById(entity.getEntiteOrganisationnelleId());
					businessService.removeOrgEntiteOrganisationnelle(e);
				}
			}
			FacesUtil.setSessionInfoMessage("succes");
			if(error_act_id_users)
				FacesUtil.setSessionErrorMessage("Un ou plusieurs utilisateurs sont affactés plus d'une fois au même collaborateur");
		} catch (IOException ex) {
			LogManager.getLogger(GroupController.class).error(ex);
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
		LogManager.getLogger(GroupController.class).debug("delete group " + groupId);
	}

	public Boolean isActiveGroup(String groupId) {
		LogManager.getLogger(GroupController.class).debug("is activate group " + groupId);
		return businessService.getGroupDetails(groupId).getIwActive();
	}

	public void activateGroup(String groupId, Boolean activate) {
		IwGroupDetails groupDetail = businessService.getGroupDetails(groupId);
		groupDetail.setIwActive(activate);
		businessService.saveIwGroupDetails(groupDetail);
	}
}
