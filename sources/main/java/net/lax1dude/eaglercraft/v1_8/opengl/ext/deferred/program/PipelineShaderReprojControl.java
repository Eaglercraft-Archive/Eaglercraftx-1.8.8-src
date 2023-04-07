package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

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
public class PipelineShaderReprojControl extends ShaderProgram<PipelineShaderReprojControl.Uniforms> {

	public static PipelineShaderReprojControl compile(boolean ssao, boolean ssr) throws ShaderException {
		List<String> compileFlags = new ArrayList(2);
		if(ssao) {
			compileFlags.add("COMPILE_REPROJECT_SSAO");
		}
		if(ssr) {
			compileFlags.add("COMPILE_REPROJECT_SSR");
		}
		IShaderGL reprojControl = ShaderCompiler.compileShader("reproj_control", GL_FRAGMENT_SHADER,
					ShaderSource.reproject_control_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("reproj_control", SharedPipelineShaders.deferred_local, reprojControl);
			return new PipelineShaderReprojControl(prog, ssao, ssr);
		}finally {
			if(reprojControl != null) {
				reprojControl.free();
			}
		}
	}

	private PipelineShaderReprojControl(IProgramGL prog, boolean ssao, boolean ssr) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_reprojectionMatrix4f = null;
		public IUniformGL u_inverseProjectionMatrix4f = null;
		public IUniformGL u_lastInverseProjMatrix4f = null;
		public IUniformGL u_reprojectionInverseViewMatrix4f = null;
		public IUniformGL u_viewToPreviousProjMatrix4f = null;
		public IUniformGL u_nearFarPlane4f = null;
		public IUniformGL u_pixelAlignment4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ssaoSampleTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionSSAOInput4f"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionReflectionInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionHitVectorInput4f"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorInput4f"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionDepthTexture"), 7);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 8);
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_reprojectionMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_lastInverseProjMatrix4f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4f");
			u_reprojectionInverseViewMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionInverseViewMatrix4f");
			u_viewToPreviousProjMatrix4f = _wglGetUniformLocation(prog, "u_viewToPreviousProjMatrix4f");
			u_nearFarPlane4f = _wglGetUniformLocation(prog, "u_nearFarPlane4f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
		}

	}

}
