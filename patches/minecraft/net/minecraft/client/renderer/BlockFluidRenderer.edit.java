
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  8  @  6 : 8

> CHANGE  15 : 17  @  15 : 17

~ 	private EaglerTextureAtlasSprite[] atlasSpritesLava = new EaglerTextureAtlasSprite[2];
~ 	private EaglerTextureAtlasSprite[] atlasSpritesWater = new EaglerTextureAtlasSprite[2];

> CHANGE  34 : 36  @  34 : 35

~ 		EaglerTextureAtlasSprite[] atextureatlassprite = blockliquid.getMaterial() == Material.lava
~ 				? this.atlasSpritesLava

> CHANGE  67 : 68  @  66 : 67

~ 				EaglerTextureAtlasSprite textureatlassprite = atextureatlassprite[0];

> CHANGE  173 : 174  @  172 : 173

~ 				EaglerTextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

> EOF
