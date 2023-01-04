
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> INSERT  7 : 17  @  7

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

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 7

~ 

> CHANGE  3 : 4  @  3 : 4

~ 		return (ModelBlockDefinition) JSONTypeProvider.deserialize(parReader, ModelBlockDefinition.class);

> CHANGE  41 : 45  @  41 : 47

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition> {
~ 		public ModelBlockDefinition deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.parseVariantsList(jsonobject);
~ 			return new ModelBlockDefinition((Collection<ModelBlockDefinition.Variants>) list);

> CHANGE  2 : 4  @  2 : 5

~ 		protected List<ModelBlockDefinition.Variants> parseVariantsList(JSONObject parJsonObject) {
~ 			JSONObject jsonobject = parJsonObject.getJSONObject("variants");

> CHANGE  2 : 4  @  2 : 4

~ 			for (String entry : jsonobject.keySet()) {
~ 				arraylist.add(this.parseVariants(entry, jsonobject.get(entry)));

> CHANGE  5 : 6  @  5 : 8

~ 		protected ModelBlockDefinition.Variants parseVariants(String s, Object jsonelement) {

> CHANGE  1 : 4  @  1 : 6

~ 			if (jsonelement instanceof JSONArray) {
~ 				for (Object jsonelement1 : (JSONArray) jsonelement) {
~ 					arraylist.add(JSONTypeProvider.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));

> CHANGE  2 : 3  @  2 : 4

~ 				arraylist.add(JSONTypeProvider.deserialize(jsonelement, ModelBlockDefinition.Variant.class));

> CHANGE  59 : 61  @  59 : 63

~ 		public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition.Variant> {
~ 			public ModelBlockDefinition.Variant deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  14 : 16  @  14 : 16

~ 			private boolean parseUvLock(JSONObject parJsonObject) {
~ 				return parJsonObject.optBoolean("uvlock", false);

> CHANGE  2 : 5  @  2 : 5

~ 			protected ModelRotation parseRotation(JSONObject parJsonObject) {
~ 				int i = parJsonObject.optInt("x", 0);
~ 				int j = parJsonObject.optInt("y", 0);

> CHANGE  2 : 3  @  2 : 3

~ 					throw new JSONException("Invalid BlockModelRotation x: " + i + ", y: " + j);

> CHANGE  5 : 7  @  5 : 7

~ 			protected String parseModel(JSONObject parJsonObject) {
~ 				return parJsonObject.getString("model");

> CHANGE  2 : 4  @  2 : 4

~ 			protected int parseWeight(JSONObject parJsonObject) {
~ 				return parJsonObject.optInt("weight", 1);

> EOF
