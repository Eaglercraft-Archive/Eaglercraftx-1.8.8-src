
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
+ import net.minecraft.client.Minecraft;

> INSERT  5 : 6  @  5

+ 	private IChatComponent lineStringProfanityFilter;

> CHANGE  9 : 17  @  9 : 10

~ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
~ 			if (lineStringProfanityFilter == null) {
~ 				lineStringProfanityFilter = ProfanityFilter.getInstance().profanityFilterChatComponent(lineString);
~ 			}
~ 			return lineStringProfanityFilter;
~ 		} else {
~ 			return lineString;
~ 		}

> EOF
