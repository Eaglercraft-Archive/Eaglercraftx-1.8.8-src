
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 12

> CHANGE  1 : 2  @  1 : 5

~ 

> INSERT  1 : 4  @  1

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;

> INSERT  1 : 5  @  1

+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;
+ 

> CHANGE  1 : 3  @  1 : 4

~ 		implements JSONTypeSerializer<AnimationMetadataSection, JSONObject> {
~ 	public AnimationMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  1 : 2  @  1 : 3

~ 		int i = jsonobject.optInt("frametime", 1);

> CHANGE  6 : 7  @  6 : 7

~ 				JSONArray jsonarray = jsonobject.getJSONArray("frames");

> CHANGE  1 : 3  @  1 : 4

~ 				for (int j = 0; j < jsonarray.length(); ++j) {
~ 					AnimationFrame animationframe = this.parseAnimationFrame(j, jsonarray.get(j));

> CHANGE  5 : 6  @  5 : 7

~ 				throw new JSONException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"),

> CHANGE  4 : 6  @  4 : 6

~ 		int k = jsonobject.optInt("width", -1);
~ 		int l = jsonobject.optInt("height", -1);

> CHANGE  8 : 9  @  8 : 9

~ 		boolean flag = jsonobject.optBoolean("interpolate", false);

> CHANGE  3 : 9  @  3 : 9

~ 	private AnimationFrame parseAnimationFrame(int parInt1, Object parJsonElement) {
~ 		if (parJsonElement instanceof Number) {
~ 			return new AnimationFrame(((Number) parJsonElement).intValue());
~ 		} else if (parJsonElement instanceof JSONObject) {
~ 			JSONObject jsonobject = (JSONObject) parJsonElement;
~ 			int i = jsonobject.optInt("time", -1);

> CHANGE  4 : 5  @  4 : 5

~ 			int j = jsonobject.getInt(getSectionName());

> CHANGE  7 : 10  @  7 : 11

~ 	public JSONObject serialize(AnimationMetadataSection animationmetadatasection) {
~ 		JSONObject jsonobject = new JSONObject();
~ 		jsonobject.put("frametime", Integer.valueOf(animationmetadatasection.getFrameTime()));

> CHANGE  1 : 2  @  1 : 2

~ 			jsonobject.put("width", Integer.valueOf(animationmetadatasection.getFrameWidth()));

> CHANGE  3 : 4  @  3 : 4

~ 			jsonobject.put("height", Integer.valueOf(animationmetadatasection.getFrameHeight()));

> CHANGE  3 : 4  @  3 : 4

~ 			JSONArray jsonarray = new JSONArray();

> CHANGE  3 : 7  @  3 : 7

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("index", Integer.valueOf(animationmetadatasection.getFrameIndex(i)));
~ 					jsonobject1.put("time", Integer.valueOf(animationmetadatasection.getFrameTimeSingle(i)));
~ 					jsonarray.put(jsonobject1);

> CHANGE  1 : 2  @  1 : 2

~ 					jsonarray.put(Integer.valueOf(animationmetadatasection.getFrameIndex(i)));

> CHANGE  3 : 4  @  3 : 4

~ 			jsonobject.put("frames", jsonarray);

> EOF
