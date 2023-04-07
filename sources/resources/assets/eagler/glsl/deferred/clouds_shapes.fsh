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
precision mediump sampler3D;

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform sampler3D u_inputTexture;
uniform float u_textureLevel1f;
uniform float u_textureLod1f;
uniform vec2 u_sampleWeights2f;

void main() {
	float objsample = textureLod(u_inputTexture, vec3(v_position2f, u_textureLevel1f), u_textureLod1f).r;
	output4f = vec4(objsample * u_sampleWeights2f.x, 0.0, 0.0, objsample * u_sampleWeights2f.y + 1.0);
}
