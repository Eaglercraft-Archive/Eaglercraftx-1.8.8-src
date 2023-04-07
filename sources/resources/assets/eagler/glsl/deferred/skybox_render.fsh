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

in vec3 v_position3f;
in vec3 v_color3f;

layout(location = 0) out vec4 output4f;

uniform vec3 u_sunDirection3f;
uniform vec3 u_sunColor3f;
uniform vec4 u_lightningColor4f;

#ifdef COMPILE_CLOUDS
uniform sampler2D u_cloudsTexture;
#endif

uniform sampler2D u_sunOcclusion;

#define SKY_BRIGHTNESS 5.0

void main() {
	gl_FragDepth = 0.0;
	vec3 viewDir = normalize(v_position3f);
#ifdef COMPILE_PARABOLOID_SKY
	output4f = vec4(v_color3f * SKY_BRIGHTNESS, 0.0);
#else
	float f = max(dot(viewDir, u_sunDirection3f) - 0.995, 0.0) * 100.0;
	float intensity = min(f * 2.0, 1.0);
	intensity *= intensity * intensity * intensity * textureLod(u_sunOcclusion, vec2(0.5, 0.5), 0.0).r * 2.0;
	output4f = vec4(v_color3f * SKY_BRIGHTNESS + intensity * u_sunColor3f, 0.0);
#endif
#ifdef COMPILE_CLOUDS
	if(viewDir.y < 0.01) {
		output4f.rgb = output4f.rgb * u_lightningColor4f.a + u_lightningColor4f.rgb;
		return;
	}
	vec2 cloudSampleCoord2f = (viewDir.xz / (viewDir.y + 1.0)) * 0.975 * 0.5 + 0.5;
	vec4 cloudSample = textureLod(u_cloudsTexture, cloudSampleCoord2f, 0.0);
	output4f.rgb = mix(output4f.rgb, output4f.rgb * cloudSample.a + cloudSample.rgb, smoothstep(0.0, 1.0, min(viewDir.y * 8.0, 1.0)));
#endif
	output4f.rgb = output4f.rgb * u_lightningColor4f.a + u_lightningColor4f.rgb;

}