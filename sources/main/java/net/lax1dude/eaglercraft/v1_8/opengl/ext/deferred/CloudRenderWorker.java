package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.TextureCopyUtil;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderCloudsNoise3D;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderCloudsSample;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderCloudsShapes;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderCloudsSunOcclusion;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;

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
public class CloudRenderWorker {

	static PipelineShaderCloudsNoise3D shader_clouds_noise3d = null;
	static PipelineShaderCloudsShapes shader_clouds_shapes = null;
	static PipelineShaderCloudsSample shader_clouds_sample = null;
	static PipelineShaderCloudsSunOcclusion shader_clouds_sun_occlusion = null;

	static int cloudNoiseTexture = -1;

	static int cloud3DSamplesTextureSizeX = 256;
	static int cloud3DSamplesTextureSizeY = 256;
	static int cloud3DSamplesTextureSizeZ = 64;

	static int cloudParaboloidTextureSize = 512;

	static int cloud3DSamplesTexture = -1;
	static IFramebufferGL[] cloud3DSamplesSlices = null;

	static IFramebufferGL[] cloudNoiseSampleParaboloidFramebuffer = new IFramebufferGL[4];
	static int[] cloudNoiseSampleParaboloidTexture = new int[] { -1, -1, -1, -1 };

	static IFramebufferGL cloudOcclusionFramebuffer = null;
	static int cloudOcclusionTexture = -1;

	static int cloudSpecialShapeTexture = -1;

	static float renderViewX = 0.0f;
	static float renderViewY = 0.0f;
	static float renderViewZ = 0.0f;

	private static final Matrix4f tmpMatrix1 = new Matrix4f();
	private static final Matrix3f tmpMatrix2 = new Matrix3f();
	private static final Matrix3f tmpMatrix3 = new Matrix3f();
	private static final Vector3f tmpVector1 = new Vector3f();
	private static final Vector3f tmpVector2 = new Vector3f();
	private static final Vector3f tmpVector3 = new Vector3f();

	private static long cloudStartTimer = 0l;
	static int cloudRenderProgress = 0;
	static int cloudRenderPeriod = 500;
	static int cloudRenderPhase = 0;

	static float cloudColorR = 0.0f;
	static float cloudColorG = 0.0f;
	static float cloudColorB = 0.0f;

	static boolean isDrawingCloudShapes = false;

	static int shapePosX = 100;
	static int shapeSizeX = 32;
	static int shapePosY = 80;
	static int shapeSizeY = 16;
	static int shapePosZ = 20;
	static int shapeSizeZ = 24;
	static float shapeRotate = 45.0f;

	static long shapeUpdateTimer = 0l;
	static long nextShapeAppearance = 0l;

	static EaglercraftRandom rand = new EaglercraftRandom();

	static void initialize() {
		destroy();

		cloudStartTimer = System.currentTimeMillis();
		cloudRenderProgress = 0;
		cloudRenderPeriod = 500;
		cloudRenderPhase = 0;
		cloudColorR = 0.0f;
		cloudColorG = 0.0f;
		cloudColorB = 0.0f;

		shapeUpdateTimer = cloudStartTimer;
		nextShapeAppearance = cloudStartTimer + rand.nextInt(1800000);

		cloudNoiseTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(cloudNoiseTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		int cloudNoiseW = 64, cloudNoiseH = 64, cloudNoiseLen = 4096;
		byte[] cloudNoiseDat = new byte[cloudNoiseLen];
		(new EaglercraftRandom(696969l)).nextBytes(cloudNoiseDat);
		ByteBuffer cloudNoiseDatBuffer = EagRuntime.allocateByteBuffer(cloudNoiseDat.length);
		cloudNoiseDatBuffer.put(cloudNoiseDat);
		cloudNoiseDatBuffer.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, cloudNoiseW, cloudNoiseH, 0, GL_RED, GL_UNSIGNED_BYTE, cloudNoiseDatBuffer);
		EagRuntime.freeByteBuffer(cloudNoiseDatBuffer);

		cloud3DSamplesTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture3D(cloud3DSamplesTexture);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		_wglTexImage3D(GL_TEXTURE_3D, 0, _GL_R8, cloud3DSamplesTextureSizeX, cloud3DSamplesTextureSizeY,
				cloud3DSamplesTextureSizeZ, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer) null);

		cloud3DSamplesSlices = new IFramebufferGL[cloud3DSamplesTextureSizeZ];
		for(int i = 0; i < cloud3DSamplesTextureSizeZ; ++i) {
			cloud3DSamplesSlices[i] = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, cloud3DSamplesSlices[i]);
			_wglFramebufferTextureLayer(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, EaglercraftGPU.getNativeTexture(cloud3DSamplesTexture), 0, i);
		}

		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		for(int i = 0; i < 4; ++i) {
			cloudNoiseSampleParaboloidFramebuffer[i] = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, cloudNoiseSampleParaboloidFramebuffer[i]);
			cloudNoiseSampleParaboloidTexture[i] = GlStateManager.generateTexture();
			GlStateManager.bindTexture(cloudNoiseSampleParaboloidTexture[i]);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, i == 3 ? GL_LINEAR : GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, i == 3 ? GL_LINEAR : GL_NEAREST);
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, cloudParaboloidTextureSize, cloudParaboloidTextureSize, GL_RGBA, true);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(cloudNoiseSampleParaboloidTexture[i]), 0);
			GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		}

		cloudSpecialShapeTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture3D(cloudSpecialShapeTexture);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		_wglTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		byte[] cloudShapeTexture = EagRuntime.getResourceBytes("/assets/eagler/glsl/deferred/clouds_shapes.bmp");
		cloudNoiseDatBuffer = EagRuntime.allocateByteBuffer(cloudShapeTexture.length);
		cloudNoiseDatBuffer.put(cloudShapeTexture);
		cloudNoiseDatBuffer.flip();
		_wglTexImage3D(GL_TEXTURE_3D, 0, _GL_R8, 32, 16, 24, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer) cloudNoiseDatBuffer);
		EagRuntime.freeByteBuffer(cloudNoiseDatBuffer);

		shader_clouds_noise3d = PipelineShaderCloudsNoise3D.compile();
		shader_clouds_noise3d.loadUniforms();
		shader_clouds_shapes = PipelineShaderCloudsShapes.compile();
		shader_clouds_shapes.loadUniforms();
		shader_clouds_sample = PipelineShaderCloudsSample.compile();
		shader_clouds_sample.loadUniforms();
		shader_clouds_sun_occlusion = PipelineShaderCloudsSunOcclusion.compile();
		shader_clouds_sun_occlusion.loadUniforms();

		cloudOcclusionFramebuffer = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, cloudOcclusionFramebuffer);
		cloudOcclusionTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(cloudOcclusionTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_R8, 1, 1, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(cloudOcclusionTexture), 0);
	}

	static void setPosition(float x, float y, float z) {
		renderViewX = x;
		renderViewY = y;
		renderViewZ = z;
	}

	static void bindParaboloid() {
		GlStateManager.bindTexture(cloudNoiseSampleParaboloidTexture[3]);
	}

	static void update() {
		long millis = System.currentTimeMillis();
		int cloudProgress = (int)(millis - cloudStartTimer);
		int totalCloudSteps = 32 + 32 - 1;
		int currentCloudStep = cloudProgress * totalCloudSteps / cloudRenderPeriod;
		boolean b = false;
		if(currentCloudStep > totalCloudSteps) {
			currentCloudStep = totalCloudSteps;
			b = true;
		}

		float playerCoordsNoiseMapScale = 0.02f;
		FloatBuffer matrixCopyBuffer = EaglerDeferredPipeline.matrixCopyBuffer;

		WorldClient wc = Minecraft.getMinecraft().theWorld;
		float rain = wc.getRainStrength(0.0f);
		if(cloudRenderProgress == 0) {
			shader_clouds_noise3d.useProgram();
			_wglUniform2f(shader_clouds_noise3d.uniforms.u_textureSize2f, 1.0f / cloud3DSamplesTextureSizeX, 1.0f / cloud3DSamplesTextureSizeY);
			float m = (float)((millis % 1200000l) * 0.00002);
			_wglUniform3f(shader_clouds_noise3d.uniforms.u_cloudMovement3f, m, 0.0f, m);//2.213f, 0.0f, 2.213f);
			
			tmpMatrix1.setIdentity();
			tmpVector1.set(renderViewX * playerCoordsNoiseMapScale, 0.0f, renderViewZ * playerCoordsNoiseMapScale);
			Matrix4f.translate(tmpVector1, tmpMatrix1, tmpMatrix1);
			float s = 1500.0f;
			tmpVector1.set(s, s * 0.0015f, s);
			Matrix4f.scale(tmpVector1, tmpMatrix1, tmpMatrix1);
			matrixCopyBuffer.clear();
			matrixCopyBuffer.put(tmpMatrix1.m00);
			matrixCopyBuffer.put(tmpMatrix1.m01);
			matrixCopyBuffer.put(tmpMatrix1.m02);
			matrixCopyBuffer.put(tmpMatrix1.m10);
			matrixCopyBuffer.put(tmpMatrix1.m11);
			matrixCopyBuffer.put(tmpMatrix1.m12);
			matrixCopyBuffer.put(tmpMatrix1.m20);
			matrixCopyBuffer.put(tmpMatrix1.m21);
			matrixCopyBuffer.put(tmpMatrix1.m22);
			matrixCopyBuffer.put(tmpMatrix1.m30);
			matrixCopyBuffer.put(tmpMatrix1.m31);
			matrixCopyBuffer.put(tmpMatrix1.m32);
			matrixCopyBuffer.flip();
			_wglUniformMatrix4x3fv(shader_clouds_noise3d.uniforms.u_sampleOffsetMatrix4f, false, matrixCopyBuffer);
			
			shader_clouds_sample.useProgram();
			_wglUniform1f(shader_clouds_sample.uniforms.u_rainStrength1f, 0.0f);
			_wglUniform1f(shader_clouds_sample.uniforms.u_cloudTimer1f, 0.0f);
			_wglUniform3f(shader_clouds_sample.uniforms.u_cloudOffset3f, renderViewX, renderViewY, renderViewZ);
			Vector3f currentSunAngle = DeferredStateManager.currentSunLightAngle;
			_wglUniform3f(shader_clouds_sample.uniforms.u_sunDirection3f, -currentSunAngle.x, -currentSunAngle.y, -currentSunAngle.z);
			currentSunAngle = tmpVector1;
			currentSunAngle.set(DeferredStateManager.currentSunLightColor);
			float luma = currentSunAngle.x * 0.299f + currentSunAngle.y * 0.587f + currentSunAngle.z * 0.114f;
			float sat = 0.65f; // desaturate sun a bit
			currentSunAngle.x = (currentSunAngle.x - luma) * sat + luma;
			currentSunAngle.y = (currentSunAngle.y - luma) * sat + luma;
			currentSunAngle.z = (currentSunAngle.z - luma) * sat + luma;
			cloudColorR += (currentSunAngle.x - cloudColorR) * 0.1f;
			cloudColorG += (currentSunAngle.y - cloudColorG) * 0.1f;
			cloudColorB += (currentSunAngle.z - cloudColorB) * 0.1f;
			_wglUniform3f(shader_clouds_sample.uniforms.u_sunColor3f, cloudColorR, cloudColorG, cloudColorB);
			
			float cloudDensityTimer = (float)((System.currentTimeMillis() % 10000000l) * 0.001);
			cloudDensityTimer += MathHelper.sin(cloudDensityTimer * 1.5f) * 1.5f;
			float x = cloudDensityTimer * 0.004f;
			float f1 = MathHelper.sin(x + 0.322f) * 0.544f + MathHelper.sin(x * 4.5f + 1.843f) * 0.69f + MathHelper.sin(x * 3.4f + 0.8f) * 0.6f + MathHelper.sin(x * 6.1f + 1.72f) * 0.7f;
			x = cloudDensityTimer * 0.002f;
			float f2 = MathHelper.cos(x + 2.7f) + MathHelper.cos(x * 1.28f + 1.3f) * 0.4f + MathHelper.cos(x * 4.0f + 2.5f) * 0.3f + MathHelper.cos(x * 2.3f + 1.07f);
			float rain2 = rain + wc.getThunderStrength(0.0f);
			_wglUniform4f(shader_clouds_sample.uniforms.u_densityModifier4f, 0.015f + f1 * 0.0021f * (1.0f - rain2 * 0.35f) + rain2 * 0.00023f, 0.0325f, -0.0172f + f2 * 0.00168f * (1.0f - rain2 * 0.35f) + rain * 0.0015f, 0.0f);
		}

		if(cloudRenderProgress < 32 && currentCloudStep > cloudRenderProgress) {
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture(cloudNoiseTexture);
			
			GlStateManager.viewport(0, 0, cloud3DSamplesTextureSizeX, cloud3DSamplesTextureSizeY);

			updateShape();
			boolean shapeAllow = isDrawingCloudShapes;
			boolean shapeInit = false;
			
			for(int i = cloudRenderProgress, j = currentCloudStep < 32 ? currentCloudStep : 32; i < j; ++i) {
				int ccl = i * 2;

				boolean drawShape = false;
				if(isDrawingCloudShapes && shapeAllow) {
					if(ccl >= shapePosZ && ccl < shapePosZ + shapeSizeZ) {
						drawShape = true;
						if(!shapeInit) {
							shapeInit = true;
							Matrix3f mat = tmpMatrix2;
							mat.setIdentity();
							mat.m00 = MathHelper.cos(shapeRotate * 0.0174532f);
							mat.m01 = MathHelper.sin(shapeRotate * 0.0174532f);
							mat.m10 = -mat.m01;
							mat.m11 = mat.m00;
							mat = tmpMatrix3;
							mat.setIdentity();
							mat.m00 = (float)shapeSizeX * 0.5f;
							mat.m11 = (float)shapeSizeY * 0.5f;
							Matrix3f.mul(tmpMatrix2, mat, tmpMatrix2);
							tmpMatrix2.m20 = shapePosX - renderViewX * playerCoordsNoiseMapScale * 128.0f;
							tmpMatrix2.m21 = shapePosY - renderViewZ * playerCoordsNoiseMapScale * 128.0f;
							mat.setIdentity();
							mat.m00 = 2.0f / cloud3DSamplesTextureSizeX;
							mat.m11 = 2.0f / cloud3DSamplesTextureSizeY;
							Matrix3f.mul(mat, tmpMatrix2, tmpMatrix2);
							mat = tmpMatrix2;
							mat.m20 -= 1.0f;
							mat.m21 -= 1.0f;
							if(!checkFrustum(mat)) {
								drawShape = false;
								shapeAllow = false;
							}else {
								matrixCopyBuffer.clear();
								matrixCopyBuffer.put(mat.m00);
								matrixCopyBuffer.put(mat.m01);
								matrixCopyBuffer.put(mat.m10);
								matrixCopyBuffer.put(mat.m11);
								matrixCopyBuffer.put(mat.m20);
								matrixCopyBuffer.put(mat.m21);
								matrixCopyBuffer.flip();
								shader_clouds_shapes.useProgram();
								_wglUniformMatrix3x2fv(shader_clouds_shapes.uniforms.u_transformMatrix3x2f, false, matrixCopyBuffer);
								_wglUniform1f(shader_clouds_shapes.uniforms.u_textureLod1f, 0.0f);
								_wglUniform2f(shader_clouds_shapes.uniforms.u_sampleWeights2f, 0.35f, 0.55f);
							}
						}
					}
				}
				
				shader_clouds_noise3d.useProgram();
				
				_wglBindFramebuffer(_GL_FRAMEBUFFER, cloud3DSamplesSlices[ccl]);
				_wglUniform1f(shader_clouds_noise3d.uniforms.u_textureSlice1f, (float)(ccl / (float)cloud3DSamplesTextureSizeZ));
				
				DrawUtils.drawStandardQuad2D();
				
				if(drawShape) {
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL_ONE, GL_SRC_ALPHA);
					shader_clouds_shapes.useProgram();
					_wglUniform1f(shader_clouds_shapes.uniforms.u_textureLevel1f, (float)(ccl - shapePosZ + 0.5f) / (float)shapeSizeZ);
					GlStateManager.bindTexture3D(cloudSpecialShapeTexture);
					DrawUtils.drawStandardQuad2D();
					GlStateManager.disableBlend();
					shader_clouds_noise3d.useProgram();
					GlStateManager.bindTexture(cloudNoiseTexture);
				}
				
				_wglBindFramebuffer(_GL_FRAMEBUFFER, cloud3DSamplesSlices[ccl + 1]);
				_wglUniform1f(shader_clouds_noise3d.uniforms.u_textureSlice1f, (float)((ccl + 1) / (float)cloud3DSamplesTextureSizeZ));
				
				DrawUtils.drawStandardQuad2D();
				
				if(drawShape && ccl + 1 < shapePosZ + shapeSizeZ) {
					GlStateManager.enableBlend();
					shader_clouds_shapes.useProgram();
					_wglUniform1f(shader_clouds_shapes.uniforms.u_textureLevel1f, (float)((ccl + 1) - shapePosZ + 0.5f) / (float)shapeSizeZ);
					GlStateManager.bindTexture3D(cloudSpecialShapeTexture);
					DrawUtils.drawStandardQuad2D();
					GlStateManager.disableBlend();
				}
			}
		}
		
		if(currentCloudStep >= 32 && currentCloudStep > cloudRenderProgress) {
			_wglBindFramebuffer(_GL_FRAMEBUFFER, cloudNoiseSampleParaboloidFramebuffer[cloudRenderPhase]);
			GlStateManager.viewport(0, 0, cloudParaboloidTextureSize, cloudParaboloidTextureSize);

			GlStateManager.setActiveTexture(GL_TEXTURE1);
			GlStateManager.bindTexture(EaglerDeferredPipeline.instance.atmosphereIrradianceTexture);
			GlStateManager.setActiveTexture(GL_TEXTURE0);
			GlStateManager.bindTexture3D(cloud3DSamplesTexture);
			shader_clouds_sample.useProgram();
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_DST_ALPHA, GL_ONE, GL_DST_ALPHA, GL_ZERO);
			
			for(int i = cloudRenderProgress > 32 ? cloudRenderProgress - 32 : 0, j = currentCloudStep - 31; i < j; ++i) {
				if(i == 0) {
					GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
					GlStateManager.clear(GL_COLOR_BUFFER_BIT);
				}
				
				_wglUniform1f(shader_clouds_sample.uniforms.u_sampleStep1f, i * 2);
				DrawUtils.drawStandardQuad2D();
				
				_wglUniform1f(shader_clouds_sample.uniforms.u_sampleStep1f, i * 2 + 1);
				DrawUtils.drawStandardQuad2D();
			}

			GlStateManager.disableBlend();
		}
		
		if(b) {
			cloudRenderProgress = 0;
			cloudStartTimer = System.currentTimeMillis();
			cloudProgress = 0;
			cloudRenderPhase = (cloudRenderPhase + 1) % 3;
		}else {
			cloudRenderProgress = currentCloudStep;
		}
		
		_wglBindFramebuffer(_GL_FRAMEBUFFER, cloudNoiseSampleParaboloidFramebuffer[3]);
		GlStateManager.viewport(0, 0, cloudParaboloidTextureSize, cloudParaboloidTextureSize);
		
		float fadeFactor = cloudProgress / (float)cloudRenderPeriod;
		if(fadeFactor > 1.0f) fadeFactor = 1.0f;
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(cloudNoiseSampleParaboloidTexture[(cloudRenderPhase + 1) % 3]);
		TextureCopyUtil.blitTexture();
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_CONSTANT_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);
		GlStateManager.setBlendConstants(0.0f, 0.0f, 0.0f, fadeFactor);
		GlStateManager.bindTexture(cloudNoiseSampleParaboloidTexture[(cloudRenderPhase + 2) % 3]);
		TextureCopyUtil.blitTexture();
		GlStateManager.disableBlend();
		
		_wglBindFramebuffer(_GL_FRAMEBUFFER, cloudOcclusionFramebuffer);
		GlStateManager.viewport(0, 0, 1, 1);
		if(rain >= 1.0f) {
			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		}else if(DeferredStateManager.currentSunLightAngle.y < 0.0f) {
			shader_clouds_sun_occlusion.useProgram();
			GlStateManager.bindTexture(cloudNoiseSampleParaboloidTexture[3]);
			matrixCopyBuffer.clear();
			
			tmpVector1.set(0.0f, 1.0f, 0.0f);
			Vector3f vec33 = tmpVector3;
			vec33.set(DeferredStateManager.currentSunLightAngle);
			vec33.x = -vec33.x;
			vec33.y = -vec33.y;
			vec33.z = -vec33.z;
			Vector3f.cross(tmpVector1, vec33, tmpVector1);
			Vector3f.cross(vec33, tmpVector1, tmpVector2);
			
			float rad = 0.1f;
			
			matrixCopyBuffer.put(tmpVector1.x * rad);
			matrixCopyBuffer.put(tmpVector2.x * rad);
			matrixCopyBuffer.put(vec33.x * rad);
			
			matrixCopyBuffer.put(tmpVector1.y * rad);
			matrixCopyBuffer.put(tmpVector2.y * rad);
			matrixCopyBuffer.put(vec33.y * rad);
			
			matrixCopyBuffer.put(tmpVector1.z * rad);
			matrixCopyBuffer.put(tmpVector2.z * rad);
			matrixCopyBuffer.put(vec33.z * rad);
			
			rad = 1.0f - rad;
			matrixCopyBuffer.put(vec33.x * rad);
			matrixCopyBuffer.put(vec33.y * rad);
			matrixCopyBuffer.put(vec33.z * rad);
			
			matrixCopyBuffer.flip();
			_wglUniformMatrix4x3fv(shader_clouds_sun_occlusion.uniforms.u_sampleMatrix4x3f, false, matrixCopyBuffer);
			
			if(rain > 0.0f) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_CONSTANT_ALPHA, GL_ZERO);
				GlStateManager.setBlendConstants(0.0f, 0.0f, 0.0f, 1.0f - rain);
				DrawUtils.drawStandardQuad2D();
				GlStateManager.disableBlend();
			}else {
				DrawUtils.drawStandardQuad2D();
			}
		}else {
			GlStateManager.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		}
	}

	static void destroy() {
		if(cloudNoiseTexture != -1) {
			GlStateManager.deleteTexture(cloudNoiseTexture);
			cloudNoiseTexture = -1;
		}
		for(int i = 0; i < 4; ++i) {
			if(cloudNoiseSampleParaboloidFramebuffer[i] != null) {
				_wglDeleteFramebuffer(cloudNoiseSampleParaboloidFramebuffer[i]);
				cloudNoiseSampleParaboloidFramebuffer[i] = null;
			}
			if(cloudNoiseSampleParaboloidTexture[i] != -1) {
				GlStateManager.deleteTexture(cloudNoiseSampleParaboloidTexture[i]);
				cloudNoiseSampleParaboloidTexture[i] = -1;
			}
		}
		if(cloud3DSamplesTexture != -1) {
			GlStateManager.deleteTexture(cloud3DSamplesTexture);
			cloud3DSamplesTexture = -1;
		}
		if(cloud3DSamplesSlices != null) {
			for(int i = 0; i < cloud3DSamplesSlices.length; ++i) {
				_wglDeleteFramebuffer(cloud3DSamplesSlices[i]);
			}
			cloud3DSamplesSlices = null;
		}
		if(cloudSpecialShapeTexture != -1) {
			GlStateManager.deleteTexture(cloudSpecialShapeTexture);
			cloudSpecialShapeTexture = -1;
		}
		if(cloudOcclusionFramebuffer != null) {
			_wglDeleteFramebuffer(cloudOcclusionFramebuffer);
			cloudOcclusionFramebuffer = null;
		}
		if(cloudOcclusionTexture != -1) {
			GlStateManager.deleteTexture(cloudOcclusionTexture);
			cloudOcclusionTexture = -1;
		}
		if(shader_clouds_noise3d != null) {
			shader_clouds_noise3d.destroy();
			shader_clouds_noise3d = null;
		}
		if(shader_clouds_shapes != null) {
			shader_clouds_shapes.destroy();
			shader_clouds_shapes = null;
		}
		if(shader_clouds_sample != null) {
			shader_clouds_sample.destroy();
			shader_clouds_sample = null;
		}
		if(shader_clouds_sun_occlusion != null) {
			shader_clouds_sun_occlusion.destroy();
			shader_clouds_sun_occlusion = null;
		}
	}

	private static void updateShape() {
		long millis = System.currentTimeMillis();
		float dt = (float)((millis - shapeUpdateTimer) * 0.001);
		shapeUpdateTimer = millis;
		if(millis > nextShapeAppearance) {
			float playerCoordsNoiseMapScale = 0.02f * 128.0f;
			if(!isDrawingCloudShapes) {
				float shapeScaleBase = rand.nextFloat() * 3.0f + 2.0f;
				shapeSizeX = (int)(32 * shapeScaleBase * (0.9f + rand.nextFloat() * 0.2f));
				shapeSizeY = (int)(16 * shapeScaleBase * (0.95f + rand.nextFloat() * 0.1f));
				shapeSizeZ = (int)(24 * shapeScaleBase * (0.48f + rand.nextFloat() * 0.04f));
				do {
					shapePosX = (int)(cloud3DSamplesTextureSizeX * (rand.nextFloat() * 1.5f - 0.75f));
					shapePosY = (int)(cloud3DSamplesTextureSizeY * (rand.nextFloat() * 1.5f - 0.75f));
				}while(shapePosX > -192 && shapePosY > -192 && shapePosX < 192 && shapePosY < 192);
				float l = -MathHelper.sqrt_float(shapePosX * shapePosX + shapePosY * shapePosY);
				shapeRotate = (float)Math.atan2(shapePosY / l, shapePosX / l) / 0.0174532f;
				shapeRotate += (rand.nextFloat() - 0.5f) * 90.0f;
				shapePosX += renderViewX * playerCoordsNoiseMapScale + cloud3DSamplesTextureSizeX * 0.5f;
				shapePosY += renderViewZ * playerCoordsNoiseMapScale + cloud3DSamplesTextureSizeY * 0.5f;
				shapePosZ = (int)((cloud3DSamplesTextureSizeZ - shapeSizeZ) * (rand.nextFloat() * 0.5f + 0.25f));
				isDrawingCloudShapes = true;
			}else {
				float dx = MathHelper.cos(-shapeRotate * 0.0174532f);
				float dy = MathHelper.sin(-shapeRotate * 0.0174532f);
				shapePosX += (int)(dx * 10.0f * dt);
				shapePosY -= (int)(dy * 10.0f * dt);
				if(MathHelper.abs(shapePosX - renderViewX * playerCoordsNoiseMapScale - cloud3DSamplesTextureSizeX * 0.5f) > 300.0f ||
						MathHelper.abs(shapePosY - renderViewZ * playerCoordsNoiseMapScale - cloud3DSamplesTextureSizeY * 0.5f) > 300.0f) {
					nextShapeAppearance = millis + 300000l + rand.nextInt(1500000);
					isDrawingCloudShapes = false;
				}
			}
		}else {
			isDrawingCloudShapes = false;
		}
	}

	static boolean checkFrustum(Matrix3f mat) {
		Vector3f tmp = tmpVector1;
		tmp.x = -1.0f;
		tmp.y = -1.0f;
		tmp.z = 1.0f;
		Matrix3f.transform(mat, tmp, tmp);
		if(tmp.x >= -1.0f && tmp.x <= 1.0f && tmp.y >= -1.0f && tmp.y <= 1.0f) {
			return true;
		}
		tmp.x = 1.0f;
		tmp.y = -1.0f;
		Matrix3f.transform(mat, tmp, tmp);
		if(tmp.x >= -1.0f && tmp.x <= 1.0f && tmp.y >= -1.0f && tmp.y <= 1.0f) {
			return true;
		}
		tmp.x = 1.0f;
		tmp.y = 1.0f;
		Matrix3f.transform(mat, tmp, tmp);
		if(tmp.x >= -1.0f && tmp.x <= 1.0f && tmp.y >= -1.0f && tmp.y <= 1.0f) {
			return true;
		}
		tmp.x = -1.0f;
		tmp.y = 1.0f;
		Matrix3f.transform(mat, tmp, tmp);
		if(tmp.x >= -1.0f && tmp.x <= 1.0f && tmp.y >= -1.0f && tmp.y <= 1.0f) {
			return true;
		}
		return false;
	}

}
