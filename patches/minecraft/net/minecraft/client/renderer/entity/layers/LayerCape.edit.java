
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  4 : 5  @  3 : 4

~ import net.minecraft.client.model.ModelPlayer;

> DELETE  6  @  5 : 6

> CHANGE  20 : 22  @  20 : 21

~ 				&& abstractclientplayer.getLocationCape() != null
~ 				&& this.playerRenderer.getMainModel() instanceof ModelPlayer) {

> CHANGE  64 : 65  @  63 : 64

~ 			((ModelPlayer) this.playerRenderer.getMainModel()).renderCape(0.0625F);

> EOF
