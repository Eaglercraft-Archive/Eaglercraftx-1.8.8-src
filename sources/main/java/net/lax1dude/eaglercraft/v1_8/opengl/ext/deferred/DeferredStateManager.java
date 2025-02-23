/*
 * Copyright (c) 2023 lax1dude, ayunami2000. All Rights Reserved.
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

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

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
	static float materialConstantsSubsurfScatting = 0.0f;
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

	public static boolean isDeferredRenderer() {
		return EaglerDeferredPipeline.instance != null;
	}

	public static boolean isInDeferredPass() {
		return EaglerDeferredPipeline.instance != null && GlStateManager.isExtensionPipeline();
	}

	public static boolean isInForwardPass() {
		return enableForwardRender && !enableShadowRender;
	}

	public static boolean isInParaboloidPass() {
		return enableParaboloidRender;
	}

	public static boolean isRenderingRealisticWater() {
		return EaglerDeferredPipeline.instance != null && EaglerDeferredPipeline.instance.config.is_rendering_realisticWater;
	}

	public static boolean isRenderingGlassHighlights() {
		return EaglerDeferredPipeline.instance != null && EaglerDeferredPipeline.instance.config.is_rendering_useEnvMap;
	}

	public static void setDefaultMaterialConstants() {
		materialConstantsRoughness = 0.5f;
		materialConstantsMetalness = 0.02f;
		materialConstantsEmission = 0.0f;
		materialConstantsSubsurfScatting = 0.0f;
		++materialConstantsSerial;
	}

	public static void startUsingEnvMap() {
		materialConstantsUseEnvMap = true;
	}

	public static void endUsingEnvMap() {
		materialConstantsUseEnvMap = false;
	}

	public static void reportForwardRenderObjectPosition(int centerX, int centerY, int centerZ) {
		EaglerDeferredPipeline instance = EaglerDeferredPipeline.instance;
		if(instance != null && enableForwardRender) {
			EaglerDeferredConfig cfg = instance.config;
			if(!cfg.is_rendering_dynamicLights || !cfg.shaderPackInfo.DYNAMIC_LIGHTS) {
				return;
			}
			instance.bindLightSourceBucket(centerX, centerY, centerZ, 1);
		}
	}

	public static void reportForwardRenderObjectPosition2(float x, float y, float z) {
		EaglerDeferredPipeline instance = EaglerDeferredPipeline.instance;
		if(instance != null && enableForwardRender) {
			EaglerDeferredConfig cfg = instance.config;
			if(!cfg.is_rendering_dynamicLights || !cfg.shaderPackInfo.DYNAMIC_LIGHTS) {
				return;
			}
			float posX = (float)((x + TileEntityRendererDispatcher.staticPlayerX) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerX / 16.0) << 4));
			float posY = (float)((y + TileEntityRendererDispatcher.staticPlayerY) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerY / 16.0) << 4));
			float posZ = (float)((z + TileEntityRendererDispatcher.staticPlayerZ) - (MathHelper.floor_double(TileEntityRendererDispatcher.staticPlayerZ / 16.0) << 4));
			instance.bindLightSourceBucket((int)posX, (int)posY, (int)posZ, 1);
		}
	}

	public static void setHDRTranslucentPassBlendFunc() {
		GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ZERO);
	}

	public static void enableMaterialTexture() {
		enableMaterialMapTexture = true;
	}

	public static void disableMaterialTexture() {
		enableMaterialMapTexture = false;
	}

	public static void enableForwardRender() {
		enableForwardRender = true;
	}

	public static void disableForwardRender() {
		enableForwardRender = false;
	}

	public static void enableParaboloidRender() {
		enableParaboloidRender = true;
	}

	public static void disableParaboloidRender() {
		enableParaboloidRender = false;
	}

	public static void enableShadowRender() {
		enableShadowRender = true;
	}

	public static void disableShadowRender() {
		enableShadowRender = false;
	}

	public static boolean isEnableShadowRender() {
		return enableShadowRender;
	}

	public static void enableClipPlane() {
		enableClipPlane = true;
	}

	public static void disableClipPlane() {
		enableClipPlane = false;
	}

	public static void setClipPlaneY(float yValue) {
		clipPlaneY = yValue;
	}

	public static void enableDrawWavingBlocks() {
		enableDrawWavingBlocks = true;
	}

	public static void disableDrawWavingBlocks() {
		enableDrawWavingBlocks = false;
	}

	public static boolean isEnableDrawWavingBlocks() {
		return enableDrawWavingBlocks;
	}

	public static void enableDrawRealisticWaterMask() {
		enableDrawRealisticWaterMask = true;
	}

	public static void disableDrawRealisticWaterMask() {
		enableDrawRealisticWaterMask = false;
	}

	public static boolean isDrawRealisticWaterMask() {
		return enableDrawRealisticWaterMask;
	}

	public static void enableDrawRealisticWaterRender() {
		enableDrawRealisticWaterRender = true;
	}

	public static void disableDrawRealisticWaterRender() {
		enableDrawRealisticWaterRender = false;
	}

	public static boolean isDrawRealisticWaterRender() {
		return enableDrawRealisticWaterRender;
	}

	public static void enableDrawGlassHighlightsRender() {
		enableDrawGlassHighlightsRender = true;
	}

	public static void disableDrawGlassHighlightsRender() {
		enableDrawGlassHighlightsRender = false;
	}

	public static boolean isDrawGlassHighlightsRender() {
		return enableDrawGlassHighlightsRender;
	}

	public static void setWavingBlockOffset(float x, float y, float z) {
		wavingBlockOffsetX = x;
		wavingBlockOffsetY = y;
		wavingBlockOffsetZ = z;
		++wavingBlockOffsetSerial;
	}

	public static void setWavingBlockParams(float x, float y, float z, float w) {
		wavingBlockParamX = x;
		wavingBlockParamY = y;
		wavingBlockParamZ = z;
		wavingBlockParamW = w;
		++wavingBlockParamSerial;
	}

	public static void setRoughnessConstant(float roughness) {
		materialConstantsRoughness = roughness;
		++materialConstantsSerial;
	}

	public static void setMetalnessConstant(float metalness) {
		materialConstantsMetalness = metalness;
		++materialConstantsSerial;
	}

	public static void setEmissionConstant(float emission) {
		materialConstantsEmission = emission;
		++materialConstantsSerial;
	}

	public static void setSubsurfScatteringConstant(float sss) {
		materialConstantsSubsurfScatting = sss;
		++materialConstantsSerial;
	}

	public static void setBlockConstant(int blockId) {
		constantBlock = blockId;
	}

	public static AxisAlignedBB getShadowMapBounds() {
		return shadowMapBounds;
	}

	public static void setShadowMapBounds(AxisAlignedBB newShadowMapBounds) {
		shadowMapBounds = newShadowMapBounds;
	}

	public static void loadGBufferViewMatrix() {
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

	public static void loadPassViewMatrix() {
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

	public static void loadShadowPassViewMatrix() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		passViewMatrix.load(matrixCopyBuffer);
		Matrix4f.invert(passViewMatrix, passInverseViewMatrix);
		passProjMatrix.setIdentity();
		++passViewMatrixSerial;
		isShadowPassMatrixLoaded = true;
	}

	public static void setPassMatrixToGBuffer() {
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

	public static void loadSunShadowMatrixLOD0() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix0.load(matrixCopyBuffer);
	}

	public static void loadSunShadowMatrixLOD1() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix1.load(matrixCopyBuffer);
	}

	public static void loadSunShadowMatrixLOD2() {
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, matrixCopyBuffer);
		sunShadowMatrix2.load(matrixCopyBuffer);
	}

	public static Matrix4f getSunShadowMatrixLOD0() {
		return sunShadowMatrix0;
	}

	public static Matrix4f getSunShadowMatrixLOD1() {
		return sunShadowMatrix1;
	}

	public static Matrix4f getSunShadowMatrixLOD2() {
		return sunShadowMatrix2;
	}

	public static void setGBufferNearFarPlanes(float zNear, float zFar) {
		gbufferNearPlane = zNear;
		gbufferFarPlane = zFar;
	}

	public static void setWaterWindOffset(float sx, float sy, float fx, float fy) {
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

	public static void enableFogLinear(float near, float far, boolean atmosphere, float colorLightR,
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

	public static void enableFogExp(float density, boolean atmosphere, float colorLightR, float colorLightG,
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

	public static void disableFog() {
		fogLinearExp = 0;
	}

	public static void disableAll() {
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