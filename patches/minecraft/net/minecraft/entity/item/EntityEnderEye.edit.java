
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;

> DELETE  1  @  1 : 2

> INSERT  168 : 183  @  168

+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		return 0.5f;
+ 	}
+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		if (isInFrustum && renderX * renderX + renderY * renderY + renderZ * renderZ < 150.0) {
+ 			float mag = 0.5f;
+ 			DynamicLightManager.renderDynamicLight("entity_" + getEntityId() + "_endereye", entityX, entityY + 0.2,
+ 					entityZ, mag * 0.1990f, mag * 0.7750f, mag * 0.4130f, false);
+ 		}
+ 	}
+ 

> EOF
