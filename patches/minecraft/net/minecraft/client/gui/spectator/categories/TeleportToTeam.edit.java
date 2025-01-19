
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 2

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 2

> CHANGE  41 : 43  @  41 : 43

~ 		for (int i = 0, l = this.field_178672_a.size(); i < l; ++i) {
~ 			if (this.field_178672_a.get(i).func_178662_A_()) {

> INSERT  23 : 27  @  23

+ 			this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
+ 
+ 			// TODO: program team skins
+ 

> CHANGE  1 : 6  @  1 : 5

~ 				// String s1 = ((NetworkPlayerInfo) this.field_178675_d
~ 				// .get((new
~ 				// EaglercraftRandom()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
~ 				// this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
~ 				// AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);

> CHANGE  1 : 2  @  1 : 2

~ 				// this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();

> EOF
