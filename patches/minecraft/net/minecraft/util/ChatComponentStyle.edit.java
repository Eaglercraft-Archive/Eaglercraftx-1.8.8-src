
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.util.Iterator;
+ import java.util.List;
+ 

> DELETE  3  @  3 : 9

> CHANGE  22 : 24  @  22 : 24

~ 		for (int i = 0, l = this.siblings.size(); i < l; ++i) {
~ 			this.siblings.get(i).getChatStyle().setParentStyle(this.getChatStyle());

> CHANGE  9 : 11  @  9 : 11

~ 			for (int i = 0, l = this.siblings.size(); i < l; ++i) {
~ 				this.siblings.get(i).getChatStyle().setParentStyle(this.style);

> EOF
