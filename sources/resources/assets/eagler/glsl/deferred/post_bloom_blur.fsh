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

uniform vec2 u_sampleOffset2f;
uniform vec4 u_outputSize4f;

void main() {
	vec2 alignedUV = (floor(v_position2f * u_outputSize4f.xy) + 0.5) * u_outputSize4f.zw;
	vec4 accum = textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 7.0, 0.0) * 0.0005;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 6.0, 0.0) * 0.0024;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 5.0, 0.0) * 0.0092;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 4.0, 0.0) * 0.0278;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 3.0, 0.0) * 0.0656;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 2.0, 0.0) * 0.1210;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f, 0.0) * 0.1747;
	accum += textureLod(u_inputTexture, alignedUV, 0.0) * 0.1974;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f, 0.0) * 0.1747;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 2.0, 0.0) * 0.1210;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 3.0, 0.0) * 0.0656;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 4.0, 0.0) * 0.0278;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 5.0, 0.0) * 0.0092;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 6.0, 0.0) * 0.0024;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 7.0, 0.0) * 0.0005;
	output4f = accum;
}
