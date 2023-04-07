package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.minecraft.entity.Entity;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class NameTagRenderer {

	public static boolean doRenderNameTags = false;
	public static final NameTagRenderer[] nameTagsThisFrame = new NameTagRenderer[256];
	public static int nameTagsCount = 0;

	static {
		for(int i = 0; i < nameTagsThisFrame.length; ++i) {
			nameTagsThisFrame[i] = new NameTagRenderer();
		}
	}

	public Entity entityIn;
	public String str;
	public double x;
	public double y;
	public double z;
	public int maxDistance;
	public double dst2;

	public static void renderNameTag(Entity entityIn, String str, double x, double y, double z, int maxDistance) {
		if(!doRenderNameTags || nameTagsCount >= nameTagsThisFrame.length) {
			return;
		}
		NameTagRenderer n = nameTagsThisFrame[nameTagsCount++];
		n.entityIn = entityIn;
		n.str = str;
		n.x = x;
		n.y = y;
		n.z = z;
		n.dst2 = x * x + y * y + z * z;
		n.maxDistance = maxDistance;
	}

}
