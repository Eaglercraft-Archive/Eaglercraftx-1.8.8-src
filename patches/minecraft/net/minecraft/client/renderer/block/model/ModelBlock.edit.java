
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> CHANGE  11 : 12  @  20 : 27

~ 

> CHANGE  13 : 15  @  28 : 30

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  16 : 25  @  31

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.minecraft.util.ResourceLocation;
+ 

> CHANGE  27 : 28  @  33 : 40

~ 

> DELETE  37  @  49 : 53

> CHANGE  38 : 39  @  54 : 55

~ 		return (ModelBlock) JSONTypeProvider.deserialize(new JSONObject(parString1), ModelBlock.class);

> CHANGE  163 : 166  @  179 : 180

~ 			} catch (ModelBlock.LoopException var5) {
~ 				throw var5;
~ 			} catch (Throwable var6) {

> CHANGE  181 : 184  @  195 : 200

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlock> {
~ 		public ModelBlock deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.getModelElements(jsonobject);

> CHANGE  188 : 189  @  204 : 205

~ 				throw new JSONException("BlockModel requires either elements or parent, found neither");

> CHANGE  190 : 191  @  206 : 207

~ 				throw new JSONException("BlockModel requires either elements or parent, found both");

> CHANGE  196 : 198  @  212 : 215

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("display");
~ 					itemcameratransforms = JSONTypeProvider.deserialize(jsonobject1, ItemCameraTransforms.class);

> CHANGE  205 : 206  @  222 : 223

~ 		private Map<String, String> getTextures(JSONObject parJsonObject) {

> CHANGE  208 : 209  @  225 : 226

~ 				JSONObject jsonobject = parJsonObject.getJSONObject("textures");

> CHANGE  210 : 212  @  227 : 229

~ 				for (String entry : jsonobject.keySet()) {
~ 					hashmap.put(entry, jsonobject.getString(entry));

> CHANGE  218 : 220  @  235 : 237

~ 		private String getParent(JSONObject parJsonObject) {
~ 			return parJsonObject.optString("parent", "");

> CHANGE  222 : 224  @  239 : 241

~ 		protected boolean getAmbientOcclusionEnabled(JSONObject parJsonObject) {
~ 			return parJsonObject.optBoolean("ambientocclusion", true);

> CHANGE  226 : 227  @  243 : 245

~ 		protected List<BlockPart> getModelElements(JSONObject parJsonObject) {

> CHANGE  229 : 231  @  247 : 249

~ 				for (Object jsonelement : parJsonObject.getJSONArray("elements")) {
~ 					arraylist.add((BlockPart) JSONTypeProvider.deserialize(jsonelement, BlockPart.class));

> EOF
