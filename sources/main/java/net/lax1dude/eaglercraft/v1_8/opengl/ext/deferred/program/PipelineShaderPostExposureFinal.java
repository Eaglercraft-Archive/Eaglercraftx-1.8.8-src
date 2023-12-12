package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class PipelineShaderPostExposureFinal extends ShaderProgram<PipelineShaderPostExposureFinal.Uniforms> {

	public static PipelineShaderPostExposureFinal compile() throws ShaderException {
		IShaderGL postExposureFinal = ShaderCompiler.compileShader("post_exposure_final", GL_FRAGMENT_SHADER,
					ShaderSource.post_exposure_final_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_exposure_final", SharedPipelineShaders.deferred_local, postExposureFinal);
			return new PipelineShaderPostExposureFinal(prog);
		}finally {
			if(postExposureFinal != null) {
				postExposureFinal.free();
			}
		}
	}

	private PipelineShaderPostExposureFinal(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inputSize2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_exposureValue"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunOcclusionValue"), 2);
			u_inputSize2f = _wglGetUniformLocation(prog, "u_inputSize2f");
		}

	}

}
