package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class PipelineShaderMoonRender extends ShaderProgram<PipelineShaderMoonRender.Uniforms> {

	public static PipelineShaderMoonRender compile() {
		IShaderGL moonRenderVSH = ShaderCompiler.compileShader("moon_render", GL_VERTEX_SHADER,
				ShaderSource.moon_render_vsh);
		IShaderGL moonRenderFSH = null;
		try {
			moonRenderFSH = ShaderCompiler.compileShader("moon_render", GL_FRAGMENT_SHADER,
					ShaderSource.moon_render_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("moon_render", moonRenderVSH, moonRenderFSH);
			return new PipelineShaderMoonRender(prog);
		}finally {
			if(moonRenderVSH != null) {
				moonRenderVSH.free();
			}
			if(moonRenderFSH != null) {
				moonRenderFSH.free();
			}
		}
	}

	private PipelineShaderMoonRender(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_modelMatrix4f = null;
		public IUniformGL u_viewMatrix4f = null;
		public IUniformGL u_projMatrix4f = null;
		public IUniformGL u_moonColor3f = null;
		public IUniformGL u_lightDir3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_modelMatrix4f = _wglGetUniformLocation(prog, "u_modelMatrix4f");
			u_viewMatrix4f = _wglGetUniformLocation(prog, "u_viewMatrix4f");
			u_projMatrix4f = _wglGetUniformLocation(prog, "u_projMatrix4f");
			u_moonColor3f = _wglGetUniformLocation(prog, "u_moonColor3f");
			u_lightDir3f = _wglGetUniformLocation(prog, "u_lightDir3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_moonTextures"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsTexture"), 1);
		}

	}

}
