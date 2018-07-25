package com.imaginepartners.imagineworkflow.license.providers;

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;

public class LicensePasswordProvider implements PasswordProvider {

	@Override
	public char[] getPassword() {
		return "2Encrypt".toCharArray();
	}

}
