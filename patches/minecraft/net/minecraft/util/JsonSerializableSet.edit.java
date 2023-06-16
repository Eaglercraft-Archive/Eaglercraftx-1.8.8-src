
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.Set;
+ 
+ import org.json.JSONArray;
+ 

> DELETE  2  @  2 : 7

> CHANGE  4 : 9  @  4 : 8

~ 	public void fromJson(Object jsonelement) {
~ 		if (jsonelement instanceof JSONArray) {
~ 			JSONArray arr = (JSONArray) jsonelement;
~ 			for (int i = 0; i < arr.length(); ++i) {
~ 				underlyingSet.add(arr.getString(i));

> CHANGE  5 : 7  @  5 : 7

~ 	public Object getSerializableElement() {
~ 		JSONArray jsonarray = new JSONArray();

> CHANGE  2 : 3  @  2 : 3

~ 			jsonarray.put(s);

> EOF
