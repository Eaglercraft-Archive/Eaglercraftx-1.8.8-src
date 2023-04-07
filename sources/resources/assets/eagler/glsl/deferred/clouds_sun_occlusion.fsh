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

layout(location = 0) out float occlusionOut1f;

uniform mat4x3 u_sampleMatrix4x3f;
uniform sampler2D u_cloudsTexture;

#define SAMPLE_DENSITY(v, a_, f)\
	f = u_sampleMatrix4x3f * v;\
	f.xy = (f.xz / (f.y + 1.0)) * 0.975 * 0.5 + 0.5;\
	if(f.xy == clamp(f.xy, vec2(0.001), vec2(0.999)))\
		a_ += textureLod(u_cloudsTexture, f.xy, 0.0).a * 0.125;\
	else\
		a_ += 0.125;

void main() {
	vec3 f;
	float accum = 0.0;

	SAMPLE_DENSITY(vec4(0.000, 0.000, 1.000, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.844, 0.521, 0.126, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.187, 0.979, 0.087, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.402, -0.904, 0.145, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.944, -0.316, 0.098, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.759, 0.427, 0.491, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.955, -0.285, 0.076, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.322, -0.664, 0.675, 1.0), accum, f)

	occlusionOut1f = clamp(sqrt(accum) * 3.0 - 1.0, 0.0, 1.0);
}
