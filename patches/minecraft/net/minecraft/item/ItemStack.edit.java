
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  3  @  3 : 4

> INSERT  1 : 11  @  1

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
+ 
+ import java.util.Set;
+ 
+ import com.google.common.collect.HashMultimap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Multimap;
+ 

> INSERT  1 : 2  @  1

+ import net.minecraft.client.Minecraft;

> DELETE  12  @  12 : 16

> INSERT  19 : 21  @  19

+ 	private String profanityFilteredName;
+ 	private String profanityFilteredNameFiltered;

> CHANGE  166 : 167  @  166 : 167

~ 	public boolean attemptDamageItem(int amount, EaglercraftRandom rand) {

> INSERT  197 : 218  @  197

+ 	public String getDisplayNameProfanityFilter() {
+ 		String s = this.getItem().getItemStackDisplayName(this);
+ 		if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
+ 			NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
+ 			if (nbttagcompound.hasKey("Name", 8)) {
+ 				s = nbttagcompound.getString("Name");
+ 				if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 					if (!s.equals(profanityFilteredName)) {
+ 						profanityFilteredName = s;
+ 						profanityFilteredNameFiltered = ProfanityFilter.getInstance().profanityFilterString(s);
+ 					}
+ 					if (profanityFilteredNameFiltered != null) {
+ 						s = profanityFilteredNameFiltered;
+ 					}
+ 				}
+ 			}
+ 		}
+ 
+ 		return s;
+ 	}
+ 

> INSERT  36 : 44  @  36

+ 		return getTooltipImpl(playerIn, advanced, false);
+ 	}
+ 
+ 	public List<String> getTooltipProfanityFilter(EntityPlayer playerIn, boolean advanced) {
+ 		return getTooltipImpl(playerIn, advanced, true);
+ 	}
+ 
+ 	public List<String> getTooltipImpl(EntityPlayer playerIn, boolean advanced, boolean profanityFilter) {

> CHANGE  1 : 2  @  1 : 2

~ 		String s = profanityFilter ? this.getDisplayNameProfanityFilter() : this.getDisplayName();

> CHANGE  14 : 15  @  14 : 15

~ 				s = s + HString.format("#%04d/%d%s",

> CHANGE  2 : 3  @  2 : 3

~ 				s = s + HString.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });

> CHANGE  56 : 57  @  56 : 57

~ 			for (Entry entry : (Set<Entry>) multimap.entries()) {

> EOF
