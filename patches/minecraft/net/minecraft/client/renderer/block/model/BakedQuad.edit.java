
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  6 : 7  @  6

+ 	protected final int[] vertexDataWithNormals;

> INSERT  5 : 6  @  5

+ 		this.vertexDataWithNormals = null;

> INSERT  4 : 11  @  4

+ 	public BakedQuad(int[] vertexDataIn, int[] vertexDataWithNormalsIn, int tintIndexIn, EnumFacing faceIn) {
+ 		this.vertexData = vertexDataIn;
+ 		this.vertexDataWithNormals = vertexDataWithNormalsIn;
+ 		this.tintIndex = tintIndexIn;
+ 		this.face = faceIn;
+ 	}
+ 

> INSERT  4 : 8  @  4

+ 	public int[] getVertexDataWithNormals() {
+ 		return this.vertexDataWithNormals;
+ 	}
+ 

> EOF
