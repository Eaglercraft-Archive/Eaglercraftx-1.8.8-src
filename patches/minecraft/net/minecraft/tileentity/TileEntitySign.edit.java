
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
~ import net.minecraft.client.Minecraft;

> DELETE  10  @  10 : 11

> INSERT  8 : 10  @  8

+ import org.json.JSONException;
+ 

> INSERT  3 : 4  @  3

+ 	protected IChatComponent[] signTextProfanityFilter = null;

> INSERT  16 : 35  @  16

+ 	public IChatComponent[] getSignTextProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			if (signTextProfanityFilter == null) {
+ 				signTextProfanityFilter = new IChatComponent[4];
+ 				ProfanityFilter filter = ProfanityFilter.getInstance();
+ 				for (int i = 0; i < 4; ++i) {
+ 					signTextProfanityFilter[i] = filter.profanityFilterChatComponent(signText[i]);
+ 				}
+ 			}
+ 			return signTextProfanityFilter;
+ 		} else {
+ 			return signText;
+ 		}
+ 	}
+ 
+ 	public void clearProfanityFilterCache() {
+ 		signTextProfanityFilter = null;
+ 	}
+ 

> CHANGE  56 : 57  @  56 : 57

~ 			} catch (JSONException var8) {

> EOF
