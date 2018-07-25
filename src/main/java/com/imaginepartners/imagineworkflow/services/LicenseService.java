package com.imaginepartners.imagineworkflow.services;

import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.LicenseManager;
import net.nicholaswilliams.java.licensing.exception.*;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreator;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public interface LicenseService {

	public LicenseCreator initLicenseCreator();

	public LicenseManager initLicenseManager();

	public String createProductKey(String subject, String holder, String email, Date issueDate, Date expireDate, Map<String, Long> extraFeauturs);

	public byte[] createLicense(String subject, String productKey, String holder, String email, Date issueDate, Date expire, Map<String, Long> extraFeauturs);

	public void saveLicenseToFile(byte[] bLicense, String filePath) throws IOException;

	public void downloadLicense(byte[] bLicense, String downloadLicense);

	public void loadLicenses();

	public boolean isModuleLicenseExpired(String module);

	public boolean isModuleLicensed(String module);

	public String getModuleLicenseFilePath(String module);

	public License loadLicenseFromBytes(byte[] bLicense) throws KeyNotFoundException, AlgorithmNotSupportedException, InappropriateKeySpecificationException, InappropriateKeyException, CorruptSignatureException, InvalidSignatureException, FailedToDecryptException, InvalidLicenseException;

	public Map<String, License> getLicenses();

	public Map<String, String> getLicensesStatus();

	public License getLicense(String module);

	public File createLicenseFolder();

	public void switchLicenseMode(String mode);

	public long getLoggedinUsersNbr();

	public long getRegisteredUsersNbr();

	public boolean canLogin() throws Exception;

	public boolean canCreateNewUser() throws Exception;

	public void loadLicense();

	public boolean getHasModuleLdap() throws Exception;

	public boolean getHasModuleFormBuilder() throws Exception;

	public boolean getHasModuleDashboard() throws Exception;

	public boolean getHasModuleDocument() throws Exception;

	public boolean getHasModuleModeler() throws Exception;

	public boolean getHasModuleMobile() throws Exception;

	public boolean getHasModuleWebService() throws Exception;

	public Long getDaysNbr() throws Exception;

	public Long getMaxUsersNumber() throws Exception;

	public Long getMaxSimultaneousUsersNumber() throws Exception;

	public Long getMaxMobileUsersNumber() throws Exception;

	public Long getMaxMobileSimultaneousUsersNumber() throws Exception;

	public Date getExpirationDate() throws Exception;

	public String getLicenseStatus();
}
