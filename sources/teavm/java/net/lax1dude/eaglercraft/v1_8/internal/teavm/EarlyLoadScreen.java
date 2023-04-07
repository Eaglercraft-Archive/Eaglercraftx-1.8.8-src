package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagUtils;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
public class EarlyLoadScreen {

	public static final String loadScreen = "iVBORw0KGgoAAAANSUhEUgAAAMAAAADACAYAAABS3GwHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAHx0lEQVR42u3da27jIBRAYbfqFp1FuovM/GLEMIDBhsRJviNVapsYY8y5vPz4ut/v9wX4UL4VAQgAEAAgAEAAgAAAAQACAAQACAAQACAAQACAAAABAAIABAAIABAAIABAAIAAAAEAAgAEAAgAEAAgAEAAgAAAAQACAAQACAAQACAAQACAAAABAAIABAAIABAAIABAAIAAAAEAAgAEAAgAEAAgAAgAEAAgAEAAgAAAAQACAAQACAAQACAAQACAAMBr86MI3ovf39/i/9Z1XdZ1VUgEeN/Kf7vdqt8hgC7QW6OCE+CjK/+2bcv9fieCLtDjux9x/1t/u1xOveWSlisBXmQASoB/+fr6+vv7/X7vHteE8hxZrrpAkyo/2mU42soSgAAfN8YZ3aoSQOV/GNu2ZX9vGdjPEuBnVmXIVYqePly8famCne0TtuS1tt/a9kfSbWnqZw2u9yQesc91XZv7/iO2a+I+iG3b7uu63pdl2f1Z17WaTksaaXrbtk3JaynvR/O5l6/WtPaON3d8tf3v7e9d+RkVPeIVyDRKpREtfL+nGdxL7/f3d9m2bTdS5VZL4/Rz0fcRszm32604jZrLUyi/UXlb1/WlunKhTE63iCMif0tkao1IaXqlqFWKlr2RsTUPpXRLrUnYpqVlircfdby9LUCpbHpa1lyeW8tgL51SmZ9N+2dE5GqJlrkI0xJxaumV0ixt0xrd07TDdrl+aDoeGNnfbzne0RE1HqSOaF3SljptyXP7qF3QN3zi4Yw9LdF0r5+Zs7u175mLirU85KJiLbK3pt2bj1qZ1CJaz356WoD0u2ejaq11XNf1708uf73jqqeOAXotbIlgZ/t0tfSPRulZ050j0jubRjz2CGU/clyRRvvwv1LPIR4X5r6TtlJPmwY9W5la54vfea5+Zhm2dnniyj+j3GtdxCsMzL+vWAmuyujK2dLXnVGGYSZsduXPlV0625Vbk0nlnFlXhrYAezdjPFOa2sD4GRetlY5hdhnmpoHjKcXZlb927Llp4JCvWYHy8leDxpHgbCH0zBo9s3vyiLK8QiBIxwiPaHWnjwFGZbjl9r5RAtxut92Fp5GLTqPHP735qpXDrK5QbjFz27b/Wp802IXu2Yz6cGoadDmwCHV0enVJFpbCfkqLQ6Mvg9g7riPToEfyfrYMl4ZLOUadw1rZh33H/ytNjcbnunfavakeX02As3P1rZVoT4KeVdBXESDN05HV4pFXDaQrxqkE6TnISfC0dYAZA5PSSu3orkeYiSil/Sl3cm3b9t+NKbMHxHtTpenvcT7C33Gez+b1e3QFvvrUY2nhZ/Qi0KtMC+f6/KWpytnnsjWoXuKWyNaZkyud/HTh55mVvTYt++h8zDiXlTFnkwS1wfhlBZgxj917acNe9H9mZWuJvjPuez0azJ5RPj1T3kMe/zJyUNMzkMpdJts6MNybyckNXo/cwLI0XtZ8ZkaldBwt2x65RHvGMRwZoO9dWLh3CfqofC0zZhtKU5fpiWkVIE4n3b423Zemf0SA5cQdVenxt9x70FJ+8TEfkbxUuXqDytnp0L2p0kewzJjeOnMSWtKKt92rQCNageXEDTot05xH1iZy5Xf2lsra9iMrZDjW2dG9ha/7wLuNS5ctpDevt9y2WBu0ptvnxh2l75YutOrtu+/1m+N8tw66022PlGHrcfVuP+NCwNrg+2ETFPcPI45yLSu8s1Yg8UY3xb8K6WP2WualrzJjhDl8f2Ll721iPeiWAG8hwMw+LQhw6co/cpWaPO/DR4wBchU23APQMiMy43EhuAZDp0FfaQxwRCJjAQK8xTigp0uk4hPgowbH+vkEAD4GL8gAAQACAAQACAAQACAAQACAAAABAAIABAAIABAAIABAAIAAAAEAAgAEAK7NJR6M9S6PLQzPHZr1sulSuXmCxQu3APHz+sNP6wOspr09/CL76ym3Tzr2t2sBHhk13+UYwgsmnvFeXwI8qUtRinZxZNq27e/3tm3Lvg8gjWRpxc09Rj3eb2l/ufTiZ5CG78Sfn305eO7durX8tH4W8pB+Pz32vTQJcGAcED+0Nv5//Pbw9GTl+sKh8sVRMo2WoWkPJy0WpiRB6XVFpa5IvF28v3RfvX36mpylBwKXPktbkjiI1I69liYBTg6E4wqTkyOWolRB4nTSE5XuszaI3dvfngRppM1F+9auTG4fuW1raeXendYiWk+aBBjQf44jZW/TWoriV3gRddwi9L57IPfY9lA5Q3nF6YZyq33WIkLt/NTSJMCAcUD4/Wzhxt2o3Hjg0a3emSdPt7Q2t9vtn3KrfXY0L7U091rWo599xBggjSgh0pSa79aTl4ugaR8913qU9ld6vWlvd6bn+7mB+96MUHpcLULtHftemlqAAwKEwVd6MtNBbK4C7kWLuMkuDT5zA+za/nKzMC0VOu0CtXQhal2UeKCfG2PUPsvNZrUcey3NV8Dj0Z/cvctNQ77DmogWAM0S7M0gQQvwluS6HFZ0CQA8DJdDgwAAAQACAAQACAAQACAAQACAAAABAAIABAAIABAAIABAAIAAAAEAAgAEAAgAEAAgAEAAgAAAAQACAAQACAAQACAAQACAAAABAAIABAAIABAAIABAAIAAAAEAAgAEAAgAEAAgAEAAgAAAAYBlWf4A1W4Hx65cJAoAAAAASUVORK5CYII=";
	public static final String enableScreen = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAACXBIWXMAAC4jAAAuIwF4pT92AAAEAklEQVR42u2dvXbjIBBG7T0+xw+gTp06v//LmE6dO/VR5a3wGZNh+BGSFeveJgkIBrDy8TGKds8/Pz/PExyW8/P55AY4MP9YgmNzmeeZVUABAA8AKADgAQAFADwAoACABwAUAPAAgAIAHgBQAMADAAoAeABAAY7LOI7fpQDX65VPtZCt18w5d7rdbigAbOgBxnE8DcPwJnnDMCTrNJlsUVcizTnj9HWxeVvINfN9y361OdTEk30551ZZt3PsvYDYxOSChoPQ6sJ21mRLBm61jY0lpy61gDKWNdfcNcv5wErWLbfPF88I9/s9WtayzopXS85YtPqcMeT23SqedV1pucal1V4iTUooV/IaWSfbWHU5JmkvpmzrsayaB9DqfJnVTpMff72sc869/WzVlcjjOI7mOOVYfBzfT05exLfT5pqae008a71Ly6tPASV79CfPylvFjpm+teLH+tXiF5nA2LOAUMpCibckWpPBUOJT20btFuDjyK8p+S45Z4fX+ti+LDb3pef62PosWbfkDbBW8mFPhB/gt8Vr7gG+kZK9+C/GM2+ArffnnKRHbT5gSdJoK0+ydrziGyCW115LolLxnHOr59q3lt89b6U8Czg4pgdI5bUtKY3VzfOclGBtTLVSmmqn1cdyC7Iud+5791KX1MLJDz3Mg2s59pK6sM/asdTmLrRx5pzjS+e+awWw9lstVeuv1/a10rqwT8sn5LQr8RzaMVfmKrR2qfnFjs57/puLS0nyoTZp0fL8XGq+ap8v4AES+3Msx74kN2/tmblewWoXPl9o+RykZH5/5hTQYv+y+vj084XcPHpJbHmt1s7yGbV1q+UBnHO/gnoZje2RmuzK/Vr2F3sWEF6TGkvutqH5CG08qTmk5u77tLyK5Qtq62rgxRA8AO8FHBkygQeHLQAFADwAoACABwAUAPAAgAIAHgBQAMADAAoAeABAAQAPACgA4AEABQA8AKAAgAcAFAC+3gNM03Tqum7VQSyN4dtvMdZDKcBWC9oqhr8JoIEHeDwep77vf5VJfL0vl9fLa/u+f+vPfx9eszSGNXZo5AH6vlcXW36gsqykrzViwAIPYL3r3nXd63v5m6i9J2+VaT8viWGNHZQbYE97+KdjHPIGKH0XPSyL7eXSjPk2YZlsN03Tq21OjLAs598ZggIT2MpMbW3IMICFN0Dsv4xpfUbfAvIAK9wAcOAtAMgDwJHzAIACAB4AUADAAwAKAHgAQAEADwAoAOABAAUAPACgAIAHABQA8ACAAgAeAFAAwAMACgB4AEABAA8AKADgAQAFADwAoACABwAUAPAAgAIAHgBQAMADAAoAeABAAQAPACgA4AEABQA8AKAAgAcAFADwANCe/0of1jQ8XY5YAAAAAElFTkSuQmCC";

	private static IBufferGL vbo = null;
	private static IProgramGL program = null;
	
	public static void paintScreen() {
		
		ITextureGL tex = _wglGenTextures();
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		ImageData img = PlatformAssets.loadImageFile(Base64.decodeBase64(loadScreen));
		ByteBuffer upload = PlatformRuntime.allocateByteBuffer(192*192*4);
		IntBuffer pixelUpload = upload.asIntBuffer();
		pixelUpload.put(img.pixels);
		pixelUpload.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 192, 192, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelUpload);
		
		FloatBuffer vertexUpload = upload.asFloatBuffer();
		vertexUpload.clear();
		vertexUpload.put(0.0f); vertexUpload.put(0.0f);
		vertexUpload.put(0.0f); vertexUpload.put(1.0f);
		vertexUpload.put(1.0f); vertexUpload.put(0.0f);
		vertexUpload.put(1.0f); vertexUpload.put(0.0f);
		vertexUpload.put(0.0f); vertexUpload.put(1.0f);
		vertexUpload.put(1.0f); vertexUpload.put(1.0f);
		vertexUpload.flip();
			
		vbo = _wglGenBuffers();
		_wglBindBuffer(GL_ARRAY_BUFFER, vbo);
		_wglBufferData(GL_ARRAY_BUFFER, vertexUpload, GL_STATIC_DRAW);
		
		PlatformRuntime.freeByteBuffer(upload);

		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		_wglShaderSource(vert, "#version 300 es\nprecision lowp float; layout(location = 0) in vec2 a_pos; out vec2 v_pos; void main() { gl_Position = vec4(((v_pos = a_pos) - 0.5) * vec2(2.0, -2.0), 0.0, 1.0); }");
		_wglCompileShader(vert);
		
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);
		_wglShaderSource(frag, "#version 300 es\nprecision lowp float; in vec2 v_pos; layout(location = 0) out vec4 fragColor; uniform sampler2D tex; uniform vec2 aspect; void main() { fragColor = vec4(texture(tex, clamp(v_pos * aspect - ((aspect - 1.0) * 0.5), 0.02, 0.98)).rgb, 1.0); }");
		_wglCompileShader(frag);
		
		program = _wglCreateProgram();
		
		_wglAttachShader(program, vert);
		_wglAttachShader(program, frag);
		_wglLinkProgram(program);
		_wglDetachShader(program, vert);
		_wglDetachShader(program, frag);
		_wglDeleteShader(vert);
		_wglDeleteShader(frag);
		
		_wglUseProgram(program);
		_wglUniform1i(_wglGetUniformLocation(program, "tex"), 0);

		int width = PlatformInput.getWindowWidth();
		int height = PlatformInput.getWindowHeight();
		float x, y;
		if(width > height) {
			x = (float)width / (float)height;
			y = 1.0f;
		}else {
			x = 1.0f;
			y = (float)height / (float)width;
		}
		
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		
		_wglViewport(0, 0, width, height);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		_wglClear(GL_COLOR_BUFFER_BIT);
		
		_wglUniform2f(_wglGetUniformLocation(program, "aspect"), x, y);
		
		IBufferArrayGL vao = _wglGenVertexArrays();
		_wglBindVertexArray(vao);
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
		_wglDisableVertexAttribArray(0);
		
		PlatformInput.update();
		EagUtils.sleep(50l); // allow webgl to flush

		_wglUseProgram(null);
		_wglBindBuffer(GL_ARRAY_BUFFER, null);
		_wglBindTexture(GL_TEXTURE_2D, null);
		_wglDeleteTextures(tex);
		_wglDeleteVertexArrays(vao);
	}
	
	public static void paintEnable() {
		
		ITextureGL tex = _wglGenTextures();
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		ImageData img = PlatformAssets.loadImageFile(Base64.decodeBase64(enableScreen));
		IntBuffer upload = PlatformRuntime.allocateIntBuffer(128*128);
		upload.put(img.pixels);
		upload.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 128, 128, 0, GL_RGBA, GL_UNSIGNED_BYTE, upload);
		
		PlatformRuntime.freeIntBuffer(upload);
		
		_wglUseProgram(program);

		int width = PlatformInput.getWindowWidth();
		int height = PlatformInput.getWindowHeight();
		float x, y;
		if(width > height) {
			x = (float)width / (float)height;
			y = 1.0f;
		}else {
			x = 1.0f;
			y = (float)height / (float)width;
		}
		
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		
		_wglViewport(0, 0, width, height);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		_wglClear(GL_COLOR_BUFFER_BIT);
		
		_wglUniform2f(_wglGetUniformLocation(program, "aspect"), x, y);

		IBufferArrayGL vao = _wglGenVertexArrays();
		_wglBindVertexArray(vao);
		_wglBindBuffer(GL_ARRAY_BUFFER, vbo);
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
		_wglDisableVertexAttribArray(0);
		
		PlatformInput.update();
		EagUtils.sleep(50l); // allow webgl to flush

		_wglUseProgram(null);
		_wglBindBuffer(GL_ARRAY_BUFFER, null);
		_wglBindTexture(GL_TEXTURE_2D, null);
		_wglDeleteTextures(tex);
		_wglDeleteVertexArrays(vao);
		
	}
	
	public static void paintFinal(byte[] image) {
		ITextureGL tex = _wglGenTextures();
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		ImageData img = PlatformAssets.loadImageFile(image);
		IntBuffer upload = PlatformRuntime.allocateIntBuffer(256*256);
		upload.put(img.pixels);
		upload.flip();
		_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 256, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, upload);
		
		PlatformRuntime.freeIntBuffer(upload);
		
		_wglUseProgram(program);

		int width = PlatformInput.getWindowWidth();
		int height = PlatformInput.getWindowHeight();
		float x, y;
		if(width > height) {
			x = (float)width / (float)height;
			y = 1.0f;
		}else {
			x = 1.0f;
			y = (float)height / (float)width;
		}
		
		_wglActiveTexture(GL_TEXTURE0);
		_wglBindTexture(GL_TEXTURE_2D, tex);
		
		_wglViewport(0, 0, width, height);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		_wglClear(GL_COLOR_BUFFER_BIT);
		
		_wglUniform2f(_wglGetUniformLocation(program, "aspect"), x, y);

		IBufferArrayGL vao = _wglGenVertexArrays();
		_wglBindVertexArray(vao);
		_wglBindBuffer(GL_ARRAY_BUFFER, vbo);
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
		_wglDisableVertexAttribArray(0);
		
		PlatformInput.update();
		EagUtils.sleep(50l); // allow webgl to flush

		_wglUseProgram(null);
		_wglBindBuffer(GL_ARRAY_BUFFER, null);
		_wglBindTexture(GL_TEXTURE_2D, null);
		_wglDeleteTextures(tex);
		_wglDeleteVertexArrays(vao);
		_wglDeleteBuffers(vbo);
		_wglDeleteProgram(program);
	}
	
}
