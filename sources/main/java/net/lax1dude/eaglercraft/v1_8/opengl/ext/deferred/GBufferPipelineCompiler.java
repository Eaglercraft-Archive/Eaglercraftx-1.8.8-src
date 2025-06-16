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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.IExtPipelineCompiler;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.GBufferExtPipelineShader;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderCompiler;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;

public class GBufferPipelineCompiler implements IExtPipelineCompiler {

	private static final Logger logger = LogManager.getLogger("DeferredGBufferPipelineCompiler");

	public static final int STATE_MATERIAL_TEXTURE = 1;
	public static final int STATE_FORWARD_RENDER = 2;
	public static final int STATE_PARABOLOID_RENDER = 4;
	public static final int STATE_SHADOW_RENDER = 8;
	public static final int STATE_CLIP_PLANE = 16;
	public static final int STATE_WAVING_BLOCKS = 32;
	public static final int STATE_REALISTIC_WATER_MASK = 64;
	public static final int STATE_REALISTIC_WATER_RENDER = 128;
	public static final int STATE_GLASS_HIGHLIGHTS = 256;

	private static FloatBuffer matrixCopyBuffer = null;
	private static final Matrix4f tmpMatrix = new Matrix4f();

	@Override
	public String[] getShaderSource(int stateCoreBits, int stateExtBits, Object[] userPointer) {
		if(matrixCopyBuffer == null) {
			matrixCopyBuffer = GLAllocation.createDirectFloatBuffer(16);
		}
		userPointer[0] = new GBufferPipelineProgramInstance(stateCoreBits, stateExtBits);
		EaglerDeferredConfig conf = Minecraft.getMinecraft().gameSettings.deferredShaderConf;
		StringBuilder macros = new StringBuilder();
		if (ShaderCompiler.isBrokenD3DCompiler()) {
			macros.append("#define CMPLOD0_D3D_WORKAROUND(a, b) texture(a, b)\n");
		} else {
			macros.append("#define CMPLOD0_D3D_WORKAROUND(a, b) textureLod(a, b, 0.0)\n");
		}
		if((stateExtBits & STATE_SHADOW_RENDER) != 0) {
			if((stateExtBits & STATE_CLIP_PLANE) != 0) {
				macros.append("#define STATE_CLIP_PLANE\n");
			}
			if((stateExtBits & STATE_WAVING_BLOCKS) != 0) {
				macros.append("#define COMPILE_STATE_WAVING_BLOCKS\n");
			}
			if((stateExtBits & STATE_FORWARD_RENDER) != 0) {
				macros.append("#define COMPILE_COLORED_SHADOWS\n");
			}
			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.deferred_shadow_vsh);
			logger.info("   - {}", ShaderSource.deferred_shadow_fsh);
			return new String[] { macros.toString() + ShaderSource.getSourceFor(ShaderSource.deferred_shadow_vsh),
					macros.toString() + ShaderSource.getSourceFor(ShaderSource.deferred_shadow_fsh) };
		}else if((stateExtBits & STATE_REALISTIC_WATER_RENDER) != 0) {
			if(conf.is_rendering_dynamicLights) {
				macros.append("#define COMPILE_DYNAMIC_LIGHTS\n");
			}
			if(conf.is_rendering_shadowsSun_clamped > 0) {
				int lods = conf.is_rendering_shadowsSun_clamped - 1;
				if(lods > 2) {
					lods = 2;
				}
				macros.append("#define COMPILE_SUN_SHADOW_LOD" + lods + "\n");
				if(conf.is_rendering_shadowsSmoothed) {
					macros.append("#define COMPILE_SUN_SHADOW_SMOOTH\n");
				}
			}
			if(conf.is_rendering_lightShafts) {
				macros.append("#define COMPILE_FOG_LIGHT_SHAFTS\n");
			}
			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.realistic_water_render_vsh);
			logger.info("   - {}", ShaderSource.realistic_water_render_fsh);
			return new String[] { macros.toString() + ShaderSource.getSourceFor(ShaderSource.realistic_water_render_vsh),
					macros.toString() + ShaderSource.getSourceFor(ShaderSource.realistic_water_render_fsh) };
		}else if((stateExtBits & STATE_GLASS_HIGHLIGHTS) != 0) {
			if(conf.is_rendering_dynamicLights) {
				macros.append("#define COMPILE_DYNAMIC_LIGHTS\n");
			}
			if(conf.is_rendering_shadowsSun_clamped > 0) {
				int lods = conf.is_rendering_shadowsSun_clamped - 1;
				if(lods > 2) {
					lods = 2;
				}
				macros.append("#define COMPILE_SUN_SHADOW_LOD" + lods + "\n");
				if(conf.is_rendering_shadowsSmoothed) {
					macros.append("#define COMPILE_SUN_SHADOW_SMOOTH\n");
				}
			}
			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.forward_glass_highlights_vsh);
			logger.info("   - {}", ShaderSource.forward_glass_highlights_fsh);
			return new String[] { macros.toString() + ShaderSource.getSourceFor(ShaderSource.forward_glass_highlights_vsh),
					macros.toString() + ShaderSource.getSourceFor(ShaderSource.forward_glass_highlights_fsh) };
		}else if((stateExtBits & (STATE_FORWARD_RENDER | STATE_PARABOLOID_RENDER)) != 0) {
			if((stateExtBits & STATE_MATERIAL_TEXTURE) != 0) {
				macros.append("#define COMPILE_NORMAL_MATERIAL_TEXTURE\n");
			}
			if((stateExtBits & STATE_CLIP_PLANE) != 0) {
				macros.append("#define STATE_CLIP_PLANE\n");
			}
			if((stateExtBits & STATE_PARABOLOID_RENDER) != 0) {
				macros.append("#define COMPILE_PARABOLOID\n");
			}else {
				if(conf.is_rendering_useEnvMap) {
					macros.append("#define COMPILE_PARABOLOID_ENV_MAP\n");
				}
				if(conf.is_rendering_subsurfaceScattering) {
					macros.append("#define COMPILE_SUBSURFACE_SCATTERING\n");
				}
			}
			if(conf.is_rendering_dynamicLights) {
				macros.append("#define COMPILE_DYNAMIC_LIGHTS\n");
			}
			if(conf.is_rendering_shadowsSun_clamped > 0) {
				int lods = conf.is_rendering_shadowsSun_clamped - 1;
				if(lods > 2) {
					lods = 2;
				}
				macros.append("#define COMPILE_SUN_SHADOW_LOD" + lods + "\n");
				if(conf.is_rendering_shadowsSmoothed) {
					macros.append("#define COMPILE_SUN_SHADOW_SMOOTH\n");
				}
			}
			if(conf.is_rendering_lightShafts) {
				macros.append("#define COMPILE_FOG_LIGHT_SHAFTS\n");
			}
			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.forward_core_vsh);
			logger.info("   - {}", ShaderSource.forward_core_fsh);
			return new String[] { macros.toString() + ShaderSource.getSourceFor(ShaderSource.forward_core_vsh),
					macros.toString() + ShaderSource.getSourceFor(ShaderSource.forward_core_fsh) };
		}else if((stateExtBits & STATE_REALISTIC_WATER_MASK) != 0) {
			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.realistic_water_mask_vsh);
			logger.info("   - {}", ShaderSource.realistic_water_mask_fsh);
			return new String[] { ShaderSource.getSourceFor(ShaderSource.realistic_water_mask_vsh),
					ShaderSource.getSourceFor(ShaderSource.realistic_water_mask_fsh) };
		}else {
			if((stateExtBits & STATE_MATERIAL_TEXTURE) != 0) {
				macros.append("#define COMPILE_NORMAL_MATERIAL_TEXTURE\n");
			}
			if((stateExtBits & STATE_CLIP_PLANE) != 0) {
				macros.append("#define COMPILE_STATE_CLIP_PLANE\n");
			}
			if((stateExtBits & STATE_WAVING_BLOCKS) != 0) {
				macros.append("#define COMPILE_STATE_WAVING_BLOCKS\n");
			}
			if(conf.is_rendering_subsurfaceScattering) {
				macros.append("#define COMPILE_SUBSURFACE_SCATTERING\n");
			}

			logger.info("Compiling program for core state: {}, ext state: {}", visualizeBits(stateCoreBits), visualizeBits(stateExtBits));
			logger.info("   - {}", ShaderSource.deferred_core_vsh);
			logger.info("   - {}", ShaderSource.deferred_core_gbuffer_fsh);

			return new String[] { macros.toString() + ShaderSource.getSourceFor(ShaderSource.deferred_core_vsh),
					macros.toString() + ShaderSource.getSourceFor(ShaderSource.deferred_core_gbuffer_fsh) };
		}
	}

	@Override
	public int getExtensionStatesCount() {
		return 9;
	}

	@Override
	public int getCoreStateMask(int stateExtBits) {
		return DeferredStateManager.enableShadowRender
				? (FixedFunctionShader.FixedFunctionState.STATE_ENABLE_TEXTURE2D
						| FixedFunctionShader.FixedFunctionState.STATE_ENABLE_ALPHA_TEST
						| (DeferredStateManager.enableDrawWavingBlocks
								? FixedFunctionShader.FixedFunctionState.STATE_ENABLE_LIGHTMAP
								: 0))
				: ((DeferredStateManager.enableDrawRealisticWaterMask) ? FixedFunctionShader.FixedFunctionState.STATE_ENABLE_LIGHTMAP
						: (DeferredStateManager.enableDrawRealisticWaterRender
								? (FixedFunctionShader.FixedFunctionState.STATE_ENABLE_LIGHTMAP
										| FixedFunctionShader.FixedFunctionState.STATE_ENABLE_TEXTURE2D)
								: (2943))); 
	}

	@Override
	public int getCurrentExtensionStateBits(int stateCoreBits) {
		return ((DeferredStateManager.enableMaterialMapTexture && !DeferredStateManager.enableShadowRender
				&& !DeferredStateManager.enableDrawRealisticWaterMask
				&& !DeferredStateManager.enableDrawRealisticWaterRender) ? STATE_MATERIAL_TEXTURE : 0) |
				(DeferredStateManager.enableForwardRender ? STATE_FORWARD_RENDER : 0) |
				(DeferredStateManager.enableParaboloidRender ? STATE_PARABOLOID_RENDER : 0) |
				(DeferredStateManager.enableShadowRender ? STATE_SHADOW_RENDER : 0) |
				(DeferredStateManager.enableClipPlane ? STATE_CLIP_PLANE : 0) |
				(DeferredStateManager.enableDrawWavingBlocks ? STATE_WAVING_BLOCKS : 0) |
				(DeferredStateManager.enableDrawRealisticWaterMask ? STATE_REALISTIC_WATER_MASK : 0) |
				(DeferredStateManager.enableDrawRealisticWaterRender ? STATE_REALISTIC_WATER_RENDER : 0) |
				(DeferredStateManager.enableDrawGlassHighlightsRender ? STATE_GLASS_HIGHLIGHTS : 0);
	}

	@Override
	public void initializeNewShader(IProgramGL compiledProg, int stateCoreBits, int stateExtBits,
			Object[] userPointer) {
		EaglercraftGPU.bindGLShaderProgram(compiledProg);
		GBufferExtPipelineShader newShader = new GBufferExtPipelineShader(compiledProg, stateCoreBits, stateExtBits);
		((GBufferPipelineProgramInstance)userPointer[0]).shaderObject = newShader;
		newShader.loadUniforms();
	}

	@Override
	public void updatePipeline(IProgramGL compiledProg, int stateCoreBits, int stateExtBits, Object[] userPointer) {
		int serial;
		GBufferExtPipelineShader.Uniforms uniforms = null;
		if((stateExtBits & STATE_MATERIAL_TEXTURE) == 0) {
			uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			serial = DeferredStateManager.materialConstantsSerial;
			if(uniforms.materialConstantsSerial != serial) {
				uniforms.materialConstantsSerial = serial;
				float roughness = 1.0f - DeferredStateManager.materialConstantsRoughness;
				float metalness = DeferredStateManager.materialConstantsMetalness;
				float emission = DeferredStateManager.materialConstantsEmission;
				if(uniforms.u_materialConstants4f != null) {
					float subsurfScattering = 1.0f - DeferredStateManager.materialConstantsSubsurfScatting;
					if(uniforms.materialConstantsRoughness != roughness || uniforms.materialConstantsMetalness != metalness
							|| uniforms.materialConstantsEmission != emission || uniforms.materialConstantsSubsurfScattering != subsurfScattering) {
						uniforms.materialConstantsRoughness = roughness;
						uniforms.materialConstantsMetalness = metalness;
						uniforms.materialConstantsEmission = emission;
						uniforms.materialConstantsSubsurfScattering = subsurfScattering;
						_wglUniform4f(uniforms.u_materialConstants4f, roughness, metalness, emission, subsurfScattering);
					}
				}else {
					if(uniforms.materialConstantsRoughness != roughness || uniforms.materialConstantsMetalness != metalness
							|| uniforms.materialConstantsEmission != emission) {
						uniforms.materialConstantsRoughness = roughness;
						uniforms.materialConstantsMetalness = metalness;
						uniforms.materialConstantsEmission = emission;
						_wglUniform3f(uniforms.u_materialConstants3f, roughness, metalness, emission);
					}
				}
			}
		}
		if((stateCoreBits & FixedFunctionShader.FixedFunctionState.STATE_HAS_ATTRIB_NORMAL) == 0) {
			if(uniforms == null) {
				uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			}
			int blockId = DeferredStateManager.constantBlock;
			if(uniforms.constantBlock != blockId) {
				uniforms.constantBlock = blockId;
				_wglUniform1f(uniforms.u_blockConstant1f, (blockId - 127) * 0.007874f);
			}
		}
		if((stateExtBits & STATE_CLIP_PLANE) != 0) {
			if(uniforms == null) {
				uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			}
			float clipPlaneYState = DeferredStateManager.clipPlaneY;
			if(uniforms.clipPlaneY != clipPlaneYState) {
				uniforms.clipPlaneY = clipPlaneYState;
				_wglUniform1f(uniforms.u_clipPlaneY1f, clipPlaneYState);
			}
		}
		if((stateExtBits & STATE_WAVING_BLOCKS) != 0) {
			if(uniforms == null) {
				uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			}
			serial = DeferredStateManager.passViewMatrixSerial;
			boolean modelDirty = false;
			if(serial != uniforms.viewMatrixSerial) {
				uniforms.viewMatrixSerial = serial;
				matrixCopyBuffer.clear();
				DeferredStateManager.passViewMatrix.store(matrixCopyBuffer);
				matrixCopyBuffer.flip();
				_wglUniformMatrix4fv(uniforms.u_viewMatrix4f, false, matrixCopyBuffer);
				modelDirty = true;
			}
			serial = GlStateManager.getModelViewSerial();
			if(uniforms.modelMatrixSerial != serial || modelDirty) {
				uniforms.modelMatrixSerial = serial;
				Matrix4f mat = GlStateManager.getModelViewReference();
				matrixCopyBuffer.clear();
				if(!DeferredStateManager.isShadowPassMatrixLoaded) {
					Matrix4f.mul(DeferredStateManager.passInverseViewMatrix, mat, tmpMatrix);
					tmpMatrix.store(matrixCopyBuffer);
				}else {
					mat.store(matrixCopyBuffer);
				}
				matrixCopyBuffer.flip();
				_wglUniformMatrix4fv(uniforms.u_modelMatrix4f, false, matrixCopyBuffer);
			}
			serial = DeferredStateManager.wavingBlockOffsetSerial;
			if(serial != uniforms.wavingBlockOffsetSerial) {
				uniforms.wavingBlockOffsetSerial = serial;
				float x = DeferredStateManager.wavingBlockOffsetX;
				float y = DeferredStateManager.wavingBlockOffsetY;
				float z = DeferredStateManager.wavingBlockOffsetZ;
				if(uniforms.wavingBlockOffsetX != x || uniforms.wavingBlockOffsetY != y || uniforms.wavingBlockOffsetZ != z) {
					uniforms.wavingBlockOffsetX = x;
					uniforms.wavingBlockOffsetY = y;
					uniforms.wavingBlockOffsetZ = z;
					_wglUniform3f(uniforms.u_wavingBlockOffset3f, x, y, z);
				}
			}
			serial = DeferredStateManager.wavingBlockParamSerial;
			if(serial != uniforms.wavingBlockParamSerial) {
				uniforms.wavingBlockParamSerial = serial;
				float x = DeferredStateManager.wavingBlockParamX;
				float y = DeferredStateManager.wavingBlockParamY;
				float z = DeferredStateManager.wavingBlockParamZ;
				float w = DeferredStateManager.wavingBlockParamW;
				if(uniforms.wavingBlockParamX != x || uniforms.wavingBlockParamY != y || uniforms.wavingBlockParamZ != z || uniforms.wavingBlockParamW != w) {
					uniforms.wavingBlockParamX = x;
					uniforms.wavingBlockParamY = y;
					uniforms.wavingBlockParamZ = z;
					uniforms.wavingBlockParamW = w;
					_wglUniform4f(uniforms.u_wavingBlockParam4f, x, y, z, w);
				}
			}
		}
		if((stateExtBits & STATE_FORWARD_RENDER) != 0) {
			if(uniforms == null) {
				uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			}
			serial = DeferredStateManager.passViewMatrixSerial;
			if(serial != uniforms.inverseViewMatrixSerial) {
				uniforms.inverseViewMatrixSerial = serial;
				matrixCopyBuffer.clear();
				DeferredStateManager.passInverseViewMatrix.store(matrixCopyBuffer);
				matrixCopyBuffer.flip();
				_wglUniformMatrix4fv(uniforms.u_inverseViewMatrix4f, false, matrixCopyBuffer);
			}
			if((stateExtBits & STATE_PARABOLOID_RENDER) != 0) {
				float farPlane = DeferredStateManager.gbufferFarPlane * 0.125f; //TODO
				if(farPlane != uniforms.farPlane1f) {
					uniforms.farPlane1f = farPlane;
					_wglUniform1f(uniforms.u_farPlane1f, farPlane);
				}
			}
			if((stateExtBits & STATE_REALISTIC_WATER_RENDER) != 0) {
				serial = DeferredStateManager.passViewMatrixSerial * 87917 + DeferredStateManager.passProjMatrixSerial;
				if(serial != uniforms.modelViewProjMatrixAltSerial) {
					uniforms.modelViewProjMatrixAltSerial = serial;
					Matrix4f.mul(DeferredStateManager.passProjMatrix, DeferredStateManager.passViewMatrix, tmpMatrix);
					matrixCopyBuffer.clear();
					tmpMatrix.store(matrixCopyBuffer);
					matrixCopyBuffer.flip();
					_wglUniformMatrix4fv(uniforms.u_modelViewProjMat4f_, false, matrixCopyBuffer);
				}
				serial = DeferredStateManager.waterWindOffsetSerial;
				if(serial != uniforms.waterWindOffsetSerial) {
					uniforms.waterWindOffsetSerial = serial;
					Vector4f vec = DeferredStateManager.u_waterWindOffset4f;
					_wglUniform4f(uniforms.u_waterWindOffset4f, vec.x, vec.y, vec.z, vec.w);
				}
				serial = DeferredStateManager.wavingBlockOffsetSerial;
				if(serial != uniforms.wavingBlockOffsetSerial) {
					uniforms.wavingBlockOffsetSerial = serial;
					float x = DeferredStateManager.wavingBlockOffsetX;
					float y = DeferredStateManager.wavingBlockOffsetY;
					float z = DeferredStateManager.wavingBlockOffsetZ;
					if(uniforms.wavingBlockOffsetX != x || uniforms.wavingBlockOffsetY != y || uniforms.wavingBlockOffsetZ != z) {
						uniforms.wavingBlockOffsetX = x;
						uniforms.wavingBlockOffsetY = y;
						uniforms.wavingBlockOffsetZ = z;
						_wglUniform3f(uniforms.u_wavingBlockOffset3f, x, y, z);
					}
				}
			}
		}else if((stateExtBits & (STATE_SHADOW_RENDER | STATE_REALISTIC_WATER_MASK)) == 0) {
			if(uniforms == null) {
				uniforms = ((GBufferPipelineProgramInstance)userPointer[0]).shaderObject.uniforms;
			}
			if(uniforms.u_useEnvMap1f != null) {
				float use = DeferredStateManager.materialConstantsUseEnvMap ? 1.0f : 0.0f;
				if(uniforms.materialConstantsUseEnvMap != use) {
					uniforms.materialConstantsUseEnvMap = use;
					_wglUniform1f(uniforms.u_useEnvMap1f, use);
				}
			}
		}
	}

	@Override
	public void destroyPipeline(IProgramGL shaderProgram, int stateCoreBits, int stateExtBits, Object[] userPointer) {
		
	}

	private static String visualizeBits(int bits) {
		return FixedFunctionPipeline.visualizeBits(bits);
	}
}