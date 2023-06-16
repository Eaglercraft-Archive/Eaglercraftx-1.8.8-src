package net.lax1dude.eaglercraft.v1_8.opengl;

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
public class OpenGlHelper {

	public static final int defaultTexUnit = RealOpenGLEnums.GL_TEXTURE0;

	public static final int lightmapTexUnit = RealOpenGLEnums.GL_TEXTURE1;

	public static void setLightmapTextureCoords(int unit, float x, float y) {
		GlStateManager.setActiveTexture(lightmapTexUnit);
		GlStateManager.texCoords2D(x, y);
		GlStateManager.setActiveTexture(defaultTexUnit);
	}
	

}
