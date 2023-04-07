package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

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
public class PipelineShaderSkyboxRenderEnd extends ShaderProgram<PipelineShaderSkyboxRenderEnd.Uniforms> {

	public static PipelineShaderSkyboxRenderEnd compile() throws ShaderException {
		IShaderGL vertexShader = ShaderCompiler.compileShader("skybox_render_end", GL_VERTEX_SHADER,
					ShaderSource.skybox_render_end_vsh);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("skybox_render_end", GL_FRAGMENT_SHADER,
					ShaderSource.skybox_render_end_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("skybox_render_end", vertexShader, fragmentShader);
			return new PipelineShaderSkyboxRenderEnd(prog);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	private PipelineShaderSkyboxRenderEnd(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_viewMatrix4f = null;
		public IUniformGL u_projMatrix4f = null;
		public IUniformGL u_skyTextureScale2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_viewMatrix4f = _wglGetUniformLocation(prog, "u_viewMatrix4f");
			u_projMatrix4f = _wglGetUniformLocation(prog, "u_projMatrix4f");
			u_skyTextureScale2f = _wglGetUniformLocation(prog, "u_skyTextureScale2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_skyTexture"), 0);
		}

	}

}
