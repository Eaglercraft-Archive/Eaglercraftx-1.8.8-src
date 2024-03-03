
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 10

> DELETE  10  @  10 : 15

> CHANGE  3 : 5  @  3 : 6

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  1 : 5  @  1

+ import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ 

> CHANGE  3 : 4  @  3 : 4

~ 	private final VFile2 statsFile;

> CHANGE  4 : 5  @  4 : 5

~ 	public StatisticsFile(MinecraftServer serverIn, VFile2 statsFileIn) {

> CHANGE  5 : 6  @  5 : 6

~ 		if (this.statsFile.exists()) {

> CHANGE  2 : 4  @  2 : 6

~ 				this.statsData.putAll(this.parseJson(this.statsFile.getAllChars()));
~ 			} catch (JSONException jsonparseexception) {

> CHANGE  7 : 8  @  7 : 13

~ 		this.statsFile.setAllChars(dumpJson(this.statsData));

> CHANGE  33 : 39  @  33 : 35

~ 		JSONObject jsonobject = null;
~ 		try {
~ 			jsonobject = new JSONObject(parString1);
~ 		} catch (JSONException ex) {
~ 		}
~ 		if (jsonobject == null) {

> DELETE  2  @  2 : 3

> CHANGE  2 : 3  @  2 : 3

~ 			for (Entry<String, Object> entry : jsonobject.toMap().entrySet()) {

> CHANGE  3 : 10  @  3 : 12

~ 					if (entry.getValue() instanceof Integer) {
~ 						tupleintjsonserializable.setIntegerValue((Integer) entry.getValue());
~ 					} else if (entry.getValue() instanceof JSONObject) {
~ 						JSONObject jsonobject1 = (JSONObject) entry.getValue();
~ 						Object value = jsonobject1.opt("value");
~ 						if (value != null && (value instanceof Integer)) {
~ 							tupleintjsonserializable.setIntegerValue(jsonobject1.getInt("value"));

> CHANGE  4 : 5  @  4 : 5

~ 								Constructor constructor = statbase.func_150954_l().getConstructor();

> CHANGE  22 : 23  @  22 : 23

~ 		JSONObject jsonobject = new JSONObject();

> CHANGE  3 : 5  @  3 : 5

~ 				JSONObject jsonobject1 = new JSONObject();
~ 				jsonobject1.put("value",

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject1.put("progress", ((TupleIntJsonSerializable) entry.getValue()).getJsonSerializableValue()

> CHANGE  6 : 7  @  6 : 7

~ 				jsonobject.put(((StatBase) entry.getKey()).statId, jsonobject1);

> CHANGE  1 : 2  @  1 : 2

~ 				jsonobject.put(((StatBase) entry.getKey()).statId,

> CHANGE  31 : 33  @  31 : 32

~ 		for (int i = 0, l = AchievementList.achievementList.size(); i < l; ++i) {
~ 			Achievement achievement = AchievementList.achievementList.get(i);

> EOF
