package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

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
public class PipelineShaderGBufferDebugView extends ShaderProgram<PipelineShaderGBufferDebugView.Uniforms> {

	public static PipelineShaderGBufferDebugView compile(int view) throws ShaderException {
		IShaderGL debugView = ShaderCompiler.compileShader("gbuffer_debug_view", GL_FRAGMENT_SHADER,
					ShaderSource.gbuffer_debug_view_fsh, ("DEBUG_VIEW_" + view));
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("gbuffer_debug_view", SharedPipelineShaders.deferred_local, debugView);
			return new PipelineShaderGBufferDebugView(prog, view);
		}finally {
			if(debugView != null) {
				debugView.free();
			}
		}
	}

	private PipelineShaderGBufferDebugView(IProgramGL prog, int mode) {
		super(prog, new Uniforms(mode));
	}

	public static class Uniforms implements IProgramUniforms {

		public final int mode;

		public IUniformGL u_inverseViewMatrix = null;
		public IUniformGL u_depthSliceStartEnd2f = null;

		private Uniforms(int mode) {
			this.mode = mode;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_texture0"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_texture1"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_texture3D0"), 0);
			u_inverseViewMatrix = _wglGetUniformLocation(prog, "u_inverseViewMatrix");
			u_depthSliceStartEnd2f = _wglGetUniformLocation(prog, "u_depthSliceStartEnd2f");
		}

	}

}
