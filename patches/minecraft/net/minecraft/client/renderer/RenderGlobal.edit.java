
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  6 : 8  @  11 : 12

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  4 : 18  @  3

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.ChunkUpdateManager;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  26  @  12 : 25

> DELETE  4  @  17 : 18

> DELETE  6  @  7 : 8

> DELETE  4  @  5 : 8

> DELETE  2  @  5 : 8

> DELETE  29  @  32 : 37

> DELETE  20  @  25 : 29

> CHANGE  3 : 4  @  7 : 10

~ 	private final EaglerTextureAtlasSprite[] destroyBlockIcons = new EaglerTextureAtlasSprite[10];

> CHANGE  12 : 13  @  14 : 15

~ 	private final ChunkUpdateManager renderDispatcher = new ChunkUpdateManager();

> CHANGE  23 : 25  @  23 : 25

~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  4 : 7  @  4 : 16

~ 		this.vboEnabled = false;
~ 		this.renderContainer = new RenderList();
~ 		this.renderChunkFactory = new ListChunkFactory();

> DELETE  22  @  31 : 35

> DELETE  1  @  5 : 26

> DELETE  3  @  24 : 30

> CHANGE  4 : 5  @  10 : 12

~ 		return false;

> DELETE  6  @  7 : 10

> CHANGE  6 : 11  @  9 : 22

~ 		this.glSkyList2 = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList2, GL_COMPILE);
~ 		this.renderSky(worldrenderer, -16.0F, true);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  11  @  19 : 22

> CHANGE  6 : 11  @  9 : 22

~ 		this.glSkyList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList, GL_COMPILE);
~ 		this.renderSky(worldrenderer, 16.0F, false);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  34  @  42 : 45

> CHANGE  6 : 13  @  9 : 24

~ 		this.starGLCallList = GLAllocation.generateDisplayLists();
~ 		GlStateManager.pushMatrix();
~ 		EaglercraftGPU.glNewList(this.starGLCallList, GL_COMPILE);
~ 		this.renderStars(worldrenderer);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();
~ 		GlStateManager.popMatrix();

> CHANGE  11 : 12  @  19 : 20

~ 		EaglercraftRandom random = new EaglercraftRandom(10842L);

> DELETE  71  @  71 : 80

> DELETE  1  @  10 : 16

> DELETE  28  @  34 : 38

> DELETE  1  @  5 : 6

> DELETE  41  @  42 : 79

> CHANGE  53 : 54  @  90 : 91

~ 					for (TileEntity tileentity2 : (List<TileEntity>) list1) {

> CHANGE  53 : 54  @  53 : 54

~ 		return HString.format("C: %d/%d %sD: %d, %s",

> DELETE  116  @  116 : 117

> CHANGE  2 : 3  @  3 : 4

~ 					if ((!flag1 || !renderglobal$containerlocalrenderinformation1.setFacing // TODO:

> DELETE  23  @  23 : 24

> CHANGE  7 : 9  @  8 : 9

~ 				if (this.mc.gameSettings.chunkFix ? this.isPositionInRenderChunkHack(blockpos1, renderchunk4)
~ 						: this.isPositionInRenderChunk(blockpos, renderchunk4)) {

> INSERT  23 : 33  @  22

+ 	/**
+ 	 * WARNING: use only in the above "build near" logic
+ 	 */
+ 	private boolean isPositionInRenderChunkHack(BlockPos pos, RenderChunk renderChunkIn) {
+ 		BlockPos blockpos = renderChunkIn.getPosition();
+ 		return MathHelper.abs_int(pos.getX() - blockpos.getX() - 8) > 11 ? false
+ 				: (MathHelper.abs_int(pos.getY() - blockpos.getY() - 8) > 11 ? false
+ 						: MathHelper.abs_int(pos.getZ() - blockpos.getZ() - 8) <= 11);
+ 	}
+ 

> INSERT  39 : 40  @  29

+ 		((ClippingHelperImpl) this.debugFixedClippingHelper).destroy();

> DELETE  95  @  94 : 104

> DELETE  1  @  11 : 31

> CHANGE  93 : 94  @  113 : 123

~ 			GlStateManager.callList(this.glSkyList);

> CHANGE  39 : 40  @  48 : 49

~ 							.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (f13 * 40.0F * afloat[3]))

> CHANGE  43 : 44  @  43 : 53

~ 				GlStateManager.callList(this.starGLCallList);

> CHANGE  14 : 15  @  23 : 33

~ 				GlStateManager.callList(this.glSkyList2);

> CHANGE  373 : 374  @  382 : 383

~ 		this.displayListEntitiesDirty |= this.renderDispatcher.updateChunks(finishTimeNano);

> DELETE  18  @  18 : 19

> CHANGE  175 : 176  @  176 : 177

~ 							EaglerTextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];

> CHANGE  22 : 23  @  22 : 23

~ 			EaglercraftGPU.glLineWidth(2.0F);

> CHANGE  241 : 242  @  241 : 242

~ 		EaglercraftRandom random = this.theWorld.rand;

> INSERT  230 : 247  @  230

+ 
+ 	public String getDebugInfoShort() {
+ 		int i = this.viewFrustum.renderChunks.length;
+ 		int j = 0;
+ 		int k = 0;
+ 
+ 		for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
+ 			CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
+ 			if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty()) {
+ 				++j;
+ 				k += compiledchunk.getTileEntities().size();
+ 			}
+ 		}
+ 
+ 		return "" + Minecraft.getDebugFPS() + "fps | C: " + j + "/" + i + ", E: " + this.countEntitiesRendered + "+" + k
+ 				+ ", " + renderDispatcher.getDebugInfo();
+ 	}

> EOF
