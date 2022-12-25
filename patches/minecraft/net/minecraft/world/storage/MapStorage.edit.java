
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  4 : 8  @  12 : 16

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 

> DELETE  9  @  17 : 18

> CHANGE  22 : 23  @  31 : 63

~ 		return (WorldSavedData) this.loadedDataMap.get(s);

> CHANGE  36 : 37  @  76 : 81

~ 			((WorldSavedData) this.loadedDataList.get(i)).setDirty(false);

> DELETE  41  @  85 : 105

> CHANGE  42 : 43  @  106 : 131

~ 		this.idCounts.clear();

> CHANGE  54 : 55  @  142 : 165

~ 		return oshort.shortValue();

> EOF
