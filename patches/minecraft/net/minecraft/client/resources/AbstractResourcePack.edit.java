
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 9

> CHANGE  4 : 15  @  11 : 12

~ import java.nio.charset.StandardCharsets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  16  @  13 : 14

> DELETE  19  @  17 : 20

> CHANGE  22 : 23  @  23 : 24

~ 	protected final String resourcePackFile;

> CHANGE  24 : 25  @  25 : 26

~ 	public AbstractResourcePack(String resourcePackFileIn) {

> CHANGE  29 : 30  @  30 : 31

~ 		return HString.format("%s/%s/%s",

> DELETE  33  @  34 : 38

> CHANGE  52 : 60  @  57 : 58

~ 		try {
~ 			return readMetadata(parIMetadataSerializer, this.getInputStreamByName("pack.mcmeta"), parString1);
~ 		} catch (JSONException e) {
~ 			if (SYS.VFS != null) {
~ 				SYS.deleteResourcePack(this.resourcePackFile);
~ 			}
~ 			throw e;
~ 		}

> CHANGE  64 : 65  @  62 : 64

~ 		JSONObject jsonobject = null;

> CHANGE  67 : 70  @  66 : 70

~ 			jsonobject = new JSONObject(IOUtils.inputStreamToString(parInputStream, StandardCharsets.UTF_8));
~ 		} catch (RuntimeException | IOException runtimeexception) {
~ 			throw new JSONException(runtimeexception);

> CHANGE  71 : 72  @  71 : 72

~ 			IOUtils.closeQuietly(parInputStream);

> CHANGE  77 : 78  @  77 : 78

~ 	public ImageData getPackImage() throws IOException {

> CHANGE  82 : 83  @  82 : 83

~ 		return this.resourcePackFile;

> EOF
