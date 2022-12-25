
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 12

> INSERT  3 : 6  @  13

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  7 : 11  @  14 : 18

~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ 
~ public class SoundListSerializer implements JSONTypeDeserializer<JSONObject, SoundList> {
~ 	public SoundList deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  12 : 13  @  19 : 20

~ 		soundlist.setReplaceExisting(jsonobject.optBoolean("replace", false));

> CHANGE  14 : 15  @  21 : 22

~ 				.getCategory(jsonobject.optString("category", SoundCategory.MASTER.getCategoryName()));

> CHANGE  18 : 19  @  25 : 26

~ 			JSONArray jsonarray = jsonobject.getJSONArray("sounds");

> CHANGE  20 : 22  @  27 : 29

~ 			for (int i = 0; i < jsonarray.length(); ++i) {
~ 				Object jsonelement = jsonarray.get(i);

> CHANGE  23 : 28  @  30 : 35

~ 				if (jsonelement instanceof String) {
~ 					soundlist$soundentry.setSoundEntryName((String) jsonelement);
~ 				} else if (jsonelement instanceof JSONObject) {
~ 					JSONObject jsonobject1 = (JSONObject) jsonelement;
~ 					soundlist$soundentry.setSoundEntryName(jsonobject1.getString("name"));

> CHANGE  30 : 31  @  37 : 38

~ 								.getType(jsonobject1.getString("type"));

> CHANGE  36 : 37  @  43 : 44

~ 						float f = jsonobject1.getFloat("volume");

> CHANGE  42 : 43  @  49 : 50

~ 						float f1 = jsonobject1.getFloat("pitch");

> CHANGE  48 : 49  @  55 : 56

~ 						int j = jsonobject1.getInt("weight");

> CHANGE  54 : 55  @  61 : 62

~ 						soundlist$soundentry.setStreaming(jsonobject1.getBoolean("stream"));

> EOF
