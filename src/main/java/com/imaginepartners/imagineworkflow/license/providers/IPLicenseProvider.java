package com.imaginepartners.imagineworkflow.license.providers;

import com.imaginepartners.imagineworkflow.license.LicenseUtils;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import net.nicholaswilliams.java.licensing.DeserializingLicenseProvider;
import net.nicholaswilliams.java.licensing.FileLicenseProvider;
import net.nicholaswilliams.java.licensing.LicenseProvider;
import net.nicholaswilliams.java.licensing.SignedLicense;

public class IPLicenseProvider implements LicenseProvider {

	private String mode;

	private final FileLicenseProvider flp = new FileLicenseProvider();

	private final DeserializingLicenseProvider dlp = new DeserializingLicenseProvider() {
		@Override
		protected byte[] getLicenseData(Object o) {
			return (byte[]) o;
		}
	};

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	@Override
	public SignedLicense getLicense(Object o) {
		if (mode == null) {
			throw new NullPointerException(FacesUtil.getMessage("license.provider.modecantbenull"));
		}
		if (LicenseUtils.LICENSE_PROVIDER_FILE_MODE.equals(mode)) {
			return flp.getLicense(o);
		} else {
			return dlp.getLicense(o);
		}
	}
}
