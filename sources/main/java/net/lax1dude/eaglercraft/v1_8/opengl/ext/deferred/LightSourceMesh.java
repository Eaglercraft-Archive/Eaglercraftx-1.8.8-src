/*
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IVertexArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class LightSourceMesh {

	public final ResourceLocation meshLocation;
	private final byte[] typeBytes;

	private IBufferGL meshVBO = null;
	private IBufferGL meshIBO = null;
	private IVertexArrayGL meshVAO = null;

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
			if(is.readUnsignedByte() != 0xEE || is.readUnsignedByte() != 0xAA || is.readUnsignedByte() != 0x66
					|| is.readUnsignedByte() != '%') {
				throw new IOException("Bad file type for: " + meshLocation.toString());
			}
			byte[] bb = new byte[is.readUnsignedByte()];
			is.readFully(bb);
			if(!Arrays.equals(bb, typeBytes)) {
				throw new IOException("Bad file type \"" + new String(bb, StandardCharsets.UTF_8) + "\" for: " + meshLocation.toString());
			}
			
			int vboLength = is.readInt() * 6;
			byte[] readBuffer = new byte[vboLength];
			is.readFully(readBuffer);
			
			ByteBuffer buf = EagRuntime.allocateByteBuffer(readBuffer.length);
			buf.put(readBuffer);
			buf.flip();
			
			meshVBO = _wglGenBuffers();
			EaglercraftGPU.bindGLArrayBuffer(meshVBO);
			_wglBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
			
			EagRuntime.freeByteBuffer(buf);
			
			int iboLength = meshIndexCount = is.readInt();
			int iboType = is.readUnsignedByte();
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
			is.readFully(readBuffer);
			
			buf = EagRuntime.allocateByteBuffer(readBuffer.length);
			buf.put(readBuffer);
			buf.flip();
			
			meshVAO = _wglGenVertexArrays();
			EaglercraftGPU.bindGLVertexArray(meshVAO);
			
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
		EaglercraftGPU.bindGLVertexArray(meshVAO);
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