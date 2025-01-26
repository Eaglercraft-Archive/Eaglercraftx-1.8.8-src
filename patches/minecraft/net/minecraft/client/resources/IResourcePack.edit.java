
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.ResourceIndex;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> CHANGE  13 : 14  @  13 : 14

~ 	ImageData getPackImage() throws IOException;

> INSERT  2 : 4  @  2

+ 
+ 	ResourceIndex getEaglerFileIndex();

> EOF
