
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  3 : 9  @  3 : 5

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  2  @  2 : 4

> CHANGE  11 : 12  @  11 : 12

~ 		ImageData bufferedimage = null;

> CHANGE  5 : 6  @  5 : 6

~ 					ImageData bufferedimage1 = TextureUtil.readBufferedImage(inputstream);

> CHANGE  1 : 2  @  1 : 2

~ 						bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, true);

> CHANGE  2 : 4  @  2 : 3

~ 					bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 							bufferedimage1.width, bufferedimage1.height);

> EOF
