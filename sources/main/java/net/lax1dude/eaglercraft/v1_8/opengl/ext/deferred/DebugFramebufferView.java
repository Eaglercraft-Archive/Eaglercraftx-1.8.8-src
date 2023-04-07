package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderGBufferDebugView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class DebugFramebufferView {

	public static boolean debugViewShown = false;
	private static long debugViewNameTimer = 0l;
	private static int currentDebugView = 0;

	public static final List<DebugFramebufferView> views = Arrays.asList(
			(new DebugFramebufferView("GBuffer: Diffuse Color", (pipeline) -> {
				pipeline.useDebugViewShader(0);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferDiffuseTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("GBuffer: Normal Vectors", (pipeline) -> {
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(1);
				EaglerDeferredPipeline.uniformMatrixHelper(dbv.uniforms.u_inverseViewMatrix, DeferredStateManager.inverseViewMatrix);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferNormalsTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("GBuffer: Block/Sky Light Values", (pipeline) -> {
				pipeline.useDebugViewShader(2);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				GlStateManager.bindTexture(pipeline.gBufferNormalsTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferDiffuseTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("GBuffer: Material Params (1)", (pipeline) -> {
				pipeline.useDebugViewShader(0);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferMaterialTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("GBuffer: Material Params (2)", (pipeline) -> {
				pipeline.useDebugViewShader(3);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferMaterialTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("GBuffer: Depth Buffer", (pipeline) -> {
				float depthStart = 0.001f;
				float depthScale = 25.0f;
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(4);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, depthStart, depthScale);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.gBufferDepthTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Sun Shadow Depth: LOD 1", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 1) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(5);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, 1.0f / pipeline.config.is_rendering_shadowsSun_clamped, 0.0f);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunShadowDepthBuffer);
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, GL_NONE);
				DrawUtils.drawStandardQuad2D();
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			})),
			(new DebugFramebufferView("Sun Shadow Color: LOD 1", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 1 || !pipeline.config.is_rendering_shadowsColored) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(10);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, 1.0f / pipeline.config.is_rendering_shadowsSun_clamped, 0.0f);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				GlStateManager.bindTexture(pipeline.sunShadowColorBuffer);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunShadowDepthBuffer);
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, GL_NONE);
				DrawUtils.drawStandardQuad2D();
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			})),
			(new DebugFramebufferView("Sun Shadow Depth: LOD 2", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 2) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(5);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, 1.0f / pipeline.config.is_rendering_shadowsSun_clamped, 1.0f);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunShadowDepthBuffer);
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, GL_NONE);
				DrawUtils.drawStandardQuad2D();
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			})),
			(new DebugFramebufferView("Sun Shadow Color: LOD 2", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 2 || !pipeline.config.is_rendering_shadowsColored) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(10);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, 1.0f / pipeline.config.is_rendering_shadowsSun_clamped, 1.0f);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				GlStateManager.bindTexture(pipeline.sunShadowColorBuffer);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunShadowDepthBuffer);
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, GL_NONE);
				DrawUtils.drawStandardQuad2D();
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			})),
			(new DebugFramebufferView("Sun Shadow Depth: LOD 3", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 3) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(5);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, 1.0f / pipeline.config.is_rendering_shadowsSun_clamped, 2.0f);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunShadowDepthBuffer);
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, GL_NONE);
				DrawUtils.drawStandardQuad2D();
				_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			})),
			(new DebugFramebufferView("GBuffer Shadow Values", (pipeline) -> {
				if(pipeline.config.is_rendering_shadowsSun_clamped < 1) throw new NoDataException();
				if(pipeline.config.is_rendering_shadowsColored) {
					pipeline.useDebugViewShader(0);
				}else {
					pipeline.useDebugViewShader(6);
				}
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunLightingShadowTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Light Shafts Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_lightShafts) throw new NoDataException();
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lightShaftsTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Forward Render Mask", (pipeline) -> {
				pipeline.useDebugViewShader(7);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lightingHDRFramebufferColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Final HDR Color Buffer", (pipeline) -> {
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lightingHDRFramebufferColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Final Depth Buffer", (pipeline) -> {
				float depthStart = 0.001f;
				float depthScale = 25.0f;
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(4);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, depthStart, depthScale);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lightingHDRFramebufferDepthTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Last Frame Color Buffer", (pipeline) -> {
				if(!pipeline.reprojectionEngineEnable && !pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lastFrameColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Last Frame Depth Buffer", (pipeline) -> {
				if(!pipeline.reprojectionEngineEnable && !pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				float depthStart = 0.001f;
				float depthScale = 25.0f;
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(4);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, depthStart, depthScale);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lastFrameDepthTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSAO: Raw GBuffer Samples", (pipeline) -> {
				if(!pipeline.config.is_rendering_ssao) throw new NoDataException();
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.ssaoGenerateTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSAO: Reprojected Samples", (pipeline) -> {
				if(!pipeline.config.is_rendering_ssao) throw new NoDataException();
				pipeline.useDebugViewShader(9);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionControlSSAOTexture[pipeline.reprojectionPhase]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSAO: History Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_ssao) throw new NoDataException();
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionControlSSAOTexture[pipeline.reprojectionPhase]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSR: Reflection Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_raytracing) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionSSRTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSR: Reflection Traces", (pipeline) -> {
				if(!pipeline.config.is_rendering_raytracing) throw new NoDataException();
				pipeline.useDebugViewShader(11);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionSSRTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSR: Reflection Hit Vectors", (pipeline) -> {
				if(!pipeline.config.is_rendering_raytracing) throw new NoDataException();
				pipeline.useDebugViewShader(12);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionSSRHitVector[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSR: Reflection Hit Mask", (pipeline) -> {
				if(!pipeline.config.is_rendering_raytracing) throw new NoDataException();
				pipeline.useDebugViewShader(13);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionSSRHitVector[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("SSR: History Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_raytracing) throw new NoDataException();
				pipeline.useDebugViewShader(11);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.reprojectionSSRHitVector[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Clouds 3D Noise Map", (pipeline) -> {
				PipelineShaderGBufferDebugView dbg = pipeline.useDebugViewShader(18);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture3D(CloudRenderWorker.cloud3DSamplesTexture);
				_wglUniform1f(_wglGetUniformLocation(dbg.program, "u_fuckU1f"), (float)((System.currentTimeMillis() % 5000l) / 5000.0));
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Clouds Back Buffer", (pipeline) -> {
				pipeline.useDebugViewShader(0);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(CloudRenderWorker.cloudNoiseSampleParaboloidTexture[CloudRenderWorker.cloudRenderPhase]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Clouds Front Buffer", (pipeline) -> {
				pipeline.useDebugViewShader(0);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				CloudRenderWorker.bindParaboloid();
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Cached Atmosphere Colors", (pipeline) -> {
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.atmosphereHDRFramebufferColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Dual Paraboloid Map: Atmosphere", (pipeline) -> {
				pipeline.useDebugViewShader(14);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.envMapAtmosphereTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Dual Paraboloid Map: Skybox", (pipeline) -> {
				pipeline.useDebugViewShader(14);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.envMapSkyTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Dual Paraboloid Map: Terrain", (pipeline) -> {
				if(!pipeline.config.is_rendering_useEnvMap) throw new NoDataException();
				pipeline.useDebugViewShader(14);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.envMapColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Dual Paraboloid Map: Mask", (pipeline) -> {
				if(!pipeline.config.is_rendering_useEnvMap) throw new NoDataException();
				pipeline.useDebugViewShader(15);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.envMapColorTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Skybox Irradiance Map", (pipeline) -> {
				pipeline.useDebugViewShader(14);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.skyIrradianceTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Atmosphere Irradiance Map", (pipeline) -> {
				pipeline.useDebugViewShader(14);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.atmosphereIrradianceTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: Surface Normals", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(1);
				EaglerDeferredPipeline.uniformMatrixHelper(dbv.uniforms.u_inverseViewMatrix, DeferredStateManager.inverseViewMatrix);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterMaskTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: Surface Depth", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				float depthStart = 0.001f;
				float depthScale = 25.0f;
				PipelineShaderGBufferDebugView dbv = pipeline.useDebugViewShader(4);
				_wglUniform2f(dbv.uniforms.u_depthSliceStartEnd2f, depthStart, depthScale);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterDepthBuffer);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: Distortion Map", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(16);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterNormalMapTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: Refraction Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterRefractionTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: SSR Reflect Buffer", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterControlReflectionTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: SSR Reflect Traces", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(11);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterControlReflectionTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: SSR Reflect Hit Vectors", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(12);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterControlHitVectorTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: SSR Reflect Hit Mask", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(13);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterControlHitVectorTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Water: SSR Reflect History", (pipeline) -> {
				if(!pipeline.config.is_rendering_realisticWater) throw new NoDataException();
				pipeline.useDebugViewShader(11);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.realisticWaterControlHitVectorTexture[1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Exposure Average -2", (pipeline) -> {
				pipeline.useDebugViewShader(17);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lumaAvgDownscaleTexture[pipeline.lumaAvgDownscaleFramebuffers.length - 2]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Exposure Average -1", (pipeline) -> {
				pipeline.useDebugViewShader(17);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.lumaAvgDownscaleTexture[pipeline.lumaAvgDownscaleFramebuffers.length - 1]);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Dynamic Exposure Value", (pipeline) -> {
				pipeline.useDebugViewShader(17);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.exposureBlendTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Bloom Bright Pass", (pipeline) -> {
				if(!pipeline.config.is_rendering_bloom) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.bloomBrightPassTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Bloom Horz. Blur", (pipeline) -> {
				if(!pipeline.config.is_rendering_bloom) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.bloomHBlurTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Bloom Vert. Blur", (pipeline) -> {
				if(!pipeline.config.is_rendering_bloom) throw new NoDataException();
				pipeline.useDebugViewShader(8);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.bloomVBlurTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Sun Occlusion: World", (pipeline) -> {
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(CloudRenderWorker.cloudOcclusionTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("Sun Occlusion: Screen", (pipeline) -> {
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.sunOcclusionValueTexture);
				DrawUtils.drawStandardQuad2D();
			})),
			(new DebugFramebufferView("FXAA Luma Values", (pipeline) -> {
				if(!pipeline.config.is_rendering_fxaa) throw new NoDataException();
				pipeline.useDebugViewShader(6);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(pipeline.tonemapOutputTexture);
				DrawUtils.drawStandardQuad2D();
			}))
		);

	private static class NoDataException extends RuntimeException {
	}

	public static void renderDebugView() {
		Minecraft mc = Minecraft.getMinecraft();
		boolean noData = false;
		DebugFramebufferView view = views.get(currentDebugView);
		try {
			view.renderHandler.accept(EaglerDeferredPipeline.instance);
		}catch(NoDataException ex) {
			GlStateManager.clearColor(0.0f, 0.0f, 0.1f, 0.0f);
			GlStateManager.clear(GL_COLOR_BUFFER_BIT);
			noData = true;
		}
		long millis = System.currentTimeMillis();
		long elapsed = millis - debugViewNameTimer;
		if(elapsed < 2000l || noData) {
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.pushMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.pushMatrix();
			ScaledResolution scaledresolution = new ScaledResolution(mc);
			int w = scaledresolution.getScaledWidth();
			mc.entityRenderer.setupOverlayRendering();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			int h = scaledresolution.getScaledHeight() / 2;
			
			if(noData) {
				String noDataTxt = "No Data";
				mc.entityRenderer.setupOverlayRendering();
				int viewNameWidth = mc.fontRendererObj.getStringWidth(noDataTxt) * 2;
				GlStateManager.pushMatrix();
				GlStateManager.translate((w - viewNameWidth) * 0.5f, h - 70.0f, 0.0f);
				GlStateManager.scale(2.0f, 2.0f, 2.0f);
				mc.fontRendererObj.drawStringWithShadow(noDataTxt, 0, 0, 0xFFFFFFFF);
				GlStateManager.popMatrix();
			}
			
			if(elapsed < 2000l) {
				for(int i = 0; i < 9; ++i) {
					int i2 = currentDebugView - 4 + i;
					if(i2 >= 0 && i2 < views.size()) {
						String str = views.get(i2).name;
						int j = mc.fontRendererObj.getStringWidth(str);
						float alphaF = ((i == 0 || i == 8) ? 0.25f : ((i == 1 || i == 7) ? 0.65f : 1.0f));
						int x = 5;
						if(elapsed > 1800l) {
							x -= (int)(elapsed - 1800l);
							alphaF *= (1.0f - (float)(elapsed - 1800l) / 190.0f);
						}
						int y = h + (i - 5) * 11;
						Gui.drawRect(x, y, x + j + 2, y + 10, (int)(alphaF * 127.0f) << 24);
						mc.fontRendererObj.drawStringWithShadow(str, x + 1, y + 1, (i == 4 ? 0xFFFF00 : 0xFFFFFF) | ((int)(alphaF * 255.0f) << 24));
					}
				}

				mc.fontRendererObj.drawStringWithShadow("Use arrow keys to select framebuffers", 5, 23, 0xFFFFFF);
				mc.fontRendererObj.drawStringWithShadow("Press F+4 to exit", 5, 33, 0xFFFFFF);
			}
			
			GlStateManager.disableBlend();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
		}
	}

	public static void toggleDebugView() {
		debugViewShown = !debugViewShown;
		if(debugViewShown) {
			debugViewNameTimer = System.currentTimeMillis();
		}
	}

	public static void switchView(int dir) {
		if(!debugViewShown) return;
		debugViewNameTimer = System.currentTimeMillis();
		currentDebugView += dir;
		if(currentDebugView < 0) currentDebugView = views.size() - 1;
		if(currentDebugView >= views.size()) currentDebugView = 0;
	}

	protected final String name;
	protected final Consumer<EaglerDeferredPipeline> renderHandler;

	protected DebugFramebufferView(String name, Consumer<EaglerDeferredPipeline> renderHandler) {
		this.name = name;
		this.renderHandler = renderHandler;
	}

}
