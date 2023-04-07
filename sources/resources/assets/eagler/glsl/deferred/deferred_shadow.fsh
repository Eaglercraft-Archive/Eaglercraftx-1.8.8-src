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

#if !defined(COMPILE_ENABLE_ALPHA_TEST) && !defined(COMPILE_COLORED_SHADOWS)
#undef COMPILE_ENABLE_TEXTURE2D
#endif

#ifdef COMPILE_COLORED_SHADOWS
layout(location = 0) out vec4 output4f;
uniform vec4 u_color4f;
#endif

#ifdef COMPILE_ENABLE_TEXTURE2D
uniform sampler2D u_samplerTexture;
#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 v_texture2f;
#else
uniform vec2 u_textureCoords01;
#endif
#ifdef COMPILE_ENABLE_ALPHA_TEST
uniform float u_alphaTestRef1f;
#endif
#endif

void main() {
#ifdef COMPILE_COLORED_SHADOWS
	vec4 color = u_color4f;
#else
	vec4 color = vec4(1.0);
#endif

#ifdef COMPILE_ENABLE_TEXTURE2D
#ifdef COMPILE_TEXTURE_ATTRIB
	color *= texture(u_samplerTexture, v_texture2f);
#else
	color *= texture(u_samplerTexture, u_textureCoords01);
#endif
#ifdef COMPILE_ENABLE_ALPHA_TEST
	if(color.a < u_alphaTestRef1f) discard;
#endif
#endif

#ifdef COMPILE_COLORED_SHADOWS
	output4f = vec4(mix(vec3(1.0), color.rgb, color.a), color.a);
#endif
}
