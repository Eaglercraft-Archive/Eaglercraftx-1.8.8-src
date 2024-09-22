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
public class PipelineShaderPostExposureAvg extends ShaderProgram<PipelineShaderPostExposureAvg.Uniforms> {

	public static PipelineShaderPostExposureAvg compile(boolean luma) throws ShaderException {
		List<String> compileFlags = new ArrayList<>(1);
		if(luma) {
			compileFlags.add("CALCULATE_LUMINANCE");
		}
		IShaderGL postExposureAvg = ShaderCompiler.compileShader("post_exposure_avg", GL_FRAGMENT_SHADER,
					ShaderSource.post_exposure_avg_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_exposure_avg", SharedPipelineShaders.deferred_local, postExposureAvg);
			return new PipelineShaderPostExposureAvg(prog);
		}finally {
			if(postExposureAvg != null) {
				postExposureAvg.free();
			}
		}
	}

	private PipelineShaderPostExposureAvg(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleOffset4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
			u_sampleOffset4f = _wglGetUniformLocation(prog, "u_sampleOffset4f");
		}
		
	}

}
