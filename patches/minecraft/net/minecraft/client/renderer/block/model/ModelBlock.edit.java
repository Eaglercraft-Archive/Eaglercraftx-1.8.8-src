
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> CHANGE  9 : 10  @  18 : 25

~ 

> CHANGE  2 : 4  @  8 : 10

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  3 : 12  @  3

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.minecraft.util.ResourceLocation;
+ 

> CHANGE  11 : 12  @  2 : 9

~ 

> DELETE  10  @  16 : 20

> CHANGE  1 : 2  @  5 : 6

~ 		return (ModelBlock) JSONTypeProvider.deserialize(new JSONObject(parString1), ModelBlock.class);

> CHANGE  125 : 128  @  125 : 126

~ 			} catch (ModelBlock.LoopException var5) {
~ 				throw var5;
~ 			} catch (Throwable var6) {

> CHANGE  18 : 21  @  16 : 21

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlock> {
~ 		public ModelBlock deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.getModelElements(jsonobject);

> CHANGE  7 : 8  @  9 : 10

~ 				throw new JSONException("BlockModel requires either elements or parent, found neither");

> CHANGE  2 : 3  @  2 : 3

~ 				throw new JSONException("BlockModel requires either elements or parent, found both");

> CHANGE  6 : 8  @  6 : 9

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("display");
~ 					itemcameratransforms = JSONTypeProvider.deserialize(jsonobject1, ItemCameraTransforms.class);

> CHANGE  9 : 10  @  10 : 11

~ 		private Map<String, String> getTextures(JSONObject parJsonObject) {

> CHANGE  3 : 4  @  3 : 4

~ 				JSONObject jsonobject = parJsonObject.getJSONObject("textures");

> CHANGE  2 : 4  @  2 : 4

~ 				for (String entry : jsonobject.keySet()) {
~ 					hashmap.put(entry, jsonobject.getString(entry));

> CHANGE  8 : 10  @  8 : 10

~ 		private String getParent(JSONObject parJsonObject) {
~ 			return parJsonObject.optString("parent", "");

> CHANGE  4 : 6  @  4 : 6

~ 		protected boolean getAmbientOcclusionEnabled(JSONObject parJsonObject) {
~ 			return parJsonObject.optBoolean("ambientocclusion", true);

> CHANGE  4 : 5  @  4 : 6

~ 		protected List<BlockPart> getModelElements(JSONObject parJsonObject) {

> CHANGE  3 : 5  @  4 : 6

~ 				for (Object jsonelement : parJsonObject.getJSONArray("elements")) {
~ 					arraylist.add((BlockPart) JSONTypeProvider.deserialize(jsonelement, BlockPart.class));

> EOF
