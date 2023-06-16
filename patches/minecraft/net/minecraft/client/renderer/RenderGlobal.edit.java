
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  6 : 8  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  2 : 16  @  2

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

> DELETE  12  @  12 : 25

> DELETE  4  @  4 : 5

> DELETE  6  @  6 : 7

> DELETE  4  @  4 : 7

> DELETE  2  @  2 : 5

> DELETE  29  @  29 : 34

> DELETE  20  @  20 : 24

> CHANGE  3 : 4  @  3 : 6

~ 	private final EaglerTextureAtlasSprite[] destroyBlockIcons = new EaglerTextureAtlasSprite[10];

> CHANGE  11 : 12  @  11 : 12

~ 	private final ChunkUpdateManager renderDispatcher = new ChunkUpdateManager();

> CHANGE  22 : 24  @  22 : 24

~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  2 : 5  @  2 : 14

~ 		this.vboEnabled = false;
~ 		this.renderContainer = new RenderList();
~ 		this.renderChunkFactory = new ListChunkFactory();

> DELETE  19  @  19 : 23

> DELETE  1  @  1 : 22

> DELETE  3  @  3 : 9

> CHANGE  4 : 5  @  4 : 6

~ 		return false;

> DELETE  5  @  5 : 8

> CHANGE  6 : 11  @  6 : 19

~ 		this.glSkyList2 = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList2, GL_COMPILE);
~ 		this.renderSky(worldrenderer, -16.0F, true);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  6  @  6 : 9

> CHANGE  6 : 11  @  6 : 19

~ 		this.glSkyList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList, GL_COMPILE);
~ 		this.renderSky(worldrenderer, 16.0F, false);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  29  @  29 : 32

> CHANGE  6 : 13  @  6 : 21

~ 		this.starGLCallList = GLAllocation.generateDisplayLists();
~ 		GlStateManager.pushMatrix();
~ 		EaglercraftGPU.glNewList(this.starGLCallList, GL_COMPILE);
~ 		this.renderStars(worldrenderer);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();
~ 		GlStateManager.popMatrix();

> CHANGE  4 : 5  @  4 : 5

~ 		EaglercraftRandom random = new EaglercraftRandom(10842L);

> DELETE  70  @  70 : 79

> DELETE  1  @  1 : 7

> DELETE  28  @  28 : 32

> DELETE  1  @  1 : 2

> DELETE  41  @  41 : 78

> CHANGE  53 : 54  @  53 : 54

~ 					for (TileEntity tileentity2 : (List<TileEntity>) list1) {

> CHANGE  52 : 53  @  52 : 53

~ 		return HString.format("C: %d/%d %sD: %d, %s",

> DELETE  115  @  115 : 116

> CHANGE  2 : 3  @  2 : 3

~ 					if ((!flag1 || !renderglobal$containerlocalrenderinformation1.setFacing // TODO:

> DELETE  22  @  22 : 23

> CHANGE  7 : 9  @  7 : 8

~ 				if (this.mc.gameSettings.chunkFix ? this.isPositionInRenderChunkHack(blockpos1, renderchunk4)
~ 						: this.isPositionInRenderChunk(blockpos, renderchunk4)) {

> INSERT  21 : 31  @  21

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

> INSERT  29 : 30  @  29

+ 		((ClippingHelperImpl) this.debugFixedClippingHelper).destroy();

> DELETE  94  @  94 : 104

> DELETE  1  @  1 : 21

> CHANGE  93 : 94  @  93 : 103

~ 			GlStateManager.callList(this.glSkyList);

> CHANGE  38 : 39  @  38 : 39

~ 							.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (f13 * 40.0F * afloat[3]))

> CHANGE  42 : 43  @  42 : 52

~ 				GlStateManager.callList(this.starGLCallList);

> CHANGE  13 : 14  @  13 : 23

~ 				GlStateManager.callList(this.glSkyList2);

> CHANGE  372 : 373  @  372 : 373

~ 		this.displayListEntitiesDirty |= this.renderDispatcher.updateChunks(finishTimeNano);

> DELETE  17  @  17 : 18

> CHANGE  175 : 176  @  175 : 176

~ 							EaglerTextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];

> CHANGE  21 : 22  @  21 : 22

~ 			EaglercraftGPU.glLineWidth(2.0F);

> CHANGE  240 : 241  @  240 : 241

~ 		EaglercraftRandom random = this.theWorld.rand;

> INSERT  229 : 246  @  229

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
