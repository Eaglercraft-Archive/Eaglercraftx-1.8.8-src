
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  7 : 14  @  9 : 15

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  20  @  21 : 23

> CHANGE  33 : 41  @  36 : 41

~ 		if (resource.cachedPointer != null) {
~ 			TextureUtil.bindTexture(((ITextureObject) resource.cachedPointer).getGlTextureId()); // unsafe, lol
~ 		} else {
~ 			Object object = (ITextureObject) this.mapTextureObjects.get(resource);
~ 			if (object == null) {
~ 				object = new SimpleTexture(resource);
~ 				this.loadTexture(resource, (ITextureObject) object);
~ 			}

> CHANGE  42 : 45  @  42 : 43

~ 			resource.cachedPointer = object;
~ 			TextureUtil.bindTexture(((ITextureObject) object).getGlTextureId());
~ 		}

> CHANGE  56 : 57  @  54 : 55

~ 	public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {

> INSERT  70 : 71  @  68

+ 			final ITextureObject textureObj2 = textureObj;

> CHANGE  73 : 74  @  70 : 71

~ 					return textureObj2.getClass().getName();

> INSERT  79 : 80  @  76

+ 		textureLocation.cachedPointer = textureObj;

> CHANGE  85 : 90  @  81 : 82

~ 		if (textureLocation.cachedPointer != null) {
~ 			return (ITextureObject) textureLocation.cachedPointer;
~ 		} else {
~ 			return (ITextureObject) (textureLocation.cachedPointer = this.mapTextureObjects.get(textureLocation));
~ 		}

> CHANGE  102 : 103  @  94 : 95

~ 				HString.format("dynamic/%s_%d", new Object[] { name, integer }));

> CHANGE  115 : 116  @  107 : 108

~ 		ITextureObject itextureobject = this.mapTextureObjects.remove(textureLocation);

> DELETE  119  @  111 : 112

> EOF
