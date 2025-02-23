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

import net.lax1dude.eaglercraft.v1_8.internal.IVertexArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

public class EarlyLoadScreen {

	private static IBufferGL vbo = null;
	private static IProgramGL program = null;
	private static ITextureGL finalTexture = null;

	public static void initialize() {
		boolean gles3 = checkOpenGLESVersion() >= 300;

		MemoryStack.push();
		try {
			FloatBuffer vertexUpload = MemoryStack.mallocFloatBuffer(12);
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

		IVertexArrayGL vao = null;
		if(vaos) {
			if(softVAOs) {
				vao = EaglercraftGPU.createGLVertexArray();
				EaglercraftGPU.bindGLVertexArray(vao);
			}else {
				vao = _wglGenVertexArrays();
				_wglBindVertexArray(vao);
			}
		}
		if(vaos && softVAOs) {
			EaglercraftGPU.bindVAOGLArrayBuffer(vbo);
			EaglercraftGPU.enableVertexAttribArray(0);
			EaglercraftGPU.vertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
			EaglercraftGPU.drawArrays(GL_TRIANGLES, 0, 6);
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
				EaglercraftGPU.destroyGLVertexArray(vao);
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