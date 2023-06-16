
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  1  @  1 : 6

> DELETE  4  @  4 : 7

> CHANGE  5 : 8  @  5 : 8

~ 	public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face,
~ 			EaglerTextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn,
~ 			BlockPartRotation partRotation, boolean uvLocked, boolean shade) {

> CHANGE  14 : 15  @  14 : 15

~ 	private int[] makeQuadVertexData(BlockPartFace partFace, EaglerTextureAtlasSprite sprite, EnumFacing facing,

> CHANGE  47 : 49  @  47 : 49

~ 			float[] sprite, EaglerTextureAtlasSprite modelRotationIn, ModelRotation partRotation,
~ 			BlockPartRotation uvLocked, boolean shade, boolean parFlag2) {

> CHANGE  13 : 14  @  13 : 14

~ 			EaglerTextureAtlasSprite sprite, BlockFaceUV faceUV) {

> CHANGE  113 : 114  @  113 : 114

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  76 : 77  @  76 : 77

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
