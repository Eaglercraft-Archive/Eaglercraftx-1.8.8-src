package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
