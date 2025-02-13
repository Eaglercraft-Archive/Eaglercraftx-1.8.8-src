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

public class PipelineShaderLensSunOcclusion extends ShaderProgram<PipelineShaderLensSunOcclusion.Uniforms> {

	public static PipelineShaderLensSunOcclusion compile() throws ShaderException {
		IShaderGL sunOcclusion = ShaderCompiler.compileShader("lens_sun_occlusion", GL_FRAGMENT_SHADER,
					ShaderSource.lens_sun_occlusion_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("lens_sun_occlusion", SharedPipelineShaders.deferred_local, sunOcclusion);
			return new PipelineShaderLensSunOcclusion(prog);
		}finally {
			if(sunOcclusion != null) {
				sunOcclusion.free();
			}
		}
	}

	private PipelineShaderLensSunOcclusion(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleMatrix3f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sampleMatrix3f = _wglGetUniformLocation(prog, "u_sampleMatrix3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_depthBufferTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsSunOcclusion"), 1);
		}

	}

}