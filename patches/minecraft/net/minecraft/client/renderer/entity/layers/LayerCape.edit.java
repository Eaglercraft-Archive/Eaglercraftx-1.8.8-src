
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  2 : 3  @  1 : 2

~ import net.minecraft.client.model.ModelPlayer;

> DELETE  2  @  2 : 3

> CHANGE  14 : 16  @  15 : 16

~ 				&& abstractclientplayer.getLocationCape() != null
~ 				&& this.playerRenderer.getMainModel() instanceof ModelPlayer) {

> CHANGE  44 : 45  @  43 : 44

~ 			((ModelPlayer) this.playerRenderer.getMainModel()).renderCape(0.0625F);

> EOF
