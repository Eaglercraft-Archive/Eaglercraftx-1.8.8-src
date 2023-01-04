package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import io.netty.channel.Channel;
import net.md_5.bungee.UserConnection;

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
public class EaglerConnectionInstance {

	public final Channel channel;

	public final long creationTime;
	public boolean hasBeenForwarded = false;
	
	public long lastServerPingPacket;
	public long lastClientPingPacket;
	public long lastClientPongPacket;

	public boolean isWebSocket = false;
	public boolean isRegularHttp = false;
	
	public UserConnection userConnection = null;
	
	public EaglerConnectionInstance(Channel channel) {
		this.channel = channel;
		this.creationTime = this.lastServerPingPacket = this.lastClientPingPacket =
				this.lastClientPongPacket = System.currentTimeMillis();
	}

}
