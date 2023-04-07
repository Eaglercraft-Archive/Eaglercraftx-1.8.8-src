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

layout(location = 0) out vec4 output4f;

in vec2 v_position2f;

uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_fogDepthTexture;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
uniform sampler2D u_lightShaftsTexture;
#endif

#ifdef COMPILE_FOG_ATMOSPHERE
uniform sampler2D u_environmentMap;
uniform vec3 u_sunColorAdd3f;
#endif

uniform mat4 u_inverseViewProjMatrix4f;

#ifdef COMPILE_FOG_LINEAR
uniform vec2 u_linearFogParam2f;
#else
uniform float u_expFogDensity1f;
#endif

uniform vec4 u_fogColorLight4f;
uniform vec4 u_fogColorDark4f;

void main() {
	vec4 fragPos4f = vec4(v_position2f, textureLod(u_fogDepthTexture, v_position2f, 0.0).r, 1.0);

#ifdef COMPILE_FOG_ATMOSPHERE
	if(fragPos4f.z <= 0.0000001) {
		discard;
	}
#endif

	float solidDepth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;
	if(solidDepth != fragPos4f.z) {
		discard;
	}

	fragPos4f.xyz *= 2.0;
	fragPos4f.xyz -= 1.0;

	fragPos4f = u_inverseViewProjMatrix4f * fragPos4f;
	fragPos4f.xyz /= fragPos4f.w;
	fragPos4f.w = 1.0;

	float l = sqrt(dot(fragPos4f.xyz, fragPos4f.xyz));
#ifdef COMPILE_FOG_LINEAR
	float f = (l - u_linearFogParam2f.x) / (u_linearFogParam2f.y - u_linearFogParam2f.x);
#else
	float f = 1.0 - exp(-u_expFogDensity1f * l);
#endif
	float f2 = textureLod(u_gbufferNormalTexture, v_position2f, 0.0).a;
	vec4 fogColor4f = mix(u_fogColorDark4f, u_fogColorLight4f, f2 * f2);
	f = clamp(f, 0.0, 1.0) * fogColor4f.a;

#ifdef COMPILE_FOG_ATMOSPHERE
	fragPos4f.xyz /= -l;
	fragPos4f.xz /= abs(fragPos4f.y) + 1.0;
	fragPos4f.xz *= 0.75;

	vec3 envMapSample3f;

	fragPos4f.xz *= vec2(-0.5, -0.25);
	fragPos4f.xz += vec2(0.5, 0.25);
	envMapSample3f = textureLod(u_environmentMap, fragPos4f.xz, 0.0).rgb + u_sunColorAdd3f;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
	envMapSample3f *= pow(textureLod(u_lightShaftsTexture, v_position2f, 0.0).r * 0.9 + 0.1, 2.25);
	f = f * 0.9 + 0.1;
#endif

	output4f = vec4(envMapSample3f * fogColor4f.rgb, f);
#else
	output4f = vec4(fogColor4f.rgb, f);
#endif

}
