
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

#ifdef LIB_INCLUDE_WAVING_BLOCKS_FUNCTION
#ifndef _HAS_INCLUDED_WAVING_BLOCKS
#define _HAS_INCLUDED_WAVING_BLOCKS

#define _WAVING_BLOCK_TYPE_LEAF_BLOCK 1
#define _WAVING_BLOCK_TYPE_LEAF_BLOCK_F 1.0
#define _WAVING_BLOCK_TYPE_TALL_GRASS 2
#define _WAVING_BLOCK_TYPE_TALL_GRASS_F 2.0
#define _WAVING_BLOCK_TYPE_CROPS 3
#define _WAVING_BLOCK_TYPE_CROPS_F 3.0
#define _WAVING_BLOCK_TYPE_DOUBLE_PLANT_BOTTOM 4
#define _WAVING_BLOCK_TYPE_DOUBLE_PLANT_BOTTOM_F 4.0
#define _WAVING_BLOCK_TYPE_DOUBLE_PLANT_TOP 5
#define _WAVING_BLOCK_TYPE_DOUBLE_PLANT_TOP_F 5.0
#define _WAVING_BLOCK_TYPE_PLANT 6
#define _WAVING_BLOCK_TYPE_PLANT_F 6.0
#define _WAVING_BLOCK_TYPE_SUGARCANE 7
#define _WAVING_BLOCK_TYPE_SUGARCANE_F 7.0
#define _WAVING_BLOCK_TYPE_VINES 8
#define _WAVING_BLOCK_TYPE_VINES_F 8.0
#define _WAVING_BLOCK_TYPE_WATER_STILL 9
#define _WAVING_BLOCK_TYPE_WATER_STILL_F 9.0
#define _WAVING_BLOCK_TYPE_WATER_FLOW 10
#define _WAVING_BLOCK_TYPE_WATER_FLOW_F 10.0
#define _WAVING_BLOCK_TYPE_LILYPAD 11
#define _WAVING_BLOCK_TYPE_LILYPAD_F 11.0
#define _WAVING_BLOCK_TYPE_FIRE_FLOOR 12
#define _WAVING_BLOCK_TYPE_FIRE_FLOOR_F 12.0
#define _WAVING_BLOCK_TYPE_FIRE_WALL 13
#define _WAVING_BLOCK_TYPE_FIRE_WALL_F 13.0

// ignore wall fire for now, they clip
#define _WAVING_BLOCK_MIN _WAVING_BLOCK_TYPE_LEAF_BLOCK_F
#define _WAVING_BLOCK_MAX _WAVING_BLOCK_TYPE_FIRE_FLOOR_F

#ifndef FAKE_SIN
#error the FAKE_SIN function must be defined to use waving blocks
#endif

#define _WAVING_BLOCK_COORD_DERIVE_HACK 0.001
#define _WAVING_BLOCK_COORD_DERIVE_HACK_05 0.0005
#define _WAVING_BLOCK_COORD_DERIVE_HACK_INV 1000.0

vec3 _computeWavingBlockNoise(in vec3 pos, in vec3 amp1, in float timer) {
	float fac, fac2;
	fac = dot(vec4(pos, timer), vec4(0.5, 0.5, 0.5, 0.0027));
	FAKE_SIN(fac, fac2)
	fac2 *= 0.04;
	fac2 += 0.04;
	vec3 vf0, d0;
	vf0 = timer * vec3(0.0127, 0.0089, 0.0114);
	FAKE_SIN(vf0, d0);
	d0.xyz += d0.yzx;
	d0.xyz += timer * vec3(0.0063, 0.0224, 0.0015);
	d0.y += pos.z;
	d0.xz += pos.y;
	d0.xz += pos.zx;
	d0.xz -= pos.xz;
	vec3 ret;
	FAKE_SIN(d0, ret)
	ret *= fac2;
	return ret * amp1;
}

vec3 _computeWavingBlockNoise(in vec3 pos, in vec3 vf_a, in vec3 vf_b, in vec3 amp1, in vec3 amp2, in float timer) {
	float fac, fac2;
	fac = dot(vec4(pos, timer), vec4(0.5, 0.5, 0.5, 0.0027));
	FAKE_SIN(fac, fac2)
	fac2 *= 0.04;
	fac2 += 0.04;
	vec3 vf0, d0;
	vf0 = timer * vec3(0.0127, 0.0089, 0.0114);
	FAKE_SIN(vf0, d0);
	d0.xyz += d0.yzx;
	d0.xyz += timer * vec3(0.0063, 0.0224, 0.0015);
	d0.y += pos.z;
	d0.xz += pos.y;
	d0.xz += pos.zx;
	d0.xz -= pos.xz;
	vec3 ret;
	FAKE_SIN(d0, ret)
	ret *= fac2;
	vec3 move = ret * amp1;
	vec3 pos2 = move + pos;
	fac = dot(vec4(pos2, timer), vec4(0.5, 0.5, 0.5, 0.0027));
	FAKE_SIN(fac, fac2)
	fac2 *= 0.04;
	fac2 += 0.04;
	vf0 = timer * vf_a;
	FAKE_SIN(vf0, d0);
	d0.xyz += d0.yzx;
	d0.xyz += timer * vf_b;
	d0.y += pos2.z;
	d0.xz += pos2.y;
	d0.xz += pos2.zx;
	d0.xz -= pos2.xz;
	FAKE_SIN(d0, ret)
	ret *= fac2;
	move += ret * amp2;
	return move;
}

vec3 _computeWavingBlockById(in vec3 realPos, in vec3 referencePos, in vec4 wavingBlockParam, in float type) {
	int typeInt = int(type);
	highp float refY, fractY1, fractY2;
	refY = referencePos.y;
	switch(typeInt) {
	case _WAVING_BLOCK_TYPE_CROPS:
		refY += 0.0625625;
	case _WAVING_BLOCK_TYPE_TALL_GRASS:
	case _WAVING_BLOCK_TYPE_PLANT:
	case _WAVING_BLOCK_TYPE_DOUBLE_PLANT_BOTTOM:
	case _WAVING_BLOCK_TYPE_FIRE_FLOOR:
		// check if it is the bottom half of the block,
		// if vertex is at Y = 0.0 then don't offset
		fractY1 = fract(refY + _WAVING_BLOCK_COORD_DERIVE_HACK_05);
		fractY2 = fract(refY - _WAVING_BLOCK_COORD_DERIVE_HACK_05);
		if(fractY2 > fractY1) {
			return vec3(0.0);
		}
	default:
		break;
	}
	vec3 ret = vec3(0.0);
	switch(typeInt) {
	case _WAVING_BLOCK_TYPE_LEAF_BLOCK:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(0.0040, 0.0064, 0.0043),
			vec3(0.0035, 0.0037, 0.0041),
			vec3(1.0, 0.2, 1.0),
			vec3(0.5, 0.1, 0.5),
			wavingBlockParam.y);
		break;
	case _WAVING_BLOCK_TYPE_TALL_GRASS:
	case _WAVING_BLOCK_TYPE_CROPS:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(1.0, 0.2, 1.0),
			wavingBlockParam.y);
		break;
	case _WAVING_BLOCK_TYPE_PLANT:
	case _WAVING_BLOCK_TYPE_DOUBLE_PLANT_BOTTOM:
	case _WAVING_BLOCK_TYPE_DOUBLE_PLANT_TOP:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(0.0041, 0.007, 0.0044),
			vec3(0.0038, 0.024, 0.0),
			vec3(0.8, 0.0, 0.8),
			vec3(0.4, 0.0, 0.4),
			wavingBlockParam.y);
		break;
	case _WAVING_BLOCK_TYPE_SUGARCANE:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(0.3, 0.0, 0.3),
			wavingBlockParam.y);
		break;
	case _WAVING_BLOCK_TYPE_VINES:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(0.0040, 0.0064, 0.0043),
			vec3(0.0035, 0.0037, 0.0041),
			vec3(0.5, 0.3, 0.5),
			vec3(0.25, 0.2, 0.25),
			wavingBlockParam.y);
		break;
	case _WAVING_BLOCK_TYPE_WATER_STILL:
	
		break;
	case _WAVING_BLOCK_TYPE_WATER_FLOW:
	
		break;
	case _WAVING_BLOCK_TYPE_FIRE_FLOOR:
		ret = _computeWavingBlockNoise(
			referencePos,
			vec3(0.0105, 0.0096, 0.0087),
			vec3(0.0063, 0.0097, 0.0156),
			vec3(1.2, 0.4, 1.2),
			vec3(0.8, 0.8, 0.8),
			wavingBlockParam.y);
		break;
	default:
		break;
	}
	return ret;
}

#define COMPUTE_WAVING_BLOCKS(pos4f, amount, range, block1f, modelMatrix, viewMatrix, modelViewMatrix, wavingBlockOffset, wavingBlockParam)\
	if(block1f >= _WAVING_BLOCK_MIN && block1f <= _WAVING_BLOCK_MAX) {\
		pos4f = modelMatrix * pos4f;\
		pos4f.xyz /= pos4f.w;\
		pos4f.w = 1.0;\
		if(dot(pos4f.xyz, pos4f.xyz) < range * range) {\
			pos4f.xyz += _computeWavingBlockById(pos4f.xyz, pos4f.xyz + wavingBlockOffset, wavingBlockParam, block1f) * amount;\
		}\
		pos4f = viewMatrix * pos4f;\
	}else {\
		pos4f = modelViewMatrix * pos4f;\
	}

#endif
#endif
