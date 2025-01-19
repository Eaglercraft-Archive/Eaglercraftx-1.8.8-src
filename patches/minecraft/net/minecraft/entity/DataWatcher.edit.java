
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 13  @  3 : 6

~ 
~ import org.apache.commons.lang3.ObjectUtils;
~ 
~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;
~ import com.carrotsearch.hppc.ObjectIntHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
~ import com.carrotsearch.hppc.cursors.ObjectCursor;
~ import com.google.common.collect.Lists;
~ 

> DELETE  2  @  2 : 3

> DELETE  5  @  5 : 6

> CHANGE  4 : 6  @  4 : 6

~ 	private static final ObjectIntMap<Class<?>> dataTypes = new ObjectIntHashMap<>();
~ 	private final IntObjectMap<DataWatcher.WatchableObject> watchedObjects = new IntObjectHashMap<>();

> DELETE  1  @  1 : 2

> CHANGE  6 : 8  @  6 : 8

~ 		int integer = dataTypes.getOrDefault(object.getClass(), -1);
~ 		if (integer == -1) {

> CHANGE  3 : 4  @  3 : 4

~ 		} else if (this.watchedObjects.containsKey(id)) {

> CHANGE  2 : 5  @  2 : 7

~ 			DataWatcher.WatchableObject datawatcher$watchableobject = new DataWatcher.WatchableObject(integer, id,
~ 					object);
~ 			this.watchedObjects.put(id, datawatcher$watchableobject);

> CHANGE  7 : 8  @  7 : 10

~ 		this.watchedObjects.put(id, datawatcher$watchableobject);

> DELETE  28  @  28 : 30

> CHANGE  2 : 3  @  2 : 3

~ 			datawatcher$watchableobject = (DataWatcher.WatchableObject) this.watchedObjects.get(id);

> DELETE  6  @  6 : 8

> CHANGE  30 : 32  @  30 : 32

~ 			for (int i = 0, l = objectsList.size(); i < l; ++i) {
~ 				writeWatchableObjectToPacketBuffer(buffer, objectsList.get(i));

> CHANGE  9 : 12  @  9 : 12

~ 			for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject_ : this.watchedObjects
~ 					.values()) {
~ 				DataWatcher.WatchableObject datawatcher$watchableobject = datawatcher$watchableobject_.value;

> DELETE  9  @  9 : 11

> CHANGE  7 : 9  @  7 : 11

~ 		for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject : this.watchedObjects.values()) {
~ 			writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject.value);

> DELETE  1  @  1 : 3

> DELETE  5  @  5 : 6

> CHANGE  1 : 2  @  1 : 2

~ 		for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject : this.watchedObjects.values()) {

> CHANGE  4 : 5  @  4 : 5

~ 			arraylist.add(datawatcher$watchableobject.value);

> DELETE  2  @  2 : 3

> DELETE  93  @  93 : 94

> CHANGE  1 : 3  @  1 : 2

~ 		for (int i = 0, l = parList.size(); i < l; ++i) {
~ 			DataWatcher.WatchableObject datawatcher$watchableobject = parList.get(i);

> CHANGE  1 : 2  @  1 : 2

~ 					.get(datawatcher$watchableobject.getDataValueId());

> DELETE  6  @  6 : 7

> CHANGE  12 : 20  @  12 : 20

~ 		dataTypes.put(Byte.class, 0);
~ 		dataTypes.put(Short.class, 1);
~ 		dataTypes.put(Integer.class, 2);
~ 		dataTypes.put(Float.class, 3);
~ 		dataTypes.put(String.class, 4);
~ 		dataTypes.put(ItemStack.class, 5);
~ 		dataTypes.put(BlockPos.class, 6);
~ 		dataTypes.put(Rotations.class, 7);

> EOF
