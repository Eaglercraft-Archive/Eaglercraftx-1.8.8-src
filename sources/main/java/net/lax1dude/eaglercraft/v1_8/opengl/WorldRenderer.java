package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import java.util.BitSet;
import java.util.function.IntBinaryOperator;

import com.carrotsearch.hppc.sorting.QuickSort;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.MathHelper;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class WorldRenderer {
	
	private static final Logger logger = LogManager.getLogger("WorldRenderer");
	
	private boolean needsUpdate;
	private int drawMode;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private boolean isDrawing;
	
	private VertexFormat vertexFormat;
	
	private int vertexCount;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	
	private boolean hasBeenFreed = false;

	public WorldRenderer(int bufferSizeIn) {
		this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn << 2);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
	}
	
	public void free() {
		if(!hasBeenFreed) {
			hasBeenFreed = true;
			EagRuntime.freeByteBuffer(byteBuffer);
		}
	}
	
	public void finalize() {
		free();
	}

	private void grow(int parInt1) {
		int pos = (this.vertexCount * this.vertexFormat.attribStride) >> 2;
		int i = this.byteBuffer.capacity() >> 2;
		if (parInt1 > (i - pos)) {
			int k = (((pos + parInt1 + (parInt1 >> 1)) >> 16) + 1) << 16;
			logger.warn("Needed to grow BufferBuilder buffer: Old size " + (i << 2) +
					" bytes, new size " + (k << 2) + " bytes.");
			ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(k << 2);
			this.byteBuffer.position(0);
			bytebuffer.put(this.byteBuffer);
			bytebuffer.rewind();
			EagRuntime.freeByteBuffer(this.byteBuffer);
			this.byteBuffer = bytebuffer;
			this.intBuffer = this.byteBuffer.asIntBuffer();
			this.floatBuffer = this.byteBuffer.asFloatBuffer();
		}
	}

	private float[] sortArrayCacheA = null;
	private int[] sortArrayCacheB = null;
	private BitSet sortBitSetCache = null;
	private final IntBinaryOperator sortArrayCacheLambda = this::sortFunction_func_181674_a;
	private final IntBinaryOperator swapArrayCacheLambda = this::swapFunction_func_181674_a;

	protected int sortFunction_func_181674_a(int integer, int integer1) {
		return Float.compare(sortArrayCacheA[sortArrayCacheB[integer1]], sortArrayCacheA[sortArrayCacheB[integer]]);
	}

	protected int swapFunction_func_181674_a(int i, int j) {
		int swap = sortArrayCacheB[i];
		sortArrayCacheB[i] = sortArrayCacheB[j];
		sortArrayCacheB[j] = swap;
		return 0;
	}

	/**
	 * MOST LIKELY USED TO SORT QUADS BACK TO FRONT
	 */
	public void func_181674_a(float parFloat1, float parFloat2, float parFloat3) {
		int i = this.vertexCount / 4;
		if(i == 0) {
			return;
		}

		float[] afloat = sortArrayCacheA;
		if(afloat == null || afloat.length < i) {
			afloat = new float[i];
			sortArrayCacheA = afloat;
		}

		for (int j = 0; j < i; ++j) {
			afloat[j] = func_181665_a(this.floatBuffer, (float) ((double) parFloat1 + this.xOffset),
					(float) ((double) parFloat2 + this.yOffset), (float) ((double) parFloat3 + this.zOffset),
					this.vertexFormat.attribStride >> 2, j * this.vertexFormat.attribStride);
		}

		int[] ainteger = sortArrayCacheB;
		if(ainteger == null || ainteger.length < i) {
			ainteger = new int[i];
			sortArrayCacheB = ainteger;
		}

		for (int k = 0; k < i; ++k) {
			ainteger[k] = k;
		}

		QuickSort.sort(0, i, sortArrayCacheLambda, swapArrayCacheLambda);

		BitSet bitset = sortBitSetCache;
		if(bitset == null) {
			bitset = new BitSet();
			sortBitSetCache = bitset;
		}else {
			bitset.clear();
		}

		int l = this.vertexFormat.attribStride;
		int[] aint = new int[l];

		for (int l1 = 0; (l1 = bitset.nextClearBit(l1)) < i; ++l1) {
			int i1 = ainteger[l1];
			if (i1 != l1) {
				this.intBuffer.limit(i1 * l + l);
				this.intBuffer.position(i1 * l);
				this.intBuffer.get(aint);
				int j1 = i1;

				for (int k1 = ainteger[i1]; j1 != l1; k1 = ainteger[k1]) {
					this.intBuffer.limit(k1 * l + l);
					this.intBuffer.position(k1 * l);
					IntBuffer intbuffer = this.intBuffer.duplicate();
					this.intBuffer.limit(j1 * l + l);
					this.intBuffer.position(j1 * l);
					this.intBuffer.put(intbuffer);
					bitset.set(j1);
					j1 = k1;
				}

				this.intBuffer.limit(l1 * l + l);
				this.intBuffer.position(l1 * l);
				this.intBuffer.put(aint);
			}

			bitset.set(l1);
			this.intBuffer.clear();
		}

	}

	public WorldRenderer.State func_181672_a() {
		VertexFormat fmt = this.vertexFormat;
		int i = (fmt.attribStride >> 2) * vertexCount;
		IntBuffer buf = EagRuntime.allocateIntBuffer(i);
		this.intBuffer.position(0);
		this.intBuffer.limit(i);
		buf.put(this.intBuffer);
		buf.flip();
		return new WorldRenderer.State(buf, fmt);
	}

	private static float func_181665_a(FloatBuffer parFloatBuffer, float parFloat1, float parFloat2, float parFloat3,
			int parInt1, int parInt2) {
		float f = parFloatBuffer.get(parInt2 + parInt1 * 0 + 0);
		float f1 = parFloatBuffer.get(parInt2 + parInt1 * 0 + 1);
		float f2 = parFloatBuffer.get(parInt2 + parInt1 * 0 + 2);
		float f3 = parFloatBuffer.get(parInt2 + parInt1 * 1 + 0);
		float f4 = parFloatBuffer.get(parInt2 + parInt1 * 1 + 1);
		float f5 = parFloatBuffer.get(parInt2 + parInt1 * 1 + 2);
		float f6 = parFloatBuffer.get(parInt2 + parInt1 * 2 + 0);
		float f7 = parFloatBuffer.get(parInt2 + parInt1 * 2 + 1);
		float f8 = parFloatBuffer.get(parInt2 + parInt1 * 2 + 2);
		float f9 = parFloatBuffer.get(parInt2 + parInt1 * 3 + 0);
		float f10 = parFloatBuffer.get(parInt2 + parInt1 * 3 + 1);
		float f11 = parFloatBuffer.get(parInt2 + parInt1 * 3 + 2);
		float f12 = (f + f3 + f6 + f9) * 0.25F - parFloat1;
		float f13 = (f1 + f4 + f7 + f10) * 0.25F - parFloat2;
		float f14 = (f2 + f5 + f8 + f11) * 0.25F - parFloat3;
		return f12 * f12 + f13 * f13 + f14 * f14;
	}

	public void setVertexState(WorldRenderer.State state) {
		IntBuffer buf = state.getRawBuffer();
		int pp = buf.position();
		this.grow(buf.remaining());
		int p = intBuffer.position();
		this.intBuffer.position(0);
		this.intBuffer.put(buf);
		buf.position(pp);
		this.intBuffer.position(p);
		this.vertexCount = state.getVertexCount();
		this.vertexFormat = state.getVertexFormat();
	}

	public void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.intBuffer.clear();
	}

	public void begin(int parInt1, VertexFormat parVertexFormat) {
		if (this.isDrawing) {
			throw new IllegalStateException("WorldRenderer already building you eagler!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = parInt1;
			this.vertexFormat = parVertexFormat;
			this.needsUpdate = false;
			this.byteBuffer.limit(this.byteBuffer.capacity());
		}
	}

	public WorldRenderer tex(double parDouble1, double parDouble2) {
		VertexFormat fmt = this.vertexFormat;
		int i = this.vertexCount * fmt.attribStride + fmt.attribTextureOffset;
		this.byteBuffer.putFloat(i, (float) parDouble1);
		this.byteBuffer.putFloat(i + 4, (float) parDouble2);
		return this;
	}

	public WorldRenderer lightmap(int parInt1, int parInt2) {
		VertexFormat fmt = this.vertexFormat;
		int i = this.vertexCount * fmt.attribStride + fmt.attribLightmapOffset;
		this.byteBuffer.putShort(i, (short) parInt2);
		this.byteBuffer.putShort(i + 2, (short) parInt1);
		return this;
	}

	/**
	 * update lightmap color of the last 4 verticies, used in AO calculation
	 */
	public void putBrightness4(int parInt1, int parInt2, int parInt3, int parInt4) {
		VertexFormat fmt = this.vertexFormat;
		int j = fmt.attribStride >> 2;
		int i = (this.vertexCount - 4) * j + (fmt.attribLightmapOffset >> 2);
		this.intBuffer.put(i, parInt1);
		this.intBuffer.put(i + j, parInt2);
		this.intBuffer.put(i + j * 2, parInt3);
		this.intBuffer.put(i + j * 3, parInt4);
	}

	/**
	 * translates the last 4 verticies to the given position plus current offset
	 */
	public void putPosition(double x, double y, double z) {
		int i = this.vertexFormat.attribStride;
		int j = (this.vertexCount - 4) * i;

		for (int k = 0; k < 4; ++k) {
			int l = j + k * i;
			int i1 = l + 4;
			int j1 = i1 + 4;
			this.byteBuffer.putFloat(l, (float) (x + this.xOffset) + this.byteBuffer.getFloat(l));
			this.byteBuffer.putFloat(i1, (float) (y + this.yOffset) + this.byteBuffer.getFloat(i1));
			this.byteBuffer.putFloat(j1, (float) (z + this.zOffset) + this.byteBuffer.getFloat(j1));
		}

	}

	/**
	 * gets the color index of a vertex parInt1 indicies before the current vertex
	 */
	private int getColorIndex(int parInt1) {
		return ((this.vertexCount - parInt1) * this.vertexFormat.attribStride +
				this.vertexFormat.attribColorOffset) >> 2;
	}

	/**
	 * multiplies the color of a vertex parInt1 indicies before the current vertex,
	 * skips if !this.needsUpdate
	 */
	public void putColorMultiplier(float red, float green, float blue, int parInt1) {
		int i = this.getColorIndex(parInt1);
		int j = -1;
		if (!this.needsUpdate) {
			j = this.intBuffer.get(i);
			int k = (int) ((float) (j & 255) * red);
			int l = (int) ((float) (j >>> 8 & 255) * green);
			int i1 = (int) ((float) (j >>> 16 & 255) * blue);
			j = j & -16777216;
			j = j | i1 << 16 | l << 8 | k;
		}
		this.intBuffer.put(i, j);
	}
	
	/**
	 * sets color multiplier of a vertex parInt1 indicies before the current vertex
	 */
	private void putColor(int argb, int parInt2) {
		int i = this.getColorIndex(parInt2);
		int j = argb >>> 16 & 255;
		int k = argb >>> 8 & 255;
		int l = argb & 255;
		int i1 = argb >>> 24 & 255;
		this.putColorRGBA(i, j, k, l, i1);
	}

	/**
	 * sets color multiplier of a vertex parInt1 indicies before the current vertex
	 */
	public void putColorRGB_F(float red, float green, float blue, int parInt1) {
		int i = this.getColorIndex(parInt1);
		int j = MathHelper.clamp_int((int) (red * 255.0F), 0, 255);
		int k = MathHelper.clamp_int((int) (green * 255.0F), 0, 255);
		int l = MathHelper.clamp_int((int) (blue * 255.0F), 0, 255);
		this.putColorRGBA(i, j, k, l, 255);
	}

	/**
	 * sets color multiplier of a vertex parInt1 indicies before the current vertex
	 */
	private void putColorRGBA(int index, int red, int parInt3, int parInt4, int parInt5) {
		this.intBuffer.put(index, parInt5 << 24 | parInt4 << 16 | parInt3 << 8 | red);
	}

	/**
	 * Marks the current renderer data as dirty (makes it skip certain calls)
	 */
	public void markDirty() {
		this.needsUpdate = true;
	}

	/**
	 * sets color of current vertex
	 */
	public WorldRenderer color(float parFloat1, float parFloat2, float parFloat3, float parFloat4) {
		return this.color((int) (parFloat1 * 255.0F), (int) (parFloat2 * 255.0F), (int) (parFloat3 * 255.0F),
				(int) (parFloat4 * 255.0F));
	}

	/**
	 * sets color of current vertex
	 */
	public WorldRenderer color(int parInt1, int parInt2, int parInt3, int parInt4) {
		if (this.needsUpdate) {
			return this;
		} else {
			VertexFormat fmt = this.vertexFormat;
			int i = this.vertexCount * fmt.attribStride + fmt.attribColorOffset;
			this.byteBuffer.putInt(i, parInt1 | parInt2 << 8 | parInt3 << 16 | parInt4 << 24);
			return this;
		}
	}

	/**
	 * adds cached vertex data to the buffer
	 */
	public void addVertexData(int[] vertexData) {
		this.grow(vertexData.length);
		int p = this.intBuffer.position();
		this.intBuffer.position((this.vertexCount * this.vertexFormat.attribStride) >> 2);
		this.intBuffer.put(vertexData);
		this.intBuffer.position(p);
		this.vertexCount += vertexData.length / (this.vertexFormat.attribStride >> 2); 
	}

	/**
	 * increases the index of the current vertex by 1
	 */
	public void endVertex() {
		++this.vertexCount;
		this.grow(this.vertexFormat.attribStride >> 2);
	}

	/**
	 * sets position of current vertex
	 */
	public WorldRenderer pos(double parDouble1, double parDouble2, double parDouble3) {
		int i = this.vertexCount * this.vertexFormat.attribStride;
		this.byteBuffer.putFloat(i, (float) (parDouble1 + this.xOffset));
		this.byteBuffer.putFloat(i + 4, (float) (parDouble2 + this.yOffset));
		this.byteBuffer.putFloat(i + 8, (float) (parDouble3 + this.zOffset));
		return this;
	}

	/**
	 * sets the normal of the previous 4 verticies in the buffer
	 */
	public void putNormal(float x, float y, float z) {
		int i = (byte) ((int) (x * 127.0F)) & 255;
		int j = (byte) ((int) (y * 127.0F)) & 255;
		int k = (byte) ((int) (z * 127.0F)) & 255;
		int l = i | j << 8 | k << 16;
		VertexFormat fmt = this.vertexFormat;
		int i1 = fmt.attribStride;
		int j1 = (this.vertexCount - 4) * i1 + fmt.attribNormalOffset;
		this.byteBuffer.putInt(j1, l);
		this.byteBuffer.putInt(j1 + i1, l);
		this.byteBuffer.putInt(j1 + i1 * 2, l);
		this.byteBuffer.putInt(j1 + i1 * 3, l);
	}

	public void putNormal(float x, float y, float z, int id) {
		int i = (byte) ((int) (x * 127.0F)) & 255;
		int j = (byte) ((int) (y * 127.0F)) & 255;
		int k = (byte) ((int) (z * 127.0F)) & 255;
		int l = i | j << 8 | k << 16 | ((byte)id) << 24;
		VertexFormat fmt = this.vertexFormat;
		int i1 = fmt.attribStride;
		int j1 = (this.vertexCount - 4) * i1 + fmt.attribNormalOffset;
		this.byteBuffer.putInt(j1, l);
		this.byteBuffer.putInt(j1 + i1, l);
		this.byteBuffer.putInt(j1 + i1 * 2, l);
		this.byteBuffer.putInt(j1 + i1 * 3, l);
	}

	/**
	 * set normal of current vertex
	 */
	public WorldRenderer normal(float parFloat1, float parFloat2, float parFloat3) { //TODO: crash with particles
		VertexFormat fmt = this.vertexFormat;
		int i = this.vertexCount * fmt.attribStride + fmt.attribNormalOffset;
		this.byteBuffer.put(i, (byte) ((int) parFloat1 * 127 & 255));
		this.byteBuffer.put(i + 1, (byte) ((int) parFloat2 * 127 & 255));
		this.byteBuffer.put(i + 2, (byte) ((int) parFloat3 * 127 & 255));
		return this;
	}

	private final Vector3f tmpVec1 = new Vector3f();
	private final Vector3f tmpVec2 = new Vector3f();
	private final Vector3f tmpVec3 = new Vector3f();
	private final Vector3f tmpVec4 = new Vector3f();
	private final Vector3f tmpVec5 = new Vector3f();
	private final Vector3f tmpVec6 = new Vector3f();

	public void genNormals(boolean b, int vertId) {
		VertexFormat fmt = this.vertexFormat;
		int i1 = fmt.attribStride;
		int j1 = (this.vertexCount - 4) * i1;
		tmpVec1.x = this.byteBuffer.getFloat(j1);
		tmpVec1.y = this.byteBuffer.getFloat(j1 + 4);
		tmpVec1.z = this.byteBuffer.getFloat(j1 + 8);
		j1 += i1;
		tmpVec2.x = this.byteBuffer.getFloat(j1);
		tmpVec2.y = this.byteBuffer.getFloat(j1 + 4);
		tmpVec2.z = this.byteBuffer.getFloat(j1 + 8);
		j1 += i1 * 2;
		tmpVec3.x = this.byteBuffer.getFloat(j1);
		tmpVec3.y = this.byteBuffer.getFloat(j1 + 4);
		tmpVec3.z = this.byteBuffer.getFloat(j1 + 8);
		Vector3f.sub(tmpVec1, tmpVec2, tmpVec4);
		Vector3f.sub(tmpVec3, tmpVec2, tmpVec5);
		Vector3f.cross(tmpVec5, tmpVec4, tmpVec6);
		float f = (float) Math
				.sqrt((double) (tmpVec6.x * tmpVec6.x + tmpVec6.y * tmpVec6.y + tmpVec6.z * tmpVec6.z));
		tmpVec6.x /= f;
		tmpVec6.y /= f;
		tmpVec6.z /= f;
		int i = (byte) ((int) (tmpVec6.x * 127.0F)) & 255;
		int j = (byte) ((int) (tmpVec6.y * 127.0F)) & 255;
		int k = (byte) ((int) (tmpVec6.z * 127.0F)) & 255;
		int l = i | j << 8 | k << 16 | vertId << 24;
		int jj1 = (this.vertexCount - 4) * i1 + fmt.attribNormalOffset;
		this.byteBuffer.putInt(jj1, l);
		this.byteBuffer.putInt(jj1 + i1, l);
		if(!b) {
			this.byteBuffer.putInt(jj1 + i1 * 2, l);
		}
		this.byteBuffer.putInt(jj1 + i1 * 3, l);
		if(b) {
			j1 = (this.vertexCount - 2) * i1;
			tmpVec1.x = this.byteBuffer.getFloat(j1);
			tmpVec1.y = this.byteBuffer.getFloat(j1 + 4);
			tmpVec1.z = this.byteBuffer.getFloat(j1 + 8);
			Vector3f.sub(tmpVec2, tmpVec1, tmpVec4);
			Vector3f.sub(tmpVec3, tmpVec1, tmpVec5);
			Vector3f.cross(tmpVec5, tmpVec4, tmpVec6);
			f = (float) Math.sqrt((double) (tmpVec6.x * tmpVec6.x + tmpVec6.y * tmpVec6.y + tmpVec6.z * tmpVec6.z));
			tmpVec6.x /= f;
			tmpVec6.y /= f;
			tmpVec6.z /= f;
			i = (byte) ((int) (tmpVec6.x * 127.0F)) & 255;
			j = (byte) ((int) (tmpVec6.y * 127.0F)) & 255;
			k = (byte) ((int) (tmpVec6.z * 127.0F)) & 255;
			l = i | j << 8 | k << 16 | vertId << 24;
			this.byteBuffer.putInt(jj1 + i1 * 2, l);
		}
	}

	/**
	 * sets translation applied to all positions set by functions
	 */
	public void setTranslation(double x, double y, double z) {
		this.xOffset = x;
		this.yOffset = y;
		this.zOffset = z;
	}

	public void finishDrawing() {
		if (!this.isDrawing) {
			throw new IllegalStateException("Not building!");
		} else {
			this.isDrawing = false;
			this.byteBuffer.position(0);
			this.byteBuffer.limit(this.vertexCount * this.vertexFormat.attribStride);
		}
	}

	public ByteBuffer getByteBuffer() {
		return this.byteBuffer;
	}

	public VertexFormat getVertexFormat() {
		return this.vertexFormat;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public int getDrawMode() {
		return this.drawMode;
	}

	public void putColor4(int argb) {
		for (int i = 0; i < 4; ++i) {
			this.putColor(argb, i + 1);
		}

	}

	public void putColorRGB_F4(float red, float green, float blue) {
		for (int i = 0; i < 4; ++i) {
			this.putColorRGB_F(red, green, blue, i + 1);
		}

	}

	public class State {
		private final IntBuffer stateRawBuffer;
		private final VertexFormat stateVertexFormat;
		private int refCount = 1;

		public State(IntBuffer parArrayOfInt, VertexFormat parVertexFormat) {
			this.stateRawBuffer = parArrayOfInt;
			this.stateVertexFormat = parVertexFormat;
		}

		public IntBuffer getRawBuffer() {
			return this.stateRawBuffer;
		}

		public int getVertexCount() {
			return this.stateRawBuffer.remaining() / (this.stateVertexFormat.attribStride >> 2);
		}

		public VertexFormat getVertexFormat() {
			return this.stateVertexFormat;
		}

		public void retain() {
			++refCount;
		}

		public void release() {
			if(--refCount == 0) {
				EagRuntime.freeIntBuffer(stateRawBuffer);
			}
			if(refCount < 0) {
				logger.error("WorldRenderer.State released multiple times");
			}
		}
	}
}