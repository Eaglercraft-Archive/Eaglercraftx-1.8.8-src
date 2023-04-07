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
precision highp sampler2D;

layout(location = 0) in vec3 a_position3f;
layout(location = 1) in vec2 a_colorIndex2f;

out vec3 v_position3f;
out vec3 v_color3f;

uniform mat4 u_viewMatrix4f;
#ifdef COMPILE_PARABOLOID_SKY
uniform float u_farPlane1f;
#else
uniform mat4 u_projMatrix4f;
#endif
uniform sampler2D u_renderedAtmosphere;

void main() {
	v_position3f = a_position3f;
	v_color3f = textureLod(u_renderedAtmosphere, a_colorIndex2f, 0.0).rgb;
	vec4 pos = u_viewMatrix4f * vec4(a_position3f, 0.0);

#ifdef COMPILE_PARABOLOID_SKY
	float dist = pos.z;
	pos.xyz = normalize(pos.xyz);
	pos.xy /= 1.0 - pos.z;
	pos.z = dist / u_farPlane1f;
	gl_Position = vec4(pos.xyz, 1.0);
#else
	gl_Position = u_projMatrix4f * vec4(pos.xyz, 1.0);
#endif
}
