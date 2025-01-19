
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  21 : 27  @  21

+ 
+ 	private static final Pattern patternControlCodeAlternate = Pattern.compile("(?i)&([0-9A-FK-OR])");
+ 
+ 	public static String translateControlCodesAlternate(String parString1) {
+ 		return patternControlCodeAlternate.matcher(parString1).replaceAll(new String(new char[] { 0xA7, '$', '1' }));
+ 	}

> EOF
