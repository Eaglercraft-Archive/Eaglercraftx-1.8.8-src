
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  5 : 12  @  5 : 11

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  6  @  6 : 8

> CHANGE  13 : 21  @  13 : 18

~ 		if (resource.cachedPointer != null) {
~ 			TextureUtil.bindTexture(((ITextureObject) resource.cachedPointer).getGlTextureId()); // unsafe, lol
~ 		} else {
~ 			Object object = (ITextureObject) this.mapTextureObjects.get(resource);
~ 			if (object == null) {
~ 				object = new SimpleTexture(resource);
~ 				this.loadTexture(resource, (ITextureObject) object);
~ 			}

> CHANGE  1 : 4  @  1 : 2

~ 			resource.cachedPointer = object;
~ 			TextureUtil.bindTexture(((ITextureObject) object).getGlTextureId());
~ 		}

> CHANGE  11 : 12  @  11 : 12

~ 	public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {

> INSERT  13 : 14  @  13

+ 			final ITextureObject textureObj2 = textureObj;

> CHANGE  2 : 3  @  2 : 3

~ 					return textureObj2.getClass().getName();

> INSERT  5 : 6  @  5

+ 		textureLocation.cachedPointer = textureObj;

> CHANGE  5 : 10  @  5 : 6

~ 		if (textureLocation.cachedPointer != null) {
~ 			return (ITextureObject) textureLocation.cachedPointer;
~ 		} else {
~ 			return (ITextureObject) (textureLocation.cachedPointer = this.mapTextureObjects.get(textureLocation));
~ 		}

> CHANGE  12 : 13  @  12 : 13

~ 				HString.format("dynamic/%s_%d", new Object[] { name, integer }));

> CHANGE  12 : 13  @  12 : 13

~ 		ITextureObject itextureobject = this.mapTextureObjects.remove(textureLocation);

> DELETE  3  @  3 : 4

> EOF
