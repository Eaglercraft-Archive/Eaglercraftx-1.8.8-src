
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  4 : 5  @  7

+ import java.util.HashSet;

> INSERT  8 : 9  @  10

+ import java.util.Set;

> CHANGE  10 : 19  @  11 : 13

~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.base.Splitter;
~ import com.google.common.collect.Iterables;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;

> DELETE  20  @  14 : 16

> INSERT  27 : 29  @  23

+ 	private static final Set<String> hasShownMissing = new HashSet();
+ 

> CHANGE  33 : 34  @  27 : 28

~ 			String s1 = HString.format("lang/%s.lang", new Object[] { s });

> CHANGE  37 : 45  @  31 : 32

~ 					List<IResource> res = resourceManager.getAllResources(new ResourceLocation(s2, s1));
~ 					if (res.size() > 0) {
~ 						this.loadLocaleData(res);
~ 					} else {
~ 						if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
~ 							EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
~ 						}
~ 					}

> CHANGE  46 : 49  @  33 : 34

~ 					if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
~ 						EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
~ 					}

> CHANGE  99 : 101  @  84 : 85

~ 					String s2 = pattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s" but must be
~ 																				// "%s" to work with TeaVM (why?)

> INSERT  102 : 105  @  86

+ 					if (s1.startsWith("eaglercraft.")) {
+ 						this.properties.put(s1.substring(12), s2);
+ 					}

> CHANGE  120 : 121  @  101 : 102

~ 			return HString.format(s, parameters);

> EOF
