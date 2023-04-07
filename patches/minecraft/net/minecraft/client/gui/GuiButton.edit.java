
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  3  @  3 : 6

> INSERT  13 : 14  @  13

+ 	public float fontScale = 1.0f;

> CHANGE  51 : 66  @  51 : 53

~ 			if (fontScale == 1.0f) {
~ 				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
~ 						this.yPosition + (this.height - 8) / 2, j);
~ 			} else {
~ 				float xScale = fontScale;
~ 				float yScale = 1.0f + (fontScale - 1.0f) * 0.7f;
~ 				float strWidth = fontrenderer.getStringWidth(displayString) / xScale;
~ 				GlStateManager.pushMatrix();
~ 				GlStateManager.translate(this.xPosition + this.width / 2,
~ 						this.yPosition + (this.height - 8 * yScale) / 2, 1.0f);
~ 				GlStateManager.scale(xScale, yScale, 1.0f);
~ 				GlStateManager.translate(-strWidth * 0.5f * xScale, 0.0f, 0.0f);
~ 				fontrenderer.drawStringWithShadow(displayString, 0, 0, j);
~ 				GlStateManager.popMatrix();
~ 			}

> EOF
