
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  5 : 11  @  7 : 13

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  14  @  16 : 17

> CHANGE  31 : 33  @  34 : 35

~ 			EaglerTextureAtlasSprite textureatlassprite = textureMapIn
~ 					.getAtlasSprite((new ResourceLocation(s1)).toString());

> CHANGE  45 : 47  @  47 : 48

~ 	private List<BlockPart> func_178394_a(int parInt1, String parString1,
~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  59 : 61  @  60 : 61

~ 	private List<BlockPart> func_178397_a(EaglerTextureAtlasSprite parTextureAtlasSprite, String parString1,
~ 			int parInt1) {

> CHANGE  163 : 164  @  163 : 164

~ 	private List<ItemModelGenerator.Span> func_178393_a(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
