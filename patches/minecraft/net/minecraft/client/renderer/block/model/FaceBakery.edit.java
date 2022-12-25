
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  7  @  3 : 8

> DELETE  11  @  12 : 15

> CHANGE  16 : 19  @  20 : 23

~ 	public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face,
~ 			EaglerTextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn,
~ 			BlockPartRotation partRotation, boolean uvLocked, boolean shade) {

> CHANGE  33 : 34  @  37 : 38

~ 	private int[] makeQuadVertexData(BlockPartFace partFace, EaglerTextureAtlasSprite sprite, EnumFacing facing,

> CHANGE  81 : 83  @  85 : 87

~ 			float[] sprite, EaglerTextureAtlasSprite modelRotationIn, ModelRotation partRotation,
~ 			BlockPartRotation uvLocked, boolean shade, boolean parFlag2) {

> CHANGE  96 : 97  @  100 : 101

~ 			EaglerTextureAtlasSprite sprite, BlockFaceUV faceUV) {

> CHANGE  210 : 211  @  214 : 215

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  287 : 288  @  291 : 292

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
