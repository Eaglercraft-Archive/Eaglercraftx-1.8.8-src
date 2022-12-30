
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  3 : 9  @  6 : 8

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  8  @  4 : 6

> CHANGE  11 : 12  @  13 : 14

~ 		ImageData bufferedimage = null;

> CHANGE  6 : 7  @  6 : 7

~ 					ImageData bufferedimage1 = TextureUtil.readBufferedImage(inputstream);

> CHANGE  2 : 3  @  2 : 3

~ 						bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, true);

> CHANGE  3 : 5  @  3 : 4

~ 					bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 							bufferedimage1.width, bufferedimage1.height);

> EOF
