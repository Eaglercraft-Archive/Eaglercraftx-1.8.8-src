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

layout(location = 0) out float exposureOut1f;

uniform sampler2D u_inputTexture;
uniform vec4 u_sampleOffset4f;

#ifdef CALCULATE_LUMINANCE
#define TAKE_SAMPLE(samplerIn, posIn) dot(textureLod(samplerIn, posIn, 0.0).rgb, vec3(0.299, 0.587, 0.114))
#else
#define TAKE_SAMPLE(samplerIn, posIn) textureLod(samplerIn, posIn, 0.0).r
#endif

void main() {

	vec2 pixelPos = floor(v_position2f / u_sampleOffset4f.xy);

	float a = min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.25, 0.25)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.75, 0.25)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.75, 0.75)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.25, 0.75)) * u_sampleOffset4f.zw), 250.0);

	exposureOut1f = a * 0.25;
}
