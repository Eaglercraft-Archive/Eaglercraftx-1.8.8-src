
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 9  @  5

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  2  @  2 : 6

> CHANGE  27 : 28  @  27 : 28

~ 					this.scoreObjectiveCriterias.put(criteria, (List<ScoreObjective>) object);

> CHANGE  32 : 33  @  32 : 33

~ 				this.entitiesScoreObjectives.put(name, (Map<ScoreObjective, Score>) object);

> CHANGE  61 : 62  @  61 : 62

~ 		for (Map map : (Collection<Map>) collection) {

> EOF
