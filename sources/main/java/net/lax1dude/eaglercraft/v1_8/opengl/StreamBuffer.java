package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class StreamBuffer {

	public final int initialSize;
	public final int initialCount;
	public final int maxCount;

	protected StreamBufferInstance[] buffers;

	protected int currentBufferId = 0;
	protected int overflowCounter = 0;

	protected final IStreamBufferInitializer initializer;

	public static class StreamBufferInstance {

		protected IBufferArrayGL vertexArray = null;
		protected IBufferGL vertexBuffer = null;
		protected int vertexBufferSize = 0;

		public boolean bindQuad16 = false;
		public boolean bindQuad32 = false;

		public IBufferArrayGL getVertexArray() {
			return vertexArray;
		}

		public IBufferGL getVertexBuffer() {
			return vertexBuffer;
		}

	}

	public static interface IStreamBufferInitializer {
		void initialize(IBufferArrayGL vertexArray, IBufferGL vertexBuffer);
	}

	public StreamBuffer(int initialSize, int initialCount, int maxCount, IStreamBufferInitializer initializer) {
		this.buffers = new StreamBufferInstance[initialCount];
		for(int i = 0; i < this.buffers.length; ++i) {
			this.buffers[i] = new StreamBufferInstance();
		}
		this.initialSize = initialSize;
		this.initialCount = initialCount;
		this.maxCount = maxCount;
		this.initializer = initializer;
	}

	public StreamBufferInstance getBuffer(int requiredMemory) {
		StreamBufferInstance next = buffers[(currentBufferId++) % buffers.length];
		if(next.vertexBuffer == null) {
			next.vertexBuffer = _wglGenBuffers();
		}
		if(next.vertexArray == null) {
			next.vertexArray = _wglGenVertexArrays();
			initializer.initialize(next.vertexArray, next.vertexBuffer);
		}
		if(next.vertexBufferSize < requiredMemory) {
			int newSize = (requiredMemory & 0xFFFFF000) + 0x2000;
			EaglercraftGPU.bindGLArrayBuffer(next.vertexBuffer);
			_wglBufferData(GL_ARRAY_BUFFER, newSize, GL_STREAM_DRAW);
			next.vertexBufferSize = newSize;
		}
		return next;
	}

	public void optimize() {
		overflowCounter += currentBufferId - buffers.length;
		if(overflowCounter < -25) {
			int newCount = buffers.length - 1 + ((overflowCounter + 25) / 5);
			if(newCount < initialCount) {
				newCount = initialCount;
			}
			if(newCount < buffers.length) {
				StreamBufferInstance[] newArray = new StreamBufferInstance[newCount];
				for(int i = 0; i < buffers.length; ++i) {
					if(i < newArray.length) {
						newArray[i] = buffers[i];
					}else {
						if(buffers[i].vertexArray != null) {
							_wglDeleteVertexArrays(buffers[i].vertexArray);
						}
						if(buffers[i].vertexBuffer != null) {
							_wglDeleteBuffers(buffers[i].vertexBuffer);
						}
					}
				}
				buffers = newArray;
			}
			overflowCounter = 0;
		}else if(overflowCounter > 15) {
			int newCount = buffers.length + 1 + ((overflowCounter - 15) / 5);
			if(newCount > maxCount) {
				newCount = maxCount;
			}
			if(newCount > buffers.length) {
				StreamBufferInstance[] newArray = new StreamBufferInstance[newCount];
				for(int i = 0; i < newArray.length; ++i) {
					if(i < buffers.length) {
						newArray[i] = buffers[i];
					}else {
						newArray[i] = new StreamBufferInstance();
					}
				}
				buffers = newArray;
			}
			overflowCounter = 0;
		}
		currentBufferId = 0;
	}

	public void destroy() {
		for(int i = 0; i < buffers.length; ++i) {
			StreamBufferInstance next = buffers[i];
			if(next.vertexArray != null) {
				_wglDeleteVertexArrays(next.vertexArray);
			}
			if(next.vertexBuffer != null) {
				_wglDeleteBuffers(next.vertexBuffer);
			}
		}
		buffers = new StreamBufferInstance[initialCount];
		for(int i = 0; i < buffers.length; ++i) {
			buffers[i] = new StreamBufferInstance();
		}
	}

}
