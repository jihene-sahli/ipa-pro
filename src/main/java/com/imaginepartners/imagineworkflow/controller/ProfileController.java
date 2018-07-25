package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwList;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhFormation;
import com.imaginepartners.imagineworkflow.models.rh.RhPosteOccupe;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("view")
public class ProfileController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private User user;

	private UploadedFile avatarFile;

	private IwUserDetails iwUserDetails;

	private String passwordConfirmation;

	@Autowired
	private ActivitiService activitiService;

	private List<Group> userGroupList;

	private List<Group> userGroupListClone;

	private Map<String, IwUserDetails> userDetailsMap = new HashMap<String, IwUserDetails>();

	@Autowired
	@Qualifier("pwdEncoder")
	transient private BCryptPasswordEncoder pwdEncoder;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserService userService;

	private RhCollaborateur collaborateur;

	private RhPosteOccupe posteOccupe;

	private List<RhPosteOccupe> evolutions;

	private List<RhFormation> formations;


	public ProfileController() {
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	@PostConstruct
	public void init() {
		user = userService.getLoggedInUser();
		userId = user.getId();
		iwUserDetails = userService.getLoggedInUserDetails();
		if (iwUserDetails == null) {
			iwUserDetails = new IwUserDetails();
			iwUserDetails.setIwActive(true);
		}
		userGroupList = activitiService.getUserGroupList(userId, businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
		userGroupListClone = new ArrayList(userGroupList);
		getCollaboratorInformation();
	}

	private void getCollaboratorInformation() {
		formations = new ArrayList<RhFormation>();
		evolutions = new ArrayList<RhPosteOccupe>();
		collaborateur = businessService.getRhCollaborateurByActIdUser(userId);

		if (collaborateur != null) {
			formations.addAll(collaborateur.getListRhFormations());
			evolutions.addAll(collaborateur.getListPosteOccupes());
			posteOccupe = collaborateur.getIdPosteOccupe();
		}
	}

	public String saveProfil() {
		if (StringUtils.isBlank(userId)) {
			List<User> existingUsers = activitiService.getUserListByEmail(user.getEmail(), new String[]{user.getId()});
			if (!existingUsers.isEmpty()) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.utilisateur.utilisateurmailexistedeja", user.getEmail()));
				return null;
			}
		}
		if (avatarFile != null && avatarFile.getFileName() != null && !avatarFile.getFileName().isEmpty()) {
			FacesUtil.setSessionInfoMessage(avatarFile.getContentType());
			Picture pic = new Picture(avatarFile.getContents(), avatarFile.getContentType());
			activitiService.saveUser(user, pic);
			FacesUtil.setSessionUpdateElement(":templateForm:profilAvatar");
		} else {
			activitiService.saveUser(user);
		}
		businessService.saveIwUserDetails(iwUserDetails);
		return "formulaire?faces-redirect=true";
	}

	public UploadedFile getAvatarFile() {
		return avatarFile;
	}

	public void setAvatarFile(UploadedFile avatarFile) {
		this.avatarFile = avatarFile;
	}

	public void handleAvatarUpload(FileUploadEvent event) {
		FacesUtil.setAjaxInfoMessage("success");
		avatarFile = event.getFile();
	}

	public IwUserDetails getIwUserDetails() {
		return iwUserDetails;
	}

	public void setIwUserDetails(IwUserDetails iwUserDetails) {
		this.iwUserDetails = iwUserDetails;
	}

	public BCryptPasswordEncoder getPwdEncoder() {
		return pwdEncoder;
	}

	public void setPwdEncoder(BCryptPasswordEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
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

	public List<Group> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Group> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public boolean containsGroup(List<Group> grpList, Group grp) {
		for (Group loopGroup : grpList) {
			if (loopGroup.getId().equals(grp.getId())) {
				return true;
			}
		}
		return false;
	}

	public List<Group> getUserGroupListClone() {
		return userGroupListClone;
	}

	public void setUserGroupListClone(List<Group> userGroupListClone) {
		this.userGroupListClone = userGroupListClone;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public IwUserDetails getUserDetails(String userId) {
		if (userDetailsMap.get(userId) == null) {
			userDetailsMap.put(userId, businessService.getIwUserDetails(userId));
		}
		return userDetailsMap.get(userId);
	}

	public boolean isUserActive(String userId) {
		IwUserDetails userDetails = getUserDetails(userId);
		if (userDetails == null) {
			return false;
		}
		return userDetails.getIwActive();
	}

	public void disable() {
		String userId = FacesUtil.getUrlParam("user");
		if (StringUtils.isNotBlank(userId)) {
			IwUserDetails userDetails = getUserDetails(userId);
			userDetails.setIwActive(false);
			businessService.saveIwUserDetails(userDetails);
		}
	}

	public void enable() {
		String userId = FacesUtil.getUrlParam("user");
		if (StringUtils.isNotBlank(userId)) {
			IwUserDetails userDetails = getUserDetails(userId);
			userDetails.setIwActive(true);
			businessService.saveIwUserDetails(userDetails);
		}
	}

	public void remove() {
		String userId = FacesUtil.getUrlParam("user");
		if (StringUtils.isNotBlank(userId)) {
			businessService.deleteIwUserDetails(userId);
			activitiService.deleteUser(userId);
		}
	}

	public Map<String, IwUserDetails> getUserDetailsMap() {
		return userDetailsMap;
	}

	public void setUserDetailsMap(Map<String, IwUserDetails> userDetailsMap) {
		this.userDetailsMap = userDetailsMap;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public IwList getTimeZoneList() {
		return businessService.getIwListByName("TIMEZONE");
	}

	public RhCollaborateur getCollaborateur() {
		return collaborateur;
	}

	public void setCollaborateur(RhCollaborateur collaborateur) {
		this.collaborateur = collaborateur;
	}

	public RhPosteOccupe getPosteOccupe() {
		return posteOccupe;
	}

	public void setPosteOccupe(RhPosteOccupe posteOccupe) {
		this.posteOccupe = posteOccupe;
	}

	public List<RhPosteOccupe> getEvolutions() {
		return evolutions;
	}

	public void setEvolutions(List<RhPosteOccupe> evolutions) {
		this.evolutions = evolutions;
	}

	public List<RhFormation> getFormations() {
		return formations;
	}

	public void setFormations(List<RhFormation> formations) {
		this.formations = formations;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
