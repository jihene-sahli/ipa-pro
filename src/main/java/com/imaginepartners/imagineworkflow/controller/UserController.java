package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class UserController implements Serializable {

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

	public UserController() {

	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	@PostConstruct
	public void init() {
		userId = FacesUtil.getUrlParam("user");
		if (StringUtils.isNotBlank(userId)) {
			user = activitiService.getUser(userId);
			iwUserDetails = businessService.getIwUserDetails(userId);
			if (iwUserDetails == null) {
				iwUserDetails = new IwUserDetails();
				iwUserDetails.setIwActive(true);
			}
			userGroupList = activitiService.getUserGroupList(userId, businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
			userGroupListClone = new ArrayList(userGroupList);
		} else {
			user = activitiService.newUser("");
			iwUserDetails = new IwUserDetails();
			iwUserDetails.setIwActive(true);
			userGroupList = new ArrayList<Group>();
			userGroupListClone = new ArrayList<Group>();
		}
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

	public String saveUser(Boolean newForm) {
		String generatedPassword = iwUserDetails.getIwGeneratedPasswordPlain();
		if (StringUtils.isBlank(userId)) {
			List<User> existingUsers = activitiService.getUserListCaseInsensitive(user.getId());
			if (!existingUsers.isEmpty()) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.utilisateur.utilisateurexistedeja", user.getId()));
				return null;
			}
			existingUsers = activitiService.getUserListByEmail(user.getEmail());
			if (!existingUsers.isEmpty()) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.utilisateur.utilisateurmailexistedeja", user.getEmail()));
				return null;
			}
			if (StringUtils.isBlank(generatedPassword)) {
				generatedPassword = RandomStringUtils.randomAlphanumeric(10);
			}
		}
		if (StringUtils.isNotBlank(generatedPassword)) {
			user.setPassword(pwdEncoder.encode(generatedPassword));
		}
		if (avatarFile != null && avatarFile.getFileName() != null && !avatarFile.getFileName().isEmpty()) {
			FacesUtil.setSessionInfoMessage(avatarFile.getContentType());
			Picture pic = new Picture(avatarFile.getContents(), avatarFile.getContentType());
			activitiService.saveUser(user, pic);
		} else {
			activitiService.saveUser(user);
		}
		if (StringUtils.isBlank(userId)) {
			activitiService.createMembership(user.getId(), businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
			iwUserDetails.setIwActIdUser(user.getId());
		}
		if (StringUtils.isBlank(iwUserDetails.getIwTypeAuthentification())) {
			iwUserDetails.setIwTypeAuthentification(businessService.getConfigValue(ConfigConstants.AUTHENTICATION_MODE));
		}
		businessService.saveIwUserDetails(iwUserDetails);
		if (userGroupList != null) {
			for (Group grp : userGroupList) {
				if (!containsGroup(userGroupListClone, grp)) {
					activitiService.createMembership(user.getId(), grp.getId());
				}
			}
		}
		if (userGroupListClone != null) {
			for (Group grp : userGroupListClone) {
				if (!grp.getId().equals(businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP)) && !containsGroup(userGroupList, grp)) {
					activitiService.deleteMembership(userId, grp.getId());
				}
			}
		}
		if (newForm) {
			return "formulaire?faces-redirect=true";
		} else {
			return "details?user=" + user.getId() + "&faces-redirect=true";
		}
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

	/**
	 * Check if group list contains a specific group
	 * To use to create membership for users
	 * @param grpList
	 * @param grp
	 * @return true if the group exist, false else
	 */
	public boolean containsGroup(List<Group> grpList, Group grp) {
		if (grpList != null) {
			for (Group loopGroup : grpList) {
				if (loopGroup.getId().equals(grp.getId())) {
					return true;
				}
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

	public void login() {
		String userId = FacesUtil.getUrlParam("user");
		User usr = activitiService.getUser(userId);
		List<Group> grps = activitiService.getUserGroupList(userId);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Group grp : grps) {
			GrantedAuthority authority = new SimpleGrantedAuthority(grp.getId());
			authorities.add(authority);
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(userId, usr.getPassword(), authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
		userService.init();
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
}
