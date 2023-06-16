package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;

import java.io.InputStream;
import java.util.List;
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
	private final String prefix;

	public FolderResourcePack(String resourcePackFileIn, String prefix) {
		super(resourcePackFileIn);
		this.prefix = prefix;
	}

	protected InputStream getInputStreamByName(String name) {
		return SYS.VFS.getFile(prefix + this.resourcePackFile + "/" + name).getInputStream();
	}

	protected boolean hasResourceName(String name) {
		return SYS.VFS.fileExists(prefix + this.resourcePackFile + "/" + name);
	}

	public Set<String> getResourceDomains() {
		Set<String> set = Sets.<String>newHashSet();
		String pfx = prefix + this.resourcePackFile + "/assets/";
		List<String> files = SYS.VFS.listFiles(pfx);

		for (String file : files) {
			String s = file.substring(pfx.length());
			int ind = s.indexOf('/');
			if (ind != -1) s = s.substring(0, ind);
			if (!s.equals(s.toLowerCase())) {
				this.logNameNotLowercase(s);
			} else {
				set.add(s);
			}
		}

		return set;
	}
}