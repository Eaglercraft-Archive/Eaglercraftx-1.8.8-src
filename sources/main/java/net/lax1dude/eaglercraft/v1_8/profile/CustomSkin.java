package net.lax1dude.eaglercraft.v1_8.profile;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

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
public class CustomSkin {

	public final String name;
	public final byte[] texture;
	public SkinModel model;

	private EaglerSkinTexture textureInstance;
	private ResourceLocation resourceLocation;

	private static int texId = 0;

	public CustomSkin(String name, byte[] texture, SkinModel model) {
		this.name = name;
		this.texture = texture;
		this.model = model;
		this.textureInstance = new EaglerSkinTexture(texture, model.width, model.height);
		this.resourceLocation = null;
	}
	
	public void load() {
		if(resourceLocation == null) {
			resourceLocation = new ResourceLocation("eagler:skins/custom/tex_" + texId++);
			Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, textureInstance);
		}
	}
	
	public ResourceLocation getResource() {
		return resourceLocation;
	}
	
	public void delete() {
		if(resourceLocation != null) {
			Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
			resourceLocation = null;
		}
	}

}
