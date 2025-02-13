/*
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

package net.lax1dude.eaglercraft.v1_8.opengl;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

public class EaglerMeshLoader implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("EaglerMeshLoader");

	private static final Map<ResourceLocation, HighPolyMesh> meshCache = new HashMap<>();

	public static HighPolyMesh getEaglerMesh(ResourceLocation meshLoc) {
		if(meshLoc.cachedPointerType == ResourceLocation.CACHED_POINTER_EAGLER_MESH) {
			return (HighPolyMesh)meshLoc.cachedPointer;
		}
		HighPolyMesh theMesh = meshCache.get(meshLoc);
		if(theMesh == null) {
			theMesh = new HighPolyMesh();
			reloadMesh(meshLoc, theMesh, Minecraft.getMinecraft().getResourceManager());
			meshCache.put(meshLoc, theMesh);
		}
		meshLoc.cachedPointerType = ResourceLocation.CACHED_POINTER_EAGLER_MESH;
		meshLoc.cachedPointer = theMesh;
		return theMesh;
	}

	private static void reloadMesh(ResourceLocation meshLoc, HighPolyMesh meshStruct, IResourceManager resourceManager) {
		IntBuffer up1 = null;
		try {
			int intsOfVertex, intsOfIndex, intsTotal, stride;
			try(DataInputStream dis = new DataInputStream(resourceManager.getResource(meshLoc).getInputStream())) {
				byte[] header = new byte[8];
				dis.read(header);
				if(!Arrays.equals(header, new byte[] { (byte) 33, (byte) 69, (byte) 65, (byte) 71, (byte) 36,
						(byte) 109, (byte) 100, (byte) 108 })) {
					throw new IOException("File is not an eaglercraft high-poly mesh!");
				}
				
				char CT = (char)dis.read();
				
				if(CT == 'C') {
					meshStruct.hasTexture = false;
				}else if(CT == 'T') {
					meshStruct.hasTexture = true;
				}else {
					throw new IOException("Unsupported mesh type '" + CT + "'!");
				}
				
				dis.skipBytes(dis.readUnsignedShort());

				meshStruct.vertexCount = dis.readInt();
				meshStruct.indexCount = dis.readInt();
				int byteIndexCount = meshStruct.indexCount;
				if(byteIndexCount % 2 != 0) { // must round up to int
					byteIndexCount += 1;
				}
				stride = meshStruct.hasTexture ? 24 : 16;

				intsOfVertex = meshStruct.vertexCount * stride / 4;
				intsOfIndex = byteIndexCount / 2;
				intsTotal = intsOfIndex + intsOfVertex;
				up1 = EagRuntime.allocateIntBuffer(intsTotal);
				
				for(int i = 0; i < intsTotal; ++i) {
					int ch1 = dis.read();
					int ch2 = dis.read();
					int ch3 = dis.read();
					int ch4 = dis.read();
					if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException(); // rip
					up1.put((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
				}

			}

			if(meshStruct.vertexArray == null) {
				meshStruct.vertexArray = EaglercraftGPU.createGLBufferArray();
			}
			if(meshStruct.vertexBuffer == null) {
				meshStruct.vertexBuffer = _wglGenBuffers();
			}
			if(meshStruct.indexBuffer == null) {
				meshStruct.indexBuffer = _wglGenBuffers();
			}
			
			up1.position(0).limit(intsOfVertex);
			
			EaglercraftGPU.bindVAOGLArrayBufferNow(meshStruct.vertexBuffer);
			_wglBufferData(GL_ARRAY_BUFFER, up1, GL_STATIC_DRAW);
			
			EaglercraftGPU.bindGLBufferArray(meshStruct.vertexArray);
			
			up1.position(intsOfVertex).limit(intsTotal);
			
			EaglercraftGPU.bindVAOGLElementArrayBufferNow(meshStruct.indexBuffer);
			_wglBufferData(GL_ELEMENT_ARRAY_BUFFER, up1, GL_STATIC_DRAW);
			
			EaglercraftGPU.enableVertexAttribArray(0);
			EaglercraftGPU.vertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
			
			if(meshStruct.hasTexture) {
				EaglercraftGPU.enableVertexAttribArray(1);
				EaglercraftGPU.vertexAttribPointer(1, 2, GL_FLOAT, false, stride, 16);
			}
			
			EaglercraftGPU.enableVertexAttribArray(meshStruct.hasTexture ? 2 : 1);
			EaglercraftGPU.vertexAttribPointer(meshStruct.hasTexture ? 2 : 1, 4, GL_BYTE, true, stride, 12);
		}catch(Throwable ex) {
			if(meshStruct.vertexArray != null) {
				EaglercraftGPU.destroyGLBufferArray(meshStruct.vertexArray);
				meshStruct.vertexArray = null;
			}
			if(meshStruct.vertexBuffer != null) {
				_wglDeleteBuffers(meshStruct.vertexBuffer);
				meshStruct.vertexBuffer = null;
			}
			if(meshStruct.indexBuffer != null) {
				_wglDeleteBuffers(meshStruct.indexBuffer);
				meshStruct.indexBuffer = null;
			}
			
			meshStruct.vertexCount = 0;
			meshStruct.indexCount = 0;
			meshStruct.hasTexture = false;
			
			logger.error("Failed to load eaglercraft high-poly mesh: \"{}\"", meshLoc);
			logger.error(ex);
		}finally {
			if(up1 != null) {
				EagRuntime.freeIntBuffer(up1);
			}
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		for(Entry<ResourceLocation, HighPolyMesh> meshEntry : meshCache.entrySet()) {
			reloadMesh(meshEntry.getKey(), meshEntry.getValue(), var1);
		}
	}

}