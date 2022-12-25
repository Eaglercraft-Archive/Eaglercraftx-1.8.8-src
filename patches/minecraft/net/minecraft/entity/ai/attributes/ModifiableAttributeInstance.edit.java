
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  4 : 5  @  7

+ import java.util.List;

> CHANGE  7 : 8  @  9 : 14

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  9 : 13  @  15

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 

> CHANGE  18 : 19  @  20 : 21

~ 	private final Map<EaglercraftUUID, AttributeModifier> mapByUUID = Maps.newHashMap();

> CHANGE  63 : 64  @  65 : 66

~ 	public AttributeModifier getModifier(EaglercraftUUID uuid) {

> CHANGE  75 : 76  @  77 : 78

~ 			Set<AttributeModifier> object = (Set) this.mapByName.get(attributemodifier.getName());

> CHANGE  114 : 115  @  116 : 117

~ 			for (AttributeModifier attributemodifier : (List<AttributeModifier>) Lists.newArrayList(collection)) {

> EOF
