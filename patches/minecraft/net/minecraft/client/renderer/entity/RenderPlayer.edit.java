
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> INSERT  3 : 4  @  2

+ import net.minecraft.client.model.ModelBiped;

> CHANGE  2 : 3  @  1 : 4

~ import net.minecraft.client.model.ModelZombie;

> INSERT  17 : 18  @  19

+ 	private boolean zombieModel;

> CHANGE  3 : 4  @  2 : 3

~ 		this(renderManager, false, false);

> CHANGE  3 : 5  @  3 : 5

~ 	public RenderPlayer(RenderManager renderManager, boolean useSmallArms, boolean zombieModel) {
~ 		super(renderManager, zombieModel ? new ModelZombie(0.0F, true) : new ModelPlayer(0.0F, useSmallArms), 0.5F);

> INSERT  3 : 4  @  3

+ 		this.zombieModel = zombieModel;

> CHANGE  9 : 11  @  8 : 10

~ 	public ModelBiped getMainModel() {
~ 		return (ModelBiped) super.getMainModel();

> CHANGE  18 : 19  @  18 : 19

~ 		ModelBiped modelplayer = this.getMainModel();

> CHANGE  9 : 17  @  9 : 14

~ 			if (!zombieModel) {
~ 				ModelPlayer modelplayer_ = (ModelPlayer) modelplayer;
~ 				modelplayer_.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
~ 				modelplayer_.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
~ 				modelplayer_.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
~ 				modelplayer_.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
~ 				modelplayer_.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
~ 			}

> CHANGE  58 : 68  @  55 : 63

~ 		if (!zombieModel) {
~ 			float f = 1.0F;
~ 			GlStateManager.color(f, f, f);
~ 			ModelBiped modelplayer = this.getMainModel();
~ 			this.setModelVisibilities(clientPlayer);
~ 			modelplayer.swingProgress = 0.0F;
~ 			modelplayer.isSneak = false;
~ 			modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
~ 			((ModelPlayer) modelplayer).renderRightArm();
~ 		}

> CHANGE  13 : 23  @  11 : 19

~ 		if (!zombieModel) {
~ 			float f = 1.0F;
~ 			GlStateManager.color(f, f, f);
~ 			ModelBiped modelplayer = this.getMainModel();
~ 			this.setModelVisibilities(clientPlayer);
~ 			modelplayer.isSneak = false;
~ 			modelplayer.swingProgress = 0.0F;
~ 			modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
~ 			((ModelPlayer) modelplayer).renderLeftArm();
~ 		}

> EOF
