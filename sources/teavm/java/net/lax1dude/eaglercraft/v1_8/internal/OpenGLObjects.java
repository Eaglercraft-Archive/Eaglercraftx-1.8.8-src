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

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
class OpenGLObjects {

	static class BufferGL implements IBufferGL {
		
		final WebGLBuffer ptr;
		
		BufferGL(WebGLBuffer ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteBuffers(this);
		}
		
	}

	static class BufferArrayGL implements IBufferArrayGL {
		
		final WebGLVertexArray ptr;
		
		BufferArrayGL(WebGLVertexArray ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteVertexArrays(this);
		}
		
	}

	static class TextureGL implements ITextureGL {
		
		final WebGLTexture ptr;
		
		TextureGL(WebGLTexture ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteTextures(this);
		}
		
	}

	static class ProgramGL implements IProgramGL {
		
		final WebGLProgram ptr;
		
		ProgramGL(WebGLProgram ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteProgram(this);
		}
		
	}

	static class UniformGL implements IUniformGL {
		
		final WebGLUniformLocation ptr;
		
		UniformGL(WebGLUniformLocation ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
		}
		
	}

	static class ShaderGL implements IShaderGL {
		
		final WebGLShader ptr;
		
		ShaderGL(WebGLShader ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteShader(this);
		}
		
	}

	static class FramebufferGL implements IFramebufferGL {
		
		final WebGLFramebuffer ptr;
		
		FramebufferGL(WebGLFramebuffer ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteFramebuffer(this);
		}
		
	}

	static class RenderbufferGL implements IRenderbufferGL {
		
		final WebGLRenderbuffer ptr;
		
		RenderbufferGL(WebGLRenderbuffer ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteRenderbuffer(this);
		}
		
	}

	static class QueryGL implements IQueryGL {
		
		final WebGLQuery ptr;
		
		QueryGL(WebGLQuery ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteQueries(this);
		}
		
	}
	
}
