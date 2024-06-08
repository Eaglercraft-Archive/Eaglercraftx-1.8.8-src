package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.MathHelper;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class DynamicLightsStateManager {

	static final DynamicLightsPipelineCompiler deferredExtPipeline = new DynamicLightsPipelineCompiler();
	static final Map<String, DynamicLightInstance> lightRenderers = new HashMap();
	static final List<DynamicLightInstance> lightRenderList = new LinkedList();
	static final Matrix4f inverseViewMatrix = new Matrix4f();
	static int inverseViewMatrixSerial = 0;
	static DynamicLightBucketLoader bucketLoader = null;
	static DynamicLightsAcceleratedEffectRenderer accelParticleRenderer = null;
	static int lastTotal = 0;
	static long renderTimeout = 5000l;
	private static long lastTick = 0l;

	public static final void enableDynamicLightsRender() {
		if(bucketLoader == null) {
			bucketLoader = new DynamicLightBucketLoader();
			bucketLoader.initialize();
			bucketLoader.bindUniformBuffer(0);
			FixedFunctionPipeline.loadExtensionPipeline(deferredExtPipeline);
		}
		if(accelParticleRenderer == null) {
			accelParticleRenderer = new DynamicLightsAcceleratedEffectRenderer();
			accelParticleRenderer.initialize();
		}
	}

	public static final void bindAcceleratedEffectRenderer(EffectRenderer renderer) {
		renderer.acceleratedParticleRenderer = accelParticleRenderer;
	}

	public static final void disableDynamicLightsRender(boolean unloadPipeline) {
		if(bucketLoader != null) {
			bucketLoader.destroy();
			bucketLoader = null;
			if(unloadPipeline) {
				FixedFunctionPipeline.loadExtensionPipeline(null);
			}
		}
		if(accelParticleRenderer != null) {
			accelParticleRenderer.destroy();
			accelParticleRenderer = null;
		}
		destroyAll();
		lightRenderList.clear();
	}

	public static final boolean isDynamicLightsRender() {
		return bucketLoader != null;
	}

	public static final boolean isInDynamicLightsPass() {
		return GlStateManager.isExtensionPipeline() && bucketLoader != null;
	}

	public static final void reportForwardRenderObjectPosition(int centerX, int centerY, int centerZ) {
		if(bucketLoader != null) {
			bucketLoader.loadLightSourceBucket(centerX, centerY, centerZ);
		}
	}

	public static final void reportForwardRenderObjectPosition2(float x, float y, float z) {
		if(bucketLoader != null) {
			float posX = (float)((x + TileEntityRendererDispatcher.staticPlayerX) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerX / 16.0) << 4));
			float posY = (float)((y + TileEntityRendererDispatcher.staticPlayerY) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerY / 16.0) << 4));
			float posZ = (float)((z + TileEntityRendererDispatcher.staticPlayerZ) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerZ / 16.0) << 4));
			bucketLoader.loadLightSourceBucket((int)posX, (int)posY, (int)posZ);
		}
	}

	public static final void renderDynamicLight(String lightName, double posX, double posY, double posZ, float radius) {
		if(bucketLoader != null) {
			DynamicLightInstance dl = lightRenderers.get(lightName);
			if(dl == null) {
				lightRenderers.put(lightName, dl = new DynamicLightInstance(lightName));
			}
			dl.updateLight(posX, posY, posZ, radius);
			lightRenderList.add(dl);
		}
	}

	public static final void clearRenderList() {
		lightRenderList.clear();
	}

	public static final void commitLightSourceBuckets(double renderPosX, double renderPosY, double renderPosZ) {
		updateTimers();
		lastTotal = lightRenderList.size();
		if(bucketLoader != null) {
			bucketLoader.clearBuckets();
			int entityChunkOriginX = MathHelper.floor_double(renderPosX / 16.0) << 4;
			int entityChunkOriginY = MathHelper.floor_double(renderPosY / 16.0) << 4;
			int entityChunkOriginZ = MathHelper.floor_double(renderPosZ / 16.0) << 4;
			Iterator<DynamicLightInstance> itr = lightRenderList.iterator();
			while(itr.hasNext()) {
				DynamicLightInstance dl = itr.next();
				float lightChunkPosX = (float)(dl.posX - entityChunkOriginX);
				float lightChunkPosY = (float)(dl.posY - entityChunkOriginY);
				float lightChunkPosZ = (float)(dl.posZ - entityChunkOriginZ);
				bucketLoader.bucketLightSource(lightChunkPosX, lightChunkPosY, lightChunkPosZ, dl);
			}
			bucketLoader.setRenderPos(renderPosX, renderPosY, renderPosZ);
			bucketLoader.truncateOverflowingBuffers();
		}
		lightRenderList.clear();
	}

	public static final void setupInverseViewMatrix() {
		Matrix4f.invert(GlStateManager.getModelViewReference(), inverseViewMatrix);
		inverseViewMatrixSerial = GlStateManager.getModelViewSerial();
	}

	private static final void updateTimers() {
		long millis = System.currentTimeMillis();
		if(millis - lastTick > 1000l) {
			lastTick = millis;
			Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
			while(itr.hasNext()) {
				DynamicLightInstance dl = itr.next();
				if(millis - dl.lastCacheHit > renderTimeout) {
					dl.destroy();
					itr.remove();
				}
			}
		}
	}

	public static final void destroyAll() {
		Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
		while(itr.hasNext()) {
			itr.next().destroy();
		}
		lightRenderers.clear();
	}

	public static String getF3String() {
		return "DynamicLightsTotal: " + lastTotal;
	}

}
