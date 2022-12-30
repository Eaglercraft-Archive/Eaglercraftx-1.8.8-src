
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

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

> CHANGE  24 : 25  @  23 : 24

~ 			EaglercraftUUID uuid;

> CHANGE  2 : 3  @  2 : 3

~ 				uuid = EaglercraftUUID.fromString(s1);

> CHANGE  5 : 6  @  5 : 6

~ 			Multimap<String, Property> propertiesMap = MultimapBuilder.hashKeys().arrayListValues().build();

> DELETE  3  @  3 : 4

> CHANGE  2 : 3  @  3 : 5

~ 					for (int i = 0, l = nbttaglist.tagCount(); i < l; ++i) {

> CHANGE  2 : 10  @  3 : 9

~ 						String value = nbttagcompound1.getString("Value");
~ 						if (!StringUtils.isNullOrEmpty(value)) {
~ 							String sig = nbttagcompound1.getString("Signature");
~ 							if (!StringUtils.isNullOrEmpty(sig)) {
~ 								propertiesMap.put(s2, new Property(s2, value, sig));
~ 							} else {
~ 								propertiesMap.put(s2, new Property(s2, value));
~ 							}

> CHANGE  13 : 14  @  11 : 12

~ 			return new GameProfile(uuid, s, propertiesMap);

> CHANGE  13 : 15  @  13 : 14

~ 		Multimap<String, Property> propertiesMap = profile.getProperties();
~ 		if (!propertiesMap.isEmpty()) {

> DELETE  3  @  2 : 3

> DELETE  15  @  16 : 18

> EOF
