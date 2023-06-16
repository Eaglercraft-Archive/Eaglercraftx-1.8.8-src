
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 12

> INSERT  1 : 4  @  1

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  1 : 5  @  1 : 5

~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ 
~ public class SoundListSerializer implements JSONTypeDeserializer<JSONObject, SoundList> {
~ 	public SoundList deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  1 : 2  @  1 : 2

~ 		soundlist.setReplaceExisting(jsonobject.optBoolean("replace", false));

> CHANGE  1 : 2  @  1 : 2

~ 				.getCategory(jsonobject.optString("category", SoundCategory.MASTER.getCategoryName()));

> CHANGE  3 : 4  @  3 : 4

~ 			JSONArray jsonarray = jsonobject.getJSONArray("sounds");

> CHANGE  1 : 3  @  1 : 3

~ 			for (int i = 0; i < jsonarray.length(); ++i) {
~ 				Object jsonelement = jsonarray.get(i);

> CHANGE  1 : 6  @  1 : 6

~ 				if (jsonelement instanceof String) {
~ 					soundlist$soundentry.setSoundEntryName((String) jsonelement);
~ 				} else if (jsonelement instanceof JSONObject) {
~ 					JSONObject jsonobject1 = (JSONObject) jsonelement;
~ 					soundlist$soundentry.setSoundEntryName(jsonobject1.getString("name"));

> CHANGE  2 : 3  @  2 : 3

~ 								.getType(jsonobject1.getString("type"));

> CHANGE  5 : 6  @  5 : 6

~ 						float f = jsonobject1.getFloat("volume");

> CHANGE  5 : 6  @  5 : 6

~ 						float f1 = jsonobject1.getFloat("pitch");

> CHANGE  5 : 6  @  5 : 6

~ 						int j = jsonobject1.getInt("weight");

> CHANGE  5 : 6  @  5 : 6

~ 						soundlist$soundentry.setStreaming(jsonobject1.getBoolean("stream"));

> EOF
