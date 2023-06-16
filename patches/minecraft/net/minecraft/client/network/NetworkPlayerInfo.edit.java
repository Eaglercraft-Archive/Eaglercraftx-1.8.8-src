
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> DELETE  2  @  2 : 3

> DELETE  10  @  10 : 13

> CHANGE  40 : 41  @  40 : 41

~ 		return true;

> CHANGE  3 : 5  @  3 : 4

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile)
~ 				.getSkinModel().profileSkinType;

> CHANGE  3 : 4  @  3 : 9

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile).getResourceLocation();

> CHANGE  3 : 4  @  3 : 8

~ 		return null;

> DELETE  6  @  6 : 33

> EOF
