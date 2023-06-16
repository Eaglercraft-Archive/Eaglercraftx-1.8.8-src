
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  3 : 4  @  3 : 6

~ import net.minecraft.client.network.NetHandlerPlayClient;

> DELETE  2  @  2 : 3

> CHANGE  55 : 59  @  55 : 64

~ 				if (parGameProfile != null && parGameProfile.getId() != null) {
~ 					NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
~ 					if (netHandler != null) {
~ 						resourcelocation = netHandler.getSkinCache().getSkin(parGameProfile).getResourceLocation();

> DELETE  2  @  2 : 3

> EOF
