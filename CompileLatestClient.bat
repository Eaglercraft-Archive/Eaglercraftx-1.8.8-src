@echo off
title CompileLatestClient
java -cp "buildtools/BuildTools.jar" net.lax1dude.eaglercraft.v1_8.buildtools.gui.CompileLatestClientGUI
del /S /Q "##TEAVM.TMP##\*"
rmdir /S /Q "##TEAVM.TMP##"
pause