package com.imaginepartners.imagineworkflow.security.authentication;

import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.User;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

public class ImagineworkflowAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private ActiveDirectoryLdapAuthenticationProvider ldapActiveDirectoryAuthenticationProvider;

	@Autowired
	CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	@Qualifier("dataSourceAuthenticationManager")
	private AuthenticationManager dataSourceAuthenticationManager;

	@Autowired
	@Qualifier("inMemoryRescueAuthenticationManager")
	private AuthenticationManager inMemoryRescueAuthenticationManager;

	@Autowired
	private LicenseService licenseService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	private Boolean rescueMode;

	/**
	 * Depending on the authentication mode in the application settings, execute either AuthenticationManager.authenticate() or
	 * ActiveDirectoryLdapAuthenticationProvider.authenticate()
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			if (rescueMode != null && rescueMode) {
				/**
				 * Using Rescue Mode, this mode is used for troubleshooting the application
				 */
				return inMemoryRescueAuthenticationManager.authenticate(authentication);
			} else {
				String authenticationMode = businessService.getConfigValueOrDefault(ConfigConstants.AUTHENTICATION_MODE);
				/**
				 * Local Strategy for authentication
				 */
				if ("db".equals(authenticationMode)) {
					return dataSourceAuthenticationManager.authenticate(authentication);
				} else {
					String domain = businessService.getConfigValue("ldap_domain");
					String url = businessService.getConfigValue("ldap_server");
					boolean AuthenticationRequestCredentials = true;
					/**
					 * Instantiating provider and bootstrap settings
					 */
					ldapActiveDirectoryAuthenticationProvider = new ActiveDirectoryLdapAuthenticationProvider(domain, url);
					ldapActiveDirectoryAuthenticationProvider.setUseAuthenticationRequestCredentials(AuthenticationRequestCredentials);
					ldapActiveDirectoryAuthenticationProvider.setConvertSubErrorCodesToExceptions(AuthenticationRequestCredentials);
					ldapActiveDirectoryAuthenticationProvider.setUserDetailsContextMapper(userDetailsContextMapper);
					/**
					 * LDAP or Mixe Strategy for authentication
					 */
					if ("ad".equals(authenticationMode)) {
						if (licenseService.getHasModuleLdap()) {
							return ldapActiveDirectoryAuthenticationProvider.authenticate(authentication);
						} else {
							throw new ProviderNotFoundException(FacesUtil.getMessage("iw.license.paslicensepourmodule", "ActiveDirectory/LDAP"));
						}
					}
					if ("mixte".equals(authenticationMode)) {
						/**
						 * Check if the user exist in the database
						 */
						User usr = activitiService.getUser(String.valueOf(authentication.getPrincipal()));
						IwUserDetails usrDetails = businessService.getIwUserDetails(String.valueOf(authentication.getPrincipal()));
						if (usr != null && usrDetails != null) {
							if ("ad".equals(usrDetails.getIwTypeAuthentification())) {
								return ldapActiveDirectoryAuthenticationProvider.authenticate(authentication);
							} else {
								return dataSourceAuthenticationManager.authenticate(authentication);
							}
						} else {
							return ldapActiveDirectoryAuthenticationProvider.authenticate(authentication);
						}
					}
				}
			}
			throw new ProviderNotFoundException(FacesUtil.getMessage("iw.authentication.aucunemethodeauthentificationnestpossible"));
		} catch (Exception ex) {
			LogManager.getLogger(ImagineworkflowAuthenticationProvider.class).error(null, ex);
			throw new AuthenticationServiceException(ex.getLocalizedMessage());
		}
	}

	@Override
	public boolean supports(Class<?> type) {
		return true;
	}

	public AuthenticationManager getDataSourceAuthenticationManager() {
		return dataSourceAuthenticationManager;
	}

	public void setDataSourceAuthenticationManager(AuthenticationManager dataSourceAuthenticationManager) {
		this.dataSourceAuthenticationManager = dataSourceAuthenticationManager;
	}

	public ActiveDirectoryLdapAuthenticationProvider getLdapActiveDirectoryAuthenticationProvider() {
		return ldapActiveDirectoryAuthenticationProvider;
	}

	public void setLdapActiveDirectoryAuthenticationProvider(ActiveDirectoryLdapAuthenticationProvider ldapActiveDirectoryAuthenticationProvider) {
		this.ldapActiveDirectoryAuthenticationProvider = ldapActiveDirectoryAuthenticationProvider;
	}

	public Authentication createAuthenticationWithDomain(Authentication authentication, String domain) {
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal() + "@" + domain,
		authentication.getCredentials(),
		authentication.getAuthorities());
	}

	public AuthenticationManager getInMemoryRescueAuthenticationManager() {
		return inMemoryRescueAuthenticationManager;
	}

	public void setInMemoryRescueAuthenticationManager(AuthenticationManager inMemoryRescueAuthenticationManager) {
		this.inMemoryRescueAuthenticationManager = inMemoryRescueAuthenticationManager;
	}

	public Boolean getRescueMode() {
		return rescueMode;
	}

	public void setRescueMode(Boolean rescueMode) {
		this.rescueMode = rescueMode;
	}

	public LicenseService getLicenseService() {
		return licenseService;
	}

	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}
}
