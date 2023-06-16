package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.MOTDConnection;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.md_5.bungee.api.plugin.Event;

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
public class EaglercraftMOTDEvent extends Event {

	protected final MOTDConnection connection;

	public EaglercraftMOTDEvent(MOTDConnection connection) {
		this.connection = connection;
	}

	public InetAddress getRemoteAddress() {
		return connection.getAddress();
	}
	
	public EaglerListenerConfig getListener() {
		return connection.getListener();
	}
	
	public String getAccept() {
		return connection.getAccept();
	}
	
	public MOTDConnection getConnection() {
		return connection;
	}

}
