
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.minecraft.client.Minecraft;

> INSERT  5 : 6  @  5

+ import net.minecraft.item.Item;

> INSERT  184 : 206  @  184

+ 
+ 	public boolean eaglerEmissiveFlag = false;
+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		eaglerEmissiveFlag = Minecraft.getMinecraft().entityRenderer.renderItemEntityLight(this, 0.1f);
+ 	}
+ 
+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		float f = super.getEaglerDynamicLightsValueSimple(partialTicks);
+ 		ItemStack itm = this.getDisplayedItem();
+ 		if (itm != null && itm.stackSize > 0) {
+ 			Item item = itm.getItem();
+ 			if (item != null) {
+ 				float f2 = item.getHeldItemBrightnessEagler(itm) * 0.75f;
+ 				f = Math.min(f + f2 * 0.5f, 1.0f) + f2 * 0.5f;
+ 			}
+ 		}
+ 		return f;
+ 	}

> EOF
