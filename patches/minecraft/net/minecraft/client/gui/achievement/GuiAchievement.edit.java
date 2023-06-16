
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  3  @  3 : 4

> INSERT  114 : 146  @  114

+ 	public int getHeight() {
+ 		if (this.theAchievement != null && this.notificationTime != 0L && Minecraft.getMinecraft().thePlayer != null) {
+ 			double d0 = (double) (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;
+ 			if (!this.permanentNotification) {
+ 				if (d0 < 0.0D || d0 > 1.0D) {
+ 					this.notificationTime = 0L;
+ 					return 0;
+ 				}
+ 			} else if (d0 > 0.5D) {
+ 				d0 = 0.5D;
+ 			}
+ 
+ 			double d1 = d0 * 2.0D;
+ 			if (d1 > 1.0D) {
+ 				d1 = 2.0D - d1;
+ 			}
+ 
+ 			d1 = d1 * 4.0D;
+ 			d1 = 1.0D - d1;
+ 			if (d1 < 0.0D) {
+ 				d1 = 0.0D;
+ 			}
+ 
+ 			d1 = d1 * d1;
+ 			d1 = d1 * d1;
+ 
+ 			return 32 - (int) (d1 * 32.0D);
+ 		} else {
+ 			return 0;
+ 		}
+ 	}
+ 

> EOF
