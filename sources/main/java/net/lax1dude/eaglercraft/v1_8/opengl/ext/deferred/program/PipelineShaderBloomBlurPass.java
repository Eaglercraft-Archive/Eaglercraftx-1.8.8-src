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

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

public class PipelineShaderBloomBlurPass extends ShaderProgram<PipelineShaderBloomBlurPass.Uniforms> {

	public static PipelineShaderBloomBlurPass compile() {
		IShaderGL bloomBlurPass = ShaderCompiler.compileShader("post_bloom_blur", GL_FRAGMENT_SHADER,
				ShaderSource.post_bloom_blur_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_bloom_blur", SharedPipelineShaders.deferred_local, bloomBlurPass);
			return new PipelineShaderBloomBlurPass(prog);
		}finally {
			if(bloomBlurPass != null) {
				bloomBlurPass.free();
			}
		}
	}

	private PipelineShaderBloomBlurPass(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleOffset2f = null;
		public IUniformGL u_outputSize4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sampleOffset2f = _wglGetUniformLocation(prog, "u_sampleOffset2f");
			u_outputSize4f = _wglGetUniformLocation(prog, "u_outputSize4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
		}

	}

}