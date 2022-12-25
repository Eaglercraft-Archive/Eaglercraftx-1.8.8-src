
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> INSERT  9 : 19  @  18

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

> DELETE  20  @  19 : 20

> CHANGE  23 : 24  @  23 : 27

~ 

> CHANGE  27 : 28  @  30 : 31

~ 		return (ModelBlockDefinition) JSONTypeProvider.deserialize(parReader, ModelBlockDefinition.class);

> CHANGE  69 : 73  @  72 : 78

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition> {
~ 		public ModelBlockDefinition deserialize(JSONObject jsonobject) throws JSONException {
~ 			List list = this.parseVariantsList(jsonobject);
~ 			return new ModelBlockDefinition((Collection<ModelBlockDefinition.Variants>) list);

> CHANGE  75 : 77  @  80 : 83

~ 		protected List<ModelBlockDefinition.Variants> parseVariantsList(JSONObject parJsonObject) {
~ 			JSONObject jsonobject = parJsonObject.getJSONObject("variants");

> CHANGE  79 : 81  @  85 : 87

~ 			for (String entry : jsonobject.keySet()) {
~ 				arraylist.add(this.parseVariants(entry, jsonobject.get(entry)));

> CHANGE  86 : 87  @  92 : 95

~ 		protected ModelBlockDefinition.Variants parseVariants(String s, Object jsonelement) {

> CHANGE  88 : 91  @  96 : 101

~ 			if (jsonelement instanceof JSONArray) {
~ 				for (Object jsonelement1 : (JSONArray) jsonelement) {
~ 					arraylist.add(JSONTypeProvider.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));

> CHANGE  93 : 94  @  103 : 105

~ 				arraylist.add(JSONTypeProvider.deserialize(jsonelement, ModelBlockDefinition.Variant.class));

> CHANGE  153 : 155  @  164 : 168

~ 		public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition.Variant> {
~ 			public ModelBlockDefinition.Variant deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  169 : 171  @  182 : 184

~ 			private boolean parseUvLock(JSONObject parJsonObject) {
~ 				return parJsonObject.optBoolean("uvlock", false);

> CHANGE  173 : 176  @  186 : 189

~ 			protected ModelRotation parseRotation(JSONObject parJsonObject) {
~ 				int i = parJsonObject.optInt("x", 0);
~ 				int j = parJsonObject.optInt("y", 0);

> CHANGE  178 : 179  @  191 : 192

~ 					throw new JSONException("Invalid BlockModelRotation x: " + i + ", y: " + j);

> CHANGE  184 : 186  @  197 : 199

~ 			protected String parseModel(JSONObject parJsonObject) {
~ 				return parJsonObject.getString("model");

> CHANGE  188 : 190  @  201 : 203

~ 			protected int parseWeight(JSONObject parJsonObject) {
~ 				return parJsonObject.optInt("weight", 1);

> EOF
