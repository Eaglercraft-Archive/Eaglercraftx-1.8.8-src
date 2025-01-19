
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;

> DELETE  9  @  9 : 10

> CHANGE  36 : 37  @  36 : 37

~ 		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));

> INSERT  22 : 26  @  22

+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		return 1.0f;
+ 	}
+ 

> INSERT  163 : 170  @  163

+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		float mag = 5.0f;
+ 		DynamicLightManager.renderDynamicLight("entity_" + getEntityId() + "_blaze", entityX, entityY + 0.75, entityZ,
+ 				mag, 0.487f * mag, 0.1411f * mag, false);
+ 	}

> EOF
