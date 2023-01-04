
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 9  @  2 : 8

~ import com.google.common.collect.Multimap;
~ import com.google.common.collect.MultimapBuilder;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.Property;

> CHANGE  17 : 18  @  17 : 18

~ 			EaglercraftUUID uuid;

> CHANGE  1 : 2  @  1 : 2

~ 				uuid = EaglercraftUUID.fromString(s1);

> CHANGE  4 : 5  @  4 : 5

~ 			Multimap<String, Property> propertiesMap = MultimapBuilder.hashKeys().arrayListValues().build();

> DELETE  2  @  2 : 3

> CHANGE  2 : 3  @  2 : 4

~ 					for (int i = 0, l = nbttaglist.tagCount(); i < l; ++i) {

> CHANGE  1 : 9  @  1 : 7

~ 						String value = nbttagcompound1.getString("Value");
~ 						if (!StringUtils.isNullOrEmpty(value)) {
~ 							String sig = nbttagcompound1.getString("Signature");
~ 							if (!StringUtils.isNullOrEmpty(sig)) {
~ 								propertiesMap.put(s2, new Property(s2, value, sig));
~ 							} else {
~ 								propertiesMap.put(s2, new Property(s2, value));
~ 							}

> CHANGE  5 : 6  @  5 : 6

~ 			return new GameProfile(uuid, s, propertiesMap);

> CHANGE  12 : 14  @  12 : 13

~ 		Multimap<String, Property> propertiesMap = profile.getProperties();
~ 		if (!propertiesMap.isEmpty()) {

> DELETE  1  @  1 : 2

> DELETE  15  @  15 : 17

> EOF
