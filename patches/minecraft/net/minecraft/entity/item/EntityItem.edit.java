
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  1 : 2  @  1

+ import net.minecraft.client.Minecraft;

> DELETE  4  @  4 : 5

> DELETE  2  @  2 : 3

> DELETE  5  @  5 : 7

> DELETE  68  @  68 : 72

> DELETE  21  @  21 : 24

> DELETE  136  @  136 : 145

> DELETE  1  @  1 : 36

> DELETE  11  @  11 : 19

> INSERT  66 : 75  @  66

+ 
+ 	public boolean eaglerEmissiveFlag = false;
+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		eaglerEmissiveFlag = Minecraft.getMinecraft().entityRenderer.renderItemEntityLight(this, 0.1f);
+ 	}

> EOF
