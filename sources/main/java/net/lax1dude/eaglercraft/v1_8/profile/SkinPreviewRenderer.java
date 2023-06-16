package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.RenderHelper;

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
public class SkinPreviewRenderer {

	private static ModelPlayer playerModelSteve = null;
	private static ModelPlayer playerModelAlex = null;
	private static ModelZombie playerModelZombie = null;
	
	public static void initialize() {
		playerModelSteve = new ModelPlayer(0.0f, false);
		playerModelSteve.isChild = false;
		playerModelAlex = new ModelPlayer(0.0f, true);
		playerModelAlex.isChild = false;
		playerModelZombie = new ModelZombie(0.0f, true);
		playerModelZombie.isChild = false;
	}

	public static void renderBiped(int x, int y, int mx, int my, SkinModel skinModel) {
		ModelBiped model;
		switch(skinModel) {
		case STEVE:
		default:
			model = playerModelSteve;
			break;
		case ALEX:
			model = playerModelAlex;
			break;
		case ZOMBIE:
			model = playerModelZombie;
			break;
		}
		
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y - 80.0f, 100.0f);
		GlStateManager.scale(50.0f, 50.0f, 50.0f);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(1.0f, -1.0f, 1.0f);
		
		RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.translate(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(((y - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(((x - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0f, -1.0f, 0.0f);
		
		model.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 2000000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625f);
		
		GlStateManager.popMatrix();
		GlStateManager.disableLighting();
	}

}
