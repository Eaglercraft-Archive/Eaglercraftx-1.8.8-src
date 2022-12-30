
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 6  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  13  @  9 : 10

> DELETE  1  @  2 : 3

> INSERT  66 : 70  @  67

+ 			this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
+ 
+ 			// TODO: program team skins
+ 

> CHANGE  5 : 10  @  1 : 5

~ 				// String s1 = ((NetworkPlayerInfo) this.field_178675_d
~ 				// .get((new
~ 				// EaglercraftRandom()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
~ 				// this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
~ 				// AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);

> CHANGE  6 : 7  @  5 : 6

~ 				// this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();

> EOF
