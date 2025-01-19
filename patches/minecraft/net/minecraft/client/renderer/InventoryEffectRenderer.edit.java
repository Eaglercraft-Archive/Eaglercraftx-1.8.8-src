
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import com.carrotsearch.hppc.ObjectContainer;
~ import com.carrotsearch.hppc.cursors.ObjectCursor;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 2

> CHANGE  40 : 41  @  40 : 41

~ 		ObjectContainer<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();

> INSERT  3 : 4  @  3

+ 			GlStateManager.enableAlpha();

> CHANGE  5 : 7  @  5 : 6

~ 			for (ObjectCursor<PotionEffect> potioneffect_ : collection) {
~ 				PotionEffect potioneffect = potioneffect_.value;

> EOF
