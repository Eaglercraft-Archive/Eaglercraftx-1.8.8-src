package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

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
public class PipelineShaderGBufferCombine extends ShaderProgram<PipelineShaderGBufferCombine.Uniforms> {

	public static PipelineShaderGBufferCombine compile(boolean ssao, boolean env, boolean ssr) throws ShaderException {
		IShaderGL coreGBuffer = null;
		List<String> compileFlags = new ArrayList<>(2);
		if(ssao) {
			compileFlags.add("COMPILE_GLOBAL_AMBIENT_OCCLUSION");
		}
		if(env) {
			compileFlags.add("COMPILE_ENV_MAP_REFLECTIONS");
		}
		if(ssr) {
			compileFlags.add("COMPILE_SCREEN_SPACE_REFLECTIONS");
		}
		coreGBuffer = ShaderCompiler.compileShader("deferred_combine", GL_FRAGMENT_SHADER,
				ShaderSource.deferred_combine_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("deferred_combine", SharedPipelineShaders.deferred_local, coreGBuffer);
			return new PipelineShaderGBufferCombine(prog, ssao, env, ssr);
		}finally {
			if(coreGBuffer != null) {
				coreGBuffer.free();
			}
		}
	}

	private PipelineShaderGBufferCombine(IProgramGL program, boolean ssao, boolean env, boolean ssr) {
		super(program, new Uniforms(ssao, env, ssr));
	}

	public static class Uniforms implements IProgramUniforms {

		public final boolean ssao;
		public final boolean env;
		public final boolean ssr;

		public IUniformGL u_halfResolutionPixelAlignment2f;
		public IUniformGL u_inverseProjMatrix4f;
		public IUniformGL u_inverseViewMatrix4f;
		public IUniformGL u_sunDirection3f;
		public IUniformGL u_skyLightFactor1f;

		private Uniforms(boolean ssao, boolean env, boolean ssr) {
			this.ssao = ssao;
			this.ssr = ssr;
			this.env = env;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ssaoTexture"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ssrReflectionTexture"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_environmentMap"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_irradianceMap"), 7);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_brdfLUT"), 8);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 9);
			u_halfResolutionPixelAlignment2f = _wglGetUniformLocation(prog, "u_halfResolutionPixelAlignment2f");
			u_inverseProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjMatrix4f");
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
			u_skyLightFactor1f = _wglGetUniformLocation(prog, "u_skyLightFactor1f");
		}

	}

}
