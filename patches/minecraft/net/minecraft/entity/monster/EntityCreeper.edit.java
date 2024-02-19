
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;

> DELETE  12  @  12 : 14

> INSERT  204 : 217  @  204

+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		float ff = getCreeperFlashIntensity(partialTicks);
+ 		if ((int) (ff * 10.0F) % 2 != 0) {
+ 			float dynamicLightMag = 7.0f;
+ 			DynamicLightManager.renderDynamicLight("entity_" + getEntityId() + "_creeper_flash", entityX, entityY + 1.0,
+ 					entityZ, dynamicLightMag, dynamicLightMag * 0.7792f, dynamicLightMag * 0.618f, false);
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 		}
+ 	}

> EOF
