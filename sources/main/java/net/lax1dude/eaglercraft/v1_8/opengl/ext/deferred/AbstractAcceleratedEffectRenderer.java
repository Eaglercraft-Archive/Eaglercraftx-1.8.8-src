package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
import net.minecraft.client.particle.EntityFX;
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
public abstract class AbstractAcceleratedEffectRenderer implements IAcceleratedParticleEngine {

	public float partialTicks;

	@Override
	public void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY, int lightMapData,
			int texSize, float particleSize, float r, float g, float b, float a) {
		float xx = (float) (entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * (double) partialTicks - EntityFX.interpPosX);
		float yy = (float) (entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * (double) partialTicks - EntityFX.interpPosY);
		float zz = (float) (entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * (double) partialTicks - EntityFX.interpPosZ);
		drawParticle(xx, yy, zz, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, r, g, b, a);
	}

	@Override
	public void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY, int lightMapData,
			int texSize, float particleSize, int rgba) {
		float xx = (float) (entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * (double) partialTicks - EntityFX.interpPosX);
		float yy = (float) (entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * (double) partialTicks - EntityFX.interpPosY);
		float zz = (float) (entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * (double) partialTicks - EntityFX.interpPosZ);
		drawParticle(xx, yy, zz, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, rgba);
	}

	@Override
	public void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a) {
		int color = ((int)(a * 255.0f) << 24) | ((int)(r * 255.0f) << 16) | ((int)(g * 255.0f) << 8) | (int)(b * 255.0f);
		drawParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, color);
	}

}
