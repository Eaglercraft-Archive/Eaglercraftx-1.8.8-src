package net.lax1dude.eaglercraft.v1_8.socket;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;

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
public class AddressResolver {

	public static String resolveURI(ServerData input) {
		return resolveURI(input.serverIP);
	}
	
	public static String resolveURI(String input) {
		String lc = input.toLowerCase();
		if(!lc.startsWith("ws://") && !lc.startsWith("wss://")) {
			if(EagRuntime.requireSSL()) {
				input = "wss://" + input;
			}else {
				input = "ws://" + input;
			}
		}
		return input;
	}

	public static ServerAddress resolveAddressFromURI(String input) {
		String uri = resolveURI(input);
		String lc = input.toLowerCase();
		if(lc.startsWith("ws://")) {
			input = input.substring(5);
		}else if(lc.startsWith("wss://")) {
			input = input.substring(6);
		}
		int port = EagRuntime.requireSSL() ? 443: 80;
		int i = input.indexOf('/');
		if(i != -1) {
			input = input.substring(0, i);
		}
		i = input.lastIndexOf(':');
		if(i != -1) {
			try {
				port = Integer.parseInt(input.substring(i + 1));
			}catch(Throwable t) {
			}
		}
		return new ServerAddress(uri, port);
	}
	
}
