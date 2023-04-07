package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.TextureCopyUtil;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderBloomBlurPass;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderBloomBrightPass;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderFXAA;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderGBufferCombine;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderGBufferDebugView;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderGBufferFog;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderHandDepthMask;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLensDistortion;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLensSunOcclusion;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLightShaftsSample;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLightingPoint;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLightingSun;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderMoonRender;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderPostExposureAvg;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderPostExposureFinal;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderRealisticWaterControl;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderRealisticWaterNoise;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderRealisticWaterNormalMap;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderReprojControl;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderReprojSSR;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderSSAOGenerate;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderShadowsSun;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderSkyboxAtmosphere;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderSkyboxIrradiance;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderSkyboxRender;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderSkyboxRenderEnd;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderTonemap;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.MetalsLUT;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.TemperaturesLUT;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
public class EaglerDeferredPipeline {

	public static final Logger logger = LogManager.getLogger("EaglerDeferredPipeline");

	public static final byte[] ditherPattern = new byte[] { (byte) 12, (byte) 139, (byte) 44, (byte) 171, (byte) 4,
			(byte) 131, (byte) 36, (byte) 163, (byte) 239, (byte) 112, (byte) 207, (byte) 80, (byte) 247, (byte) 120,
			(byte) 215, (byte) 88, (byte) 48, (byte) 175, (byte) 16, (byte) 143, (byte) 56, (byte) 183, (byte) 24,
			(byte) 151, (byte) 191, (byte) 64, (byte) 223, (byte) 96, (byte) 199, (byte) 72, (byte) 231, (byte) 104,
			(byte) 0, (byte) 127, (byte) 32, (byte) 159, (byte) 8, (byte) 135, (byte) 40, (byte) 167, (byte) 251,
			(byte) 124, (byte) 219, (byte) 92, (byte) 243, (byte) 116, (byte) 211, (byte) 84, (byte) 60, (byte) 187,
			(byte) 28, (byte) 155, (byte) 52, (byte) 179, (byte) 20, (byte) 147, (byte) 203, (byte) 76, (byte) 235,
			(byte) 108, (byte) 195, (byte) 68, (byte) 227, (byte) 100 };

	public static EaglerDeferredPipeline instance = null;
	public static boolean isSuspended = false;

	public EaglerDeferredConfig config = null;
	public GBufferPipelineCompiler deferredExtPipeline = new GBufferPipelineCompiler();

	public final Minecraft mc;

	public int currentWidth = -1;
	public int currentHeight = -1;

	public double currentRenderX = 0.0;
	public double currentRenderY = 0.0;
	public double currentRenderZ = 0.0;

	public IFramebufferGL gBufferFramebuffer = null;

	public int gBufferDiffuseTexture = -1;
	public int gBufferNormalsTexture = -1;
	public int gBufferMaterialTexture = -1;

	public IFramebufferGL gBufferQuarterFramebuffer = null;
	public int gBufferQuarterDepthTexture = -1;

	public int[] gBufferDrawBuffers = null;

	public int gBufferDepthTexture = -1;

	public IFramebufferGL lastFrameGBufferFramebuffer = null;
	public int lastFrameGBufferDepthTexture = -1;

	public IFramebufferGL lastFrameFramebuffer = null;
	public int lastFrameColorTexture = -1;
	public int lastFrameDepthTexture = -1;

	public IFramebufferGL fogDepthCopyBuffer = null;
	public int fogDepthCopyTexture = -1;

	public IFramebufferGL sunShadowFramebuffer = null;
	public IFramebufferGL sunShadowColorFramebuffer = null;

	public int sunShadowDepthBuffer = -1;
	public int sunShadowDepthBufferRes = -1;
	public int sunShadowColorBuffer = -1;

	public IFramebufferGL sunLightingShadowFramebuffer = null;
	public int sunLightingShadowTexture = -1;

	public IFramebufferGL ssaoGenerateFramebuffer = null;
	public int ssaoGenerateTexture = -1;

	private int reprojectionStartup = 0;

	public int ssaoNoiseTexture = -1;

	public IFramebufferGL lightingHDRFramebuffer = null;
	public int lightingHDRFramebufferColorTexture = -1;
	public int lightingHDRFramebufferDepthTexture = -1;

	public IFramebufferGL handRenderFramebuffer = null;
	public int handRenderFramebufferDepthTexture = -1;

	public IFramebufferGL[] reprojectionControlFramebuffer = new IFramebufferGL[] { null, null };
	public int[] reprojectionControlDrawBuffers = new int[0];
	public int[] reprojectionControlSSAOTexture = new int[] { -1, -1 };

	public int reprojectionPhase = 0;

	public IFramebufferGL[] reprojectionSSRFramebuffer = new IFramebufferGL[] { null, null };
	public int[] reprojectionSSRTexture = new int[] { -1, -1 };
	public int[] reprojectionSSRHitVector = new int[] { -1, -1 };
	public boolean reprojectionEngineEnable = false;

	private static final int[] SSRColorAttachments = new int[] { _GL_COLOR_ATTACHMENT0, _GL_COLOR_ATTACHMENT1 };

	public IFramebufferGL lightShaftsFramebuffer = null;
	public int lightShaftsTexture = -1;

	public IFramebufferGL atmosphereHDRFramebuffer = null;
	public int atmosphereHDRFramebufferColorTexture = -1;

	public IFramebufferGL envMapAtmosphereFramebuffer = null;
	public int envMapAtmosphereTexture = -1;

	public IFramebufferGL envMapSkyFramebuffer = null;
	public int envMapSkyTexture = -1;
	public IFramebufferGL envMapFramebuffer = null;
	public int envMapColorTexture = -1;
	public int envMapDepthTexture = -1;

	public int moonTextures = -1;

	public int irradiancePhase = 0;

	public IFramebufferGL atmosphereIrradianceFramebuffer = null;
	public int atmosphereIrradianceTexture = -1;

	public IFramebufferGL skyIrradianceFramebuffer = null;
	public int skyIrradianceTexture = -1;

	public IFramebufferGL realisticWaterMaskFramebuffer = null;
	public int realisticWaterMaskTexture = -1;
	public int realisticWaterDepthBuffer = -1;

	public IFramebufferGL realisticWaterCombinedNormalsFramebuffer = null;
	public int realisticWaterCombinedNormalsTexture = -1;

	public IFramebufferGL realisticWaterDisplacementMapFramebuffer = null;
	public int realisticWaterDisplacementMapTexture = -1;
	public int realisticWaterNoiseMap = -1;

	public IFramebufferGL realisticWaterNormalMapFramebuffer = null;
	public int realisticWaterNormalMapTexture = -1;

	public IFramebufferGL realisticWaterControlFramebuffer = null;
	public IFramebufferGL[] realisticWaterSSRFramebuffer = new IFramebufferGL[2];
	public int[] realisticWaterControlReflectionTexture = new int[] { -1, -1 };
	public int[] realisticWaterControlHitVectorTexture = new int[] { -1, -1 };
	public int realisticWaterRefractionTexture = -1;

	public IFramebufferGL[] lumaAvgDownscaleFramebuffers = null;
	public int[] lumaAvgDownscaleTexture = null;

	public IFramebufferGL exposureBlendFramebuffer = null;
	public int exposureBlendTexture = -1;

	public IFramebufferGL sunOcclusionValueFramebuffer = null;
	public int sunOcclusionValueTexture = -1;

	public int dither8x8Texture = -1;

	public IFramebufferGL bloomBrightPassFramebuffer = null;
	public int bloomBrightPassTextureW = -1;
	public int bloomBrightPassTextureH = -1;
	public int bloomBrightPassTexture = -1;

	public IFramebufferGL bloomDownscaleAFramebuffer = null;
	public int bloomDownscaleATextureW = -1;
	public int bloomDownscaleATextureH = -1;
	public int bloomDownscaleATexture = -1;

	public IFramebufferGL bloomDownscaleBFramebuffer = null;
	public int bloomDownscaleBTextureW = -1;
	public int bloomDownscaleBTextureH = -1;
	public int bloomDownscaleBTexture = -1;

	public IFramebufferGL bloomHBlurFramebuffer = null;
	public int bloomBlurTextureW = -1;
	public int bloomBlurTextureH = -1;
	public int bloomHBlurTexture = -1;

	public IFramebufferGL bloomVBlurFramebuffer = null;
	public int bloomVBlurTexture = -1;

	public IFramebufferGL lensDistortFramebuffer = null;
	public int lensDistortTexture = -1;

	public IFramebufferGL tonemapOutputFramebuffer = null;
	public int tonemapOutputTexture = -1;

	public PipelineShaderSSAOGenerate shader_ssao_generate = null;
	private int reprojectionTexWidth = -1;
	private int reprojectionTexHeight = -1;
	public PipelineShaderGBufferCombine shader_deferred_combine = null;
	public int brdfTexture = -1;
	public PipelineShaderLightingSun shader_lighting_sun = null;
	public PipelineShaderShadowsSun shader_shadows_sun = null;
	public PipelineShaderLightShaftsSample shader_light_shafts_sample = null;
	public PipelineShaderTonemap shader_post_tonemap = null;
	public PipelineShaderLensDistortion shader_post_lens_distort = null;
	public PipelineShaderPostExposureAvg shader_post_exposure_avg = null;
	public PipelineShaderPostExposureAvg shader_post_exposure_avg_luma = null;
	public PipelineShaderPostExposureFinal shader_post_exposure_final = null;
	public PipelineShaderBloomBrightPass shader_post_bloom_bright = null;
	public PipelineShaderBloomBlurPass shader_post_bloom_blur = null;
	public PipelineShaderLensSunOcclusion shader_lens_sun_occlusion = null;
	public PipelineShaderSkyboxAtmosphere shader_skybox_atmosphere = null;
	public PipelineShaderSkyboxIrradiance[] shader_skybox_irradiance = new PipelineShaderSkyboxIrradiance[3];
	public PipelineShaderSkyboxRender shader_skybox_render = null;
	public PipelineShaderSkyboxRender shader_skybox_render_paraboloid = null;
	public PipelineShaderSkyboxRender shader_skybox_render_paraboloid_noclouds = null;
	public PipelineShaderSkyboxRenderEnd shader_skybox_render_end = null;
	public PipelineShaderGBufferFog shader_colored_fog_linear = null;
	public PipelineShaderGBufferFog shader_colored_fog_exp = null;
	public PipelineShaderGBufferFog shader_atmosphere_fog = null;
	public PipelineShaderMoonRender shader_moon_render = null;
	public PipelineShaderLightingPoint shader_lighting_point = null;
	public PipelineShaderReprojControl shader_reproject_control = null;
	public PipelineShaderReprojSSR shader_reproject_ssr = null;
	public PipelineShaderRealisticWaterControl shader_realistic_water_control = null;
	public PipelineShaderRealisticWaterNoise shader_realistic_water_noise = null;
	public PipelineShaderRealisticWaterNormalMap shader_realistic_water_normals = null;
	public PipelineShaderHandDepthMask shader_hand_depth_mask = null;
	public PipelineShaderFXAA shader_post_fxaa = null;
	public SkyboxRenderer skybox = null;
	public LightSourceMesh pointLightMesh = null;
	public final GBufferAcceleratedEffectRenderer gbufferEffectRenderer = new GBufferAcceleratedEffectRenderer();
	public final ForwardAcceleratedEffectRenderer forwardEffectRenderer = new ForwardAcceleratedEffectRenderer();

	public final PipelineShaderGBufferDebugView[] shader_gbuffer_debug_view = new PipelineShaderGBufferDebugView[19];

	public final EaglercraftRandom random = new EaglercraftRandom();

	public static FloatBuffer matrixCopyBuffer = null;

	public IBufferGL buffer_worldLightingData;
	private ByteBuffer worldLightingDataCopyBuffer;

	public IBufferGL buffer_chunkLightingData;
	private ByteBuffer chunkLightingDataCopyBuffer;
	private boolean isChunkLightingEnabled = false;
	public ListSerial<DynamicLightInstance> currentBoundLightSourceBucket;

	public static final Matrix4f tmpMatrixViewProj = new Matrix4f();
	public static final Matrix4f tmpMatrixViewReproject = new Matrix4f();
	public static final Matrix4f tmpMatrixViewProjReproject = new Matrix4f();
	public static final Matrix4f tmpMatrixLastFrameViewReproject = new Matrix4f();
	public static final Matrix4f tmpMatrixLastFrameProj = new Matrix4f();
	public static final Matrix4f tmpMatrixLastFrameViewProjReproject = new Matrix4f();
	public static final Matrix4f tmpMatrixInverseViewProj = new Matrix4f();
	public static final Matrix4f tmpMatrixInverseViewProjReproject = new Matrix4f();
	public static final Matrix4f tmpShadowLOD0MatrixTexSpace = new Matrix4f();
	public static final Matrix4f tmpShadowLOD1MatrixTexSpace = new Matrix4f();
	public static final Matrix4f tmpShadowLOD2MatrixTexSpace = new Matrix4f();
	public static final Vector3f vec3_05 = new Vector3f(0.5f, 0.5f, 0.5f);
	public static final Vector3f vec3_2 = new Vector3f(2.0f, 2.0f, 2.0f);
	public static final Vector3f vec3_n1 = new Vector3f(-1.0f, -1.0f, -1.0f);
	public static final Matrix4f tmpClipToTexSpaceMatLeft = new Matrix4f().translate(vec3_05).scale(vec3_05);
	public static final Matrix4f tmpTexToClipSpaceMatRight = new Matrix4f().translate(vec3_n1).scale(vec3_2);
	public static final Matrix4f tmpMatrix1 = new Matrix4f();
	public static final Matrix4f tmpMatrix2 = new Matrix4f();
	public static final Matrix3f tmpMatrix3 = new Matrix3f();
	public static final Vector3f tmpVector1 = new Vector3f();
	public static final Vector4f tmpVector2 = new Vector4f();
	public static final Vector3f tmpVector3 = new Vector3f();
	public static final Vector3f tmpVector4 = new Vector3f();

	public final ListSerial<DynamicLightInstance>[] lightSourceBuckets;
	public ListSerial<DynamicLightInstance> currentLightSourceBucket;

	public static final int MAX_LIGHTS_PER_CHUNK = 12;

	private final int lightSourceBucketsWidth;
	private final int lightSourceBucketsHeight;

	private double reprojectionOriginCoordinateX = 0.0;
	private double reprojectionOriginCoordinateY = 0.0;
	private double reprojectionOriginCoordinateZ = 0.0;

	private float reprojectionViewerOffsetX = 0.0f;
	private float reprojectionViewerOffsetY = 0.0f;
	private float reprojectionViewerOffsetZ = 0.0f;

	private double cloudRenderOriginCoordinateX = 0.0;
	private double cloudRenderOriginCoordinateZ = 0.0;

	private float cloudRenderViewerOffsetX = 0.0f;
	private float cloudRenderViewerOffsetZ = 0.0f;

	private long recalcAtmosphereTimer = 0l;

	private long lastExposureUpdate = 0l;

	private float partialTicks = 0.0f;

	public EaglerDeferredPipeline(Minecraft mc) {
		this.mc = mc;
		if(matrixCopyBuffer == null) {
			matrixCopyBuffer = GLAllocation.createDirectFloatBuffer(16);
		}
		this.lightSourceBucketsWidth = 5;
		this.lightSourceBucketsHeight = 3;
		int cnt = 5 * 3 * 5;
		this.lightSourceBuckets = new ListSerial[cnt];
		for(int i = 0; i < cnt; ++i) {
			this.lightSourceBuckets[i] = new ArrayListSerial(16);
		}
	}

	public void rebuild(EaglerDeferredConfig config) {
		destroy();
		DeferredStateManager.checkGLError("Pre: rebuild pipeline");
		this.config = config;
		this.currentWidth = -1;
		this.currentHeight = -1;
		logger.info("Rebuilding pipeline...");

		gBufferFramebuffer = _wglCreateFramebuffer();

		_wglBindFramebuffer(_GL_FRAMEBUFFER, gBufferFramebuffer);

		gBufferDiffuseTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(gBufferDiffuseTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(gBufferDiffuseTexture), 0);
		gBufferNormalsTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(gBufferNormalsTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(gBufferNormalsTexture), 0);
		gBufferMaterialTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(gBufferMaterialTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(gBufferMaterialTexture), 0);
		gBufferDrawBuffers = new int[] { _GL_COLOR_ATTACHMENT0, _GL_COLOR_ATTACHMENT1, _GL_COLOR_ATTACHMENT2 };
		_wglDrawBuffers(gBufferDrawBuffers);

		gBufferDepthTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(gBufferDepthTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(gBufferDepthTexture), 0);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: gbuffers");

		boolean shadowsSun = config.is_rendering_shadowsSun_clamped > 0;
		if(shadowsSun) {
			sunShadowFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowFramebuffer);
			sunShadowDepthBuffer = GlStateManager.generateTexture();
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			setNearest();
			_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_FUNC, GL_GREATER);
			_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			int lods = config.is_rendering_shadowsSun_clamped;
			if(lods > 3) {
				lods = 3;
			}
			sunShadowDepthBufferRes = 2048;
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT24, sunShadowDepthBufferRes, sunShadowDepthBufferRes * lods, 0, _GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, (ByteBuffer)null);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(sunShadowDepthBuffer), 0);
			sunLightingShadowFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunLightingShadowFramebuffer);
			sunLightingShadowTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(sunLightingShadowTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(sunLightingShadowTexture), 0);
			if(config.is_rendering_shadowsColored) {
				sunShadowColorFramebuffer = _wglCreateFramebuffer();
				_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowColorFramebuffer);
				GlStateManager.bindTexture(sunShadowDepthBuffer);
				_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(sunShadowDepthBuffer), 0);
				sunShadowColorBuffer = GlStateManager.generateTexture();
				GlStateManager.bindTexture(sunShadowColorBuffer);
				setNearest();
				_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, sunShadowDepthBufferRes, sunShadowDepthBufferRes * lods, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
				_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(sunShadowColorBuffer), 0);
			}

			DeferredStateManager.checkGLError("Post: rebuild pipeline: shadowsSun");
		}

		reprojectionEngineEnable = config.is_rendering_ssao || config.is_rendering_raytracing;
		if(reprojectionEngineEnable || config.is_rendering_realisticWater) {
			lastFrameFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lastFrameFramebuffer);
			lastFrameColorTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lastFrameColorTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lastFrameColorTexture), 0);
			lastFrameDepthTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lastFrameDepthTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lastFrameDepthTexture), 0);
			lastFrameGBufferFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lastFrameGBufferFramebuffer);
			lastFrameGBufferDepthTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lastFrameGBufferDepthTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lastFrameGBufferDepthTexture), 0);
			DeferredStateManager.checkGLError("Post: rebuild pipeline: lastFrame");
		}

		if(reprojectionEngineEnable) {
			gBufferQuarterFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, gBufferQuarterFramebuffer);
			gBufferQuarterDepthTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(gBufferQuarterDepthTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(gBufferQuarterDepthTexture), 0);
			reprojectionStartup = 0;
			for(int i = 0; i < 2; ++i) {
				reprojectionControlFramebuffer[i] = _wglCreateFramebuffer();
				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionControlFramebuffer[i]);
				if(config.is_rendering_ssao) {
					reprojectionControlSSAOTexture[i] = GlStateManager.generateTexture();
					GlStateManager.bindTexture(reprojectionControlSSAOTexture[i]);
					setNearest();
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(reprojectionControlSSAOTexture[i]), 0);
				}
				if(config.is_rendering_raytracing) {
					reprojectionSSRTexture[i] = GlStateManager.generateTexture();
					GlStateManager.bindTexture(reprojectionSSRTexture[0]); // yes this should be 0
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER,
							config.is_rendering_ssao ? _GL_COLOR_ATTACHMENT1 : _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
							EaglercraftGPU.getNativeTexture(reprojectionSSRTexture[0]), 0);
					reprojectionSSRHitVector[i] = GlStateManager.generateTexture();
					GlStateManager.bindTexture(reprojectionSSRHitVector[0]); // yes this should be 0
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER,
							config.is_rendering_ssao ? _GL_COLOR_ATTACHMENT2 : _GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D,
							EaglercraftGPU.getNativeTexture(reprojectionSSRHitVector[0]), 0);
					reprojectionSSRFramebuffer[i] = _wglCreateFramebuffer();
					_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[i]);
					_wglDrawBuffers(SSRColorAttachments);
					GlStateManager.bindTexture(reprojectionSSRTexture[i]);
					setNearest();
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(reprojectionSSRTexture[i]), 0);
					GlStateManager.bindTexture(reprojectionSSRHitVector[i]);
					setNearest();
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(reprojectionSSRHitVector[i]), 0);
				}
			}
			shader_reproject_control = PipelineShaderReprojControl.compile(config.is_rendering_ssao, config.is_rendering_raytracing);
			shader_reproject_control.loadUniforms();
			if(config.is_rendering_raytracing) {
				shader_reproject_ssr = PipelineShaderReprojSSR.compile();
				shader_reproject_ssr.loadUniforms();
			}
			reprojectionControlDrawBuffers = new int[(config.is_rendering_ssao ? 1 : 0) + (config.is_rendering_raytracing ? 2 : 0)];
			int i = 0;
			if(config.is_rendering_ssao) {
				reprojectionControlDrawBuffers[i] = _GL_COLOR_ATTACHMENT0;
				++i;
			}
			if(config.is_rendering_raytracing) {
				reprojectionControlDrawBuffers[i] = _GL_COLOR_ATTACHMENT0 + i;
				++i;
				reprojectionControlDrawBuffers[i] = _GL_COLOR_ATTACHMENT0 + i;
			}
			for(int j = 0; j < 2; ++j) {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionControlFramebuffer[j]);
				_wglDrawBuffers(reprojectionControlDrawBuffers);
			}
			DeferredStateManager.checkGLError("Post: rebuild pipeline: reprojectionEngineEnable");
		}

		if(config.is_rendering_ssao) {
			ssaoGenerateFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, ssaoGenerateFramebuffer);
			ssaoGenerateTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(ssaoGenerateTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(ssaoGenerateTexture), 0);
			ssaoNoiseTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(ssaoNoiseTexture);
			setNearest();
			int noiseTexSize = 64, noiseTexLen = 16384;
			byte[] noiseTexDat = EagRuntime.getResourceBytes("assets/eagler/glsl/deferred/ssao_noise.bmp");
			if(noiseTexDat == null || noiseTexDat.length != noiseTexLen) {
				noiseTexDat = new byte[noiseTexLen];
				for(int i = 0; i < 4096; ++i) {
					noiseTexDat[(i << 2) + 2] = (byte)255; // dumb fallback
				}
			}
			ByteBuffer noiseTextureBytes = EagRuntime.allocateByteBuffer(noiseTexLen);
			noiseTextureBytes.put(noiseTexDat);
			noiseTextureBytes.flip();
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, noiseTexSize, noiseTexSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, noiseTextureBytes);
			EagRuntime.freeByteBuffer(noiseTextureBytes);
			shader_ssao_generate = PipelineShaderSSAOGenerate.compile();
			shader_ssao_generate.loadUniforms();
			DeferredStateManager.checkGLError("Post: rebuild pipeline: SSAO");
		}

		lightingHDRFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
		lightingHDRFramebufferColorTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lightingHDRFramebufferColorTexture), 0);
		lightingHDRFramebufferDepthTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lightingHDRFramebufferDepthTexture), 0);

		handRenderFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, handRenderFramebuffer);
		GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lightingHDRFramebufferColorTexture), 0);
		handRenderFramebufferDepthTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(handRenderFramebufferDepthTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(handRenderFramebufferDepthTexture), 0);

		shader_hand_depth_mask = PipelineShaderHandDepthMask.compile();
		shader_hand_depth_mask.loadUniforms();
		shader_deferred_combine = PipelineShaderGBufferCombine.compile(config.is_rendering_ssao, config.is_rendering_useEnvMap, config.is_rendering_raytracing);
		shader_deferred_combine.loadUniforms();

		DeferredStateManager.checkGLError("Post: rebuild pipeline: lightingHDRFramebuffer");

		brdfTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(brdfTexture);
		setLinear();
		int brdfLutW = 64, brdfLutH = 64, brdfLutLen = 8192;
		byte[] brdfLutDat = EagRuntime.getResourceBytes("assets/eagler/glsl/deferred/brdf_lut.bmp");
		if(brdfLutDat == null || brdfLutDat.length != brdfLutLen) {
			brdfLutDat = new byte[brdfLutLen];
			for(int i = 0; i < 4096; ++i) {
				brdfLutDat[i << 1] = (byte)192; // dumb fallback
			}
		}
		ByteBuffer brdfLutDatBuffer = EagRuntime.allocateByteBuffer(brdfLutDat.length);
		brdfLutDatBuffer.put(brdfLutDat);
		brdfLutDatBuffer.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_RG8, brdfLutW, brdfLutH, 0, _GL_RG, GL_UNSIGNED_BYTE, brdfLutDatBuffer);
		EagRuntime.freeByteBuffer(brdfLutDatBuffer);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: brdfLut");

		dither8x8Texture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(dither8x8Texture);
		setNearest();
		ByteBuffer dither8x8DatBuffer = EagRuntime.allocateByteBuffer(ditherPattern.length);
		dither8x8DatBuffer.put(ditherPattern);
		dither8x8DatBuffer.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, 8, 8, 0, GL_RED, GL_UNSIGNED_BYTE, dither8x8DatBuffer);
		EagRuntime.freeByteBuffer(dither8x8DatBuffer);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: dither8x8Texture");

		shader_lighting_sun = PipelineShaderLightingSun.compile(shadowsSun ? config.is_rendering_shadowsSun_clamped : 0,
				config.is_rendering_shadowsColored);
		shader_lighting_sun.loadUniforms();
		if(shadowsSun) {
			shader_shadows_sun = PipelineShaderShadowsSun.compile(config.is_rendering_shadowsSun_clamped,
					config.is_rendering_shadowsSmoothed, config.is_rendering_shadowsColored);
			shader_shadows_sun.loadUniforms();
		}
		shader_post_tonemap = PipelineShaderTonemap.compile();
		shader_post_tonemap.loadUniforms();
		shader_post_fxaa = PipelineShaderFXAA.compile();
		shader_post_fxaa.loadUniforms();
		shader_post_exposure_avg = PipelineShaderPostExposureAvg.compile(false);
		shader_post_exposure_avg.loadUniforms();
		shader_post_exposure_avg_luma = PipelineShaderPostExposureAvg.compile(true);
		shader_post_exposure_avg_luma.loadUniforms();
		shader_post_exposure_final = PipelineShaderPostExposureFinal.compile();
		shader_post_exposure_final.loadUniforms();

		DeferredStateManager.checkGLError("Post: rebuild pipeline: compile shaders 1");

		if(config.is_rendering_lensFlares) {
			sunOcclusionValueFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunOcclusionValueFramebuffer);
			sunOcclusionValueTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(sunOcclusionValueTexture);
			setNearest();
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, 1, 1, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(sunOcclusionValueTexture), 0);
			shader_lens_sun_occlusion = PipelineShaderLensSunOcclusion.compile();
			shader_lens_sun_occlusion.loadUniforms();
			DeferredStateManager.checkGLError("Post: rebuild pipeline: sunOcclusionValueFramebuffer");
		}

		if(config.is_rendering_lensDistortion) {
			lensDistortFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lensDistortFramebuffer);
			lensDistortTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lensDistortTexture);
			setLinear();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lensDistortTexture), 0);
			shader_post_lens_distort = PipelineShaderLensDistortion.compile();
			shader_post_lens_distort.loadUniforms();
			DeferredStateManager.checkGLError("Post: rebuild pipeline: lens distortion");
		}

		lastExposureUpdate = 0l;

		exposureBlendFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, exposureBlendFramebuffer);
		exposureBlendTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(exposureBlendTexture);
		setNearest();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 1, 1, GL_RED, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(exposureBlendTexture), 0);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: exposureBlendFramebuffer");

		skybox = new SkyboxRenderer(new ResourceLocation("eagler:glsl/deferred/skybox.dat"));
		try {
			skybox.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load skybox!", e);
		}

		pointLightMesh = new LightSourceMesh(new ResourceLocation("eagler:glsl/deferred/light_point_mesh.dat"), "light_point_mesh");
		try {
			pointLightMesh.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load point light mesh!", e);
		}

		DeferredStateManager.checkGLError("Post: rebuild pipeline: meshes");

		atmosphereHDRFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, atmosphereHDRFramebuffer);
		atmosphereHDRFramebufferColorTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(atmosphereHDRFramebufferColorTexture);
		setNearest();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, skybox.getAtmosLUTWidth(), skybox.getAtmosLUTHeight(), GL_RGBA, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(atmosphereHDRFramebufferColorTexture), 0);

		envMapAtmosphereFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapAtmosphereFramebuffer);
		envMapAtmosphereTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(envMapAtmosphereTexture);
		setLinear();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 128, 256, GL_RGBA, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(envMapAtmosphereTexture), 0);

		envMapSkyFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapSkyFramebuffer);
		envMapSkyTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(envMapSkyTexture);
		setLinear();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 128, 256, GL_RGBA, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(envMapSkyTexture), 0);

		irradiancePhase = 0;

		atmosphereIrradianceFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, atmosphereIrradianceFramebuffer);
		atmosphereIrradianceTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(atmosphereIrradianceTexture);
		setLinear();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 32, 64, GL_RGBA, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(atmosphereIrradianceTexture), 0);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);

		skyIrradianceFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, skyIrradianceFramebuffer);
		skyIrradianceTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(skyIrradianceTexture);
		setLinear();
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 32, 64, GL_RGBA, true);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(skyIrradianceTexture), 0);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: atmosphere");

		moonTextures = GlStateManager.generateTexture();
		GlStateManager.bindTexture(moonTextures);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		ByteBuffer copyBuffer = EagRuntime.allocateByteBuffer(262144);
		int mip = 0;
		try(DataInputStream dis = new DataInputStream(EagRuntime.getResourceStream("/assets/eagler/glsl/deferred/eagler_moon.bmp"))) {
			while(dis.read() == 'E') {
				int w = dis.readShort();
				int h = dis.readShort();
				copyBuffer.clear();
				for(int i = 0, l = w * h * 4; i < l; ++i) {
					copyBuffer.put((byte)dis.read());
				}
				copyBuffer.flip();
				_wglTexImage2D(GL_TEXTURE_2D, mip++, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, copyBuffer);
			}
		}catch(IOException ex) {
			throw new RuntimeException("Could not load \"eagler_moon.bmp\"!", ex);
		}finally {
			EagRuntime.freeByteBuffer(copyBuffer);
		}
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, mip - 1);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: moon");

		CloudRenderWorker.initialize();

		DeferredStateManager.checkGLError("Post: rebuild pipeline: clouds");

		fogDepthCopyBuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, fogDepthCopyBuffer);
		fogDepthCopyTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(fogDepthCopyTexture);
		setNearest();
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(fogDepthCopyTexture), 0);

		shader_atmosphere_fog = PipelineShaderGBufferFog.compile(false, true, config.is_rendering_lightShafts);
		shader_atmosphere_fog.loadUniforms();
		shader_colored_fog_linear = PipelineShaderGBufferFog.compile(true, false, false);
		shader_colored_fog_linear.loadUniforms();
		shader_colored_fog_exp = PipelineShaderGBufferFog.compile(false, false, false);
		shader_colored_fog_exp.loadUniforms();

		DeferredStateManager.checkGLError("Post: rebuild pipeline: fog");

		if(config.is_rendering_useEnvMap) {
			envMapFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapFramebuffer);
			envMapColorTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(envMapColorTexture);
			setLinear();
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 128, 256, GL_RGBA, true);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(envMapColorTexture), 0);
			envMapDepthTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(envMapDepthTexture);
			setNearest();
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT24, 128, 256, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, (ByteBuffer)null);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(envMapDepthTexture), 0);

			DeferredStateManager.checkGLError("Post: rebuild pipeline: env map");
		}

		if(config.is_rendering_realisticWater) {
			realisticWaterMaskFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterMaskFramebuffer);
			realisticWaterMaskTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterMaskTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterMaskTexture), 0);
			realisticWaterDepthBuffer = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterDepthBuffer);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterDepthBuffer), 0);
			realisticWaterCombinedNormalsFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterCombinedNormalsFramebuffer);
			realisticWaterCombinedNormalsTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterCombinedNormalsTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterCombinedNormalsTexture), 0);
			realisticWaterRefractionTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterRefractionTexture);
			setNearest();
			realisticWaterControlFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterControlFramebuffer);
			realisticWaterControlReflectionTexture[0] = GlStateManager.generateTexture();
			realisticWaterControlReflectionTexture[1] = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterControlReflectionTexture[0]);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterControlReflectionTexture[0]), 0);
			realisticWaterControlHitVectorTexture[0] = GlStateManager.generateTexture();
			realisticWaterControlHitVectorTexture[1] = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[0]);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterControlHitVectorTexture[0]), 0);
			GlStateManager.bindTexture(realisticWaterRefractionTexture);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterRefractionTexture), 0);
			_wglDrawBuffers(new int[] { _GL_COLOR_ATTACHMENT0, _GL_COLOR_ATTACHMENT1, _GL_COLOR_ATTACHMENT2 });
			for(int i = 0; i < 2; ++i) {
				realisticWaterSSRFramebuffer[i] = _wglCreateFramebuffer();
				_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[i]);
				GlStateManager.bindTexture(realisticWaterControlReflectionTexture[i]);
				setNearest();
				_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterControlReflectionTexture[i]), 0);
				GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[i]);
				setNearest();
				_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterControlHitVectorTexture[i]), 0);
				_wglDrawBuffers(new int[] { _GL_COLOR_ATTACHMENT0, _GL_COLOR_ATTACHMENT1 });
			}
			realisticWaterDisplacementMapFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterDisplacementMapFramebuffer);
			realisticWaterDisplacementMapTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterDisplacementMapTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterDisplacementMapTexture), 0);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 256, 256, GL_RED, true);
			realisticWaterNormalMapFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterNormalMapFramebuffer);
			realisticWaterNormalMapTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterNormalMapTexture);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(realisticWaterNormalMapTexture), 0);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_RG8, 256, 256, 0, _GL_RG, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			realisticWaterNoiseMap = GlStateManager.generateTexture();
			GlStateManager.bindTexture(realisticWaterNoiseMap);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			String realistic_water_noise_filename = "assets/eagler/glsl/deferred/realistic_water_noise.bmp";
			byte[] bitmapBytes = EagRuntime.getResourceBytes(realistic_water_noise_filename);
			try {
				if(bitmapBytes.length != 32768) {
					throw new IOException("File is length " + bitmapBytes.length + ", expected " + 32768);
				}
			}catch(Throwable t) {
				throw new RuntimeException("File \"" + realistic_water_noise_filename + "\" could not be loaded!", t);
			}
			ByteBuffer buf = EagRuntime.allocateByteBuffer(32768);
			buf.put(bitmapBytes);
			buf.flip();
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_RG8, 128, 128, 0, _GL_RG, GL_UNSIGNED_BYTE, buf);
			EagRuntime.freeByteBuffer(buf);
			shader_realistic_water_control = PipelineShaderRealisticWaterControl.compile();
			shader_realistic_water_control.loadUniforms();
			shader_realistic_water_noise = PipelineShaderRealisticWaterNoise.compile();
			shader_realistic_water_noise.loadUniforms();
			shader_realistic_water_normals = PipelineShaderRealisticWaterNormalMap.compile();
			shader_realistic_water_normals.loadUniforms();
			_wglUniform2f(shader_realistic_water_normals.uniforms.u_sampleOffset2f, 0.00390625f, 0.00390625f);
			if(!config.is_rendering_raytracing) {
				shader_reproject_ssr = PipelineShaderReprojSSR.compile();
				shader_reproject_ssr.loadUniforms();
			}

			DeferredStateManager.checkGLError("Post: rebuild pipeline: realistic water");
		}

		if(config.is_rendering_fxaa) {
			tonemapOutputFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, tonemapOutputFramebuffer);
			tonemapOutputTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(tonemapOutputTexture);
			setNearest();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(tonemapOutputTexture), 0);

			DeferredStateManager.checkGLError("Post: rebuild pipeline: fxaa");
		}

		if(config.is_rendering_lensFlares) {
			LensFlareMeshRenderer.initialize();
			DeferredStateManager.checkGLError("Post: rebuild pipeline: lensFlares");
		}

		recalcAtmosphereTimer = 0l;

		shader_skybox_atmosphere = PipelineShaderSkyboxAtmosphere.compile();
		shader_skybox_atmosphere.loadUniforms();

		shader_skybox_render = PipelineShaderSkyboxRender.compile(false, true);
		shader_skybox_render.loadUniforms();

		shader_skybox_render_paraboloid = PipelineShaderSkyboxRender.compile(true, true);
		shader_skybox_render_paraboloid.loadUniforms();

		shader_skybox_render_paraboloid_noclouds = PipelineShaderSkyboxRender.compile(true, false);
		shader_skybox_render_paraboloid_noclouds.loadUniforms();

		shader_skybox_irradiance[0] = PipelineShaderSkyboxIrradiance.compile(0);
		shader_skybox_irradiance[0].loadUniforms();

		shader_skybox_irradiance[1] = PipelineShaderSkyboxIrradiance.compile(1);
		shader_skybox_irradiance[1].loadUniforms();

		shader_skybox_irradiance[2] = PipelineShaderSkyboxIrradiance.compile(2);
		shader_skybox_irradiance[2].loadUniforms();

		shader_moon_render = PipelineShaderMoonRender.compile();
		shader_moon_render.loadUniforms();

		DeferredStateManager.checkGLError("Post: rebuild pipeline: skybox shaders");

		if(config.is_rendering_lightShafts) {
			lightShaftsFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lightShaftsFramebuffer);
			lightShaftsTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lightShaftsTexture);
			setLinear();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(lightShaftsTexture), 0);
			shader_light_shafts_sample = PipelineShaderLightShaftsSample.compile(config.is_rendering_shadowsSun_clamped);
			shader_light_shafts_sample.loadUniforms();

			DeferredStateManager.checkGLError("Post: rebuild pipeline: light shafts");
		}

		if(config.is_rendering_bloom) {
			bloomBrightPassFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomBrightPassFramebuffer);
			bloomBrightPassTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(bloomBrightPassTexture);
			setNearest();
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(bloomBrightPassTexture), 0);
			bloomDownscaleAFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomDownscaleAFramebuffer);
			bloomDownscaleATexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(bloomDownscaleATexture);
			setLinear();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(bloomDownscaleATexture), 0);
			bloomDownscaleBFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomDownscaleBFramebuffer);
			bloomDownscaleBTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(bloomDownscaleBTexture);
			setLinear();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(bloomDownscaleBTexture), 0);
			bloomHBlurFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomHBlurFramebuffer);
			bloomHBlurTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(bloomHBlurTexture);
			setNearest();
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(bloomHBlurTexture), 0);
			bloomVBlurFramebuffer = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomVBlurFramebuffer);
			bloomVBlurTexture = GlStateManager.generateTexture();
			GlStateManager.bindTexture(bloomVBlurTexture);
			setLinear();
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(bloomVBlurTexture), 0);
			shader_post_bloom_bright = PipelineShaderBloomBrightPass.compile();
			shader_post_bloom_bright.loadUniforms();
			shader_post_bloom_blur = PipelineShaderBloomBlurPass.compile();
			shader_post_bloom_blur.loadUniforms();

			DeferredStateManager.checkGLError("Post: rebuild pipeline: bloom");
		}

		gbufferEffectRenderer.initialize();
		forwardEffectRenderer.initialize(config.is_rendering_dynamicLights, config.is_rendering_shadowsSun_clamped);

		if(config.is_rendering_dynamicLights) {
			shader_lighting_point = PipelineShaderLightingPoint.compile(false);
			shader_lighting_point.loadUniforms();

			buffer_chunkLightingData = _wglGenBuffers();
			EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
			int lightingDataLength = 8 * MAX_LIGHTS_PER_CHUNK + 4;
			chunkLightingDataCopyBuffer = EagRuntime.allocateByteBuffer(lightingDataLength << 2);
			for(int i = 0; i < lightingDataLength; ++i) {
				chunkLightingDataCopyBuffer.putInt(0);
			}
			chunkLightingDataCopyBuffer.flip();
			_wglBufferData(_GL_UNIFORM_BUFFER, chunkLightingDataCopyBuffer, GL_DYNAMIC_DRAW);

			DeferredStateManager.checkGLError("Post: rebuild pipeline: dynamic lights");
		}

		buffer_worldLightingData = _wglGenBuffers();
		EaglercraftGPU.bindGLUniformBuffer(buffer_worldLightingData);
		worldLightingDataCopyBuffer = EagRuntime.allocateByteBuffer(304);
		for(int i = 0; i < 76; ++i) {
			worldLightingDataCopyBuffer.putInt(0);
		}
		worldLightingDataCopyBuffer.flip();
		_wglBufferData(_GL_UNIFORM_BUFFER, worldLightingDataCopyBuffer, GL_DYNAMIC_DRAW);

		DeferredStateManager.checkGLError("Post: rebuild pipeline: world lighting data");

		FixedFunctionPipeline.loadExtensionPipeline(deferredExtPipeline);

		if(!EaglercraftGPU.checkHDRFramebufferSupport(16)) {
			logger.warn("16-bit HDR (floating point) framebuffers are not supported on this device, 32-bit framebuffers will be used instead which may slow the game down");
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		DeferredStateManager.checkGLError("Post: rebuild pipeline");
	}

	public void updateReprojectionCoordinates(double worldX, double worldY, double worldZ) {
		double distX = worldX - reprojectionOriginCoordinateX;
		double distY = worldY - reprojectionOriginCoordinateY;
		double distZ = worldZ - reprojectionOriginCoordinateZ;
		if(distX * distX + distY * distY + distZ * distZ > 48.0 * 48.0) {
			reprojectionOriginCoordinateX = worldX;
			reprojectionOriginCoordinateY = worldY;
			reprojectionOriginCoordinateZ = worldZ;
			reprojectionViewerOffsetX = 0.0f;
			reprojectionViewerOffsetY = 0.0f;
			reprojectionViewerOffsetZ = 0.0f;
			reprojectionStartup = 0;
		}else {
			reprojectionViewerOffsetX = (float) distX;
			reprojectionViewerOffsetY = (float) distY;
			reprojectionViewerOffsetZ = (float) distZ;
		}
		distX = worldX - cloudRenderOriginCoordinateX;
		distZ = worldZ - cloudRenderOriginCoordinateZ;
		if(distX * distX + distZ * distZ > 256.0 * 256.0) {
			cloudRenderOriginCoordinateX = worldX;
			cloudRenderOriginCoordinateZ = worldZ;
			cloudRenderViewerOffsetX = 0.0f;
			cloudRenderViewerOffsetZ = 0.0f;
		}else {
			cloudRenderViewerOffsetX = (float) distX;
			cloudRenderViewerOffsetZ = (float) distZ;
		}
	}

	public void setPartialTicks(float partialTicks_) {
		partialTicks = partialTicks_;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public void resize(int w, int h) {
		if(w == currentWidth && h == currentHeight) {
			return;
		}

		DeferredStateManager.checkGLError("Pre: resize pipeline to " + w + " x " + h);

		GlStateManager.bindTexture(gBufferDiffuseTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GlStateManager.bindTexture(gBufferNormalsTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GlStateManager.bindTexture(gBufferMaterialTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

		GlStateManager.bindTexture(gBufferDepthTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);

		DeferredStateManager.checkGLError("Post: resize pipeline: gbuffer");

		if(config.is_rendering_shadowsSun_clamped > 0) {
			GlStateManager.bindTexture(sunLightingShadowTexture);
			if(config.is_rendering_shadowsColored) {
				_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			}else {
				_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, w, h, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			}
			DeferredStateManager.checkGLError("Post: resize pipeline: sunLightingShadowTexture");
		}

		reprojectionStartup = 0;
		reprojectionTexWidth = w >> 1;
		reprojectionTexHeight = h >> 1;

		shader_deferred_combine.useProgram();
		_wglUniform2f(shader_deferred_combine.uniforms.u_halfResolutionPixelAlignment2f, (float)w / (reprojectionTexWidth << 1), (float)h / (reprojectionTexHeight << 1));

		if(config.is_rendering_ssao) {
			GlStateManager.bindTexture(ssaoGenerateTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, reprojectionTexWidth, reprojectionTexHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			DeferredStateManager.checkGLError("Post: resize pipeline: ssao");
		}

		if(reprojectionEngineEnable || config.is_rendering_realisticWater) {
			GlStateManager.bindTexture(lastFrameColorTexture);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
			GlStateManager.bindTexture(lastFrameDepthTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, reprojectionTexWidth, reprojectionTexHeight, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
			GlStateManager.bindTexture(lastFrameGBufferDepthTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
			DeferredStateManager.checkGLError("Post: resize pipeline: lastFrame");
		}

		if(config.is_rendering_raytracing || config.is_rendering_realisticWater) {
			shader_reproject_ssr.useProgram();
			_wglUniform4f(shader_reproject_ssr.uniforms.u_pixelAlignment4f, reprojectionTexWidth, reprojectionTexHeight, w, h);
		}

		if(reprojectionEngineEnable) {
			GlStateManager.bindTexture(gBufferQuarterDepthTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, reprojectionTexWidth, reprojectionTexHeight, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);

			for(int i = 0; i < 2; ++i) {
				if(config.is_rendering_ssao) {
					GlStateManager.bindTexture(reprojectionControlSSAOTexture[i]);
					_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, reprojectionTexWidth, reprojectionTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
				}
				if(config.is_rendering_raytracing) {
					GlStateManager.bindTexture(reprojectionSSRTexture[i]);
					EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
					GlStateManager.bindTexture(reprojectionSSRHitVector[i]);
					EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
				}
			}

			shader_reproject_control.useProgram();
			_wglUniform4f(shader_reproject_control.uniforms.u_pixelAlignment4f, reprojectionTexWidth, reprojectionTexHeight, w, h);
			DeferredStateManager.checkGLError("Post: resize pipeline: reprojectionEngineEnable");
		}

		if(config.is_rendering_realisticWater) {
			GlStateManager.bindTexture(realisticWaterMaskTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			GlStateManager.bindTexture(realisticWaterDepthBuffer);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
			GlStateManager.bindTexture(realisticWaterCombinedNormalsTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			GlStateManager.bindTexture(realisticWaterRefractionTexture);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
			for(int i = 0; i < 2; ++i) {
				GlStateManager.bindTexture(realisticWaterControlReflectionTexture[i]);
				EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
				GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[i]);
				EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, reprojectionTexWidth, reprojectionTexHeight, GL_RGBA, true);
			}

			shader_realistic_water_control.useProgram();
			_wglUniform4f(shader_realistic_water_control.uniforms.u_pixelAlignment4f, reprojectionTexWidth, reprojectionTexHeight, w, h);
			DeferredStateManager.checkGLError("Post: resize pipeline: realisticWater");
		}

		if(config.is_rendering_lightShafts) {
			GlStateManager.bindTexture(lightShaftsTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, reprojectionTexWidth, reprojectionTexHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			DeferredStateManager.checkGLError("Post: resize pipeline: lightShafts");
		}

		GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
		EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, w, h, GL_RGBA, true); // USE RGBA! WebGL won't render to RGB16F

		GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);

		GlStateManager.bindTexture(handRenderFramebufferDepthTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);

		DeferredStateManager.checkGLError("Post: resize pipeline: lightingHDRFramebuffer");

		GlStateManager.bindTexture(fogDepthCopyTexture);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT32F, w, h, 0, _GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);

		DeferredStateManager.checkGLError("Post: resize pipeline: fogDepthCopyTexture");

		if(config.is_rendering_lensDistortion) {
			GlStateManager.bindTexture(lensDistortTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			DeferredStateManager.checkGLError("Post: resize pipeline: lensDistortion");
		}

		if(config.is_rendering_fxaa) {
			GlStateManager.bindTexture(tonemapOutputTexture);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			DeferredStateManager.checkGLError("Post: resize pipeline: fxaa");
		}

		if(config.is_rendering_bloom) {
			int bloomStageW = w;
			int bloomStageH = h;
			GlStateManager.bindTexture(bloomBrightPassTexture);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, bloomStageW, bloomStageH, GL_RGBA, true);
			bloomBrightPassTextureW = bloomStageW;
			bloomBrightPassTextureH = bloomStageH;
			bloomDownscaleATextureW = bloomDownscaleATextureH = 0;
			bloomDownscaleBTextureW = bloomDownscaleBTextureH = 0;
			if(bloomStageW > 150 && bloomStageH > 85) {
				setLinear();
				bloomStageW >>= 1;
				bloomStageH >>= 1;
				if(bloomStageW > 150 && bloomStageH > 85) {
					GlStateManager.bindTexture(bloomDownscaleATexture);
					EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, bloomStageW, bloomStageH, GL_RGBA, true);
					bloomDownscaleATextureW = bloomStageW;
					bloomDownscaleATextureH = bloomStageH;
					bloomStageW >>= 1;
					bloomStageH >>= 1;
					if(bloomStageW > 150 && bloomStageH > 85) {
						GlStateManager.bindTexture(bloomDownscaleBTexture);
						EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, bloomStageW, bloomStageH, GL_RGBA, true);
						bloomDownscaleBTextureW = bloomStageW;
						bloomDownscaleBTextureH = bloomStageH;
						bloomStageW >>= 1;
						bloomStageH >>= 1;
					}
				}
			}else {
				setNearest();
			}
			GlStateManager.bindTexture(bloomHBlurTexture);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, bloomStageW, bloomStageH, GL_RGBA, true);
			GlStateManager.bindTexture(bloomVBlurTexture);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, bloomStageW, bloomStageH, GL_RGBA, true);
			bloomBlurTextureW = bloomStageW;
			bloomBlurTextureH = bloomStageH;
			DeferredStateManager.checkGLError("Post: resize pipeline: bloom");
		}

		if(lumaAvgDownscaleFramebuffers != null) {
			for(int i = 0; i < lumaAvgDownscaleFramebuffers.length; ++i) {
				_wglDeleteFramebuffer(lumaAvgDownscaleFramebuffers[i]);
			}
		}

		if(lumaAvgDownscaleTexture != null) {
			for(int i = 0; i < lumaAvgDownscaleTexture.length; ++i) {
				GlStateManager.deleteTexture(lumaAvgDownscaleTexture[i]);
			}
		}

		int j = 0;
		int k = h > w ? w : h;
		while(k > 8) {
			++j;
			k >>= 2;
		}

		lumaAvgDownscaleFramebuffers = new IFramebufferGL[j];
		lumaAvgDownscaleTexture = new int[j];

		int kw = w;
		int kh = h;
		int kw2, kh2;
		for(int i = 0; i < j; ++i) {
			kw2 = kw >> 2;
			kh2 = kh >> 2;
			lumaAvgDownscaleFramebuffers[i] = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lumaAvgDownscaleFramebuffers[i]);
			lumaAvgDownscaleTexture[i] = GlStateManager.generateTexture();
			GlStateManager.bindTexture(lumaAvgDownscaleTexture[i]);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, i == j - 1 ? GL_NEAREST : GL_LINEAR);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, i == j - 1 ? GL_NEAREST : GL_LINEAR);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, ((kw & 3) != 0) ? (kw2 + 1) : kw2,
					((kh & 3) != 0) ? (kh2 + 1) : kh2, GL_RED, true);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
					EaglercraftGPU.getNativeTexture(lumaAvgDownscaleTexture[i]), 0);
			kw = kw2;
			kh = kh2;
		}

		currentWidth = w;
		currentHeight = h;

		DeferredStateManager.checkGLError("Post: resize pipeline: lumaAvg");
	}

	public void loadViewMatrix() {
		DeferredStateManager.loadGBufferViewMatrix();
		DeferredStateManager.loadGBufferProjectionMatrix();
	}

	public void beginDrawDeferred() {
		DeferredStateManager.checkGLError("Pre: beginDrawDeferred()");
		DynamicLightManager.lightRenderList.clear();
	}

	public void beginDrawMainGBuffer() {
		DeferredStateManager.checkGLError("Pre: beginDrawMainGBuffer()");
		resize(mc.displayWidth, mc.displayHeight);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, gBufferFramebuffer);
		_wglDrawBuffers(gBufferDrawBuffers);
		_wglClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		_wglClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableExtensionPipeline();
		GlStateManager.globalDisableBlend();
		DeferredStateManager.checkGLError("Post: beginDrawMainGBuffer()");
	}

	public void beginDrawMainGBufferTerrain() {
		DeferredStateManager.checkGLError("Pre: beginDrawMainGBufferTerrain()");
		TextureManager mgr = mc.getTextureManager();
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.enableTexture2D();
		mgr.bindTexture(TextureMap.locationBlocksTexture);
		DeferredStateManager.checkGLError("Post: beginDrawMainGBufferTerrain()");
	}

	public void beginDrawMainGBufferEntities() {
		DeferredStateManager.checkGLError("Pre: beginDrawMainGBufferEntities()");
	}

	public void beginDrawMainGBufferDestroyProgress() {
		DeferredStateManager.checkGLError("Pre: beginDrawMainGBufferDestroyProgress()");
		GlStateManager.disableExtensionPipeline();
	}

	public void endDrawMainGBufferDestroyProgress() {
		DeferredStateManager.checkGLError("Pre: endDrawMainGBufferDestroyProgress()");
		GlStateManager.enableExtensionPipeline();
	}

	public void endDrawMainGBuffer() {
		DeferredStateManager.checkGLError("Pre: endDrawMainGBuffer()");
		_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
		DeferredStateManager.checkGLError("Post: endDrawMainGBuffer()");
	}

	public void beginDrawMainShadowMap() {
		DeferredStateManager.checkGLError("Pre: beginDrawMainShadowMap()");
		if(config.is_rendering_shadowsColored) {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowColorFramebuffer);
			_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
			GlStateManager.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.clearDepth(1.0f);
			GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowFramebuffer);
		}else {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowFramebuffer);
			_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
			GlStateManager.clearDepth(1.0f);
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
		}
		GlStateManager.enableCull();
		GlStateManager.cullFace(GL_FRONT);
		DeferredStateManager.enableShadowRender();
		GlStateManager.colorMask(false, false, false, false);
		DeferredStateManager.checkGLError("Post: beginDrawMainShadowMap()");
	}

	public void endDrawMainShadowMap() {
		DeferredStateManager.checkGLError("Pre: endDrawMainShadowMap()");
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		GlStateManager.cullFace(GL_BACK);
		DeferredStateManager.disableShadowRender();
		GlStateManager.colorMask(true, true, true, true);
		DeferredStateManager.checkGLError("Post: endDrawMainShadowMap()");
	}

	public void beginDrawMainShadowMapLOD(int lod) {
		DeferredStateManager.checkGLError("Pre: beginDrawMainShadowMapLOD(" + lod + ")");
		GlStateManager.viewport(0, sunShadowDepthBufferRes * lod, sunShadowDepthBufferRes, sunShadowDepthBufferRes);
	}

	public void beginDrawColoredShadows() {
		DeferredStateManager.checkGLError("Pre: beginDrawColoredShadows()");
		_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowColorFramebuffer);
		DeferredStateManager.enableForwardRender();
		GlStateManager.globalEnableBlend();
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GL_ZERO, GL_SRC_COLOR, GL_ZERO, GL_ZERO);
		GlStateManager.enablePolygonOffset();
		GlStateManager.doPolygonOffset(0.25f, 1.0f);
		GlStateManager.colorMask(true, true, true, true);
		DeferredStateManager.checkGLError("Post: beginDrawColoredShadows()");
	}

	public void endDrawColoredShadows() {
		DeferredStateManager.checkGLError("Pre: endDrawColoredShadows()");
		_wglBindFramebuffer(_GL_FRAMEBUFFER, sunShadowFramebuffer);
		DeferredStateManager.disableForwardRender();
		GlStateManager.disableBlend();
		GlStateManager.globalDisableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.disablePolygonOffset();
		GlStateManager.colorMask(false, false, false, false);
		DeferredStateManager.checkGLError("Post: endDrawColoredShadows()");
	}

	private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");

	public void combineGBuffersAndIlluminate() {
		DeferredStateManager.checkGLError("Pre: combineGBuffersAndIlluminate()");
		DynamicLightManager.updateTimers();

		// ========================= CLEAR STATE ========================== //

		GlStateManager.disableExtensionPipeline();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		GlStateManager.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		GlStateManager.globalEnableBlend();
		GlStateManager.disableBlend();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(GL_MODELVIEW);
		DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): CLEAR STATE");

		// ==================== CACHE SOME MATRICIES ===================== //

		DeferredStateManager.passViewMatrix.load(DeferredStateManager.viewMatrix);
		DeferredStateManager.passProjMatrix.load(DeferredStateManager.projMatrix);
		DeferredStateManager.passInverseViewMatrix.load(DeferredStateManager.inverseViewMatrix);
		DeferredStateManager.passInverseProjMatrix.load(DeferredStateManager.inverseProjMatrix);
		Matrix4f.mul(DeferredStateManager.projMatrix, DeferredStateManager.viewMatrix, tmpMatrixViewProj);
		DeferredStateManager.currentGBufferFrustum.set(tmpMatrixViewProj);
		Matrix4f.invert(tmpMatrixViewProj, tmpMatrixInverseViewProj);

		Entity renderViewEntity = mc.getRenderViewEntity();
		if(renderViewEntity == null) {
			renderViewEntity = mc.thePlayer;
		}

		double entityPosX = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * partialTicks;
		double entityPosY = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * partialTicks;
		double entityPosZ = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * partialTicks;
		int entityChunkOriginX = MathHelper.floor_double(entityPosX / 16.0) << 4;
		int entityChunkOriginY = MathHelper.floor_double(entityPosY / 16.0) << 4;
		int entityChunkOriginZ = MathHelper.floor_double(entityPosZ / 16.0) << 4;

		Vector3f currentSunAngle = DeferredStateManager.currentSunAngle;
		float sunKelvin = 1500.0f + (2500.0f * Math.max(-currentSunAngle.y, 0.0f));
		float fff = mc.theWorld.getRainStrength(partialTicks);
		float ff2 = mc.theWorld.getThunderStrength(partialTicks);
		long millis = System.currentTimeMillis();
		int dim = Minecraft.getMinecraft().theWorld.provider.getDimensionId();

		// ==================== UPDATE CLOUD RENDERER ===================== //

		if(dim == 0) {
			CloudRenderWorker.setPosition(cloudRenderViewerOffsetX, (float)entityPosY, cloudRenderViewerOffsetZ);
			CloudRenderWorker.update();
			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): UPDATE CLOUD RENDERER");
		}

		if(millis - recalcAtmosphereTimer > 100l) {

			if(dim == 0) {

				// =============== CALCULATE ATMOSPHERE COLORS ================ //

				recalcAtmosphereTimer = millis;
				_wglBindFramebuffer(_GL_FRAMEBUFFER, atmosphereHDRFramebuffer);
				shader_skybox_atmosphere.useProgram();
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(skybox.getNormalsLUT());
				GlStateManager.viewport(0, 0, skybox.getAtmosLUTWidth(), skybox.getAtmosLUTHeight());
				_wglUniform4f(shader_skybox_atmosphere.uniforms.u_sunDirectionIntensity4f, -currentSunAngle.x, -currentSunAngle.y, -currentSunAngle.z, 5.0f);
				_wglUniform1f(shader_skybox_atmosphere.uniforms.u_altitude1f, Math.max((float)(entityPosY - 85.0), -20.0f));
				Vector3f sunColorTmp = tmpVector3;
				sunColorTmp.set(DeferredStateManager.currentSunLightColor);
				float luma = sunColorTmp.x * 0.299f + sunColorTmp.y * 0.587f + sunColorTmp.z * 0.114f;
				float sat = 0.3f; // desaturate
				sunColorTmp.x = (sunColorTmp.x - luma) * sat + luma;
				sunColorTmp.y = (sunColorTmp.y - luma) * sat + luma;
				sunColorTmp.z = (sunColorTmp.z - luma) * sat + luma;
				sunColorTmp.scale(0.3f - ff2 * 0.175f);
				_wglUniform4f(shader_skybox_atmosphere.uniforms.u_blendColor4f, sunColorTmp.x * 0.05f, sunColorTmp.y * 0.05f, sunColorTmp.z * 0.05f, fff);

				DrawUtils.drawStandardQuad2D();

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): CALCULATE ATMOSPHERE COLORS");

				// =============== GENERATE SKY REFLECTION MAP ================ //

				_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapSkyFramebuffer);
				GlStateManager.viewport(0, 0, 128, 128);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				CloudRenderWorker.bindParaboloid();
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(atmosphereHDRFramebufferColorTexture);
				shader_skybox_render_paraboloid.useProgram();
				uniformMatrixHelper(shader_skybox_render_paraboloid.uniforms.u_viewMatrix4f, DeferredStateManager.paraboloidTopViewMatrix);
				_wglUniform1f(shader_skybox_render_paraboloid.uniforms.u_farPlane1f, 2.0f);
				if (mc.theWorld.getLastLightningBolt() > 0) {
					float f = 0.3f + fff;
					_wglUniform4f(shader_skybox_render_paraboloid.uniforms.u_lightningColor4f, 0.02f * f, 0.02f * f, 0.02f * f, 1.0f - f * 0.25f);
				}else {
					_wglUniform4f(shader_skybox_render_paraboloid.uniforms.u_lightningColor4f, 0.0f, 0.0f, 0.0f, 1.0f);
				}
				skybox.drawTop();

				GlStateManager.viewport(0, 128, 128, 128);
				uniformMatrixHelper(shader_skybox_render_paraboloid.uniforms.u_viewMatrix4f, DeferredStateManager.paraboloidBottomViewMatrix);
				skybox.drawBottom();

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): GENERATE SKY REFLECTION MAP");


				if(irradiancePhase++ % 10 == 0) {

					// =============== GENERATE ATMOSPHERE REFLECTION MAP ================ //

					_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapAtmosphereFramebuffer);
					GlStateManager.viewport(0, 0, 128, 128);
					shader_skybox_render_paraboloid_noclouds.useProgram();
					uniformMatrixHelper(shader_skybox_render_paraboloid_noclouds.uniforms.u_viewMatrix4f, DeferredStateManager.paraboloidTopViewMatrix);
					_wglUniform1f(shader_skybox_render_paraboloid_noclouds.uniforms.u_farPlane1f, 2.0f);
					if (mc.theWorld.getLastLightningBolt() > 0) {
						float f = 0.3f + fff;
						_wglUniform4f(shader_skybox_render_paraboloid_noclouds.uniforms.u_lightningColor4f, 0.02f * f, 0.02f * f, 0.02f * f, 1.0f - f * 0.25f);
					}else {
						_wglUniform4f(shader_skybox_render_paraboloid_noclouds.uniforms.u_lightningColor4f, 0.0f, 0.0f, 0.0f, 1.0f);
					}
					skybox.drawTop();

					GlStateManager.viewport(0, 128, 128, 128);
					uniformMatrixHelper(shader_skybox_render_paraboloid_noclouds.uniforms.u_viewMatrix4f, DeferredStateManager.paraboloidBottomViewMatrix);
					skybox.drawBottom();

					DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): GENERATE ATMOSPHERE REFLECTION MAP");

					// =============== GENERATE ATMOSPHERE IRRADIANCE MAP ================ //

					_wglBindFramebuffer(_GL_FRAMEBUFFER, atmosphereIrradianceFramebuffer);
					GlStateManager.bindTexture(envMapAtmosphereTexture);
					GlStateManager.viewport(0, 0, 32, 64);

					shader_skybox_irradiance[0].useProgram();
					DrawUtils.drawStandardQuad2D();

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL_ONE, GL_ONE);

					shader_skybox_irradiance[1].useProgram();
					DrawUtils.drawStandardQuad2D();

					shader_skybox_irradiance[2].useProgram();
					DrawUtils.drawStandardQuad2D();

					GlStateManager.disableBlend();

					DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): GENERATE ATMOSPHERE IRRADIANCE MAP");

				}else {

					// =============== GENERATE SKY IRRADIANCE MAP ================ //

					_wglBindFramebuffer(_GL_FRAMEBUFFER, skyIrradianceFramebuffer);
					GlStateManager.bindTexture(envMapSkyTexture);
					GlStateManager.viewport(0, 0, 32, 64);

					shader_skybox_irradiance[0].useProgram();
					DrawUtils.drawStandardQuad2D();

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL_ONE, GL_ONE);

					shader_skybox_irradiance[1].useProgram();
					DrawUtils.drawStandardQuad2D();

					shader_skybox_irradiance[2].useProgram();
					DrawUtils.drawStandardQuad2D();

					GlStateManager.disableBlend();

					DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): GENERATE SKY IRRADIANCE MAP");
				}
			}else if(dim == -1) {

				// =============== NETHER SKY REFLECTION MAP ================ //

				_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapSkyFramebuffer);
				GlStateManager.clearColor(0.55f, 0.25f, 0.05f, 1.0f);
				GlStateManager.clear(GL_COLOR_BUFFER_BIT);

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): NETHER SKY REFLECTION MAP");

				// =============== NETHER SKY IRRADIANCE MAP ================ //

				_wglBindFramebuffer(_GL_FRAMEBUFFER, skyIrradianceFramebuffer);
				GlStateManager.clearColor(0.22f, 0.08f, 0.01f, 1.0f);
				GlStateManager.clear(GL_COLOR_BUFFER_BIT);

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): NETHER SKY IRRADIANCE MAP");

			}

		}

		if(reprojectionEngineEnable) {

			// ============ DOWNSCALE DEPTH BUFFER, FOR PERFORMANCE =========== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, gBufferQuarterFramebuffer);
			GlStateManager.enableDepth();
			GlStateManager.depthFunc(GL_ALWAYS);
			GlStateManager.depthMask(true);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(gBufferDepthTexture);
			_wglDrawBuffers(GL_NONE);
			GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);
			TextureCopyUtil.alignPixelsTopLeft(reprojectionTexWidth << 1, reprojectionTexHeight << 1, reprojectionTexWidth, reprojectionTexHeight);
			TextureCopyUtil.blitTextureDepth();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.depthFunc(GL_LEQUAL);

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): DOWNSCALE DEPTH BUFFER, FOR PERFORMANCE");

			if(config.is_rendering_ssao) {

				// ====================== RUN SSAO ALGORITHM ====================== //

				_wglBindFramebuffer(_GL_FRAMEBUFFER, ssaoGenerateFramebuffer);
				GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(ssaoNoiseTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				GlStateManager.bindTexture(gBufferNormalsTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(gBufferQuarterDepthTexture);
				shader_ssao_generate.useProgram();
				uniformMatrixHelper(shader_ssao_generate.uniforms.u_projectionMatrix4f, DeferredStateManager.projMatrix);
				uniformMatrixHelper(shader_ssao_generate.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
				matrixCopyBuffer.clear();
				matrixCopyBuffer.put(((random.nextFloat() * 25.0f - 12.5f) + (random.nextBoolean() ? 1.0f : -1.0f) * (random.nextFloat() * 6.0f + 6.0f)) * 10.0f);
				matrixCopyBuffer.put(((random.nextFloat() * 25.0f - 12.5f) + (random.nextBoolean() ? 1.0f : -1.0f) * (random.nextFloat() * 6.0f + 6.0f)) * 10.0f);
				matrixCopyBuffer.put(((random.nextFloat() * 25.0f - 12.5f) + (random.nextBoolean() ? 1.0f : -1.0f) * (random.nextFloat() * 6.0f + 6.0f)) * 10.0f);
				matrixCopyBuffer.put(((random.nextFloat() * 25.0f - 12.5f) + (random.nextBoolean() ? 1.0f : -1.0f) * (random.nextFloat() * 6.0f + 6.0f)) * 10.0f);
				matrixCopyBuffer.flip();
				_wglUniformMatrix2fv(shader_ssao_generate.uniforms.u_randomizerDataMatrix2f, false, matrixCopyBuffer);

				DrawUtils.drawStandardQuad2D();

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RUN SSAO ALGORITHM");
			}

			GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);

			// ============== RUN REPROJECTION CONTROL SHADER ================ //

			GlStateManager.setActiveTexture(GL_TEXTURE8);
			if(config.is_rendering_raytracing) {
				GlStateManager.bindTexture(gBufferMaterialTexture);
			}else {
				GlStateManager.bindTexture(-1);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE7);
			GlStateManager.bindTexture(lastFrameGBufferDepthTexture); // may be full of garbage data, let's pretend it won't
			GlStateManager.setActiveTexture(GL_TEXTURE6);
			if(config.is_rendering_raytracing) {
				GlStateManager.bindTexture(lastFrameColorTexture);
			}else {
				GlStateManager.bindTexture(-1);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE5);
			if(config.is_rendering_raytracing) {
				GlStateManager.bindTexture(reprojectionSSRHitVector[1]); // may be garbage data
			}else {
				GlStateManager.bindTexture(-1);
			}

			GlStateManager.setActiveTexture(GL_TEXTURE4);
			if(config.is_rendering_raytracing) {
				GlStateManager.bindTexture(reprojectionSSRTexture[1]); // may be garbage data
			}else {
				GlStateManager.bindTexture(-1);
			}
			
			GlStateManager.setActiveTexture(GL_TEXTURE3);
			GlStateManager.bindTexture(gBufferNormalsTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE2);
			if(config.is_rendering_ssao) {
				GlStateManager.bindTexture(reprojectionControlSSAOTexture[1 - reprojectionPhase]); // may be garbage
			}else {
				GlStateManager.bindTexture(-1);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE1);
			if(config.is_rendering_ssao) {
				GlStateManager.bindTexture(ssaoGenerateTexture);
			}else {
				GlStateManager.bindTexture(-1);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(gBufferDepthTexture);

			_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionControlFramebuffer[reprojectionPhase]);
			shader_reproject_control.useProgram();
			tmpVector1.set(-reprojectionViewerOffsetX, -reprojectionViewerOffsetY, -reprojectionViewerOffsetZ);
			tmpMatrix1.setIdentity();
			Matrix4f.translate(tmpVector1, tmpMatrix1, tmpMatrix1);
			Matrix4f.mul(DeferredStateManager.viewMatrix, tmpMatrix1, tmpMatrixViewReproject);
			Matrix4f.mul(tmpMatrixViewProj, tmpMatrix1, tmpMatrixViewProjReproject);
			Matrix4f.invert(tmpMatrixViewProjReproject, tmpMatrixInverseViewProjReproject);
			
			uniformMatrixHelper(shader_reproject_control.uniforms.u_inverseViewProjMatrix4f, tmpMatrixInverseViewProjReproject);
			uniformMatrixHelper(shader_reproject_control.uniforms.u_reprojectionMatrix4f, tmpMatrixLastFrameViewProjReproject);
			if(config.is_rendering_raytracing) {
				uniformMatrixHelper(shader_reproject_control.uniforms.u_projectionMatrix4f, DeferredStateManager.projMatrix);
				uniformMatrixHelper(shader_reproject_control.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
				Matrix4f.invert(tmpMatrixLastFrameProj, tmpMatrix1);
				uniformMatrixHelper(shader_reproject_control.uniforms.u_lastInverseProjMatrix4f, tmpMatrix1);
				Matrix4f.invert(tmpMatrixLastFrameViewReproject, tmpMatrix1);
				Matrix4f.mul(tmpMatrixViewReproject, tmpMatrix1, tmpMatrix1);
				uniformMatrixHelper(shader_reproject_control.uniforms.u_reprojectionInverseViewMatrix4f, tmpMatrix1);
				Matrix4f.invert(tmpMatrix1, tmpMatrix1);
				Matrix4f.mul(tmpMatrixLastFrameProj, tmpMatrix1, tmpMatrix1);
				uniformMatrixHelper(shader_reproject_control.uniforms.u_viewToPreviousProjMatrix4f, tmpMatrix1);
			}
			_wglUniform4f(shader_reproject_control.uniforms.u_nearFarPlane4f, DeferredStateManager.gbufferNearPlane,
					DeferredStateManager.gbufferFarPlane, DeferredStateManager.gbufferNearPlane * DeferredStateManager.gbufferFarPlane * 2.0f,
					DeferredStateManager.gbufferFarPlane - DeferredStateManager.gbufferNearPlane);

			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RUN REPROJECTION CONTROL SHADER");

			if(config.is_rendering_raytracing) {

				// =========== RUN SCREENSPACE REFLECTIONS ALGORITHM ============= //

				GlStateManager.setActiveTexture(GL_TEXTURE5);
				GlStateManager.bindTexture(lastFrameDepthTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE4);
				GlStateManager.bindTexture(lastFrameColorTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(reprojectionSSRHitVector[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(reprojectionSSRTexture[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE1);
				GlStateManager.bindTexture(gBufferNormalsTexture);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(gBufferQuarterDepthTexture);

				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[1]);

				shader_reproject_ssr.useProgram();
				uniformMatrixHelper(shader_reproject_ssr.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
				Matrix4f.mul(tmpMatrixLastFrameViewProjReproject, tmpMatrixInverseViewProjReproject, tmpMatrix1);
				Matrix4f.mul(tmpMatrix1, DeferredStateManager.projMatrix, tmpMatrix1);
				uniformMatrixHelper(shader_reproject_ssr.uniforms.u_lastProjectionMatrix4f, tmpMatrix1);
				Matrix4f.invert(tmpMatrix1, tmpMatrix1);
				matrixCopyBuffer.clear();
				matrixCopyBuffer.put(tmpMatrix1.m02);
				matrixCopyBuffer.put(tmpMatrix1.m03);
				matrixCopyBuffer.put(tmpMatrix1.m12);
				matrixCopyBuffer.put(tmpMatrix1.m13);
				matrixCopyBuffer.put(tmpMatrix1.m22);
				matrixCopyBuffer.put(tmpMatrix1.m23);
				matrixCopyBuffer.put(tmpMatrix1.m32);
				matrixCopyBuffer.put(tmpMatrix1.m33);
				matrixCopyBuffer.flip();
				_wglUniformMatrix4x2fv(shader_reproject_ssr.uniforms.u_lastInverseProjMatrix4x2f, false, matrixCopyBuffer);
				_wglUniform1f(shader_reproject_ssr.uniforms.u_sampleStep1f, 0.125f);

				DrawUtils.drawStandardQuad2D(); // sample 1

				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(reprojectionSSRHitVector[1]);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(reprojectionSSRTexture[1]);

				DrawUtils.drawStandardQuad2D(); // sample 2

				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[1]);
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(reprojectionSSRHitVector[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(reprojectionSSRTexture[0]);

				DrawUtils.drawStandardQuad2D(); // sample 3

				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(reprojectionSSRHitVector[1]);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(reprojectionSSRTexture[1]);

				DrawUtils.drawStandardQuad2D(); // sample 4

				_wglBindFramebuffer(_GL_FRAMEBUFFER, reprojectionSSRFramebuffer[1]);
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(reprojectionSSRHitVector[0]);
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(reprojectionSSRTexture[0]);

				DrawUtils.drawStandardQuad2D(); // sample 5

				DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RUN SCREENSPACE REFLECTIONS ALGORITHM");
			}
		}

		if(config.is_rendering_shadowsSun_clamped > 0) {

			// ==================== RENDER SUNLIGHT SHADOWS ===================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, sunLightingShadowFramebuffer);
			GlStateManager.viewport(0, 0, currentWidth, currentHeight);

			shader_shadows_sun.useProgram();
			uniformMatrixHelper(shader_shadows_sun.uniforms.u_inverseViewMatrix4f, DeferredStateManager.inverseViewMatrix);
			uniformMatrixHelper(shader_shadows_sun.uniforms.u_inverseViewProjMatrix4f, tmpMatrixInverseViewProj);

			if(config.is_rendering_shadowsColored) {
				GlStateManager.setActiveTexture(GL_TEXTURE3);
				GlStateManager.bindTexture(sunShadowColorBuffer);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE2);
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			if(config.is_rendering_shadowsSmoothed) {
				setLinear();
			}
			GlStateManager.setActiveTexture(GL_TEXTURE1);
			GlStateManager.bindTexture(gBufferDepthTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(gBufferNormalsTexture);
			Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix0, tmpShadowLOD0MatrixTexSpace);
			uniformMatrixHelper(shader_shadows_sun.uniforms.u_sunShadowMatrixLOD04f, tmpShadowLOD0MatrixTexSpace);
			if(config.is_rendering_shadowsSun_clamped > 1) {
				Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix1, tmpShadowLOD1MatrixTexSpace);
				uniformMatrixHelper(shader_shadows_sun.uniforms.u_sunShadowMatrixLOD14f, tmpShadowLOD1MatrixTexSpace);
				if(config.is_rendering_shadowsSun_clamped > 2) {
					Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix2, tmpShadowLOD2MatrixTexSpace);
					uniformMatrixHelper(shader_shadows_sun.uniforms.u_sunShadowMatrixLOD24f, tmpShadowLOD2MatrixTexSpace);
				}
			}

			Vector3f currentSunShadowAngle = DeferredStateManager.currentSunLightAngle;
			_wglUniform3f(shader_shadows_sun.uniforms.u_sunDirection3f, -currentSunShadowAngle.x, -currentSunShadowAngle.y, -currentSunShadowAngle.z);
			DrawUtils.drawStandardQuad2D();

			if(config.is_rendering_shadowsSmoothed) {
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				setNearest();
				GlStateManager.setActiveTexture(GL_TEXTURE0);
			}

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER SUNLIGHT SHADOWS");
		}

		// ================ INITIALIZE HDR FRAMEBUFFER ================== //

		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		_wglBindFramebuffer(_GL_READ_FRAMEBUFFER, gBufferFramebuffer);
		_wglBindFramebuffer(_GL_DRAW_FRAMEBUFFER, lightingHDRFramebuffer);
		_wglBlitFramebuffer(0, 0, currentWidth, currentHeight, 0, 0, currentWidth, currentHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);

		if(dim == -1) {
			float f = 0.13f;
			GlStateManager.clearColor(0.57f * 0.57f * f, 0.38f * 0.38f * f, 0.20f * 0.20f * f, 0.0f);
		}else {
			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		}
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);

		DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): INITIALIZE HDR FRAMEBUFFER");

		// ================= RENDER AMBIENT LIGHTING ==================== //

		GlStateManager.setActiveTexture(GL_TEXTURE9);
		GlStateManager.bindTexture(MetalsLUT.getGLTexture());
		GlStateManager.setActiveTexture(GL_TEXTURE8);
		GlStateManager.bindTexture(brdfTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE7);
		GlStateManager.bindTexture(skyIrradianceTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE6);
		if(config.is_rendering_useEnvMap) {
			GlStateManager.bindTexture(envMapColorTexture);
		}else {
			GlStateManager.bindTexture(-1);
		}
		GlStateManager.setActiveTexture(GL_TEXTURE5);
		if(config.is_rendering_raytracing) {
			GlStateManager.bindTexture(reprojectionSSRTexture[1]);
		}else {
			GlStateManager.bindTexture(-1);
		}
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		if(config.is_rendering_ssao) {
			GlStateManager.bindTexture(reprojectionControlSSAOTexture[reprojectionPhase]);
		}else {
			GlStateManager.bindTexture(-1);
		}
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(gBufferDepthTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(gBufferMaterialTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(gBufferNormalsTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(gBufferDiffuseTexture);

		shader_deferred_combine.useProgram();
		uniformMatrixHelper(shader_deferred_combine.uniforms.u_inverseViewMatrix4f, DeferredStateManager.inverseViewMatrix);
		uniformMatrixHelper(shader_deferred_combine.uniforms.u_inverseProjMatrix4f, DeferredStateManager.inverseProjMatrix);
		_wglUniform3f(shader_deferred_combine.uniforms.u_sunDirection3f, DeferredStateManager.currentSunAngle.x, DeferredStateManager.currentSunAngle.y, DeferredStateManager.currentSunAngle.z);
		float lightningBoost = mc.theWorld.getLastLightningBolt() > 0 ? 1.0f : 0.0f;
		lightningBoost *= 0.3f + fff;
		_wglUniform1f(shader_deferred_combine.uniforms.u_skyLightFactor1f, getSkyBrightnessTimeParam() + lightningBoost);
		DrawUtils.drawStandardQuad2D();

		DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER AMBIENT LIGHTING");

		// ================ BEGIN HDR LIGHTING PASS ================= //

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);

		// ==================== RENDER SUNLIGHT ===================== //

		if(dim == 0) {
			shader_lighting_sun.useProgram();
			uniformMatrixHelper(shader_lighting_sun.uniforms.u_inverseViewMatrix4f, DeferredStateManager.inverseViewMatrix);
			uniformMatrixHelper(shader_lighting_sun.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
			GlStateManager.setActiveTexture(GL_TEXTURE5);
			GlStateManager.bindTexture(MetalsLUT.getGLTexture());
			GlStateManager.setActiveTexture(GL_TEXTURE4);
			if(config.is_rendering_shadowsSun_clamped > 0) {
				GlStateManager.bindTexture(sunLightingShadowTexture);
			}else {
				GlStateManager.bindTexture(-1);
			}
			GlStateManager.setActiveTexture(GL_TEXTURE0);
	
			float ffff = getSkyBrightnessParam();
			float[] sunRGB;
			if(currentSunAngle.y < 0.05f) {
				sunRGB = TemperaturesLUT.getColorTemperature((int)sunKelvin);
				Vector3f currentSunLightColor3f = DeferredStateManager.currentSunLightColor; // reuse variable
				currentSunLightColor3f.x = sunRGB[0] * 8.0f * (0.1f + ffff * 0.9f);
				currentSunLightColor3f.y = sunRGB[1] * 8.0f * (0.1f + ffff * 0.9f);
				currentSunLightColor3f.z = sunRGB[2] * 8.0f * (0.1f + ffff * 0.9f);
				_wglUniform3f(shader_lighting_sun.uniforms.u_sunColor3f, sunRGB[0] * 4.0f * ffff, sunRGB[1] * 4.0f * ffff, sunRGB[2] * 4.0f * ffff);
			}else {
				sunRGB = TemperaturesLUT.getColorTemperature((int)(9000.0f + 2500.0f * currentSunAngle.y));
				Vector3f currentSunLightColor3f = DeferredStateManager.currentSunLightColor; // reuse variable
				currentSunLightColor3f.x = sunRGB[0] * 0.3f * (0.2f + ffff * 0.8f);
				currentSunLightColor3f.y = sunRGB[1] * 0.3f * (0.2f + ffff * 0.8f);
				currentSunLightColor3f.z = sunRGB[2] * 0.3f * (0.2f + ffff * 0.8f);
				_wglUniform3f(shader_lighting_sun.uniforms.u_sunColor3f, sunRGB[0] * 0.1f * (0.5f + ffff * 0.5f), sunRGB[1] * 0.1f * (0.5f + ffff * 0.5f), sunRGB[2] * 0.1f * (0.5f + ffff * 0.5f));
			}
	
			_wglUniform3f(shader_lighting_sun.uniforms.u_sunDirection3f, -DeferredStateManager.currentSunLightAngle.x, -DeferredStateManager.currentSunLightAngle.y, -DeferredStateManager.currentSunLightAngle.z);
	
			DrawUtils.drawStandardQuad2D();
	
			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER SUNLIGHT");
		}else {
			DeferredStateManager.currentSunLightColor.set(0.0f, 0.0f, 0.0f);
		}

		// ================== RENDER DYNAMIC LIGHTS =================== //

		if(config.is_rendering_dynamicLights) {
			shader_lighting_point.useProgram();
			uniformMatrixHelper(shader_lighting_point.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
			uniformMatrixHelper(shader_lighting_point.uniforms.u_inverseViewMatrix4f, DeferredStateManager.inverseViewMatrix);
			_wglUniform2f(shader_lighting_point.uniforms.u_viewportSize2f, 1.0f / currentWidth, 1.0f / currentHeight);
			Iterator<DynamicLightInstance> itr = DynamicLightManager.lightRenderList.iterator();
			AxisAlignedBB aabb = renderViewEntity.getEntityBoundingBox();
			double eyeHeight = renderViewEntity.getEyeHeight();
			while(itr.hasNext()) {
				DynamicLightInstance dl = itr.next();
				float lightPosX = (float)(dl.posX - entityPosX);
				float lightPosY = (float)(dl.posY - entityPosY);
				float lightPosZ = (float)(dl.posZ - entityPosZ);
				float lightChunkPosX = (float)(dl.posX - entityChunkOriginX);
				float lightChunkPosY = (float)(dl.posY - entityChunkOriginY);
				float lightChunkPosZ = (float)(dl.posZ - entityChunkOriginZ);
				bucketLightSource(lightChunkPosX, lightChunkPosY, lightChunkPosZ, dl);
				if(dl.posX > aabb.minX - 0.25 && dl.posY > aabb.minY + eyeHeight - 0.25 && dl.posZ > aabb.minZ - 0.25 &&
						dl.posX < aabb.maxX + 0.25 && dl.posY < aabb.minY + eyeHeight + 0.25 && dl.posZ < aabb.maxZ + 0.25) {
					tmpMatrix1.setIdentity();
					uniformMatrixHelper(shader_lighting_point.uniforms.u_modelViewProjMatrix4f, tmpMatrix1);
					_wglUniform3f(shader_lighting_point.uniforms.u_lightColor3f, dl.red, dl.green, dl.blue);
					_wglUniform3f(shader_lighting_point.uniforms.u_lightPosition3f, lightPosX, lightPosY, lightPosZ);
					DrawUtils.drawStandardQuad3D();
				}else {
					float radius = dl.radius;
					tmpVector1.set(lightPosX, lightPosY, lightPosZ);
					if(DeferredStateManager.currentGBufferFrustum.testSphere(tmpVector1, radius)) {
						tmpMatrix1.setIdentity();
						Matrix4f.translate(tmpVector1, tmpMatrix1, tmpMatrix1);
						tmpVector1.set(radius, radius, radius);
						Matrix4f.scale(tmpVector1, tmpMatrix1, tmpMatrix1);
						Matrix4f.mul(tmpMatrixViewProj, tmpMatrix1, tmpMatrix1);
						uniformMatrixHelper(shader_lighting_point.uniforms.u_modelViewProjMatrix4f, tmpMatrix1);
						_wglUniform3f(shader_lighting_point.uniforms.u_lightColor3f, dl.red, dl.green, dl.blue);
						_wglUniform3f(shader_lighting_point.uniforms.u_lightPosition3f, lightPosX, lightPosY, lightPosZ);
						pointLightMesh.drawMeshVAO();
					}
				}
			}

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER DYNAMIC LIGHTS");
		}

		DynamicLightManager.lightRenderList.clear();

		// ================== END HDR LIGHTING PASS ================== //

		GlStateManager.disableBlend();

		if(reprojectionEngineEnable || config.realisticWater) {

			// =========== SAVE REPROJECTION DATA FOR NEXT FRAME ============= //

			_wglBindFramebuffer(_GL_READ_FRAMEBUFFER, lightingHDRFramebuffer);
			_wglBindFramebuffer(_GL_DRAW_FRAMEBUFFER, lastFrameGBufferFramebuffer);
			_wglBlitFramebuffer(0, 0, currentWidth, currentHeight, 0, 0, currentWidth, currentHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): SAVE REPROJECTION DATA FOR NEXT FRAME");

		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);

		// =================== RENDER SKYBOX MESH =================== //

		if(dim == 0) {
			GlStateManager.enableDepth();
			GlStateManager.setActiveTexture(GL_TEXTURE2);
			GlStateManager.bindTexture(CloudRenderWorker.cloudOcclusionTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE1);
			CloudRenderWorker.bindParaboloid();
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(atmosphereHDRFramebufferColorTexture);
			shader_skybox_render.useProgram();
			uniformMatrixHelper(shader_skybox_render.uniforms.u_viewMatrix4f, DeferredStateManager.viewMatrix);
			uniformMatrixHelper(shader_skybox_render.uniforms.u_projMatrix4f, DeferredStateManager.projMatrix);
			_wglUniform3f(shader_skybox_render.uniforms.u_sunDirection3f, -currentSunAngle.x, -currentSunAngle.y, -currentSunAngle.z);
			float mag = 25.0f;
			float[] sunRGB2 = TemperaturesLUT.getColorTemperature((int)sunKelvin - 1000);
			_wglUniform3f(shader_skybox_render.uniforms.u_sunColor3f, sunRGB2[0] * mag, sunRGB2[1] * mag, sunRGB2[2] * mag);
			if (mc.theWorld.getLastLightningBolt() > 0) {
				float f = 0.3f + fff;
				_wglUniform4f(shader_skybox_render.uniforms.u_lightningColor4f, 0.02f * f, 0.02f * f, 0.02f * f, 1.0f - f * 0.25f);
			}else {
				_wglUniform4f(shader_skybox_render.uniforms.u_lightningColor4f, 0.0f, 0.0f, 0.0f, 1.0f);
			}
			skybox.drawFull();
	
			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER SKYBOX MESH");
		}else if(dim == 1) {
			GlStateManager.enableDepth();

			GlStateManager.setActiveTexture(GL_TEXTURE0);
			mc.getTextureManager().bindTexture(locationEndSkyPng);

			if(shader_skybox_render_end == null) {
				shader_skybox_render_end = PipelineShaderSkyboxRenderEnd.compile();
				shader_skybox_render_end.loadUniforms();
			}

			shader_skybox_render_end.useProgram();
			uniformMatrixHelper(shader_skybox_render_end.uniforms.u_viewMatrix4f, DeferredStateManager.viewMatrix);
			uniformMatrixHelper(shader_skybox_render_end.uniforms.u_projMatrix4f, DeferredStateManager.projMatrix);
			_wglUniform2f(shader_skybox_render_end.uniforms.u_skyTextureScale2f, 4.0f, 4.0f);

			skybox.drawFull();

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER SKYBOX MESH");
		}

		if(dim == 0 && fff < 1.0f) {

			// ===================== RENDER MOON ====================== //

			Matrix4f moonMatrix = tmpMatrix2;
			moonMatrix.setIdentity();
			tmpVector3.set(-1.0f, -1.0f, 1.0f);
			Matrix4f.scale(tmpVector3, moonMatrix, moonMatrix);
			tmpVector3.set(0.0f, 0.0f, 1.0f);
			Matrix4f.rotate(2.7f, tmpVector3, moonMatrix, moonMatrix);
			tmpVector3.set(-1.0f, 0.0f, 0.0f);
			tmpVector4.set(currentSunAngle);
			tmpVector4.scale(-1.0f);
			Vector3f.cross(tmpVector3, tmpVector4, tmpVector1);
			Vector3f.cross(tmpVector4, tmpVector1, tmpVector3);
			moonMatrix = tmpMatrix1;
			moonMatrix.setIdentity();
			moonMatrix.m00 = tmpVector1.x;
			moonMatrix.m01 = tmpVector1.y;
			moonMatrix.m02 = tmpVector1.z;
			moonMatrix.m10 = tmpVector3.x;
			moonMatrix.m11 = tmpVector3.y;
			moonMatrix.m12 = tmpVector3.z;
			moonMatrix.m20 = tmpVector4.x;
			moonMatrix.m21 = tmpVector4.y;
			moonMatrix.m22 = tmpVector4.z;
			Matrix4f.mul(moonMatrix, tmpMatrix2, moonMatrix);
			
			GlStateManager.bindTexture(moonTextures);
			shader_moon_render.useProgram();
			
			uniformMatrixHelper(shader_moon_render.uniforms.u_modelMatrix4f, moonMatrix);
			uniformMatrixHelper(shader_moon_render.uniforms.u_viewMatrix4f, DeferredStateManager.viewMatrix);
			uniformMatrixHelper(shader_moon_render.uniforms.u_projMatrix4f, DeferredStateManager.projMatrix);
			float fffff = 0.1f + MathHelper.clamp_float((-currentSunAngle.y + 0.1f) * 8.0f, 0.0f, 0.5f);
			_wglUniform3f(shader_moon_render.uniforms.u_moonColor3f, 1.4f * fffff, 1.2f * fffff, 1.0f * fffff);
			
			float f = (float)(Minecraft.getMinecraft().theWorld.getWorldTime() - 18000f) / 24000f / 4f * 3.14159f;
			_wglUniform3f(shader_moon_render.uniforms.u_lightDir3f, MathHelper.sin(f), 0.0f, MathHelper.cos(f));
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
			
			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("combineGBuffersAndIlluminate(): RENDER MOON");
		}

		GlStateManager.disableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
	}

	public void loadLightSourceBucket(int relativeBlockX, int relativeBlockY, int relativeBlockZ) {
		int hw = lightSourceBucketsWidth / 2;
		int hh = lightSourceBucketsHeight / 2;
		int bucketX = (relativeBlockX >> 4) + hw;
		int bucketY = (relativeBlockY >> 4) + hh;
		int bucketZ = (relativeBlockZ >> 4) + hw;
		if(bucketX >= 0 && bucketY >= 0 && bucketZ >= 0 && bucketX < lightSourceBucketsWidth
				&& bucketY < lightSourceBucketsHeight && bucketZ < lightSourceBucketsWidth) {
			currentLightSourceBucket = lightSourceBuckets[bucketY * lightSourceBucketsWidth * lightSourceBucketsWidth
					+ bucketZ * lightSourceBucketsWidth + bucketX];
		}else {
			currentLightSourceBucket = null;
		}
		updateLightSourceUBO();
	}

	public ListSerial<DynamicLightInstance> getLightSourceBucketRelativeChunkCoords(int cx, int cy, int cz) {
		int hw = lightSourceBucketsWidth / 2;
		int hh = lightSourceBucketsHeight / 2;
		cx += hw;
		cy += hh;
		cz += hw;
		if(cx < 0 || cx >= lightSourceBucketsWidth || cy < 0 || cy >= lightSourceBucketsHeight || cz < 0
				|| cz >= lightSourceBucketsWidth) {
			return null;
		}else {
			return lightSourceBuckets[cy * lightSourceBucketsWidth * lightSourceBucketsWidth
					+ cz * lightSourceBucketsWidth + cx];
		}
	}

	public void addLightSourceToBucket(int cx, int cy, int cz, DynamicLightInstance dl) {
		ListSerial<DynamicLightInstance> lst = getLightSourceBucketRelativeChunkCoords(cx, cy, cz);
		if(lst != null) {
			lst.add(dl);
		}
	}

	public void bucketLightSource(float x, float y, float z, DynamicLightInstance dl) {
		int bucketX = MathHelper.floor_float(x / 16.0f);
		int bucketY = MathHelper.floor_float(y / 16.0f);
		int bucketZ = MathHelper.floor_float(z / 16.0f);
		addLightSourceToBucket(bucketX, bucketY, bucketZ, dl);
		int minX = bucketX, maxX = bucketX;
		int minY = bucketY, maxY = bucketY;
		int minZ = bucketZ, maxZ = bucketZ;
		float lightLocalX = x - (bucketX << 4);
		float lightLocalY = y - (bucketY << 4);
		float lightLocalZ = z - (bucketZ << 4);
		float radius = dl.radius;
		boolean outOfBounds = false;
		if(lightLocalX - radius < 0.0f) {
			minX -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX - 1, bucketY, bucketZ, dl);
		}
		if(lightLocalY - radius < 0.0f) {
			minY -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY - 1, bucketZ, dl);
		}
		if(lightLocalZ - radius < 0.0f) {
			minZ -= 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY, bucketZ - 1, dl);
		}
		if(lightLocalX + radius >= 16.0f) {
			maxX += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX + 1, bucketY, bucketZ, dl);
		}
		if(lightLocalY + radius >= 16.0f) {
			maxY += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY + 1, bucketZ, dl);
		}
		if(lightLocalZ + radius >= 16.0f) {
			maxZ += 1;
			outOfBounds = true;
			addLightSourceToBucket(bucketX, bucketY, bucketZ + 1, dl);
		}
		if(!outOfBounds) {
			return;
		}
		radius *= radius;
		for(int yy = minY; yy <= maxY; ++yy) {
			for(int zz = minZ; zz <= maxZ; ++zz) {
				for(int xx = minX; xx <= maxX; ++xx) {
					if((xx == bucketX ? 1 : 0) + (yy == bucketY ? 1 : 0) + (zz == bucketZ ? 1 : 0) > 1) {
						continue;
					}
					List<DynamicLightInstance> lst = getLightSourceBucketRelativeChunkCoords(xx, yy, zz);
					if(lst != null) {
						int bucketBoundsX = xx << 4;
						int bucketBoundsY = yy << 4;
						int bucketBoundsZ = zz << 4;
						if(testAabSphere(bucketBoundsX, bucketBoundsY, bucketBoundsZ, bucketBoundsX + 16, bucketBoundsY + 16, bucketBoundsZ + 16, x, y, z, radius)) {
							lst.add(dl);
						}
					}
				}
			}
		}
	}

	/**
	 * source: https://github.com/JOML-CI/JOML/blob/main/src/main/java/org/joml/Intersectionf.java
	 */
	public static boolean testAabSphere(float minX, float minY, float minZ, float maxX, float maxY, float maxZ,
			float centerX, float centerY, float centerZ, float radius2) {
		if (centerX < minX) {
			float d = (centerX - minX);
			radius2 -= d * d;
		} else if (centerX > maxX) {
			float d = (centerX - maxX);
			radius2 -= d * d;
		}
		if (centerY < minY) {
			float d = (centerY - minY);
			radius2 -= d * d;
		} else if (centerY > maxY) {
			float d = (centerY - maxY);
			radius2 -= d * d;
		}
		if (centerZ < minZ) {
			float d = (centerZ - minZ);
			radius2 -= d * d;
		} else if (centerZ > maxZ) {
			float d = (centerZ - maxZ);
			radius2 -= d * d;
		}
		return radius2 >= 0.0f;
	}

	public void updateLightSourceUBO() {
		if(currentLightSourceBucket == null) {
			currentBoundLightSourceBucket = null;
			if(isChunkLightingEnabled) {
				isChunkLightingEnabled = false;
				EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
				chunkLightingDataCopyBuffer.clear();
				chunkLightingDataCopyBuffer.putInt(0);
				chunkLightingDataCopyBuffer.flip();
				_wglBufferSubData(_GL_UNIFORM_BUFFER, 0, chunkLightingDataCopyBuffer);
			}
		}else {
			boolean isNew;
			if(!isChunkLightingEnabled) {
				isChunkLightingEnabled = true;
				isNew = true;
			}else {
				isNew = currentLightSourceBucket != currentBoundLightSourceBucket;
			}
			currentBoundLightSourceBucket = currentLightSourceBucket;
			if(isNew || currentBoundLightSourceBucket.eaglerCheck()) {
				populateLightSourceUBOFromBucket(currentBoundLightSourceBucket);
				currentBoundLightSourceBucket.eaglerResetCheck();
			}
		}
	}

	private static final List<DynamicLightInstance> tmpListLights = new ArrayList(32);
	private static final Comparator<DynamicLightInstance> comparatorLightRadius = (l1, l2) -> {
		return l1.radius < l2.radius ? 1 : -1;
	};

	private void populateLightSourceUBOFromBucket(List<DynamicLightInstance> lights) {
		int max = lights.size();
		if(max > MAX_LIGHTS_PER_CHUNK) {
			tmpListLights.clear();
			tmpListLights.addAll(lights);
			lights = tmpListLights;
			lights.sort(comparatorLightRadius);
			max = MAX_LIGHTS_PER_CHUNK;
		}
		chunkLightingDataCopyBuffer.clear();
		chunkLightingDataCopyBuffer.putInt(max);
		if(max > 0) {
			chunkLightingDataCopyBuffer.putInt(0); //padding
			chunkLightingDataCopyBuffer.putInt(0); //padding
			chunkLightingDataCopyBuffer.putInt(0); //padding
			for(int i = 0; i < max; ++i) {
				DynamicLightInstance dl = lights.get(i);
				chunkLightingDataCopyBuffer.putFloat((float)(dl.posX - currentRenderX));
				chunkLightingDataCopyBuffer.putFloat((float)(dl.posY - currentRenderY));
				chunkLightingDataCopyBuffer.putFloat((float)(dl.posZ - currentRenderZ));
				chunkLightingDataCopyBuffer.putInt(0); //padding
				chunkLightingDataCopyBuffer.putFloat(dl.red);
				chunkLightingDataCopyBuffer.putFloat(dl.green);
				chunkLightingDataCopyBuffer.putFloat(dl.blue);
				chunkLightingDataCopyBuffer.putInt(0); //padding
			}
		}
		chunkLightingDataCopyBuffer.flip();
		EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
		_wglBufferSubData(_GL_UNIFORM_BUFFER, 0, chunkLightingDataCopyBuffer);
	}

	public void beginDrawEnvMap() {
		DeferredStateManager.checkGLError("Pre: beginDrawEnvMap()");
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		DeferredStateManager.enableForwardRender();
		DeferredStateManager.enableParaboloidRender();
		DeferredStateManager.disableFog();
		GlStateManager.enableExtensionPipeline();
		updateForwardRenderWorldLightingData();
		EaglercraftGPU.bindGLUniformBuffer(buffer_worldLightingData);
		EaglercraftGPU.bindUniformBufferRange(0, buffer_worldLightingData, 0, worldLightingDataCopyBuffer.remaining());
		if(config.is_rendering_dynamicLights) {
			EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
			EaglercraftGPU.bindUniformBufferRange(1, buffer_chunkLightingData, 0, chunkLightingDataCopyBuffer.capacity());
		}
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, envMapFramebuffer);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GlStateManager.setActiveTexture(GL_TEXTURE10);
		GlStateManager.bindTexture(skyIrradianceTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		DeferredStateManager.checkGLError("Post: beginDrawEnvMap()");
	}

	public void beginDrawEnvMapTop(float eyeHeight) {
		DeferredStateManager.checkGLError("Pre: beginDrawEnvMapTop()");
		GlStateManager.loadIdentity();
		tmpMatrix1.setIdentity();
		tmpMatrix1.m32 = eyeHeight;
		Matrix4f.mul(tmpMatrix1, DeferredStateManager.paraboloidTopViewMatrix, tmpMatrix1);
		GlStateManager.getModelViewReference().load(tmpMatrix1);
		DeferredStateManager.passProjMatrix.setIdentity();
		DeferredStateManager.passInverseProjMatrix.setIdentity();
		++DeferredStateManager.passProjMatrixSerial;
		DeferredStateManager.passViewMatrix.load(tmpMatrix1);
		Matrix4f.invert(DeferredStateManager.passViewMatrix, DeferredStateManager.passInverseViewMatrix);
		++DeferredStateManager.passViewMatrixSerial;
		GlStateManager.viewport(0, 0, 128, 128);
		DeferredStateManager.checkGLError("Post: beginDrawEnvMapTop()");
	}

	public void beginDrawEnvMapSolid() {
		DeferredStateManager.checkGLError("Pre: beginDrawEnvMapSolid()");
		GlStateManager.disableBlend();
		bindEnvMapBlockTexture();
		DeferredStateManager.checkGLError("Post: beginDrawEnvMapSolid()");
	}

	public void beginDrawEnvMapTranslucent() {
		DeferredStateManager.checkGLError("Pre: beginDrawEnvMapTranslucent()");
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA, GL_ONE);
		bindEnvMapBlockTexture();
		DeferredStateManager.checkGLError("Post: beginDrawEnvMapTranslucent()");
	}

	private void bindEnvMapBlockTexture() {
		DeferredStateManager.checkGLError("Pre: bindEnvMapBlockTexture()");
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		if(config.is_rendering_shadowsSun_clamped > 0) {
			GlStateManager.bindTexture(sunShadowDepthBuffer);
		}else {
			GlStateManager.bindTexture(-1);
		}
		TextureManager mgr = mc.getTextureManager();
		GlStateManager.setActiveTexture(GL_TEXTURE10);
		GlStateManager.bindTexture(skyIrradianceTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(MetalsLUT.getGLTexture());
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		mgr.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.enableCull();
		DeferredStateManager.checkGLError("Post: bindEnvMapBlockTexture()");
	}

	public void beginDrawEnvMapBottom(float eyeHeight) {
		DeferredStateManager.checkGLError("Pre: beginDrawEnvMapBottom()");
		GlStateManager.loadIdentity();
		tmpMatrix1.setIdentity();
		tmpMatrix1.m32 = -eyeHeight;
		Matrix4f.mul(tmpMatrix1, DeferredStateManager.paraboloidBottomViewMatrix, tmpMatrix1);
		GlStateManager.getModelViewReference().load(tmpMatrix1);
		DeferredStateManager.passViewMatrix.load(tmpMatrix1);
		Matrix4f.invert(DeferredStateManager.passViewMatrix, DeferredStateManager.passInverseViewMatrix);
		++DeferredStateManager.passViewMatrixSerial;
		GlStateManager.viewport(0, 128, 128, 128);
		DeferredStateManager.checkGLError("Post: beginDrawEnvMapBottom()");
	}

	public void endDrawEnvMap() {
		DeferredStateManager.checkGLError("Pre: endDrawEnvMap()");
		DeferredStateManager.disableForwardRender();
		DeferredStateManager.disableParaboloidRender();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableExtensionPipeline();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		DeferredStateManager.checkGLError("Post: endDrawEnvMap()");
	}

	private void updateForwardRenderWorldLightingData() {
		worldLightingDataCopyBuffer.clear();
		worldLightingDataCopyBuffer.putFloat(-DeferredStateManager.currentSunLightAngle.x);
		worldLightingDataCopyBuffer.putFloat(-DeferredStateManager.currentSunLightAngle.y);
		worldLightingDataCopyBuffer.putFloat(-DeferredStateManager.currentSunLightAngle.z);
		worldLightingDataCopyBuffer.putFloat(-DeferredStateManager.currentSunAngle.y);
		float f = getSkyBrightnessParam();
		if(DeferredStateManager.currentSunAngle.y > 0.05f) { // moon:
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.x * 0.025f * f);
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.y * 0.025f * f);
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.z * 0.025f * f);
		}else {
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.x * f);
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.y * f);
			worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.z * f);
		}
		float lightningBoost = mc.theWorld.getLastLightningBolt() > 0 ? 1.0f : 0.0f;
		lightningBoost *= 0.3f + mc.theWorld.getRainStrength(partialTicks);
		worldLightingDataCopyBuffer.putFloat(getSkyBrightnessTimeParam() + lightningBoost);
		worldLightingDataCopyBuffer.putFloat((float)DeferredStateManager.fogLinearExp);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogDensity);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogNear);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogFar);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorDarkR);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorDarkG);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorDarkB);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorDarkA);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorLightR);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorLightG);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorLightB);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.fogColorLightA);
		float mul = 0.05f * MathHelper.clamp_float(-1.0f - DeferredStateManager.getSunHeight() * 20.0f, 0.0f, 1.0f) + 0.01f;
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.x * mul);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.y * mul);
		worldLightingDataCopyBuffer.putFloat(DeferredStateManager.currentSunLightColor.z * mul);
		worldLightingDataCopyBuffer.putInt(0);
		worldLightingDataCopyBuffer.putFloat(1.0f);
		worldLightingDataCopyBuffer.putFloat(1.0f);
		worldLightingDataCopyBuffer.putFloat(1.0f);
		worldLightingDataCopyBuffer.putFloat(1.0f);
		if(config.is_rendering_shadowsSun_clamped > 0) {
			tmpShadowLOD0MatrixTexSpace.store(worldLightingDataCopyBuffer);
			if(config.is_rendering_shadowsSun_clamped > 1) {
				tmpShadowLOD1MatrixTexSpace.store(worldLightingDataCopyBuffer);
				if(config.is_rendering_shadowsSun_clamped > 2) {
					tmpShadowLOD2MatrixTexSpace.store(worldLightingDataCopyBuffer);
				}
			}
		}
		worldLightingDataCopyBuffer.flip();
		EaglercraftGPU.bindGLUniformBuffer(buffer_worldLightingData);
		_wglBufferSubData(_GL_UNIFORM_BUFFER, 0, worldLightingDataCopyBuffer);
	}

	public void setForwardRenderLightFactors(float block, float sky, float sun, float dynamic) {
		worldLightingDataCopyBuffer.clear();
		worldLightingDataCopyBuffer.putFloat(block);
		worldLightingDataCopyBuffer.putFloat(sky);
		worldLightingDataCopyBuffer.putFloat(sun);
		worldLightingDataCopyBuffer.putFloat(dynamic);
		worldLightingDataCopyBuffer.flip();
		EaglercraftGPU.bindGLUniformBuffer(buffer_worldLightingData);
		_wglBufferSubData(_GL_UNIFORM_BUFFER, 96, worldLightingDataCopyBuffer);
	}

	private float getSkyBrightnessParam() {
		float fff = mc.theWorld.getRainStrength(partialTicks) * 0.9f;
		fff += mc.theWorld.getThunderStrength(partialTicks) * 0.05f;
		return 1.0f - fff;
	}

	private float getSkyBrightnessTimeParam() {
		return (2.0f + MathHelper.clamp_float(-DeferredStateManager.currentSunAngle.y * 8.0f, 0.0f, 1.5f)) * getSkyBrightnessParam();
	}

	public void beginDrawRealisticWaterMask() {
		DeferredStateManager.checkGLError("Pre: beginDrawRealisticWaterMask()");
		_wglBindFramebuffer(_GL_READ_FRAMEBUFFER, gBufferFramebuffer);
		_wglBindFramebuffer(_GL_DRAW_FRAMEBUFFER, realisticWaterMaskFramebuffer);
		_wglBlitFramebuffer(0, 0, currentWidth, currentHeight, 0, 0, currentWidth, currentHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterMaskFramebuffer);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		GlStateManager.enableDepth();
		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		DeferredStateManager.enableDrawRealisticWaterMask();
		GlStateManager.enableExtensionPipeline();
		DeferredStateManager.checkGLError("Post: beginDrawRealisticWaterMask()");
	}

	public void endDrawRealisticWaterMask() {
		DeferredStateManager.checkGLError("Pre: endDrawRealisticWaterMask()");
		GlStateManager.disableDepth();
		GlStateManager.disableCull();
		DeferredStateManager.disableDrawRealisticWaterMask();
		GlStateManager.disableExtensionPipeline();

		if(config.is_rendering_lightShafts) {

			// ================== RENDER LIGHT SHAFTS =================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, lightShaftsFramebuffer);
			GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);
			GlStateManager.setActiveTexture(GL_TEXTURE2);
			GlStateManager.bindTexture(dither8x8Texture);
			GlStateManager.setActiveTexture(GL_TEXTURE1);
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(realisticWaterDepthBuffer);
			shader_light_shafts_sample.useProgram();
			_wglUniform2f(shader_light_shafts_sample.uniforms.u_ditherScale2f, reprojectionTexWidth * 0.125f, reprojectionTexHeight * 0.125f);
			uniformMatrixHelper(shader_light_shafts_sample.uniforms.u_inverseViewProjMatrix4f, tmpMatrixInverseViewProj);
			_wglUniform3f(shader_light_shafts_sample.uniforms.u_eyePosition3f, DeferredStateManager.inverseViewMatrix.m30,
					DeferredStateManager.inverseViewMatrix.m31, DeferredStateManager.inverseViewMatrix.m32);
			Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix0, tmpShadowLOD0MatrixTexSpace);
			uniformMatrixHelper(shader_light_shafts_sample.uniforms.u_sunShadowMatrixLOD04f, tmpShadowLOD0MatrixTexSpace);
			if(config.is_rendering_shadowsSun_clamped > 1) {
				Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix1, tmpShadowLOD1MatrixTexSpace);
				uniformMatrixHelper(shader_light_shafts_sample.uniforms.u_sunShadowMatrixLOD14f, tmpShadowLOD1MatrixTexSpace);
				if(config.is_rendering_shadowsSun_clamped > 2) {
					Matrix4f.mul(tmpClipToTexSpaceMatLeft, DeferredStateManager.sunShadowMatrix2, tmpShadowLOD2MatrixTexSpace);
					uniformMatrixHelper(shader_light_shafts_sample.uniforms.u_sunShadowMatrixLOD24f, tmpShadowLOD2MatrixTexSpace);
				}
			}

			GlStateManager.enableBlend();
			GlStateManager.setBlendConstants(0.0f, 0.0f, 0.0f, 0.25f);

			GlStateManager.blendFunc(GL_CONSTANT_ALPHA, GL_ZERO);
			_wglUniform1f(shader_light_shafts_sample.uniforms.u_sampleStep1f, 0.0f);
			DrawUtils.drawStandardQuad2D();

			GlStateManager.blendFunc(GL_CONSTANT_ALPHA, GL_ONE);
			_wglUniform1f(shader_light_shafts_sample.uniforms.u_sampleStep1f, 1.0f);
			DrawUtils.drawStandardQuad2D();

			_wglUniform1f(shader_light_shafts_sample.uniforms.u_sampleStep1f, 2.0f);
			DrawUtils.drawStandardQuad2D();

			_wglUniform1f(shader_light_shafts_sample.uniforms.u_sampleStep1f, 3.0f);
			DrawUtils.drawStandardQuad2D();

			GlStateManager.disableBlend();

			DeferredStateManager.checkGLError("endDrawRealisticWaterMask(): RENDER LIGHT SHAFTS");
		}

		// =================== COMBINE NORMALS =================== //

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterCombinedNormalsFramebuffer);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		GlStateManager.bindTexture(gBufferNormalsTexture);
		TextureCopyUtil.blitTexture();

		GlStateManager.bindTexture(realisticWaterMaskTexture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
		TextureCopyUtil.blitTexture();
		GlStateManager.disableBlend();

		DeferredStateManager.checkGLError("endDrawRealisticWaterMask(): COMBINE NORMALS");

		// ================ REPROJ CONTROL AND FOG =============== //

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterControlFramebuffer);
		GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);

		GlStateManager.setActiveTexture(GL_TEXTURE7);
		GlStateManager.bindTexture(lastFrameDepthTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE6);
		GlStateManager.bindTexture(lastFrameColorTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE5);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterDepthBuffer);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterCombinedNormalsTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(gBufferDepthTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
		shader_realistic_water_control.useProgram();

		if(!reprojectionEngineEnable) {
			tmpVector1.set(-reprojectionViewerOffsetX, -reprojectionViewerOffsetY, -reprojectionViewerOffsetZ);
			tmpMatrix1.setIdentity();
			Matrix4f.translate(tmpVector1, tmpMatrix1, tmpMatrix1);
			Matrix4f.mul(DeferredStateManager.viewMatrix, tmpMatrix1, tmpMatrixViewReproject);
			Matrix4f.mul(tmpMatrixViewProj, tmpMatrix1, tmpMatrixViewProjReproject);
			Matrix4f.invert(tmpMatrixViewProjReproject, tmpMatrixInverseViewProjReproject);
		}

		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_inverseViewProjMatrix4f, tmpMatrixInverseViewProjReproject);
		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_reprojectionMatrix4f, tmpMatrixLastFrameViewProjReproject);

		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_projectionMatrix4f, DeferredStateManager.projMatrix);
		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
		Matrix4f.invert(tmpMatrixLastFrameProj, tmpMatrix1);
		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_lastInverseProjMatrix4f, tmpMatrix1);
		Matrix4f.invert(tmpMatrixLastFrameViewReproject, tmpMatrix1);
		Matrix4f.mul(tmpMatrixViewReproject, tmpMatrix1, tmpMatrix1);
		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_reprojectionInverseViewMatrix4f, tmpMatrix1);
		Matrix4f.invert(tmpMatrix1, tmpMatrix1);
		Matrix4f.mul(tmpMatrixLastFrameProj, tmpMatrix1, tmpMatrix1);
		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_viewToPreviousProjMatrix4f, tmpMatrix1);

		_wglUniform4f(shader_realistic_water_control.uniforms.u_nearFarPlane4f, DeferredStateManager.gbufferNearPlane,
				DeferredStateManager.gbufferFarPlane, DeferredStateManager.gbufferNearPlane * 2.0f,
				DeferredStateManager.gbufferFarPlane - DeferredStateManager.gbufferNearPlane);

		float fr = 0.03f;
		float fg = 0.06f;
		float fb = 0.20f;
		float ff = 0.1f;
		float fac = MathHelper.clamp_float(DeferredStateManager.currentSunAngle.y * -4.0f, 0.1f, 1.0f);
		_wglUniform4f(shader_realistic_water_control.uniforms.u_refractFogColor4f, fr * ff, fg * ff, fb * ff, fac);

		uniformMatrixHelper(shader_realistic_water_control.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);

		DrawUtils.drawStandardQuad2D();

		DeferredStateManager.checkGLError("endDrawRealisticWaterMask(): REPROJ CONTROL AND FOG");

		// =========== RUN SCREENSPACE REFLECTIONS ALGORITHM ============= //

		GlStateManager.setActiveTexture(GL_TEXTURE5);
		GlStateManager.bindTexture(lastFrameDepthTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		GlStateManager.bindTexture(lastFrameColorTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(realisticWaterCombinedNormalsTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(realisticWaterDepthBuffer);

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[1]);

		shader_reproject_ssr.useProgram();
		uniformMatrixHelper(shader_reproject_ssr.uniforms.u_inverseProjectionMatrix4f, DeferredStateManager.inverseProjMatrix);
		Matrix4f.mul(tmpMatrixLastFrameViewProjReproject, tmpMatrixInverseViewProjReproject, tmpMatrix1);
		Matrix4f.mul(tmpMatrix1, DeferredStateManager.projMatrix, tmpMatrix1);
		uniformMatrixHelper(shader_reproject_ssr.uniforms.u_lastProjectionMatrix4f, tmpMatrix1);
		Matrix4f.invert(tmpMatrix1, tmpMatrix1);
		matrixCopyBuffer.clear();
		matrixCopyBuffer.put(tmpMatrix1.m02);
		matrixCopyBuffer.put(tmpMatrix1.m03);
		matrixCopyBuffer.put(tmpMatrix1.m12);
		matrixCopyBuffer.put(tmpMatrix1.m13);
		matrixCopyBuffer.put(tmpMatrix1.m22);
		matrixCopyBuffer.put(tmpMatrix1.m23);
		matrixCopyBuffer.put(tmpMatrix1.m32);
		matrixCopyBuffer.put(tmpMatrix1.m33);
		matrixCopyBuffer.flip();
		_wglUniformMatrix4x2fv(shader_reproject_ssr.uniforms.u_lastInverseProjMatrix4x2f, false, matrixCopyBuffer);
		_wglUniform1f(shader_reproject_ssr.uniforms.u_sampleStep1f, 0.5f);

		DrawUtils.drawStandardQuad2D(); // sample 1

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[1]);

		DrawUtils.drawStandardQuad2D(); // sample 2

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[0]);

		DrawUtils.drawStandardQuad2D(); // sample 3

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[1]);

		DrawUtils.drawStandardQuad2D(); // sample 4

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterSSRFramebuffer[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(realisticWaterControlHitVectorTexture[0]);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[0]);

		DrawUtils.drawStandardQuad2D(); // sample 5

		DeferredStateManager.checkGLError("endDrawRealisticWaterMask(): RUN SCREENSPACE REFLECTIONS ALGORITHM");

		// ============== GENERATE WAVE NORMAL MAP ================ //

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterDisplacementMapFramebuffer);
		GlStateManager.viewport(0, 0, 256, 256);

		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(realisticWaterNoiseMap);

		shader_realistic_water_noise.useProgram();
		float waveTimer = (float)((System.currentTimeMillis() % 600000l) * 0.001);
		_wglUniform4f(shader_realistic_water_noise.uniforms.u_waveTimer4f, waveTimer, 0.0f, 0.0f, 0.0f);

		DrawUtils.drawStandardQuad2D();

		_wglBindFramebuffer(_GL_FRAMEBUFFER, realisticWaterNormalMapFramebuffer);

		GlStateManager.bindTexture(realisticWaterDisplacementMapTexture);

		shader_realistic_water_normals.useProgram();

		DrawUtils.drawStandardQuad2D();

		DeferredStateManager.checkGLError("endDrawRealisticWaterMask(): GENERATE WAVE NORMAL MAP");
	}

	public void applyGBufferFog() {
		DeferredStateManager.checkGLError("Pre: applyGBufferFog()");
		if(DeferredStateManager.fogLinearExp == 0) {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
			return;
		}
		_wglBindFramebuffer(_GL_READ_FRAMEBUFFER, lightingHDRFramebuffer);
		_wglBindFramebuffer(_GL_DRAW_FRAMEBUFFER, fogDepthCopyBuffer);
		_wglBlitFramebuffer(0, 0, currentWidth, currentHeight, 0, 0, currentWidth, currentHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
		if(config.is_rendering_lightShafts) {
			GlStateManager.setActiveTexture(GL_TEXTURE4);
			GlStateManager.bindTexture(lightShaftsTexture);
		}
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(skyIrradianceTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(fogDepthCopyTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(gBufferNormalsTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(gBufferDepthTexture);
		tmpMatrix1.load(DeferredStateManager.inverseViewMatrix);
		tmpMatrix1.m30 = tmpMatrix1.m31 = tmpMatrix1.m32 = 0.0f;
		Matrix4f.mul(tmpMatrix1, DeferredStateManager.inverseProjMatrix, tmpMatrix1);
		PipelineShaderGBufferFog fogShader;
		switch(DeferredStateManager.fogLinearExp) {
		case 1:
			fogShader = shader_colored_fog_linear;
			fogShader.useProgram();
			_wglUniform2f(fogShader.uniforms.u_linearFogParam2f, DeferredStateManager.fogNear, DeferredStateManager.fogFar);
			break;
		case 2:
			fogShader = shader_colored_fog_exp;
			fogShader.useProgram();
			_wglUniform1f(fogShader.uniforms.u_expFogDensity1f, DeferredStateManager.fogDensity);
			break;
		case 6:
			fogShader = shader_atmosphere_fog;
			fogShader.useProgram();
			_wglUniform1f(fogShader.uniforms.u_expFogDensity1f, DeferredStateManager.fogDensity);
			float mul = 0.05f * MathHelper.clamp_float(-1.0f - DeferredStateManager.getSunHeight() * 20.0f, 0.0f, 1.0f) + 0.01f;
			_wglUniform3f(fogShader.uniforms.u_sunColorAdd3f, DeferredStateManager.currentSunLightColor.x * mul, DeferredStateManager.currentSunLightColor.y * mul, DeferredStateManager.currentSunLightColor.z * mul);
			break;
		default:
			throw new RuntimeException("Invalid fog type: " + DeferredStateManager.fogLinearExp);
		}
		uniformMatrixHelper(fogShader.uniforms.u_inverseViewProjMatrix4f, tmpMatrix1);
		_wglUniform4f(fogShader.uniforms.u_fogColorLight4f, DeferredStateManager.fogColorLightR,
				DeferredStateManager.fogColorLightG, DeferredStateManager.fogColorLightB,
				DeferredStateManager.fogColorLightA);
		_wglUniform4f(fogShader.uniforms.u_fogColorDark4f, DeferredStateManager.fogColorDarkR,
				DeferredStateManager.fogColorDarkG, DeferredStateManager.fogColorDarkB,
				DeferredStateManager.fogColorDarkA);
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
		DrawUtils.drawStandardQuad2D();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		DeferredStateManager.setHDRTranslucentPassBlendFunc();
		DeferredStateManager.checkGLError("Post: applyGBufferFog()");
	}

	public void beginDrawHDRTranslucent() {
		DeferredStateManager.checkGLError("Pre: beginDrawHDRTranslucent()");
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.1f);
		GlStateManager.enableBlend();
		DeferredStateManager.setHDRTranslucentPassBlendFunc();
		DeferredStateManager.enableForwardRender();
		GlStateManager.enableExtensionPipeline();
		updateForwardRenderWorldLightingData();
		EaglercraftGPU.bindGLUniformBuffer(buffer_worldLightingData);
		EaglercraftGPU.bindUniformBufferRange(0, buffer_worldLightingData, 0, worldLightingDataCopyBuffer.remaining());
		if(config.is_rendering_dynamicLights) {
			EaglercraftGPU.bindGLUniformBuffer(buffer_chunkLightingData);
			EaglercraftGPU.bindUniformBufferRange(1, buffer_chunkLightingData, 0, chunkLightingDataCopyBuffer.capacity());
		}
		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		DeferredStateManager.setPassMatrixToGBuffer();
		GlStateManager.setActiveTexture(GL_TEXTURE10);
		GlStateManager.bindTexture(skyIrradianceTexture);
		if(config.is_rendering_lightShafts) {
			GlStateManager.setActiveTexture(GL_TEXTURE11);
			GlStateManager.bindTexture(lightShaftsTexture);
		}
		if(config.is_rendering_useEnvMap) {
			GlStateManager.setActiveTexture(GL_TEXTURE5);
			GlStateManager.bindTexture(envMapColorTexture);
		}
		GlStateManager.setActiveTexture(GL_TEXTURE6);
		GlStateManager.bindTexture(brdfTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		DeferredStateManager.checkGLError("Post: beginDrawHDRTranslucent()");
	}

	public void beginDrawRealisticWaterSurface() {
		DeferredStateManager.checkGLError("Pre: beginDrawRealisticWaterSurface()");
		DeferredStateManager.enableDrawRealisticWaterRender();
		GlStateManager.setActiveTexture(GL_TEXTURE9);
		GlStateManager.bindTexture(realisticWaterNormalMapTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE8);
		GlStateManager.bindTexture(realisticWaterRefractionTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE7);
		GlStateManager.bindTexture(realisticWaterControlReflectionTexture[1]);
		GlStateManager.setActiveTexture(GL_TEXTURE5);
		GlStateManager.bindTexture(envMapSkyTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		if(config.is_rendering_shadowsSun_clamped > 0) {
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			if(config.is_rendering_shadowsSmoothed) {
				setLinear();
			}
		}else {
			GlStateManager.bindTexture(-1);
		}
		TextureManager mgr = mc.getTextureManager();
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(MetalsLUT.getGLTexture());
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		mgr.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.enableCull();
		DeferredStateManager.checkGLError("Post: beginDrawRealisticWaterSurface()");
	}

	public void endDrawRealisticWaterSurface() {
		DeferredStateManager.checkGLError("Pre: endDrawRealisticWaterSurface()");
		DeferredStateManager.disableDrawRealisticWaterRender();
		if(config.is_rendering_useEnvMap) {
			GlStateManager.setActiveTexture(GL_TEXTURE5);
			GlStateManager.bindTexture(envMapColorTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
		}
		DeferredStateManager.checkGLError("Post: endDrawRealisticWaterSurface()");
	}

	public void beginDrawTranslucentBlocks() {
		DeferredStateManager.checkGLError("Pre: beginDrawTranslucentBlocks()");
	}

	public void beginDrawGlassHighlights() {
		DeferredStateManager.checkGLError("Pre: beginDrawGlassHighlights()");
		DeferredStateManager.enableDrawGlassHighlightsRender();
		GlStateManager.depthMask(false);
		GlStateManager.enablePolygonOffset();
		GlStateManager.doPolygonOffset(0.25f, 1.0f);
		GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
		DeferredStateManager.checkGLError("Post: beginDrawGlassHighlights()");
	}

	public void endDrawGlassHighlights() {
		DeferredStateManager.checkGLError("Pre: endDrawGlassHighlights()");
		DeferredStateManager.disableDrawGlassHighlightsRender();
		GlStateManager.depthMask(true);
		GlStateManager.disablePolygonOffset();
		DeferredStateManager.setHDRTranslucentPassBlendFunc();
		DeferredStateManager.checkGLError("Post: endDrawGlassHighlights()");
	}

	public void beginDrawTranslucentEntities() {
		DeferredStateManager.checkGLError("Pre: beginDrawTranslucentEntities()");
		GlStateManager.setActiveTexture(GL_TEXTURE4);
		if(config.is_rendering_shadowsSun_clamped > 0) {
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			if(config.is_rendering_shadowsSmoothed) {
				setLinear();
			}
		}else {
			GlStateManager.bindTexture(-1);
		}
		TextureManager mgr = mc.getTextureManager();
		GlStateManager.setActiveTexture(GL_TEXTURE3);
		GlStateManager.bindTexture(MetalsLUT.getGLTexture());
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		mgr.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.enableCull();
		DeferredStateManager.checkGLError("Post: beginDrawTranslucentEntities()");
	}

	public void saveReprojData() {
		DeferredStateManager.checkGLError("Pre: saveReprojData()");
		if(reprojectionEngineEnable || config.is_rendering_realisticWater) {

			// =========== SAVE REPROJECTION DATA FOR NEXT FRAME ============= //

			tmpMatrixLastFrameProj.load(DeferredStateManager.projMatrix);
			tmpMatrixLastFrameViewReproject.load(tmpMatrixViewReproject);
			tmpMatrixLastFrameViewProjReproject.load(tmpMatrixViewProjReproject);

			GlStateManager.disableBlend();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, lastFrameFramebuffer);
			GlStateManager.viewport(0, 0, reprojectionTexWidth, reprojectionTexHeight);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
			_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
			TextureCopyUtil.alignPixelsTopLeft(reprojectionTexWidth << 1, reprojectionTexHeight << 1, reprojectionTexWidth, reprojectionTexHeight);
			TextureCopyUtil.blitTexture();
			GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);
			GlStateManager.enableDepth();
			GlStateManager.depthFunc(GL_ALWAYS);
			GlStateManager.depthMask(true);
			_wglDrawBuffers(GL_NONE);
			TextureCopyUtil.alignPixelsTopLeft(reprojectionTexWidth << 1, reprojectionTexHeight << 1, reprojectionTexWidth, reprojectionTexHeight);
			TextureCopyUtil.blitTextureDepth();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.depthFunc(GL_LEQUAL);
			_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);

			reprojectionPhase = (reprojectionPhase + 1) & 1;
			++reprojectionStartup;

			_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
			GlStateManager.viewport(0, 0, currentWidth, currentHeight);
			GlStateManager.enableBlend();
		}
		DeferredStateManager.checkGLError("Post: saveReprojData()");
	}

	public void beginDrawHandOverlay() {
		DeferredStateManager.checkGLError("Pre: beginDrawHandOverlay()");
		_wglBindFramebuffer(_GL_FRAMEBUFFER, handRenderFramebuffer);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		GlStateManager.clearDepth(1.0f);
		GlStateManager.depthMask(true);
		GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
		GlStateManager.enableDepth();
		DeferredStateManager.setDefaultMaterialConstants();
		DeferredStateManager.checkGLError("Post: beginDrawHandOverlay()");
	}

	public void endDrawHandOverlay() {
		DeferredStateManager.checkGLError("Pre: endDrawHandOverlay()");
		_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		shader_hand_depth_mask.useProgram();
		_wglDrawBuffers(GL_NONE);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(handRenderFramebufferDepthTexture);
		DrawUtils.drawStandardQuad2D();
		_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
		DeferredStateManager.checkGLError("Post: endDrawHandOverlay()");
	}

	public void endDrawHDRTranslucent() {
		DeferredStateManager.checkGLError("Pre: endDrawHDRTranslucent()");
		DeferredStateManager.disableForwardRender();
		DeferredStateManager.disableFog();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableExtensionPipeline();
		if(config.is_rendering_shadowsSun_clamped > 0 && config.is_rendering_shadowsSmoothed) {
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			setNearest();
		}
		DeferredStateManager.checkGLError("Post: endDrawHDRTranslucent()");
	}

	public void endDrawDeferred() {
		DeferredStateManager.checkGLError("Pre: endDrawDeferred()");

		if(config.is_rendering_lensFlares && mc.theWorld.provider.getDimensionId() == 0 &&
				DeferredStateManager.currentSunAngle.y < 0.2f && mc.theWorld.getRainStrength(partialTicks) < 1.0f) {

			// =============== CALCULATE SUN COORDINATES ================ //

			tmpVector2.x = DeferredStateManager.currentSunAngle.x * 10.0f;
			tmpVector2.y = DeferredStateManager.currentSunAngle.y * 10.0f;
			tmpVector2.z = DeferredStateManager.currentSunAngle.z * 10.0f;
			tmpVector2.w = 1.0f;
	
			Matrix4f.transform(tmpMatrixViewProj, tmpVector2, tmpVector2);
	
			tmpVector2.z /= tmpVector2.w;
			float margin = 0.2f;
			if (tmpVector2.z <= -1.0f) {
				tmpVector2.x = DeferredStateManager.currentSunAngle.x * 10.0f;
				tmpVector2.y = DeferredStateManager.currentSunAngle.y * 10.0f;
				tmpVector2.z = DeferredStateManager.currentSunAngle.z * 10.0f;
				tmpVector2.w = 0.0f;
				Matrix4f.transform(tmpMatrixViewProj, tmpVector2, tmpVector2);
				tmpVector2.x /= tmpVector2.w;
				tmpVector2.y /= tmpVector2.w;
				if (tmpVector2.x < 1.0f + margin && tmpVector2.x > -1.0f - margin && tmpVector2.y < 1.0f + margin
						&& tmpVector2.y > -1.0f - margin) {

					// ============ CALCULATE DEPTH SUN OCCLUSION ============ //
		
					_wglBindFramebuffer(_GL_FRAMEBUFFER, sunOcclusionValueFramebuffer);
					GlStateManager.viewport(0, 0, 1, 1);

					GlStateManager.setActiveTexture(GL_TEXTURE1);
					GlStateManager.bindTexture(CloudRenderWorker.cloudOcclusionTexture);
					GlStateManager.setActiveTexture(GL_TEXTURE0);
					GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);

					float fov = 90.0f / mc.entityRenderer.getFOVModifier(partialTicks, true);
					float radius = 0.05f * fov;
					float aspectRatio = (float)currentHeight / (float)currentWidth;
		
					tmpMatrix3.setIdentity();
					tmpMatrix3.m00 = aspectRatio * radius;
					tmpMatrix3.m11 = radius;
					tmpMatrix3.m20 = tmpVector2.x * 0.5f + 0.5f;
					tmpMatrix3.m21 = tmpVector2.y * 0.5f + 0.5f;
		
					shader_lens_sun_occlusion.useProgram();

					uniformMatrixHelper(shader_lens_sun_occlusion.uniforms.u_sampleMatrix3f, tmpMatrix3);

					DrawUtils.drawStandardQuad2D();

					DeferredStateManager.checkGLError("endDrawDeferred(): CALCULATE DEPTH SUN OCCLUSION");

					// ============ RENDER SUN LENS FLARES MESHES ============ //

					_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
					GlStateManager.viewport(0, 0, currentWidth, currentHeight);

					LensFlareMeshRenderer.drawLensFlares(tmpVector2.x, tmpVector2.y);

					DeferredStateManager.checkGLError("endDrawDeferred(): RENDER SUN LENS FLARES MESHES");
				}
			}
		}

		// ================ DOWNSCALE AND AVERAGE LUMA =============== //

		long millis = System.currentTimeMillis();
		if(millis - lastExposureUpdate > 33l) {
			if(lumaAvgDownscaleFramebuffers.length == 0) {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, exposureBlendFramebuffer);
				GlStateManager.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.clear(GL_COLOR_BUFFER_BIT);
			}else {
				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
				setLinear();
				
				int iw = currentWidth;
				int ih = currentHeight;
				int iw2 = 0, ih2 = 0, iw3 = 0, ih3 = 0;
				for(int i = 0; i < lumaAvgDownscaleFramebuffers.length; ++i) {
					iw2 = iw >> 2;
					ih2 = ih >> 2;
					// cheap way to round up:
					iw3 = ((iw & 3) != 0) ? (iw2 + 1) : iw2;
					ih3 = ((ih & 3) != 0) ? (ih2 + 1) : ih2;
					_wglBindFramebuffer(_GL_FRAMEBUFFER, lumaAvgDownscaleFramebuffers[i]);
					
					if(i == 0) {
						shader_post_exposure_avg_luma.useProgram();
						_wglUniform4f(shader_post_exposure_avg_luma.uniforms.u_sampleOffset4f, 1.0f / iw3, 1.0f / ih3, 4.0f / iw, 4.0f / ih);
					}else {
						shader_post_exposure_avg.useProgram();
						GlStateManager.bindTexture(lumaAvgDownscaleTexture[i - 1]);
						_wglUniform4f(shader_post_exposure_avg.uniforms.u_sampleOffset4f, 1.0f / iw3, 1.0f / ih3, 4.0f / iw, 4.0f / ih);
					}

					GlStateManager.viewport(0, 0, iw3, ih3);

					DrawUtils.drawStandardQuad2D();

					iw = iw2;
					ih = ih2;
				}

				GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
				setNearest();

				_wglBindFramebuffer(_GL_FRAMEBUFFER, exposureBlendFramebuffer);
				GlStateManager.viewport(0, 0, 1, 1);

				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_CONSTANT_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);

				GlStateManager.setBlendConstants(0.0f, 0.0f, 0.0f, Math.min((float)((millis - lastExposureUpdate) * 0.001), 1.0f));

				GlStateManager.setActiveTexture(GL_TEXTURE0);
				GlStateManager.bindTexture(lumaAvgDownscaleTexture[lumaAvgDownscaleTexture.length - 1]);

				shader_post_exposure_final.useProgram();
				_wglUniform2f(shader_post_exposure_final.uniforms.u_inputSize2f, 1.0f / iw3, 1.0f / ih3);

				DrawUtils.drawStandardQuad2D();

				GlStateManager.disableBlend();

				lastExposureUpdate = millis;
			}

			DeferredStateManager.checkGLError("endDrawDeferred(): DOWNSCALE AND AVERAGE LUMA");
		}

		if(config.is_rendering_bloom) {

			// ==================== BLOOM: BRIGHT PASS ==================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomBrightPassFramebuffer);
			GlStateManager.viewport(0, 0, bloomBrightPassTextureW, bloomBrightPassTextureH);
			boolean flag = bloomBrightPassTextureW != currentWidth || bloomBrightPassTextureH != currentHeight;
			GlStateManager.setActiveTexture(GL_TEXTURE3);
			GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE2);
			GlStateManager.bindTexture(gBufferMaterialTexture);
			if(flag) {
				setLinear();
			}
			GlStateManager.setActiveTexture(GL_TEXTURE1);
			GlStateManager.bindTexture(exposureBlendTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
			if(flag) {
				setLinear();
			}
			shader_post_bloom_bright.useProgram();
			_wglUniform4f(shader_post_bloom_bright.uniforms.u_outputSize4f, bloomBrightPassTextureW, bloomBrightPassTextureH, (flag ? 2.0f : 1.0f) / currentWidth, (flag ? 2.0f : 1.0f) / currentHeight);
			DrawUtils.drawStandardQuad2D();
			if(flag) {
				setNearest();
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				setNearest();
				GlStateManager.setActiveTexture(GL_TEXTURE0);
			}

			DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: BRIGHT PASS");

			// ==================== BLOOM: DOWNSCALE A ==================== //

			int bloomStageW = bloomBrightPassTextureW;
			int bloomStageH = bloomBrightPassTextureH;
			int texx = bloomBrightPassTexture;
			if(bloomStageW > 300 && bloomStageH > 170) {
				bloomStageW >>= 1;
				bloomStageH >>= 1;
				_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomDownscaleAFramebuffer);
				GlStateManager.viewport(0, 0, bloomStageW, bloomStageH);
				GlStateManager.bindTexture(texx);
				texx = bloomDownscaleATexture;
				TextureCopyUtil.alignPixels(bloomStageW, bloomStageH, 0.5f, 0.5f);
				TextureCopyUtil.blitTexture();

				DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: DOWNSCALE A");

				if(bloomStageW > 300 && bloomStageH > 170) {
					
					// ==================== BLOOM: DOWNSCALE B ==================== //
					
					bloomStageW >>= 1;
					bloomStageH >>= 1;
					_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomDownscaleBFramebuffer);
					GlStateManager.viewport(0, 0, bloomStageW, bloomStageH);
					GlStateManager.bindTexture(texx);
					texx = bloomDownscaleBTexture;
					TextureCopyUtil.alignPixels(bloomStageW, bloomStageH, 0.5f, 0.5f);
					TextureCopyUtil.blitTexture();

					DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: DOWNSCALE B");
				}
			}

			// ===================== BLOOM: HORZ BLUR ===================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomHBlurFramebuffer);
			GlStateManager.viewport(0, 0, bloomBlurTextureW, bloomBlurTextureH);
			flag = bloomBrightPassTextureW != bloomBlurTextureW || bloomBrightPassTextureH != bloomBlurTextureH;
			GlStateManager.bindTexture(texx);
			shader_post_bloom_blur.useProgram();
			_wglUniform2f(shader_post_bloom_blur.uniforms.u_sampleOffset2f, (flag ? 2.0f : 1.0f) / bloomStageW, 0.0f);
			_wglUniform4f(shader_post_bloom_blur.uniforms.u_outputSize4f, bloomBlurTextureW, bloomBlurTextureH, (flag ? 2.0f : 1.0f) / bloomStageW, (flag ? 2.0f : 1.0f) / bloomStageH);
			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: HORZ BLUR");

			// ===================== BLOOM: VERT BLUR ===================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, bloomVBlurFramebuffer);
			GlStateManager.bindTexture(bloomHBlurTexture);
			shader_post_bloom_blur.useProgram();
			_wglUniform2f(shader_post_bloom_blur.uniforms.u_sampleOffset2f, 0.0f, 1.0f / bloomBlurTextureH);
			_wglUniform4f(shader_post_bloom_blur.uniforms.u_outputSize4f, bloomBlurTextureW, bloomBlurTextureH, 1.0f / bloomBlurTextureW, 1.0f / bloomBlurTextureH);
			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: VERT BLUR");

			// ======================== BLOOM: MIX ======================= //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, lightingHDRFramebuffer);
			GlStateManager.viewport(0, 0, currentWidth, currentHeight);
			GlStateManager.bindTexture(bloomVBlurTexture);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_CONSTANT_ALPHA, GL_ONE, GL_ZERO, GL_ONE);
			GlStateManager.setBlendConstants(0.0f, 0.0f, 0.0f, 0.25f);
			TextureCopyUtil.blitTexture();
			GlStateManager.disableBlend();

			DeferredStateManager.checkGLError("endDrawDeferred(): BLOOM: MIX");
		}

		// ==================== APPLY TONEMAPPING ==================== //

		float exposure = 1.0f;

		if(config.is_rendering_fxaa) {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, tonemapOutputFramebuffer);
		}else {
			if(config.is_rendering_lensDistortion) {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, lensDistortFramebuffer);
			}else {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			}
		}
		GlStateManager.viewport(0, 0, currentWidth, currentHeight);
		shader_post_tonemap.useProgram();
		GlStateManager.disableBlend();
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(dither8x8Texture);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(exposureBlendTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(lightingHDRFramebufferColorTexture);
		_wglUniform3f(shader_post_tonemap.uniforms.u_exposure3f, exposure, exposure, exposure);
		_wglUniform2f(shader_post_tonemap.uniforms.u_ditherScale2f, currentWidth / 8.0f, currentHeight / 8.0f);
		DrawUtils.drawStandardQuad2D();
		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(-1);
		GlStateManager.setActiveTexture(GL_TEXTURE0);

		DeferredStateManager.checkGLError("endDrawDeferred(): APPLY TONEMAPPING");

		if(config.is_rendering_fxaa) {
			
			// ======================= APPLY FXAA ======================== //

			if(config.is_rendering_lensDistortion) {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, lensDistortFramebuffer);
			}else {
				_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			}
			shader_post_fxaa.useProgram();
			GlStateManager.bindTexture(tonemapOutputTexture);
			_wglUniform2f(shader_post_fxaa.uniforms.u_screenSize2f, 1.0f / currentWidth, 1.0f / currentHeight);
			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("endDrawDeferred(): APPLY FXAA");
		}
		
		if(config.is_rendering_lensDistortion) {
			
			// ================= APPLY LENS DISTORTION ================== //

			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(lensDistortTexture);
			shader_post_lens_distort.useProgram();
			DrawUtils.drawStandardQuad2D();

			DeferredStateManager.checkGLError("endDrawDeferred(): APPLY LENS DISTORTION");
		}
		
		// =========== BLIT WORLD DEPTH BUFFER TO OUTPUT ============= //

		if(EagRuntime.getPlatformType() == EnumPlatformType.DESKTOP) {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			GlStateManager.enableDepth();
			GlStateManager.depthFunc(GL_ALWAYS);
			GlStateManager.depthMask(true);
			GlStateManager.bindTexture(lightingHDRFramebufferDepthTexture);
			TextureCopyUtil.blitTextureDepth();
			GlStateManager.disableDepth();
			GlStateManager.depthFunc(GL_LEQUAL);
			GlStateManager.depthMask(false);
		}else {
			_wglBindFramebuffer(_GL_READ_FRAMEBUFFER, lightingHDRFramebuffer);
			_wglBindFramebuffer(_GL_DRAW_FRAMEBUFFER, null);
			_wglBlitFramebuffer(0, 0, currentWidth, currentHeight, 0, 0, currentWidth, currentHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		}

		DeferredStateManager.checkGLError("endDrawDeferred(): BLIT WORLD DEPTH BUFFER TO OUTPUT");

		// ================= OPTIONAL DEBUG OUTPUT =================== //

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		drawDebugViewIfEnabled();

		for(int i = 0; i < lightSourceBuckets.length; ++i) {
			lightSourceBuckets[i].clear();
		}

		DeferredStateManager.checkGLError("endDrawDeferred(): OPTIONAL DEBUG OUTPUT");
	}

	static void uniformMatrixHelper(IUniformGL uniform, Matrix4f matrix) {
		matrixCopyBuffer.clear();
		matrix.store(matrixCopyBuffer);
		matrixCopyBuffer.flip();
		_wglUniformMatrix4fv(uniform, false, matrixCopyBuffer);
	}

	static void uniformMatrixHelper(IUniformGL uniform, Matrix3f matrix) {
		matrixCopyBuffer.clear();
		matrix.store(matrixCopyBuffer);
		matrixCopyBuffer.flip();
		_wglUniformMatrix3fv(uniform, false, matrixCopyBuffer);
	}

	public static void setNearest() {
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	}

	public static void setLinear() {
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}

	public PipelineShaderGBufferDebugView useDebugViewShader(int idx) {
		PipelineShaderGBufferDebugView dbgShader = shader_gbuffer_debug_view[idx];
		if(dbgShader == null) {
			shader_gbuffer_debug_view[idx] = dbgShader = PipelineShaderGBufferDebugView.compile(idx);
			dbgShader.loadUniforms();
		}
		dbgShader.useProgram();
		return dbgShader;
	}

	private void drawDebugViewIfEnabled() {
		if(DebugFramebufferView.debugViewShown) {
			DebugFramebufferView.renderDebugView();
		}
	}

	public void destroy() {
		DeferredStateManager.checkGLError("Pre: destroy()");
		if(gBufferFramebuffer != null) {
			_wglDeleteFramebuffer(gBufferFramebuffer);
			gBufferFramebuffer = null;
		}
		if(gBufferDiffuseTexture != -1) {
			GlStateManager.bindTexture(gBufferDiffuseTexture);
			gBufferDiffuseTexture = -1;
		}
		if(gBufferNormalsTexture != -1) {
			GlStateManager.bindTexture(gBufferNormalsTexture);
			gBufferNormalsTexture = -1;
		}
		if(gBufferMaterialTexture != -1) {
			GlStateManager.bindTexture(gBufferMaterialTexture);
			gBufferMaterialTexture = -1;
		}
		if(gBufferDepthTexture != -1) {
			GlStateManager.bindTexture(gBufferDepthTexture);
			gBufferDepthTexture = -1;
		}
		if(sunShadowFramebuffer != null) {
			_wglDeleteFramebuffer(sunShadowFramebuffer);
			sunShadowFramebuffer = null;
		}
		if(sunShadowDepthBuffer != -1) {
			GlStateManager.deleteTexture(sunShadowDepthBuffer);
			sunShadowDepthBuffer = -1;
		}
		if(sunLightingShadowFramebuffer != null) {
			_wglDeleteFramebuffer(sunLightingShadowFramebuffer);
			sunLightingShadowFramebuffer = null;
		}
		if(sunLightingShadowTexture != -1) {
			GlStateManager.deleteTexture(sunLightingShadowTexture);
			sunLightingShadowTexture = -1;
		}
		if(ssaoGenerateFramebuffer != null) {
			_wglDeleteFramebuffer(ssaoGenerateFramebuffer);
			ssaoGenerateFramebuffer = null;
		}
		if(ssaoGenerateTexture != -1) {
			GlStateManager.deleteTexture(ssaoGenerateTexture);
			ssaoGenerateTexture = -1;
			reprojectionTexWidth = -1;
			reprojectionTexHeight = -1;
		}
		if(shader_ssao_generate != null) {
			shader_ssao_generate.destroy();
			shader_ssao_generate = null;
		}
		if(ssaoNoiseTexture != -1) {
			GlStateManager.deleteTexture(ssaoNoiseTexture);
			ssaoNoiseTexture = -1;
		}
		for(int i = 0; i < 2; ++i) {
			if(reprojectionControlFramebuffer[i] != null) {
				_wglDeleteFramebuffer(reprojectionControlFramebuffer[i]);
				reprojectionControlFramebuffer[i] = null;
			}
			if(reprojectionControlSSAOTexture[i] != -1) {
				GlStateManager.deleteTexture(reprojectionControlSSAOTexture[i]);
				reprojectionControlSSAOTexture[i] = -1;
			}
			if(reprojectionSSRFramebuffer[i] != null) {
				_wglDeleteFramebuffer(reprojectionSSRFramebuffer[i]);
				reprojectionSSRFramebuffer[i] = null;
			}
			if(reprojectionSSRTexture[i] != -1) {
				GlStateManager.deleteTexture(reprojectionSSRTexture[i]);
				reprojectionSSRTexture[i] = -1;
			}
			if(reprojectionSSRHitVector[i] != -1) {
				GlStateManager.deleteTexture(reprojectionSSRHitVector[i]);
				reprojectionSSRHitVector[i] = -1;
			}
		}
		if(lastFrameFramebuffer != null) {
			_wglDeleteFramebuffer(lastFrameFramebuffer);
			lastFrameFramebuffer = null;
		}
		if(lastFrameColorTexture != -1) {
			GlStateManager.deleteTexture(lastFrameColorTexture);
			lastFrameColorTexture = -1;
		}
		if(lastFrameDepthTexture != -1) {
			GlStateManager.deleteTexture(lastFrameDepthTexture);
			lastFrameDepthTexture = -1;
		}
		if(gBufferQuarterFramebuffer != null) {
			_wglDeleteFramebuffer(gBufferQuarterFramebuffer);
			gBufferQuarterFramebuffer = null;
		}
		if(gBufferQuarterDepthTexture != -1) {
			GlStateManager.deleteTexture(gBufferQuarterDepthTexture);
			gBufferQuarterDepthTexture = -1;
		}
		if(lastFrameGBufferFramebuffer != null) {
			_wglDeleteFramebuffer(lastFrameGBufferFramebuffer);
			lastFrameGBufferFramebuffer = null;
		}
		if(lastFrameGBufferDepthTexture != -1) {
			GlStateManager.deleteTexture(lastFrameGBufferDepthTexture);
			lastFrameGBufferDepthTexture = -1;
		}
		if(lightingHDRFramebuffer != null) {
			_wglDeleteFramebuffer(lightingHDRFramebuffer);
			lightingHDRFramebuffer = null;
		}
		if(lightingHDRFramebufferColorTexture != -1) {
			GlStateManager.deleteTexture(lightingHDRFramebufferColorTexture);
			lightingHDRFramebufferColorTexture = -1;
		}
		if(lightingHDRFramebufferDepthTexture != -1) {
			GlStateManager.deleteTexture(lightingHDRFramebufferDepthTexture);
			lightingHDRFramebufferDepthTexture = -1;
		}
		if(handRenderFramebuffer != null) {
			_wglDeleteFramebuffer(handRenderFramebuffer);
			handRenderFramebuffer = null;
		}
		if(handRenderFramebufferDepthTexture != -1) {
			GlStateManager.deleteTexture(handRenderFramebufferDepthTexture);
			handRenderFramebufferDepthTexture = -1;
		}
		if(atmosphereHDRFramebuffer != null) {
			_wglDeleteFramebuffer(atmosphereHDRFramebuffer);
			atmosphereHDRFramebuffer = null;
		}
		if(atmosphereHDRFramebufferColorTexture != -1) {
			GlStateManager.deleteTexture(atmosphereHDRFramebufferColorTexture);
			atmosphereHDRFramebufferColorTexture = -1;
		}
		if(envMapAtmosphereFramebuffer != null) {
			_wglDeleteFramebuffer(envMapAtmosphereFramebuffer);
			envMapAtmosphereFramebuffer = null;
		}
		if(envMapAtmosphereTexture != -1) {
			GlStateManager.deleteTexture(envMapAtmosphereTexture);
			envMapAtmosphereTexture = -1;
		}
		if(envMapSkyFramebuffer != null) {
			_wglDeleteFramebuffer(envMapSkyFramebuffer);
			envMapSkyFramebuffer = null;
		}
		if(envMapSkyTexture != -1) {
			GlStateManager.deleteTexture(envMapSkyTexture);
			envMapSkyTexture = -1;
		}
		if(moonTextures != -1) {
			GlStateManager.deleteTexture(moonTextures);
			moonTextures = -1;
		}
		if(envMapFramebuffer != null) {
			_wglDeleteFramebuffer(envMapFramebuffer);
			envMapFramebuffer = null;
		}
		if(envMapColorTexture != -1) {
			GlStateManager.deleteTexture(envMapColorTexture);
			envMapColorTexture = -1;
		}
		if(envMapDepthTexture != -1) {
			GlStateManager.deleteTexture(envMapDepthTexture);
			envMapDepthTexture = -1;
		}
		if(atmosphereIrradianceFramebuffer != null) {
			_wglDeleteFramebuffer(atmosphereIrradianceFramebuffer);
			atmosphereIrradianceFramebuffer = null;
		}
		if(atmosphereIrradianceTexture != -1) {
			GlStateManager.deleteTexture(atmosphereIrradianceTexture);
			atmosphereIrradianceTexture = -1;
		}
		if(skyIrradianceFramebuffer != null) {
			_wglDeleteFramebuffer(skyIrradianceFramebuffer);
			skyIrradianceFramebuffer = null;
		}
		if(skyIrradianceTexture != -1) {
			GlStateManager.deleteTexture(skyIrradianceTexture);
			skyIrradianceTexture = -1;
		}
		if(tonemapOutputFramebuffer != null) {
			_wglDeleteFramebuffer(tonemapOutputFramebuffer);
			tonemapOutputFramebuffer = null;
		}
		if(tonemapOutputTexture != -1) {
			GlStateManager.deleteTexture(tonemapOutputTexture);
			tonemapOutputTexture = -1;
		}
		if(lensDistortFramebuffer != null) {
			_wglDeleteFramebuffer(lensDistortFramebuffer);
			lensDistortFramebuffer = null;
		}
		if(lensDistortTexture != -1) {
			GlStateManager.deleteTexture(lensDistortTexture);
			lensDistortTexture = -1;
		}
		if(lumaAvgDownscaleFramebuffers != null) {
			for(int i = 0; i < lumaAvgDownscaleFramebuffers.length; ++i) {
				_wglDeleteFramebuffer(lumaAvgDownscaleFramebuffers[i]);
			}
			lumaAvgDownscaleFramebuffers = null;
		}
		if(lumaAvgDownscaleTexture != null) {
			for(int i = 0; i < lumaAvgDownscaleTexture.length; ++i) {
				GlStateManager.deleteTexture(lumaAvgDownscaleTexture[i]);
			}
			lumaAvgDownscaleTexture = null;
		}
		if(exposureBlendFramebuffer != null) {
			_wglDeleteFramebuffer(exposureBlendFramebuffer);
			exposureBlendFramebuffer = null;
		}
		if(exposureBlendTexture != -1) {
			GlStateManager.deleteTexture(exposureBlendTexture);
			exposureBlendTexture = -1;
		}
		if(bloomBrightPassFramebuffer != null) {
			_wglDeleteFramebuffer(bloomBrightPassFramebuffer);
			bloomBrightPassFramebuffer = null;
		}
		if(bloomBrightPassTexture != -1) {
			GlStateManager.bindTexture(bloomBrightPassTexture);
			bloomBrightPassTexture = -1;
		}
		if(bloomHBlurFramebuffer != null) {
			_wglDeleteFramebuffer(bloomHBlurFramebuffer);
			bloomHBlurFramebuffer = null;
		}
		if(bloomHBlurTexture != -1) {
			GlStateManager.deleteTexture(bloomHBlurTexture);
			bloomHBlurTexture = -1;
		}
		if(bloomVBlurFramebuffer != null) {
			_wglDeleteFramebuffer(bloomVBlurFramebuffer);
			bloomVBlurFramebuffer = null;
		}
		if(bloomVBlurTexture != -1) {
			GlStateManager.deleteTexture(bloomVBlurTexture);
			bloomVBlurTexture = -1;
		}
		if(bloomDownscaleAFramebuffer != null) {
			_wglDeleteFramebuffer(bloomDownscaleAFramebuffer);
			bloomDownscaleAFramebuffer = null;
		}
		if(bloomDownscaleATexture != -1) {
			GlStateManager.deleteTexture(bloomDownscaleATexture);
			bloomDownscaleATexture = -1;
		}
		if(bloomDownscaleBFramebuffer != null) {
			_wglDeleteFramebuffer(bloomDownscaleBFramebuffer);
			bloomDownscaleBFramebuffer = null;
		}
		if(bloomDownscaleBTexture != -1) {
			GlStateManager.deleteTexture(bloomDownscaleBTexture);
			bloomDownscaleBTexture = -1;
		}
		if(sunOcclusionValueFramebuffer != null) {
			_wglDeleteFramebuffer(sunOcclusionValueFramebuffer);
			sunOcclusionValueFramebuffer = null;
		}
		if(sunOcclusionValueTexture != -1) {
			GlStateManager.deleteTexture(sunOcclusionValueTexture);
			sunOcclusionValueTexture = -1;
		}
		if(dither8x8Texture != -1) {
			GlStateManager.deleteTexture(dither8x8Texture);
			dither8x8Texture = -1;
		}
		if(shader_deferred_combine != null) {
			shader_deferred_combine.destroy();
			shader_deferred_combine = null;
		}
		if(shader_hand_depth_mask != null) {
			shader_hand_depth_mask.destroy();
			shader_hand_depth_mask = null;
		}
		if(brdfTexture != -1) {
			GlStateManager.deleteTexture(brdfTexture);
			brdfTexture = -1;
		}
		if(shader_lighting_sun != null) {
			shader_lighting_sun.destroy();
			shader_lighting_sun = null;
		}
		if(shader_shadows_sun != null) {
			shader_shadows_sun.destroy();
			shader_shadows_sun = null;
		}
		if(shader_light_shafts_sample != null) {
			shader_light_shafts_sample.destroy();
			shader_light_shafts_sample = null;
		}
		if(skybox != null) {
			skybox.destroy();
			skybox = null;
		}
		if(pointLightMesh != null) {
			pointLightMesh.destroy();
			pointLightMesh = null;
		}
		if(shader_skybox_atmosphere != null) {
			shader_skybox_atmosphere.destroy();
			shader_skybox_atmosphere = null;
		}
		if(shader_skybox_render != null) {
			shader_skybox_render.destroy();
			shader_skybox_render = null;
		}
		if(shader_lighting_point != null) {
			shader_lighting_point.destroy();
			shader_lighting_point = null;
		}
		if(shader_post_lens_distort != null) {
			shader_post_lens_distort.destroy();
			shader_post_lens_distort = null;
		}
		if(shader_post_tonemap != null) {
			shader_post_tonemap.destroy();
			shader_post_tonemap = null;
		}
		if(shader_post_exposure_avg != null) {
			shader_post_exposure_avg.destroy();
			shader_post_exposure_avg = null;
		}
		if(shader_post_exposure_avg_luma != null) {
			shader_post_exposure_avg_luma.destroy();
			shader_post_exposure_avg_luma = null;
		}
		if(shader_post_exposure_final != null) {
			shader_post_exposure_final.destroy();
			shader_post_exposure_final = null;
		}
		if(shader_post_bloom_bright != null) {
			shader_post_bloom_bright.destroy();
			shader_post_bloom_bright = null;
		}
		if(shader_post_bloom_blur != null) {
			shader_post_bloom_blur.destroy();
			shader_post_bloom_blur = null;
		}
		if(shader_lens_sun_occlusion != null) {
			shader_lens_sun_occlusion.destroy();
			shader_lens_sun_occlusion = null;
		}
		if(shader_realistic_water_control != null) {
			shader_realistic_water_control.destroy();
			shader_realistic_water_control = null;
		}
		if(shader_realistic_water_noise != null) {
			shader_realistic_water_noise.destroy();
			shader_realistic_water_noise = null;
		}
		if(shader_realistic_water_normals != null) {
			shader_realistic_water_normals.destroy();
			shader_realistic_water_normals = null;
		}
		if(shader_post_fxaa != null) {
			shader_post_fxaa.destroy();
			shader_post_fxaa = null;
		}
		if(shader_skybox_render_paraboloid != null) {
			shader_skybox_render_paraboloid.destroy();
			shader_skybox_render_paraboloid = null;
		}
		if(shader_skybox_render_paraboloid_noclouds != null) {
			shader_skybox_render_paraboloid_noclouds.destroy();
			shader_skybox_render_paraboloid_noclouds = null;
		}
		if(shader_skybox_render_end != null) {
			shader_skybox_render_end.destroy();
			shader_skybox_render_end = null;
		}
		for(int i = 0; i < 3; ++i) {
			if(shader_skybox_irradiance[i] != null) {
				shader_skybox_irradiance[i].destroy();
				shader_skybox_irradiance[i] = null;
			}
		}
		if(shader_colored_fog_linear != null) {
			shader_colored_fog_linear.destroy();
			shader_colored_fog_linear = null;
		}
		if(shader_colored_fog_exp != null) {
			shader_colored_fog_exp.destroy();
			shader_colored_fog_exp = null;
		}
		if(shader_atmosphere_fog != null) {
			shader_atmosphere_fog.destroy();
			shader_atmosphere_fog = null;
		}
		if(shader_moon_render != null) {
			shader_moon_render.destroy();
			shader_moon_render = null;
		}
		if(shader_reproject_control != null) {
			shader_reproject_control.destroy();
			shader_reproject_control = null;
		}
		if(shader_reproject_ssr != null) {
			shader_reproject_ssr.destroy();
			shader_reproject_ssr = null;
		}
		if(realisticWaterMaskFramebuffer != null) {
			_wglDeleteFramebuffer(realisticWaterMaskFramebuffer);
			realisticWaterMaskFramebuffer = null;
		}
		if(realisticWaterMaskTexture != -1) {
			GlStateManager.deleteTexture(realisticWaterMaskTexture);
			realisticWaterMaskTexture = -1;
		}
		if(realisticWaterDepthBuffer != -1) {
			GlStateManager.deleteTexture(realisticWaterDepthBuffer);
			realisticWaterDepthBuffer = -1;
		}
		if(realisticWaterCombinedNormalsFramebuffer != null) {
			_wglDeleteFramebuffer(realisticWaterCombinedNormalsFramebuffer);
			realisticWaterCombinedNormalsFramebuffer = null;
		}
		if(realisticWaterCombinedNormalsTexture != -1) {
			GlStateManager.deleteTexture(realisticWaterCombinedNormalsTexture);
			realisticWaterCombinedNormalsTexture = -1;
		}
		if(realisticWaterRefractionTexture != -1) {
			GlStateManager.deleteTexture(realisticWaterRefractionTexture);
			realisticWaterRefractionTexture = -1;
		}
		if(realisticWaterControlFramebuffer != null) {
			_wglDeleteFramebuffer(realisticWaterControlFramebuffer);
			realisticWaterControlFramebuffer = null;
		}
		for(int i = 0; i < 2; ++i) {
			if(realisticWaterSSRFramebuffer[i] != null) {
				_wglDeleteFramebuffer(realisticWaterSSRFramebuffer[i]);
				realisticWaterSSRFramebuffer[i] = null;
			}
			if(realisticWaterControlReflectionTexture[i] != -1) {
				GlStateManager.deleteTexture(realisticWaterControlReflectionTexture[i]);
				realisticWaterControlReflectionTexture[i] = -1;
			}
			if(realisticWaterControlHitVectorTexture[i] != -1) {
				GlStateManager.deleteTexture(realisticWaterControlHitVectorTexture[i]);
				realisticWaterControlHitVectorTexture[i] = -1;
			}
		}
		if(realisticWaterNormalMapFramebuffer != null) {
			_wglDeleteFramebuffer(realisticWaterNormalMapFramebuffer);
			realisticWaterNormalMapFramebuffer = null;
		}
		if(realisticWaterNormalMapTexture != -1) {
			GlStateManager.deleteTexture(realisticWaterNormalMapTexture);
			realisticWaterNormalMapTexture = -1;
		}
		if(realisticWaterDisplacementMapFramebuffer != null) {
			_wglDeleteFramebuffer(realisticWaterDisplacementMapFramebuffer);
			realisticWaterDisplacementMapFramebuffer = null;
		}
		if(realisticWaterDisplacementMapTexture != -1) {
			GlStateManager.deleteTexture(realisticWaterDisplacementMapTexture);
			realisticWaterDisplacementMapTexture = -1;
		}
		if(realisticWaterNoiseMap != -1) {
			GlStateManager.deleteTexture(realisticWaterNoiseMap);
			realisticWaterNoiseMap = -1;
		}
		if(buffer_chunkLightingData != null) {
			_wglDeleteBuffers(buffer_chunkLightingData);
			buffer_chunkLightingData = null;
		}
		if(buffer_worldLightingData != null) {
			_wglDeleteBuffers(buffer_worldLightingData);
			buffer_worldLightingData = null;
		}
		if(worldLightingDataCopyBuffer != null) {
			EagRuntime.freeByteBuffer(worldLightingDataCopyBuffer);
			worldLightingDataCopyBuffer = null;
		}
		if(chunkLightingDataCopyBuffer != null) {
			EagRuntime.freeByteBuffer(chunkLightingDataCopyBuffer);
			chunkLightingDataCopyBuffer = null;
		}
		for(int i = 0; i < lightSourceBuckets.length; ++i) {
			lightSourceBuckets[i].clear();
		}
		currentLightSourceBucket = null;
		currentBoundLightSourceBucket = null;
		isChunkLightingEnabled = false;
		for(int i = 0; i < shader_gbuffer_debug_view.length; ++i) {
			if(shader_gbuffer_debug_view[i] != null) {
				shader_gbuffer_debug_view[i].destroy();
				shader_gbuffer_debug_view[i] = null;
			}
		}
		gbufferEffectRenderer.destroy();
		forwardEffectRenderer.destroy();
		DynamicLightManager.destroyAll();
		LensFlareMeshRenderer.destroy();
		CloudRenderWorker.destroy();
		FixedFunctionPipeline.loadExtensionPipeline(null);
		DeferredStateManager.checkGLError("Post: destroy()");
	}

	public void resetContextStateAfterException() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		NameTagRenderer.doRenderNameTags = false;
		DeferredStateManager.disableAll();
		DeferredStateManager.setDefaultMaterialConstants();
		GlStateManager.disableExtensionPipeline();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.disableShaderBlendAdd();
		GlStateManager.disableTexGen();
		GlStateManager.globalEnableBlend();
		DynamicLightManager.setIsRenderingLights(false);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.shadeModel(GL_SMOOTH);
		GlStateManager.clearDepth(1.0f);
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		GlStateManager.cullFace(GL_BACK);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.loadIdentity();
		if(config.is_rendering_shadowsSun_clamped > 0 && config.is_rendering_shadowsSmoothed) {
			GlStateManager.bindTexture(sunShadowDepthBuffer);
			_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
			setNearest();
		}
	}

	public static final boolean isSupported() {
		return EaglercraftGPU.checkHasHDRFramebufferSupport();
	}

	public static final String getReasonUnsupported() {
		if(!EaglercraftGPU.checkHasHDRFramebufferSupport()) {
			return I18n.format("shaders.gui.unsupported.reason.hdrFramebuffer");
		}else {
			return null;
		}
	}

	public static final void renderSuspended() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		GlStateManager.globalEnableBlend();
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		GlStateManager.clearColor(0.5f, 0.0f, 0.0f, 1.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int w = scaledresolution.getScaledWidth();
		mc.entityRenderer.setupOverlayRendering();
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
		String str = "Shaders Suspended";
		GlStateManager.translate((w - mc.fontRendererObj.getStringWidth(str) * 2) * 0.5f, 45.0f, 0.0f);
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		mc.fontRendererObj.drawStringWithShadow(str, 0, 0, 0xFFFFFF);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		str = "(check console)";
		GlStateManager.translate((w - mc.fontRendererObj.getStringWidth(str) * 1.5) * 0.5f, 80.0f, 0.0f);
		GlStateManager.scale(1.5f, 1.5f, 1.5f);
		mc.fontRendererObj.drawStringWithShadow(str, 0, 0, 0xFFFFFF);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		EagUtils.sleep(10l);
	}
}
