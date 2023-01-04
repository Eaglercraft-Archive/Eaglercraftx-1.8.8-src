
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> CHANGE  1 : 7  @  1 : 2

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  1  @  1 : 4

> CHANGE  2 : 3  @  2 : 5

~ 	public LanguageMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  2 : 7  @  2 : 8

~ 		for (String s : jsonobject.keySet()) {
~ 			JSONObject jsonobject1 = jsonobject.getJSONObject(s);
~ 			String s1 = jsonobject1.getString("region");
~ 			String s2 = jsonobject1.getString("name");
~ 			boolean flag = jsonobject1.optBoolean("bidirectional", false);

> CHANGE  1 : 2  @  1 : 2

~ 				throw new JSONException("Invalid language->\'" + s + "\'->region: empty value");

> CHANGE  3 : 4  @  3 : 4

~ 				throw new JSONException("Invalid language->\'" + s + "\'->name: empty value");

> CHANGE  3 : 4  @  3 : 4

~ 				throw new JSONException("Duplicate language->\'" + s + "\' defined");

> EOF
