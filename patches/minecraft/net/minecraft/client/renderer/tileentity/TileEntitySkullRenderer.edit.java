
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  7 : 8  @  10 : 13

~ import net.minecraft.client.network.NetHandlerPlayClient;

> DELETE  10  @  15 : 16

> CHANGE  65 : 69  @  71 : 80

~ 				if (parGameProfile != null && parGameProfile.getId() != null) {
~ 					NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
~ 					if (netHandler != null) {
~ 						resourcelocation = netHandler.getSkinCache().getSkin(parGameProfile).getResourceLocation();

> DELETE  71  @  82 : 83

> EOF
