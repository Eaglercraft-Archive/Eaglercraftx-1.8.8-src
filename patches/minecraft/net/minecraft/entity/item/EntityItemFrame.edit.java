
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.minecraft.client.Minecraft;

> DELETE  39  @  39 : 44

> DELETE  126  @  126 : 139

> INSERT  6 : 15  @  6

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
