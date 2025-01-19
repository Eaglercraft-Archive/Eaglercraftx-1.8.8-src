
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 6  @  1 : 3

~ 
~ import com.carrotsearch.hppc.ObjectIntHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
~ import com.carrotsearch.hppc.cursors.ObjectIntCursor;
~ 

> CHANGE  7 : 8  @  7 : 8

~ 	private ObjectIntMap<StatBase> field_148976_a;

> CHANGE  4 : 5  @  4 : 5

~ 	public S37PacketStatistics(ObjectIntMap<StatBase> parMap) {

> CHANGE  9 : 10  @  9 : 10

~ 		this.field_148976_a = new ObjectIntHashMap<>();

> CHANGE  5 : 6  @  5 : 6

~ 				this.field_148976_a.put(statbase, k);

> CHANGE  8 : 11  @  8 : 11

~ 		for (ObjectIntCursor<StatBase> entry : this.field_148976_a) {
~ 			parPacketBuffer.writeString(entry.key.statId);
~ 			parPacketBuffer.writeVarIntToBuffer(entry.value);

> CHANGE  4 : 5  @  4 : 5

~ 	public ObjectIntMap<StatBase> func_148974_c() {

> EOF
