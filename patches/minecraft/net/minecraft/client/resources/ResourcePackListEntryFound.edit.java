
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 5

> CHANGE  23 : 24  @  23 : 24

~ 		return this.field_148319_c.getResourcePackEaglerDisplayName();

> INSERT  5 : 10  @  5

+ 
+ 	@Override
+ 	protected String getEaglerFolderName() {
+ 		return field_148319_c.getResourcePackName();
+ 	}

> EOF
