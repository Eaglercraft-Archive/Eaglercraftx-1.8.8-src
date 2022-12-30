
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  7  @  3 : 4

> DELETE  1  @  2 : 3

> DELETE  1  @  2 : 4

> DELETE  10  @  12 : 13

> CHANGE  61 : 63  @  62 : 64

~ 		EaglerTextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
~ 		EaglerTextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");

> CHANGE  20 : 21  @  20 : 21

~ 			EaglerTextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;

> CHANGE  180 : 181  @  180 : 181

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
