package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.activiti.engine.identity.User;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;

@Controller("authenticationController")
@Scope("view")
public class AuthenticationController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String password;

	private String newPassword;

	private String newPasswordConfirmation;

	@Autowired
	@Qualifier("pwdEncoder")
	transient private BCryptPasswordEncoder pwdEncoder;

	@Autowired
	transient private LicenseService licenseService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivitiService activitiService;

	public AuthenticationController() {
	}

	public String doLogin() throws ServletException, IOException {

		LogManager.getLogger(AuthenticationController.class).debug("----> login request");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		boolean canLogin = false;
		try {
			canLogin = licenseService.canLogin();
		} catch (Exception exp) {
			FacesUtil.setAjaxErrorMessage(exp.getLocalizedMessage());
			LogManager.getLogger(AuthenticationController.class).debug(exp);
		}

		if (!canLogin && LicenseUtils.LICENSE_VALID.equals(licenseService.getLicenseStatus())) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.nbrmaxutilisateurssimultanesatteint"));
			return null;
		} else {
			RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
			.getRequestDispatcher("/login");
			dispatcher.forward((ServletRequest) context.getRequest(),
			(ServletResponse) context.getResponse());
			FacesContext.getCurrentInstance().responseComplete();
		}
		return null;
	}

	public void afterPhase(PhaseEvent event) {
	}

	/* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     *
     * Do something before rendering phase.
     */
	public void beforePhase(PhaseEvent event) {
		Exception e = (Exception) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (e instanceof BadCredentialsException) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
			WebAttributes.AUTHENTICATION_EXCEPTION, null);
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
			FacesUtil.getMessage("iw.erreur"), FacesUtil.getMessage("iw.message.mauvaisnomutilisateurmotpasse")));
		}
	}

	/* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     *
     * In which phase you want to interfere?
     */
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	public void updateMessages() throws Exception {
		Exception ex = (Exception) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (ex != null) {
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
		}
	}

	public void changePassword() {

		if (!newPassword.equals(newPasswordConfirmation)) {
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
			FacesUtil.getMessage("iw.erreur"), FacesUtil.getMessage("iw.message.confirmationmotpasseincorrect")));
			return;
		}

		User user = userService.getLoggedInUser();
		IwUserDetails userDetails = businessService.getIwUserDetails(user.getId());

		if (!pwdEncoder.matches(password, user.getPassword())) {
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("iw.erreur"), FacesUtil.getMessage("iw.message.motpassenonvalide")));
		} else if (!pwdEncoder.matches(newPassword, user.getPassword())) {
			user.setPassword(pwdEncoder.encode(newPassword));
			userDetails.setIwPasswordChangeDate(new Date());
			activitiService.saveUser(user);
			businessService.saveIwUserDetails(userDetails);

			FacesUtil.redirect(FacesUtil.getContextPath() + "/logout");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("iw.erreur"), FacesUtil.getMessage("iw.message.nouveauetancienpassedifferents")));
		}
	}

	public void doLogout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public BCryptPasswordEncoder getPwdEncoder() {
		return pwdEncoder;
	}

	public void setPwdEncoder(BCryptPasswordEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	public LicenseService getLicenseService() {
		return licenseService;
	}

	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}
}
