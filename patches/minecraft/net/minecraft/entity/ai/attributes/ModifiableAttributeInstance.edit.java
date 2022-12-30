
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 3  @  5

+ import java.util.List;

> CHANGE  3 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  2 : 6  @  6

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 

> CHANGE  9 : 10  @  5 : 6

~ 	private final Map<EaglercraftUUID, AttributeModifier> mapByUUID = Maps.newHashMap();

> CHANGE  45 : 46  @  45 : 46

~ 	public AttributeModifier getModifier(EaglercraftUUID uuid) {

> CHANGE  12 : 13  @  12 : 13

~ 			Set<AttributeModifier> object = (Set) this.mapByName.get(attributemodifier.getName());

> CHANGE  39 : 40  @  39 : 40

~ 			for (AttributeModifier attributemodifier : (List<AttributeModifier>) Lists.newArrayList(collection)) {

> EOF
