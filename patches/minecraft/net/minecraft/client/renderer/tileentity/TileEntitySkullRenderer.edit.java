
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  5 : 6  @  8 : 11

~ import net.minecraft.client.network.NetHandlerPlayClient;

> DELETE  3  @  5 : 6

> CHANGE  55 : 59  @  56 : 65

~ 				if (parGameProfile != null && parGameProfile.getId() != null) {
~ 					NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
~ 					if (netHandler != null) {
~ 						resourcelocation = netHandler.getSkinCache().getSkin(parGameProfile).getResourceLocation();

> DELETE  6  @  11 : 12

> EOF
