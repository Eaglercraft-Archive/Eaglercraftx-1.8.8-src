
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  9  @  5 : 6

> DELETE  10  @  7 : 8

> DELETE  11  @  9 : 11

> DELETE  21  @  21 : 22

> CHANGE  82 : 84  @  83 : 85

~ 		EaglerTextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
~ 		EaglerTextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");

> CHANGE  102 : 103  @  103 : 104

~ 			EaglerTextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;

> CHANGE  282 : 283  @  283 : 284

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
