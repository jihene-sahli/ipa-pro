package com.imaginepartners.imagineworkflow.license;

import com.imaginepartners.imagineworkflow.util.FacesUtil;
import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.LicenseValidator;
import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;

public class LicenseSubjectValidator implements LicenseValidator {

	@Override
	public void validateLicense(License lcns) throws InvalidLicenseException {
		if (lcns.getSubject() == null) {
			throw new InvalidLicenseException(FacesUtil.getMessage("license.licensedetails.modulecantbenull"));
		}
		if ("".equals(lcns.getSubject())) {
			throw new InvalidLicenseException(FacesUtil.getMessage("license.licensedetails.modulecantbeempty"));
		}
	}
}
