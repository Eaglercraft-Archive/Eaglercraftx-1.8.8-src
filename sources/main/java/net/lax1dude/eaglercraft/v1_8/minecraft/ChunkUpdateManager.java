package net.lax1dude.eaglercraft.v1_8.minecraft;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldVertexBufferUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;

public class ChunkUpdateManager {

	private static final Logger LOGGER = LogManager.getLogger();

	private final WorldVertexBufferUploader worldVertexUploader;
	private final RegionRenderCacheBuilder renderCache;

	private int chunkUpdatesTotal = 0;
	private int chunkUpdatesTotalLast = 0;
	private int chunkUpdatesTotalImmediate = 0;
	private int chunkUpdatesTotalImmediateLast = 0;
	private int chunkUpdatesQueued = 0;
	private int chunkUpdatesQueuedLast = 0;
	private long chunkUpdatesTotalLastUpdate = 0l;
	
	private final List<ChunkCompileTaskGenerator> queue = new LinkedList();

	public ChunkUpdateManager() {
		worldVertexUploader = new WorldVertexBufferUploader();
		renderCache = new RegionRenderCacheBuilder();
	}
	
	public static class EmptyBlockLayerException extends IllegalStateException {
	}
	
	private void runGenerator(ChunkCompileTaskGenerator generator, Entity entity) {
		generator.setRegionRenderCacheBuilder(renderCache);
		float f = (float) entity.posX;
		float f1 = (float) entity.posY + entity.getEyeHeight();
		float f2 = (float) entity.posZ;
		ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
		generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
		if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
			generator.getRenderChunk().rebuildChunk(f, f1, f2, generator);
		} else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
			RenderChunk r = generator.getRenderChunk();
			try {
				r.resortTransparency(f, f1, f2, generator);
				CompiledChunk ch = generator.getCompiledChunk();
				if(ch.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT) && ch.isLayerEmpty(EnumWorldBlockLayer.REALISTIC_WATER)) {
					throw new EmptyBlockLayerException();
				}
			}catch(EmptyBlockLayerException ex) {
				LOGGER.error("RenderChunk {} tried to update it's TRANSLUCENT layer with no proper initialization", r.getPosition());
				generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
				return; // rip
			}
		}

		generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);

		final CompiledChunk compiledchunk = generator.getCompiledChunk();
		if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
			for (EnumWorldBlockLayer enumworldblocklayer : EnumWorldBlockLayer.values()) {
				if (!compiledchunk.isLayerEmpty(enumworldblocklayer)) {
					this.uploadChunk(enumworldblocklayer,
							generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer),
							generator.getRenderChunk(), compiledchunk);
					generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
				}
			}
			generator.getRenderChunk().setCompiledChunk(compiledchunk);
		} else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
			if(!compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
				this.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder()
								.getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT),
						generator.getRenderChunk(), compiledchunk);
			}
			if(!compiledchunk.isLayerEmpty(EnumWorldBlockLayer.REALISTIC_WATER)) {
				this.uploadChunk(EnumWorldBlockLayer.REALISTIC_WATER, generator.getRegionRenderCacheBuilder()
								.getWorldRendererByLayer(EnumWorldBlockLayer.REALISTIC_WATER),
						generator.getRenderChunk(), compiledchunk);
			}
			generator.getRenderChunk().setCompiledChunk(compiledchunk);
			generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
		}
	}
	
	public boolean updateChunks(long timeout) {
		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		if (entity == null) {
			queue.clear();
			chunkUpdatesQueued = 0;
			return false;
		}else {
			boolean flag = false;
			long millis = System.currentTimeMillis();
			List<ChunkCompileTaskGenerator> droppedUpdates = new LinkedList();
			while(!queue.isEmpty()) {
				ChunkCompileTaskGenerator generator = queue.remove(0);
				
				if(!generator.canExecuteYet()) {
					if(millis - generator.goddamnFuckingTimeout < 60000l) {
						droppedUpdates.add(generator);
					}
					continue;
				}
				
				runGenerator(generator, entity);
				flag = true;
				
				++chunkUpdatesTotal;
				
				if(timeout < System.nanoTime()) {
					break;
				}
			}
			queue.addAll(droppedUpdates);
			return flag;
		}
	}

	public boolean updateChunkLater(RenderChunk chunkRenderer) {
		final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
		boolean flag = queue.size() < 100;
		if (!flag) {
			chunkcompiletaskgenerator.finish();
		}else {
			chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
				@Override
				public void run() {
					if(queue.remove(chunkcompiletaskgenerator)) {
						++chunkUpdatesTotal;
					}
				}
			});
			queue.add(chunkcompiletaskgenerator);
			++chunkUpdatesQueued;
		}
		return flag;
	}

	public boolean updateChunkNow(RenderChunk chunkRenderer) {
		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		if (entity != null) {
			runGenerator(chunkRenderer.makeCompileTaskChunk(), entity);
			++chunkUpdatesTotalImmediate;
		}
		return true;
	}

	public void stopChunkUpdates() {
		queue.clear();
		chunkUpdatesQueued = 0;
	}

	public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
		if(isAlreadyQueued(chunkRenderer)) {
			return true;
		}
		final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
		if (chunkcompiletaskgenerator == null) {
			return true;
		}
		chunkcompiletaskgenerator.goddamnFuckingTimeout = System.currentTimeMillis();
		if(queue.size() < 100) {
			chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
				@Override
				public void run() {
					if(queue.remove(chunkcompiletaskgenerator)) {
						++chunkUpdatesTotal;
					}
				}
			});
			queue.add(chunkcompiletaskgenerator);
			++chunkUpdatesQueued;
			return true;
		}else {
			return false;
		}
	}

	public void uploadChunk(final EnumWorldBlockLayer player, final WorldRenderer chunkRenderer,
			final RenderChunk compiledChunkIn, final CompiledChunk parCompiledChunk) {
		this.uploadDisplayList(chunkRenderer,
				((ListedRenderChunk) compiledChunkIn).getDisplayList(player, parCompiledChunk), compiledChunkIn);
		chunkRenderer.setTranslation(0.0D, 0.0D, 0.0D);
	}

	private void uploadDisplayList(WorldRenderer chunkRenderer, int parInt1, RenderChunk parRenderChunk) {
		EaglercraftGPU.glNewList(parInt1, GL_COMPILE);
		GlStateManager.pushMatrix();
		this.worldVertexUploader.func_181679_a(chunkRenderer);
		GlStateManager.popMatrix();
		EaglercraftGPU.glEndList();
	}

	public boolean isAlreadyQueued(RenderChunk update) {
		for(int i = 0, l = queue.size(); i < l; ++i) {
			if(queue.get(i).getRenderChunk() == update) {
				return true;
			}
		}
		return false;
	}

	public String getDebugInfo() {
		long millis = System.currentTimeMillis();
		
		if(millis - chunkUpdatesTotalLastUpdate > 500l) {
			chunkUpdatesTotalLastUpdate = millis;
			chunkUpdatesTotalLast = chunkUpdatesTotal;
			chunkUpdatesTotalImmediateLast = chunkUpdatesTotalImmediate;
			chunkUpdatesTotalImmediate = 0;
			chunkUpdatesTotal = 0;
			chunkUpdatesQueuedLast = chunkUpdatesQueued;
			chunkUpdatesQueued -= chunkUpdatesTotalLast;
			if(chunkUpdatesQueued < 0) {
				chunkUpdatesQueued = 0;
			}
		}
		
		return "Uq: " + (chunkUpdatesTotalLast + chunkUpdatesTotalImmediateLast) + "/"
				+ (chunkUpdatesQueuedLast + chunkUpdatesTotalImmediateLast);
	}
	
}
