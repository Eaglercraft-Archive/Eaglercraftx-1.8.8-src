
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EmissiveItems;

> DELETE  1  @  1 : 4

> CHANGE  24 : 26  @  24 : 25

~ 		ItemStack itm = this.func_177082_d(entity);
~ 		this.field_177083_e.func_181564_a(itm, ItemCameraTransforms.TransformType.GROUND);

> INSERT  2 : 10  @  2

+ 		if (DynamicLightManager.isRenderingLights()) {
+ 			float[] emission = EmissiveItems.getItemEmission(itm);
+ 			if (emission != null) {
+ 				float mag = 0.1f;
+ 				DynamicLightManager.renderDynamicLight("entity_" + entity.getEntityId() + "_item_throw", d0, d1, d2,
+ 						emission[0] * mag, emission[1] * mag, emission[2] * mag, false);
+ 			}
+ 		}

> EOF
