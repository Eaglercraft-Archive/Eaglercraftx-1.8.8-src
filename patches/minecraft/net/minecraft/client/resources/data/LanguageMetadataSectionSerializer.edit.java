
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> CHANGE  1 : 7  @  7 : 8

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  7  @  2 : 5

> CHANGE  2 : 3  @  5 : 8

~ 	public LanguageMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  3 : 8  @  5 : 11

~ 		for (String s : jsonobject.keySet()) {
~ 			JSONObject jsonobject1 = jsonobject.getJSONObject(s);
~ 			String s1 = jsonobject1.getString("region");
~ 			String s2 = jsonobject1.getString("name");
~ 			boolean flag = jsonobject1.optBoolean("bidirectional", false);

> CHANGE  6 : 7  @  7 : 8

~ 				throw new JSONException("Invalid language->\'" + s + "\'->region: empty value");

> CHANGE  4 : 5  @  4 : 5

~ 				throw new JSONException("Invalid language->\'" + s + "\'->name: empty value");

> CHANGE  4 : 5  @  4 : 5

~ 				throw new JSONException("Duplicate language->\'" + s + "\' defined");

> EOF
