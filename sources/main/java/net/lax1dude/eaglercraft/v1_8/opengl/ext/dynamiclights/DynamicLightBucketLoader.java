/*
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.util.Comparator;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ArrayListSerial;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ListSerial;
import net.minecraft.util.MathHelper;

public class DynamicLightBucketLoader {

	public IBufferGL buffer_chunkLightingData;
	public IBufferGL buffer_chunkLightingDataZero;
	private ByteBuffer chunkLightingDataCopyBuffer;
	public ListSerial<DynamicLightInstance> currentBoundLightSourceBucket;

	public final ListSerial<DynamicLightInstance>[] lightSourceBuckets;
	private final int[] lightSourceBucketsSerials;
	private final int[] lightSourceRenderPosSerials;
	public ListSerial<DynamicLightInstance> currentLightSourceBucket;
	private int currentLightSourceBucketId = -1;
	private int lightingBufferSliceLength = -1;

	public static final int MAX_LIGHTS_PER_CHUNK = 12;
	public static final int LIGHTING_BUFFER_LENGTH = 16 * MAX_LIGHTS_PER_CHUNK + 16;

	private final int lightSourceBucketsWidth;
	private final int lightSourceBucketsHeight;

	private double currentRenderX = 0.0;
	private double currentRenderY = 0.0;
	private double currentRenderZ = 0.0;
	private int currentRenderPosSerial = 0;

	public DynamicLightBucketLoader() {
		this.lightSourceBucketsWidth = 5;
		this.lightSourceBucketsHeight = 3;
		int cnt = 5 * 3 * 5;
		this.lightSourceBuckets = new ListSerial[cnt];
		this.lightSourceBucketsSerials = new int[cnt];
		this.lightSourceRenderPosSerials = new int[cnt];
	}

	public void initialize() {
		destroy();
		
		int alignment = EaglercraftGPU.getUniformBufferOffsetAlignment();
		lightingBufferSliceLength = MathHelper.ceiling_float_int((float)LIGHTING_BUFFER_LENGTH / (float)alignment) * alignment;
		
		chunkLightingDataCopyBuffer = EagRuntime.allocateByteBuffer(LIGHTING_BUFFER_LENGTH);
		for(int i = 0; i < LIGHTING_BUFFER_LENGTH; i += 4) {
			chunkLightingDataCopyBuffer.putInt(0);
		}
		chunkLightingDataCopyBuffer.flip();
		
		buffer_chunkLightingData = _wglGenBuffers();
		EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
		int cnt = lightSourceBucketsWidth * lightSourceBucketsHeight * lightSourceBucketsWidth;
		_wglBufferData(_GL_UNIFORM_BUFFER, cnt * lightingBufferSliceLength, GL_DYNAMIC_DRAW);
		
		buffer_chunkLightingDataZero = _wglGenBuffers();
		EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingDataZero);
		_wglBufferData(_GL_UNIFORM_BUFFER, chunkLightingDataCopyBuffer, GL_STATIC_DRAW);
		
		for(int i = 0; i < this.lightSourceBuckets.length; ++i) {
			this.lightSourceBuckets[i] = new ArrayListSerial<>(16);
			this.lightSourceBucketsSerials[i] = -1;
			this.lightSourceRenderPosSerials[i] = -1;
		}
	}

	public void clearBuckets() {
		for(int i = 0; i < this.lightSourceBuckets.length; ++i) {
			this.lightSourceBuckets[i].clear();
		}
	}

	public void bindLightSourceBucket(int relativeBlockX, int relativeBlockY, int relativeBlockZ, int uboIndex) {
		int hw = lightSourceBucketsWidth / 2;
		int hh = lightSourceBucketsHeight / 2;
		int bucketX = (relativeBlockX >> 4) + hw;
		int bucketY = (relativeBlockY >> 4) + hh;
		int bucketZ = (relativeBlockZ >> 4) + hw;
		if(bucketX >= 0 && bucketY >= 0 && bucketZ >= 0 && bucketX < lightSourceBucketsWidth
				&& bucketY < lightSourceBucketsHeight && bucketZ < lightSourceBucketsWidth) {
			currentLightSourceBucketId = bucketY * lightSourceBucketsWidth * lightSourceBucketsWidth
					+ bucketZ * lightSourceBucketsWidth + bucketX;
			currentLightSourceBucket = lightSourceBuckets[currentLightSourceBucketId];
			int ser = currentLightSourceBucket.getEaglerSerial();
			int max = currentLightSourceBucket.size();
			if(max > 0) {
				EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
				int offset = currentLightSourceBucketId * lightingBufferSliceLength;
				if (lightSourceBucketsSerials[currentLightSourceBucketId] != ser
						|| lightSourceRenderPosSerials[currentLightSourceBucketId] != currentRenderPosSerial) {
					lightSourceBucketsSerials[currentLightSourceBucketId] = ser;
					lightSourceRenderPosSerials[currentLightSourceBucketId] = currentRenderPosSerial;
					if(max > MAX_LIGHTS_PER_CHUNK) {
						max = MAX_LIGHTS_PER_CHUNK;
					}
					chunkLightingDataCopyBuffer.clear();
					chunkLightingDataCopyBuffer.putInt(max);
					chunkLightingDataCopyBuffer.putInt(0); //padding
					chunkLightingDataCopyBuffer.putInt(0); //padding
					chunkLightingDataCopyBuffer.putInt(0); //padding
					for(int i = 0; i < max; ++i) {
						DynamicLightInstance dl = currentLightSourceBucket.get(i);
						chunkLightingDataCopyBuffer.putFloat((float)(dl.posX - currentRenderX));
						chunkLightingDataCopyBuffer.putFloat((float)(dl.posY - currentRenderY));
						chunkLightingDataCopyBuffer.putFloat((float)(dl.posZ - currentRenderZ));
						chunkLightingDataCopyBuffer.putFloat(dl.radius);
					}
					chunkLightingDataCopyBuffer.flip();
					_wglBufferSubData(_GL_UNIFORM_BUFFER, offset, chunkLightingDataCopyBuffer);
				}
				EaglercraftGPU.bindUniformBufferRange(uboIndex, buffer_chunkLightingData, offset, LIGHTING_BUFFER_LENGTH);
			}else {
				EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingDataZero);
				EaglercraftGPU.bindUniformBufferRange(uboIndex, buffer_chunkLightingDataZero, 0, LIGHTING_BUFFER_LENGTH);
			}
		}else {
			currentLightSourceBucketId = -1;
			currentLightSourceBucket = null;
			EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingDataZero);
			EaglercraftGPU.bindUniformBufferRange(uboIndex, buffer_chunkLightingDataZero, 0, LIGHTING_BUFFER_LENGTH);
		}
	}

	public ListSerial<DynamicLightInstance> getLightSourceBucketRelativeChunkCoords(int cx, int cy, int cz) {
		int hw = lightSourceBucketsWidth / 2;
		int hh = lightSourceBucketsHeight / 2;
		cx += hw;
		cy += hh;
		cz += hw;
		if(cx < 0 || cx >= lightSourceBucketsWidth || cy < 0 || cy >= lightSourceBucketsHeight || cz < 0
				|| cz >= lightSourceBucketsWidth) {
			return null;
		}else {
			return lightSourceBuckets[cy * lightSourceBucketsWidth * lightSourceBucketsWidth
					+ cz * lightSourceBucketsWidth + cx];
		}
	}

	public void addLightSourceToBucket(int cx, int cy, int cz, DynamicLightInstance dl) {
		ListSerial<DynamicLightInstance> lst = getLightSourceBucketRelativeChunkCoords(cx, cy, cz);
		if(lst != null) {
			lst.add(dl);
		}
	}

	public void bucketLightSource(float x, float y, float z, DynamicLightInstance dl) {
		int bucketX = MathHelper.floor_float(x / 16.0f);
		int bucketY = MathHelper.floor_float(y / 16.0f);
		int bucketZ = MathHelper.floor_float(z / 16.0f);
		addLightSourceToBucket(bucketX, bucketY, bucketZ, dl);
		int minX = bucketX, maxX = bucketX;
		int minY = bucketY, maxY = bucketY;
		int minZ = bucketZ, maxZ = bucketZ;
		float lightLocalX = x - (bucketX << 4);
		float lightLocalY = y - (bucketY << 4);
		float lightLocalZ = z - (bucketZ << 4);
		float radius = dl.radius;
		boolean outOfBounds = false;
		if(lightLocalX - radius < 0.0f) {
			minX -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX - 1, bucketY, bucketZ, dl);
		}
		if(lightLocalY - radius < 0.0f) {
			minY -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY - 1, bucketZ, dl);
		}
		if(lightLocalZ - radius < 0.0f) {
			minZ -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY, bucketZ - 1, dl);
		}
		if(lightLocalX + radius >= 16.0f) {
			maxX += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX + 1, bucketY, bucketZ, dl);
		}
		if(lightLocalY + radius >= 16.0f) {
			maxY += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY + 1, bucketZ, dl);
		}
		if(lightLocalZ + radius >= 16.0f) {
			maxZ += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY, bucketZ + 1, dl);
		}
		if(!outOfBounds) {
			return;
		}
		radius *= radius;
		for(int yy = minY; yy <= maxY; ++yy) {
			for(int zz = minZ; zz <= maxZ; ++zz) {
				for(int xx = minX; xx <= maxX; ++xx) {
					if((xx == bucketX ? 1 : 0) + (yy == bucketY ? 1 : 0) + (zz == bucketZ ? 1 : 0) > 1) {
						continue;
					}
					List<DynamicLightInstance> lst = getLightSourceBucketRelativeChunkCoords(xx, yy, zz);
					if(lst != null) {
						int bucketBoundsX = xx << 4;
						int bucketBoundsY = yy << 4;
						int bucketBoundsZ = zz << 4;
						if (EaglerDeferredPipeline.testAabSphere(bucketBoundsX, bucketBoundsY, bucketBoundsZ,
								bucketBoundsX + 16, bucketBoundsY + 16, bucketBoundsZ + 16, x, y, z, radius)) {
							lst.add(dl);
						}
					}
				}
			}
		}
	}

	public void truncateOverflowingBuffers() {
		for(int i = 0; i < lightSourceBuckets.length; ++i) {
			List<DynamicLightInstance> lst = lightSourceBuckets[i];
			int k = lst.size();
			if(k > MAX_LIGHTS_PER_CHUNK) {
				lst.sort(comparatorLightRadius);
				for(int l = MAX_LIGHTS_PER_CHUNK - 1; l >= MAX_LIGHTS_PER_CHUNK; --l) {
					lst.remove(l);
				}
			}
		}
	}

	private static final Comparator<DynamicLightInstance> comparatorLightRadius = (l1, l2) -> {
		return l1.radius < l2.radius ? 1 : -1;
	};

	public void setRenderPos(double currentRenderX, double currentRenderY, double currentRenderZ) {
		if (this.currentRenderX != currentRenderX || this.currentRenderY != currentRenderY
				|| this.currentRenderZ != currentRenderZ || this.currentRenderPosSerial == 0) {
			this.currentRenderX = currentRenderX;
			this.currentRenderY = currentRenderY;
			this.currentRenderZ = currentRenderZ;
			++this.currentRenderPosSerial;
		}
	}

	public void destroy() {
		if(chunkLightingDataCopyBuffer != null) {
			EagRuntime.freeByteBuffer(chunkLightingDataCopyBuffer);
			chunkLightingDataCopyBuffer = null;
		}
		if(buffer_chunkLightingData != null) {
			_wglDeleteBuffers(buffer_chunkLightingData);
			buffer_chunkLightingData = null;
		}
		if(buffer_chunkLightingDataZero != null) {
			_wglDeleteBuffers(buffer_chunkLightingDataZero);
			buffer_chunkLightingDataZero = null;
		}
		for(int i = 0; i < lightSourceBuckets.length; ++i) {
			lightSourceBuckets[i] = null;
			lightSourceBucketsSerials[i] = -1;
			lightSourceRenderPosSerials[i] = -1;
		}
		currentLightSourceBucket = null;
		currentLightSourceBucketId = -1;
	}
}