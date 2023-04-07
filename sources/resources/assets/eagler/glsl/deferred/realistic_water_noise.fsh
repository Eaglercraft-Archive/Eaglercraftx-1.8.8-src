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

layout(location = 0) out float realisticWaterDisplacementOutput1f;

uniform sampler2D u_noiseTexture;

uniform vec4 u_waveTimer4f;

void main() {
	vec2 sampleA = v_position2f + vec2(-0.093, -0.056) * (u_waveTimer4f.x + 0.12);
	sampleA = textureLod(u_noiseTexture, fract(sampleA) * 0.46875 + vec2(0.015625, 0.015625), 0.0).rg;
	vec2 sampleB = v_position2f + vec2(0.075, 0.153) * (u_waveTimer4f.x + 1.33);
	sampleB = textureLod(u_noiseTexture, fract(sampleB) * 0.46875 + vec2(0.515625, 0.015625), 0.0).rg;
	vec2 sampleC = v_position2f + vec2(0.075, -0.113) * u_waveTimer4f.x + vec2(sampleA.g, sampleB.g) * 0.15;
	sampleC = textureLod(u_noiseTexture, fract(sampleC) * 0.46875 + vec2(0.515625, 0.515625), 0.0).rg;
	vec2 sampleD = v_position2f + vec2(-0.135, 0.092) * u_waveTimer4f.x + sampleC * 0.1;
	sampleD = textureLod(u_noiseTexture, fract(sampleD) * 0.46875 + vec2(0.015625, 0.515625), 0.0).rg;
	realisticWaterDisplacementOutput1f = dot(vec4(sampleA.r, sampleB.r, sampleC.r, sampleD.r), vec4(0.63, 0.40, 0.035, 0.035)) + dot(vec2(sampleC.g, sampleD.g), vec2(-0.075 * sampleA.g, 0.053 * sampleA.r));
}
