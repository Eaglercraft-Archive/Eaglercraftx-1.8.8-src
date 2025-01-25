
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 9

> CHANGE  2 : 14  @  2 : 3

~ import java.nio.charset.StandardCharsets;
~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.ResourceIndex;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 6

> CHANGE  3 : 5  @  3 : 4

~ 	protected final String resourcePackFile;
~ 	protected ResourceIndex resourceIndex;

> CHANGE  1 : 2  @  1 : 2

~ 	public AbstractResourcePack(String resourcePackFileIn) {

> CHANGE  4 : 5  @  4 : 5

~ 		return HString.format("%s/%s/%s",

> DELETE  3  @  3 : 7

> CHANGE  19 : 27  @  19 : 20

~ 		try {
~ 			return readMetadata(parIMetadataSerializer, this.getInputStreamByName("pack.mcmeta"), parString1);
~ 		} catch (JSONException e) {
~ 			if (this instanceof EaglerFolderResourcePack) {
~ 				EaglerFolderResourcePack.deleteResourcePack((EaglerFolderResourcePack) this);
~ 			}
~ 			throw e;
~ 		}

> CHANGE  4 : 5  @  4 : 7

~ 		JSONObject jsonobject = null;

> CHANGE  1 : 4  @  1 : 5

~ 			jsonobject = new JSONObject(IOUtils.inputStreamToString(parInputStream, StandardCharsets.UTF_8));
~ 		} catch (RuntimeException | IOException runtimeexception) {
~ 			throw new JSONException(runtimeexception);

> CHANGE  1 : 2  @  1 : 2

~ 			IOUtils.closeQuietly(parInputStream);

> CHANGE  5 : 6  @  5 : 6

~ 	public ImageData getPackImage() throws IOException {

> CHANGE  4 : 5  @  4 : 5

~ 		return this.resourcePackFile;

> INSERT  1 : 5  @  1

+ 
+ 	public ResourceIndex getEaglerFileIndex() {
+ 		return this.resourceIndex;
+ 	}

> EOF
