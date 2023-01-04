
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  3 : 4  @  3

+ import java.util.regex.Matcher;

> DELETE  1  @  1 : 3

> INSERT  1 : 10  @  1

+ import com.google.common.base.Charsets;
+ import com.google.common.base.Splitter;
+ import com.google.common.collect.Iterables;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.IOUtils;
+ 

> CHANGE  8 : 10  @  8 : 10

~ 		this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
~ 	}

> CHANGE  1 : 13  @  1 : 9

~ 	public static void doCLINIT() {
~ 		InputStream inputstream = EagRuntime.getResourceStream("/assets/minecraft/lang/en_US.lang");
~ 		for (String s : IOUtils.readLines(inputstream, Charsets.UTF_8)) {
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

> CHANGE  20 : 21  @  20 : 21

~ 			return HString.format(s, format);

> EOF
