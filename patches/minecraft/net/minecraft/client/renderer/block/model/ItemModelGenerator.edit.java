
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 9  @  5 : 11

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  9  @  9 : 10

> CHANGE  17 : 19  @  18 : 19

~ 			EaglerTextureAtlasSprite textureatlassprite = textureMapIn
~ 					.getAtlasSprite((new ResourceLocation(s1)).toString());

> CHANGE  14 : 16  @  13 : 14

~ 	private List<BlockPart> func_178394_a(int parInt1, String parString1,
~ 			EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  14 : 16  @  13 : 14

~ 	private List<BlockPart> func_178397_a(EaglerTextureAtlasSprite parTextureAtlasSprite, String parString1,
~ 			int parInt1) {

> CHANGE  104 : 105  @  103 : 104

~ 	private List<ItemModelGenerator.Span> func_178393_a(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
