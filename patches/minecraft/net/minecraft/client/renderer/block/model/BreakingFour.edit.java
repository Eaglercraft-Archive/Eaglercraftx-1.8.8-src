
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 6

> INSERT  1 : 3  @  1

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 

> DELETE  1  @  1 : 2

> CHANGE  1 : 5  @  1 : 5

~ 	public BreakingFour(BakedQuad parBakedQuad, EaglerTextureAtlasSprite textureIn) {
~ 		super(Arrays.copyOf(parBakedQuad.getVertexData(), parBakedQuad.getVertexData().length),
~ 				Arrays.copyOf(parBakedQuad.getVertexDataWithNormals(), parBakedQuad.getVertexDataWithNormals().length),
~ 				parBakedQuad.tintIndex, parBakedQuad.face, textureIn);

> CHANGE  43 : 51  @  43 : 45

~ 		this.vertexData[i + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double) f3));
~ 		this.vertexData[i + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV((double) f4));
~ 		if (this.vertexDataWithNormals != null) {
~ 			int i2 = 8 * parInt1;
~ 			this.vertexDataWithNormals[i2 + 4] = this.vertexData[i + 4];
~ 			this.vertexDataWithNormals[i2 + 4 + 1] = this.vertexData[i + 4 + 1];
~ 
~ 		}

> EOF
