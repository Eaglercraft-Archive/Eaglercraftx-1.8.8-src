#!/bin/sh
cd ../
chmod +x gradlew
./gradlew target_teavm_wasm_gc:makeMainWasmClientBundle
