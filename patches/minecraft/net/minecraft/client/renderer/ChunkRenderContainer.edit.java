
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 6  @  1 : 2

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> INSERT  3 : 4  @  3

+ import net.minecraft.util.MathHelper;

> CHANGE  16 : 17  @  16 : 17

~ 	public void preRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer enumworldblocklayer) {

> CHANGE  1 : 11  @  1 : 4

~ 		float posX = (float) ((double) blockpos.getX() - this.viewEntityX);
~ 		float posY = (float) ((double) blockpos.getY() - this.viewEntityY);
~ 		float posZ = (float) ((double) blockpos.getZ() - this.viewEntityZ);
~ 		GlStateManager.translate(posX, posY, posZ);
~ 		if (DeferredStateManager.isInForwardPass()) {
~ 			posX = (float) (blockpos.getX() - (MathHelper.floor_double(this.viewEntityX / 16.0) << 4)); // TODO
~ 			posY = (float) (blockpos.getY() - (MathHelper.floor_double(this.viewEntityY / 16.0) << 4));
~ 			posZ = (float) (blockpos.getZ() - (MathHelper.floor_double(this.viewEntityZ / 16.0) << 4));
~ 			DeferredStateManager.reportForwardRenderObjectPosition((int) posX, (int) posY, (int) posZ);
~ 		}

> EOF
