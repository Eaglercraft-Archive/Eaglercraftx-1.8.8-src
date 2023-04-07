package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
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
public class ShaderProgram<U extends IProgramUniforms> {

	public final IProgramGL program;
	public final U uniforms;

	public ShaderProgram(IProgramGL program, U uniforms) {
		this.program = program;
		this.uniforms = uniforms;
	}

	public ShaderProgram<U> loadUniforms() {
		if(uniforms != null) {
			EaglercraftGPU.bindGLShaderProgram(program);
			uniforms.loadUniforms(program);
		}
		return this;
	}

	public void useProgram() {
		EaglercraftGPU.bindGLShaderProgram(program);
	}

	public void destroy() {
		program.free();
	}

}
