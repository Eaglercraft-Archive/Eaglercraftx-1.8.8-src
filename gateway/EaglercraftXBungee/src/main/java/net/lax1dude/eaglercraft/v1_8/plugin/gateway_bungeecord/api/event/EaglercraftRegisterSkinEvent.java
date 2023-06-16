package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.util.UUID;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.protocol.Property;

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
public class EaglercraftRegisterSkinEvent extends Event {

	private final String username;
	private final UUID uuid;
	private Property useMojangProfileProperty = null;
	private boolean useLoginResultTextures = false;
	private int presetId = -1;
	private byte[] customTex = null;
	private String customURL = null;

	public EaglercraftRegisterSkinEvent(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
	}

	public void setForceUseMojangProfileProperty(Property prop) {
		useMojangProfileProperty = prop;
		useLoginResultTextures = false;
		presetId = -1;
		customTex = null;
		customURL = null;
	}

	public void setForceUseLoginResultObjectTextures(boolean b) {
		useMojangProfileProperty = null;
		useLoginResultTextures = b;
		presetId = -1;
		customTex = null;
		customURL = null;
	}

	public void setForceUsePreset(int p) {
		useMojangProfileProperty = null;
		useLoginResultTextures = false;
		presetId = p;
		customTex = new byte[5];
		customTex[0] = (byte)1;
		customTex[1] = (byte)(p >> 24);
		customTex[2] = (byte)(p >> 16);
		customTex[3] = (byte)(p >> 8);
		customTex[4] = (byte)(p & 0xFF);
		customURL = null;
	}

	public void setForceUseCustom(int model, byte[] tex) {
		useMojangProfileProperty = null;
		useLoginResultTextures = false;
		presetId = -1;
		customTex = new byte[2 + tex.length];
		customTex[0] = (byte)2;
		customTex[1] = (byte)model;
		System.arraycopy(tex, 0, customTex, 2, tex.length);
		customURL = null;
	}

	public void setForceUseCustomByPacket(byte[] packet) {
		useMojangProfileProperty = null;
		useLoginResultTextures = false;
		presetId = -1;
		customTex = packet;
		customURL = null;
	}

	public void setForceUseURL(String url) {
		useMojangProfileProperty = null;
		useLoginResultTextures = false;
		presetId = -1;
		customTex = null;
		customURL = url;
	}

	public String getUsername() {
		return username;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Property getForceUseMojangProfileProperty() {
		return useMojangProfileProperty;
	}

	public boolean getForceUseLoginResultObjectTextures() {
		return useLoginResultTextures;
	}

	public byte[] getForceSetUseCustomPacket() {
		return customTex;
	}

	public String getForceSetUseURL() {
		return customURL;
	}

}
