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

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

#ifdef DEBUG_VIEW_18
precision highp sampler3D;
uniform sampler3D u_texture3D0;
uniform float u_fuckU1f;
#endif

uniform mat4 u_inverseViewMatrix;
uniform vec2 u_depthSliceStartEnd2f;

void main() {
#ifdef DEBUG_VIEW_0
	output4f = vec4(textureLod(u_texture0, v_position2f, 0.0).rgb, 1.0);
#endif
#ifdef DEBUG_VIEW_1
	vec3 color3f = textureLod(u_texture0, v_position2f, 0.0).rgb;
	if(color3f.x == 0.0 && color3f.y == 0.0 && color3f.z == 0.0) {
		output4f = vec4(0.0, 0.0, 0.0, 1.0);
	}else {
		output4f = vec4(normalize(mat3(u_inverseViewMatrix) * (color3f * 2.0 - 1.0)), 1.0);
	}
#endif
#ifdef DEBUG_VIEW_2
	output4f = vec4(textureLod(u_texture0, v_position2f, 0.0).a, textureLod(u_texture1, v_position2f, 0.0).a, 0.0, 1.0);
#endif
#ifdef DEBUG_VIEW_3
	vec4 color4f = textureLod(u_texture0, v_position2f, 0.0);
	output4f = vec4(color4f.b > 0.99 ? 1.0 : 0.0, color4f.a, 0.0, 1.0);
#endif
#ifdef DEBUG_VIEW_4
	output4f = vec4(vec3(clamp((textureLod(u_texture0, v_position2f, 0.0).r - u_depthSliceStartEnd2f.x) * u_depthSliceStartEnd2f.y, 0.0, 1.0)), 1.0);
#endif
#ifdef DEBUG_VIEW_5
	output4f = vec4(vec3(textureLod(u_texture0, (v_position2f + vec2(0.0, u_depthSliceStartEnd2f.y)) * vec2(1.0, u_depthSliceStartEnd2f.x), 0.0).r), 1.0);
#endif
#ifdef DEBUG_VIEW_6
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).r), 1.0);
#endif
#ifdef DEBUG_VIEW_7
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).a > 0.0 ? 1.0 : 0.0), 1.0);
#endif
#ifdef DEBUG_VIEW_8
	output4f = vec4(textureLod(u_texture0, v_position2f, 0.0).rgb * 10.0, 1.0);
	output4f.xyz /= (output4f.xyz + 1.0);
	output4f.xyz = sqrt(output4f.xyz);
#endif
#ifdef DEBUG_VIEW_9
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).g), 1.0);
#endif
#ifdef DEBUG_VIEW_10
	vec2 coord = (v_position2f + vec2(0.0, u_depthSliceStartEnd2f.y)) * vec2(1.0, u_depthSliceStartEnd2f.x);
	vec4 color2 = textureLod(u_texture1, coord, 0.0);
	output4f = vec4(mix(color2.rgb, vec3(textureLod(u_texture0, coord, 0.0).r), color2.a), 1.0);
#endif
#ifdef DEBUG_VIEW_11
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).a * 0.017), 1.0);
#endif
#ifdef DEBUG_VIEW_12
	output4f = vec4(abs(textureLod(u_texture0, v_position2f, 0.0).rgb) * 0.1, 1.0);
#endif
#ifdef DEBUG_VIEW_13
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).g > 0.0 ? 1.0 : 0.0), 1.0);
#endif
#ifdef DEBUG_VIEW_14
	output4f = vec4(textureLod(u_texture0, v_position2f.yx, 0.0).rgb * 2.5, 1.0);
	output4f.xyz /= (output4f.xyz + 1.0);
	output4f.xyz = sqrt(output4f.xyz);
#endif
#ifdef DEBUG_VIEW_15
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f.yx, 0.0).a > 0.0 ? 1.0 : 0.0), 1.0);
#endif
#ifdef DEBUG_VIEW_16
	output4f = vec4(textureLod(u_texture0, v_position2f, 0.0).rg, 0.0, 1.0);
#endif
#ifdef DEBUG_VIEW_17
	output4f = vec4(vec3(textureLod(u_texture0, v_position2f, 0.0).r) * 10.0, 1.0);
	output4f.xyz /= (output4f.xyz + 1.0);
	output4f.xyz = sqrt(output4f.xyz);
#endif
#ifdef DEBUG_VIEW_18
	output4f = vec4(vec3(textureLod(u_texture3D0, vec3(v_position2f, u_fuckU1f), 0.0).r), 1.0);
#endif
}
