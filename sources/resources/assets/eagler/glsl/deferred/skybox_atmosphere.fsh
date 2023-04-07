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
precision mediump sampler2D;

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_skyNormals;
uniform vec4 u_sunDirectionIntensity4f;
uniform vec3 u_scatteringCoefficient3f;
uniform float u_altitude1f;
uniform vec4 u_blendColor4f;

// Source: https://github.com/wwwtyro/glsl-atmosphere

#define PI 3.141592
#define iSteps 16
#define jSteps 4

vec2 rsi(vec3 r0, vec3 rd, float sr) {
	float a = dot(rd, rd);
	float b = 2.0 * dot(rd, r0);
	float c = dot(r0, r0) - (sr * sr);
	float d = (b*b) - 4.0*a*c;
	if (d < 0.0) return vec2(1e5,-1e5);
	a *= 2.0;
	d = sqrt(d);
	return vec2(-b - d, -b + d) / a;
}

vec3 atmosphere(vec3 r, vec3 r0, vec3 pSun, float iSun, float rPlanet, float rAtmos, vec3 kRlh, float kMie, float shRlh, float shMie, float g) {
	pSun = normalize(pSun);
	r = normalize(r);
	
	vec2 p = rsi(r0, r, rAtmos);
	if (p.x > p.y) return vec3(0.0);
	p.y = min(p.y, rsi(r0, r, rPlanet).x);
	float iStepSize = (p.y - p.x) / float(iSteps);
	
	float iTime = 0.0;
	
	vec3 totalRlh = vec3(0.0);
	vec3 totalMie = vec3(0.0);
	
	float iOdRlh = 0.0;
	float iOdMie = 0.0;
	
	float mu = dot(r, pSun);
	float mumu = mu * mu;
	float gg = g * g;
	float pRlh = 3.0 / (16.0 * PI) * (1.0 + mumu);
	float pMie = 3.0 / (8.0 * PI) * ((1.0 - gg) * (mumu + 1.0)) / (pow(1.0 + gg - 2.0 * mu * g, 1.5) * (2.0 + gg));
	
	for (int i = 0; i < iSteps; i++) {
		vec3 iPos = r0 + r * (iTime + iStepSize * 0.5);
		float iHeight = length(iPos) - rPlanet;
		float odStepRlh = exp(-iHeight / shRlh) * iStepSize;
		float odStepMie = exp(-iHeight / shMie) * iStepSize;
		iOdRlh += odStepRlh;
		iOdMie += odStepMie;
		float jStepSize = rsi(iPos, pSun, rAtmos).y / float(jSteps);
		float jTime = 0.0;
		float jOdRlh = 0.0;
		float jOdMie = 0.0;
		for (int j = 0; j < jSteps; j++) {
			vec3 jPos = iPos + pSun * (jTime + jStepSize * 0.5);
			float jHeight = length(jPos) - rPlanet;
			jOdRlh += exp(-jHeight / shRlh) * jStepSize;
			jOdMie += exp(-jHeight / shMie) * jStepSize;
			jTime += jStepSize;
		}
		vec3 attn = exp(-(kMie * (iOdMie + jOdMie) + kRlh * (iOdRlh + jOdRlh)));
		totalRlh += odStepRlh * attn;
		totalMie += odStepMie * attn;
		iTime += iStepSize;

	}
	return iSun * (pRlh * kRlh * totalRlh + pMie * kMie * totalMie);
}

void main() {
	if(u_blendColor4f.a >= 1.0) {
		output4f = vec4(u_blendColor4f.rgb, 0.0);
		return;
	}
	vec4 normalIn = textureLod(u_skyNormals, v_position2f, 0.0);
	if(normalIn.a != 1.0) {
		output4f = vec4(0.0);
		return;
	}
	normalIn.xyz *= 2.0;
	normalIn.xyz -= 1.0;
	output4f = vec4(atmosphere(
		normalIn.xyz,					// normalized ray direction
		vec3(0,6373e3 + u_altitude1f * 50.0,0),	// ray origin
		u_sunDirectionIntensity4f.xyz,	// position of the sun
		u_sunDirectionIntensity4f.w,	// intensity of the sun
		6371e3,							// radius of the planet in meters
		6471e3,							// radius of the atmosphere in meters
		vec3(5.5e-6, 13.0e-6, 22.4e-6),	// Rayleigh scattering coefficient
		21e-6,							// Mie scattering coefficient
		8e3,							// Rayleigh scale height
		1.2e3,							// Mie scale height
		0.758							// Mie preferred scattering direction
	), 0.0);
	if(u_blendColor4f.a > 0.0) {
		output4f.rgb = mix(output4f.rgb, u_blendColor4f.rgb, u_blendColor4f.a);
	}
}
