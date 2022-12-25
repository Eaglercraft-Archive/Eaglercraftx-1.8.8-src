
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

> CHANGE  26 : 27  @  25 : 26

~ 			EaglercraftUUID uuid;

> CHANGE  28 : 29  @  27 : 28

~ 				uuid = EaglercraftUUID.fromString(s1);

> CHANGE  33 : 34  @  32 : 33

~ 			Multimap<String, Property> propertiesMap = MultimapBuilder.hashKeys().arrayListValues().build();

> DELETE  36  @  35 : 36

> CHANGE  38 : 39  @  38 : 40

~ 					for (int i = 0, l = nbttaglist.tagCount(); i < l; ++i) {

> CHANGE  40 : 48  @  41 : 47

~ 						String value = nbttagcompound1.getString("Value");
~ 						if (!StringUtils.isNullOrEmpty(value)) {
~ 							String sig = nbttagcompound1.getString("Signature");
~ 							if (!StringUtils.isNullOrEmpty(sig)) {
~ 								propertiesMap.put(s2, new Property(s2, value, sig));
~ 							} else {
~ 								propertiesMap.put(s2, new Property(s2, value));
~ 							}

> CHANGE  53 : 54  @  52 : 53

~ 			return new GameProfile(uuid, s, propertiesMap);

> CHANGE  66 : 68  @  65 : 66

~ 		Multimap<String, Property> propertiesMap = profile.getProperties();
~ 		if (!propertiesMap.isEmpty()) {

> DELETE  69  @  67 : 68

> DELETE  84  @  83 : 85

> EOF
