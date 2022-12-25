
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> INSERT  5 : 6  @  4

+ import net.minecraft.client.model.ModelBiped;

> CHANGE  7 : 8  @  5 : 8

~ import net.minecraft.client.model.ModelZombie;

> INSERT  24 : 25  @  24

+ 	private boolean zombieModel;

> CHANGE  27 : 28  @  26 : 27

~ 		this(renderManager, false, false);

> CHANGE  30 : 32  @  29 : 31

~ 	public RenderPlayer(RenderManager renderManager, boolean useSmallArms, boolean zombieModel) {
~ 		super(renderManager, zombieModel ? new ModelZombie(0.0F, true) : new ModelPlayer(0.0F, useSmallArms), 0.5F);

> INSERT  33 : 34  @  32

+ 		this.zombieModel = zombieModel;

> CHANGE  42 : 44  @  40 : 42

~ 	public ModelBiped getMainModel() {
~ 		return (ModelBiped) super.getMainModel();

> CHANGE  60 : 61  @  58 : 59

~ 		ModelBiped modelplayer = this.getMainModel();

> CHANGE  69 : 77  @  67 : 72

~ 			if (!zombieModel) {
~ 				ModelPlayer modelplayer_ = (ModelPlayer) modelplayer;
~ 				modelplayer_.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
~ 				modelplayer_.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
~ 				modelplayer_.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
~ 				modelplayer_.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
~ 				modelplayer_.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
~ 			}

> CHANGE  127 : 137  @  122 : 130

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

> CHANGE  140 : 150  @  133 : 141

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
