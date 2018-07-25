package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwConfig;
import com.imaginepartners.imagineworkflow.models.IwUserDetails;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.User;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.LogManager;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@Scope("view")
public class ConfigController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	private List<IwConfig> configList;

	private List<IwConfig> configListPerso;

	private List<IwConfig> configListMailSending;

	private List<IwConfig> configListAdvance;

	private List<IwConfig> configListAuthentication;

	private List<IwConfig> lDapOrMixteConfigListe;

	private List<IwConfig> configListSmtp;

	private List<IwConfig> configListFileExportPaths;

	private DualListModel<User> userList;

	private List<User> userListSource;

	private List<User> userListTarget;

	private IwUserDetails iwUserDetails;

	@Autowired
	@Qualifier("pwdEncoder")
	transient private BCryptPasswordEncoder pwdEncoder;

	private IwConfig authenticationConfigSelected;

	private UploadedFile favicon;

	private UploadedFile companyLogo;

	private UploadedFile loginLogo;

	private boolean lDapOrMixteSelected;

	public ConfigController() {
	}

	@PostConstruct
	public void init() {
		userListSource = new ArrayList<>();
		userListTarget = new ArrayList<>();

		if (businessService.getConfigValue("authentication_mode").equalsIgnoreCase("ad") || businessService.getConfigValue("authentication_mode").equalsIgnoreCase("mixte"))
			lDapOrMixteSelected = true;
		else lDapOrMixteSelected = false;
		configListPerso = businessService.getConfigList(Arrays.asList(ConfigConstants.COMPANY_NAME,
			ConfigConstants.COMPANY_SLOGAN,
			ConfigConstants.IMAGINEWORKFLOW_TITLE,
			ConfigConstants.COMPANY_THEME,
			ConfigConstants.COMPANY_LOGO,
			ConfigConstants.COMPANY_FAVICON
		));
		configListAdvance = businessService.getConfigList(Arrays.asList(
			ConfigConstants.UPLOAD_DIRECTORY_NAME,
			ConfigConstants.LOGIN_LOGO,
			ConfigConstants.DISPLAY_COMPLETED_TASK,
			ConfigConstants.DISPLAY_IMPLIQUE_TASK,
			ConfigConstants.DISPLAY_INSTANCE_CREATION_DIALOG,
			ConfigConstants.DISPLAY_STANDALONE_CREATION_DIALOG,
			ConfigConstants.DISPLAY_FOLDER_NUMBER,
			ConfigConstants.DISPLAY_TASK_NUMBER,
			ConfigConstants.ENABLE_STICKY_TITLE,
			ConfigConstants.DISPLAY_TRADUCTION_LIST,
			ConfigConstants.DISPLAY_PROCESS_PROGRESS_BAR,
			ConfigConstants.SHOW_AGENDA_RESERVATION
		));
		configListAuthentication = businessService.getConfigList(Arrays.asList(
			ConfigConstants.AUTHENTICATION_MODE,
			ConfigConstants.LDAP_DOMAIN,
			ConfigConstants.LDAP_SERVER,
			ConfigConstants.LDAP_SYNC_AD_GROUPS,
			ConfigConstants.LDAP_SYNC_AD_USERS,
			ConfigConstants.LDAP_SYNC_AD_MEMBERSHIP
		));
		authenticationConfigSelected = configListAuthentication.get(0);

		lDapOrMixteConfigListe = businessService.getConfigList(Arrays.asList(
			ConfigConstants.LDAP_DOMAIN,
			ConfigConstants.LDAP_SERVER,
			ConfigConstants.LDAP_SYNC_AD_GROUPS,
			ConfigConstants.LDAP_SYNC_AD_USERS,
			ConfigConstants.LDAP_SYNC_AD_MEMBERSHIP
		));

		configList = businessService.getConfigList(Arrays.asList(ConfigConstants.COMPANY_NAME,
			ConfigConstants.COMPANY_SLOGAN,
			ConfigConstants.IMAGINEWORKFLOW_TITLE,
			ConfigConstants.COMPANY_THEME,
			ConfigConstants.COMPANY_LOGO,
			ConfigConstants.UPLOAD_DIRECTORY_NAME,
			ConfigConstants.LOGIN_LOGO,
			ConfigConstants.COMPANY_FAVICON,
			ConfigConstants.AUTHENTICATION_MODE,
			ConfigConstants.DISPLAY_COMPLETED_TASK,
			ConfigConstants.DISPLAY_IMPLIQUE_TASK,
			ConfigConstants.DISPLAY_INSTANCE_CREATION_DIALOG,
			ConfigConstants.DISPLAY_STANDALONE_CREATION_DIALOG,
			ConfigConstants.DISPLAY_FOLDER_NUMBER,
			ConfigConstants.DISPLAY_TASK_NUMBER,
			ConfigConstants.ENABLE_STICKY_TITLE,
			ConfigConstants.DISPLAY_TRADUCTION_LIST,
			ConfigConstants.DISPLAY_PROCESS_PROGRESS_BAR
		));

		configListMailSending = businessService.getConfigList(Arrays.asList(
			ConfigConstants.SMTP_MAIL_SERVER_HOST,
			ConfigConstants.SMTP_MAIL_SERVER_PORT,
			ConfigConstants.SMTP_MAIL_SERVER_USERNAME,
			ConfigConstants.SMTP_MAIL_SERVER_PASSWORD,
			ConfigConstants.SMTP_MAIL_SERVER_USE_SSL,
			ConfigConstants.SMTP_MAIL_SERVER_USE_TLS,
			ConfigConstants.SMTP_MAIL_SERVER_DEFAULT_FROM
		));

		configListFileExportPaths = businessService.getConfigList(Arrays.asList(
		ConfigConstants.FILE_EXPORT_PATH_CSV,
		ConfigConstants.FILE_EXPORT_PATH_EXCEL,
		ConfigConstants.FILE_EXPORT_PATH_PDF,
		ConfigConstants.FILE_EXPORT_PATH_LOG
		));

		userListSource =  activitiService.getUserList();
		userList = new DualListModel<>(userListSource, userListTarget);
	}

	public void saveConfig() {
		businessService.saveIwConfig(authenticationConfigSelected);
		for (IwConfig config : configListPerso) {
			if (ConfigConstants.COMPANY_FAVICON.equals(config.getConfigName())) {
				String fileName = saveFile(favicon, ConfigConstants.COMPANY_FAVICON);
				if (fileName != null) {
					config.setConfigValue(fileName);
				}
			}
			if (ConfigConstants.COMPANY_LOGO.equals(config.getConfigName())) {
				String fileName = saveFile(companyLogo, ConfigConstants.COMPANY_LOGO);
				if (fileName != null) {
					config.setConfigValue(fileName);
				}
			}
			businessService.saveIwConfig(config);
		}
		for (IwConfig config : configListAdvance) {
			if (ConfigConstants.LOGIN_LOGO.equals(config.getConfigName())) {
				String fileName = saveFile(loginLogo, ConfigConstants.LOGIN_LOGO);
				if (fileName != null) {
					config.setConfigValue(fileName);
				}
			}
			if (ConfigConstants.UPLOAD_DIRECTORY_NAME.equals(config.getConfigName())) {
				String newUploadedFile = config.getConfigValue();
				File newUploadedFileFolder = new File(newUploadedFile);
				if (!newUploadedFileFolder.exists()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.repertory_not_found"));
					return;
				}
				if (!newUploadedFileFolder.canWrite()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.no_right_on_repertory"));
					return;
				}
			}
			businessService.saveIwConfig(config);
		}
		for (IwConfig config : lDapOrMixteConfigListe) {
			businessService.saveIwConfig(config);
		}

		for (IwConfig config : configListMailSending) {
			businessService.saveIwConfig(config);
		}

		for (IwConfig config : configListFileExportPaths) {
			if (ConfigConstants.FILE_EXPORT_PATH_CSV.equals(config.getConfigName())) {
				String newUploadedFile = config.getConfigValue();
				File newUploadedFileFolder = new File(newUploadedFile);
				if (!newUploadedFileFolder.exists()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.repertory_not_found"));
					return;
				}
				if (!newUploadedFileFolder.canWrite()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.no_right_on_repertory"));
					return;
				}
			}

			if (ConfigConstants.FILE_EXPORT_PATH_EXCEL.equals(config.getConfigName())) {
				String newUploadedFile = config.getConfigValue();
				File newUploadedFileFolder = new File(newUploadedFile);
				if (!newUploadedFileFolder.exists()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.repertory_not_found"));
					return;
				}
				if (!newUploadedFileFolder.canWrite()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.no_right_on_repertory"));
					return;
				}
			}

			if (ConfigConstants.FILE_EXPORT_PATH_PDF.equals(config.getConfigName())) {
				String newUploadedFile = config.getConfigValue();
				File newUploadedFileFolder = new File(newUploadedFile);
				if (!newUploadedFileFolder.exists()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.repertory_not_found"));
					return;
				}
				if (!newUploadedFileFolder.canWrite()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.no_right_on_repertory"));
					return;
				}
			}

			if (ConfigConstants.FILE_EXPORT_PATH_LOG.equals(config.getConfigName())) {
				String newUploadedFile = config.getConfigValue();
				File newUploadedFileFolder = new File(newUploadedFile);
				if (!newUploadedFileFolder.exists()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.repertory_not_found"));
					return;
				}
				if (!newUploadedFileFolder.canWrite()) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.configuration.no_right_on_repertory"));
					return;
				}
			}
			businessService.saveIwConfig(config);
		}
	}

	public void generateAndNotifyUsersPasswords() {
		List<User> usersList = new ArrayList<>();
		for (User user: userList.getTarget()) {
			String password;
			iwUserDetails = businessService.getIwUserDetails(user.getId());
			password = RandomStringUtils.randomAlphanumeric(10);
			user.setPassword(pwdEncoder.encode(password));
			iwUserDetails.setIwGeneratedPasswordPlain(password);
			iwUserDetails.setIwPasswordChangeDate(new Date());
			activitiService.saveUser(user);
			businessService.saveEntite(iwUserDetails);
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				usersList.add(user);
			}
		}

		for (User user: usersList) {
			String to;
			IwUserDetails userDetails;
			userDetails = businessService.getIwUserDetails(user.getId());
			String subjectValue = "Votre mot de passe systeme";
			String bodyValue = "Bonjour " + user.getFirstName() + " " + user.getLastName() + " ," +
			"\n\nVotre mot de passe est : " + userDetails.getIwGeneratedPasswordPlain() +
			"\n\nCordialement";
			try {
				to = user.getEmail();

				// Create the email message
				Email email = new SimpleEmail();

				email.setHostName("smtp.gmail.com");
				email.setAuthentication("mailsender@imaginepartners.com", "mailsender2013");
				email.setStartTLSEnabled(true);
				email.setSmtpPort(465);
				email.setFrom("mailsender@imaginepartners.com", "Systeme");
				email.setSubject(subjectValue);
				email.addTo(to, user.getFirstName() + " " + user.getLastName());
				email.setMsg(bodyValue);

				// send the email
				email.setSSLOnConnect(true);
				email.send();

			} catch (ActivitiException e) {
				LogManager.getLogger(ConfigController.class).error(e.getMessage());
			} catch (EmailException e) {
				LogManager.getLogger(ConfigController.class).error("Could not send e-mail in execution " + e.getMessage());
			}
		}
	}


	public String saveFile(UploadedFile uploadedFile, String directoryName) {
		String newFile = null;
		InputStream input = null;
		String destDirPath = businessService.getConfigValue(ConfigConstants.UPLOAD_DIRECTORY_NAME)
		+ File.separator
		+ directoryName
		+ File.separator
		+ new Date().getTime();

		String fileName = destDirPath + File.separator + uploadedFile.getFileName();
		try {
			input = uploadedFile.getInputstream();
			File distDir = new File(destDirPath);
			File file = new File(fileName);

			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream output = new FileOutputStream(file);
			try {
				IOUtils.copy(input, output);
				newFile = fileName;
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
		} catch (IOException ex) {
			LogManager.getLogger(ConfigController.class).error(ex);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LogManager.getLogger(ConfigController.class).error(ex);
			}
		}
		return newFile;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<IwConfig> getConfigList() {
		return configList;
	}

	public void setConfigList(List<IwConfig> configList) {
		this.configList = configList;
	}

	public UploadedFile getFavicon() {
		return favicon;
	}

	public void setFavicon(UploadedFile favicon) {
		this.favicon = favicon;
	}

	public UploadedFile getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(UploadedFile companyLogo) {
		this.companyLogo = companyLogo;
	}

	public UploadedFile getLoginLogo() {
		return loginLogo;
	}

	public void setLoginLogo(UploadedFile loginLogo) {
		this.loginLogo = loginLogo;
	}

	public List<IwConfig> getConfigListPerso() {
		return configListPerso;
	}

	public void setConfigListPerso(List<IwConfig> configListPerso) {
		this.configListPerso = configListPerso;
	}

	public List<IwConfig> getConfigListAdvance() {
		return configListAdvance;
	}

	public void setConfigListAdvance(List<IwConfig> configListAdvance) {
		this.configListAdvance = configListAdvance;
	}

	public List<IwConfig> getConfigListAuthentication() {
		return configListAuthentication;
	}

	public void setConfigListAuthentication(List<IwConfig> configListAuthentication) {
		this.configListAuthentication = configListAuthentication;
	}

	public List<IwConfig> getConfigListSmtp() {
		return configListSmtp;
	}

	public void setConfigListSmtp(List<IwConfig> configListSmtp) {
		this.configListSmtp = configListSmtp;
	}

	public List<IwConfig> getlDapOrMixteConfigListe() {
		return lDapOrMixteConfigListe;
	}

	public void setlDapOrMixteConfigListe(List<IwConfig> lDapOrMixteConfigListe) {
		this.lDapOrMixteConfigListe = lDapOrMixteConfigListe;
	}

	public boolean islDapOrMixteSelected() {
		return lDapOrMixteSelected;
	}

	public void setlDapOrMixteSelected(boolean lDapOrMixteSelected) {
		this.lDapOrMixteSelected = lDapOrMixteSelected;
	}

	public void displayLdapConfigOption(IwConfig iwConfigSelected) {
		if (iwConfigSelected.getConfigValue().equalsIgnoreCase("ad") || iwConfigSelected.getConfigValue().equalsIgnoreCase("mixte"))
			lDapOrMixteSelected = true;
		else lDapOrMixteSelected = false;
	}

	public IwConfig getAuthenticationConfigSelected() {
		return authenticationConfigSelected;
	}

	public void setAuthenticationConfigSelected(IwConfig authenticationConfigSelected) {
		this.authenticationConfigSelected = authenticationConfigSelected;
	}

	public List<IwConfig> getConfigListMailSending() {
		return configListMailSending;
	}

	public void setConfigListMailSending(List<IwConfig> configListMailSending) {
		this.configListMailSending = configListMailSending;
	}

	public List<IwConfig> getConfigListFileExportPaths() {
		return configListFileExportPaths;
	}

	public void setConfigListFileExportPaths(List<IwConfig> configListFileExportPaths) {
		this.configListFileExportPaths = configListFileExportPaths;
	}

	public DualListModel<User> getUserList() {
		return userList;
	}

	public void setUserList(DualListModel<User> userList) {
		this.userList = userList;
	}

	public List<User> getUserListSource() {
		return userListSource;
	}

	public void setUserListSource(List<User> userListSource) {
		this.userListSource = userListSource;
	}

	public List<User> getUserListTarget() {
		return userListTarget;
	}

	public void setUserListTarget(List<User> userListTarget) {
		this.userListTarget = userListTarget;
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
}
