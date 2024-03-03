
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  6  @  6 : 12

> INSERT  1 : 6  @  1

+ import com.google.common.collect.Iterators;
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> CHANGE  12 : 14  @  12 : 13

~ 		for (int i = 0; i < args.length; ++i) {
~ 			Object object = args[i];

> CHANGE  45 : 46  @  45 : 46

~ 							HString.format(format.substring(j, k), new Object[0]));

> CHANGE  25 : 26  @  25 : 26

~ 						HString.format(format.substring(j), new Object[0]));

> CHANGE  29 : 31  @  29 : 30

~ 		for (int i = 0; i < this.formatArgs.length; ++i) {
~ 			Object object = this.formatArgs[i];

> CHANGE  6 : 8  @  6 : 8

~ 			for (int i = 0, l = this.children.size(); i < l; ++i) {
~ 				this.children.get(i).getChatStyle().setParentStyle(chatstyle);

> CHANGE  15 : 17  @  15 : 17

~ 		for (int i = 0, l = this.children.size(); i < l; ++i) {
~ 			stringbuilder.append(this.children.get(i).getUnformattedTextForChat());

> CHANGE  19 : 22  @  19 : 21

~ 		List<IChatComponent> lst = this.getSiblings();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			chatcomponenttranslation.appendSibling(lst.get(i).createCopy());

> EOF
