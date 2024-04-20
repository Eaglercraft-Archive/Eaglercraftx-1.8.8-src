
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;

> DELETE  1  @  1 : 3

> DELETE  10  @  10 : 13

> CHANGE  40 : 41  @  40 : 41

~ 		return true;

> CHANGE  3 : 5  @  3 : 4

~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile)
~ 				.getSkinModel().profileSkinType;

> CHANGE  2 : 5  @  2 : 6

~ 	public SkinModel getEaglerSkinModel() {
~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile).getSkinModel();
~ 	}

> CHANGE  1 : 3  @  1 : 3

~ 	public ResourceLocation getLocationSkin() {
~ 		return Minecraft.getMinecraft().getNetHandler().getSkinCache().getSkin(this.gameProfile).getResourceLocation();

> CHANGE  3 : 5  @  3 : 8

~ 		return Minecraft.getMinecraft().getNetHandler().getCapeCache().getCape(this.gameProfile.getId())
~ 				.getResourceLocation();

> DELETE  6  @  6 : 33

> EOF
