
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

> INSERT  1309 : 1320  @  1309

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

> EOF
