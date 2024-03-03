
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> DELETE  5  @  5 : 11

> CHANGE  51 : 54  @  51 : 53

~ 		List<IChatComponent> lst = component.getSiblings();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			((IChatComponent) object).appendSibling(processComponent(commandSender, lst.get(i), entityIn));

> EOF
