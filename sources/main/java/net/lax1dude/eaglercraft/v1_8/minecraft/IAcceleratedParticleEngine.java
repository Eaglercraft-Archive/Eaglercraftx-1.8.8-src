package net.lax1dude.eaglercraft.v1_8.minecraft;

import net.minecraft.entity.Entity;

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
public interface IAcceleratedParticleEngine {

	void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a);

	void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba);

	void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a);

	void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba);

}
