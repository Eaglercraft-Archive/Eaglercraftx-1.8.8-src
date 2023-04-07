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

in vec2 v_texCoords2f;

#ifndef COMPILE_BLIT_DEPTH
layout(location = 0) out vec4 output4f;
#endif

uniform sampler2D u_inputTexture;
uniform float u_textureLod1f;

#ifdef COMPILE_PIXEL_ALIGNMENT
uniform vec4 u_pixelAlignmentSizes4f;
uniform vec2 u_pixelAlignmentOffset2f;
#endif

void main() {
	vec2 uv2f = v_texCoords2f;
#ifdef COMPILE_PIXEL_ALIGNMENT
	uv2f = (floor(uv2f * u_pixelAlignmentSizes4f.xy) + u_pixelAlignmentOffset2f) * u_pixelAlignmentSizes4f.zw;
#endif
#ifndef COMPILE_BLIT_DEPTH
	output4f = textureLod(u_inputTexture, uv2f, u_textureLod1f);
#else
	gl_FragDepth = textureLod(u_inputTexture, uv2f, u_textureLod1f).r;
#endif
}
