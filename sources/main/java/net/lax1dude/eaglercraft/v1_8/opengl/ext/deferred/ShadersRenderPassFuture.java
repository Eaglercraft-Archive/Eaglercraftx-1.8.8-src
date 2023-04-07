package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
public abstract class ShadersRenderPassFuture {

	public static enum PassType {
		MAIN, SHADOW
	}

	protected float x;
	protected float y;
	protected float z;
	protected float partialTicks;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public ShadersRenderPassFuture(float x, float y, float z, float partialTicks) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
	}

	public ShadersRenderPassFuture(Entity e, float partialTicks) {
		this.x = (float)((e.posX - e.prevPosX) * partialTicks + e.prevPosX - TileEntityRendererDispatcher.staticPlayerX);
		this.y = (float)((e.posY - e.prevPosY) * partialTicks + e.prevPosY - TileEntityRendererDispatcher.staticPlayerY);
		this.z = (float)((e.posZ - e.prevPosZ) * partialTicks + e.prevPosZ - TileEntityRendererDispatcher.staticPlayerZ);
	}

	public ShadersRenderPassFuture(Entity e) {
		this(e, EaglerDeferredPipeline.instance.getPartialTicks());
	}

	public abstract void draw(PassType pass);

	private final float[] tmp = new float[1];

	public float[] tmpValue() {
		return tmp;
	}
}
