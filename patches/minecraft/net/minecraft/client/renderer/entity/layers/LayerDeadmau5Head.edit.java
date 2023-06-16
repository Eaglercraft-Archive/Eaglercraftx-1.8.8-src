
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.model.ModelPlayer;

> DELETE  1  @  1 : 2

> CHANGE  11 : 12  @  11 : 12

~ 				&& !abstractclientplayer.isInvisible() && this.playerRenderer.getMainModel() instanceof ModelPlayer) {

> CHANGE  19 : 20  @  19 : 20

~ 				((ModelPlayer) this.playerRenderer.getMainModel()).renderDeadmau5Head(0.0625F);

> EOF
