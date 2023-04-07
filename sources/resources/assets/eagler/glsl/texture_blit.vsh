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
precision lowp float;
precision lowp sampler2D;

layout(location = 0) in vec2 a_position2f;

out vec2 v_texCoords2f;

uniform vec4 u_srcCoords4f;
uniform vec4 u_dstCoords4f;

void main() {
	vec2 uv = a_position2f * 0.5 + 0.5;
	v_texCoords2f = u_srcCoords4f.xy + u_srcCoords4f.zw * uv;
	gl_Position = vec4(u_dstCoords4f.xy + u_dstCoords4f.zw * uv, 0.0, 1.0);
}
