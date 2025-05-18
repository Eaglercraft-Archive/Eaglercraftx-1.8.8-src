@echo off
title CompileLoaderWASM
mkdir "bin/emscripten"
call emcc -c -O3 ../src/wasm-gc-teavm-loader/c/main.c -o bin/emscripten/main.o
call emcc -c -O3 ../src/wasm-gc-teavm-loader/c/xz/xz_crc32.c -o bin/emscripten/xz_crc32.o
call emcc -c -O3 ../src/wasm-gc-teavm-loader/c/xz/xz_dec_lzma2.c -o bin/emscripten/xz_dec_lzma2.o
call emcc -c -O3 ../src/wasm-gc-teavm-loader/c/xz/xz_dec_stream.c -o bin/emscripten/xz_dec_stream.o
call emcc -O3 -sMALLOC=dlmalloc -sALLOW_MEMORY_GROWTH -sINITIAL_HEAP=16777216 -sMAXIMUM_MEMORY=67108864 --closure 1 --closure-args=--isolation_mode=IIFE --closure-args=--emit_use_strict --pre-js ../src/wasm-gc-teavm-loader/js/pre.js --js-library ../src/wasm-gc-teavm-loader/js/library.js bin/emscripten/main.o bin/emscripten/xz_crc32.o bin/emscripten/xz_dec_lzma2.o bin/emscripten/xz_dec_stream.o -o javascript/loader.js
pause