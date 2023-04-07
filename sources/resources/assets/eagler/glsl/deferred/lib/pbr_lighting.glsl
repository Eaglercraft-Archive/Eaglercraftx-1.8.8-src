
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

#ifdef LIB_INCLUDE_PBR_LIGHTING_FUNCTION
#ifndef _HAS_PBR_LIGHTING_FUNCTION
#define _HAS_PBR_LIGHTING_FUNCTION

#ifdef LIB_INCLUDE_PBR_LIGHTING_PREFETCH
#define PREFETCH_METALS(albedo, materialG1f, metalN3f, metalK3f)\
	if(materialG1f >= 0.9 && materialG1f < 0.964) {\
		metalK3f.xy = vec2(0.25, (materialG1f - 0.9) * 15.625);\
		metalN3f = textureLod(u_metalsLUT, metalK3f.xy, 0.0).rgb;\
		metalK3f.x += 0.5;\
		metalK3f = textureLod(u_metalsLUT, metalK3f.xy, 0.0).rgb;\
	}
#endif

#ifdef LIB_INCLUDE_PBR_LIGHTING_PREFETCH
vec3 eaglercraftLighting(in vec3 albedo, in vec3 radiance, in vec3 viewDir, in vec3 lightDir, in vec3 normalVec, in vec3 materials, in vec3 metalN, in vec3 metalK) {
#else
vec3 eaglercraftLighting(in vec3 albedo, in vec3 radiance, in vec3 viewDir, in vec3 lightDir, in vec3 normalVec, in vec3 materials) {
#endif
	float roughness = 1.0 - materials.r * 0.85;
	vec3 H = normalize(viewDir + lightDir);
	vec3 NdotHVL = max(normalVec * mat3(H, viewDir, lightDir), vec3(0.0));
	float NDF = roughness * roughness;
	NDF *= NDF;
	float denom = NdotHVL.x * NdotHVL.x * (NDF - 1.0) + 1.0;
	NDF /= denom * denom * 3.141592;
	float gs = roughness + 1.0;
	gs *= gs * 0.125;
	vec2 Ndot = NdotHVL.yz;
	Ndot /= Ndot * (1.0 - gs) + gs;
	NDF *= Ndot.x * Ndot.y;
	float fresnel = pow(max(1.0 - NdotHVL.x, 0.0), 5.0);
	vec3 kD = vec3(0.03);
	vec3 F;
	if(materials.g < 0.9) {
		F = vec3(materials.g + (1.0 - materials.g) * fresnel);
		kD = (1.0 - F) * albedo / 3.141592;
	}else if(materials.g < 0.964) {
#ifdef LIB_INCLUDE_PBR_LIGHTING_PREFETCH
		vec3 mN = metalN;
		vec3 mK = metalK;
#else
		vec2 lutUV = vec2(0.25, (materials.g - 0.9) * 15.625);
		vec3 mN = textureLod(u_metalsLUT, lutUV, 0.0).rgb;
		lutUV.x += 0.5;
		vec3 mK = textureLod(u_metalsLUT, lutUV, 0.0).rgb * length(albedo);
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
		F = albedo + (1.0 - albedo) * fresnel;
	}
	denom = 4.0 * NdotHVL.y * NdotHVL.z + 0.0001;
	return (kD + (NDF * F / denom)) * radiance * NdotHVL.z;
}

#endif
#endif
