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

in vec2 v_texCoord2f;
in vec4 v_color4f;
in vec2 v_lightmap2f;

layout(location = 0) out vec4 gbufferColor4f;
layout(location = 1) out vec4 gbufferNormal4f;
layout(location = 2) out vec4 gbufferMaterial4f;

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_samplerNormalMaterial;

uniform vec2 u_textureYScale2f;

void main() {
	vec4 diffuseRGBA = texture(u_diffuseTexture, v_texCoord2f) * v_color4f;
	if(diffuseRGBA.a < 0.004) {
		discard;
	}
	gbufferColor4f = vec4(diffuseRGBA.rgb, v_lightmap2f.r);
	gbufferNormal4f = vec4(0.5, 0.5, 1.0, v_lightmap2f.g);
	gbufferMaterial4f = vec4(texture(u_samplerNormalMaterial, vec2(v_texCoord2f.x, v_texCoord2f.y * u_textureYScale2f.x + u_textureYScale2f.y)).rgb, 1.0);
}
