
# EaglercraftX WASM-GC Runtime

This folder contains the Gradle project for compiling the EaglercraftX 1.8 client to WASM. This requires a special fork of TeaVM that has been modified for Eaglercraft. The `settings.gradle` and `build.gradle` are set up to download the binaries automatically but if you would like to build the TeaVM fork yourself you can use the TeaVM fork's `publishToMavenLocal` gradle task and replace the URL maven repository declarations in the gradle build scripts with `mavenLocal()` instead.

**TeaVM Fork: [https://github.com/Eaglercraft-TeaVM-Fork/eagler-teavm/tree/eagler-r1](https://github.com/Eaglercraft-TeaVM-Fork/eagler-teavm/tree/eagler-r1)**

### To compile the client:
1. Run `CompileEPK` to compile the assets.epk file
2. Run `CompileWASM` to compile the classes.wasm file
3. Run `CompileEagRuntimeJS` to compile the eagruntime.js file
4. Run `MakeWASMClientBundle` to bundle the client into an EPW file

The final assets.epw and offline download will be in the "javascript_dist" folder

### Optional Steps:
- Run `CompileBootstrapJS` to recompile bootstrap.js in the javascript_dist folder
- Run `CompileLoaderWASM`to recompile loader.js and loader.wasm (requires emscripten)

### Potential issues when porting:
- Disabling VSync causes bad input lag, the solution to this problem is to remove the vsync option and force people to play with vsync enabled, like all other browser games
- TeaVM's WASM GC backend is still under development and will sometimes generate broken code with nested try/finally statements in a try/catch block, or other strange runtime glitches
- Fewer reflection features are supported in WASM GC than the JavaScript backend (so far)
- Do not use `@Async` or any sort of callback (like addEventListener) in your Java, you must implement async functions in JavaScript in `../src/wasm-gc-teavm/js`, using JSPI to suspend/resume the thread for promises, or by pushing events into a queue that you can poll from your Java for event handlers.
- Functions imported via the `@Import` will not catch exceptions, if you want proper exception handling you must call the imported function through the JSO
