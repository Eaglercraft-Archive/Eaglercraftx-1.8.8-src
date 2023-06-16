
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> CHANGE  6 : 7  @  6 : 7

~ 	public ImageData parseUserSkin(ImageData bufferedimage) {

> CHANGE  5 : 21  @  5 : 21

~ 			ImageData bufferedimage1 = new ImageData(this.imageWidth, this.imageHeight, true);
~ 			bufferedimage1.copyPixelsFrom(bufferedimage, 0, 0, bufferedimage.width, bufferedimage.height, 0, 0,
~ 					bufferedimage.width, bufferedimage.height);
~ 			if (bufferedimage.height == 32) {
~ 				bufferedimage1.drawLayer(bufferedimage, 24, 48, 20, 52, 4, 16, 8, 20);
~ 				bufferedimage1.drawLayer(bufferedimage, 28, 48, 24, 52, 8, 16, 12, 20);
~ 				bufferedimage1.drawLayer(bufferedimage, 20, 52, 16, 64, 8, 20, 12, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 24, 52, 20, 64, 4, 20, 8, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 28, 52, 24, 64, 0, 20, 4, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 32, 52, 28, 64, 12, 20, 16, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 40, 48, 36, 52, 44, 16, 48, 20);
~ 				bufferedimage1.drawLayer(bufferedimage, 44, 48, 40, 52, 48, 16, 52, 20);
~ 				bufferedimage1.drawLayer(bufferedimage, 36, 52, 32, 64, 48, 20, 52, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 40, 52, 36, 64, 44, 20, 48, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 44, 52, 40, 64, 40, 20, 44, 32);
~ 				bufferedimage1.drawLayer(bufferedimage, 48, 52, 44, 64, 52, 20, 56, 32);

> CHANGE  2 : 3  @  2 : 4

~ 			this.imageData = bufferedimage1.pixels;

> EOF
