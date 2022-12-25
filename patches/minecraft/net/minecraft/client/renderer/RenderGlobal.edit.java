
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  8 : 10  @  13 : 14

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  12 : 26  @  16

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

> DELETE  38  @  28 : 41

> DELETE  42  @  45 : 46

> DELETE  48  @  52 : 53

> DELETE  52  @  57 : 60

> DELETE  54  @  62 : 65

> DELETE  83  @  94 : 99

> DELETE  103  @  119 : 123

> CHANGE  106 : 107  @  126 : 129

~ 	private final EaglerTextureAtlasSprite[] destroyBlockIcons = new EaglerTextureAtlasSprite[10];

> CHANGE  118 : 119  @  140 : 141

~ 	private final ChunkUpdateManager renderDispatcher = new ChunkUpdateManager();

> CHANGE  141 : 143  @  163 : 165

~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  145 : 148  @  167 : 179

~ 		this.vboEnabled = false;
~ 		this.renderContainer = new RenderList();
~ 		this.renderChunkFactory = new ListChunkFactory();

> DELETE  167  @  198 : 202

> DELETE  168  @  203 : 224

> DELETE  171  @  227 : 233

> CHANGE  175 : 176  @  237 : 239

~ 		return false;

> DELETE  181  @  244 : 247

> CHANGE  187 : 192  @  253 : 266

~ 		this.glSkyList2 = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList2, GL_COMPILE);
~ 		this.renderSky(worldrenderer, -16.0F, true);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  198  @  272 : 275

> CHANGE  204 : 209  @  281 : 294

~ 		this.glSkyList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.glSkyList, GL_COMPILE);
~ 		this.renderSky(worldrenderer, 16.0F, false);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();

> DELETE  238  @  323 : 326

> CHANGE  244 : 251  @  332 : 347

~ 		this.starGLCallList = GLAllocation.generateDisplayLists();
~ 		GlStateManager.pushMatrix();
~ 		EaglercraftGPU.glNewList(this.starGLCallList, GL_COMPILE);
~ 		this.renderStars(worldrenderer);
~ 		tessellator.draw();
~ 		EaglercraftGPU.glEndList();
~ 		GlStateManager.popMatrix();

> CHANGE  255 : 256  @  351 : 352

~ 		EaglercraftRandom random = new EaglercraftRandom(10842L);

> DELETE  326  @  422 : 431

> DELETE  327  @  432 : 438

> DELETE  355  @  466 : 470

> DELETE  356  @  471 : 472

> DELETE  397  @  513 : 550

> CHANGE  450 : 451  @  603 : 604

~ 					for (TileEntity tileentity2 : (List<TileEntity>) list1) {

> CHANGE  503 : 504  @  656 : 657

~ 		return HString.format("C: %d/%d %sD: %d, %s",

> DELETE  619  @  772 : 773

> CHANGE  621 : 622  @  775 : 776

~ 					if ((!flag1 || !renderglobal$containerlocalrenderinformation1.setFacing // TODO:

> DELETE  644  @  798 : 799

> CHANGE  651 : 653  @  806 : 807

~ 				if (this.mc.gameSettings.chunkFix ? this.isPositionInRenderChunkHack(blockpos1, renderchunk4)
~ 						: this.isPositionInRenderChunk(blockpos, renderchunk4)) {

> INSERT  674 : 684  @  828

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

> INSERT  713 : 714  @  857

+ 		((ClippingHelperImpl) this.debugFixedClippingHelper).destroy();

> DELETE  808  @  951 : 961

> DELETE  809  @  962 : 982

> CHANGE  902 : 903  @  1075 : 1085

~ 			GlStateManager.callList(this.glSkyList);

> CHANGE  941 : 942  @  1123 : 1124

~ 							.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (f13 * 40.0F * afloat[3]))

> CHANGE  984 : 985  @  1166 : 1176

~ 				GlStateManager.callList(this.starGLCallList);

> CHANGE  998 : 999  @  1189 : 1199

~ 				GlStateManager.callList(this.glSkyList2);

> CHANGE  1371 : 1372  @  1571 : 1572

~ 		this.displayListEntitiesDirty |= this.renderDispatcher.updateChunks(finishTimeNano);

> DELETE  1389  @  1589 : 1590

> CHANGE  1564 : 1565  @  1765 : 1766

~ 							EaglerTextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];

> CHANGE  1586 : 1587  @  1787 : 1788

~ 			EaglercraftGPU.glLineWidth(2.0F);

> CHANGE  1827 : 1828  @  2028 : 2029

~ 		EaglercraftRandom random = this.theWorld.rand;

> INSERT  2057 : 2074  @  2258

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
