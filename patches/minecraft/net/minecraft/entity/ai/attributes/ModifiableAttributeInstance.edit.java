
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 3  @  2

+ import java.util.List;

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  1 : 7  @  1

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 

> CHANGE  3 : 4  @  3 : 4

~ 	private final IntObjectMap<Set<AttributeModifier>> mapByOperation = new IntObjectHashMap<>();

> CHANGE  1 : 2  @  1 : 2

~ 	private final Map<EaglercraftUUID, AttributeModifier> mapByUUID = Maps.newHashMap();

> CHANGE  10 : 11  @  10 : 11

~ 			this.mapByOperation.put(i, Sets.newHashSet());

> CHANGE  20 : 21  @  20 : 21

~ 		return this.mapByOperation.get(i);

> CHANGE  12 : 13  @  12 : 13

~ 	public AttributeModifier getModifier(EaglercraftUUID uuid) {

> CHANGE  11 : 12  @  11 : 12

~ 			Set<AttributeModifier> object = (Set) this.mapByName.get(attributemodifier.getName());

> CHANGE  5 : 6  @  5 : 6

~ 			((Set) this.mapByOperation.get(attributemodifier.getOperation())).add(attributemodifier);

> CHANGE  13 : 14  @  13 : 14

~ 			Set set = (Set) this.mapByOperation.get(i);

> CHANGE  18 : 19  @  18 : 19

~ 			for (AttributeModifier attributemodifier : (List<AttributeModifier>) Lists.newArrayList(collection)) {

> EOF
