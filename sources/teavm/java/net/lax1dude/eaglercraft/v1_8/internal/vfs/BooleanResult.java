package net.lax1dude.eaglercraft.v1_8.internal.vfs;

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

public class BooleanResult {
	
	public static final BooleanResult TRUE = new BooleanResult(true);
	public static final BooleanResult FALSE = new BooleanResult(false);
	
	public final boolean bool;
	
	private BooleanResult(boolean b) {
		bool = b;
	}
	
	public static BooleanResult _new(boolean b) {
		return b ? TRUE : FALSE;
	}
	
}