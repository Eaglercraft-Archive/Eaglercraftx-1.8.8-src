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

public class PipelineShaderSkyboxAtmosphere extends ShaderProgram<PipelineShaderSkyboxAtmosphere.Uniforms> {

	public static PipelineShaderSkyboxAtmosphere compile() throws ShaderException {
		IShaderGL skyboxAtmosphere = ShaderCompiler.compileShader("skybox_atmosphere", GL_FRAGMENT_SHADER,
					ShaderSource.skybox_atmosphere_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("skybox_atmosphere", SharedPipelineShaders.deferred_local, skyboxAtmosphere);
			return new PipelineShaderSkyboxAtmosphere(prog);
		}finally {
			if(skyboxAtmosphere != null) {
				skyboxAtmosphere.free();
			}
		}
	}

	private PipelineShaderSkyboxAtmosphere(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sunDirectionIntensity4f = null;
		public IUniformGL u_altitude1f = null;
		public IUniformGL u_blendColor4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sunDirectionIntensity4f = _wglGetUniformLocation(prog, "u_sunDirectionIntensity4f");
			u_altitude1f = _wglGetUniformLocation(prog, "u_altitude1f");
			u_blendColor4f = _wglGetUniformLocation(prog, "u_blendColor4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_skyNormals"), 0);
		}

	}
}