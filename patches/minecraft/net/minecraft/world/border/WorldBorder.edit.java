
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  4  @  4 : 6

> CHANGE  105 : 108  @  105 : 107

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onCenterChanged(this, x, z);

> CHANGE  32 : 35  @  32 : 34

~ 		List<IBorderListener> lst = this.getListeners();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			lst.get(i).onSizeChanged(this, newSize);

> CHANGE  10 : 13  @  10 : 12

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
