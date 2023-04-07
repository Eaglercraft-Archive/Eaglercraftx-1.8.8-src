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
precision mediump sampler2D;

layout(location = 0) in vec2 a_position2f;

out vec2 v_position2f;

uniform mat3x2 u_transformMatrix3x2f;

void main() {
	v_position2f = a_position2f * 0.5 + 0.5;
	gl_Position = vec4(u_transformMatrix3x2f * vec3(a_position2f, 1.0), 0.0, 1.0);
}
