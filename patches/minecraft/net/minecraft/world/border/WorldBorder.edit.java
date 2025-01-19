
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 5  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;

> DELETE  4  @  4 : 6

> INSERT  34 : 41  @  34

+ 	public boolean contains(long range) {
+ 		int chunkXPos = (int) (range & 4294967295L);
+ 		int chunkZPos = (int) (range >>> 32);
+ 		return (double) ((chunkXPos << 4) + 15) > this.minX() && (double) (chunkXPos << 4) < this.maxX()
+ 				&& (double) ((chunkZPos << 4) + 15) > this.minZ() && (double) (chunkZPos << 4) < this.maxZ();
+ 	}
+ 

> CHANGE  71 : 74  @  71 : 73

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onCenterChanged(this, x, z);

> CHANGE  6 : 7  @  6 : 7

~ 			double d0 = (double) ((float) (EagRuntime.steadyTimeMillis() - this.startTime)

> CHANGE  12 : 13  @  12 : 13

~ 		return this.getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - EagRuntime.steadyTimeMillis() : 0L;

> CHANGE  9 : 10  @  9 : 10

~ 		this.endTime = EagRuntime.steadyTimeMillis();

> CHANGE  2 : 5  @  2 : 4

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onSizeChanged(this, newSize);

> CHANGE  7 : 8  @  7 : 8

~ 		this.startTime = EagRuntime.steadyTimeMillis();

> CHANGE  2 : 5  @  2 : 4

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onTransitionStarted(this, oldSize, newSize, time);

> CHANGE  27 : 30  @  27 : 29

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onDamageBufferChanged(this, bufferSize);

> CHANGE  11 : 14  @  11 : 13

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onDamageAmountChanged(this, newAmount);

> CHANGE  16 : 19  @  16 : 18

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onWarningTimeChanged(this, warningTime);

> CHANGE  11 : 14  @  11 : 13

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onWarningDistanceChanged(this, warningDistance);

> EOF
