package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class SharedPipelineShaders {

	public static IShaderGL deferred_local = null;
	public static IShaderGL lighting_mesh = null;

	public static void init() throws ShaderException {
		free();
		deferred_local = ShaderCompiler.compileShader("deferred_local_vsh", GL_VERTEX_SHADER, ShaderSource.deferred_local_vsh);
		lighting_mesh = ShaderCompiler.compileShader("lighting_mesh", GL_VERTEX_SHADER, ShaderSource.lighting_mesh_vsh);
	}

	public static void free() {
		if(deferred_local != null) {
			deferred_local.free();
			deferred_local = null;
		}
		if(lighting_mesh != null) {
			lighting_mesh.free();
			lighting_mesh = null;
		}
	}

}
