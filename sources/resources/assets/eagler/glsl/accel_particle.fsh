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
precision mediump float;
precision mediump sampler2D;

in vec2 v_texCoord2f;
in vec4 v_color4f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_inputTexture;

void main() {
	output4f = texture(u_inputTexture, v_texCoord2f) * v_color4f;
	if(output4f.a < 0.004) {
		discard;
	}
}
