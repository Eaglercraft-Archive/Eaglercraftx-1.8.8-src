package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

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
public class ShaderPackInfoReloadListener implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void onResourceManagerReload(IResourceManager mcResourceManager) {
		Minecraft mc = Minecraft.getMinecraft();
		try {
			mc.gameSettings.deferredShaderConf.reloadShaderPackInfo(mcResourceManager);
		}catch(IOException ex) {
			logger.info("Could not reload shader pack info!");
			logger.info(ex);
			logger.info("Shaders have been disabled");
			mc.gameSettings.shaders = false;
		}
		TextureMap tm = mc.getTextureMapBlocks();
		if(tm != null) {
			mc.getTextureMapBlocks().setEnablePBREagler(mc.gameSettings.shaders);
		}
	}

}
