
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import java.util.List;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 2

> CHANGE  40 : 41  @  40 : 41

~ 		List<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffectsList();

> INSERT  3 : 4  @  3

+ 			GlStateManager.enableAlpha();

> CHANGE  5 : 6  @  5 : 6

~ 			for (PotionEffect potioneffect : collection) {

> EOF
