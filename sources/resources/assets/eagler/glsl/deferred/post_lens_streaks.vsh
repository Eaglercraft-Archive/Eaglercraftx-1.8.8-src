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
layout(location = 1) in vec2 a_texcoord2f;

out vec2 v_texcoord2f;
out float v_occlusion1f;

uniform sampler2D u_sunOcclusionValue;

uniform mat3 u_sunFlareMatrix3f;

void main() {
	v_occlusion1f = max(textureLod(u_sunOcclusionValue, vec2(0.5, 0.5), 0.0).r * 1.5 - 0.5, 0.0);
	if(v_occlusion1f == 0.0) {
		gl_Position = vec4(-10.0, -10.0, -10.0, 1.0);
		return;
	}
	v_texcoord2f = a_texcoord2f;
	vec3 pos3f = u_sunFlareMatrix3f * vec3(a_position2f, 1.0);
	gl_Position = vec4(pos3f.x, pos3f.y, 0.0, pos3f.z);
}
