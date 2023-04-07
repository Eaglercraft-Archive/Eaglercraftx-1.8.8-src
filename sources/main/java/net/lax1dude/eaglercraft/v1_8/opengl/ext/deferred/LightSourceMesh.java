package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

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
public class LightSourceMesh {

	public final ResourceLocation meshLocation;
	private final byte[] typeBytes;

	private IBufferGL meshVBO = null;
	private IBufferGL meshIBO = null;
	private IBufferArrayGL meshVAO = null;

	private int meshIndexType = -1;
	private int meshIndexCount = -1;

	public LightSourceMesh(ResourceLocation is, String type) {
		meshLocation = is;
		typeBytes = type.getBytes(StandardCharsets.UTF_8);
	}

	public void load() throws IOException {
		destroy();
		try (DataInputStream is = new DataInputStream(
				Minecraft.getMinecraft().getResourceManager().getResource(meshLocation).getInputStream())) {
			if(is.read() != 0xEE || is.read() != 0xAA || is.read() != 0x66 || is.read() != '%') {
				throw new IOException("Bad file type for: " + meshLocation.toString());
			}
			byte[] bb = new byte[is.read()];
			is.read(bb);
			if(!Arrays.equals(bb, typeBytes)) {
				throw new IOException("Bad file type \"" + new String(bb, StandardCharsets.UTF_8) + "\" for: " + meshLocation.toString());
			}
			
			int vboLength = is.readInt() * 6;
			byte[] readBuffer = new byte[vboLength];
			is.read(readBuffer);
			
			ByteBuffer buf = EagRuntime.allocateByteBuffer(readBuffer.length);
			buf.put(readBuffer);
			buf.flip();
			
			meshVBO = _wglGenBuffers();
			EaglercraftGPU.bindGLArrayBuffer(meshVBO);
			_wglBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
			
			EagRuntime.freeByteBuffer(buf);
			
			int iboLength = meshIndexCount = is.readInt();
			int iboType = is.read();
			iboLength *= iboType;
			switch(iboType) {
			case 1:
				meshIndexType = GL_UNSIGNED_BYTE;
				break;
			case 2:
				meshIndexType = GL_UNSIGNED_SHORT;
				break;
			case 4:
				meshIndexType = GL_UNSIGNED_INT;
				break;
			default:
				throw new IOException("Unsupported index buffer type: " + iboType);
			}
			
			readBuffer = new byte[iboLength];
			is.read(readBuffer);
			
			buf = EagRuntime.allocateByteBuffer(readBuffer.length);
			buf.put(readBuffer);
			buf.flip();
			
			meshVAO = _wglGenVertexArrays();
			EaglercraftGPU.bindGLBufferArray(meshVAO);
			
			meshIBO = _wglGenBuffers();
			_wglBindBuffer(GL_ELEMENT_ARRAY_BUFFER, meshIBO);
			_wglBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
			EagRuntime.freeByteBuffer(buf);
			
			EaglercraftGPU.bindGLArrayBuffer(meshVBO);
			
			_wglEnableVertexAttribArray(0);
			_wglVertexAttribPointer(0, 3, _GL_HALF_FLOAT, false, 6, 0);
		}
	}

	public void drawMeshVAO() {
		EaglercraftGPU.bindGLBufferArray(meshVAO);
		_wglDrawElements(GL_TRIANGLES, meshIndexCount, meshIndexType, 0);
	}

	public void destroy() {
		if(meshVBO != null) {
			_wglDeleteBuffers(meshVBO);
			meshVBO = null;
		}
		if(meshIBO != null) {
			_wglDeleteBuffers(meshIBO);
			meshIBO = null;
		}
		if(meshVAO != null) {
			_wglDeleteVertexArrays(meshVAO);
			meshVAO = null;
		}
	}
}
