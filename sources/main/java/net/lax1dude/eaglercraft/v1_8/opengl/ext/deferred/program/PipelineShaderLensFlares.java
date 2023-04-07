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
public class PipelineShaderLensFlares extends ShaderProgram<PipelineShaderLensFlares.Uniforms> {

	public static PipelineShaderLensFlares compileStreaks() {
		IShaderGL vertexShader = ShaderCompiler.compileShader("post_lens_streaks", GL_VERTEX_SHADER,
					ShaderSource.post_lens_streaks_vsh);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("post_lens_streaks", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_streaks_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_streaks", vertexShader, fragmentShader);
			return new PipelineShaderLensFlares(prog);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	public static PipelineShaderLensFlares compileGhosts() {
		IShaderGL vertexShader = ShaderCompiler.compileShader("post_lens_ghosts", GL_VERTEX_SHADER,
				ShaderSource.post_lens_ghosts_vsh);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("post_lens_ghosts", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_ghosts_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_ghosts", vertexShader, fragmentShader);
			return new PipelineShaderLensFlares(prog);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	private PipelineShaderLensFlares(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sunFlareMatrix3f = null;
		public IUniformGL u_flareColor3f = null;
		public IUniformGL u_sunPosition2f = null;
		public IUniformGL u_aspectRatio1f = null;
		public IUniformGL u_baseScale1f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sunFlareMatrix3f = _wglGetUniformLocation(prog, "u_sunFlareMatrix3f");
			u_flareColor3f = _wglGetUniformLocation(prog, "u_flareColor3f");
			u_sunPosition2f = _wglGetUniformLocation(prog, "u_sunPosition2f");
			u_aspectRatio1f = _wglGetUniformLocation(prog, "u_aspectRatio1f");
			u_baseScale1f = _wglGetUniformLocation(prog, "u_baseScale1f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_flareTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_exposureValue"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunOcclusionValue"), 2);
		}

	}

}
