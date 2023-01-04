package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

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
	public static final int STATE_STALLING = 0xFF;

	public static final int SERVER_ERROR_UNKNOWN_PACKET = 0x01;
	public static final int SERVER_ERROR_INVALID_PACKET = 0x02;
	public static final int SERVER_ERROR_WRONG_PACKET = 0x03;
	public static final int SERVER_ERROR_EXCESSIVE_PROFILE_DATA = 0x04;
	public static final int SERVER_ERROR_DUPLICATE_PROFILE_DATA = 0x05;
	public static final int SERVER_ERROR_RATELIMIT_BLOCKED = 0x06;
	public static final int SERVER_ERROR_RATELIMIT_LOCKED = 0x07;
	public static final int SERVER_ERROR_CUSTOM_MESSAGE = 0x08;
	public static final int SERVER_ERROR_AUTHENTICATION_REQUIRED = 0x09;

}
