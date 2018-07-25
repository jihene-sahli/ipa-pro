package com.imaginepartners.imagineworkflow.license.providers;

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;

public final class PublicPasswordProvider implements PasswordProvider {

	@Override
	public char[] getPassword() {
		return new char[]{
		0x00000041, 0x00000032, 0x00000046, 0x00000030, 0x00000043, 0x00000041, 0x00000041, 0x00000034,
		0x00000045, 0x00000035, 0x00000032, 0x00000045, 0x00000039, 0x00000036, 0x00000037, 0x00000038
		};
	}
}
