
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 4

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ 
+ 	private static final Logger excLogger = LogManager.getLogger("GuiSlotRenderer");
+ 

> CHANGE  381 : 388  @  381 : 382

~ 			try {
~ 				this.drawSlot(j, mouseXIn, k, l, parInt3, parInt4);
~ 			} catch (Throwable t) {
~ 				excLogger.error(
~ 						"Exception caught rendering a slot of a list on the screen! Game will continue running due to the suspicion that this could be an intentional crash attempt, and therefore it would be inconvenient if the user were to be locked out of this gui due to repeatedly triggering a full crash report");
~ 				excLogger.error(t);
~ 			}

> EOF
