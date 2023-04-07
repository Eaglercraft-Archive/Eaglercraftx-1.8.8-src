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
public class PipelineShaderRealisticWaterControl extends ShaderProgram<PipelineShaderRealisticWaterControl.Uniforms> {

	public static PipelineShaderRealisticWaterControl compile() throws ShaderException {
		IShaderGL realisticWaterControl = ShaderCompiler.compileShader("realistic_water_control", GL_FRAGMENT_SHADER,
					ShaderSource.realistic_water_control_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("realistic_water_control", SharedPipelineShaders.deferred_local, realisticWaterControl);
			return new PipelineShaderRealisticWaterControl(prog);
		}finally {
			if(realisticWaterControl != null) {
				realisticWaterControl.free();
			}
		}
	}

	public PipelineShaderRealisticWaterControl(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseProjectionMatrix4f = null;
		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_reprojectionMatrix4f = null;
		public IUniformGL u_lastInverseProjMatrix4f = null;
		public IUniformGL u_reprojectionInverseViewMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_viewToPreviousProjMatrix4f = null;
		public IUniformGL u_nearFarPlane4f = null;
		public IUniformGL u_pixelAlignment4f = null;
		public IUniformGL u_refractFogColor4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture4f"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_realisticWaterMaskNormal"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_realisticWaterDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameReflectionInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameHitVectorInput4f"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorTexture"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameDepthTexture"), 7);
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_reprojectionMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionMatrix4f");
			u_lastInverseProjMatrix4f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4f");
			u_reprojectionInverseViewMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionInverseViewMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_viewToPreviousProjMatrix4f = _wglGetUniformLocation(prog, "u_viewToPreviousProjMatrix4f");
			u_nearFarPlane4f = _wglGetUniformLocation(prog, "u_nearFarPlane4f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
			u_refractFogColor4f = _wglGetUniformLocation(prog, "u_refractFogColor4f");
		}

	}

}
