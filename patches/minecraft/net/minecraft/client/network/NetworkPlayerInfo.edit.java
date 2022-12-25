
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> DELETE  8  @  8 : 9

> DELETE  18  @  19 : 22

> CHANGE  58 : 59  @  62 : 63

~ 		return true;

> CHANGE  62 : 64  @  66 : 67

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile)
~ 				.getSkinModel().profileSkinType;

> CHANGE  67 : 68  @  70 : 76

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile).getResourceLocation();

> CHANGE  71 : 72  @  79 : 84

~ 		return null;

> DELETE  78  @  90 : 117

> EOF
