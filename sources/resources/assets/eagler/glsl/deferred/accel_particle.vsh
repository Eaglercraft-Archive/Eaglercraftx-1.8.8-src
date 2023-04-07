#line 2

/*
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

precision lowp int;
precision highp float;
precision mediump sampler2D;

layout(location = 0) in vec2 a_position2f;

layout(location = 1) in vec3 p_position3f;
layout(location = 2) in vec2 p_texCoords2i;
layout(location = 3) in vec2 p_lightMap2f;
layout(location = 4) in vec2 p_particleSize_texCoordsSize_2i;
layout(location = 5) in vec4 p_color4f;

out vec2 v_texCoord2f;
out vec4 v_color4f;
out vec2 v_lightmap2f;

#ifdef COMPILE_FORWARD_VSH
out vec4 v_position4f;
uniform mat4 u_modelViewMatrix4f;
uniform mat4 u_projectionMatrix4f;
#endif

#ifdef COMPILE_GBUFFER_VSH
uniform mat4 u_matrixTransform;
#endif

uniform vec3 u_texCoordSize2f_particleSize1f;
uniform vec4 u_transformParam_1_2_3_4_f;
uniform float u_transformParam_5_f;

void main() {
	v_color4f = p_color4f.bgra;
	v_lightmap2f = p_lightMap2f;

	vec2 tex2f = a_position2f * 0.5 + 0.5;
	tex2f.y = 1.0 - tex2f.y;
	tex2f = p_texCoords2i + tex2f * p_particleSize_texCoordsSize_2i.y;
	v_texCoord2f = tex2f * u_texCoordSize2f_particleSize1f.xy;

	float particleSize = u_texCoordSize2f_particleSize1f.z * p_particleSize_texCoordsSize_2i.x;

	vec3 pos3f = p_position3f;
	pos3f.x += u_transformParam_1_2_3_4_f.x * particleSize * a_position2f.x;
	pos3f.x += u_transformParam_1_2_3_4_f.w * particleSize * a_position2f.y;
	pos3f.y += u_transformParam_1_2_3_4_f.y * particleSize * a_position2f.y;
	pos3f.z += u_transformParam_1_2_3_4_f.z * particleSize * a_position2f.x;
	pos3f.z += u_transformParam_5_f * particleSize * a_position2f.y;

#ifdef COMPILE_GBUFFER_VSH
	gl_Position = u_matrixTransform * vec4(pos3f, 1.0);
#endif
#ifdef COMPILE_FORWARD_VSH
	v_position4f = u_modelViewMatrix4f * vec4(pos3f, 1.0);
	gl_Position = u_projectionMatrix4f * v_position4f;
#endif
}
