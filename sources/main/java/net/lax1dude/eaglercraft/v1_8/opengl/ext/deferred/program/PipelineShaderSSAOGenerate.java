/*
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

public class PipelineShaderSSAOGenerate extends ShaderProgram<PipelineShaderSSAOGenerate.Uniforms> {

	public static PipelineShaderSSAOGenerate compile() throws ShaderException {
		IShaderGL ssaoGenerate = ShaderCompiler.compileShader("ssao_generate", GL_FRAGMENT_SHADER,
					ShaderSource.ssao_generate_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("ssao_generate", SharedPipelineShaders.deferred_local, ssaoGenerate);
			return new PipelineShaderSSAOGenerate(prog);
		}finally {
			if(ssaoGenerate != null) {
				ssaoGenerate.free();
			}
		}
	}

	private PipelineShaderSSAOGenerate(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_projectionMatrix4f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_randomizerDataMatrix2f;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_randomizerDataMatrix2f = _wglGetUniformLocation(prog, "u_randomizerDataMatrix2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseConstantTexture"), 2);
		}

	}

}