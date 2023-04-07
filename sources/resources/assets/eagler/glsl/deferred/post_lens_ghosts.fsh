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
precision mediump float;
precision mediump sampler2D;

layout(location = 0) out vec4 output4f;

in vec2 v_texcoord2f;
in vec3 v_color3f;
in float v_occlusion1f;

uniform sampler2D u_flareTexture;

uniform vec3 u_flareColor3f;

void main() {
	vec3 color3f = vec3(texture(u_flareTexture, v_texcoord2f).r);
	output4f = vec4(color3f * color3f * v_color3f * v_occlusion1f * u_flareColor3f, 0.0);
}
