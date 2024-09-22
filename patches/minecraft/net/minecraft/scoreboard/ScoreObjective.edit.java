
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
~ import net.minecraft.client.Minecraft;

> INSERT  7 : 8  @  7

+ 	private String displayNameProfanityFilter;

> INSERT  25 : 36  @  25

+ 	public String getDisplayNameProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			if (displayNameProfanityFilter == null) {
+ 				displayNameProfanityFilter = ProfanityFilter.getInstance().profanityFilterString(displayName);
+ 			}
+ 			return displayNameProfanityFilter;
+ 		} else {
+ 			return this.displayName;
+ 		}
+ 	}
+ 

> EOF
