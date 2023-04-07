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
public class PipelineShaderShadowsSun extends ShaderProgram<PipelineShaderShadowsSun.Uniforms> {

	public static PipelineShaderShadowsSun compile(int shadowsSun, boolean shadowsSunSmooth, boolean coloredShadows)
			throws ShaderException {
		IShaderGL shadowShader = null;
		List<String> compileFlags = new ArrayList(2);
		if(shadowsSun == 0) {
			throw new IllegalStateException("Enable shadows to compile this shader");
		}
		int lods = shadowsSun - 1;
		if(lods > 2) {
			lods = 2;
		}
		compileFlags.add("COMPILE_SUN_SHADOW_LOD" + lods);
		if(shadowsSunSmooth) {
			compileFlags.add("COMPILE_SUN_SHADOW_SMOOTH");
		}
		if(coloredShadows) {
			compileFlags.add("COMPILE_COLORED_SHADOW");
		}
		shadowShader = ShaderCompiler.compileShader("shadows_sun", GL_FRAGMENT_SHADER,
				ShaderSource.shadows_sun_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("shadows_sun", SharedPipelineShaders.deferred_local, shadowShader);
			return new PipelineShaderShadowsSun(prog, shadowsSun, shadowsSunSmooth);
		}finally {
			if(shadowShader != null) {
				shadowShader.free();
			}
		}
	}

	private PipelineShaderShadowsSun(IProgramGL program, int shadowsSun, boolean shadowsSunSmooth) {
		super(program, new Uniforms(shadowsSun, shadowsSunSmooth));
	}

	public static class Uniforms implements IProgramUniforms {

		public final int shadowsSun;
		public final boolean shadowsSunSmooth;
		public IUniformGL u_inverseViewMatrix4f;
		public IUniformGL u_inverseViewProjMatrix4f;
		public IUniformGL u_sunShadowMatrixLOD04f;
		public IUniformGL u_sunShadowMatrixLOD14f;
		public IUniformGL u_sunShadowMatrixLOD24f;
		public IUniformGL u_sunDirection3f;

		private Uniforms(int shadowsSun, boolean shadowsSunSmooth) {
			this.shadowsSun = shadowsSun;
			this.shadowsSunSmooth = shadowsSunSmooth;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowColorTexture"), 3);
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_sunShadowMatrixLOD04f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD04f");
			u_sunShadowMatrixLOD14f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD14f");
			u_sunShadowMatrixLOD24f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD24f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
		}

	}


}
