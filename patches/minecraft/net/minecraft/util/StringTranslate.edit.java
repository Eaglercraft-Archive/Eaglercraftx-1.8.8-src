
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 11  @  5

+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.IOUtils;
+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
+ 

> INSERT  2 : 4  @  2

+ import java.nio.charset.StandardCharsets;
+ import java.util.ArrayList;

> INSERT  1 : 2  @  1

+ import java.util.List;

> INSERT  1 : 2  @  1

+ import java.util.Map.Entry;

> DELETE  1  @  1 : 3

> INSERT  5 : 6  @  5

+ 	static StringTranslate fallbackInstance = null;

> CHANGE  3 : 5  @  3 : 6

~ 	private StringTranslate() {
~ 	}

> CHANGE  1 : 25  @  1 : 9

~ 	public static void initClient() {
~ 		try (InputStream inputstream = EagRuntime.getRequiredResourceStream("/assets/minecraft/lang/en_US.lang")) {
~ 			initServer(IOUtils.readLines(inputstream, StandardCharsets.UTF_8));
~ 			fallbackInstance = new StringTranslate();
~ 			fallbackInstance.replaceWith(instance.languageList);
~ 			SingleplayerServerController.updateLocale(dump());
~ 		} catch (IOException e) {
~ 			EagRuntime.debugPrintStackTrace(e);
~ 		}
~ 	}
~ 
~ 	public static void initServer(List<String> strs) {
~ 		instance.languageList.clear();
~ 		for (int i = 0, l = strs.size(); i < l; ++i) {
~ 			String s = strs.get(i);
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

+ 		instance.lastUpdateTimeInMilliseconds = EagRuntime.steadyTimeMillis();

> CHANGE  6 : 7  @  6 : 7

~ 	public static void replaceWith(Map<String, String> parMap) {

> CHANGE  2 : 3  @  2 : 3

~ 		instance.lastUpdateTimeInMilliseconds = EagRuntime.steadyTimeMillis();

> CHANGE  2 : 3  @  2 : 3

~ 	public String translateKey(String key) {

> CHANGE  3 : 4  @  3 : 4

~ 	public String translateKeyFormat(String key, Object... format) {

> CHANGE  3 : 4  @  3 : 4

~ 			return HString.format(s, format);

> CHANGE  10 : 11  @  10 : 11

~ 	public boolean isKeyTranslated(String key) {

> INSERT  6 : 14  @  6

+ 
+ 	public static List<String> dump() {
+ 		List<String> ret = new ArrayList(instance.languageList.size());
+ 		for (Entry<String, String> etr : instance.languageList.entrySet()) {
+ 			ret.add(etr.getKey() + "=" + etr.getValue());
+ 		}
+ 		return ret;
+ 	}

> EOF
