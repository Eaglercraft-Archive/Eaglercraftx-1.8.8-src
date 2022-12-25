@echo off
title epkcompiler
echo compiling, please wait...
java -jar "desktopRuntime/CompileEPK.jar" "desktopRuntime/resources" "javascript/assets.epk"
echo finished compiling epk
pause