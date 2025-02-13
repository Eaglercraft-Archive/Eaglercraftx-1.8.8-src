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

public class PipelineShaderCloudsNoise3D extends ShaderProgram<PipelineShaderCloudsNoise3D.Uniforms> {

	public static PipelineShaderCloudsNoise3D compile() {
		IShaderGL cloudsNoise3d = ShaderCompiler.compileShader("clouds_noise3d", GL_FRAGMENT_SHADER,
				ShaderSource.clouds_noise3d_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_noise3d", SharedPipelineShaders.deferred_local, cloudsNoise3d);
			return new PipelineShaderCloudsNoise3D(prog);
		}finally {
			if(cloudsNoise3d != null) {
				cloudsNoise3d.free();
			}
		}
	}

	private PipelineShaderCloudsNoise3D(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_textureSlice1f = null;
		public IUniformGL u_textureSize2f = null;
		public IUniformGL u_sampleOffsetMatrix4f = null;
		public IUniformGL u_cloudMovement3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_textureSlice1f = _wglGetUniformLocation(prog, "u_textureSlice1f");
			u_textureSize2f = _wglGetUniformLocation(prog, "u_textureSize2f");
			u_sampleOffsetMatrix4f = _wglGetUniformLocation(prog, "u_sampleOffsetMatrix4f");
			u_cloudMovement3f = _wglGetUniformLocation(prog, "u_cloudMovement3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseTexture"), 0);
		}

	}

}