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

layout(location = 0) out vec4 output4f;

uniform sampler2D u_paraboloidSkyboxTexture;

#define SAMPLE_IRRADIANCE(tex, tmat3f, st, sm, accum3f, tmp3f)\
	tmp3f = tmat3f * st;\
	tmp3f.xz /= abs(tmp3f.y) + 1.0;\
	tmp3f.xz *= vec2(0.4875, tmp3f.y >= 0.0 ? 0.24375 : -0.24375);\
	tmp3f.xz += vec2(0.5, tmp3f.y >= 0.0 ? 0.25 : 0.75);\
	accum3f += textureLod(tex, tmp3f.xz, 0.0).rgb * sm * clamp(1.0 + tmp3f.y * 2.0, 0.0, 1.0);

void main() {
	vec2 latLong = v_position2f * vec2(2.0, 4.0);
	latLong -= vec2(1.0, v_position2f.y >= 0.5 ? 3.0 : 1.0);
	float latLongLen2 = dot(latLong, latLong);
	if(latLongLen2 > 1.2) {
		output4f = vec4(0.0);
		return;
	}

	vec2 texCoords = v_position2f;
	texCoords = mod(texCoords, vec2(1.0, 0.5));
	texCoords = texCoords * vec2(2.0, 4.0) - 1.0;
	texCoords *= (v_position2f.y < 0.5) ? vec2(1.0, 1.0) : vec2(1.0, -1.0);
	
	float mag2 = 2.0 / (latLongLen2 + 1.0);
	vec3 dir;
	dir.y = ((v_position2f.y < 0.5) ? 1.0 : -1.0) * (mag2 - 1.0);
	if(dir.y < -0.25) {
		output4f = vec4(0.0);
		return;
	}

	dir.xz = texCoords * mag2;

	vec3 tmp3f = cross(dir, vec3(0.0, 1.0, 0.0));
	mat3 tmat3f = mat3(tmp3f, cross(dir, tmp3f), dir);
	vec3 accum3f = vec3(0.0);

	// note: sampling is divided into 3 draw calls to allow better driver multitasking,
	// shader would otherwise run the full 156 texture samples in only a single thread
	// per pixel which is not ideal. The resulting values of the 3 draw calls are added
	// together using GL_ONE, GL_ONE blending on the destination framebuffer

#ifdef PHASE_1
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.247, 0.000, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.479, 0.000, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.682, 0.000, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.841, 0.000, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.949, 0.000, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.997, 0.000, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.240, 0.061, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.465, 0.119, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.660, 0.169, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.815, 0.208, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.919, 0.235, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.966, 0.247, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.217, 0.119, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.421, 0.230, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.598, 0.327, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.738, 0.403, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.833, 0.455, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.875, 0.478, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.181, 0.169, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.351, 0.327, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.499, 0.465, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.616, 0.574, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.694, 0.647, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.730, 0.680, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.134, 0.208, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.259, 0.403, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.368, 0.574, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.455, 0.708, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.513, 0.799, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.539, 0.839, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.078, 0.235, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.151, 0.455, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.215, 0.647, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.265, 0.799, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.299, 0.901, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.315, 0.947, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.018, 0.247, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.034, 0.478, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.048, 0.680, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.060, 0.839, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.067, 0.947, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.071, 0.995, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.044, 0.243, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.085, 0.472, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.121, 0.671, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.150, 0.828, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.169, 0.934, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.178, 0.982, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.103, 0.225, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.200, 0.436, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.284, 0.620, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.350, 0.765, 0.540), 0.0092, accum3f, tmp3f)
#endif
#ifdef PHASE_2
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.395, 0.863, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.415, 0.907, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.155, 0.192, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.301, 0.373, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.428, 0.530, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.529, 0.655, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.596, 0.738, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.627, 0.776, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.198, 0.148, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.384, 0.287, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.546, 0.408, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.674, 0.504, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.760, 0.568, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.799, 0.597, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.229, 0.094, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.443, 0.183, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.630, 0.260, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.778, 0.321, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.877, 0.362, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.922, 0.381, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.245, 0.035, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.475, 0.068, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.675, 0.096, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.833, 0.119, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.939, 0.134, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.988, 0.141, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.246, -0.027, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.477, -0.052, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.678, -0.074, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.837, -0.091, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.943, -0.103, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.992, -0.108, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.232, -0.087, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.449, -0.168, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.638, -0.239, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.788, -0.295, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.889, -0.333, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.934, -0.350, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.203, -0.141, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.393, -0.274, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.559, -0.390, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.690, -0.481, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.779, -0.542, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.819, -0.570, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.162, -0.187, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.313, -0.363, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.446, -0.516, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.550, -0.637, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.620, -0.718, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.652, -0.755, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.110, -0.221, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.214, -0.429, 0.878), 0.0085, accum3f, tmp3f)
#endif
#ifdef PHASE_3
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.304, -0.610, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.375, -0.753, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.423, -0.849, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.445, -0.893, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.052, -0.242, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.101, -0.469, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.144, -0.666, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.177, -0.823, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.200, -0.928, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(-0.210, -0.975, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.009, -0.247, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.018, -0.479, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.026, -0.681, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.032, -0.841, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.036, -0.948, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.038, -0.997, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.070, -0.237, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.136, -0.460, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.193, -0.654, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.239, -0.807, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.269, -0.910, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.283, -0.957, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.127, -0.213, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.246, -0.412, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.349, -0.585, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.431, -0.723, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.486, -0.815, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.511, -0.857, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.175, -0.175, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.340, -0.338, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.483, -0.481, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.596, -0.594, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.673, -0.670, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.707, -0.704, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.213, -0.126, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.413, -0.244, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.587, -0.346, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.725, -0.428, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.817, -0.482, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.859, -0.507, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.238, -0.069, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.460, -0.134, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.654, -0.190, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.808, -0.235, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.911, -0.265, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.958, -0.279, 0.071), 0.0014, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.247, -0.008, 0.969), 0.0048, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.479, -0.016, 0.878), 0.0085, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.681, -0.023, 0.732), 0.0100, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.841, -0.028, 0.540), 0.0092, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.948, -0.031, 0.315), 0.0060, accum3f, tmp3f)
	SAMPLE_IRRADIANCE(u_paraboloidSkyboxTexture, tmat3f, vec3(0.997, -0.033, 0.071), 0.0014, accum3f, tmp3f)
#endif

	output4f = vec4(accum3f * min(1.0 + dir.y * 4.0, 1.0), 0.0);
}
