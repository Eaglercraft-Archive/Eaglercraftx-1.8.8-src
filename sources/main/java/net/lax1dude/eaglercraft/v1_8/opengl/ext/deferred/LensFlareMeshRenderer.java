package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderLensFlares;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.minecraft.client.Minecraft;
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
public class LensFlareMeshRenderer {

	public static final String streaksTextureLocation ="assets/eagler/glsl/deferred/lens_streaks.bmp";
	public static final String ghostsTextureLocation = "assets/eagler/glsl/deferred/lens_ghosts.bmp";
	public static final int ghostsSpriteCount = 4;

	static IBufferArrayGL streaksVertexArray = null;
	static IBufferGL streaksVertexBuffer = null;

	static IBufferArrayGL ghostsVertexArray = null;
	static IBufferGL ghostsVertexBuffer = null;

	static PipelineShaderLensFlares streaksProgram = null;
	static PipelineShaderLensFlares ghostsProgram = null;

	static int streaksTexture = -1;
	static int ghostsTexture = -1;

	static int streaksVertexCount = 0;
	static int ghostsInstanceCount = 0;

	static final Matrix3f tmpMat = new Matrix3f();
	static final Matrix3f tmpMat2 = new Matrix3f();
	static final Vector3f tmpVec = new Vector3f();

	static void initialize() {
		destroy();
		
		streaksProgram = PipelineShaderLensFlares.compileStreaks();
		streaksProgram.loadUniforms();
		
		ghostsProgram = PipelineShaderLensFlares.compileGhosts();
		ghostsProgram.loadUniforms();
		
		ByteBuffer copyBuffer = EagRuntime.allocateByteBuffer(16384);

		for(int i = 0; i < 4; ++i) {
			pushStreakQuad(copyBuffer, 0.0f, 0.0f, 1.0f, 10.0f, 0.0f, 0.0f, 1.0f, 1.0f, (i * 3.14159f / 4.0f));
			pushStreakQuad(copyBuffer, 0.0f, 0.0f, 1.5f, 5.0f, 0.0f, 0.0f, 1.0f, 1.0f, ((i + 0.25f) * 3.14159f / 4.0f));
			pushStreakQuad(copyBuffer, 0.0f, 0.0f, 0.5f, 7.0f, 0.0f, 0.0f, 1.0f, 1.0f, ((i + 0.5f) * 3.14159f / 4.0f));
			pushStreakQuad(copyBuffer, 0.0f, 0.0f, 1.5f, 5.0f, 0.0f, 0.0f, 1.0f, 1.0f, ((i + 0.75f) * 3.14159f / 4.0f));
		}

		copyBuffer.flip();
		streaksVertexCount = 64;

		streaksVertexBuffer = _wglGenBuffers();
		EaglercraftGPU.bindGLArrayBuffer(streaksVertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, copyBuffer, GL_STATIC_DRAW);

		streaksVertexArray = _wglGenVertexArrays();
		EaglercraftGPU.bindGLBufferArray(streaksVertexArray);
		EaglercraftGPU.attachQuad16EmulationBuffer(16, true);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);

		_wglEnableVertexAttribArray(1);
		_wglVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);

		copyBuffer.clear();

		ghostsInstanceCount = 0;

		float streakIntensity2 = 10.0f;
		float scale = 5.0f;

		pushGhostQuadAbberated(copyBuffer, 0.4f, 0.15f * scale, 2, 0.5f, 0.9f, 0.2f, 0.04f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 0.45f, 0.15f * scale, 2, 0.5f, 0.9f, 0.2f, 0.04f * streakIntensity2);

		pushGhostQuadAbberated(copyBuffer, 0.6f, 0.1f * scale, 0, 0.5f, 0.9f, 0.2f, 0.045f * streakIntensity2);

		pushGhostQuadAbberated(copyBuffer, 0.67f, 0.1f * scale, 0, 0.5f, 0.9f, 0.2f, 0.2f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 0.78f, 0.15f * scale, 1, 0.5f, 0.9f, 0.7f, 0.2f * streakIntensity2);

		pushGhostQuadAbberated(copyBuffer, 1.0f, 0.15f * scale, 1, 0.5f, 0.9f, 0.7f, 0.1f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.04f, 0.15f * scale, 3, 0.5f, 0.5f, 0.7f, 0.1f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.07f, 0.1f * scale, 1, 0.7f, 0.7f, 0.7f, 0.2f * streakIntensity2);
		pushGhostQuad(copyBuffer, 1.11f, 0.1f * scale, 2, 0.2f, 0.2f, 0.7f, 0.05f * streakIntensity2);
		pushGhostQuad(copyBuffer, 1.11f, 0.3f * scale, 2, 0.2f, 0.7f, 0.2f, 0.05f * streakIntensity2);

		pushGhostQuadAbberated(copyBuffer, 1.25f, 0.2f * scale, 0, 0.4f, 0.7f, 0.2f, 0.02f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.22f, 0.1f * scale, 2, 0.3f, 0.7f, 0.7f, 0.05f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.27f, 0.1f * scale, 0, 0.5f, 0.7f, 0.5f, 0.15f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.30f, 0.08f * scale, 0, 0.7f, 0.7f, 0.7f, 0.15f * streakIntensity2);

		pushGhostQuadAbberated(copyBuffer, 1.45f, 0.3f * scale, 2, 0.3f, 0.7f, 0.2f, 0.02f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.55f, 0.1f * scale, 2, 0.3f, 0.7f, 0.7f, 0.05f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.59f, 0.1f * scale, 0, 0.5f, 0.7f, 0.5f, 0.15f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 2.0f, 0.3f * scale, 3, 0.3f, 0.7f, 0.2f, 0.03f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 1.98f, 0.2f * scale, 1, 0.3f, 0.7f, 0.2f, 0.04f * streakIntensity2);
		pushGhostQuadAbberated(copyBuffer, 2.02f, 0.2f * scale, 1, 0.3f, 0.7f, 0.2f, 0.04f * streakIntensity2);

		copyBuffer.flip();

		ghostsVertexArray = _wglGenVertexArrays();
		EaglercraftGPU.bindGLBufferArray(ghostsVertexArray);
		EaglercraftGPU.bindGLArrayBuffer(DrawUtils.standardQuadVBO);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 12, 0);
		_wglVertexAttribDivisor(0, 0);

		ghostsVertexBuffer = _wglGenBuffers();
		EaglercraftGPU.bindGLArrayBuffer(ghostsVertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, copyBuffer, GL_STATIC_DRAW);

		_wglEnableVertexAttribArray(1);
		_wglVertexAttribPointer(1, 2, GL_FLOAT, false, 36, 0);
		_wglVertexAttribDivisor(1, 1);

		_wglEnableVertexAttribArray(2);
		_wglVertexAttribPointer(2, 4, GL_FLOAT, false, 36, 8);
		_wglVertexAttribDivisor(2, 1);

		_wglEnableVertexAttribArray(3);
		_wglVertexAttribPointer(3, 3, GL_FLOAT, false, 36, 24);
		_wglVertexAttribDivisor(3, 1);

		streaksTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(streaksTexture);
		byte[] flareTex = EagRuntime.getResourceBytes(streaksTextureLocation);
		if(flareTex == null) {
			throw new RuntimeException("Could not locate: " + streaksTextureLocation);
		}
		try(DataInputStream dis = new DataInputStream(new ByteArrayInputStream(flareTex))) {
			loadFlareTexture(copyBuffer, dis);
		}catch(IOException ex) {
			EagRuntime.freeByteBuffer(copyBuffer);
			throw new RuntimeException("Could not load: " + streaksTextureLocation, ex);
		}

		ghostsTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(ghostsTexture);
		flareTex = EagRuntime.getResourceBytes(ghostsTextureLocation);
		if(flareTex == null) {
			throw new RuntimeException("Could not locate: " + ghostsTextureLocation);
		}
		try(DataInputStream dis = new DataInputStream(new ByteArrayInputStream(flareTex))) {
			loadFlareTexture(copyBuffer, dis);
		}catch(IOException ex) {
			EagRuntime.freeByteBuffer(copyBuffer);
			throw new RuntimeException("Could not load: " + ghostsTextureLocation, ex);
		}

		EagRuntime.freeByteBuffer(copyBuffer);
	}

	static void loadFlareTexture(ByteBuffer copyBuffer, DataInputStream dis) throws IOException {
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		_wglPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		int mip = 0;
		while(dis.read() == 'E') {
			int w = dis.readShort();
			int h = dis.readShort();
			copyBuffer.clear();
			for(int i = 0, l = w * h; i < l; ++i) {
				copyBuffer.put((byte)dis.read());
			}
			copyBuffer.flip();
			_wglTexImage2D(GL_TEXTURE_2D, mip++, _GL_R8, w, h, 0, GL_RED, GL_UNSIGNED_BYTE, copyBuffer);
		}
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, mip - 1);
		_wglPixelStorei(GL_UNPACK_ALIGNMENT, 4);
	}

	static void pushStreakQuad(ByteBuffer copyBuffer, float x, float y, float w, float h, float tx, float ty, float tw,
			float th, float rotation) {
		tmpMat.m00 = MathHelper.cos(rotation);
		tmpMat.m01 = MathHelper.sin(rotation);
		tmpMat.m10 = -tmpMat.m01;
		tmpMat.m11 = tmpMat.m00;
		tmpMat.m20 = x;
		tmpMat.m21 = y;
		
		tmpVec.x = -w;
		tmpVec.y = -h;
		tmpVec.z = 1.0f;
		Matrix3f.transform(tmpMat, tmpVec, tmpVec);
		
		copyBuffer.putFloat(tmpVec.x);
		copyBuffer.putFloat(tmpVec.y);
		copyBuffer.putFloat(tx);
		copyBuffer.putFloat(ty);
		
		tmpVec.x = w;
		tmpVec.y = -h;
		tmpVec.z = 1.0f;
		Matrix3f.transform(tmpMat, tmpVec, tmpVec);
		
		copyBuffer.putFloat(tmpVec.x);
		copyBuffer.putFloat(tmpVec.y);
		copyBuffer.putFloat(tx + tw);
		copyBuffer.putFloat(ty);
		
		tmpVec.x = w;
		tmpVec.y = h;
		tmpVec.z = 1.0f;
		Matrix3f.transform(tmpMat, tmpVec, tmpVec);
		
		copyBuffer.putFloat(tmpVec.x);
		copyBuffer.putFloat(tmpVec.y);
		copyBuffer.putFloat(tx + tw);
		copyBuffer.putFloat(ty + th);
		
		tmpVec.x = -w;
		tmpVec.y = h;
		tmpVec.z = 1.0f;
		Matrix3f.transform(tmpMat, tmpVec, tmpVec);
		
		copyBuffer.putFloat(tmpVec.x);
		copyBuffer.putFloat(tmpVec.y);
		copyBuffer.putFloat(tx);
		copyBuffer.putFloat(ty + th);
	}

	static void pushGhostQuadAbberated(ByteBuffer copyBuffer, float offset, float scale, int sprite, float r, float g, float b, float a) {
		pushGhostQuad(copyBuffer, offset, scale, sprite, 0.0f, g, b, a);
		pushGhostQuad(copyBuffer, offset + 0.005f, scale, sprite, r, 0.0f, 0.0f, a);
	}

	static void pushGhostQuad(ByteBuffer copyBuffer, float offset, float scale, int sprite, float r, float g, float b, float a) {
		copyBuffer.putFloat(offset);
		copyBuffer.putFloat(scale);
		copyBuffer.putFloat(0.0f);
		copyBuffer.putFloat((float)sprite / ghostsSpriteCount);
		copyBuffer.putFloat(1.0f);
		copyBuffer.putFloat(1.0f / ghostsSpriteCount);
		copyBuffer.putFloat(r * a);
		copyBuffer.putFloat(g * a);
		copyBuffer.putFloat(b * a);
		++ghostsInstanceCount;
	}

	static void drawLensFlares(float sunScreenX, float sunScreenY) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);

		GlStateManager.setActiveTexture(GL_TEXTURE2);
		GlStateManager.bindTexture(EaglerDeferredPipeline.instance.sunOcclusionValueTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE1);
		GlStateManager.bindTexture(EaglerDeferredPipeline.instance.exposureBlendTexture);
		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(streaksTexture);

		streaksProgram.useProgram();

		Minecraft mc = Minecraft.getMinecraft();
		float aspectRatio = (float)mc.displayHeight / (float)mc.displayWidth;

		float fov = 90.0f / mc.entityRenderer.getFOVModifier(EaglerDeferredPipeline.instance.getPartialTicks(), true);
		float size = 0.075f * fov * (1.0f + MathHelper.sqrt_float(sunScreenX * sunScreenX + sunScreenY * sunScreenY));

		tmpMat.setIdentity();
		tmpMat.m00 = aspectRatio * 2.0f * size;
		tmpMat.m11 = size;
		tmpMat.m20 = sunScreenX;
		tmpMat.m21 = sunScreenY;

		float rotation = sunScreenX * sunScreenX * Math.signum(sunScreenX) + sunScreenY * sunScreenY * Math.signum(sunScreenY);

		tmpMat2.setIdentity();
		tmpMat2.m00 = MathHelper.cos(rotation);
		tmpMat2.m01 = MathHelper.sin(rotation);
		tmpMat2.m10 = -tmpMat2.m01;
		tmpMat2.m11 = tmpMat2.m00;
		Matrix3f.mul(tmpMat, tmpMat2, tmpMat);

		EaglerDeferredPipeline.uniformMatrixHelper(streaksProgram.uniforms.u_sunFlareMatrix3f, tmpMat);

		Vector3f v = DeferredStateManager.currentSunLightColor;
		float mag = 1.0f + DeferredStateManager.currentSunAngle.y * 0.8f;
		if(mag > 1.0f) {
			mag = 1.0f - (mag - 1.0f) * 20.0f;
			if(mag < 0.0f) {
				mag = 0.0f;
			}
		}
		mag = 0.003f * (1.0f + mag * mag * mag * 4.0f);
		_wglUniform3f(streaksProgram.uniforms.u_flareColor3f, v.x * mag * 0.5f, v.y * mag * 0.5f, v.z * mag * 0.5f);

		EaglercraftGPU.bindGLBufferArray(streaksVertexArray);
		_wglDrawElements(GL_TRIANGLES, streaksVertexCount + (streaksVertexCount >> 1), GL_UNSIGNED_SHORT, 0);

		ghostsProgram.useProgram();

		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.bindTexture(ghostsTexture);

		_wglUniform3f(ghostsProgram.uniforms.u_flareColor3f, v.x * mag, v.y * mag, v.z * mag);
		_wglUniform1f(ghostsProgram.uniforms.u_aspectRatio1f, aspectRatio);
		_wglUniform2f(ghostsProgram.uniforms.u_sunPosition2f, sunScreenX, sunScreenY);
		_wglUniform1f(ghostsProgram.uniforms.u_baseScale1f, fov);

		EaglercraftGPU.bindGLBufferArray(ghostsVertexArray);
		_wglDrawArraysInstanced(GL_TRIANGLES, 0, 6, ghostsInstanceCount);

		GlStateManager.disableBlend();
	}

	static void destroy() {
		if(streaksVertexArray != null) {
			_wglDeleteVertexArrays(streaksVertexArray);
			streaksVertexArray = null;
		}
		if(streaksVertexBuffer != null) {
			_wglDeleteBuffers(streaksVertexBuffer);
			streaksVertexBuffer = null;
		}
		if(ghostsVertexArray != null) {
			_wglDeleteVertexArrays(ghostsVertexArray);
			ghostsVertexArray = null;
		}
		if(ghostsVertexBuffer != null) {
			_wglDeleteBuffers(ghostsVertexBuffer);
			ghostsVertexBuffer = null;
		}
		if(streaksTexture != -1) {
			GlStateManager.deleteTexture(streaksTexture);
			streaksTexture = -1;
		}
		if(ghostsTexture != -1) {
			GlStateManager.deleteTexture(ghostsTexture);
			ghostsTexture = -1;
		}
		if(streaksProgram != null) {
			streaksProgram.destroy();
			streaksProgram = null;
		}
		if(ghostsProgram != null) {
			ghostsProgram.destroy();
			ghostsProgram = null;
		}
	}
}
