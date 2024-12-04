@echo off
title CompileEPWBootstrapJS
set srcFolder=../src/wasm-gc-teavm-bootstrap/js
echo Compiling %srcFolder%
java -jar buildtools/closure-compiler.jar --compilation_level ADVANCED_OPTIMIZATIONS --assume_function_wrapper --emit_use_strict --isolation_mode IIFE --js "%srcFolder%/externs.js" "%srcFolder%/main.js" --js_output_file javascript_dist/bootstrap.js
pause