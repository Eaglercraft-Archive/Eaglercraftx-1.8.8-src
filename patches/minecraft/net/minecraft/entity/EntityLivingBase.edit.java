
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 8  @  2

+ import com.carrotsearch.hppc.IntArrayList;
+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.carrotsearch.hppc.ObjectContainer;
+ import com.carrotsearch.hppc.cursors.IntObjectCursor;
+ import com.carrotsearch.hppc.cursors.ObjectCursor;

> CHANGE  2 : 5  @  2 : 5

~ 
~ import java.util.Arrays;
~ import java.util.Collections;

> CHANGE  1 : 3  @  1 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  3 : 4  @  3

+ import net.minecraft.client.Minecraft;

> DELETE  1  @  1 : 5

> CHANGE  39 : 41  @  39 : 40

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> CHANGE  4 : 5  @  4 : 5

~ 	private final IntObjectMap<PotionEffect> activePotionsMap = new IntObjectHashMap<>();

> DELETE  115  @  115 : 116

> DELETE  88  @  88 : 89

> CHANGE  54 : 55  @  54 : 55

~ 	public EaglercraftRandom getRNG() {

> CHANGE  46 : 49  @  46 : 47

~ 		ItemStack[] inv = this.getInventory();
~ 		for (int i = 0; i < inv.length; ++i) {
~ 			ItemStack itemstack = inv[i];

> CHANGE  7 : 9  @  7 : 8

~ 		for (int i = 0; i < inv.length; ++i) {
~ 			ItemStack itemstack1 = inv[i];

> CHANGE  8 : 10  @  8 : 10

~ 			for (ObjectCursor<PotionEffect> potioneffect : this.activePotionsMap.values()) {
~ 				nbttaglist.appendTag(potioneffect.value.writeCustomPotionEffectToNBT(new NBTTagCompound()));

> CHANGE  20 : 21  @  20 : 21

~ 					this.activePotionsMap.put(potioneffect.getPotionID(), potioneffect);

> CHANGE  23 : 24  @  23 : 24

~ 		IntArrayList deadPotionEffects = null;

> CHANGE  1 : 4  @  1 : 4

~ 		for (IntObjectCursor<PotionEffect> cur : this.activePotionsMap) {
~ 			int integer = cur.key;
~ 			PotionEffect potioneffect = cur.value;

> CHANGE  2 : 5  @  2 : 3

~ 					if (deadPotionEffects == null)
~ 						deadPotionEffects = new IntArrayList(4);
~ 					deadPotionEffects.add(integer);

> INSERT  7 : 11  @  7

+ 		if (deadPotionEffects != null) {
+ 			this.activePotionsMap.removeAll(deadPotionEffects);
+ 		}
+ 

> CHANGE  40 : 43  @  40 : 43

~ 			ObjectContainer<PotionEffect> cc = this.activePotionsMap.values();
~ 			int i = PotionHelper.calcPotionLiquidColor(cc);
~ 			this.dataWatcher.updateObject(8, Byte.valueOf((byte) (PotionHelper.getAreAmbient(cc) ? 1 : 0)));

> CHANGE  12 : 14  @  12 : 17

~ 		for (ObjectCursor<PotionEffect> cur : activePotionsMap.values()) {
~ 			PotionEffect potioneffect = cur.value;

> DELETE  1  @  1 : 2

> INSERT  4 : 7  @  4

+ 		if (!this.worldObj.isRemote) {
+ 			activePotionsMap.clear();
+ 		}

> CHANGE  2 : 3  @  2 : 3

~ 	public ObjectContainer<PotionEffect> getActivePotionEffects() {

> INSERT  3 : 16  @  3

+ 	public List<PotionEffect> getActivePotionEffectsList() {
+ 		if (activePotionsMap.isEmpty()) {
+ 			return Collections.emptyList();
+ 		}
+ 		PotionEffect[] arr = this.activePotionsMap.values().toArray(PotionEffect.class);
+ 		if (arr.length > 1) {
+ 			Arrays.sort(arr, (p1, p2) -> {
+ 				return p1.getPotionID() - p2.getPotionID();
+ 			});
+ 		}
+ 		return Arrays.asList(arr);
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		return this.activePotionsMap.containsKey(potionId);

> CHANGE  3 : 4  @  3 : 4

~ 		return this.activePotionsMap.containsKey(potionIn.id);

> CHANGE  3 : 4  @  3 : 4

~ 		return this.activePotionsMap.get(potionIn.id);

> CHANGE  4 : 7  @  4 : 9

~ 			if (this.activePotionsMap.containsKey(potioneffectIn.getPotionID())) {
~ 				this.activePotionsMap.get(potioneffectIn.getPotionID()).combine(potioneffectIn);
~ 				this.onChangedPotionEffect(this.activePotionsMap.get(potioneffectIn.getPotionID()), true);

> CHANGE  1 : 2  @  1 : 2

~ 				this.activePotionsMap.put(potioneffectIn.getPotionID(), potioneffectIn);

> CHANGE  22 : 23  @  22 : 23

~ 		this.activePotionsMap.remove(potionId);

> CHANGE  3 : 4  @  3 : 4

~ 		PotionEffect potioneffect = this.activePotionsMap.remove(potionId);

> DELETE  752  @  752 : 753

> DELETE  1  @  1 : 3

> DELETE  33  @  33 : 34

> DELETE  62  @  62 : 63

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 4

> DELETE  13  @  13 : 15

> DELETE  4  @  4 : 6

> DELETE  3  @  3 : 5

> INSERT  173 : 196  @  173

+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
+ 				isInFrustum);
+ 		Minecraft mc = Minecraft.getMinecraft();
+ 		if (mc.gameSettings.thirdPersonView != 0 || !(mc.getRenderViewEntity() == this)) {
+ 			Minecraft.getMinecraft().entityRenderer.renderHeldItemLight(this, 1.0f);
+ 		}
+ 	}
+ 
+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		float f = super.getEaglerDynamicLightsValueSimple(partialTicks);
+ 		ItemStack itm = this.getHeldItem();
+ 		if (itm != null && itm.stackSize > 0) {
+ 			Item item = itm.getItem();
+ 			if (item != null) {
+ 				float f2 = item.getHeldItemBrightnessEagler(itm);
+ 				f = Math.min(f + f2 * 0.5f, 1.0f) + f2 * 0.5f;
+ 			}
+ 		}
+ 		return f;
+ 	}

> EOF
