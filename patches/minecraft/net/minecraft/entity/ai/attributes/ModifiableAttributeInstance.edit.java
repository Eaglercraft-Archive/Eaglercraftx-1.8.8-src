
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 3  @  2

+ import java.util.List;

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  1 : 5  @  1

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 

> CHANGE  5 : 6  @  5 : 6

~ 	private final Map<EaglercraftUUID, AttributeModifier> mapByUUID = Maps.newHashMap();

> CHANGE  44 : 45  @  44 : 45

~ 	public AttributeModifier getModifier(EaglercraftUUID uuid) {

> CHANGE  11 : 12  @  11 : 12

~ 			Set<AttributeModifier> object = (Set) this.mapByName.get(attributemodifier.getName());

> CHANGE  38 : 39  @  38 : 39

~ 			for (AttributeModifier attributemodifier : (List<AttributeModifier>) Lists.newArrayList(collection)) {

> EOF
