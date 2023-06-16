
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.HString;

> CHANGE  3 : 4  @  3 : 4

~ 		super(HString.format("Error parsing: %s: %s", new Object[] { component, message }));

> CHANGE  3 : 4  @  3 : 4

~ 		super(HString.format("Invalid index %d requested for %s", new Object[] { Integer.valueOf(index), component }));

> CHANGE  3 : 4  @  3 : 4

~ 		super(HString.format("Error while parsing: %s", new Object[] { component }), cause);

> EOF
