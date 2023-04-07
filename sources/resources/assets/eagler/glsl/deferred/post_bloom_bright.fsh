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
uniform sampler2D u_gbufferMaterialTexture;
uniform sampler2D u_gbufferDepthTexture;

uniform vec4 u_outputSize4f;

void main() {
	float exposure = textureLod(u_framebufferLumaAvgInput, vec2(0.5), 0.0).r;
	float emission = 0.0;
	vec2 alignedUV = (floor(v_position2f * u_outputSize4f.xy) + 0.5) * u_outputSize4f.zw;
	vec4 inputColor = textureLod(u_lightingHDRFramebufferTexture, alignedUV, 0.0);
	if(inputColor.a > 0.0) {
		emission = textureLod(u_gbufferMaterialTexture, alignedUV, 0.0).b;
	}else {
		emission = textureLod(u_gbufferDepthTexture, alignedUV, 0.0).r <= 0.0000001 ? 10.0 : 0.0;
	}
	float f = dot(inputColor.rgb, vec3(0.2126, 0.7152, 0.0722)) * (5.0 + emission * 15.0);
	if(f > 2.0 + exposure) {
		output4f = vec4(min(inputColor.rgb, vec3(5.0)) * (0.75 + exposure * 1.5) * min(f - 2.0 - exposure, 1.0), 1.0);
	}else {
		output4f = vec4(0.0);
	}
}
