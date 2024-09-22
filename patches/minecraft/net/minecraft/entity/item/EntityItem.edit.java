
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.List;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  1 : 2  @  1

+ import net.minecraft.client.Minecraft;

> DELETE  13  @  13 : 15

> CHANGE  101 : 105  @  101 : 104

~ 		List<EntityItem> lst = this.worldObj.getEntitiesWithinAABB(EntityItem.class,
~ 				this.getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D));
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			this.combineItems(lst.get(i));

> INSERT  258 : 280  @  258

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
+ 		ItemStack itm = this.getEntityItem();
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
