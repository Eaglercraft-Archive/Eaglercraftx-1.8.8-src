
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 4

~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;
~ 
~ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  2  @  2 : 4

> CHANGE  6 : 7  @  6 : 7

~ 	private static final IntObjectMap<ModelRotation> mapRotations = new IntObjectHashMap<>();

> CHANGE  63 : 65  @  63 : 65

~ 		return mapRotations
~ 				.get(combineXY(MathHelper.normalizeAngle(parInt1, 360), MathHelper.normalizeAngle(parInt2, 360)));

> CHANGE  3 : 6  @  3 : 5

~ 		ModelRotation[] lst = values();
~ 		for (int i = 0; i < lst.length; ++i) {
~ 			mapRotations.put(lst[i].combinedXY, lst[i]);

> EOF
