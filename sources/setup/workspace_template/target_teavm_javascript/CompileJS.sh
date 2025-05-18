#!/bin/sh
cd ../
chmod +x gradlew
./gradlew target_teavm_javascript:assembleMainComponents
