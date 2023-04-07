
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 6

> INSERT  1 : 4  @  1

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ 

> CHANGE  1 : 2  @  1 : 2

~ 	private final EaglerTextureAtlasSprite texture;

> CHANGE  1 : 5  @  1 : 4

~ 	public BreakingFour(BakedQuad parBakedQuad, EaglerTextureAtlasSprite textureIn) {
~ 		super(Arrays.copyOf(parBakedQuad.getVertexData(), parBakedQuad.getVertexData().length),
~ 				Arrays.copyOf(parBakedQuad.getVertexDataWithNormals(), parBakedQuad.getVertexDataWithNormals().length),
~ 				parBakedQuad.tintIndex, parBakedQuad.face);

> INSERT  46 : 52  @  46

+ 		if (this.vertexDataWithNormals != null) {
+ 			int i2 = 8 * parInt1;
+ 			this.vertexDataWithNormals[i2 + 4] = this.vertexData[i + 4];
+ 			this.vertexDataWithNormals[i2 + 4 + 1] = this.vertexData[i + 4 + 1];
+ 
+ 		}

> EOF
