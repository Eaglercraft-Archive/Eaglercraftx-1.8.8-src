package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

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

public class FolderResourcePack extends AbstractResourcePack {
	public FolderResourcePack(String resourcePackFileIn, String prefix) {
		super(resourcePackFileIn);
	}

	protected InputStream getInputStreamByName(String name) {
		return new BufferedInputStream(new ByteArrayInputStream(new byte[0]));
	}

	protected boolean hasResourceName(String name) {
		return false;
	}

	public Set<String> getResourceDomains() {
		return Sets.<String>newHashSet();
	}
}