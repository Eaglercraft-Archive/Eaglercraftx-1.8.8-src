package net.lax1dude.eaglercraft.v1_8.socket;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class HandshakePacketTypes {

	public static final String AUTHENTICATION_REQUIRED = "Authentication Required:";

	public static final int PROTOCOL_CLIENT_VERSION = 0x01;
	public static final int PROTOCOL_SERVER_VERSION = 0x02;
	public static final int PROTOCOL_VERSION_MISMATCH = 0x03;
	public static final int PROTOCOL_CLIENT_REQUEST_LOGIN = 0x04;
	public static final int PROTOCOL_SERVER_ALLOW_LOGIN = 0x05;
	public static final int PROTOCOL_SERVER_DENY_LOGIN = 0x06;
	public static final int PROTOCOL_CLIENT_PROFILE_DATA = 0x07;
	public static final int PROTOCOL_CLIENT_FINISH_LOGIN = 0x08;
	public static final int PROTOCOL_SERVER_FINISH_LOGIN = 0x09;
	public static final int PROTOCOL_SERVER_ERROR = 0xFF;

	public static final int STATE_OPENED = 0x00;
	public static final int STATE_CLIENT_VERSION = 0x01;
	public static final int STATE_CLIENT_LOGIN = 0x02;
	public static final int STATE_CLIENT_COMPLETE = 0x03;

	public static final int SERVER_ERROR_UNKNOWN_PACKET = 0x01;
	public static final int SERVER_ERROR_INVALID_PACKET = 0x02;
	public static final int SERVER_ERROR_WRONG_PACKET = 0x03;
	public static final int SERVER_ERROR_EXCESSIVE_PROFILE_DATA = 0x04;
	public static final int SERVER_ERROR_DUPLICATE_PROFILE_DATA = 0x05;
	public static final int SERVER_ERROR_RATELIMIT_BLOCKED = 0x06;
	public static final int SERVER_ERROR_RATELIMIT_LOCKED = 0x07;
	public static final int SERVER_ERROR_CUSTOM_MESSAGE = 0x08;
	public static final int SERVER_ERROR_AUTHENTICATION_REQUIRED = 0x09;

	public static final int AUTH_METHOD_NONE = 0x0;
	public static final int AUTH_METHOD_EAGLER_SHA256 = 0x01;
	public static final int AUTH_METHOD_AUTHME_SHA256 = 0x02;
	public static final int AUTH_METHOD_PLAINTEXT = 0xFF;

	public static final byte[] EAGLER_SHA256_SALT_BASE = new byte[] { (byte) 117, (byte) 43, (byte) 1, (byte) 112,
			(byte) 75, (byte) 3, (byte) 188, (byte) 61, (byte) 121, (byte) 31, (byte) 34, (byte) 181, (byte) 234,
			(byte) 31, (byte) 247, (byte) 72, (byte) 12, (byte) 168, (byte) 138, (byte) 45, (byte) 143, (byte) 77,
			(byte) 118, (byte) 245, (byte) 187, (byte) 242, (byte) 188, (byte) 219, (byte) 160, (byte) 235, (byte) 235,
			(byte) 68 };

	public static final byte[] EAGLER_SHA256_SALT_SAVE = new byte[] { (byte) 49, (byte) 25, (byte) 39, (byte) 38,
			(byte) 253, (byte) 85, (byte) 70, (byte) 245, (byte) 71, (byte) 150, (byte) 253, (byte) 206, (byte) 4,
			(byte) 26, (byte) 198, (byte) 249, (byte) 145, (byte) 251, (byte) 232, (byte) 174, (byte) 186, (byte) 98,
			(byte) 27, (byte) 232, (byte) 55, (byte) 144, (byte) 83, (byte) 21, (byte) 36, (byte) 55, (byte) 170,
			(byte) 118 };

}
