@echo off
title MakeWASMClientBundle
cd javascript
java -cp "../buildtools/org.tukanni.xz.jar;../buildtools/MakeWASMClientBundle.jar;../../desktopRuntime/CompileEPK.jar;../../desktopRuntime/MakeOfflineDownload.jar" net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeWASMClientBundle epw_src.txt epw_meta.txt "../javascript_dist"
pause