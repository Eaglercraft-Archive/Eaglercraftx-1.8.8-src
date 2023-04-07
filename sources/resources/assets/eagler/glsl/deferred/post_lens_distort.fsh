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

uniform sampler2D u_inputTexture;

vec2 distortUV(in vec2 uv, in float k){
	vec2 t = uv - 0.5;
	return dot(t, t) * k * t + t + 0.5;
}

#define DISTORT_AMOUNT -0.05

void main() {
	output4f = vec4(textureLod(u_inputTexture, distortUV(v_position2f, DISTORT_AMOUNT + 0.01), 0.0).r,
		textureLod(u_inputTexture, distortUV(v_position2f, DISTORT_AMOUNT), 0.0).ga,
		textureLod(u_inputTexture, distortUV(v_position2f, DISTORT_AMOUNT - 0.01), 0.0).b).rgab;
}
