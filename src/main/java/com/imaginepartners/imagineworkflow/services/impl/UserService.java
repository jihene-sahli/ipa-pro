package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.models.IwRight;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.activiti.engine.identity.User;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements Serializable {

	private static final long serialVersionUID = 1L;

	private User loggedInUser;

	private IwUserDetails loggedInUserDetails;

	private List<IwRight> userRights;

	@Autowired
	private transient ActivitiService activitiService;

	@Autowired
	private transient BusinessService businessService;

	@PostConstruct
	public void init() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (auth.getName() != null) {
				loggedInUser = activitiService.getUser(auth.getName());
				loggedInUserDetails = businessService.getIwUserDetails(auth.getName());
			}
		}
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User user) {
		loggedInUser = user;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public IwUserDetails getLoggedInUserDetails() {
		return loggedInUserDetails;
	}

	public void setLoggedInUserDetails(IwUserDetails loggedInUserDetails) {
		this.loggedInUserDetails = loggedInUserDetails;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<IwRight> getUserRights() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			userRights = businessService.getIwRightList(auth.getName(), (Collection<GrantedAuthority>) auth.getAuthorities());
			return userRights;
		}
		return null;
	}

	public List<String> getUserProcessKeyRights() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> processKeyList = new ArrayList<String>();
		if (auth != null) {
			List<IwRight> userProcessKeyList = businessService.getIwRightList(auth.getName(), (Collection<GrantedAuthority>) auth.getAuthorities());
			if (userProcessKeyList != null && !userProcessKeyList.isEmpty()) {
				for (IwRight right : userProcessKeyList) {
					if (right.getIwProcessKey() != null && !right.getIwProcessKey().isEmpty()) {
						processKeyList.add(right.getIwProcessKey());
					}
				}
			}
			return processKeyList;
		}
		return null;
	}

	public void setUserRights(List<IwRight> userRights) {
		this.userRights = userRights;
	}

}
