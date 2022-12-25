
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 12

> CHANGE  3 : 4  @  13 : 17

~ 

> INSERT  5 : 8  @  18

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;

> INSERT  9 : 13  @  19

+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;
+ 

> CHANGE  14 : 16  @  20 : 23

~ 		implements JSONTypeSerializer<AnimationMetadataSection, JSONObject> {
~ 	public AnimationMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  17 : 18  @  24 : 26

~ 		int i = jsonobject.optInt("frametime", 1);

> CHANGE  24 : 25  @  32 : 33

~ 				JSONArray jsonarray = jsonobject.getJSONArray("frames");

> CHANGE  26 : 28  @  34 : 37

~ 				for (int j = 0; j < jsonarray.length(); ++j) {
~ 					AnimationFrame animationframe = this.parseAnimationFrame(j, jsonarray.get(j));

> CHANGE  33 : 34  @  42 : 44

~ 				throw new JSONException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"),

> CHANGE  38 : 40  @  48 : 50

~ 		int k = jsonobject.optInt("width", -1);
~ 		int l = jsonobject.optInt("height", -1);

> CHANGE  48 : 49  @  58 : 59

~ 		boolean flag = jsonobject.optBoolean("interpolate", false);

> CHANGE  52 : 58  @  62 : 68

~ 	private AnimationFrame parseAnimationFrame(int parInt1, Object parJsonElement) {
~ 		if (parJsonElement instanceof Number) {
~ 			return new AnimationFrame(((Number) parJsonElement).intValue());
~ 		} else if (parJsonElement instanceof JSONObject) {
~ 			JSONObject jsonobject = (JSONObject) parJsonElement;
~ 			int i = jsonobject.optInt("time", -1);

> CHANGE  62 : 63  @  72 : 73

~ 			int j = jsonobject.getInt(getSectionName());

> CHANGE  70 : 73  @  80 : 84

~ 	public JSONObject serialize(AnimationMetadataSection animationmetadatasection) {
~ 		JSONObject jsonobject = new JSONObject();
~ 		jsonobject.put("frametime", Integer.valueOf(animationmetadatasection.getFrameTime()));

> CHANGE  74 : 75  @  85 : 86

~ 			jsonobject.put("width", Integer.valueOf(animationmetadatasection.getFrameWidth()));

> CHANGE  78 : 79  @  89 : 90

~ 			jsonobject.put("height", Integer.valueOf(animationmetadatasection.getFrameHeight()));

> CHANGE  82 : 83  @  93 : 94

~ 			JSONArray jsonarray = new JSONArray();

> CHANGE  86 : 90  @  97 : 101

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("index", Integer.valueOf(animationmetadatasection.getFrameIndex(i)));
~ 					jsonobject1.put("time", Integer.valueOf(animationmetadatasection.getFrameTimeSingle(i)));
~ 					jsonarray.put(jsonobject1);

> CHANGE  91 : 92  @  102 : 103

~ 					jsonarray.put(Integer.valueOf(animationmetadatasection.getFrameIndex(i)));

> CHANGE  95 : 96  @  106 : 107

~ 			jsonobject.put("frames", jsonarray);

> EOF
