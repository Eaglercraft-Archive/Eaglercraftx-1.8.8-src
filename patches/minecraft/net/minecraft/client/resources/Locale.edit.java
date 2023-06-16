
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 3  @  2

+ import java.util.HashSet;

> INSERT  3 : 4  @  3

+ import java.util.Set;

> CHANGE  1 : 10  @  1 : 3

~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.base.Splitter;
~ import com.google.common.collect.Iterables;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;

> DELETE  1  @  1 : 3

> INSERT  7 : 9  @  7

+ 	private static final Set<String> hasShownMissing = new HashSet();
+ 

> CHANGE  4 : 5  @  4 : 5

~ 			String s1 = HString.format("lang/%s.lang", new Object[] { s });

> CHANGE  3 : 11  @  3 : 4

~ 					List<IResource> res = resourceManager.getAllResources(new ResourceLocation(s2, s1));
~ 					if (res.size() > 0) {
~ 						this.loadLocaleData(res);
~ 					} else {
~ 						if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
~ 							EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
~ 						}
~ 					}

> CHANGE  1 : 4  @  1 : 2

~ 					if (s2.equalsIgnoreCase("minecraft") && hasShownMissing.add(s)) {
~ 						EagRuntime.showPopup("ERROR: language \"" + s + "\" is not available on this site!");
~ 					}

> CHANGE  50 : 52  @  50 : 51

~ 					String s2 = pattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s" but must be
~ 																				// "%s" to work with TeaVM (why?)

> INSERT  1 : 4  @  1

+ 					if (s1.startsWith("eaglercraft.")) {
+ 						this.properties.put(s1.substring(12), s2);
+ 					}

> CHANGE  15 : 16  @  15 : 16

~ 			return HString.format(s, parameters);

> EOF
