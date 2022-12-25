
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  7 : 11  @  9

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  13  @  11 : 15

> CHANGE  40 : 41  @  42 : 43

~ 					this.scoreObjectiveCriterias.put(criteria, (List<ScoreObjective>) object);

> CHANGE  73 : 74  @  75 : 76

~ 				this.entitiesScoreObjectives.put(name, (Map<ScoreObjective, Score>) object);

> CHANGE  135 : 136  @  137 : 138

~ 		for (Map map : (Collection<Map>) collection) {

> EOF
