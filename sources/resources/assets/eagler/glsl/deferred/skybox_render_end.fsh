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

in vec3 v_position3f;

layout(location = 0) out vec4 output4f;

#define SKY_BRIGHTNESS 0.015

uniform sampler2D u_skyTexture;

uniform vec2 u_skyTextureScale2f;

void main() {
	gl_FragDepth = 0.0;
	vec3 viewDir = normalize(v_position3f);

	vec3 blending = abs(viewDir * viewDir * viewDir);
	blending = normalize(max(blending, 0.00001));
	float b = (blending.x + blending.y + blending.z);
	blending /= b;

	vec3 blendedSkyColor = texture(u_skyTexture, v_position3f.zy * u_skyTextureScale2f).rgb * blending.x;
	blendedSkyColor += texture(u_skyTexture, v_position3f.xz * u_skyTextureScale2f).rgb * blending.y;
	blendedSkyColor += texture(u_skyTexture, v_position3f.xy * u_skyTextureScale2f).rgb * blending.z;

	output4f = vec4(blendedSkyColor * blendedSkyColor * blendedSkyColor * SKY_BRIGHTNESS, 0.0);
}
