
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> INSERT  7 : 17  @  16

+ 
+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  11  @  1 : 2

> CHANGE  3 : 4  @  4 : 8

~ 

> CHANGE  4 : 5  @  7 : 8

~ 		return (ModelBlockDefinition) JSONTypeProvider.deserialize(parReader, ModelBlockDefinition.class);

> CHANGE  42 : 46  @  42 : 48

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition> {
~ 		public ModelBlockDefinition deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.parseVariantsList(jsonobject);
~ 			return new ModelBlockDefinition((Collection<ModelBlockDefinition.Variants>) list);

> CHANGE  6 : 8  @  8 : 11

~ 		protected List<ModelBlockDefinition.Variants> parseVariantsList(JSONObject parJsonObject) {
~ 			JSONObject jsonobject = parJsonObject.getJSONObject("variants");

> CHANGE  4 : 6  @  5 : 7

~ 			for (String entry : jsonobject.keySet()) {
~ 				arraylist.add(this.parseVariants(entry, jsonobject.get(entry)));

> CHANGE  7 : 8  @  7 : 10

~ 		protected ModelBlockDefinition.Variants parseVariants(String s, Object jsonelement) {

> CHANGE  2 : 5  @  4 : 9

~ 			if (jsonelement instanceof JSONArray) {
~ 				for (Object jsonelement1 : (JSONArray) jsonelement) {
~ 					arraylist.add(JSONTypeProvider.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));

> CHANGE  5 : 6  @  7 : 9

~ 				arraylist.add(JSONTypeProvider.deserialize(jsonelement, ModelBlockDefinition.Variant.class));

> CHANGE  60 : 62  @  61 : 65

~ 		public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition.Variant> {
~ 			public ModelBlockDefinition.Variant deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  16 : 18  @  18 : 20

~ 			private boolean parseUvLock(JSONObject parJsonObject) {
~ 				return parJsonObject.optBoolean("uvlock", false);

> CHANGE  4 : 7  @  4 : 7

~ 			protected ModelRotation parseRotation(JSONObject parJsonObject) {
~ 				int i = parJsonObject.optInt("x", 0);
~ 				int j = parJsonObject.optInt("y", 0);

> CHANGE  5 : 6  @  5 : 6

~ 					throw new JSONException("Invalid BlockModelRotation x: " + i + ", y: " + j);

> CHANGE  6 : 8  @  6 : 8

~ 			protected String parseModel(JSONObject parJsonObject) {
~ 				return parJsonObject.getString("model");

> CHANGE  4 : 6  @  4 : 6

~ 			protected int parseWeight(JSONObject parJsonObject) {
~ 				return parJsonObject.optInt("weight", 1);

> EOF
