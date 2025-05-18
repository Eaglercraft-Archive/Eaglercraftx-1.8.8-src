
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;

> DELETE  1  @  1 : 3

> INSERT  8 : 9  @  8

+ 	private String gameProfileProfanityFilter;

> DELETE  2  @  2 : 5

> INSERT  2 : 3  @  2

+ 	private IChatComponent displayNameProfanityFilter;

> INSERT  15 : 16  @  15

+ 		this.displayNameProfanityFilter = null;

> CHANGE  23 : 24  @  23 : 24

~ 		return true;

> CHANGE  3 : 4  @  3 : 4

~ 		return getEaglerSkinModel().profileSkinType;

> CHANGE  2 : 5  @  2 : 6

~ 	public SkinModel getEaglerSkinModel() {
~ 		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerSkin(this.gameProfile).getModel();
~ 	}

> CHANGE  1 : 3  @  1 : 3

~ 	public ResourceLocation getLocationSkin() {
~ 		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerSkin(this.gameProfile).getLocation();

> CHANGE  3 : 4  @  3 : 8

~ 		return Minecraft.getMinecraft().getNetHandler().getTextureCache().getPlayerCape(this.gameProfile);

> DELETE  6  @  6 : 33

> INSERT  2 : 3  @  2

+ 		this.displayNameProfanityFilter = null;

> INSERT  6 : 34  @  6

+ 	public IChatComponent getDisplayNameProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			if (this.displayName != null) {
+ 				if (this.displayNameProfanityFilter == null) {
+ 					this.displayNameProfanityFilter = ProfanityFilter.getInstance()
+ 							.profanityFilterChatComponent(this.displayName);
+ 				}
+ 				return this.displayNameProfanityFilter;
+ 			} else {
+ 				return null;
+ 			}
+ 		} else {
+ 			return this.displayName;
+ 		}
+ 	}
+ 
+ 	public String getGameProfileNameProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			if (this.gameProfileProfanityFilter == null) {
+ 				this.gameProfileProfanityFilter = ProfanityFilter.getInstance()
+ 						.profanityFilterString(this.gameProfile.getName());
+ 			}
+ 			return this.gameProfileProfanityFilter;
+ 		} else {
+ 			return this.gameProfile.getName();
+ 		}
+ 	}
+ 

> EOF
