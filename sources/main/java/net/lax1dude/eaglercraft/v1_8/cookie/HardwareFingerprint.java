package net.lax1dude.eaglercraft.v1_8.cookie;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GLSLHeader;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.opengl.VSHInputLayoutParser;
import net.minecraft.client.renderer.texture.TextureUtil;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.crypto.GeneralDigest;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA256Digest;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class HardwareFingerprint {
	
	// This is used for generating encryption keys for storing cookies,
	// its supposed to make session hijacking more difficult for skids

	private static final String fingerprintIMG = "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";

	private static final String shaderPrecision = "precision lowp int;\nprecision mediump float;\nprecision mediump sampler2D;\n";

	private static byte[] fingerprint = null;

	public static final Logger logger = LogManager.getLogger("HardwareFingerprint");

	public static byte[] getFingerprint() {
		if(fingerprint == null) {
			try {
				fingerprint = generateFingerprint();
			}catch(Throwable t) {
				fingerprint = new byte[0];
			}
			if(fingerprint.length == 0) {
				logger.error("Failed to calculate hardware fingerprint, server cookies will not be encrypted!");
			}
		}
		return fingerprint;
	}

	private static byte[] generateFingerprint() {
		ImageData img = PlatformAssets.loadImageFile(Base64.decodeBase64(fingerprintIMG), "image/jpeg");
		if(img == null) {
			logger.error("Input image data is corrupt!");
			return new byte[0];
		}
		
		int[][] mipmapLevels = TextureUtil.generateMipmapData(7, 128,
				new int[][] { img.pixels, null, null, null, null, null, null, null });
		
		int helperTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(helperTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		TextureUtil.allocateTextureImpl(helperTexture, 7, 128, 128);
		TextureUtil.uploadTextureMipmap(mipmapLevels, 128, 128, 0, 0, false, false);
		if(checkAnisotropicFilteringSupport()) {
			_wglTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY, 16.0f);
		}
		
		IShaderGL vert;
		List<VSHInputLayoutParser.ShaderInput> vertLayout;
		if(DrawUtils.vshLocal != null) {
			vert = DrawUtils.vshLocal;
			vertLayout = DrawUtils.vshLocalLayout;
		}else {
			String vshLocalSrc = EagRuntime.getRequiredResourceString("/assets/eagler/glsl/local.vsh");
			vertLayout = VSHInputLayoutParser.getShaderInputs(vshLocalSrc);
			vert = _wglCreateShader(GL_VERTEX_SHADER);
			_wglShaderSource(vert, GLSLHeader.getVertexHeaderCompat(vshLocalSrc, DrawUtils.vertexShaderPrecision));
			_wglCompileShader(vert);
			if(_wglGetShaderi(vert, GL_COMPILE_STATUS) != GL_TRUE) {
				_wglDeleteShader(vert);
				GlStateManager.deleteTexture(helperTexture);
				return new byte[0];
			}
		}
		
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);
		_wglShaderSource(frag, GLSLHeader.getFragmentHeaderCompat(EagRuntime.getRequiredResourceString("/assets/eagler/glsl/hw_fingerprint.fsh"), shaderPrecision));
		_wglCompileShader(frag);
		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			_wglDeleteShader(vert);
			_wglDeleteShader(frag);
			GlStateManager.deleteTexture(helperTexture);
			return new byte[0];
		}
		
		IProgramGL program = _wglCreateProgram();
		
		_wglAttachShader(program, vert);
		_wglAttachShader(program, frag);

		if(EaglercraftGPU.checkOpenGLESVersion() == 200) {
			VSHInputLayoutParser.applyLayout(program, vertLayout);
		}

		_wglLinkProgram(program);
		_wglDetachShader(program, vert);
		_wglDetachShader(program, frag);
		if(DrawUtils.vshLocal == null) {
			_wglDeleteShader(vert);
		}
		_wglDeleteShader(frag);
		
		if(_wglGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
			_wglDeleteProgram(program);
			GlStateManager.deleteTexture(helperTexture);
			return new byte[0];
		}
		
		EaglercraftGPU.bindGLShaderProgram(program);
		_wglUniform1i(_wglGetUniformLocation(program, "u_inputTexture"), 0);
		
		float fovy = 90.0f;
		float aspect = 1.0f;
		float zNear = 0.01f;
		float zFar = 100.0f;
		FloatBuffer matrixUploadBuffer = EagRuntime.allocateFloatBuffer(16);
		float toRad = 0.0174532925f;
		float cotangent = (float) Math.cos(fovy * toRad * 0.5f) / (float) Math.sin(fovy * toRad * 0.5f);
		
		matrixUploadBuffer.put(cotangent / aspect);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(cotangent);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put((zFar + zNear) / (zFar - zNear));
		matrixUploadBuffer.put(2.0f * zFar * zNear / (zFar - zNear));
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(0.0f);
		matrixUploadBuffer.put(-1.0f);
		matrixUploadBuffer.put(0.0f);
		
		matrixUploadBuffer.flip();
		_wglUniformMatrix4fv(_wglGetUniformLocation(program, "u_textureMatrix"), false, matrixUploadBuffer);
		EagRuntime.freeFloatBuffer(matrixUploadBuffer);
		
		int[] oldViewport = new int[4];
		EaglercraftGPU.glGetInteger(GL_VIEWPORT, oldViewport);
		IFramebufferGL framebuffer = _wglCreateFramebuffer();
		int renderTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(renderTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		int dataLength;
		int type;
		if(EaglercraftGPU.checkHDRFramebufferSupport(32)) {
			dataLength = 256 * 256 * 4 * 4;
			type = GL_FLOAT;
			EaglercraftGPU.createFramebufferHDR32FTexture(GL_TEXTURE_2D, 0, 256, 256, GL_RGBA, false);
		}else if(EaglercraftGPU.checkHDRFramebufferSupport(16)) {
			dataLength = 256 * 256 * 4 * 2;
			type = _GL_HALF_FLOAT;
			EaglercraftGPU.createFramebufferHDR16FTexture(GL_TEXTURE_2D, 0, 256, 256, GL_RGBA, false);
		}else {
			dataLength = 256 * 256 * 4;
			type = GL_UNSIGNED_BYTE;
			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 256, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		}
		
		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(renderTexture), 0);
		_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
		
		GlStateManager.viewport(0, 0, 256, 256);
		GlStateManager.disableBlend();
		GlStateManager.bindTexture(helperTexture);
		
		DrawUtils.drawStandardQuad2D();
		
		_wglDeleteProgram(program);
		GlStateManager.deleteTexture(helperTexture);
		
		ByteBuffer readBuffer = EagRuntime.allocateByteBuffer(dataLength);
		EaglercraftGPU.glReadPixels(0, 0, 256, 256, GL_RGBA, type, readBuffer);
		
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		_wglDeleteFramebuffer(framebuffer);
		GlStateManager.deleteTexture(renderTexture);
		GlStateManager.viewport(oldViewport[0], oldViewport[1], oldViewport[2], oldViewport[3]);
		
		SHA256Digest digest = new SHA256Digest();
		byte[] copyBuffer = new byte[1024];
		
		byte[] b = ("eag" + EaglercraftGPU.glGetString(GL_VENDOR) + "; eag " + EaglercraftGPU.glGetString(GL_RENDERER)).getBytes(StandardCharsets.UTF_8);
		digest.update(b, 0, b.length);
		
		digestInts(digest, _wglGetInteger(0x8869), _wglGetInteger(0x8DFB), _wglGetInteger(0x8B4C), _wglGetInteger(0x8DFC), copyBuffer);
		digestInts(digest, _wglGetInteger(0x8DFD), _wglGetInteger(0x8872), _wglGetInteger(0x84E8), 69, copyBuffer);
		digestInts(digest, _wglGetInteger(0x0D33), _wglGetInteger(0x851C), _wglGetInteger(0x8B4D), 69, copyBuffer);
		
		if(EaglercraftGPU.checkOpenGLESVersion() >= 300) {
			digestInts(digest, _wglGetInteger(0x8B4A), _wglGetInteger(0x8A2B), _wglGetInteger(0x9122), _wglGetInteger(0x8B4B), copyBuffer);
			digestInts(digest, _wglGetInteger(0x8C8A), _wglGetInteger(0x8C8B), _wglGetInteger(0x8C80), _wglGetInteger(0x8B49), copyBuffer);
			digestInts(digest, _wglGetInteger(0x8A2D), _wglGetInteger(0x9125), _wglGetInteger(0x8904), _wglGetInteger(0x8905), copyBuffer);
			digestInts(digest, _wglGetInteger(0x8824), _wglGetInteger(0x8073), _wglGetInteger(0x88FF), _wglGetInteger(0x84FD), copyBuffer);
			digestInts(digest, _wglGetInteger(0x8CDF), _wglGetInteger(0x8A2F), _wglGetInteger(0x8A30), _wglGetInteger(0x8A34), copyBuffer);
			digestInts(digest, _wglGetInteger(0x8A2E),  _wglGetInteger(0x8A31), _wglGetInteger(0x8A33), _wglGetInteger(0x8D57), copyBuffer);
		}
		
		try {
			List<String> exts = Lists.newArrayList(getAllExtensions());
			Collections.sort(exts);
			EaglercraftRandom rand = new EaglercraftRandom(6942069420l + exts.size() * 69l + b.length);
			for (int i = exts.size() - 1; i > 0; --i) {
				int j = rand.nextInt(i + 1);
				Collections.swap(exts, i, j);
			}
			b = String.join(":>", exts).getBytes(StandardCharsets.UTF_8);
			digest.update(b, 0, b.length);
		}catch(Throwable t) {
		}
		
		int i;
		while(readBuffer.hasRemaining()) {
			i = Math.min(readBuffer.remaining(), copyBuffer.length);
			readBuffer.get(copyBuffer, 0, i);
			digest.update(copyBuffer, 0, i);
		}
		
		EagRuntime.freeByteBuffer(readBuffer);
		
		byte[] hashOut = new byte[32];
		digest.doFinal(hashOut, 0);
		
		return hashOut;
	}

	private static void digestInts(GeneralDigest digest, int i1, int i2, int i3, int i4, byte[] tmpBuffer) {
		tmpBuffer[0] = (byte)(i1 >>> 24);
		tmpBuffer[1] = (byte)(i1 >>> 16);
		tmpBuffer[2] = (byte)(i1 >>> 8);
		tmpBuffer[3] = (byte)(i1 & 0xFF);
		tmpBuffer[4] = (byte)(i2 >>> 24);
		tmpBuffer[5] = (byte)(i2 >>> 16);
		tmpBuffer[6] = (byte)(i2 >>> 8);
		tmpBuffer[7] = (byte)(i2 & 0xFF);
		tmpBuffer[8] = (byte)(i3 >>> 24);
		tmpBuffer[9] = (byte)(i3 >>> 16);
		tmpBuffer[10] = (byte)(i3 >>> 8);
		tmpBuffer[11] = (byte)(i3 & 0xFF);
		tmpBuffer[12] = (byte)(i4 >>> 24);
		tmpBuffer[13] = (byte)(i4 >>> 16);
		tmpBuffer[14] = (byte)(i4 >>> 8);
		tmpBuffer[15] = (byte)(i4 & 0xFF);
		digest.update(tmpBuffer, 0, 16);
	}
}
