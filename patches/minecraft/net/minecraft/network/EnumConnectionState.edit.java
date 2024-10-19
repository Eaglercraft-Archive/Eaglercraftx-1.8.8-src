
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 7  @  5

+ 
+ import java.util.Collection;

> CHANGE  1 : 3  @  1 : 3

~ import java.util.function.Supplier;
~ 

> CHANGE  101 : 102  @  101 : 106

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

> CHANGE  4 : 5  @  4 : 5

~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class, C00Handshake::new);

> CHANGE  4 : 158  @  4 : 104

~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketKeepAlive.class, S00PacketKeepAlive::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketJoinGame.class, S01PacketJoinGame::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketChat.class, S02PacketChat::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketTimeUpdate.class, S03PacketTimeUpdate::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S04PacketEntityEquipment.class,
~ 					S04PacketEntityEquipment::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S05PacketSpawnPosition.class,
~ 					S05PacketSpawnPosition::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S06PacketUpdateHealth.class,
~ 					S06PacketUpdateHealth::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S07PacketRespawn.class, S07PacketRespawn::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S08PacketPlayerPosLook.class,
~ 					S08PacketPlayerPosLook::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S09PacketHeldItemChange.class,
~ 					S09PacketHeldItemChange::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0APacketUseBed.class, S0APacketUseBed::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0BPacketAnimation.class, S0BPacketAnimation::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0CPacketSpawnPlayer.class, S0CPacketSpawnPlayer::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0DPacketCollectItem.class, S0DPacketCollectItem::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0EPacketSpawnObject.class, S0EPacketSpawnObject::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0FPacketSpawnMob.class, S0FPacketSpawnMob::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S10PacketSpawnPainting.class,
~ 					S10PacketSpawnPainting::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S11PacketSpawnExperienceOrb.class,
~ 					S11PacketSpawnExperienceOrb::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S12PacketEntityVelocity.class,
~ 					S12PacketEntityVelocity::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S13PacketDestroyEntities.class,
~ 					S13PacketDestroyEntities::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.class, S14PacketEntity::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S15PacketEntityRelMove.class,
~ 					S14PacketEntity.S15PacketEntityRelMove::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S16PacketEntityLook.class,
~ 					S14PacketEntity.S16PacketEntityLook::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S17PacketEntityLookMove.class,
~ 					S14PacketEntity.S17PacketEntityLookMove::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S18PacketEntityTeleport.class,
~ 					S18PacketEntityTeleport::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityHeadLook.class,
~ 					S19PacketEntityHeadLook::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityStatus.class,
~ 					S19PacketEntityStatus::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1BPacketEntityAttach.class,
~ 					S1BPacketEntityAttach::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1CPacketEntityMetadata.class,
~ 					S1CPacketEntityMetadata::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1DPacketEntityEffect.class,
~ 					S1DPacketEntityEffect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1EPacketRemoveEntityEffect.class,
~ 					S1EPacketRemoveEntityEffect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1FPacketSetExperience.class,
~ 					S1FPacketSetExperience::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S20PacketEntityProperties.class,
~ 					S20PacketEntityProperties::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S21PacketChunkData.class, S21PacketChunkData::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S22PacketMultiBlockChange.class,
~ 					S22PacketMultiBlockChange::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S23PacketBlockChange.class, S23PacketBlockChange::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S24PacketBlockAction.class, S24PacketBlockAction::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S25PacketBlockBreakAnim.class,
~ 					S25PacketBlockBreakAnim::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S26PacketMapChunkBulk.class,
~ 					S26PacketMapChunkBulk::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S27PacketExplosion.class, S27PacketExplosion::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S28PacketEffect.class, S28PacketEffect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S29PacketSoundEffect.class, S29PacketSoundEffect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2APacketParticles.class, S2APacketParticles::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2BPacketChangeGameState.class,
~ 					S2BPacketChangeGameState::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2CPacketSpawnGlobalEntity.class,
~ 					S2CPacketSpawnGlobalEntity::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2DPacketOpenWindow.class, S2DPacketOpenWindow::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2EPacketCloseWindow.class, S2EPacketCloseWindow::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2FPacketSetSlot.class, S2FPacketSetSlot::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S30PacketWindowItems.class, S30PacketWindowItems::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S31PacketWindowProperty.class,
~ 					S31PacketWindowProperty::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S32PacketConfirmTransaction.class,
~ 					S32PacketConfirmTransaction::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S33PacketUpdateSign.class, S33PacketUpdateSign::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S34PacketMaps.class, S34PacketMaps::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S35PacketUpdateTileEntity.class,
~ 					S35PacketUpdateTileEntity::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S36PacketSignEditorOpen.class,
~ 					S36PacketSignEditorOpen::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S37PacketStatistics.class, S37PacketStatistics::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S38PacketPlayerListItem.class,
~ 					S38PacketPlayerListItem::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S39PacketPlayerAbilities.class,
~ 					S39PacketPlayerAbilities::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3APacketTabComplete.class, S3APacketTabComplete::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3BPacketScoreboardObjective.class,
~ 					S3BPacketScoreboardObjective::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3CPacketUpdateScore.class, S3CPacketUpdateScore::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3DPacketDisplayScoreboard.class,
~ 					S3DPacketDisplayScoreboard::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3EPacketTeams.class, S3EPacketTeams::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3FPacketCustomPayload.class,
~ 					S3FPacketCustomPayload::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S40PacketDisconnect.class, S40PacketDisconnect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S41PacketServerDifficulty.class,
~ 					S41PacketServerDifficulty::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S42PacketCombatEvent.class, S42PacketCombatEvent::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S43PacketCamera.class, S43PacketCamera::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S44PacketWorldBorder.class, S44PacketWorldBorder::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S45PacketTitle.class, S45PacketTitle::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S46PacketSetCompressionLevel.class,
~ 					S46PacketSetCompressionLevel::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S47PacketPlayerListHeaderFooter.class,
~ 					S47PacketPlayerListHeaderFooter::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S48PacketResourcePackSend.class,
~ 					S48PacketResourcePackSend::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S49PacketUpdateEntityNBT.class,
~ 					S49PacketUpdateEntityNBT::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketKeepAlive.class, C00PacketKeepAlive::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketChatMessage.class, C01PacketChatMessage::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C02PacketUseEntity.class, C02PacketUseEntity::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.class, C03PacketPlayer::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C04PacketPlayerPosition.class,
~ 					C03PacketPlayer.C04PacketPlayerPosition::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C05PacketPlayerLook.class,
~ 					C03PacketPlayer.C05PacketPlayerLook::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C06PacketPlayerPosLook.class,
~ 					C03PacketPlayer.C06PacketPlayerPosLook::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C07PacketPlayerDigging.class,
~ 					C07PacketPlayerDigging::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C08PacketPlayerBlockPlacement.class,
~ 					C08PacketPlayerBlockPlacement::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C09PacketHeldItemChange.class,
~ 					C09PacketHeldItemChange::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0APacketAnimation.class, C0APacketAnimation::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0BPacketEntityAction.class,
~ 					C0BPacketEntityAction::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0CPacketInput.class, C0CPacketInput::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0DPacketCloseWindow.class, C0DPacketCloseWindow::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0EPacketClickWindow.class, C0EPacketClickWindow::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0FPacketConfirmTransaction.class,
~ 					C0FPacketConfirmTransaction::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C10PacketCreativeInventoryAction.class,
~ 					C10PacketCreativeInventoryAction::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C11PacketEnchantItem.class, C11PacketEnchantItem::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C12PacketUpdateSign.class, C12PacketUpdateSign::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C13PacketPlayerAbilities.class,
~ 					C13PacketPlayerAbilities::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C14PacketTabComplete.class, C14PacketTabComplete::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C15PacketClientSettings.class,
~ 					C15PacketClientSettings::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C16PacketClientStatus.class,
~ 					C16PacketClientStatus::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C17PacketCustomPayload.class,
~ 					C17PacketCustomPayload::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C18PacketSpectate.class, C18PacketSpectate::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C19PacketResourcePackStatus.class,
~ 					C19PacketResourcePackStatus::new);

> DELETE  2  @  2 : 10

> CHANGE  2 : 12  @  2 : 8

~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketDisconnect.class, S00PacketDisconnect::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketEncryptionRequest.class,
~ 					S01PacketEncryptionRequest::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketLoginSuccess.class,
~ 					S02PacketLoginSuccess::new);
~ 			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketEnableCompression.class,
~ 					S03PacketEnableCompression::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketLoginStart.class, C00PacketLoginStart::new);
~ 			this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketEncryptionResponse.class,
~ 					C01PacketEncryptionResponse::new);

> INSERT  10 : 11  @  10

+ 	private final Map<EnumPacketDirection, Map<Integer, Supplier<Packet<?>>>> directionCtors;

> INSERT  3 : 4  @  3

+ 		this.directionCtors = Maps.newEnumMap(EnumPacketDirection.class);

> CHANGE  3 : 7  @  3 : 5

~ 	protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass,
~ 			Supplier<Packet<?>> packetCtor) {
~ 		BiMap<Integer, Class<? extends Packet>> object = this.directionMaps.get(direction);
~ 		Map<Integer, Supplier<Packet<?>>> object2;

> INSERT  2 : 3  @  2

+ 			object2 = Maps.newHashMap();

> INSERT  1 : 4  @  1

+ 			this.directionCtors.put(direction, object2);
+ 		} else {
+ 			object2 = this.directionCtors.get(direction);

> CHANGE  2 : 3  @  2 : 3

~ 		if (object.containsValue(packetClass)) {

> CHANGE  1 : 2  @  1 : 2

~ 					+ object.inverse().get(packetClass);

> CHANGE  3 : 5  @  3 : 4

~ 			object.put(Integer.valueOf(object.size()), packetClass);
~ 			object2.put(Integer.valueOf(object2.size()), packetCtor);

> CHANGE  10 : 12  @  10 : 12

~ 		Supplier<Packet<?>> oclass = this.directionCtors.get(direction).get(Integer.valueOf(packetId));
~ 		return oclass == null ? null : oclass.get();

> CHANGE  15 : 18  @  15 : 16

~ 		EnumConnectionState[] states = values();
~ 		for (int j = 0; j < states.length; ++j) {
~ 			EnumConnectionState enumconnectionstate = states[j];

> CHANGE  8 : 10  @  8 : 9

~ 				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
~ 						.get(enumpacketdirection)).values()) {

> DELETE  5  @  5 : 11

> EOF
