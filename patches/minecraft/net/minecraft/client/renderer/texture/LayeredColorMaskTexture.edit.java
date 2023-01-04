
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  3 : 7  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  1  @  1 : 3

> DELETE  4  @  4 : 6

> CHANGE  17 : 18  @  17 : 18

~ 		ImageData bufferedimage;

> CHANGE  1 : 2  @  1 : 2

~ 			ImageData bufferedimage1 = TextureUtil

> DELETE  1  @  1 : 5

> CHANGE  1 : 4  @  1 : 4

~ 			bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, false);
~ 			bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 					bufferedimage1.width, bufferedimage1.height);

> CHANGE  6 : 11  @  6 : 13

~ 					ImageData bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
~ 					if (bufferedimage2.width == bufferedimage.width && bufferedimage2.height == bufferedimage.height) {
~ 						for (int k = 0; k < bufferedimage2.height; ++k) {
~ 							for (int l = 0; l < bufferedimage2.width; ++l) {
~ 								int i1 = bufferedimage2.pixels[k * bufferedimage2.width + l];

> CHANGE  2 : 6  @  2 : 5

~ 									int k1 = bufferedimage1.pixels[k * bufferedimage1.width + l];
~ 									int l1 = MathHelper.func_180188_d(k1, ImageData.swapRB(mapcolor.colorValue))
~ 											& 16777215;
~ 									bufferedimage2.pixels[k * bufferedimage2.width + l] = j1 | l1;

> CHANGE  4 : 6  @  4 : 5

~ 						bufferedimage.drawLayer(bufferedimage2, 0, 0, bufferedimage2.width, bufferedimage2.height, 0, 0,
~ 								bufferedimage2.width, bufferedimage2.height);

> EOF
