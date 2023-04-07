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

layout(location = 0) out vec4 output4f;

#ifdef COMPILE_NORMAL_ATTRIB
in vec3 v_normal3f;
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 v_lightmap2f;
#else
uniform vec2 u_textureCoords02;
#endif

void main() {
#ifdef COMPILE_NORMAL_ATTRIB
	output4f = vec4(v_normal3f * 0.5 + 0.5,
#else
	output4f = vec4(0.0, 1.0, 0.0,
#endif
#ifdef COMPILE_LIGHTMAP_ATTRIB
	v_lightmap2f.y * v_lightmap2f.y * 0.99 + 0.01);
#else
	u_textureCoords02.y * u_textureCoords02.y * 0.99 + 0.01);
#endif
}
