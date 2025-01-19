
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import com.carrotsearch.hppc.cursors.ObjectCursor;

> DELETE  5  @  5 : 10

> CHANGE  51 : 53  @  51 : 53

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  53 : 54  @  53 : 54

~ 	public void updatePlayerList(List<EntityPlayer> parList, int trackingDistanceMax) {

> CHANGE  8 : 9  @  8 : 9

~ 			this.updatePlayerEntities(parList, trackingDistanceMax);

> CHANGE  15 : 17  @  15 : 17

~ 				for (int i = 0, l = parList.size(); i < l; ++i) {
~ 					EntityPlayerMP entityplayermp = (EntityPlayerMP) parList.get(i);

> CHANGE  171 : 172  @  171 : 172

~ 	public void updatePlayerEntity(EntityPlayerMP playerMP, int trackingDistanceMax) {

> CHANGE  1 : 2  @  1 : 2

~ 			if (this.func_180233_c(playerMP, trackingDistanceMax)) {

> INSERT  64 : 69  @  64

+ 					this.lastHeadMotion = MathHelper
+ 							.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
+ 					playerMP.playerNetServerHandler
+ 							.sendPacket(new S19PacketEntityHeadLook(this.trackedEntity, (byte) lastHeadMotion));
+ 

> CHANGE  3 : 4  @  3 : 4

~ 						for (ObjectCursor<PotionEffect> potioneffect : entitylivingbase.getActivePotionEffects()) {

> CHANGE  1 : 2  @  1 : 2

~ 									new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), potioneffect.value));

> CHANGE  11 : 13  @  11 : 12

~ 	public boolean func_180233_c(EntityPlayerMP playerMP, int trackingDistanceMax) {
~ 		int i = trackingDistanceThreshold > trackingDistanceMax ? trackingDistanceMax : trackingDistanceThreshold;

> CHANGE  2 : 3  @  2 : 4

~ 		return d0 >= (double) (-i) && d0 <= (double) i && d1 >= (double) (-i) && d1 <= (double) i

> CHANGE  8 : 9  @  8 : 9

~ 	public void updatePlayerEntities(List<EntityPlayer> parList, int trackingDistanceMax) {

> CHANGE  1 : 2  @  1 : 2

~ 			this.updatePlayerEntity((EntityPlayerMP) parList.get(i), trackingDistanceMax);

> CHANGE  56 : 58  @  56 : 57

~ 				s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, b0,
~ 						this.trackedEntity.getEntityId());

> EOF
