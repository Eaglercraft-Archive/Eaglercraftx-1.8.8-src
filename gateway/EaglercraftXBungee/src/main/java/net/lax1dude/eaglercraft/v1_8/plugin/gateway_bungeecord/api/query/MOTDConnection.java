package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query;

import java.net.InetAddress;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;

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
public interface MOTDConnection {

	boolean isClosed();
	void close();

	String getAccept();
	InetAddress getAddress();
	EaglerListenerConfig getListener();
	long getConnectionTimestamp();
	
	public default long getConnectionAge() {
		return System.currentTimeMillis() - getConnectionTimestamp();
	}
	
	void sendToUser();
	
	String getLine1();
	String getLine2();
	List<String> getPlayerList();
	int[] getBitmap();
	int getOnlinePlayers();
	int getMaxPlayers();
	String getSubType();
	
	void setLine1(String p);
	void setLine2(String p);
	void setPlayerList(List<String> p);
	void setPlayerList(String... p);
	void setBitmap(int[] p);
	void setOnlinePlayers(int i);
	void setMaxPlayers(int i);

}
