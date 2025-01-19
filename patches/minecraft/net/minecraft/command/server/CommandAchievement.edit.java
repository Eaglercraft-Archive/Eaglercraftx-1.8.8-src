
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  20 : 21  @  20

+ 

> CHANGE  29 : 31  @  29 : 31

~ 							for (int i = 0, l = AchievementList.achievementList.size(); i < l; ++i) {
~ 								entityplayermp.triggerAchievement(AchievementList.achievementList.get(i));

> CHANGE  5 : 8  @  5 : 7

~ 							List<Achievement> ach = Lists.reverse(AchievementList.achievementList);
~ 							for (int i = 0, l = ach.size(); i < l; ++i) {
~ 								entityplayermp.func_175145_a(ach.get(i));

> CHANGE  22 : 25  @  22 : 24

~ 								List<Achievement> ach = Lists.reverse(AchievementList.achievementList);
~ 								for (int i = 0, l = ach.size(); i < l; ++i) {
~ 									entityplayermp.triggerAchievement(ach.get(i));

> CHANGE  16 : 18  @  16 : 17

~ 								for (int i = 0, l = arraylist1.size(); i < l; ++i) {
~ 									Achievement achievement2 = (Achievement) arraylist1.get(i);

> CHANGE  16 : 18  @  16 : 18

~ 								for (int i = 0, l = arraylist2.size(); i < l; ++i) {
~ 									entityplayermp.func_175145_a((Achievement) arraylist2.get(i));

> CHANGE  30 : 32  @  30 : 32

~ 			for (int i = 0, l = StatList.allStats.size(); i < l; ++i) {
~ 				arraylist.add(StatList.allStats.get(i).statId);

> EOF
