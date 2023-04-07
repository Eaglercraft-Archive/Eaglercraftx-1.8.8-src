
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

#ifdef LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING
#ifndef _HAS_PBR_IMAGE_BASED_LIGHTING_FUNCTION
#define _HAS_PBR_IMAGE_BASED_LIGHTING_FUNCTION

vec3 eaglercraftIBL_NoBlur(in vec3 albedo, in vec3 irradiance, in vec3 envMapSample, in vec3 viewDir, in vec3 normalVec, in vec3 materials) {
	if(materials.g < 0.25) {
		return albedo * irradiance * 0.1;
	}else {
		float roughness = 1.0 - materials.r * 0.85;
		float NdotV = dot(normalVec, -viewDir);
		float fresnel = pow(max(1.0 - NdotV, 0.0), 5.0);
		vec3 kD = vec3(0.05);
		vec3 F;
		if(materials.g < 0.9) {
			F = vec3(materials.g + (max(1.0 - roughness, materials.g) - materials.g) * fresnel);
			kD = (1.0 - F) * albedo / 3.141592;
		}else if(materials.g < 0.964) {
			vec2 lutUV = vec2(0.25, (materials.g - 0.9) * 15.625);
			vec3 mN = textureLod(u_metalsLUT, lutUV, 0.0).rgb;
			lutUV.x += 0.5;
			vec3 mK = textureLod(u_metalsLUT, lutUV, 0.0).rgb;
			fresnel = 1.0 - fresnel;
			mK *= mK;
			mK += mN * mN;
			vec3 nv = mN * fresnel * 2.0;
			fresnel *= fresnel;
			vec3 num = mK - nv + fresnel;
			vec3 den = mK + nv + fresnel;
			vec3 r = num / den;
			mK *= fresnel;
			mK += 1.0;
			num = mK - nv;
			den = mK + nv;
			r += num / den;
			r = clamp(r * 0.5, vec3(0.0), vec3(1.0));
			F = r * r;
		}else {
			F = (1.0 - albedo) + albedo * fresnel;
			kD = vec3(0.05);
		}
		vec2 brdf2f = vec2(max(NdotV, 0.0), roughness);
		brdf2f = 1.0 - brdf2f;
		brdf2f *= brdf2f;
		brdf2f = 1.0 - brdf2f;
		brdf2f = textureLod(u_brdfLUT, brdf2f, 0.0).rg;
		return kD * albedo * irradiance + envMapSample * (F * brdf2f.r + brdf2f.g);
	}
}

#endif
#endif

#ifdef LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_SPECULAR
#ifndef _HAS_PBR_IMAGE_BASED_LIGHTING_SPECULAR_FUNCTION
#define _HAS_PBR_IMAGE_BASED_LIGHTING_SPECULAR_FUNCTION

#ifdef LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_PREFETCH
vec3 eaglercraftIBL_Specular(in vec3 albedo, in vec3 envMapSample, in vec3 viewDir, in vec3 normalVec, in vec3 materials, vec3 metalN, vec3 metalK) {
#else
vec3 eaglercraftIBL_Specular(in vec3 albedo, in vec3 envMapSample, in vec3 viewDir, in vec3 normalVec, in vec3 materials) {
#endif
	float NdotV = dot(normalVec, -viewDir);
	float roughness = 1.0 - materials.r * 0.85;
	float fresnel = pow(max(1.0 - NdotV, 0.0), 5.0);
	vec3 F;
	if(materials.g < 0.9) {
		F = vec3(materials.g + (max(1.0 - roughness, materials.g) - materials.g) * fresnel);
	}else if(materials.g < 0.964) {
#ifdef LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_PREFETCH
		vec3 mN = metalN;
		vec3 mK = metalK;
#else
		vec2 lutUV = vec2(0.25, (materials.g - 0.9) * 15.625);
		vec3 mN = textureLod(u_metalsLUT, lutUV, 0.0).rgb;
		lutUV.x += 0.5;
		vec3 mK = textureLod(u_metalsLUT, lutUV, 0.0).rgb;
#endif
		fresnel = 1.0 - fresnel;
		mK *= mK;
		mK += mN * mN;
		vec3 nv = mN * fresnel * 2.0;
		fresnel *= fresnel;
		vec3 num = mK - nv + fresnel;
		vec3 den = mK + nv + fresnel;
		vec3 r = num / den;
		mK *= fresnel;
		mK += 1.0;
		num = mK - nv;
		den = mK + nv;
		r += num / den;
		r = clamp(r * 0.5, vec3(0.0), vec3(1.0));
		F = r * r;
	}else {
		F = (1.0 - albedo) + albedo * fresnel;
	}
	vec2 brdf2f = vec2(max(NdotV, 0.0), roughness);
	brdf2f = 1.0 - brdf2f;
	brdf2f *= brdf2f;
	brdf2f = 1.0 - brdf2f;
	brdf2f = textureLod(u_brdfLUT, brdf2f, 0.0).rg;
	return envMapSample * (F * brdf2f.r + brdf2f.g);
}

#endif
#endif