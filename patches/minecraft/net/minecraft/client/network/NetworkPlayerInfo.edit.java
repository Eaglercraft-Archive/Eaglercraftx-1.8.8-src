
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> DELETE  5  @  5 : 6

> DELETE  10  @  11 : 14

> CHANGE  40 : 41  @  43 : 44

~ 		return true;

> CHANGE  4 : 6  @  4 : 5

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile)
~ 				.getSkinModel().profileSkinType;

> CHANGE  5 : 6  @  4 : 10

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile).getResourceLocation();

> CHANGE  4 : 5  @  9 : 14

~ 		return null;

> DELETE  7  @  11 : 38

> EOF
