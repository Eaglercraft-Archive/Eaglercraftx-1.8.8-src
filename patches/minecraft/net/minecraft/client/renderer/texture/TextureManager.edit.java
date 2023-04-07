
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  5 : 15  @  5 : 11

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.minecraft.client.Minecraft;

> DELETE  6  @  6 : 8

> CHANGE  13 : 22  @  13 : 18

~ 		int glTex;
~ 		if (resource.cachedPointer != null) {
~ 			TextureUtil.bindTexture(glTex = ((ITextureObject) resource.cachedPointer).getGlTextureId()); // unsafe, lol
~ 		} else {
~ 			Object object = (ITextureObject) this.mapTextureObjects.get(resource);
~ 			if (object == null) {
~ 				object = new SimpleTexture(resource);
~ 				this.loadTexture(resource, (ITextureObject) object);
~ 			}

> CHANGE  1 : 15  @  1 : 2

~ 			resource.cachedPointer = object;
~ 			TextureUtil.bindTexture(glTex = ((ITextureObject) object).getGlTextureId());
~ 		}
~ 		if (DeferredStateManager.isInDeferredPass()) {
~ 			TextureMap blocksTex = Minecraft.getMinecraft().getTextureMapBlocks();
~ 			if (blocksTex != null) {
~ 				if (blocksTex.getGlTextureId() == glTex) {
~ 					DeferredStateManager.enableMaterialTexture();
~ 					GlStateManager.quickBindTexture(GL_TEXTURE2, blocksTex.eaglerPBRMaterialTexture);
~ 				} else {
~ 					DeferredStateManager.disableMaterialTexture();
~ 				}
~ 			}
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
