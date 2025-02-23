/*
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal;

import org.teavm.jso.webgl.WebGLBuffer;
import org.teavm.jso.webgl.WebGLFramebuffer;
import org.teavm.jso.webgl.WebGLProgram;
import org.teavm.jso.webgl.WebGLRenderbuffer;
import org.teavm.jso.webgl.WebGLShader;
import org.teavm.jso.webgl.WebGLTexture;
import org.teavm.jso.webgl.WebGLUniformLocation;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGLQuery;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGLVertexArray;

class OpenGLObjects {

	static class BufferGL implements IBufferGL {

		private static int hashGen = 0;
		final WebGLBuffer ptr;
		final int hash;

		BufferGL(WebGLBuffer ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteBuffers(this);
		}

	}

	static class VertexArrayGL implements IVertexArrayGL {

		private static int hashGen = 0;
		final WebGLVertexArray ptr;
		final int hash;

		VertexArrayGL(WebGLVertexArray ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteVertexArrays(this);
		}

	}

	static class TextureGL implements ITextureGL {

		private static int hashGen = 0;
		final WebGLTexture ptr;
		final int hash;
		int width;
		int height;

		TextureGL(WebGLTexture ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteTextures(this);
		}

		@Override
		public void setCacheSize(int w, int h) {
			width = w;
			height = h;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

	}

	static class ProgramGL implements IProgramGL {

		private static int hashGen = 0;
		final WebGLProgram ptr;
		final int hash;

		ProgramGL(WebGLProgram ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteProgram(this);
		}

	}

	static class UniformGL implements IUniformGL {

		private static int hashGen = 0;
		final WebGLUniformLocation ptr;
		final int hash;

		UniformGL(WebGLUniformLocation ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
		}

	}

	static class ShaderGL implements IShaderGL {

		private static int hashGen = 0;
		final WebGLShader ptr;
		final int hash;
		
		ShaderGL(WebGLShader ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteShader(this);
		}
		
	}

	static class FramebufferGL implements IFramebufferGL {

		private static int hashGen = 0;
		final WebGLFramebuffer ptr;
		final int hash;
		
		FramebufferGL(WebGLFramebuffer ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteFramebuffer(this);
		}

	}

	static class RenderbufferGL implements IRenderbufferGL {

		private static int hashGen = 0;
		final WebGLRenderbuffer ptr;
		final int hash;

		RenderbufferGL(WebGLRenderbuffer ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteRenderbuffer(this);
		}

	}

	static class QueryGL implements IQueryGL {

		private static int hashGen = 0;
		final WebGLQuery ptr;
		final int hash;

		QueryGL(WebGLQuery ptr) {
			this.ptr = ptr;
			this.hash = ++hashGen;
		}

		public int hashCode() {
			return hash;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteQueries(this);
		}

	}

}