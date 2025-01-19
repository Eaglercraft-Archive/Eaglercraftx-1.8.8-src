
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  4 : 5  @  4 : 5

~ 		return new PositionedSoundRecord(soundResource, 0.25F, pitch, false, ISound.AttenuationType.NONE, 0.0F, 0.0F,

> CHANGE  4 : 5  @  4 : 5

~ 		return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, ISound.AttenuationType.NONE, 0.0F, 0.0F,

> CHANGE  5 : 6  @  5 : 6

~ 		return new PositionedSoundRecord(soundResource, 4.0F, 1.0F, false, ISound.AttenuationType.LINEAR, xPosition,

> CHANGE  5 : 6  @  5 : 6

~ 		this(soundResource, volume, pitch, false, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);

> CHANGE  3 : 4  @  3 : 5

~ 			ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition) {

> DELETE  7  @  7 : 8

> EOF
