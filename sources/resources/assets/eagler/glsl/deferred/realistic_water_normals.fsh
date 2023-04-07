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

layout(location = 0) out vec2 realisticWaterNormalOutput2f;

uniform sampler2D u_displacementTexture;
uniform vec2 u_sampleOffset2f;

void main() {
	float A = textureLod(u_displacementTexture, v_position2f, 0.0).r;
	float B = textureLod(u_displacementTexture, v_position2f + vec2(u_sampleOffset2f.x, 0.0), 0.0).r;
	float C = textureLod(u_displacementTexture, v_position2f - vec2(0.0, u_sampleOffset2f.y), 0.0).r;
	realisticWaterNormalOutput2f = clamp((vec2(A * A) - vec2(B, C) * vec2(B, C)) * 10.0 + 0.5, vec2(0.0), vec2(1.0));
}
