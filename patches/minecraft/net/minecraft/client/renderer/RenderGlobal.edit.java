
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  6 : 10  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ 

> INSERT  2 : 22  @  2

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
+ import net.lax1dude.eaglercraft.v1_8.opengl.VertexFormat;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredConfig;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.SharedPipelineShaders;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  12  @  12 : 25

> DELETE  4  @  4 : 5

> DELETE  6  @  6 : 7

> DELETE  4  @  4 : 7

> DELETE  2  @  2 : 5

> INSERT  15 : 16  @  15

+ import net.minecraft.util.ChatComponentText;

> INSERT  1 : 2  @  1

+ import net.minecraft.util.EnumChatFormatting;

> DELETE  13  @  13 : 18

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

> DELETE  67  @  67 : 79

> CHANGE  1 : 5  @  1 : 5

~ 			if (mc.gameSettings.shaders) {
~ 				if (!EaglerDeferredPipeline.isSupported()) {
~ 					mc.gameSettings.shaders = false;
~ 				}

> INSERT  2 : 6  @  2

+ 			Blocks.leaves.setGraphicsLevel(mc.gameSettings.shaders || mc.gameSettings.fancyGraphics);
+ 			Blocks.leaves2.setGraphicsLevel(mc.gameSettings.shaders || mc.gameSettings.fancyGraphics);
+ 			this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
+ 

> INSERT  19 : 76  @  19

+ 
+ 			if (mc.gameSettings.shaders) {
+ 				EaglerDeferredConfig dfc = mc.gameSettings.deferredShaderConf;
+ 				dfc.updateConfig();
+ 				if (theWorld.provider.getHasNoSky()) {
+ 					dfc.is_rendering_shadowsSun_clamped = 0;
+ 					dfc.is_rendering_lightShafts = false;
+ 				} else {
+ 					int maxDist = renderDistanceChunks << 4;
+ 					int ss = dfc.is_rendering_shadowsSun;
+ 					while (ss > 1 && (1 << (ss + 3)) > maxDist) {
+ 						--ss;
+ 					}
+ 					dfc.is_rendering_shadowsSun_clamped = ss;
+ 					dfc.is_rendering_lightShafts = dfc.lightShafts;
+ 				}
+ 				boolean flag = false;
+ 				if (EaglerDeferredPipeline.instance == null) {
+ 					EaglerDeferredPipeline.instance = new EaglerDeferredPipeline(mc);
+ 					flag = true;
+ 				}
+ 				try {
+ 					SharedPipelineShaders.init();
+ 					EaglerDeferredPipeline.instance.rebuild(dfc);
+ 					EaglerDeferredPipeline.isSuspended = false;
+ 				} catch (Throwable ex) {
+ 					logger.error("Could not enable shaders!");
+ 					logger.error(ex);
+ 					EaglerDeferredPipeline.isSuspended = true;
+ 				}
+ 				if (flag && !EaglerDeferredPipeline.isSuspended) {
+ 					ChatComponentText shaderF4Msg = new ChatComponentText("[EaglercraftX]");
+ 					shaderF4Msg.getChatStyle().setColor(EnumChatFormatting.GOLD);
+ 					ChatComponentText shaderF4Msg2 = new ChatComponentText(
+ 							" Press " + Keyboard.getKeyName(mc.gameSettings.keyBindFunction.getKeyCode())
+ 									+ "+4 to access the shader debug menu");
+ 					shaderF4Msg2.getChatStyle().setColor(EnumChatFormatting.AQUA);
+ 					shaderF4Msg.appendSibling(shaderF4Msg2);
+ 					mc.ingameGUI.getChatGUI().printChatMessage(shaderF4Msg);
+ 				}
+ 			}
+ 
+ 			mc.gameSettings.shadersAODisable = mc.gameSettings.shaders
+ 					&& mc.gameSettings.deferredShaderConf.is_rendering_ssao;
+ 
+ 			if (!mc.gameSettings.shaders || EaglerDeferredPipeline.isSuspended) {
+ 				try {
+ 					if (EaglerDeferredPipeline.instance != null) {
+ 						EaglerDeferredPipeline.instance.destroy();
+ 						EaglerDeferredPipeline.instance = null;
+ 					}
+ 				} catch (Throwable ex) {
+ 					logger.error("Could not safely disable shaders!");
+ 					logger.error(ex);
+ 				}
+ 				SharedPipelineShaders.free();
+ 			}

> DELETE  9  @  9 : 13

> DELETE  1  @  1 : 2

> INSERT  6 : 7  @  6

+ 			boolean light = DynamicLightManager.isRenderingLights();

> CHANGE  27 : 36  @  27 : 54

~ 			if (!DeferredStateManager.isDeferredRenderer()) {
~ 				for (int i = 0; i < this.theWorld.weatherEffects.size(); ++i) {
~ 					Entity entity1 = (Entity) this.theWorld.weatherEffects.get(i);
~ 					++this.countEntitiesRendered;
~ 					if (entity1.isInRangeToRender3d(d0, d1, d2)) {
~ 						if (light) {
~ 							entity1.renderDynamicLightsEagler(partialTicks, true);
~ 						}
~ 						this.renderManager.renderEntitySimple(entity1, partialTicks);

> DELETE  2  @  2 : 16

> INSERT  24 : 27  @  24

+ 							if (light) {
+ 								entity2.renderDynamicLightsEagler(partialTicks, flag2);
+ 							}

> CHANGE  31 : 32  @  31 : 32

~ 					for (TileEntity tileentity2 : (List<TileEntity>) list1) {

> INSERT  41 : 200  @  41

+ 	public static interface EntityChunkCullAdapter {
+ 		boolean shouldCull(RenderChunk renderChunk);
+ 	}
+ 
+ 	public static interface EntityObjectCullAdapter {
+ 		boolean shouldCull(RenderChunk renderChunk, RenderManager renderManager, Entity entity);
+ 	}
+ 
+ 	public void renderShadowLODEntities(Entity renderViewEntity, float partialTicks,
+ 			EntityChunkCullAdapter entityChunkCull, EntityObjectCullAdapter entityObjectCull) { // TODO
+ 		if (renderEntitiesStartupCounter <= 0) {
+ 			theWorld.theProfiler.startSection("shadow_entity_prepare");
+ 
+ 			TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(theWorld, mc.getTextureManager(),
+ 					mc.fontRendererObj, renderViewEntity, partialTicks);
+ 			renderManager.cacheActiveRenderInfo(theWorld, mc.fontRendererObj, renderViewEntity, mc.pointedEntity,
+ 					mc.gameSettings, partialTicks);
+ 
+ 			double d3 = renderViewEntity.lastTickPosX
+ 					+ (renderViewEntity.posX - renderViewEntity.lastTickPosX) * (double) partialTicks;
+ 			double d4 = renderViewEntity.lastTickPosY
+ 					+ (renderViewEntity.posY - renderViewEntity.lastTickPosY) * (double) partialTicks;
+ 			double d5 = renderViewEntity.lastTickPosZ
+ 					+ (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * (double) partialTicks;
+ 			TileEntityRendererDispatcher.staticPlayerX = d3;
+ 			TileEntityRendererDispatcher.staticPlayerY = d4;
+ 			TileEntityRendererDispatcher.staticPlayerZ = d5;
+ 			renderManager.setRenderPosition(d3, d4, d5);
+ 
+ 			this.theWorld.theProfiler.endStartSection("shadow_entities");
+ 			for (RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation : this.renderInfos) {
+ 				RenderChunk currentRenderChunk = containerlocalrenderinformation.renderChunk;
+ 
+ 				if (!entityChunkCull.shouldCull(currentRenderChunk)) {
+ 					Chunk chunk = this.theWorld
+ 							.getChunkFromBlockCoords(containerlocalrenderinformation.renderChunk.getPosition());
+ 					ClassInheritanceMultiMap<Entity> classinheritancemultimap = chunk
+ 							.getEntityLists()[containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];
+ 					if (!classinheritancemultimap.isEmpty()) {
+ 						Iterator<Entity> iterator = classinheritancemultimap.iterator();
+ 						while (iterator.hasNext()) {
+ 							Entity entity2 = iterator.next();
+ 							if (!entityObjectCull.shouldCull(currentRenderChunk, renderManager, entity2)
+ 									|| entity2.riddenByEntity == this.mc.thePlayer) {
+ 								if (entity2.posY < 0.0D || entity2.posY >= 256.0D
+ 										|| this.theWorld.isBlockLoaded(new BlockPos(entity2))) {
+ 									++this.countEntitiesRendered;
+ 									this.renderManager.renderEntitySimple(entity2, partialTicks);
+ 									mc.entityRenderer.disableLightmap();
+ 									GlStateManager.disableShaderBlendAdd();
+ 									GlStateManager.disableBlend();
+ 									GlStateManager.depthMask(true);
+ 								}
+ 							}
+ 
+ 						}
+ 
+ 						// TODO: why?
+ 						// if (!flag2 && entity2 instanceof EntityWitherSkull) {
+ 						// this.mc.getRenderManager().renderWitherSkull(entity2, partialTicks);
+ 						// }
+ 					}
+ 
+ 					List<TileEntity> tileEntities = currentRenderChunk.compiledChunk.getTileEntities();
+ 					for (int i = 0, l = tileEntities.size(); i < l; ++i) {
+ 						TileEntityRendererDispatcher.instance.renderTileEntity(tileEntities.get(i), partialTicks, -1);
+ 						mc.entityRenderer.disableLightmap();
+ 						GlStateManager.disableShaderBlendAdd();
+ 						GlStateManager.disableBlend();
+ 						GlStateManager.depthMask(true);
+ 					}
+ 				}
+ 			}
+ 
+ 			synchronized (this.field_181024_n) {
+ 				for (TileEntity tileentity : this.field_181024_n) {
+ 					TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
+ 					mc.entityRenderer.disableLightmap();
+ 					GlStateManager.disableShaderBlendAdd();
+ 					GlStateManager.disableBlend();
+ 					GlStateManager.depthMask(true);
+ 				}
+ 			}
+ 			theWorld.theProfiler.endSection();
+ 		}
+ 	}
+ 
+ 	public void renderParaboloidTileEntities(Entity renderViewEntity, float partialTicks, int up) {
+ 		if (renderEntitiesStartupCounter <= 0) {
+ 			theWorld.theProfiler.startSection("paraboloid_entity_prepare");
+ 
+ 			TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(theWorld, mc.getTextureManager(),
+ 					mc.fontRendererObj, renderViewEntity, partialTicks);
+ 			renderManager.cacheActiveRenderInfo(theWorld, mc.fontRendererObj, renderViewEntity, mc.pointedEntity,
+ 					mc.gameSettings, partialTicks);
+ 
+ 			double d3 = renderViewEntity.lastTickPosX
+ 					+ (renderViewEntity.posX - renderViewEntity.lastTickPosX) * (double) partialTicks;
+ 			double d4 = renderViewEntity.lastTickPosY
+ 					+ (renderViewEntity.posY - renderViewEntity.lastTickPosY) * (double) partialTicks;
+ 			double d5 = renderViewEntity.lastTickPosZ
+ 					+ (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * (double) partialTicks;
+ 			TileEntityRendererDispatcher.staticPlayerX = d3;
+ 			TileEntityRendererDispatcher.staticPlayerY = d4;
+ 			TileEntityRendererDispatcher.staticPlayerZ = d5;
+ 			renderManager.setRenderPosition(d3, d4, d5);
+ 
+ 			double rad = 8.0;
+ 
+ 			int minX = (int) (d3 - rad);
+ 			int minY = (int) d4;
+ 			if (up == -1) {
+ 				minY -= rad;
+ 			}
+ 			int minZ = (int) (d5 - rad);
+ 
+ 			int maxX = (int) (d3 + rad);
+ 			int maxY = (int) d4;
+ 			if (up == 1) {
+ 				maxY += rad;
+ 			}
+ 			int maxZ = (int) (d5 + rad);
+ 
+ 			BlockPos tmp = new BlockPos(0, 0, 0);
+ 			minX = MathHelper.floor_double(minX / 16.0) * 16;
+ 			minY = MathHelper.floor_double(minY / 16.0) * 16;
+ 			minZ = MathHelper.floor_double(minZ / 16.0) * 16;
+ 			maxX = MathHelper.floor_double(maxX / 16.0) * 16;
+ 			maxY = MathHelper.floor_double(maxY / 16.0) * 16;
+ 			maxZ = MathHelper.floor_double(maxZ / 16.0) * 16;
+ 
+ 			this.theWorld.theProfiler.endStartSection("paraboloid_entities");
+ 			for (int cx = minX; cx <= maxX; cx += 16) {
+ 				for (int cz = minZ; cz <= maxZ; cz += 16) {
+ 					for (int cy = minY; cy <= maxY; cy += 16) {
+ 						tmp.x = cx;
+ 						tmp.y = cy;
+ 						tmp.z = cz;
+ 						RenderChunk ch = viewFrustum.getRenderChunk(tmp);
+ 						CompiledChunk cch;
+ 						if (ch != null && (cch = ch.compiledChunk) != null) {
+ 							List<TileEntity> tileEntities = cch.getTileEntities();
+ 							for (int i = 0, l = tileEntities.size(); i < l; ++i) {
+ 								mc.entityRenderer.enableLightmap();
+ 								TileEntityRendererDispatcher.instance.renderTileEntity(tileEntities.get(i),
+ 										partialTicks, -1);
+ 								GlStateManager.disableShaderBlendAdd();
+ 								GlStateManager.disableBlend();
+ 								GlStateManager.depthMask(true);
+ 							}
+ 						}
+ 					}
+ 				}
+ 			}
+ 			theWorld.theProfiler.endSection();
+ 			mc.entityRenderer.disableLightmap();
+ 		}
+ 	}
+ 

> CHANGE  11 : 12  @  11 : 12

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

> INSERT  92 : 129  @  92

+ 	public static interface ChunkCullAdapter {
+ 		boolean shouldCull(RenderChunk chunk);
+ 	}
+ 
+ 	public int renderBlockLayerShadow(EnumWorldBlockLayer blockLayerIn, AxisAlignedBB boundingBox,
+ 			ChunkCullAdapter cullAdapter) {
+ 		int i = 0;
+ 		BlockPos tmp = new BlockPos(0, 0, 0);
+ 		int minXChunk = MathHelper.floor_double(boundingBox.minX / 16.0) * 16;
+ 		int minYChunk = MathHelper.floor_double(boundingBox.minY / 16.0) * 16;
+ 		int minZChunk = MathHelper.floor_double(boundingBox.minZ / 16.0) * 16;
+ 		int maxXChunk = MathHelper.floor_double(boundingBox.maxX / 16.0) * 16;
+ 		int maxYChunk = MathHelper.floor_double(boundingBox.maxY / 16.0) * 16;
+ 		int maxZChunk = MathHelper.floor_double(boundingBox.maxZ / 16.0) * 16;
+ 		for (int cx = minXChunk; cx <= maxXChunk; cx += 16) {
+ 			for (int cz = minZChunk; cz <= maxZChunk; cz += 16) {
+ 				for (int cy = minYChunk; cy <= maxYChunk; cy += 16) {
+ 					tmp.x = cx;
+ 					tmp.y = cy;
+ 					tmp.z = cz;
+ 					RenderChunk ch = viewFrustum.getRenderChunk(tmp);
+ 					CompiledChunk cch;
+ 					if (ch != null && (cch = ch.getCompiledChunk()) != null && !cch.isLayerEmpty(blockLayerIn)
+ 							&& !cullAdapter.shouldCull(ch)) {
+ 						this.renderContainer.addRenderChunk(ch, blockLayerIn);
+ 						++i;
+ 					}
+ 				}
+ 			}
+ 		}
+ 		if (i > 0) {
+ 			this.mc.mcProfiler.endStartSection("render_shadow_" + blockLayerIn);
+ 			this.renderContainer.renderChunkLayer(blockLayerIn);
+ 		}
+ 		return i;
+ 	}
+ 

> CHANGE  2 : 16  @  2 : 10

~ 		this.renderContainer.renderChunkLayer(blockLayerIn);
~ 		this.mc.entityRenderer.disableLightmap();
~ 	}
~ 
~ 	public int renderParaboloidBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int up,
~ 			Entity entityIn) {
~ 		double rad = 8.0;
~ 
~ 		int minX = (int) (entityIn.posX - rad);
~ 		int minY = (int) entityIn.posY;
~ 		if (up == -1) {
~ 			minY -= rad * 0.75;
~ 		} else {
~ 			minY += 1.0;

> INSERT  1 : 2  @  1

+ 		int minZ = (int) (entityIn.posZ - rad);

> CHANGE  1 : 31  @  1 : 18

~ 		int maxX = (int) (entityIn.posX + rad);
~ 		int maxY = (int) entityIn.posY;
~ 		if (up == 1) {
~ 			maxY += rad;
~ 		} else {
~ 			maxY += 2.0;
~ 		}
~ 		int maxZ = (int) (entityIn.posZ + rad);
~ 
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		minX = MathHelper.floor_double(minX / 16.0) * 16;
~ 		minY = MathHelper.floor_double(minY / 16.0) * 16;
~ 		minZ = MathHelper.floor_double(minZ / 16.0) * 16;
~ 		maxX = MathHelper.floor_double(maxX / 16.0) * 16;
~ 		maxY = MathHelper.floor_double(maxY / 16.0) * 16;
~ 		maxZ = MathHelper.floor_double(maxZ / 16.0) * 16;
~ 
~ 		int i = 0;
~ 		for (int cx = minX; cx <= maxX; cx += 16) {
~ 			for (int cz = minZ; cz <= maxZ; cz += 16) {
~ 				for (int cy = minY; cy <= maxY; cy += 16) {
~ 					tmp.x = cx;
~ 					tmp.y = cy;
~ 					tmp.z = cz;
~ 					RenderChunk ch = viewFrustum.getRenderChunk(tmp);
~ 					CompiledChunk cch;
~ 					if (ch != null && (cch = ch.getCompiledChunk()) != null && !cch.isLayerEmpty(blockLayerIn)) {
~ 						this.renderContainer.addRenderChunk(ch, blockLayerIn);
~ 						++i;
~ 					}

> CHANGE  3 : 10  @  3 : 5

~ 		if (i > 0) {
~ 			this.mc.mcProfiler.endStartSection("render_paraboloid_" + up + "_" + blockLayerIn);
~ 			this.mc.entityRenderer.enableLightmap();
~ 			this.renderContainer.renderChunkLayer(blockLayerIn);
~ 			this.mc.entityRenderer.disableLightmap();
~ 		}
~ 		return i;

> CHANGE  92 : 93  @  92 : 102

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

> CHANGE  155 : 157  @  155 : 156

~ 			worldRendererIn.begin(7, DeferredStateManager.isDeferredRenderer() ? VertexFormat.BLOCK_SHADERS
~ 					: DefaultVertexFormats.BLOCK);

> CHANGE  19 : 20  @  19 : 20

~ 							EaglerTextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];

> CHANGE  17 : 19  @  17 : 18

~ 		if (partialTicks == 0 && movingObjectPositionIn != null
~ 				&& movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {

> CHANGE  3 : 4  @  3 : 4

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
