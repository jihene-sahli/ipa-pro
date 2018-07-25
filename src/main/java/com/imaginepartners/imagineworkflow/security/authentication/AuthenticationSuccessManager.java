package com.imaginepartners.imagineworkflow.security.authentication;

import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.userdetails.*;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AuthenticationSuccessManager extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private LicenseService licenseService;

	private LdapUserDetailsManager mgr;
	@Autowired
	private ContextSource contextSource;

	private SpringSecurityLdapTemplate template;

	public ContextSource getContextSource() {
		return contextSource;
	}

	private Boolean syncActiveDirectoryGroups;

	private Boolean syncActiveDirectoryUsers;

	private Boolean syncActiveDirectoryMembership;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws ServletException, IOException {
		String authenticationMode = businessService.getConfigValueOrDefault(ConfigConstants.AUTHENTICATION_MODE);
		if ("ad".equals(authenticationMode) || "mixte".equals(authenticationMode)) {
			/**
			 * Load LDAP settings from database
			 */
			syncActiveDirectoryGroups = Boolean.valueOf(businessService.getConfigValue("ldap_sync_ad_groups"));
			syncActiveDirectoryMembership = Boolean.valueOf(businessService.getConfigValue("ldap_sync_ad_membership"));
			syncActiveDirectoryUsers = Boolean.valueOf(businessService.getConfigValue("ldap_sync_ad_users"));
			if (authentication.getPrincipal() instanceof LdapUserDetails) {
				User userInsctance = activitiService.getUser(((UserDetails) authentication.getPrincipal()).getUsername());
				IwUserDetails iwUsrDetails;
				/**
				 * Synchronise users details from LDAP server
				 */
				if(syncActiveDirectoryUsers) {
					String dn = ((LdapUserDetails) authentication.getPrincipal()).getDn();
					String firstName = null;
					String lastName = null;

					InetOrgPerson orgPer = ((InetOrgPerson) authentication.getPrincipal());
					String mail = (orgPer!=null && StringUtils.isNoneBlank(orgPer.getMail()))?orgPer.getMail():"";

					dn = dn.substring(3, dn.indexOf(","));
					if (dn.split(" ").length > 0)
						firstName = dn.split(" ")[0];
					if (dn.split(" ").length > 1)
						lastName = dn.split(" ")[1];
					/**
					 * User doesn't exist in database
					 */
					if(userInsctance == null) {
						userInsctance = activitiService.newUser(((UserDetails) authentication.getPrincipal()).getUsername());
						userInsctance.setFirstName(firstName);
						userInsctance.setLastName(lastName);
						userInsctance.setEmail(mail);
						iwUsrDetails = new IwUserDetails();
						iwUsrDetails.setIwActIdUser(userInsctance.getId());
						iwUsrDetails.setIwActive(true);
						iwUsrDetails.setIwTypeAuthentification("ad");
						activitiService.saveUser(userInsctance);
						businessService.saveIwUserDetails(iwUsrDetails);
						activitiService.createMembership(userInsctance.getId(), businessService.getConfigValue(ConfigConstants.USERS_DEFAULT_GROUP));
						for (GrantedAuthority ga : authentication.getAuthorities()) {
							Group grp = activitiService.getGroup(ga.getAuthority());
							if (grp == null && syncActiveDirectoryGroups) {
								grp = activitiService.newGroup(ga.getAuthority());
								grp.setType(AppConstants.GROUP_USER_NAME);
								grp.setName(ga.getAuthority());
								activitiService.saveGroup(grp);
							}
							if (grp != null && syncActiveDirectoryMembership) {
								if (!activitiService.isMemberOfGroup(userInsctance.getId(),grp.getId())){
									activitiService.createMembership(userInsctance.getId(), grp.getId());
								}
							}
						}
					} else {
						/**
						 * Update user informations if changes was made in the LDAP server
						 */
						userInsctance.setFirstName(firstName);
						userInsctance.setLastName(lastName);
						activitiService.saveUser(userInsctance);
						/**
						 * User already exist in database
						 */
						List<Group> userGroups = activitiService.getUserGroupList(userInsctance.getId());
						/**
						 * Synchronize groups and memberships
						 */
						if (syncActiveDirectoryGroups || syncActiveDirectoryMembership) {
							for (GrantedAuthority ga : authentication.getAuthorities()) {
								boolean addGrp = true;
								Group grp = null;
								for (Group loopGrp : userGroups) {
									if (loopGrp.getId().equals(ga.getAuthority())) {
										addGrp = false;
										grp = loopGrp;
										break;
									}
								}
								if (addGrp && syncActiveDirectoryGroups) {
									grp = activitiService.newGroup(ga.getAuthority());
									grp.setType(AppConstants.GROUP_USER_NAME);
									grp.setName(ga.getAuthority());
									activitiService.saveGroup(grp);
								}
								if (syncActiveDirectoryMembership) {
									if (grp == null) {
										grp = activitiService.getGroup(ga.getAuthority());
									}
									if (grp != null) {
										activitiService.createMembership(userInsctance.getId(), grp.getId());
									}
								}
							}
							for (Group grp : userGroups) {
								boolean removeMembership = true;
								for (GrantedAuthority ga : authentication.getAuthorities()) {
									if (grp.getId().equals(ga.getAuthority())) {
										removeMembership = false;
										break;
									}
								}
								if (removeMembership && syncActiveDirectoryMembership) {
									activitiService.deleteMembership(userInsctance.getId(), grp.getId());
								}
							}
						}
					}
				} else {
					if (userInsctance == null) {
						response.sendRedirect(request.getContextPath() + "/pages/synchronistaionUtilisateurDesactivee.xhtml");
						return;
					}
				}
			}
		}

		/**
		 * Authenticate the user
		 */
		activitiService.setAuthenticatedUserIdForActiviti(((UserDetails) authentication.getPrincipal()).getUsername());

		/**
		 * Check license validation
		 */
		Boolean hasAdmin = false;
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			if ("ROLE_ADMIN".equals(ga.getAuthority()) || "ROLE_SUPER_ADMIN".equals(ga.getAuthority())) {
				hasAdmin = true;
				break;
			}
		}
		if (LicenseUtils.LICENSE_EXPIRED.equals(licenseService.getLicenseStatus())) {
			FacesUtil.setSessionErrorMessage(FacesUtil.getMessage("iw.licence.licenseexpire"));
			if (hasAdmin) {
				response.sendRedirect(request.getContextPath() + "/pages/licence/upload.xhtml");
			} else {
				response.sendRedirect(request.getContextPath() + "/pages/problemeLicence.xhtml");
			}
		} else if (LicenseUtils.NO_LICENSE.equals(licenseService.getLicenseStatus())) {
			FacesUtil.setSessionErrorMessage(FacesUtil.getMessage("iw.licence.paslicense"));
			if (hasAdmin) {
				response.sendRedirect(request.getContextPath() + "/pages/licence/upload.xhtml");
			} else {
				response.sendRedirect(request.getContextPath() + businessService.getConfigValue(ConfigConstants.HOME_PAGE));
			}
		} else {
			response.sendRedirect(request.getContextPath() + businessService.getConfigValue(ConfigConstants.HOME_PAGE));
		}
	}

	public Boolean getSyncActiveDirectoryGroups() {
		return syncActiveDirectoryGroups;
	}

	public void setSyncActiveDirectoryGroups(Boolean syncActiveDirectoryGroups) {
		this.syncActiveDirectoryGroups = syncActiveDirectoryGroups;
	}

	public Boolean getSyncActiveDirectoryUsers() {
		return syncActiveDirectoryUsers;
	}

	public void setSyncActiveDirectoryUsers(Boolean syncActiveDirectoryUsers) {
		this.syncActiveDirectoryUsers = syncActiveDirectoryUsers;
	}

	public Boolean getSyncActiveDirectoryMembership() {
		return syncActiveDirectoryMembership;
	}

	public void setSyncActiveDirectoryMembership(Boolean syncActiveDirectoryMembership) {
		this.syncActiveDirectoryMembership = syncActiveDirectoryMembership;
	}
}
