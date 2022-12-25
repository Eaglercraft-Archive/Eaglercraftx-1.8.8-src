
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  5 : 9  @  8

+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  10  @  9 : 11

> DELETE  14  @  15 : 17

> CHANGE  31 : 32  @  34 : 35

~ 		ImageData bufferedimage;

> CHANGE  33 : 34  @  36 : 37

~ 			ImageData bufferedimage1 = TextureUtil

> DELETE  35  @  38 : 42

> CHANGE  36 : 39  @  43 : 46

~ 			bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, false);
~ 			bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 					bufferedimage1.width, bufferedimage1.height);

> CHANGE  45 : 50  @  52 : 59

~ 					ImageData bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
~ 					if (bufferedimage2.width == bufferedimage.width && bufferedimage2.height == bufferedimage.height) {
~ 						for (int k = 0; k < bufferedimage2.height; ++k) {
~ 							for (int l = 0; l < bufferedimage2.width; ++l) {
~ 								int i1 = bufferedimage2.pixels[k * bufferedimage2.width + l];

> CHANGE  52 : 56  @  61 : 64

~ 									int k1 = bufferedimage1.pixels[k * bufferedimage1.width + l];
~ 									int l1 = MathHelper.func_180188_d(k1, ImageData.swapRB(mapcolor.colorValue))
~ 											& 16777215;
~ 									bufferedimage2.pixels[k * bufferedimage2.width + l] = j1 | l1;

> CHANGE  60 : 62  @  68 : 69

~ 						bufferedimage.drawLayer(bufferedimage2, 0, 0, bufferedimage2.width, bufferedimage2.height, 0, 0,
~ 								bufferedimage2.width, bufferedimage2.height);

> EOF
