
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> CHANGE  3 : 9  @  9 : 10

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  10  @  11 : 14

> CHANGE  12 : 13  @  16 : 19

~ 	public LanguageMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  15 : 20  @  21 : 27

~ 		for (String s : jsonobject.keySet()) {
~ 			JSONObject jsonobject1 = jsonobject.getJSONObject(s);
~ 			String s1 = jsonobject1.getString("region");
~ 			String s2 = jsonobject1.getString("name");
~ 			boolean flag = jsonobject1.optBoolean("bidirectional", false);

> CHANGE  21 : 22  @  28 : 29

~ 				throw new JSONException("Invalid language->\'" + s + "\'->region: empty value");

> CHANGE  25 : 26  @  32 : 33

~ 				throw new JSONException("Invalid language->\'" + s + "\'->name: empty value");

> CHANGE  29 : 30  @  36 : 37

~ 				throw new JSONException("Duplicate language->\'" + s + "\' defined");

> EOF
