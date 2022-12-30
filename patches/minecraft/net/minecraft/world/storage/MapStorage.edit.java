
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  2 : 6  @  10 : 14

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 

> DELETE  5  @  5 : 6

> CHANGE  13 : 14  @  14 : 46

~ 		return (WorldSavedData) this.loadedDataMap.get(s);

> CHANGE  14 : 15  @  45 : 50

~ 			((WorldSavedData) this.loadedDataList.get(i)).setDirty(false);

> DELETE  5  @  9 : 29

> CHANGE  1 : 2  @  21 : 46

~ 		this.idCounts.clear();

> CHANGE  12 : 13  @  36 : 59

~ 		return oshort.shortValue();

> EOF
