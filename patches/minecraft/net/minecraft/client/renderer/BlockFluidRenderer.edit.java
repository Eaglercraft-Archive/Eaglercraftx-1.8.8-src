
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  4 : 6

> CHANGE  7 : 9  @  9 : 11

~ 	private EaglerTextureAtlasSprite[] atlasSpritesLava = new EaglerTextureAtlasSprite[2];
~ 	private EaglerTextureAtlasSprite[] atlasSpritesWater = new EaglerTextureAtlasSprite[2];

> CHANGE  19 : 21  @  19 : 20

~ 		EaglerTextureAtlasSprite[] atextureatlassprite = blockliquid.getMaterial() == Material.lava
~ 				? this.atlasSpritesLava

> CHANGE  33 : 34  @  32 : 33

~ 				EaglerTextureAtlasSprite textureatlassprite = atextureatlassprite[0];

> CHANGE  106 : 107  @  106 : 107

~ 				EaglerTextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

> EOF
