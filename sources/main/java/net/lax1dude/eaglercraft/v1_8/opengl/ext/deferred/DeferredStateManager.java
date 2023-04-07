package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

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
public class DeferredStateManager {

	public static float sunAngle = 45.0f; // realistic: 23.5f

	static boolean enableMaterialMapTexture = false;
	static boolean enableForwardRender = false;
	static boolean enableParaboloidRender = false;
	static boolean enableShadowRender = false;
	static boolean enableClipPlane = false;
	static boolean enableDrawWavingBlocks = false;
	static boolean enableDrawRealisticWaterMask = false;
	static boolean enableDrawRealisticWaterRender = false;
	static boolean enableDrawGlassHighlightsRender = false;

	static int materialConstantsSerial = 0;
	static float materialConstantsRoughness = 0.5f;
	static float materialConstantsMetalness = 0.02f;
	static float materialConstantsEmission = 0.0f;
	static boolean materialConstantsUseEnvMap = false;

	static int wavingBlockOffsetSerial = 0;
	static float wavingBlockOffsetX = 0.0f;
	static float wavingBlockOffsetY = 0.0f;
	static float wavingBlockOffsetZ = 0.0f;

	static int wavingBlockParamSerial = 0;
	static float wavingBlockParamX = 0.0f;
	static float wavingBlockParamY = 0.0f;
	static float wavingBlockParamZ = 0.0f;
	static float wavingBlockParamW = 0.0f;

	static int constantBlock = 0;

	static float clipPlaneY = 0.0f;

	static AxisAlignedBB shadowMapBounds = new AxisAlignedBB(-1, -1, -1, 1, 1, 1);

	static float gbufferNearPlane = 0.01f;
	static float gbufferFarPlane = 128.0f;

	static final Vector3f currentSunAngle = new Vector3f();
	static final Vector3f currentSunLightAngle = new Vector3f();
	static final Vector3f currentSunLightColor = new Vector3f();

	static int waterWindOffsetSerial = 0;
	static final Vector4f u_waterWindOffset4f = new Vector4f();

	private static final float[] matrixCopyBuffer = new float[16];
	static int viewMatrixSerial = -1;
	static int projMatrixSerial = -1;
	static int passViewMatrixSerial = -1;
	static int passProjMatrixSerial = -1;
	static boolean isShadowPassMatrixLoaded = false;
	static final Matrix4f viewMatrix = new Matrix4f();
	static final Matrix4f projMatrix = new Matrix4f();
	static final Matrix4f inverseViewMatrix = new Matrix4f();
	static final Matrix4f inverseProjMatrix = new Matrix4f();
	static final Matrix4f passViewMatrix = new Matrix4f();
	static final Matrix4f passProjMatrix = new Matrix4f();
	static final Matrix4f passInverseViewMatrix = new Matrix4f();
	static final Matrix4f passInverseProjMatrix = new Matrix4f();
	static final Matrix4f sunShadowMatrix0 = new Matrix4f();
	static final Matrix4f sunShadowMatrix1 = new Matrix4f();
	static final Matrix4f sunShadowMatrix2 = new Matrix4f();
	static final BetterFrustum currentGBufferFrustum = new BetterFrustum();
	static final Matrix4f paraboloidTopViewMatrix = new Matrix4f().rotate(-1.57f, new Vector3f(1.0f, 0.0f, 0.0f));
	static final Matrix4f paraboloidBottomViewMatrix = new Matrix4f().rotate(1.57f, new Vector3f(1.0f, 0.0f, 0.0f));

	public static ForwardRenderCallbackHandler forwardCallbackHandler = null;

	public static final ForwardRenderCallbackHandler forwardCallbackGBuffer = new ForwardRenderCallbackHandler();
	public static final ForwardRenderCallbackHandler forwardCallbackSun = new ForwardRenderCallbackHandler();

	public static boolean doCheckErrors = false;

	public static final boolean isDeferredRenderer() {
		return EaglerDeferredPipeline.instance != null;
	}

	public static final boolean isInDeferredPass() {
		return GlStateManager.isExtensionPipeline();
	}

	public static final boolean isInForwardPass() {
		return enableForwardRender && !enableShadowRender;
	}

	public static final boolean isInParaboloidPass() {
		return enableParaboloidRender;
	}

	public static final boolean isRenderingRealisticWater() {
		return EaglerDeferredPipeline.instance != null && EaglerDeferredPipeline.instance.config.is_rendering_realisticWater;
	}

	public static final boolean isRenderingGlassHighlights() {
		return EaglerDeferredPipeline.instance != null && EaglerDeferredPipeline.instance.config.is_rendering_useEnvMap;
	}

	public static final void setDefaultMaterialConstants() {
		materialConstantsRoughness = 0.5f;
		materialConstantsMetalness = 0.02f;
		materialConstantsEmission = 0.0f;
		++materialConstantsSerial;
	}

	public static final void startUsingEnvMap() {
		materialConstantsUseEnvMap = true;
	}

	public static final void endUsingEnvMap() {
		materialConstantsUseEnvMap = false;
	}

	public static final void reportForwardRenderObjectPosition(int centerX, int centerY, int centerZ) {
		EaglerDeferredPipeline instance = EaglerDeferredPipeline.instance;
		if(instance != null && enableForwardRender) {
			EaglerDeferredConfig cfg = instance.config;
			if(!cfg.is_rendering_dynamicLights || !cfg.shaderPackInfo.DYNAMIC_LIGHTS) {
				return;
			}
			instance.loadLightSourceBucket(centerX, centerY, centerZ);
		}
	}

	public static final void reportForwardRenderObjectPosition2(float x, float y, float z) {
		float posX = (float)((x + TileEntityRendererDispatcher.staticPlayerX) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerX / 16.0) << 4));
		float posY = (float)((y + TileEntityRendererDispatcher.staticPlayerY) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerY / 16.0) << 4));
		float posZ = (float)((z + TileEntityRendererDispatcher.staticPlayerZ) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerZ / 16.0) << 4));
		reportForwardRenderObjectPosition((int)posX, (int)posY, (int)posZ);
	}

	public static final void setHDRTranslucentPassBlendFunc() {
		GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ZERO);
	}

	public static final void enableMaterialTexture() {
		enableMaterialMapTexture = true;
	}

	public static final void disableMaterialTexture() {
		enableMaterialMapTexture = false;
	}

	public static final void enableForwardRender() {
		enableForwardRender = true;
	}

	public static final void disableForwardRender() {
		enableForwardRender = false;
	}

	public static final void enableParaboloidRender() {
		enableParaboloidRender = true;
	}

	public static final void disableParaboloidRender() {
		enableParaboloidRender = false;
	}

	public static final void enableShadowRender() {
		enableShadowRender = true;
	}

	public static final void disableShadowRender() {
		enableShadowRender = false;
	}

	public static final boolean isEnableShadowRender() {
		return enableShadowRender;
	}

	public static final void enableClipPlane() {
		enableClipPlane = true;
	}

	public static final void disableClipPlane() {
		enableClipPlane = false;
	}

	public static final void setClipPlaneY(float yValue) {
		clipPlaneY = yValue;
	}

	public static final void enableDrawWavingBlocks() {
		enableDrawWavingBlocks = true;
	}

	public static final void disableDrawWavingBlocks() {
		enableDrawWavingBlocks = false;
	}

	public static final boolean isEnableDrawWavingBlocks() {
		return enableDrawWavingBlocks;
	}

	public static final void enableDrawRealisticWaterMask() {
		enableDrawRealisticWaterMask = true;
	}

	public static final void disableDrawRealisticWaterMask() {
		enableDrawRealisticWaterMask = false;
	}

	public static final boolean isDrawRealisticWaterMask() {
		return enableDrawRealisticWaterMask;
	}

	public static final void enableDrawRealisticWaterRender() {
		enableDrawRealisticWaterRender = true;
	}

	public static final void disableDrawRealisticWaterRender() {
		enableDrawRealisticWaterRender = false;
	}

	public static final boolean isDrawRealisticWaterRender() {
		return enableDrawRealisticWaterRender;
	}

	public static final void enableDrawGlassHighlightsRender() {
		enableDrawGlassHighlightsRender = true;
	}

	public static final void disableDrawGlassHighlightsRender() {
		enableDrawGlassHighlightsRender = false;
	}

	public static final boolean isDrawGlassHighlightsRender() {
		return enableDrawGlassHighlightsRender;
	}

	public static final void setWavingBlockOffset(float x, float y, float z) {
		wavingBlockOffsetX = x;
		wavingBlockOffsetY = y;
		wavingBlockOffsetZ = z;
		++wavingBlockOffsetSerial;
	}

	public static final void setWavingBlockParams(float x, float y, float z, float w) {
		wavingBlockParamX = x;
		wavingBlockParamY = y;
		wavingBlockParamZ = z;
		wavingBlockParamW = w;
		++wavingBlockParamSerial;
	}

	public static final void setRoughnessConstant(float roughness) {
		materialConstantsRoughness = roughness;
		++materialConstantsSerial;
	}

	public static final void setMetalnessConstant(float metalness) {
		materialConstantsMetalness = metalness;
		++materialConstantsSerial;
	}

	public static final void setEmissionConstant(float emission) {
		materialConstantsEmission = emission;
		++materialConstantsSerial;
	}

	public static final void setBlockConstant(int blockId) {
		constantBlock = blockId;
	}

	public static final AxisAlignedBB getShadowMapBounds() {
		return shadowMapBounds;
	}

	public static final void setShadowMapBounds(AxisAlignedBB newShadowMapBounds) {
		shadowMapBounds = newShadowMapBounds;
	}

	public static final void loadGBufferViewMatrix() {
		loadPassViewMatrix();
		viewMatrix.load(passViewMatrix);
		inverseViewMatrix.load(passInverseViewMatrix);
		viewMatrixSerial = passViewMatrixSerial;
	}

	public static void loadGBufferProjectionMatrix() {
		loadPassProjectionMatrix();
		projMatrix.load(passProjMatrix);
		inverseProjMatrix.load(passInverseProjMatrix);
		projMatrixSerial = passProjMatrixSerial;
	}

	public static final void loadPassViewMatrix() {
		GlStateManager.getFloat(GL_MODELVIEW_MATRIX, matrixCopyBuffer);
		passViewMatrix.load(matrixCopyBuffer);
		Matrix4f.invert(passViewMatrix, passInverseViewMatrix);
		++passViewMatrixSerial;
		isShadowPassMatrixLoaded = false;
	}

	public static void loadPassProjectionMatrix() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		passProjMatrix.load(matrixCopyBuffer);
		Matrix4f.invert(passProjMatrix, passInverseProjMatrix);
		++passProjMatrixSerial;
	}

	public static final void loadShadowPassViewMatrix() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		passViewMatrix.load(matrixCopyBuffer);
		Matrix4f.invert(passViewMatrix, passInverseViewMatrix);
		passProjMatrix.setIdentity();
		++passViewMatrixSerial;
		isShadowPassMatrixLoaded = true;
	}

	public static final void setPassMatrixToGBuffer() {
		passViewMatrix.load(viewMatrix);
		passInverseViewMatrix.load(inverseViewMatrix);
		passProjMatrix.load(projMatrix);
		passInverseProjMatrix.load(inverseProjMatrix);
		++passViewMatrixSerial;
		++passProjMatrixSerial;
	}

	public static void setCurrentSunAngle(Vector3f vec) {
		currentSunAngle.set(vec);
		if(vec.y > 0.05f) {
			currentSunLightAngle.x = -vec.x;
			currentSunLightAngle.y = -vec.y;
			currentSunLightAngle.z = -vec.z;
		}else {
			currentSunLightAngle.set(vec);
		}
	}

	public static void setCurrentSunAngle(Vector4f vec) {
		currentSunAngle.set(vec);
		if(vec.y > 0.05f) {
			currentSunLightAngle.x = -vec.x;
			currentSunLightAngle.y = -vec.y;
			currentSunLightAngle.z = -vec.z;
		}else {
			currentSunLightAngle.set(vec);
		}
	}

	public static final void loadSunShadowMatrixLOD0() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix0.load(matrixCopyBuffer);
	}

	public static final void loadSunShadowMatrixLOD1() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix1.load(matrixCopyBuffer);
	}

	public static final void loadSunShadowMatrixLOD2() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix2.load(matrixCopyBuffer);
	}

	public static final Matrix4f getSunShadowMatrixLOD0() {
		return sunShadowMatrix0;
	}

	public static final Matrix4f getSunShadowMatrixLOD1() {
		return sunShadowMatrix1;
	}

	public static final Matrix4f getSunShadowMatrixLOD2() {
		return sunShadowMatrix2;
	}

	public static final void setGBufferNearFarPlanes(float zNear, float zFar) {
		gbufferNearPlane = zNear;
		gbufferFarPlane = zFar;
	}

	public static final void setWaterWindOffset(float sx, float sy, float fx, float fy) {
		++waterWindOffsetSerial;
		u_waterWindOffset4f.x = sx;
		u_waterWindOffset4f.y = sy;
		u_waterWindOffset4f.z = fx;
		u_waterWindOffset4f.w = fy;
	}

	static int fogLinearExp = 0;

	static float fogNear = 0.0f;
	static float fogFar = 100.0f;

	static float fogDensity = 0.0f;

	static float fogColorLightR = 1.0f;
	static float fogColorLightG = 1.0f;
	static float fogColorLightB = 1.0f;
	static float fogColorLightA = 1.0f;

	static float fogColorDarkR = 1.0f;
	static float fogColorDarkG = 1.0f;
	static float fogColorDarkB = 1.0f;
	static float fogColorDarkA = 1.0f;

	public static final void enableFogLinear(float near, float far, boolean atmosphere, float colorLightR,
			float colorLightG, float colorLightB, float colorLightA, float colorDarkR, float colorDarkG,
			float colorDarkB, float colorDarkA) {
		fogLinearExp = atmosphere ? 5 : 1;
		fogNear = near;
		fogFar = far;
		fogColorLightR = colorLightR;
		fogColorLightG = colorLightG;
		fogColorLightB = colorLightB;
		fogColorLightA = colorLightA;
		fogColorDarkR = colorDarkR;
		fogColorDarkG = colorDarkG;
		fogColorDarkB = colorDarkB;
		fogColorDarkA = colorDarkA;
	}

	public static final void enableFogExp(float density, boolean atmosphere, float colorLightR, float colorLightG,
			float colorLightB, float colorLightA, float colorDarkR, float colorDarkG, float colorDarkB,
			float colorDarkA) {
		fogLinearExp = atmosphere ? 6 : 2;
		fogDensity = density;
		fogColorLightR = colorLightR;
		fogColorLightG = colorLightG;
		fogColorLightB = colorLightB;
		fogColorLightA = colorLightA;
		fogColorDarkR = colorDarkR;
		fogColorDarkG = colorDarkG;
		fogColorDarkB = colorDarkB;
		fogColorDarkA = colorDarkA;
	}

	public static final void disableFog() {
		fogLinearExp = 0;
	}

	public static final void disableAll() {
		enableMaterialMapTexture = false;
		materialConstantsUseEnvMap = false;
		enableForwardRender = false;
		enableParaboloidRender = false;
		enableShadowRender = false;
		enableClipPlane = false;
		enableDrawWavingBlocks = false;
		fogLinearExp = 0;
		fogNear = 0.0f;
		fogFar = 100.0f;
		forwardCallbackHandler = null;
	}

	public static float getSunHeight() {
		return -currentSunAngle.y;
	}

	public static void checkGLError(String section) {
		if(!doCheckErrors) {
			return;
		}
		int i = EaglercraftGPU.glGetError();
		if(i != 0) {
			EaglerDeferredPipeline.logger.error("########## GL ERROR ##########");
			EaglerDeferredPipeline.logger.error("@ {}", section);
			do {
				EaglerDeferredPipeline.logger.error("#{} - {}", i, EaglercraftGPU.gluErrorString(i));
			}while((i = EaglercraftGPU.glGetError()) != 0);
			EaglerDeferredPipeline.logger.error("##############################");
		}
	}

}
