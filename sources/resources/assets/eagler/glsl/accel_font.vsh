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

layout(location = 0) in vec3 a_position3f;

layout(location = 1) in vec2 c_position2i;
layout(location = 2) in vec2 c_coords2i;
layout(location = 3) in vec4 c_color4f;

out vec2 v_texCoord2f;
out vec4 v_color4f;

uniform mat4 u_matrixTransform;
uniform vec2 u_charSize2f;
uniform vec2 u_charCoordSize2f;
uniform vec4 u_color4f;

void main() {
	v_color4f = c_color4f.bgra;
	float shadowBit = a_position3f.z;
	float boldBit = shadowBit >= 0.5 ? 1.0 : 0.0;
	shadowBit -= boldBit * 0.5;
	v_color4f.rgb *= (1.0 - shadowBit * 3.0);
	v_texCoord2f = (c_coords2i + a_position3f.xy) * u_charCoordSize2f;
	vec2 pos2d = c_position2i + vec2(shadowBit * 4.0);
	pos2d += a_position3f.xy * u_charSize2f;
	pos2d.x += boldBit;
	float italicBit = v_color4f.a >= 0.5 ? 2.0 : 0.0;
	v_color4f.a -= italicBit * 0.25;
	pos2d.x -= (a_position3f.y - 0.5) * italicBit;
	v_color4f.a *= 2.0;
	v_color4f *= u_color4f;
	gl_Position = u_matrixTransform * vec4(pos2d, 0.0, 1.0);
}
