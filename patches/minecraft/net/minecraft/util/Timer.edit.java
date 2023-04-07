
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 4

> CHANGE  53 : 60  @  53 : 54

~ 		int fastMathSetting = Minecraft.getMinecraft().gameSettings.fastMath;
~ 		if (fastMathSetting > 0) {
~ 			float f = fastMathSetting == 2 ? 16.0f : 64.0f;
~ 			this.renderPartialTicks = ((int) (this.elapsedPartialTicks * f) / f);
~ 		} else {
~ 			this.renderPartialTicks = this.elapsedPartialTicks;
~ 		}

> EOF
