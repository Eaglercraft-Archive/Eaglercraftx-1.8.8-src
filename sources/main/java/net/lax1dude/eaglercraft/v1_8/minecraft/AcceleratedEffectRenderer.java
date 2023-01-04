package net.lax1dude.eaglercraft.v1_8.minecraft;

import net.lax1dude.eaglercraft.v1_8.opengl.InstancedParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

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
public class AcceleratedEffectRenderer implements IAcceleratedParticleEngine {

	private float partialTicks;

	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

	public void begin(float partialTicks) {
		this.partialTicks = partialTicks;
		InstancedParticleRenderer.begin();
		Entity et = Minecraft.getMinecraft().getRenderViewEntity();
		if(et != null) {
			f1 = MathHelper.cos(et.rotationYaw * 0.017453292F);
			f2 = MathHelper.sin(et.rotationYaw * 0.017453292F);
			f3 = -f2 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f4 = f1 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f5 = MathHelper.cos(et.rotationPitch * 0.017453292F);
		}
	}

	public void draw(float texCoordWidth, float texCoordHeight) {
		InstancedParticleRenderer.render(texCoordWidth, texCoordHeight, 0.0625f, f1, f5, f2, f3, f4);
	}

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
		InstancedParticleRenderer.appendParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData & 0xFF,
				(lightMapData >> 16) & 0xFF, (int)(particleSize * 16.0f), texSize, r, g, b, a);
	}

	@Override
	public void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba) {
		InstancedParticleRenderer.appendParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData & 0xFF,
				(lightMapData >> 16) & 0xFF, (int)(particleSize * 16.0f), texSize, rgba);
	}

}
