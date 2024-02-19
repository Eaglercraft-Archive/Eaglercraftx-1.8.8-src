
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

> CHANGE  10 : 12  @  10 : 11

~ 		return StringTranslate.fallbackInstance != null ? StringTranslate.fallbackInstance.translateKey(key)
~ 				: localizedName.translateKey(key);

> EOF
