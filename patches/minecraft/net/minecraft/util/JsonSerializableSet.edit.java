
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.Set;
+ 
+ import org.json.JSONArray;
+ 

> DELETE  6  @  2 : 7

> CHANGE  4 : 9  @  9 : 13

~ 	public void fromJson(Object jsonelement) {
~ 		if (jsonelement instanceof JSONArray) {
~ 			JSONArray arr = (JSONArray) jsonelement;
~ 			for (int i = 0; i < arr.length(); ++i) {
~ 				underlyingSet.add(arr.getString(i));

> CHANGE  10 : 12  @  9 : 11

~ 	public Object getSerializableElement() {
~ 		JSONArray jsonarray = new JSONArray();

> CHANGE  4 : 5  @  4 : 5

~ 			jsonarray.put(s);

> EOF
