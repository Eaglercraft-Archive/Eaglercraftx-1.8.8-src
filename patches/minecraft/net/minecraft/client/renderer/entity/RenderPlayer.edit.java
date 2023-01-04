
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> INSERT  2 : 3  @  2

+ import net.minecraft.client.model.ModelBiped;

> CHANGE  1 : 2  @  1 : 4

~ import net.minecraft.client.model.ModelZombie;

> INSERT  16 : 17  @  16

+ 	private boolean zombieModel;

> CHANGE  2 : 3  @  2 : 3

~ 		this(renderManager, false, false);

> CHANGE  2 : 4  @  2 : 4

~ 	public RenderPlayer(RenderManager renderManager, boolean useSmallArms, boolean zombieModel) {
~ 		super(renderManager, zombieModel ? new ModelZombie(0.0F, true) : new ModelPlayer(0.0F, useSmallArms), 0.5F);

> INSERT  1 : 2  @  1

+ 		this.zombieModel = zombieModel;

> CHANGE  8 : 10  @  8 : 10

~ 	public ModelBiped getMainModel() {
~ 		return (ModelBiped) super.getMainModel();

> CHANGE  16 : 17  @  16 : 17

~ 		ModelBiped modelplayer = this.getMainModel();

> CHANGE  8 : 16  @  8 : 13

~ 			if (!zombieModel) {
~ 				ModelPlayer modelplayer_ = (ModelPlayer) modelplayer;
~ 				modelplayer_.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
~ 				modelplayer_.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
~ 				modelplayer_.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
~ 				modelplayer_.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
~ 				modelplayer_.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
~ 			}

> CHANGE  50 : 60  @  50 : 58

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

> CHANGE  3 : 13  @  3 : 11

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
