
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  5  @  1 : 6

> DELETE  4  @  9 : 12

> CHANGE  5 : 8  @  8 : 11

~ 	public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face,
~ 			EaglerTextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn,
~ 			BlockPartRotation partRotation, boolean uvLocked, boolean shade) {

> CHANGE  17 : 18  @  17 : 18

~ 	private int[] makeQuadVertexData(BlockPartFace partFace, EaglerTextureAtlasSprite sprite, EnumFacing facing,

> CHANGE  48 : 50  @  48 : 50

~ 			float[] sprite, EaglerTextureAtlasSprite modelRotationIn, ModelRotation partRotation,
~ 			BlockPartRotation uvLocked, boolean shade, boolean parFlag2) {

> CHANGE  15 : 16  @  15 : 16

~ 			EaglerTextureAtlasSprite sprite, BlockFaceUV faceUV) {

> CHANGE  114 : 115  @  114 : 115

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  77 : 78  @  77 : 78

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
