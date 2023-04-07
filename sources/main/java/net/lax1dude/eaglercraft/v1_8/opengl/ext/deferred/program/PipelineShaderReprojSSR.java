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
public class PipelineShaderReprojSSR extends ShaderProgram<PipelineShaderReprojSSR.Uniforms> {

	public static PipelineShaderReprojSSR compile() throws ShaderException {
		IShaderGL reprojSSR = ShaderCompiler.compileShader("reproj_ssr", GL_FRAGMENT_SHADER,
					ShaderSource.reproject_ssr_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("reproj_ssr", SharedPipelineShaders.deferred_local, reprojSSR);
			return new PipelineShaderReprojSSR(prog);
		}finally {
			if(reprojSSR != null) {
				reprojSSR.free();
			}
		}
	}

	private PipelineShaderReprojSSR(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_lastProjectionMatrix4f;
		public IUniformGL u_lastInverseProjMatrix4x2f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_sampleStep1f;
		public IUniformGL u_pixelAlignment4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionReflectionInput4f"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionHitVectorInput4f"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameDepthInput"), 5);
			u_lastProjectionMatrix4f = _wglGetUniformLocation(prog, "u_lastProjectionMatrix4f");
			u_lastInverseProjMatrix4x2f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4x2f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_sampleStep1f = _wglGetUniformLocation(prog, "u_sampleStep1f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
		}

	}

}
