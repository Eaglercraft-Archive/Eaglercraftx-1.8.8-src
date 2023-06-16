
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  4  @  4 : 6

> CHANGE  7 : 9  @  7 : 9

~ 	private EaglerTextureAtlasSprite[] atlasSpritesLava = new EaglerTextureAtlasSprite[2];
~ 	private EaglerTextureAtlasSprite[] atlasSpritesWater = new EaglerTextureAtlasSprite[2];

> CHANGE  17 : 19  @  17 : 18

~ 		EaglerTextureAtlasSprite[] atextureatlassprite = blockliquid.getMaterial() == Material.lava
~ 				? this.atlasSpritesLava

> CHANGE  31 : 32  @  31 : 32

~ 				EaglerTextureAtlasSprite textureatlassprite = atextureatlassprite[0];

> CHANGE  105 : 106  @  105 : 106

~ 				EaglerTextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

> EOF
