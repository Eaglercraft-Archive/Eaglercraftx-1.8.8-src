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

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

public class GBufferExtPipelineShader extends ShaderProgram<GBufferExtPipelineShader.Uniforms> {

	public final int coreState;
	public final int extState;

	public GBufferExtPipelineShader(IProgramGL program, int coreState, int extState) {
		super(program, new Uniforms());
		this.coreState = coreState;
		this.extState = extState;
	}

	public static class Uniforms implements IProgramUniforms {

		public int materialConstantsSerial = -1;

		public float materialConstantsRoughness = -999.0f;
		public float materialConstantsMetalness = -999.0f;
		public float materialConstantsEmission = -999.0f;
		public float materialConstantsUseEnvMap = -999.0f;

		public IUniformGL u_materialConstants3f = null;
		public IUniformGL u_useEnvMap1f = null;

		public int constantBlock = -999;
		public float clipPlaneY = -999.0f;

		public IUniformGL u_blockConstant1f = null;
		public IUniformGL u_clipPlaneY1f = null;

		public int modelMatrixSerial = -1;
		public int viewMatrixSerial = -1;
		public int inverseViewMatrixSerial = -1;
		public int modelViewProjMatrixAltSerial = -1;
		public IUniformGL u_modelMatrix4f = null;
		public IUniformGL u_viewMatrix4f = null;
		public IUniformGL u_inverseViewMatrix4f = null;
		public IUniformGL u_modelViewProjMat4f_ = null;

		public int waterWindOffsetSerial = -1;
		public IUniformGL u_waterWindOffset4f = null;

		public int wavingBlockOffsetSerial = -1;

		public float wavingBlockOffsetX = -999.0f;
		public float wavingBlockOffsetY = -999.0f;
		public float wavingBlockOffsetZ = -999.0f;

		public IUniformGL u_wavingBlockOffset3f = null;

		public int wavingBlockParamSerial = -1;

		public float wavingBlockParamX = -999.0f;
		public float wavingBlockParamY = -999.0f;
		public float wavingBlockParamZ = -999.0f;
		public float wavingBlockParamW = -999.0f;

		public IUniformGL u_wavingBlockParam4f = null;

		public int u_chunkLightingDataBlockBinding = -1;
		public int u_worldLightingDataBlockBinding = -1;

		public IUniformGL u_farPlane1f = null;
		public float farPlane1f = -1.0f;

		Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_materialConstants3f = _wglGetUniformLocation(prog, "u_materialConstants3f");
			u_useEnvMap1f = _wglGetUniformLocation(prog, "u_useEnvMap1f");
			u_blockConstant1f = _wglGetUniformLocation(prog, "u_blockConstant1f");
			u_clipPlaneY1f = _wglGetUniformLocation(prog, "u_clipPlaneY1f");
			u_modelMatrix4f = _wglGetUniformLocation(prog, "u_modelMatrix4f");
			u_viewMatrix4f = _wglGetUniformLocation(prog, "u_viewMatrix4f");
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_modelViewProjMat4f_ = _wglGetUniformLocation(prog, "u_modelViewProjMat4f_");
			u_wavingBlockOffset3f = _wglGetUniformLocation(prog, "u_wavingBlockOffset3f");
			u_wavingBlockParam4f = _wglGetUniformLocation(prog, "u_wavingBlockParam4f");
			u_farPlane1f = _wglGetUniformLocation(prog, "u_farPlane1f");
			u_waterWindOffset4f = _wglGetUniformLocation(prog, "u_waterWindOffset4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_samplerNormalMaterial"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_environmentMap"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_brdfLUT"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reflectionMap"), 7);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_refractionMap"), 8);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_normalMap"), 9);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_irradianceMap"), 10);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightShaftsTexture"), 11);
			int blockIndex = _wglGetUniformBlockIndex(prog, "u_worldLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 0);
				u_worldLightingDataBlockBinding = 0;
			}else {
				u_worldLightingDataBlockBinding = -1;
			}
			blockIndex = _wglGetUniformBlockIndex(prog, "u_chunkLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 1);
				u_chunkLightingDataBlockBinding = 1;
			}else {
				u_chunkLightingDataBlockBinding = -1;
			}
		}

	}

}