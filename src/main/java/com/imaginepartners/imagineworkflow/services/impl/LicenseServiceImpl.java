package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.license.LicenseSubjectValidator;
import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.license.providers.*;
import com.imaginepartners.imagineworkflow.models.IwLicense;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.LicenseService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.License.Builder;
import net.nicholaswilliams.java.licensing.LicenseManager;
import net.nicholaswilliams.java.licensing.LicenseManagerProperties;
import net.nicholaswilliams.java.licensing.exception.*;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreator;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreatorProperties;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@Service
public class LicenseServiceImpl implements LicenseService, Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, License> licenses = new HashMap<String, License>();

	private Map<String, String> licensesStatus = new HashMap<String, String>();

	private IPLicenseProvider licenseProvider = new IPLicenseProvider();

	private IwLicense currentIwLicense;

	private License currentLicense;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	public void LicenseServiceImpl() {

	}

	@PostConstruct
	public void init() {
		initLicenseManager();
		initLicenseCreator();
		loadLicense();
	}

	public IPLicenseProvider getLicenseProvider() {
		return licenseProvider;
	}

	@Override
	public Map<String, License> getLicenses() {
		return licenses;
	}

	@Override
	public Map<String, String> getLicensesStatus() {
		return licensesStatus;
	}

	@Override
	public void loadLicense() {
		currentIwLicense = businessService.getLastActiveIwLicense();
		if (currentIwLicense != null) {
			currentLicense = loadLicenseFromBytes(currentIwLicense.getIwLicenseBytes());
		}
	}

	@Override
	public void loadLicenses() {
		licenses = new HashMap<String, License>();
		licenseProvider.setMode(LicenseUtils.LICENSE_PROVIDER_FILE_MODE);
		File licenseFolder = createLicenseFolder();
		LicenseManager lmgr = LicenseManager.getInstance();
		FileFilter ff;
		ff = new FileFilter() {
			@Override
			public boolean accept(File fl) {
				return fl.isFile() && fl.getName().endsWith(LicenseUtils.LICENSE_FILE_EXTENSION);
			}
		};
		File[] listOfFiles = licenseFolder.listFiles(ff);
		for (File file : listOfFiles) {
			try {
				License license = lmgr.getLicense(file.getPath());
				try {
					lmgr.validateLicense(license);
					licenses.put(license.getSubject(), license);
					licensesStatus.put(license.getSubject(), LicenseUtils.LICENSE_VALID);
				} catch (ExpiredLicenseException e) {
					LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
					licensesStatus.put(license.getSubject(), LicenseUtils.LICENSE_EXPIRED);
				} catch (InvalidLicenseException e) {
					LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
					licensesStatus.put(license.getSubject(), LicenseUtils.LICENSE_INVALID);
				} catch (ObjectDeserializationException e) {
					LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
				} catch (FailedToDecryptException e) {
					LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
				}
			} catch (KeyNotFoundException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (AlgorithmNotSupportedException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (InappropriateKeySpecificationException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (InappropriateKeyException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (CorruptSignatureException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (InvalidSignatureException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			} catch (FailedToDecryptException e) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, e);
			}
		}
	}

	@Override
	public LicenseCreator initLicenseCreator() {
		LicenseCreatorProperties.setPrivateKeyDataProvider(new PrivateKeyProvider());
		LicenseCreatorProperties.setPrivateKeyPasswordProvider(new PrivatePasswordProvider());
		return LicenseCreator.getInstance();
	}

	@Override
	public LicenseManager initLicenseManager() {
		LicenseManagerProperties.setPublicKeyDataProvider(new PublicKeyProvider());
		LicenseManagerProperties.setPublicKeyPasswordProvider(new PublicPasswordProvider());
		LicenseManagerProperties.setLicenseProvider(licenseProvider);
		LicenseManagerProperties.setLicensePasswordProvider(new LicensePasswordProvider());
		LicenseManagerProperties.setLicenseValidator(new LicenseSubjectValidator());
		LicenseManagerProperties.setCacheTimeInMinutes(5);
		return LicenseManager.getInstance();
	}

	@Override
	public void switchLicenseMode(String mode) {
		licenseProvider.setMode(mode);
	}

	@Override
	public boolean isModuleLicenseExpired(String module) {
		License lcs = licenses.get(module);
		if (lcs != null) {
			return licenses.get(module).getGoodBeforeDate() < new Date().getTime();
		} else {
			return false;
		}
	}

	@Override
	public boolean isModuleLicensed(String module) {
		String lcsStatus = licensesStatus.get(module);
		if (lcsStatus != null) {
			return (!LicenseUtils.LICENSE_INVALID.equals(licensesStatus.get(module)) && !isModuleLicenseExpired(module));
		} else {
			return false;
		}
	}

	@Override
	public void downloadLicense(byte[] bLicense, String subject) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
		response.reset();
		response.setContentType(LicenseUtils.LICENSE_FILE_MIME_TYPE);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + subject + LicenseUtils.LICENSE_FILE_EXTENSION + "\"");
		OutputStream output;
		try {
			output = response.getOutputStream();
			output.write(bLicense);
		} catch (IOException ex) {
			LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, ex);
		}
		fc.responseComplete();
	}

	@Override
	public void saveLicenseToFile(byte[] bLicense, String filePath) throws IOException {
		BufferedOutputStream output;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, ex);
			}
		}
		try {
			output = new BufferedOutputStream(new FileOutputStream(filePath));
			try {
				output.write(bLicense);
			} catch (IOException ex) {
				LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, ex);
			} finally {
				try {
					output.close();
				} catch (IOException ex) {
					LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, ex);
				}
			}
		} catch (FileNotFoundException ex) {
			LogManager.getLogger(LicenseServiceImpl.class.getName()).error(null, ex);
		}
	}

	@Override
	public byte[] createLicense(String subject, String productKey, String holder, String email, Date issueDate, Date expire, Map<String, Long> extraFeauturs) {
		Builder bldr = new License.Builder();
		bldr.withProductKey(productKey).
		withHolder(holder).
		withIssuer(email).
		withIssueDate(issueDate.getTime()).
		withGoodBeforeDate(expire.getTime()).withSubject(subject);
		for (Entry<String, Long> entry : extraFeauturs.entrySet()) {
			if (entry.getValue() != null) {
				bldr.addFeature(entry.getKey(), entry.getValue());
			} else {
				bldr.addFeature(entry.getKey());
			}
		}
		License license = bldr.build();
		LicenseCreator lc = LicenseCreator.getInstance();
		byte[] bLicense = lc.signAndSerializeLicense(license, new LicensePasswordProvider().getPassword());
		return bLicense;
	}

	@Override
	public String createProductKey(String subject, String holder, String email, Date issueDate, Date expireDate, Map<String, Long> extraFeauturs) {
		try {
			MessageDigest digester = MessageDigest.getInstance(LicenseUtils.LICENSE_HASH_ALGORITHM);
			String productKey = subject
			+ LicenseUtils.PRODUCTKEY_FIELDS_SEPARATOR
			+ holder
			+ LicenseUtils.PRODUCTKEY_FIELDS_SEPARATOR
			+ email
			+ LicenseUtils.PRODUCTKEY_FIELDS_SEPARATOR
			+ issueDate.getTime()
			+ LicenseUtils.PRODUCTKEY_FIELDS_SEPARATOR
			+ expireDate.getTime()
			+ LicenseUtils.PRODUCTKEY_FIELDS_SEPARATOR
			+ extraFeauturs.toString();
			byte[] productKeyMD5Hash = digester.digest(productKey.getBytes());
			return new String(Hex.encodeHex(productKeyMD5Hash)).toUpperCase(Locale.FRENCH);
		} catch (NoSuchAlgorithmException ex) {
			LogManager.getLogger(LicenseServiceImpl.class).error(null, ex);
			return null;
		} catch (NullPointerException ex) {
			LogManager.getLogger(LicenseServiceImpl.class).error(null, ex);
			return null;
		}
	}

	@Override
	public String getModuleLicenseFilePath(String module) {
		String path = createLicenseFolder().getPath();
		try {
			path = path + URLDecoder.decode(File.separator
			+ module + LicenseUtils.MODULE_NAME_SEPARATOR + LicenseUtils.LICENSE_FILE_NAME, LicenseUtils.LICENSE_PATH_ENCODING);
			return path;
		} catch (UnsupportedEncodingException ex) {
			LogManager.getLogger(LicenseServiceImpl.class).error(null, ex);
			return null;
		}
	}

	@Override
	public License loadLicenseFromBytes(byte[] bLicense) throws KeyNotFoundException, AlgorithmNotSupportedException, InappropriateKeySpecificationException, InappropriateKeyException, CorruptSignatureException, InvalidSignatureException, FailedToDecryptException, InvalidLicenseException {
		switchLicenseMode(LicenseUtils.LICENSE_PROVIDER_BYTE_MODE);
		LicenseManager manager = LicenseManager.getInstance();
		License license = manager.getLicense(bLicense);
		manager.validateLicense(license);
		return license;
	}

	@Override
	public License getLicense(String module) {
		return licenses.get(module);
	}

	@Override
	public File createLicenseFolder() {
		try {
			String path = this.getClass().getClassLoader().getResource("").getPath().replaceAll(LicenseUtils.CLASSES_SUB_PATH_REGEXP, LicenseUtils.LICENSES_SUB_PATH);
			path = URLDecoder.decode(path, LicenseUtils.LICENSE_PATH_ENCODING);
			File licensesFolder = new File(path);
			if (!licensesFolder.exists()) {
				licensesFolder.mkdir();
			}
			return licensesFolder;
		} catch (UnsupportedEncodingException ex) {
			LogManager.getLogger(LicenseServiceImpl.class).error(null, ex);
			return null;
		}
	}

	@Override
	public long getLoggedinUsersNbr() {
		return sessionRegistry.getAllPrincipals().size();
	}

	@Override
	public long getRegisteredUsersNbr() {
		return activitiService.getUsersCount();
	}

	@Override
	public boolean getHasModuleLdap() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_LDAP)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleFormBuilder() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_FORM_BUILDER)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleDashboard() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_DASHBOARD)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleDocument() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_DOCUMENT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleModeler() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_MODELER)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleMobile() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_MOBILE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getHasModuleWebService() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MODULE_WEB_SERVICE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Long getDaysNbr() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.DAYS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	@Override
	public Long getMaxUsersNumber() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	@Override
	public Long getMaxSimultaneousUsersNumber() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_SIMULTANEOUS_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	@Override
	public Long getMaxMobileUsersNumber() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_MOBILE_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	@Override
	public Long getMaxMobileSimultaneousUsersNumber() throws Exception {
		for (License.Feature feature : getLicense().getFeatures()) {
			if (feature.getName().equals(LicenseUtils.MAX_SIMULTANEOUS_MOBILE_USERS_NUMBER)) {
				return feature.getGoodBeforeDate();
			}
		}
		return 0L;
	}

	@Override
	public Date getExpirationDate() throws Exception {
		return new Date(currentLicense.getGoodBeforeDate());
	}

	@Override
	public boolean canLogin() throws Exception {
		return getMaxMobileSimultaneousUsersNumber() > getLoggedinUsersNbr();
	}

	@Override
	public boolean canCreateNewUser() throws Exception {
		return getRegisteredUsersNbr() < getMaxUsersNumber();
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

	public SessionRegistry getSessionRegistry() {
		return sessionRegistry;
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	private License getLicense() throws Exception {
		Calendar currentCalendar = Calendar.getInstance();
		Date currentDate = currentCalendar.getTime();
		if (currentLicense == null) {
			throw new Exception(FacesUtil.getMessage("iw.licence.paslicense"));
		}
		if (currentLicense.getGoodBeforeDate() > 0L) {
			if (currentLicense.getGoodBeforeDate() > 0L && currentLicense.getGoodBeforeDate() < currentDate.getTime()) {
				throw new Exception(FacesUtil.getMessage("iw.licence.licenseexpire"));
			}
		}
		return currentLicense;
	}

	@Override
	public String getLicenseStatus() {
		Calendar currentCalendar = Calendar.getInstance();
		Date currentDate = currentCalendar.getTime();
		if (currentLicense == null) {
			return LicenseUtils.NO_LICENSE;
		}
		if (currentLicense.getGoodBeforeDate() > 0L) {
			if (currentLicense.getGoodBeforeDate() > 0L && currentLicense.getGoodBeforeDate() < currentDate.getTime()) {
				return LicenseUtils.LICENSE_EXPIRED;
			}
		}
		return LicenseUtils.LICENSE_VALID;
	}
}
