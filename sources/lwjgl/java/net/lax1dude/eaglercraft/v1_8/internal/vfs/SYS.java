package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

public class SYS {

	public static final Object VFS = null;

	public static final void loadRemoteResourcePack(String url, String hash, Consumer<String> cb, Consumer<Runnable> ast, Runnable loading) {
		return;
	}

	public static final boolean loadResourcePack(String name, InputStream data, String hash) {
		return false;
	}

	public static final List<String> getResourcePackNames() {
		return new ArrayList<>();
	}

	public static final void deleteResourcePack(String packName) {
		//
	}
}
