
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> INSERT  1 : 2  @  1

+ import net.optifine.model.QuadBounds;

> INSERT  3 : 4  @  3

+ 	protected final int[] vertexDataWithNormals;

> INSERT  2 : 4  @  2

+ 	protected final EaglerTextureAtlasSprite sprite;
+ 	private QuadBounds quadBounds;

> CHANGE  1 : 3  @  1 : 2

~ 	public BakedQuad(int[] vertexDataIn, int[] vertexDataWithNormalsIn, int tintIndexIn, EnumFacing faceIn,
~ 			EaglerTextureAtlasSprite sprite) {

> INSERT  1 : 2  @  1

+ 		this.vertexDataWithNormals = vertexDataWithNormalsIn;

> INSERT  2 : 3  @  2

+ 		this.sprite = sprite;

> INSERT  6 : 10  @  6

+ 	public int[] getVertexDataWithNormals() {
+ 		return this.vertexDataWithNormals;
+ 	}
+ 

> INSERT  11 : 52  @  11

+ 
+ 	public EaglerTextureAtlasSprite getSprite() {
+ 		return sprite;
+ 	}
+ 
+ 	public QuadBounds getQuadBounds() {
+ 		if (this.quadBounds == null) {
+ 			this.quadBounds = new QuadBounds(this.getVertexData());
+ 		}
+ 
+ 		return this.quadBounds;
+ 	}
+ 
+ 	public float getMidX() {
+ 		QuadBounds quadbounds = this.getQuadBounds();
+ 		return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0F;
+ 	}
+ 
+ 	public double getMidY() {
+ 		QuadBounds quadbounds = this.getQuadBounds();
+ 		return (double) ((quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0F);
+ 	}
+ 
+ 	public double getMidZ() {
+ 		QuadBounds quadbounds = this.getQuadBounds();
+ 		return (double) ((quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0F);
+ 	}
+ 
+ 	public boolean isFaceQuad() {
+ 		QuadBounds quadbounds = this.getQuadBounds();
+ 		return quadbounds.isFaceQuad(this.face);
+ 	}
+ 
+ 	public boolean isFullQuad() {
+ 		QuadBounds quadbounds = this.getQuadBounds();
+ 		return quadbounds.isFullQuad(this.face);
+ 	}
+ 
+ 	public boolean isFullFaceQuad() {
+ 		return this.isFullQuad() && this.isFaceQuad();
+ 	}

> EOF
