
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  5 : 11  @  8 : 10

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  13  @  12 : 14

> CHANGE  24 : 25  @  25 : 26

~ 		ImageData bufferedimage = null;

> CHANGE  30 : 31  @  31 : 32

~ 					ImageData bufferedimage1 = TextureUtil.readBufferedImage(inputstream);

> CHANGE  32 : 33  @  33 : 34

~ 						bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, true);

> CHANGE  35 : 37  @  36 : 37

~ 					bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
~ 							bufferedimage1.width, bufferedimage1.height);

> EOF
