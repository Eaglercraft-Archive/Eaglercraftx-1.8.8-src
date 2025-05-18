# eaglercraft-workspace

### Java 17 or greater is required!

**To get started, import this entire folder into your IDE as a Gradle project, this will automatically create several different projects to build all the common classes and each runtime.**

The Gradle plugin was created by [cire3](https://github.com/cire3wastaken), and the source code is available [here](https://github.com/The-Resent-Team/open-source-projects).

**To compile the JavaScript client:**

Run the `MakeOfflineDownload` script in the "target_teavm_javascript" folder (or the `makeMainOfflineDownload` Gradle task in your IDE) to build the JavaScript client. This will build the "classes.js" and "assets.epk" and the offline downloads, the results will be in the "javascript" folder.

**To compile the WASM-GC client:**

Run the `MakeWASMClientBundle` script in the "target_teavm_wasm_gc" folder (or the `makeMainWasmClientBundle` Gradle task in your IDE) to build the WASM-GC client. This will build the "assets.epw" file which contains all the code and assets if the WASM-GC client, and also create an offline download, the results will be in the "javascript_dist" folder.

The WASM-GC client uses a custom fork of TeaVM, the source is available [here](https://github.com/Eaglercraft-TeaVM-Fork/eagler-teavm).

**To run the desktop runtime:**

**Note:** Athough it may be tempting to release "desktop" copies of your client, the current desktop runtime was designed for debug use only and is a poor choice for distribution to end users.

Run the `StartDesktopRuntime` script in the "target_lwjgl_desktop" folder (or the `eaglercraftDebugRuntime` Gradle task in your IDE) to run the desktop runtime. This will run the client using the JVM and an LWJGL3-based runtime, which can be useful for debugging crashes and to speed up testing if you don't want to wait for the JavaScript or WASM-GC client to be built.

Do not use the desktop runtime as substitute for testing you client in a browser, client developers who only test their client on the desktop runtime usually end up with lots of unexpected bugs and crashes in their browser builds.

**To debug the desktop runtime:**

If you want to debug the desktop runtime from your IDE, one way you can do it is by enabling the debugger in the LWJGL target's `eaglercraftDebugRuntime` task, but something that will launch even faster is just creating a run configuration in your IDE directly in the LWJGL target project.

You can do this by creating a run configuration specifying `net.lax1dude.eaglercraft.v1_8.internal.lwjgl.MainClass` as the main class, the `desktopRuntime` folder as the working directory, `-Xmx1G -Xms1G -Djava.library.path=.` in the JVM arguments, and if you're on Linux you'll also want to add an environment variable to append the `desktopRuntime` folder to `LD_LIBRARY_PATH` and set `__GL_THREADED_OPTIMIZATIONS` to `0` if using Nvidia drivers.
