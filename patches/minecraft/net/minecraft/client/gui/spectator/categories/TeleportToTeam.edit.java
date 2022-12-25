
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 8  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  16  @  13 : 14

> DELETE  17  @  15 : 16

> INSERT  83 : 87  @  82

+ 			this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
+ 
+ 			// TODO: program team skins
+ 

> CHANGE  88 : 93  @  83 : 87

~ 				// String s1 = ((NetworkPlayerInfo) this.field_178675_d
~ 				// .get((new
~ 				// EaglercraftRandom()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
~ 				// this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
~ 				// AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);

> CHANGE  94 : 95  @  88 : 89

~ 				// this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();

> EOF
