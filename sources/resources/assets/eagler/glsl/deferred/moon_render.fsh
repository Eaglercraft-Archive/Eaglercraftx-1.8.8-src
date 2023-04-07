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
in vec3 v_position3f;

layout(location = 0) out vec4 output4f;

uniform vec3 u_moonColor3f;
uniform vec3 u_lightDir3f;

uniform sampler2D u_moonTextures;
uniform sampler2D u_cloudsTexture;

void main() {
	gl_FragDepth = 0.0;
	vec4 color4f = texture(u_moonTextures, v_position2f);
	if(color4f.a < 0.99) {
		discard;
	}
	vec3 moonNormal3f;
	moonNormal3f.xy = color4f.rg * 2.0 - 1.0;
	moonNormal3f.z = sqrt(1.0 - dot(moonNormal3f.xy, moonNormal3f.xy));
	float NdotV = dot(moonNormal3f, u_lightDir3f);
	output4f = vec4(u_moonColor3f * (color4f.b * color4f.b * mix(max(NdotV, 0.0), max(NdotV + 0.45, 0.0) * 0.5f, max(u_lightDir3f.z * u_lightDir3f.z * -u_lightDir3f.z, 0.0))), 0.0);
	vec3 viewDir = normalize(v_position3f);
	if(viewDir.y < 0.01) {
		return;
	}
	vec2 cloudSampleCoord2f = (viewDir.xz / (viewDir.y + 1.0)) * 0.975 * 0.5 + 0.5;
	vec4 cloudSample = textureLod(u_cloudsTexture, cloudSampleCoord2f, 0.0);
	output4f.rgb = mix(output4f.rgb, output4f.rgb * max(cloudSample.a * 1.25 - 0.25, 0.0), smoothstep(0.0, 1.0, min(viewDir.y * 8.0, 1.0)));
}
