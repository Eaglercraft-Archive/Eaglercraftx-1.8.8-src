
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  5 : 17  @  5 : 11

~ 
~ import com.carrotsearch.hppc.ObjectIntHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
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

> CHANGE  5 : 6  @  5 : 6

~ 	private final ObjectIntMap<String> mapTextureCounters = new ObjectIntHashMap<>();

> CHANGE  7 : 16  @  7 : 12

~ 		int glTex;
~ 		if (resource.cachedPointerType == ResourceLocation.CACHED_POINTER_TEXTURE) {
~ 			TextureUtil.bindTexture(glTex = ((ITextureObject) resource.cachedPointer).getGlTextureId());
~ 		} else {
~ 			Object object = (ITextureObject) this.mapTextureObjects.get(resource);
~ 			if (object == null) {
~ 				object = new SimpleTexture(resource);
~ 				this.loadTexture(resource, (ITextureObject) object);
~ 			}

> CHANGE  1 : 16  @  1 : 2

~ 			resource.cachedPointer = object;
~ 			resource.cachedPointerType = ResourceLocation.CACHED_POINTER_TEXTURE;
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

> INSERT  5 : 7  @  5

+ 		textureLocation.cachedPointerType = ResourceLocation.CACHED_POINTER_TEXTURE;
+ 		textureLocation.cachedPointer = textureObj;

> CHANGE  5 : 7  @  5 : 12

~ 		if (textureLocation.cachedPointerType == ResourceLocation.CACHED_POINTER_TEXTURE) {
~ 			return (ITextureObject) textureLocation.cachedPointer;

> CHANGE  1 : 3  @  1 : 2

~ 			textureLocation.cachedPointerType = ResourceLocation.CACHED_POINTER_TEXTURE;
~ 			return (ITextureObject) (textureLocation.cachedPointer = this.mapTextureObjects.get(textureLocation));

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 3  @  1

+ 	public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
+ 		int integer = this.mapTextureCounters.getOrDefault(name, 0) + 1;

> CHANGE  2 : 3  @  2 : 3

~ 				HString.format("dynamic/%s_%d", new Object[] { name, integer }));

> CHANGE  5 : 7  @  5 : 7

~ 		for (int i = 0, l = this.listTickables.size(); i < l; ++i) {
~ 			this.listTickables.get(i).tick();

> CHANGE  5 : 6  @  5 : 6

~ 		ITextureObject itextureobject = this.mapTextureObjects.remove(textureLocation);

> DELETE  3  @  3 : 4

> EOF
