/*
 * Copyright (c) 2023-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IVertexArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

public class StreamBuffer {

	public static final int poolSize = 4;

	protected static final PoolInstance[] pool = new PoolInstance[poolSize];
	protected static int poolBufferID = 0;

	static {
		for(int i = 0; i < poolSize; ++i) {
			pool[i] = new PoolInstance();
		}
	}

	protected static class PoolInstance {
		
		protected IBufferGL vertexBuffer = null;
		protected int vertexBufferSize = 0;
		
	}

	private static void resizeInstance(PoolInstance instance, int requiredMemory) {
		IBufferGL buffer = instance.vertexBuffer;
		if (buffer == null) {
			buffer = _wglGenBuffers();
			instance.vertexBuffer = buffer;
		}
		int newSize = instance.vertexBufferSize;
		if (newSize < requiredMemory) {
			newSize = (requiredMemory + 0xFFFF) & 0xFFFF0000;
			instance.vertexBufferSize = newSize;
		}
		EaglercraftGPU.bindGLArrayBuffer(buffer);
		_wglBufferData(GL_ARRAY_BUFFER, newSize, GL_STREAM_DRAW);
	}

	protected StreamBufferInstance[] buffers;

	protected final IStreamBufferInitializer initializer;

	public static class StreamBufferInstance {

		protected PoolInstance poolInstance = null;
		protected IVertexArrayGL vertexArray = null;

		public boolean bindQuad16 = false;
		public boolean bindQuad32 = false;

		public IVertexArrayGL getVertexArray() {
			return vertexArray;
		}

		public IBufferGL getVertexBuffer() {
			return poolInstance.vertexBuffer;
		}

	}

	public static interface IStreamBufferInitializer {
		void initialize(IVertexArrayGL vertexArray, IBufferGL vertexBuffer);
	}

	public StreamBuffer(IStreamBufferInitializer initializer) {
		this.buffers = new StreamBufferInstance[poolSize];
		for(int i = 0; i < this.buffers.length; ++i) {
			StreamBufferInstance j = new StreamBufferInstance();
			j.poolInstance = pool[i];
			this.buffers[i] = j;
		}
		this.initializer = initializer;
	}

	public StreamBufferInstance getBuffer(int requiredMemory) {
		StreamBufferInstance next = buffers[poolBufferID++ % buffers.length];
		resizeInstance(next.poolInstance, requiredMemory);
		if(next.vertexArray == null) {
			next.vertexArray = EaglercraftGPU.createGLVertexArray();
			initializer.initialize(next.vertexArray, next.poolInstance.vertexBuffer);
		}
		return next;
	}

	public void destroy() {
		for(int i = 0; i < buffers.length; ++i) {
			StreamBufferInstance next = buffers[i];
			if(next.vertexArray != null) {
				EaglercraftGPU.destroyGLVertexArray(next.vertexArray);
			}
		}
	}

	public static void destroyPool() {
		for(int i = 0; i < pool.length; ++i) {
			if(pool[i].vertexBuffer != null) {
				_wglDeleteBuffers(pool[i].vertexBuffer);
				pool[i].vertexBuffer = null;
			}
		}
	}

}