package net.lax1dude.eaglercraft.v1_8.internal;

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
		
		final int ptr;
		
		BufferGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteBuffers(this);
		}
		
	}

	static class BufferArrayGL implements IBufferArrayGL {
		
		final int ptr;
		
		BufferArrayGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteVertexArrays(this);
		}
		
	}

	static class TextureGL implements ITextureGL {
		
		final int ptr;
		
		TextureGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteTextures(this);
		}
		
	}

	static class ProgramGL implements IProgramGL {
		
		final int ptr;
		
		ProgramGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteProgram(this);
		}
		
	}

	static class UniformGL implements IUniformGL {
		
		final int ptr;
		
		UniformGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
		}
		
	}

	static class ShaderGL implements IShaderGL {
		
		final int ptr;
		
		ShaderGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteShader(this);
		}
		
	}

	static class FramebufferGL implements IFramebufferGL {
		
		final int ptr;
		
		FramebufferGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteFramebuffer(this);
		}
		
	}

	static class RenderbufferGL implements IRenderbufferGL {
		
		final int ptr;
		
		RenderbufferGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteRenderbuffer(this);
		}
		
	}

	static class QueryGL implements IQueryGL {
		
		final int ptr;
		
		QueryGL(int ptr) {
			this.ptr = ptr;
		}

		@Override
		public void free() {
			PlatformOpenGL._wglDeleteQueries(this);
		}
		
	}
	
}
