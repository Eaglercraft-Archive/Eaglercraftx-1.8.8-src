
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

> INSERT  1 : 3  @  1

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ 

> INSERT  6 : 7  @  6

+ 	protected boolean hasAllocated;

> CHANGE  2 : 10  @  2 : 4

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

> INSERT  15 : 16  @  15

+ 			hasAllocated = false;

> INSERT  12 : 28  @  12

+ 
+ 	/**
+ 	 * This function is needed due to EaglercraftX's use of glTexStorage2D to
+ 	 * allocate memory for textures, some OpenGL implementations don't like it when
+ 	 * you call glTexStorage2D on the same texture object more than once
+ 	 */
+ 	protected void regenerateIfNotAllocated() {
+ 		if (this.glTextureId != -1) {
+ 			if (hasAllocated) {
+ 				if (EaglercraftGPU.checkTexStorageCapable()) {
+ 					EaglercraftGPU.regenerateTexture(glTextureId);
+ 				}
+ 			}
+ 			hasAllocated = true;
+ 		}
+ 	}

> EOF
