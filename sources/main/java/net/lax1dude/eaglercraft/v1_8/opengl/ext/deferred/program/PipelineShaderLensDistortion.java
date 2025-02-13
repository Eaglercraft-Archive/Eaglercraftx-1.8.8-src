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

public class PipelineShaderLensDistortion extends ShaderProgram<PipelineShaderLensDistortion.Uniforms> {

	public static PipelineShaderLensDistortion compile() throws ShaderException {
		IShaderGL lensDistort = ShaderCompiler.compileShader("post_lens_distort", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_distort_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_distort", SharedPipelineShaders.deferred_local, lensDistort);
			return new PipelineShaderLensDistortion(prog);
		}finally {
			if(lensDistort != null) {
				lensDistort.free();
			}
		}
	}

	private PipelineShaderLensDistortion(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
		}

	}

}