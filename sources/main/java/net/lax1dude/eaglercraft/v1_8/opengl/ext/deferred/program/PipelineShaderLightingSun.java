package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

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
public class PipelineShaderLightingSun extends ShaderProgram<PipelineShaderLightingSun.Uniforms> {

	public static PipelineShaderLightingSun compile(int shadowsSun, boolean coloredShadows) throws ShaderException {
		IShaderGL sunShader = null;
		List<String> compileFlags = new ArrayList(1);
		if(shadowsSun > 0) {
			compileFlags.add("COMPILE_SUN_SHADOW");
		}
		if(coloredShadows) {
			compileFlags.add("COMPILE_COLORED_SHADOW");
		}
		sunShader = ShaderCompiler.compileShader("lighting_sun", GL_FRAGMENT_SHADER,
				ShaderSource.lighting_sun_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("lighting_sun", SharedPipelineShaders.deferred_local, sunShader);
			return new PipelineShaderLightingSun(prog, shadowsSun);
		}finally {
			if(sunShader != null) {
				sunShader.free();
			}
		}
	}

	private PipelineShaderLightingSun(IProgramGL program, int shadowsSun) {
		super(program, new Uniforms(shadowsSun));
	}

	public static class Uniforms implements IProgramUniforms {

		public final int shadowsSun;
		public IUniformGL u_inverseViewMatrix4f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_sunDirection3f;
		public IUniformGL u_sunColor3f;

		private Uniforms(int shadowsSun) {
			this.shadowsSun = shadowsSun;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowTexture"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 5);
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
			u_sunColor3f = _wglGetUniformLocation(prog, "u_sunColor3f");
		}

	}

}
