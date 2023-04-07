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

in vec2 v_position2f;

layout(location = 0) out float exposureOut1f;

uniform sampler2D u_inputTexture;
uniform vec2 u_inputSize2f;

void main() {

	float a = 0.0;
	float b = 0.0;
	for(vec2 v = vec2(0.0, u_inputSize2f * 0.5); v.y < 1.0; v.y += u_inputSize2f.y) {
		for(v.x = u_inputSize2f.x * 0.5; v.x < 1.0; v.x += u_inputSize2f.x) {
			a += textureLod(u_inputTexture, v, 0.0).r * (1.0 - length(v - 0.5));
			b += 1.0;
		}
	}

	exposureOut1f = a / b;
}
