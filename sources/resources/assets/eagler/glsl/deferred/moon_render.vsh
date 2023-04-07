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

layout(location = 0) in vec2 a_position2f;

out vec2 v_position2f;
out vec3 v_position3f;

uniform mat4 u_modelMatrix4f;
uniform mat4 u_viewMatrix4f;
uniform mat4 u_projMatrix4f;

void main() {
	v_position2f = a_position2f * 0.5 + 0.5;
	v_position3f = (u_modelMatrix4f * vec4(a_position2f, -13.0, 1.0)).xyz;
	gl_Position = u_viewMatrix4f * vec4(v_position3f, 0.0);
	gl_Position = u_projMatrix4f * vec4(gl_Position.xyz, 1.0);
}
