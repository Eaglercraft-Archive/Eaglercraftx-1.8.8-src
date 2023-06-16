package net.lax1dude.eaglercraft.v1_8.internal;

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
public enum EnumEaglerConnectionState {
	CLOSED(true, false), CONNECTING(false, false), CONNECTED(false, true), FAILED(true, false);

	private final boolean typeClosed;
	private final boolean typeOpen;
	
	private EnumEaglerConnectionState(boolean typeClosed, boolean typeOpen) {
		this.typeClosed = typeClosed;
		this.typeOpen = typeOpen;
	}
	
	public boolean isClosed() {
		return typeClosed;
	}
	
	public boolean isOpen() {
		return typeOpen;
	}
	
}
