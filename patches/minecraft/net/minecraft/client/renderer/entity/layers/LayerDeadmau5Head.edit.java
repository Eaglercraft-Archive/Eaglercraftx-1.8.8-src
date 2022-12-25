
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  4 : 5  @  3 : 4

~ import net.minecraft.client.model.ModelPlayer;

> DELETE  6  @  5 : 6

> CHANGE  17 : 18  @  17 : 18

~ 				&& !abstractclientplayer.isInvisible() && this.playerRenderer.getMainModel() instanceof ModelPlayer) {

> CHANGE  37 : 38  @  37 : 38

~ 				((ModelPlayer) this.playerRenderer.getMainModel()).renderDeadmau5Head(0.0625F);

> EOF
