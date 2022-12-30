
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  5 : 12  @  7 : 13

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  13  @  12 : 14

> CHANGE  13 : 21  @  15 : 20

~ 		if (resource.cachedPointer != null) {
~ 			TextureUtil.bindTexture(((ITextureObject) resource.cachedPointer).getGlTextureId()); // unsafe, lol
~ 		} else {
~ 			Object object = (ITextureObject) this.mapTextureObjects.get(resource);
~ 			if (object == null) {
~ 				object = new SimpleTexture(resource);
~ 				this.loadTexture(resource, (ITextureObject) object);
~ 			}

> CHANGE  9 : 12  @  6 : 7

~ 			resource.cachedPointer = object;
~ 			TextureUtil.bindTexture(((ITextureObject) object).getGlTextureId());
~ 		}

> CHANGE  14 : 15  @  12 : 13

~ 	public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {

> INSERT  14 : 15  @  14

+ 			final ITextureObject textureObj2 = textureObj;

> CHANGE  3 : 4  @  2 : 3

~ 					return textureObj2.getClass().getName();

> INSERT  6 : 7  @  6

+ 		textureLocation.cachedPointer = textureObj;

> CHANGE  6 : 11  @  5 : 6

~ 		if (textureLocation.cachedPointer != null) {
~ 			return (ITextureObject) textureLocation.cachedPointer;
~ 		} else {
~ 			return (ITextureObject) (textureLocation.cachedPointer = this.mapTextureObjects.get(textureLocation));
~ 		}

> CHANGE  17 : 18  @  13 : 14

~ 				HString.format("dynamic/%s_%d", new Object[] { name, integer }));

> CHANGE  13 : 14  @  13 : 14

~ 		ITextureObject itextureobject = this.mapTextureObjects.remove(textureLocation);

> DELETE  4  @  4 : 5

> EOF
