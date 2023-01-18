#line 2

/*
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_inputTexture;
uniform float u_textureLod1f;
uniform vec4 u_blendFactor4f;
uniform vec4 u_blendBias4f;
uniform mat3 u_matrixTransform;

void main() {
	vec3 coords = u_matrixTransform * vec3(v_position2f, 1.0);
	vec4 color4f = textureLod(u_inputTexture, coords.xy, u_textureLod1f);
	output4f = color4f * u_blendFactor4f + u_blendBias4f;
}
