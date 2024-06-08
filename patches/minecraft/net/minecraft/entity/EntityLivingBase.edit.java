
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  3 : 4  @  3

+ import net.minecraft.client.Minecraft;

> DELETE  1  @  1 : 5

> CHANGE  39 : 41  @  39 : 40

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> CHANGE  264 : 265  @  264 : 265

~ 	public EaglercraftRandom getRNG() {

> CHANGE  46 : 49  @  46 : 47

~ 		ItemStack[] inv = this.getInventory();
~ 		for (int i = 0; i < inv.length; ++i) {
~ 			ItemStack itemstack = inv[i];

> CHANGE  7 : 9  @  7 : 8

~ 		for (int i = 0; i < inv.length; ++i) {
~ 			ItemStack itemstack1 = inv[i];

> INSERT  1254 : 1277  @  1254

+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		Minecraft mc = Minecraft.getMinecraft();
+ 		if (mc.gameSettings.thirdPersonView != 0 || !(mc.getRenderViewEntity() == this)) {
+ 			Minecraft.getMinecraft().entityRenderer.renderHeldItemLight(this, 1.0f);
+ 		}
+ 	}
+ 
+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		float f = super.getEaglerDynamicLightsValueSimple(partialTicks);
+ 		ItemStack itm = this.getHeldItem();
+ 		if (itm != null && itm.stackSize > 0) {
+ 			Item item = itm.getItem();
+ 			if (item != null) {
+ 				float f2 = item.getHeldItemBrightnessEagler();
+ 				f = Math.min(f + f2 * 0.5f, 1.0f) + f2 * 0.5f;
+ 			}
+ 		}
+ 		return f;
+ 	}

> EOF
