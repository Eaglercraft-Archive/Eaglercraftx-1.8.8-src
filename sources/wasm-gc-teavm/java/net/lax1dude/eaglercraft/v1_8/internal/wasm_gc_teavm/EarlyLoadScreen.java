/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

public class EarlyLoadScreen {

	public static final String loadScreen = "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";

	private static IBufferGL vbo = null;
	private static IProgramGL program = null;
	private static ITextureGL finalTexture = null;

	public static void extractingAssetsScreen() {
		boolean gles3 = checkOpenGLESVersion() >= 300;
		boolean vaos = checkVAOCapable();

		// create textures:
		
		ITextureGL tex = _wglGenTextures();
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		ImageData img = PlatformAssets.loadImageFile(Base64.decodeBase64(loadScreen));
		MemoryStack.push();
		try {
			ByteBuffer upload = MemoryStack.mallocByteBuffer(192*192*4);
			IntBuffer pixelUpload = upload.asIntBuffer();
			pixelUpload.put(img.pixels);
			pixelUpload.flip();
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 192, 192, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelUpload);
			
			// create vertex buffer:
			
			FloatBuffer vertexUpload = upload.asFloatBuffer();
			vertexUpload.clear();
			vertexUpload.put(0.0f); vertexUpload.put(0.0f);
			vertexUpload.put(0.0f); vertexUpload.put(1.0f);
			vertexUpload.put(1.0f); vertexUpload.put(0.0f);
			vertexUpload.put(1.0f); vertexUpload.put(0.0f);
			vertexUpload.put(0.0f); vertexUpload.put(1.0f);
			vertexUpload.put(1.0f); vertexUpload.put(1.0f);
			vertexUpload.flip();
			
			vbo = _wglGenBuffers();
			_wglBindBuffer(GL_ARRAY_BUFFER, vbo);
			_wglBufferData(GL_ARRAY_BUFFER, vertexUpload, GL_STATIC_DRAW);
		}finally {
			MemoryStack.pop();
		}

		// compile the splash shader:
		
		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		_wglShaderSource(vert, gles3
				? "#version 300 es\nprecision mediump float; layout(location = 0) in vec2 a_pos; out vec2 v_pos; void main() { gl_Position = vec4(((v_pos = a_pos) - 0.5) * vec2(2.0, -2.0), 0.0, 1.0); }"
				: "#version 100\nprecision mediump float; attribute vec2 a_pos; varying vec2 v_pos; void main() { gl_Position = vec4(((v_pos = a_pos) - 0.5) * vec2(2.0, -2.0), 0.0, 1.0); }");
		_wglCompileShader(vert);
		
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);
		_wglShaderSource(frag, gles3
				? "#version 300 es\nprecision mediump float; precision mediump sampler2D; in vec2 v_pos; layout(location = 0) out vec4 fragColor; uniform sampler2D tex; uniform vec2 aspect; void main() { fragColor = vec4(textureLod(tex, clamp(v_pos * aspect - ((aspect - 1.0) * 0.5), 0.02, 0.98), 0.0).rgb, 1.0); }"
				: "#version 100\nprecision mediump float; precision mediump sampler2D; varying vec2 v_pos; uniform sampler2D tex; uniform vec2 aspect; void main() { gl_FragColor = vec4(texture2D(tex, clamp(v_pos * aspect - ((aspect - 1.0) * 0.5), 0.02, 0.98)).rgb, 1.0); }");
		_wglCompileShader(frag);
		
		program = _wglCreateProgram();
		
		_wglAttachShader(program, vert);
		_wglAttachShader(program, frag);
		if(!gles3) {
			_wglBindAttribLocation(program, 0, "a_pos");
		}
		_wglLinkProgram(program);
		_wglDetachShader(program, vert);
		_wglDetachShader(program, frag);
		_wglDeleteShader(vert);
		_wglDeleteShader(frag);
		
		_wglUseProgram(program);
		_wglUniform1i(_wglGetUniformLocation(program, "tex"), 0);

		int width = PlatformInput.getWindowWidth();
		int height = PlatformInput.getWindowHeight();
		float x, y;
		if(width > height) {
			x = (float)width / (float)height;
			y = 1.0f;
		}else {
			x = 1.0f;
			y = (float)height / (float)width;
		}
		
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		
		_wglViewport(0, 0, width, height);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		_wglClear(GL_COLOR_BUFFER_BIT);

		_wglUseProgram(program);
		_wglUniform2f(_wglGetUniformLocation(program, "aspect"), x, y);
		
		IBufferArrayGL vao = null;
		if(vaos) {
			vao = _wglGenVertexArrays();
			_wglBindVertexArray(vao);
		}
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
		
		_wglDisableVertexAttribArray(0);
		
		PlatformInput.update();

		_wglUseProgram(null);
		_wglBindBuffer(GL_ARRAY_BUFFER, null);
		_wglBindTexture(GL_TEXTURE_2D, null);
		_wglDeleteTextures(tex);
		if(vaos) {
			_wglDeleteVertexArrays(vao);
		}
	}

	public static void loadFinal(byte[] finalLoadScreen) {
		ImageData img = PlatformAssets.loadImageFile(finalLoadScreen);
		if(img == null) {
			return;
		}
		finalTexture = _wglGenTextures();
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, finalTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		MemoryStack.push();
		try {
			IntBuffer upload = MemoryStack.mallocIntBuffer(img.width * img.height);
			upload.put(img.pixels);
			upload.flip();
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.width, img.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, upload);
		}finally {
			MemoryStack.pop();
		}
	}

	public static void paintFinal(boolean softVAOs) {
		if(finalTexture == null) return;
		boolean vaos = checkVAOCapable();
		
		_wglBindTexture(GL_TEXTURE_2D, finalTexture);
		_wglUseProgram(program);

		int width = PlatformInput.getWindowWidth();
		int height = PlatformInput.getWindowHeight();
		float x, y;
		if(width > height) {
			x = (float)width / (float)height;
			y = 1.0f;
		}else {
			x = 1.0f;
			y = (float)height / (float)width;
		}
		
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, finalTexture);
		
		_wglViewport(0, 0, width, height);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		_wglClear(GL_COLOR_BUFFER_BIT);
		
		_wglUniform2f(_wglGetUniformLocation(program, "aspect"), x, y);

		IBufferArrayGL vao = null;
		if(vaos) {
			if(softVAOs) {
				vao = EaglercraftGPU.createGLBufferArray();
				EaglercraftGPU.bindGLBufferArray(vao);
			}else {
				vao = _wglGenVertexArrays();
				_wglBindVertexArray(vao);
			}
		}
		if(vaos && softVAOs) {
			EaglercraftGPU.bindVAOGLArrayBuffer(vbo);
			EaglercraftGPU.enableVertexAttribArray(0);
			EaglercraftGPU.vertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
			EaglercraftGPU.doDrawArrays(GL_TRIANGLES, 0, 6);
		}else {
			_wglBindBuffer(GL_ARRAY_BUFFER, vbo);
			_wglEnableVertexAttribArray(0);
			_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
			_wglDrawArrays(GL_TRIANGLES, 0, 6);
		}

		if(!softVAOs) {
			_wglDisableVertexAttribArray(0);
		}
		
		PlatformInput.update();

		_wglUseProgram(null);
		if(!(vaos && softVAOs)) {
			_wglBindBuffer(GL_ARRAY_BUFFER, null);
		}
		_wglBindTexture(GL_TEXTURE_2D, null);
		if(softVAOs) {
			EaglercraftGPU.clearCurrentBinding(EaglercraftGPU.CLEAR_BINDING_ACTIVE_TEXTURE | EaglercraftGPU.CLEAR_BINDING_TEXTURE0);
		}
		if(vaos) {
			if(softVAOs) {
				EaglercraftGPU.destroyGLBufferArray(vao);
			}else {
				_wglDeleteVertexArrays(vao);
			}
		}
	}

	public static void destroy() {
		if(vbo != null) {
			_wglDeleteBuffers(vbo);
			vbo = null;
		}
		if(program != null) {
			_wglDeleteProgram(program);
			program = null;
		}
		if(finalTexture != null) {
			_wglDeleteTextures(finalTexture);
			finalTexture = null;
		}
	}

}