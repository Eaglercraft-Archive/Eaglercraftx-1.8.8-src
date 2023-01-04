
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 3  @  1 : 3

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> CHANGE  7 : 10  @  7 : 11

~ 	public DynamicTexture(ImageData bufferedImage) {
~ 		this(bufferedImage.width, bufferedImage.height);
~ 		System.arraycopy(bufferedImage.pixels, 0, dynamicTextureData, 0, bufferedImage.pixels.length);

> EOF
