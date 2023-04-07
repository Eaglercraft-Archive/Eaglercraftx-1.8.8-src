
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

> INSERT  1 : 3  @  1

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ 

> CHANGE  8 : 16  @  8 : 10

~ 		if (blur != parFlag || mipmap != parFlag2) {
~ 			this.blur = parFlag;
~ 			this.mipmap = parFlag2;
~ 			setBlurMipmapDirect0(parFlag, parFlag2);
~ 		}
~ 	}
~ 
~ 	protected void setBlurMipmapDirect0(boolean parFlag, boolean parFlag2) {

> CHANGE  10 : 12  @  10 : 12

~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, i);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, short1);

> EOF
