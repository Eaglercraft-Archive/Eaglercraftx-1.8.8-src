
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.VertexMarkerState;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

> DELETE  1  @  1 : 6

> DELETE  4  @  4 : 7

> CHANGE  5 : 11  @  5 : 8

~ 	private int stride = 7;
~ 
~ 	public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face,
~ 			EaglerTextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn,
~ 			BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
~ 		stride = 7;

> CHANGE  1 : 4  @  1 : 3

~ 				modelRotationIn, partRotation, uvLocked, shade, null);
~ 		Vector3f calcNormal = getNormalFromVertexData(aint);
~ 		EnumFacing enumfacing = getFacingFromVertexData(calcNormal);

> CHANGE  8 : 21  @  8 : 9

~ 		stride = 8;
~ 		int[] aint2 = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo),
~ 				modelRotationIn, partRotation, uvLocked, shade, calcNormal);
~ 		if (uvLocked) {
~ 			this.func_178409_a(aint2, enumfacing, face.blockFaceUV, sprite);
~ 		}
~ 
~ 		if (partRotation == null) {
~ 			this.func_178408_a(aint2, enumfacing);
~ 		}
~ 		stride = 7;
~ 
~ 		return new BakedQuad(aint, aint2, face.tintIndex, enumfacing);

> CHANGE  2 : 3  @  2 : 3

~ 	private int[] makeQuadVertexData(BlockPartFace partFace, EaglerTextureAtlasSprite sprite, EnumFacing facing,

> CHANGE  1 : 3  @  1 : 3

~ 			boolean parFlag2, Vector3f calcNormal) {
~ 		int[] aint = new int[stride * 4];

> CHANGE  3 : 4  @  3 : 4

~ 					parFlag2, calcNormal);

> CHANGE  40 : 42  @  40 : 42

~ 			float[] sprite, EaglerTextureAtlasSprite modelRotationIn, ModelRotation partRotation,
~ 			BlockPartRotation uvLocked, boolean shade, boolean parFlag2, Vector3f calcNormal) {

> CHANGE  1 : 2  @  1 : 2

~ 		int i = (parFlag2 && stride != 8) ? this.getFaceShadeColor(enumfacing) : -1;

> CHANGE  7 : 9  @  7 : 8

~ 		this.storeVertexData(faceData, j, vertexIndex, vector3f, i, modelRotationIn, partFace.blockFaceUV, enumfacing,
~ 				calcNormal);

> CHANGE  3 : 5  @  3 : 8

~ 			EaglerTextureAtlasSprite sprite, BlockFaceUV faceUV, EnumFacing facing, Vector3f calcNormal) {
~ 		int i = storeIndex * stride;

> INSERT  4 : 27  @  4

+ 		if (stride == 8) {
+ 			faceData[i] = Float.floatToRawIntBits(position.x * VertexMarkerState.localCoordDeriveHackX);
+ 			faceData[i + 1] = Float.floatToRawIntBits(position.y * VertexMarkerState.localCoordDeriveHackY);
+ 			faceData[i + 2] = Float.floatToRawIntBits(position.z * VertexMarkerState.localCoordDeriveHackZ);
+ 			if (calcNormal != null) {
+ 				int x = (byte) ((int) (calcNormal.x * 127.0F)) & 255;
+ 				int y = (byte) ((int) (calcNormal.y * 127.0F)) & 255;
+ 				int z = (byte) ((int) (calcNormal.z * 127.0F)) & 255;
+ 				int l = x | y << 8 | z << 16 | ((byte) VertexMarkerState.markId) << 24;
+ 				faceData[i + 6] = l;
+ 			} else {
+ 				Vec3i vec = facing.getDirectionVec();
+ 				int x = (byte) ((int) (vec.x * 127.0F)) & 255;
+ 				int y = (byte) ((int) (vec.y * 127.0F)) & 255;
+ 				int z = (byte) ((int) (vec.z * 127.0F)) & 255;
+ 				int l = x | y << 8 | z << 16 | ((byte) VertexMarkerState.markId) << 24;
+ 				faceData[i + 6] = l;
+ 			}
+ 		} else {
+ 			faceData[i] = Float.floatToRawIntBits(position.x);
+ 			faceData[i + 1] = Float.floatToRawIntBits(position.y);
+ 			faceData[i + 2] = Float.floatToRawIntBits(position.z);
+ 		}

> CHANGE  66 : 67  @  66 : 67

~ 	public static Vector3f getNormalFromVertexData(int[] faceData) {

> INSERT  17 : 21  @  17

+ 		return vector3f5;
+ 	}
+ 
+ 	public static EnumFacing getFacingFromVertexData(Vector3f normal) {

> CHANGE  6 : 7  @  6 : 7

~ 			float f2 = Vector3f.dot(normal, vector3f6);

> CHANGE  14 : 15  @  14 : 15

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  18 : 19  @  18 : 19

~ 			int j = stride * i;

> CHANGE  31 : 32  @  31 : 32

~ 			int j1 = stride * i1;

> CHANGE  10 : 11  @  10 : 11

~ 				int l = stride * k;

> CHANGE  14 : 16  @  14 : 16

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {
~ 		int i = stride * facing;

> CHANGE  43 : 44  @  43 : 44

~ 		int j = parBlockFaceUV.func_178345_c(facing) * stride;

> EOF
