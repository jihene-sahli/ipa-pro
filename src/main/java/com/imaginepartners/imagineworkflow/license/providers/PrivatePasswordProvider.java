package com.imaginepartners.imagineworkflow.license.providers;

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;

public final class PrivatePasswordProvider implements PasswordProvider {

	@Override
	public char[] getPassword() {
		return new char[]{
		0x0000002A, 0x0000002A, 0x0000004E, 0x0000006F, 0x0000004F, 0x0000006E, 0x00000065, 0x00000053,
		0x00000068, 0x0000006F, 0x00000075, 0x0000006C, 0x00000064, 0x0000004B, 0x0000006E, 0x0000006F,
		0x00000077, 0x00000041, 0x00000062, 0x0000006F, 0x00000075, 0x00000074, 0x00000054, 0x00000068,
		0x00000069, 0x00000073, 0x00000042, 0x00000075, 0x00000074, 0x00000054, 0x00000068, 0x00000069,
		0x00000073, 0x00000043, 0x0000006F, 0x00000064, 0x00000065, 0x00000039, 0x00000039, 0x00000039,
		0x00000039, 0x0000002A, 0x0000002A
		};
	}
}