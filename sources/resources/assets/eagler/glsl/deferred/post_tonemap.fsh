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

uniform sampler2D u_lightingHDRFramebufferTexture;
uniform sampler2D u_framebufferLumaAvgInput;
uniform sampler2D u_ditherTexture;
uniform vec3 u_exposure3f;
uniform vec2 u_ditherScale2f;

void main() {
	float lumaHDR = textureLod(u_framebufferLumaAvgInput, vec2(0.5), 0.0).r;
	vec3 input3f = textureLod(u_lightingHDRFramebufferTexture, v_position2f, 0.0).rgb;

	input3f /= (0.1 + min(lumaHDR * 6.0, 4.0));

	input3f *= u_exposure3f;

	// ACES, modified to approximate gamma correction
	const float a = 1.22;
	const float b = 1.78;
	const float c = 1.22;
	const float d = 1.79;
	const float e = 0.29;

	input3f = clamp((input3f * (a * input3f + b)) / (input3f * (c * input3f + d) + e), 0.0, 1.0);

	// desaturate a bit, makes it look like less of a cartoon
	float sat = 0.8;
	float luma = dot(input3f, vec3(0.299, 0.587, 0.114));
	input3f = (input3f - luma) * sat + luma;
	input3f += textureLod(u_ditherTexture, v_position2f * u_ditherScale2f, 0.0).r / 255.0;

	output4f = vec4(clamp(input3f, 0.0, 1.0), luma);
}
