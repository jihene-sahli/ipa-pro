package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.models.IwLicense;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.CorruptSignatureException;
import net.nicholaswilliams.java.licensing.exception.FailedToDecryptException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;
import net.nicholaswilliams.java.licensing.exception.InvalidSignatureException;
import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.exception.ObjectDeserializationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class LicenseController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private LicenseService licenseService;

	private String holder;

	private String email;

	private Date expires;

	private List<String> modules = new ArrayList<String>();

	private Long usersNumber;

	private Long loggedinUsersNumber;

	private Long usersNumberMobile;

	private Long loggedinUsersNumberMobile;

	private Long daysNumber;

	private UploadedFile file;

	private String currentLicenseId;

	private IwLicense currentIwLicense;

	private License currentLicense;

	public LicenseController() {
	}

	@PostConstruct
	public void init() {
		currentLicenseId = FacesUtil.getUrlParam("licence");
		if (StringUtils.isNotBlank(currentLicenseId)) {
			currentIwLicense = businessService.getIwLicense(Integer.valueOf(currentLicenseId));
			currentLicense = licenseService.loadLicenseFromBytes(currentIwLicense.getIwLicenseBytes());
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		if (file != null) {
			License license;
			String module;
			try {
				license = licenseService.loadLicenseFromBytes(file.getContents());
				if (!license.getSubject().equals(businessService.getConfigValue(ConfigConstants.PRODUCT_NAME))) {
					throw new InvalidLicenseException();
				}
				try {
					this.save();
					licenseService.loadLicense();
					FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.licence.licenseadded"));
					FacesUtil.redirect("liste.xhtml");
				} catch (IOException ex) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.cantsavelicense"));
				}
			} catch (KeyNotFoundException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (AlgorithmNotSupportedException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (InappropriateKeySpecificationException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (InappropriateKeyException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (CorruptSignatureException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (InvalidSignatureException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (FailedToDecryptException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (InvalidLicenseException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			} catch (ObjectDeserializationException e) {
				FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.licence.licenseinvalid"));
				LogManager.getLogger(LicenseController.class.getName()).error(null, e);
			}
		}
	}

	public void save() throws IOException {
		InputStream input = file.getInputstream();
		IwLicense iwLicense = new IwLicense();
		iwLicense.setIwLicenseBytes(file.getContents());
		iwLicense.setIwActive(Boolean.TRUE);
		iwLicense.setIwFileName(file.getFileName());
		iwLicense.setIwDate(new Date());
		businessService.saveIwLicense(iwLicense);
	}

	public void generateLicense() {
		Date issue = new Date();
		HashMap extraFeatures = new HashMap<String, Long>();
		extraFeatures.put(LicenseUtils.MAX_USERS_NUMBER, usersNumber);
		extraFeatures.put(LicenseUtils.MAX_SIMULTANEOUS_USERS_NUMBER, loggedinUsersNumber);
		extraFeatures.put(LicenseUtils.MAX_MOBILE_USERS_NUMBER, usersNumberMobile);
		extraFeatures.put(LicenseUtils.MAX_SIMULTANEOUS_MOBILE_USERS_NUMBER, loggedinUsersNumberMobile);
		extraFeatures.put(LicenseUtils.DAYS_NUMBER, daysNumber);
		for (String module : modules) {
			extraFeatures.put(module, -1L);
		}
		String productKey = licenseService.createProductKey(businessService.getConfigValue(ConfigConstants.PRODUCT_NAME), holder, email, issue, expires, extraFeatures
		);
		byte[] bLicense = licenseService.createLicense(businessService.getConfigValue(ConfigConstants.PRODUCT_NAME), productKey, holder, email, issue, expires, extraFeatures);
		licenseService.downloadLicense(bLicense, businessService.getConfigValue(ConfigConstants.PRODUCT_NAME));
	}

	public List<IwLicense> getLicenseList() {
		return businessService.getIwLicenseList();
	}

	public boolean getHasModuleLdap() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_LDAP)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleFormBuilder() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_FORM_BUILDER)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleDashboard() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_DASHBOARD)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleDocument() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_DOCUMENT)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleModeler() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_MODELER)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleMobile() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_MOBILE)) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasModuleWebService() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_WEB_SERVICE)) {
				return true;
			}
		}
		return false;
	}

	public Long getDaysNbr() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.DAYS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	public Long getMaxUsersNumber() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	public Long getMaxSimultaneousUsersNumber() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_SIMULTANEOUS_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	public Long getMaxMobileUsersNumber() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_MOBILE_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	public Long getMaxMobileSimultaneousUsersNumber() {
		for (License.Feature feature : currentLicense.getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_SIMULTANEOUS_MOBILE_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	public Date getExpirationDate() {
		return new Date(currentLicense.getGoodBeforeDate());
	}

	public void enable() {
		String iwLicenseId = FacesUtil.getUrlParam("license");
		if (StringUtils.isNotBlank(iwLicenseId)) {
			IwLicense iwLicense = businessService.getIwLicense(Integer.valueOf(iwLicenseId));
			iwLicense.setIwActive(Boolean.TRUE);
			businessService.saveIwLicense(iwLicense);
			licenseService.loadLicense();
		}
	}

	public void disable() {
		String iwLicenseId = FacesUtil.getUrlParam("license");
		if (StringUtils.isNotBlank(iwLicenseId)) {
			IwLicense iwLicense = businessService.getIwLicense(Integer.valueOf(iwLicenseId));
			iwLicense.setIwActive(Boolean.FALSE);
			businessService.saveIwLicense(iwLicense);
			licenseService.loadLicense();
		}

	}

	public void delete() {
		String iwLicenseId = FacesUtil.getUrlParam("license");
		if (StringUtils.isNotBlank(iwLicenseId)) {
			businessService.deleteIwLicense(Integer.valueOf(iwLicenseId));
			licenseService.loadLicense();
		}
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public LicenseService getLicenseService() {
		return licenseService;
	}

	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUsersNumber() {
		return usersNumber;
	}

	public void setUsersNumber(Long usersNumber) {
		this.usersNumber = usersNumber;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public Long getLoggedinUsersNumber() {
		return loggedinUsersNumber;
	}

	public void setLoggedinUsersNumber(Long loggedinUsersNumber) {
		this.loggedinUsersNumber = loggedinUsersNumber;
	}

	public Long getUsersNumberMobile() {
		return usersNumberMobile;
	}

	public void setUsersNumberMobile(Long usersNumberMobile) {
		this.usersNumberMobile = usersNumberMobile;
	}

	public Long getLoggedinUsersNumberMobile() {
		return loggedinUsersNumberMobile;
	}

	public void setLoggedinUsersNumberMobile(Long loggedinUsersNumberMobile) {
		this.loggedinUsersNumberMobile = loggedinUsersNumberMobile;
	}

	public Long getDaysNumber() {
		return daysNumber;
	}

	public void setDaysNumber(Long daysNumber) {
		this.daysNumber = daysNumber;
	}

	public String getCurrentLicenseId() {
		return currentLicenseId;
	}

	public void setCurrentLicenseId(String currentLicenseId) {
		this.currentLicenseId = currentLicenseId;
	}

	public IwLicense getCurrentIwLicense() {
		return currentIwLicense;
	}

	public void setCurrentIwLicense(IwLicense currentIwLicense) {
		this.currentIwLicense = currentIwLicense;
	}

	public License getCurrentLicense() {
		return currentLicense;
	}

	public void setCurrentLicense(License currentLicense) {
		this.currentLicense = currentLicense;
	}
}
