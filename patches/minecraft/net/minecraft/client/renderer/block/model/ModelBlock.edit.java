
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> CHANGE  9 : 10  @  9 : 16

~ 

> CHANGE  1 : 3  @  1 : 3

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  1 : 10  @  1

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.minecraft.util.ResourceLocation;
+ 

> CHANGE  2 : 3  @  2 : 9

~ 

> DELETE  9  @  9 : 13

> CHANGE  1 : 2  @  1 : 2

~ 		return (ModelBlock) JSONTypeProvider.deserialize(new JSONObject(parString1), ModelBlock.class);

> CHANGE  124 : 127  @  124 : 125

~ 			} catch (ModelBlock.LoopException var5) {
~ 				throw var5;
~ 			} catch (Throwable var6) {

> CHANGE  15 : 18  @  15 : 20

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlock> {
~ 		public ModelBlock deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.getModelElements(jsonobject);

> CHANGE  4 : 5  @  4 : 5

~ 				throw new JSONException("BlockModel requires either elements or parent, found neither");

> CHANGE  1 : 2  @  1 : 2

~ 				throw new JSONException("BlockModel requires either elements or parent, found both");

> CHANGE  5 : 7  @  5 : 8

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("display");
~ 					itemcameratransforms = JSONTypeProvider.deserialize(jsonobject1, ItemCameraTransforms.class);

> CHANGE  7 : 8  @  7 : 8

~ 		private Map<String, String> getTextures(JSONObject parJsonObject) {

> CHANGE  2 : 3  @  2 : 3

~ 				JSONObject jsonobject = parJsonObject.getJSONObject("textures");

> CHANGE  1 : 3  @  1 : 3

~ 				for (String entry : jsonobject.keySet()) {
~ 					hashmap.put(entry, jsonobject.getString(entry));

> CHANGE  6 : 8  @  6 : 8

~ 		private String getParent(JSONObject parJsonObject) {
~ 			return parJsonObject.optString("parent", "");

> CHANGE  2 : 4  @  2 : 4

~ 		protected boolean getAmbientOcclusionEnabled(JSONObject parJsonObject) {
~ 			return parJsonObject.optBoolean("ambientocclusion", true);

> CHANGE  2 : 3  @  2 : 4

~ 		protected List<BlockPart> getModelElements(JSONObject parJsonObject) {

> CHANGE  2 : 4  @  2 : 4

~ 				for (Object jsonelement : parJsonObject.getJSONArray("elements")) {
~ 					arraylist.add((BlockPart) JSONTypeProvider.deserialize(jsonelement, BlockPart.class));

> EOF
