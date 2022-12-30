
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 12

> INSERT  1 : 4  @  11

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  4 : 8  @  1 : 5

~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ 
~ public class SoundListSerializer implements JSONTypeDeserializer<JSONObject, SoundList> {
~ 	public SoundList deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  5 : 6  @  5 : 6

~ 		soundlist.setReplaceExisting(jsonobject.optBoolean("replace", false));

> CHANGE  2 : 3  @  2 : 3

~ 				.getCategory(jsonobject.optString("category", SoundCategory.MASTER.getCategoryName()));

> CHANGE  4 : 5  @  4 : 5

~ 			JSONArray jsonarray = jsonobject.getJSONArray("sounds");

> CHANGE  2 : 4  @  2 : 4

~ 			for (int i = 0; i < jsonarray.length(); ++i) {
~ 				Object jsonelement = jsonarray.get(i);

> CHANGE  3 : 8  @  3 : 8

~ 				if (jsonelement instanceof String) {
~ 					soundlist$soundentry.setSoundEntryName((String) jsonelement);
~ 				} else if (jsonelement instanceof JSONObject) {
~ 					JSONObject jsonobject1 = (JSONObject) jsonelement;
~ 					soundlist$soundentry.setSoundEntryName(jsonobject1.getString("name"));

> CHANGE  7 : 8  @  7 : 8

~ 								.getType(jsonobject1.getString("type"));

> CHANGE  6 : 7  @  6 : 7

~ 						float f = jsonobject1.getFloat("volume");

> CHANGE  6 : 7  @  6 : 7

~ 						float f1 = jsonobject1.getFloat("pitch");

> CHANGE  6 : 7  @  6 : 7

~ 						int j = jsonobject1.getInt("weight");

> CHANGE  6 : 7  @  6 : 7

~ 						soundlist$soundentry.setStreaming(jsonobject1.getBoolean("stream"));

> EOF
