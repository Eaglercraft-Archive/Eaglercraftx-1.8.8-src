# Eaglercraft Code Standards

**These are some basic rules to follow if you would like to write code that is consistent with the Eaglercraft 1.8 codebase. If you are already familiar with Eaglercraft 1.5 or b1.3, please abandon whatever you think is the best practice as a result of reading that code, those clients should be considered as obsolete prototypes.**

## Part A. Coding Style

### 1. Tabs, not spaces

Tabs not spaces, it makes indentation easier to manage and reduces file size. Other popular projects that are also known to use tabs instead of spaces include the linux kernel. We prefer to set tab width to 4 spaces on our editors.

Format code like the eclipse formatter on factory settings

### 2. Avoid redundant hash map lookups

Don't retrieve the same value from a hash map more than once, that includes checking if an entry exists first before retrieving its value. If you do this, you are a horrible person!

**Incorrect:**

```java
if(hashMap.containsKey("eagler")) {
    Object val = hashMap.get("eagler");
    // do something with val
}
```

**Correct:**

```java
Object val = hashMap.get("eagler");
if(val != null) {
    // do something with val
}
```

### 3. Cache the return value of a function if you plan to use it multiple times

This is somewhat an extension of rule #2, don't repeatedly call the same function multiple times if there's no reason to, even if its a relatively fast function. Everything is slower and less efficient in a browser.

**Incorrect:**

```java
while(itr.hasNext()) {
    if(!Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(SomeEntity.class).shouldRender(itr.next())) {
        itr.remove();
    }
}
```

**Correct:**

```java
Render<SomeEntity> render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(SomeEntity.class);
while(itr.hasNext()) {
    if(!render.shouldRender(itr.next())) {
        itr.remove();
    }
}
```

### 4. Iterators aren't that great

Avoid using iterators when possible, this includes a `for(Item item : list)` type loop, since this may compile into bytecode that uses an iterator. If the list is a linked list or some other type of data structure that can’t perform random access efficiently, then it is recommended to use an iterator, but if the collection is guaranteed to be something similar to an ArrayList then implement it via a traditional for loop instead.

**Recommended way to iterate an ArrayList:**

```java
for(int i = 0, l = list.size(); i < l; ++i) {
    Item item = list.get(i);
    // do something
}
```

### 5. Don't shit on the heap

Avoid creating temporary single-use objects in performance critical code, since the overhead of doing so is larger in a browser where there’s no type safety to predefine object structures. This includes using lambdas or using most of the stuff in the google guava package. Also this is partially why I prefer not using iterators whenever possible.

**Incorrect, creates 5 temporary objects:**

```java
List<String> list1 = Arrays.asList("eagler", "eagler", "deevis");
List<String> list2 = Lists.newArrayList(
    Collections2.transform(
        Collections2.filter(
            list1,
            (e) -> !e.equals("deevis")
        ),
        (e) -> (e + "!")
    )
);
```

**Correct, creates no temporary objects:**

```java
List<String> list1 = Arrays.asList("eagler", "eagler", "deevis");
List<String> list2 = Lists.newArrayList();
for(int i = 0, l = list1.size(); i < l; ++i) {
    String s = list1.get(i);
    if(!s.equals("deevis")) {
        list2.add(s + "!");
    }
}
```

(note: we are ignoring the StringBuilder instances that the compiler generates from ` + "!"`)

### 6. Don't base game/render logic off of the system time

Use `EagRuntime.steadyTimeMillis()` instead to access a monotonic clock, as in a clock that is guaranteed to only run forwards, and is not affected by changes in the system time. `System.currentTimeMillis()` should only be used in situations where you want to know the actual wall time or are measuring elapsed time across multiple page refreshes.

### 7. Prefer multiplication over division

If you're always gonna divide a number by some constant, it is better to multiply it by one-over-the-constant instead.

**Incorrect**

```java
float b = a / 50.0f;
```

**Correct**

```java
float b = a * 0.02f;
```

### 8. Shaders should take advantage of compiler intrinsics

Although you may think these two pieces of code are identical, its more than likely that the "Correct" example will compile to a more efficient shader on almost any hardware. The functions in GLSL are not a library, they are compiler intrinsics that usually compile to inline assembly that can take advantage of different acceleration instructions in the GPU's instruction set. Vector math should be done in ways that promotes the use of SIMD instructions when the code is compiled to a shader.

**Incorrect:**

```glsl
float dx = pos1.x - pos2.x;
float dy = pos1.y - pos2.y;
float dz = pos1.z - pos2.z;
float distance = sqrt(dx * dx + dy * dy + dz * dz);
float fogDensity = pow(2.718, -density * distance);
```

**Correct:**

```glsl
float fogDensity = exp(-density * length(pos1.xyz - pos2.xyz));
```

### 9. Flatten the control flow of shaders

Modern GPUs are able to execute multiple instances of a shader on a single core, but if one of those shaders encounters a branch (if statement, or related) that causes it to begin executing different code from the other instances of the shader running on that core, that instance of the shader can no longer be executed at the same time as the other instances, and suddenly you've significantly increased the amount of time this core will now be busy executing shader instructions to account for all of the branches the different shader instances have taken.

**Incorrect:**

```glsl
float lightValue = dot(lightDirection, normal);
if(lightValue > 0.0) {
    color += lightValue * lightColor * diffuseColor;
}
```

**Correct:**
```glsl
float lightValue = max(dot(lightDirection, normal), 0.0);
color += lightValue * lightColor * diffuseColor;
```

### 10. Use textureLod unless mipmapping is necessary

This will prevent the shader from wasting time trying to determine what mipmap levels to read from when the texture is sampled.

**Incorrect:**

```glsl
float depthValue = texture(depthBuffer, pos).r;
```

**Correct:**

```glsl
float depthValue = textureLod(depthBuffer, pos, 0.0).r;
```

### 11. Divide complex and branch-intensive shaders into multiple draw calls

You can use a variety of different blending modes to mathematically combine the results of shaders. This is done for the same reason as flattening the control flow, to try and keep instruction pointers in sync by periodically resetting their positions, and also to allow for the driver to multitask better on GPUs with insane numbers of cores. It also allows the shader’s execution to be distributed across multiple frames in the case of something that doesn’t need to update often (like clouds).


### 12. Don't abuse `@JSBody` in TeaVM code

TeaVM provides lots of ways of interacting with JavaScript, using `@JSBody` is not the only way, consider using an overlay type.

**Incorrect**

```java
@JSObject(params = { "obj" }, script = "return obj.valueA;")
public static native JSObject getValueA(JSObject obj);

@JSObject(params = { "obj" }, script = "return obj.valueB;")
public static native JSObject getValueB(JSObject obj);

@JSObject(params = { "obj" }, script = "return obj.valueC;")
public static native JSObject getValueC(JSObject obj);

@JSObject(params = { "obj" }, script = "obj.dumbFunction();")
public static native void callDumbFunction(JSObject obj);
```

**Correct**

```java
public interface MyObject extends JSObject {

    @JSProperty
    JSObject getValueA();

    @JSProperty
    JSObject getValueB();

    @JSProperty
    JSObject getValueC();

    void dumbFunction();

}
```

### 13. Don't fall for TeaVM's threads

It is impossible to have multithreading in JavaScript, only worker objects can be used to execute code concurrently, which can't share javascript variables. Therefore, when you create a thread in TeaVM, you're creating a virtual thread that isn't capable of running at the same time as any other virtual thread in the TeaVM context. This means it's impossible to speed a TeaVM program up through the use of multiple Java threads, instead it is more than likely that it will just slow the program down more to implement multithreading through TeaVM's threads due to the additional time required for synchronization and context switches. Its more efficient to just program the entire application to be single threaded to begin with, just put everything in the main loop and realize that if it was in a different thread it would just periodically interrupt the main loop.

### 14. Always use try-with-resources

For any code that deals with streams to be considered safe, it should either use a try-with-resources or try/finally in order to release resources when complete, since otherwise the stream might not close if an IO error causes the function to return early. This is especially important for plugin code since its supposed to be able to run on a large server for weeks at a time without the underlying JVM being restarted. If hackers discover a bug in the code to cause a function to return early like this without closing a stream, they might exploit it to fatally crash the server by spamming whatever corrupt packet causes the function to leak the stream, so all code must be written so it can fail at any time without leaking resources.

**Incorrect**

```java
InputStream is = new FileInputStream(new File("phile.txt"));
is.write(someArray);
is.close();
```

**Correct**

```java
try(InputStream is = new FileInputStream(new File("phile.txt"))) {
    is.write(someArray);
}
```

Notice that the `.close()` can be omitted completely when using a try-with-resources

### 15. Always close compression/decompression streams 

In the desktop runtime, the default oracle JDK uses native code to implement the compression/decompression streams (InflaterInputStream, GZIPInputStream, etc) and therefore if you forget to close the compression/decompression stream it will cause a memory leak when the code isn't running in a browser. This is a common issue when using byte array input/output streams since you might believe when decompressing data from a byte array that there's no reason to close the stream when you're done since its not a file, but that will still cause a memory leak due to the decompression stream not being cleaned up.

## Part B. Project Structure

### 1. Code decompiled from Minecraft goes in `src/game/java`

Don't add any new classes to `src/game/java`, and ideally any significant additions to the game's source (functions, etc) should be done through creating new classes in `src/main/java` instead of adding it directly to the decompiled classes.

### 2. Do not put platform-dependent code in `src/main/java` or `src/game/java`

One of the objectives of Eaglercraft is to make Minecraft Java edition truly cross platform, why stop at just a desktop and JavaScript runtime? There are plans to create an Android runtime and several WebAssembly runtimes, all of which will be compatible with any pre-existing eaglercraft clients that only depend on the EaglercraftX runtime library and don't directly depend on components of TeaVM or LWJGL. Ideally, all core features of the client should be implemented in the `src/main/java` and `src/game/java` and any platform-dependent features should be stubbed out in some abstract platform-independent way in classes in the `src/teavm/java` and `src/lwjgl/java` and any other future runtime you want your client to support. Ideally, every source folder of platform-dependent code should expose an identical API for access to the platform-independent code as all the other platform-dependant code folders currently expose.

### 3. Don't mix JavaScript with Java

Don’t implement features in the JavaScript runtime by requiring additional JavaScript files be included on index.html, if you must access browser APIs then use the TeaVM JSO to write your code in Java instead so it’s baked directly into classes.js. Certain browser APIs may be missing from the default TeaVM JSO-APIs library but it is not difficult to create the overlay types for them manually. Clients that violate this rule may also not possible to automatically import into the EaglercraftX boot menu depending on how fucked up they are. There aren't any limitations to the TeaVM JSO that give you a good enough excuse not to follow this rule.

### 4. Don't access the classes named "Platform\*" directly from your platform-independent code

Much like the Java runtime environment itself, Eaglercraft's runtime library consists of two layers, the internal classes full of platform-dependent code that expose an intermediate API not meant to be used by programmers directly, and the platform-independent API classes that provide a platform-independent wrapper for the platform dependent classes and also provide all the miscellaneous utility functions that don't require platform dependent code to be implemented. Chances are if you are directly using a function on a class that has a name that starts with "Platform\*", that there is a different class in `src/main/java` that you are meant to use in order to access that feature, that may perform additional checks or adjust the values you are passing to the function before calling the function in the Platform class.

## Part C. Compatibility Standards

### 1. Target minimum JDK version is Java 8

Its difficult to find a platform where its not possible to run Java 8 in some capacity, therefore the desktop runtime of EaglercraftX and the BungeeCord plugin should target Java 8. The Velocity plugin is an exception since Velocity itself doesn't support Java 8 either.

### 2. Target minimum supported browser is Google Chrome 38

Released on October 7, 2014, we think its a good target for the JavaScript versions of EaglercraftX. This is the last version of Chrome that supports hardware accelerated WebGL 1.0 on Windows XP. All base features of the underlying Minecraft 1.8 client must be functional, however things such as EaglercraftX's shaders or dynamic lighting are not required to work. The client cannot crash as a result of any missing features on an old browser, you must either implement fallbacks or safely disable the unsupported features.

### 3. Target minimum supported graphics API is OpenGL ES 2.0 (WebGL 1.0)

The most widely supported graphics API in the world is currently OpenGL ES 2.0, so ideally that should be the target for EaglercraftX 1.8. We can guarantee the client will be on an OpenGL ES 3.0 context 99% of the time, however its not that hard to also maintain support for GLES 2.0 (WebGL 1.0) as well with slightly reduced functionality so we might as well make it a feature in case of the 1% of the time that functionality is not available. The client cannot depend on any GL extensions in order to run in GLES 2.0 mode, however its reasonable to assume there will be VAO support via extensions in most GLES 2.0 contexts so the client includes an abstraction layer (via EaglercraftGPU.java) to seamlessly emulate VAO functionality even when the client is running in GLES 2.0 mode with no VAO extensions. The only core feature of Minecraft 1.8 that is completely unavailable in GLES 2.0 mode is mip-mapping for the blocks/items texture atlas due to being unable to limit the max mipmap level.

### 4. Use preprocessor directives to make portable shaders that can be compiled for both OpenGL ES 2.0 and 3.0 contexts

Most of the shaders in the base "glsl" directory of the resources EPK file use a file called "gles2_compat.glsl" to polyfill certain GLSL features (such as input/output declarations) via preprocessor directives to allow them to be compiled on both OpenGL ES 3.0 and 2.0 contexts. This is the preferred way to implement backwards compatibility over creating seprate versions of the same shaders, since future developers don't need to waste time maintaining multiple versions of the same code if they don't really care about backwards compatibility in the first place.

### 5. Target minimum version of the JavaScript syntax is ES5 strict mode

A shim is included to provide certain ES6 functions, however you should always program with syntax compatible with ES5, so the script doesn't crash immediately due to syntax errors even if the functions that use unsupported syntax aren't actually being called. `build.gradle` currently patches out all the ES5 strict mode incompatible syntax in the output of TeaVM 0.9.2, but this will probably break if you try to update TeaVM. Don't worry though because future WASM versions of EaglercraftX will use the latest versions of TeaVM. **Some common incompatible syntax to avoid includes `const`, `let`, `async`, `( ) => `, and using named functions! You can't do any of these things in your JSBody annotations.**

### 6. You cannot depend on any deprecated browser features

The same way we want EaglercraftX to work on browsers from over 10 years ago, we want it to still work in browsers 10 years from today, therefore the client cannot depend on any deprecated browser features in order for all the base Minecraft 1.8 game's features to work properly. However it is okay to use deprecated features as fallback if any modern non-deprecated feature (such as keyboard event handling) that the game needs if the game is running in an old browser.

### 7. Always use addEventListener to register event handlers

Always use addEventListener to register event handlers for browser APIs, never through the use of assigning the legacy "on\*" (onclick, onkeydown, onmessage, etc) variables, the TeaVMUtils class has a universal helper function for accessing addEventListener on any JSO objects that don’t already implement the function.

### 8. JavaScript should be executed in strict mode

Always make sure your JavaScript files start with `"use strict";`, be careful when adding this to your code retroactively because it will probably break hastily written code unless you haven’t made a single typo that’s not forbidden in strict mode. Be aware that in Chrome 38 this means you can't use stuff such as `const` and `let` or named functions in any of your JSBody annotations!
