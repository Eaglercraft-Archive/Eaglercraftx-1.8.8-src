
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 9  @  5

+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.IOUtils;
+ 

> INSERT  2 : 3  @  2

+ import java.nio.charset.StandardCharsets;

> INSERT  1 : 2  @  1

+ import java.util.List;

> DELETE  2  @  2 : 4

> CHANGE  15 : 17  @  15 : 18

~ 	private StringTranslate() {
~ 	}

> CHANGE  1 : 13  @  1 : 9

~ 	public static void init(List<String> strs) {
~ 		instance.languageList.clear();
~ 		for (String s : strs) {
~ 			if (!s.isEmpty() && s.charAt(0) != 35) {
~ 				String[] astring = (String[]) Iterables.toArray(equalSignSplitter.split(s), String.class);
~ 				if (astring != null && astring.length == 2) {
~ 					String s1 = astring[0];
~ 					String s2 = numericVariablePattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s"
~ 																								// but must be "%s" to
~ 																								// work with TeaVM
~ 																								// (why?)
~ 					instance.languageList.put(s1, s2);

> DELETE  2  @  2 : 6

> INSERT  2 : 3  @  2

+ 		instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();

> CHANGE  6 : 7  @  6 : 7

~ 	public static void replaceWith(Map<String, String> parMap) {

> CHANGE  5 : 6  @  5 : 6

~ 	public String translateKey(String key) {

> CHANGE  3 : 4  @  3 : 4

~ 	public String translateKeyFormat(String key, Object... format) {

> CHANGE  3 : 4  @  3 : 4

~ 			return HString.format(s, format);

> CHANGE  10 : 11  @  10 : 11

~ 	public boolean isKeyTranslated(String key) {

> EOF
