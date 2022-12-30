
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 9  @  7

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  6  @  2 : 6

> CHANGE  27 : 28  @  31 : 32

~ 					this.scoreObjectiveCriterias.put(criteria, (List<ScoreObjective>) object);

> CHANGE  33 : 34  @  33 : 34

~ 				this.entitiesScoreObjectives.put(name, (Map<ScoreObjective, Score>) object);

> CHANGE  62 : 63  @  62 : 63

~ 		for (Map map : (Collection<Map>) collection) {

> EOF
