
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

> INSERT  1 : 3  @  1

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ 

> CHANGE  8 : 20  @  8 : 19

~ 		if (blur != parFlag || mipmap != parFlag2) {
~ 			this.blur = parFlag;
~ 			this.mipmap = parFlag2;
~ 			int i = -1;
~ 			short short1 = -1;
~ 			if (parFlag) {
~ 				i = parFlag2 ? 9987 : 9729;
~ 				short1 = 9729;
~ 			} else {
~ 				i = parFlag2 ? 9986 : 9728;
~ 				short1 = 9728;
~ 			}

> CHANGE  1 : 4  @  1 : 3

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, i);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, short1);
~ 		}

> EOF
