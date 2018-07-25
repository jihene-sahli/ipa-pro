package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.models.*;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
import com.imaginepartners.imagineworkflow.models.rh.RhPosteOccupe;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

@Controller("navigationController")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NavigationController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	BusinessService businessService;

	@Autowired
	transient private ActivitiService activitiService;

	@Autowired
	private UserService userService;

	@Autowired
	transient private LicenseService licenseService;

	private List<IwApplication> applicationList;

	private Map<String, Object> languageList = new HashMap<String, Object>();

	private Locale currentLocale;

	private String currentLanguage;

	private Boolean displayStickyTitle;

	private List<IwConfig> stickyTitleConfig;

	private MenuModel breadCrumb;

	Boolean displayCompletedTasks;

	Boolean displayInvolvedTasks;

	Boolean displayInstanceNameDialog;

	Boolean displayStandalonetaskDialog;

	Boolean displayLangagesList ;

	Boolean displayProcessProgression ;

	private int indexActiveApp;

	private int indexActiveProcess;

	private int indexActiveAppStd;

	private int indexActiveFunction;

	Boolean showAgendaReservation ;

	@PostConstruct
	public void init() {
		currentLocale = Locale.FRANCE;
		currentLanguage = currentLocale.getLanguage();
		languageList.put(AppConstants.FRANCAIS, Locale.FRENCH);
		languageList.put(AppConstants.ENGLISH, Locale.ENGLISH);
		indexActiveApp = 0;
		indexActiveProcess = 0;

	}

	public void controlNavigation() {
		if (!"/pages/licence/upload.xhtml".equals(FacesUtil.getViewId())
		&& !LicenseUtils.LICENSE_VALID.equals(licenseService.getLicenseStatus())) {
			FacesUtil.redirect(FacesUtil.getContextPath() + "/pages/licence/upload.xhtml");
		}
	}

	public void localeChnageListener(ValueChangeEvent event) {
		String newLocaleValue = event.getNewValue().toString();
		for (Map.Entry<String, Object> entry : languageList.entrySet()) {
			if (entry.getValue().toString().equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
				Locale.setDefault(((Locale) entry.getValue()));
				currentLanguage = ((Locale) entry.getValue()).getLanguage();
				currentLocale = Locale.getDefault();
			}
		}
	}

	public Boolean getDisplayCompletedTasks() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_COMPLETED_TASK);
		if (StringUtils.isNotEmpty(configVal)) {
			displayCompletedTasks = Boolean.valueOf(configVal);
		}
		return displayCompletedTasks;
	}

	public void setDisplayCompletedTasks(Boolean displayCompletedTasks) {
		this.displayCompletedTasks = displayCompletedTasks;
	}

	public Boolean getDisplayInvolvedTasks() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_IMPLIQUE_TASK);
		if (StringUtils.isNotEmpty(configVal)) {
			displayInvolvedTasks = Boolean.valueOf(configVal);
		}
		return displayInvolvedTasks;
	}

	public void setDisplayInvolvedTasks(Boolean displayInvolvedTasks) {
		this.displayInvolvedTasks = displayInvolvedTasks;
	}

	public Boolean getDisplayInstanceNameDialog() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_INSTANCE_CREATION_DIALOG);
		if (StringUtils.isNotEmpty(configVal)) {
			displayInstanceNameDialog = Boolean.valueOf(configVal);
		}
		return displayInstanceNameDialog;
	}

	public void setDisplayInstanceNameDialog(Boolean displayInstanceNameDialog) {
		this.displayInstanceNameDialog = displayInstanceNameDialog;
	}

	public NavigationController() {
	}

	public String getCompanySlogan() {
		return businessService.getConfigValue(ConfigConstants.COMPANY_SLOGAN);
	}

	public String getImagineworkflowTitle() {
		return businessService.getConfigValue(ConfigConstants.IMAGINEWORKFLOW_TITLE);
	}

	public String getHomePage() {
		return businessService.getConfigValue(ConfigConstants.HOME_PAGE);
	}

	public void updateMessages(boolean update) throws Exception {
		FacesUtil.loadSessionMessages(update);
	}

	public void updateSessionElements() {
		FacesUtil.updateSessionElements();
	}

	public String getLoggedInUserName() {
		String userName = "";
		if(userService.getLoggedInUser()!=null) {
			if (StringUtils.isNotBlank(userService.getLoggedInUser().getLastName())) {
				userName = userService.getLoggedInUser().getLastName();
			}
			if (StringUtils.isNotBlank(userService.getLoggedInUser().getFirstName())) {
				userName += " " + userService.getLoggedInUser().getFirstName();
			}
			if (StringUtils.isBlank(userName)) {
				userName = userService.getLoggedInUser().getId();
			}
		}
		return userName;
	}

	public List<ProcessDefinition> getLastProcDefList() {
		return activitiService.getLastProcDefList();
	}

	public List<ProcessDefinition> getLastProcDefListByCategorie(String category) {
		return activitiService.getLastProcDefList(category);
	}

	public List<ProcessDefinition> getLastProcDefListByAppKey(String appKey) {
		return activitiService.getLastProcDefList(getAutorisedProcessKeis(appKey));
	}
	public List<IwStandAloneTask> getFonctionsByAppKey(String appKey) {
		return businessService.getStandAloneTaskListByIwRight(appKey, userService.getUserRights());
	}

	public List<String> getAutorisedProcessKeis(String appKey) {
		List<String> result = new ArrayList<String>();
		List<String> processKeisList = businessService.getProcessKeyByApplication(appKey);
		List<String> processKeisIwRight = userService.getUserProcessKeyRights();
		if (processKeisList != null && !processKeisList.isEmpty()
		&& processKeisIwRight != null && !processKeisIwRight.isEmpty()) {
			for (String key : processKeisList) {
				if (processKeisIwRight.contains(key)) {
					result.add(key);
				}
			}
		}
		return result;
	}

	public List<ProcessDefinition> getUserLastProcDefList() {
		List<String> processKies = userService.getUserProcessKeyRights();
		return activitiService.getLastProcDefList(processKies);
	}

	public List<ProcessDefinition> getProcDefList() {
		return activitiService.getProcDefList();
	}

	public Locale getLocale() {
		return FacesUtil.getClientLocale();
	}

	public String getLongDateFormat() {
		return businessService.getConfigValue(ConfigConstants.LONG_DATE_PATTERN);
	}

	public String getShortDateFormat() {
		return businessService.getConfigValue(ConfigConstants.SHORT_DATE_PATTERN);
	}

	public String getCompanyLogo() {
		return businessService.getConfigValue(ConfigConstants.COMPANY_LOGO);
	}

	public String getCompanyFavIcon() {
		IwConfig iwConfig = businessService.getConfig(ConfigConstants.COMPANY_FAVICON);
		if (iwConfig != null) {
			return iwConfig.getDefaultValue();
		}
		return null;
	}

	public String getCompanyTheme() {
		return businessService.getConfigValue(ConfigConstants.COMPANY_THEME);
	}

	public String getLoginLogo() {
		return businessService.getConfigValue(ConfigConstants.LOGIN_LOGO);
	}

	public boolean displayFolderNumber() {
		String configValue = businessService.getConfigValue(ConfigConstants.DISPLAY_FOLDER_NUMBER);
		if (configValue.toLowerCase().equals("oui") || configValue.toLowerCase().contains("oui")) {
			return true;
		} else {
			return Boolean.valueOf(configValue);
		}
	}

	public boolean displayTaskNumber() {
		String configValue = businessService.getConfigValue(ConfigConstants.DISPLAY_TASK_NUMBER);
		if (configValue.toLowerCase().equals("oui") || configValue.toLowerCase().contains("oui")) {
			return true;
		} else {
			return Boolean.valueOf(configValue);
		}
	}

	public User getLoggedInUser() {
		return userService.getLoggedInUser();
	}

	public IwUserDetails getLoggedInUserDetails() {
		return userService.getLoggedInUserDetails();
	}

	public String getPasswordValidationRegExp() {
		return businessService.getConfigValue(ConfigConstants.PASSWORD_VALIDATION_REGEX);
	}

	public String getPasswordValidationTip() {
		return businessService.getConfigValue(ConfigConstants.PASSWORD_VALIDATION_TIP);
	}

	public String getModelEditUrl(String modelId) {
		return AppConstants.EDITOR_APP_URL + "?" + AppConstants.EDITOR_APP_URL_PARAM_NAME + "=" + modelId;
	}

	public String getFormEditUrl(String formId) {
		return AppConstants.FORM_BUILDER_APP_URL + "?" + AppConstants.FORM_BUILDER_APP_URL_PARAM_NAME + "=" + formId;
	}

	public String getProgressUrl(String procDefId, String procInstId) {
		return AppConstants.PROGRESS_APP_URL + "?" + AppConstants.PROGRESS_APP_URL_PROC_DEF_PARAM_NAME + "=" + procDefId
		+ "&" + AppConstants.PROGRESS_APP_URL_PROC_INST_PARAM_NAME + "=" + procInstId;
	}

	public String getProgressUrlFull(String procDefId, String procInstId) {
		return FacesUtil.getRequest().getContextPath() + AppConstants.PROGRESS_APP_URL + "?" + AppConstants.PROGRESS_APP_URL_PROC_DEF_PARAM_NAME
		+ "=" + procDefId + "&" + AppConstants.PROGRESS_APP_URL_PROC_INST_PARAM_NAME + "=" + procInstId;
	}

	public String getNewFormUrl() {
		return FacesUtil.getRequest().getContextPath() + AppConstants.FORM_BUILDER_APP_URL;
	}

	public String getEmailRegExp() {
		return businessService.getConfigValue(ConfigConstants.EMAIL_VALIDATION_REGEX);
	}

	public String getGroupIdRegExp() {
		return businessService.getConfigValue(ConfigConstants.GROUP_ID_VALIDATION_REGEX);
	}

	public String getUserIdRegExp() {
		return businessService.getConfigValue(ConfigConstants.USER_ID_VALIDATION_REGEX);
	}

	public boolean isPictureSet(String userId) {
		if (StringUtils.isBlank(userId)) {
			return false;
		}
		User user = activitiService.getUser(userId);
		Boolean displayInvolvedTasks;
		if (user == null) {
			return false;
		}
		return user.isPictureSet();
	}

	public String getAuthenticationMode() {
		return businessService.getConfigValue(ConfigConstants.AUTHENTICATION_MODE);
	}

	public String getAdminRole() {
		return businessService.getConfigValue(ConfigConstants.USERS_ADMIN_GROUP);
	}

	public String getNormalHierarchyName() {
		return AppConstants.GROUP_TYTPE_HIERARCHY;
	}

	public String getFunctionalHierarchyName() {
		return AppConstants.GROUP_TYTPE_FUNCTIONAL;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void reinitBreadCrumb() {
		breadCrumb = new DefaultMenuModel();
		DefaultMenuItem item = new DefaultMenuItem("Home");
		item.setUrl("/");
		breadCrumb.addElement(item);
		if ("taskList".equals(FacesUtil.getViewId())) {
			DefaultMenuItem item2 = new DefaultMenuItem("Mes taches");
			item2.setOutcome(FacesUtil.getViewId());
			breadCrumb.addElement(item2);
		} else {
			DefaultMenuItem item2 = new DefaultMenuItem(FacesUtil.getViewId());
			item2.setOutcome(FacesUtil.getViewId());
			breadCrumb.addElement(item2);
		}
	}

	public MenuModel getBreadCrumb() {
		reinitBreadCrumb();
		return breadCrumb;
	}

	public void setBreadCrumb(MenuModel breadCrumb) {
		this.breadCrumb = breadCrumb;
	}

	public String getCopyright() {
		return businessService.getConfigValue(ConfigConstants.COPYRIGHT);
	}

	public TimeZone getTimeZone() {
		return FacesUtil.getTimeZone();
	}

	public Date getDate() {
		return new Date();
	}

	public String getRandomAlphabetic() {
		return RandomStringUtils.randomAlphabetic(6);
	}

	public String getRandomNumeric() {
		return RandomStringUtils.randomNumeric(6);
	}

	public String formatTextareaOutput(String text) {
		if (text == null) {
			return "";
		}
		return text.replaceAll("\\\r\\\n|\\\n\\\r|\\\r|\\\n", "<br />");
	}

	public List<IwApplication> getUserApplicationList() {
		List<String> apps = new ArrayList<String>();
		List<IwRight> rights = userService.getUserRights();
		if (rights == null || rights.isEmpty()) {
			return new ArrayList<IwApplication>();
		}
		for (IwRight right : rights) {
			apps.add(right.getIwApplicationKey());
		}
		return applicationList = businessService.getUserApplicaitonList(apps);
	}

	public boolean showSubMenu(String appKey){
		List<String> listGroupIds= businessService.getGroupIdsByAppKey(appKey);
		for(String id: listGroupIds){
			if(activitiService.isMemberOfGroup(userService.getLoggedInUser().getId(), id))
				return true;
		}
		List<IwRight> listRights= businessService.getIwRightByAppAndUser(appKey, userService.getLoggedInUser().getId());
		for(IwRight right: listRights){
			if( right.getIwRightToLaunch() != null && right.getIwRightToLaunch())
				return true;
		}
		return false;
	}

	private boolean isNotExcluded(String appKey, String processKey){
		IwRight right= businessService.getIwRightByAppAndUser(appKey, userService.getLoggedInUser().getId(), processKey);
		if(right != null && right.getIwRightToLaunch() != null)
			return right.getIwRightToLaunch();
		return true;
	}

	public boolean showMenuItem(String appKey, String processKey){
		List<Group> groups= activitiService.getUserGroupList(userService.getLoggedInUser().getId());
		for(Group gr: groups){
			IwRight right= businessService.getIwRightByAppAndGroup(appKey, gr.getId(), processKey);
			if( right != null && right.getIwRightToLaunch() != null && right.getIwGroup().equals(gr.getId())){
				if(right.getIwRightToLaunch())
					return isNotExcluded(appKey, processKey);
				else
					return false;
			}
		}
		// Filter by user right
		User user= userService.getLoggedInUser();
		IwRight iwright= businessService.getIwRightByAppAndUser(appKey, user.getId(), processKey);
		if(iwright != null && iwright.getIwRightToLaunch() != null )
			if(iwright.getIwRightToLaunch())
				return true;
			else
				return false;
		return true;
	}

	public String formatDate(Date date) {
		String format = "dd/MM/yyyy";
		if (date == null) {
			return null;
		}
		return date.toString();
	}

	public List<IwApplication> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<IwApplication> applicationList) {
		this.applicationList = applicationList;
	}

	public Map<String, Object> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(Map<String, Object> languageList) {
		this.languageList = languageList;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

	public Long convertLongToString(String id) {
		if (id == null) {
			return null;
		}
		return Long.valueOf(id);
	}

	public Boolean getDisplayStandalonetaskDialog() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_STANDALONE_CREATION_DIALOG);
		if (StringUtils.isNotEmpty(configVal)) {
			displayStandalonetaskDialog = Boolean.valueOf(configVal);
		}
		return displayStandalonetaskDialog;
	}

	public void setDisplayStandalonetaskDialog(Boolean displayStandalonetaskDialog) {
		this.displayStandalonetaskDialog = displayStandalonetaskDialog;
	}

	public Boolean getDisplayStickyTitle() {
		String stickyTitleConfigStr = businessService.getConfigValue(ConfigConstants.ENABLE_STICKY_TITLE);
		if (StringUtils.isNotEmpty(stickyTitleConfigStr)) {
			displayStickyTitle = Boolean.valueOf(stickyTitleConfigStr) == true;
		}
		return displayStickyTitle;
	}

	public void setDisplayStickyTitle(Boolean displayStickyTitle) {
		this.displayStickyTitle = displayStickyTitle;
	}

	public List<IwConfig> getStickyTitleConfig() {
		return stickyTitleConfig;
	}

	public void setStickyTitleConfig(List<IwConfig> stickyTitleConfig) {
		this.stickyTitleConfig = stickyTitleConfig;
	}

	public Boolean getDisplayLangagesList() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_TRADUCTION_LIST);
		if (StringUtils.isNotEmpty(configVal)) {
			displayLangagesList = Boolean.valueOf(configVal);
		}
		return displayLangagesList;
	}

	public void setDisplayLangagesList(Boolean displayLangagesList) {
		this.displayLangagesList = displayLangagesList;
	}

	public Boolean getDisplayProcessProgression() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.DISPLAY_PROCESS_PROGRESS_BAR);
		if (StringUtils.isNotEmpty(configVal)) {
			displayProcessProgression = Boolean.valueOf(configVal);
		}
		return displayProcessProgression;
	}

	public void setDisplayProcessProgression(Boolean displayProcessProgression) {
		this.displayProcessProgression = displayProcessProgression;
	}

	public int getIndexActiveApp() {
		return indexActiveApp;
	}

	public void setIndexActiveApp(int indexActiveApp) {
		this.indexActiveApp = indexActiveApp;
	}

	public int getIndexActiveProcess() {
		return indexActiveProcess;
	}

	public void setIndexActiveProcess(int indexActiveProcess) {
		this.indexActiveProcess = indexActiveProcess;
	}

	public void updateTabIndexApp(TabChangeEvent event){
		this.indexActiveApp = ((TabView) event.getComponent()).getActiveIndex();
	}

	public void updateTabIndexProcess(TabChangeEvent event) {
		this.indexActiveProcess = ((TabView) event.getComponent()).getActiveIndex();
	}

	public void  updateTabIndexAppStd(TabChangeEvent event){
		this.indexActiveAppStd = ((TabView) event.getComponent()).getActiveIndex();
	}

	public void updateTabIndexFunctoin(TabChangeEvent event){
		this.indexActiveFunction = ((TabView) event.getComponent()).getActiveIndex();
	}

	public int getIndexActiveAppStd() {
		return indexActiveAppStd;
	}

	public void setIndexActiveAppStd(int indexActiveAppStd) {
		this.indexActiveAppStd = indexActiveAppStd;
	}

	public int getIndexActiveFunction() {
		return indexActiveFunction;
	}

	public void setIndexActiveFunction(int indexActiveFunction) {
		this.indexActiveFunction = indexActiveFunction;
	}

	public Boolean getShowAgendaReservation() {
		String configVal = businessService.getConfigValueOrDefault(ConfigConstants.SHOW_AGENDA_RESERVATION);
		if (StringUtils.isNotEmpty(configVal)) {
			showAgendaReservation = Boolean.valueOf(configVal);
		}
		return showAgendaReservation;
	}

	public void setShowAgendaReservation(Boolean showAgendaReservation) {
		this.showAgendaReservation = showAgendaReservation;
	}
}
