
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  3 : 7  @  6

+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  5  @  1 : 3

> DELETE  4  @  6 : 8

> CHANGE  17 : 18  @  19 : 20

~ 		ImageData bufferedimage;

> CHANGE  2 : 3  @  2 : 3

~ 			ImageData bufferedimage1 = TextureUtil

> DELETE  2  @  2 : 6

> CHANGE  1 : 4  @  5 : 8

~ 			bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, false);
~ 			bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 					bufferedimage1.width, bufferedimage1.height);

> CHANGE  9 : 14  @  9 : 16

~ 					ImageData bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
~ 					if (bufferedimage2.width == bufferedimage.width && bufferedimage2.height == bufferedimage.height) {
~ 						for (int k = 0; k < bufferedimage2.height; ++k) {
~ 							for (int l = 0; l < bufferedimage2.width; ++l) {
~ 								int i1 = bufferedimage2.pixels[k * bufferedimage2.width + l];

> CHANGE  7 : 11  @  9 : 12

~ 									int k1 = bufferedimage1.pixels[k * bufferedimage1.width + l];
~ 									int l1 = MathHelper.func_180188_d(k1, ImageData.swapRB(mapcolor.colorValue))
~ 											& 16777215;
~ 									bufferedimage2.pixels[k * bufferedimage2.width + l] = j1 | l1;

> CHANGE  8 : 10  @  7 : 8

~ 						bufferedimage.drawLayer(bufferedimage2, 0, 0, bufferedimage2.width, bufferedimage2.height, 0, 0,
~ 								bufferedimage2.width, bufferedimage2.height);

> EOF
