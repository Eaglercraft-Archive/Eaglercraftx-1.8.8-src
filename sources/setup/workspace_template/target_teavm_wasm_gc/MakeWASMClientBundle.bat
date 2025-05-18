@echo off
cd ../
call gradlew target_teavm_wasm_gc:makeMainWasmClientBundle
pause