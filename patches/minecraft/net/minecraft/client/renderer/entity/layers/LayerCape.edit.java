
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.model.ModelPlayer;

> DELETE  1  @  1 : 2

> CHANGE  14 : 16  @  14 : 15

~ 				&& abstractclientplayer.getLocationCape() != null
~ 				&& this.playerRenderer.getMainModel() instanceof ModelPlayer) {

> CHANGE  42 : 43  @  42 : 43

~ 			((ModelPlayer) this.playerRenderer.getMainModel()).renderCape(0.0625F);

> EOF
