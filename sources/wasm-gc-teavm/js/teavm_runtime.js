/*
 *  Copyright 2024 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/** @type {Object} */
var wasmGC;

(function() {

let globalsCache = new Map();
let exceptionFrameRegex = /.+:wasm-function\[[0-9]+]:0x([0-9a-f]+).*/;
/**
 * @param {string} name
 */
let getGlobalName = function(name) {
    let result = globalsCache.get(name);
    if (typeof result === "undefined") {
        result = new Function("return " + name + ";");
        globalsCache.set(name, result);
    }
    return result();
}
/**
 * @param {string} name
 * @param {Object} value
 */
let setGlobalName = function(name, value) {
    new Function("value", name + " = value;")(value);
}

/**
 * @param {Object} imports
 */
function defaults(imports) {
    let context = {
        /** @type {Object} */
        exports: null,
        /** @type {function(number)|null} */
        stackDeobfuscator: null,
        /** @type {function(function())|null} */
        asyncRunnableQueue: null
    };
    dateImports(imports);
    consoleImports(imports);
    coreImports(imports, context);
    jsoImports(imports, context);
    imports["teavmMath"] = Math;
    return {
        supplyExports(/** Object */ exports) {
            context.exports = exports;
        },
        supplyStackDeobfuscator(/** function(number) */ deobfuscator) {
            context.stackDeobfuscator = deobfuscator;
        },
        supplyAsyncRunnableQueue(/** function(function()) */ queueFunc) {
            context.asyncRunnableQueue = queueFunc;
        }
    }
}

let javaExceptionSymbol = Symbol("javaException");
class JavaError extends Error {
    constructor(context, javaException) {
        super();
        this.context = context;
        this[javaExceptionSymbol] = javaException;
        context.exports["teavm.setJsException"](javaException, this);
    }
    get message() {
        let exceptionMessage = this.context.exports["teavm.exceptionMessage"];
        if (typeof exceptionMessage === "function") {
            let message = exceptionMessage(this[javaExceptionSymbol]);
            if (message != null) {
                return message;
            }
        }
        return "(could not fetch message)";
    }
}

/**
 * @param {Object} imports
 */
function dateImports(imports) {
    imports["teavmDate"] = {
        "currentTimeMillis": () => new Date().getTime(),
        "dateToString": (/** number */ timestamp) => new Date(timestamp).toString(),
        "getYear": (/** number */ timestamp) => new Date(timestamp).getFullYear(),
        "setYear": (/** number */ timestamp, /** number */ year) => {
            let date = new Date(timestamp);
            date.setFullYear(year);
            return date.getTime();
        },
        "getMonth": (/** number */ timestamp) =>new Date(timestamp).getMonth(),
        "setMonth": (/** number */ timestamp, /** number */ month) => {
            let date = new Date(timestamp);
            date.setMonth(month);
            return date.getTime();
        },
        "getDate": (/** number */ timestamp) =>new Date(timestamp).getDate(),
        "setDate": (/** number */ timestamp, /** number */ value) => {
            let date = new Date(timestamp);
            date.setDate(value);
            return date.getTime();
        },
        "create": (/** number */ year, /** number */ month, /** number */ date, /** number */ hrs, /** number */ min, /** number */ sec) => new Date(year, month, date, hrs, min, sec).getTime(),
        "createFromUTC": (/** number */ year, /** number */ month, /** number */ date, /** number */ hrs, /** number */ min, /** number */ sec) => Date.UTC(year, month, date, hrs, min, sec)
    };
}

/**
 * @param {Object} imports
 */
function consoleImports(imports) {
    let stderr = [];
    let stdout = [];
    imports["teavmConsole"] = {
        "putcharStderr": function(/** number */ c) {
            if (c === 10) {
                let stderrStr = String.fromCharCode(...stderr);
                console.error(stderrStr);
                if(currentRedirectorFunc) {
                    currentRedirectorFunc(stderrStr, true);
                }
                stderr.length = 0;
            } else {
                stderr.push(c);
            }
        },
        "putcharStdout": function(/** number */ c) {
            if (c === 10) {
                let stdoutStr = String.fromCharCode(...stdout);
                console.log(stdoutStr);
                if(currentRedirectorFunc) {
                    currentRedirectorFunc(stdoutStr, false);
                }
                stdout.length = 0;
            } else {
                stdout.push(c);
            }
        },
    };
}

/**
 * @param {Object} imports
 * @param {Object} context
 */
function coreImports(imports, context) {
    let finalizationRegistry = new FinalizationRegistry(heldValue => {
        let report = context.exports["teavm.reportGarbageCollectedValue"];
        if (typeof report !== "undefined") {
            context.asyncRunnableQueue(function() {
                report(heldValue.queue, heldValue.ref);
            });
        }
    });
    let stringFinalizationRegistry = new FinalizationRegistry(heldValue => {
        let report = context.exports["teavm.reportGarbageCollectedString"];
        if (typeof report === "function") {
            context.asyncRunnableQueue(function() {
                report(heldValue);
            });
        }
    });
    imports["teavm"] = {
        "createWeakRef": (value, ref, queue) => {
            if (queue !== null) {
                finalizationRegistry.register(value, { ref: ref, queue: queue });
            }
            return new WeakRef(value);
        },
        "deref": weakRef => weakRef.deref(),
        "createStringWeakRef": (value, heldValue) => {
            stringFinalizationRegistry.register(value, heldValue)
            return new WeakRef(value);
        },
        "stringDeref": weakRef => weakRef.deref(),
        "takeStackTrace": () => {
            let stack = new Error().stack;
            let addresses = [];
            for (let line of stack.split("\n")) {
                let match = exceptionFrameRegex.exec(line);
                if (match !== null && match.length >= 2) {
                    let address = parseInt(match[1], 16);
                    addresses.push(address);
                }
            }
            return {
                "getStack": function() {
                    let result;
                    if (context.stackDeobfuscator) {
                        try {
                            result = context.stackDeobfuscator(addresses);
                        } catch (e) {
                            console.warn("Could not deobfuscate stack", e);
                        }
                    }
                    if (!result) {
                        result = addresses.map(address => {
                            return {
                                className: "java.lang.Throwable$FakeClass",
                                method: "fakeMethod",
                                file: "Throwable.java",
                                line: address
                            };
                        });
                    }
                    return result;
                }
            };
        },
        "decorateException": (javaException) => {
            new JavaError(context, javaException);
        }
    };
}

/**
 * @param {Object} imports
 * @param {Object} context
 */
function jsoImports(imports, context) {
    let javaObjectSymbol = Symbol("javaObject");
    let functionsSymbol = Symbol("functions");
    let functionOriginSymbol = Symbol("functionOrigin");
    let wrapperCallMarkerSymbol = Symbol("wrapperCallMarker");

    let jsWrappers = new WeakMap();
    let javaWrappers = new WeakMap();
    let primitiveWrappers = new Map();
    let primitiveFinalization = new FinalizationRegistry(token => primitiveWrappers.delete(token));
    let hashCodes = new WeakMap();
    let lastHashCode = 2463534242;
    let nextHashCode = () => {
        let x = lastHashCode;
        x ^= x << 13;
        x ^= x >>> 17;
        x ^= x << 5;
        lastHashCode = x;
        return x;
    }

    function identity(value) {
        return value;
    }
    function sanitizeName(str) {
        let result = "";
        let firstChar = str.charAt(0);
        result += isIdentifierStart(firstChar) ? firstChar : '_';
        for (let i = 1; i < str.length; ++i) {
            let c = str.charAt(i)
            result += isIdentifierPart(c) ? c : '_';
        }
        return result;
    }
    function isIdentifierStart(s) {
        return s >= 'A' && s <= 'Z' || s >= 'a' && s <= 'z' || s === '_' || s === '$';
    }
    function isIdentifierPart(s) {
        return isIdentifierStart(s) || s >= '0' && s <= '9';
    }
    function setProperty(obj, prop, value) {
        if (obj === null) {
            setGlobalName(prop, value);
        } else {
            obj[prop] = value;
        }
    }
    function javaExceptionToJs(e) {
        if (e instanceof WebAssembly.Exception) {
            let tag = context.exports["teavm.javaException"];
            let getJsException = context.exports["teavm.getJsException"];
            if (e.is(tag)) {
                let javaException = e.getArg(tag, 0);
                let extracted = extractException(javaException);
                if (extracted !== null) {
                    return extracted;
                }
                let wrapper = getJsException(javaException);
                if (typeof wrapper === "undefined") {
                    wrapper = new JavaError(context, javaException);
                }
                return wrapper;
            }
        }
        return e;
    }
    function jsExceptionAsJava(e) {
        if (javaExceptionSymbol in e) {
            return e[javaExceptionSymbol];
        } else {
            return context.exports["teavm.js.wrapException"](e);
        }
    }
    function rethrowJsAsJava(e) {
        context.exports["teavm.js.throwException"](jsExceptionAsJava(e));
    }
    function extractException(e) {
        return context.exports["teavm.js.extractException"](e);
    }
    function rethrowJavaAsJs(e) {
        throw javaExceptionToJs(e);
    }
    function getProperty(obj, prop) {
        try {
            return obj !== null ? obj[prop] : getGlobalName(prop)
        } catch (e) {
            rethrowJsAsJava(e);
        }
    }
    function defineFunction(fn) {
        let params = [];
        for (let i = 0; i < fn.length; ++i) {
            params.push("p" + i);
        }
        let paramsAsString = params.length === 0 ? "" : params.join(", ");
        let ret = new Function("rethrowJavaAsJs", "fn",
            `return function(${paramsAsString}) {\n` +
            `    try {\n` +
            `        return fn(${paramsAsString});\n` +
            `    } catch (e) {\n` +
            `        rethrowJavaAsJs(e);\n` +
            `    }\n` +
            `};`
        )(rethrowJavaAsJs, fn);
        ret["__impl"] = fn;
        ret["__rethrow"] = rethrowJavaAsJs;
        return ret;
    }
    function renameConstructor(name, c) {
        return new Function(
            "constructor",
            `return function ${name}(marker, javaObject) {\n` +
            `    return constructor.call(this, marker, javaObject);\n` +
            `}\n`
        )(c);
    }
    imports["teavmJso"] = {
        "emptyString": () => "",
        "stringFromCharCode": code => String.fromCharCode(code),
        "concatStrings": (a, b) => a + b,
        "stringLength": s => s.length,
        "charAt": (s, index) => s.charCodeAt(index),
        "emptyArray": () => [],
        "appendToArray": (array, e) => array.push(e),
        "unwrapBoolean": value => value ? 1 : 0,
        "wrapBoolean": value => !!value,
        "getProperty": getProperty,
        "setProperty": setProperty,
        "setPropertyPure": setProperty,
        "global": (name) => {
            try {
                return getGlobalName(name);
            } catch (e) {
                rethrowJsAsJava(e);
            }
        },
        "createClass": (name, parent, constructor) => {
            name = sanitizeName(name || "JavaObject");
            let action;
            if (parent === null) {
                action = function (javaObject) {
                    this[javaObjectSymbol] = javaObject;
                    this[functionsSymbol] = null;
                };
            } else {
                action = function (javaObject) {
                    parent.call(this, javaObject);
                };
                //TODO: what are these for
                //fn.prototype = Object.create(parent);
                //fn.prototype.constructor = parent;
            }
            let fn = renameConstructor(name, function (marker, javaObject) {
                if (marker === wrapperCallMarkerSymbol) {
                    action.call(this, javaObject);
                } else if (constructor === null) {
                    throw new Error("This class can't be instantiated directly");
                } else {
                    try {
                        return constructor.apply(null, arguments);
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            });
            fn.prototype = Object.create(parent || Object.prototype);
            fn.prototype.constructor = fn;
            let boundFn = renameConstructor(name, function(javaObject) {
                return fn.call(this, wrapperCallMarkerSymbol, javaObject);
            });
            boundFn[wrapperCallMarkerSymbol] = fn;
            boundFn.prototype = fn.prototype;
            return boundFn;
        },
        "exportClass": (cls) => {
            return cls[wrapperCallMarkerSymbol];
        },
        "defineMethod": (cls, name, fn) => {
            let params = [];
            for (let i = 1; i < fn.length; ++i) {
                params.push("p" + i);
            }
            let paramsAsString = params.length === 0 ? "" : params.join(", ");
            cls.prototype[name] = new Function("rethrowJavaAsJs", "fn",
                `return function(${paramsAsString}) {\n` +
                `    try {\n` +
                `        return fn(${['this', params].join(", ")});\n` +
                `    } catch (e) {\n` +
                `        rethrowJavaAsJs(e);\n` +
                `    }\n` +
                `};`
            )(rethrowJavaAsJs, fn);
        },
        "defineStaticMethod": (cls, name, fn) => {
            cls[name] = defineFunction(fn);
        },
        "defineFunction": defineFunction,
        "defineProperty": (cls, name, getFn, setFn) => {
            let descriptor = {
                get() {
                    try {
                        return getFn(this);
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            };
            if (setFn !== null) {
                descriptor.set = function(value) {
                    try {
                        setFn(this, value);
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            }
            Object.defineProperty(cls.prototype, name, descriptor);
        },
        "defineStaticProperty": (cls, name, getFn, setFn) => {
            let descriptor = {
                get() {
                    try {
                        return getFn();
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            };
            if (setFn !== null) {
                descriptor.set = function(value) {
                    try {
                        setFn(value);
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            }
            Object.defineProperty(cls, name, descriptor);
        },
        "javaObjectToJS": (instance, cls) => {
            if (instance === null) {
                return null;
            }
            let existing = jsWrappers.get(instance);
            if (typeof existing != "undefined") {
                let result = existing.deref();
                if (typeof result !== "undefined") {
                    return result;
                }
            }
            let obj = new cls(instance);
            jsWrappers.set(instance, new WeakRef(obj));
            return obj;
        },
        "unwrapJavaObject": (instance) => {
            return instance[javaObjectSymbol];
        },
        "asFunction": (instance, propertyName) => {
            let functions = instance[functionsSymbol];
            if (functions === null) {
                functions = Object.create(null);
                instance[functionsSymbol] = functions;
            }
            let result = functions[propertyName];
            if (typeof result !== 'function') {
                result = function() {
                    return instance[propertyName].apply(instance, arguments);
                }
                result[functionOriginSymbol] = instance;
                functions[propertyName] = result;
            }
            return result;
        },
        "functionAsObject": (fn, property) => {
            let origin = fn[functionOriginSymbol];
            if (typeof origin !== 'undefined') {
                let functions = origin[functionsSymbol];
                if (functions !== void 0 && functions[property] === fn) {
                    return origin;
                }
            }
            return {
                [property]: function(...args) {
                    try {
                        return fn(...args);
                    } catch (e) {
                        rethrowJavaAsJs(e);
                    }
                }
            };
        },
        "wrapObject": (obj) => {
            if (obj === null) {
                return null;
            }
            if (typeof obj === "object" || typeof obj === "function" || typeof "obj" === "symbol") {
                let result = obj[javaObjectSymbol];
                if (typeof result === "object") {
                    return result;
                }
                result = javaWrappers.get(obj);
                if (result !== void 0) {
                    result = result.deref();
                    if (result !== void 0) {
                        return result;
                    }
                }
                result = context.exports["teavm.jso.createWrapper"](obj);
                javaWrappers.set(obj, new WeakRef(result));
                return result;
            } else {
                let result = primitiveWrappers.get(obj);
                if (result !== void 0) {
                    result = result.deref();
                    if (result !== void 0) {
                        return result;
                    }
                }
                result = context.exports["teavm.jso.createWrapper"](obj);
                primitiveWrappers.set(obj, new WeakRef(result));
                primitiveFinalization.register(result, obj);
                return result;
            }
        },
        "isPrimitive": (value, type) => typeof value === type,
        "instanceOf": (value, type) => value instanceof type,
        "instanceOfOrNull": (value, type) => value === null || value instanceof type,
        "sameRef": (a, b) => a === b,
        "hashCode": (obj) => {
            if (typeof obj === "object" || typeof obj === "function" || typeof obj === "symbol") {
                let code = hashCodes.get(obj);
                if (typeof code === "number") {
                    return code;
                }
                code = nextHashCode();
                hashCodes.set(obj, code);
                return code;
            } else if (typeof obj === "number") {
                return obj | 0;
            } else if (typeof obj === "bigint") {
                return BigInt.asIntN(32, obj);
            } else if (typeof obj === "boolean") {
                return obj ? 1 : 0;
            } else {
                return 0;
            }
        },
        "apply": (instance, method, args) => {
            try {
                if (instance === null) {
                    let fn = getGlobalName(method);
                    return fn(...args);
                } else {
                    return instance[method](...args);
                }
            } catch (e) {
                rethrowJsAsJava(e);
            }
        },
        "concatArray": (a, b) => a.concat(b),
        "getJavaException": e => e[javaExceptionSymbol]
    };
    for (let name of ["wrapByte", "wrapShort", "wrapChar", "wrapInt", "wrapFloat", "wrapDouble", "unwrapByte",
        "unwrapShort", "unwrapChar", "unwrapInt", "unwrapFloat", "unwrapDouble"]) {
        imports["teavmJso"][name] = identity;
    }
    function wrapCallFromJavaToJs(call) {
        try {
            return call();
        } catch (e) {
            rethrowJsAsJava(e);
        }
    }
    let argumentList = [];
    for (let i = 0; i < 32; ++i) {
        let args = argumentList.length === 0 ? "" : argumentList.join(", ");
        let argsAndBody = [...argumentList, "body"].join(", ");
        imports["teavmJso"]["createFunction" + i] = new Function("wrapCallFromJavaToJs", ...argumentList, "body",
            `return new Function('wrapCallFromJavaToJs', ${argsAndBody}).bind(this, wrapCallFromJavaToJs);`
        ).bind(null, wrapCallFromJavaToJs);
        imports["teavmJso"]["bindFunction" + i] = (f, ...args) => f.bind(null, ...args);
        imports["teavmJso"]["callFunction" + i] = new Function("rethrowJsAsJava", "fn", ...argumentList,
            `try {\n` +
            `    return fn(${args});\n` +
            `} catch (e) {\n` +
            `    rethrowJsAsJava(e);\n` +
            `}`
        ).bind(null, rethrowJsAsJava);
        imports["teavmJso"]["callMethod" + i] = new Function("rethrowJsAsJava", "getGlobalName", "instance",
            "method", ...argumentList,
            `try {\n`+
            `     return instance !== null\n` +
            `        ? instance[method](${args})\n` +
            `        : getGlobalName(method)(${args});\n` +
            `} catch (e) {\n` +
            `    rethrowJsAsJava(e);\n` +
            `}`
        ).bind(null, rethrowJsAsJava, getGlobalName);
        imports["teavmJso"]["construct" + i] = new Function("rethrowJsAsJava", "constructor", ...argumentList,
            `try {\n` +
            `    return new constructor(${args});\n` +
            `} catch (e) {\n` +
            `    rethrowJsAsJava(e);\n` +
            `}`
        ).bind(null, rethrowJsAsJava);
        imports["teavmJso"]["arrayOf" + i] = new Function(...argumentList, "return [" + args + "]");

        let param = "p" + (i + 1);
        argumentList.push(param);
    }
}

/**
 * @param {Object} importObj
 */
function wrapImport(importObj) {
    return new Proxy(importObj, {
        get(target, prop) {
            let result = target[prop];
            return new WebAssembly.Global({ "value": "externref", "mutable": false }, result);
        }
    });
}

/*
 * @param {!WebAssembly.Module} wasmModule
 * @param {Object} imports
 * @return {!Promise}
 *
async function wrapImports(wasmModule, imports) {
    let promises = [];
    let propertiesToAdd = {};
    for (let { module, name, kind } of WebAssembly.Module.imports(wasmModule)) {
        if (kind !== "global" || module in imports) {
            continue;
        }
        let names = propertiesToAdd[module];
        if (names === void 0) {
            let namesByModule = [];
            names = namesByModule;
            propertiesToAdd[module] = names;
            promises.push((async () => {
                let moduleInstance = await import(module);
                let importsByModule = {};
                for (let name of namesByModule) {
                    let importedName = name === "__self__" ? moduleInstance : moduleInstance[name];
                    importsByModule[name] = new WebAssembly.Global(
                        { value: "externref", mutable: false },
                        importedName
                    );
                }
                imports[module] = importsByModule;
            })());
        }
        names.push(name);
    }
    if (promises.length === 0) {
        return;
    }
    await Promise.all(promises);
}*/

/**
 * @param {string|!WebAssembly.Module} path
 * @param {Object} options
 * @return {!Promise<Object>}
 */
async function load(path, options) {
    if (!options) {
        options = {};
    }

    let deobfuscatorOptions = options.stackDeobfuscator || {};
    let [deobfuscatorFactory, module, debugInfo] = await Promise.all([
        deobfuscatorOptions.enabled ? getDeobfuscator(deobfuscatorOptions) : Promise.resolve(null),
        (path instanceof WebAssembly.Module) ? Promise.resolve(path) : WebAssembly.compileStreaming(fetch(path)),
        fetchExternalDebugInfo(deobfuscatorOptions.infoLocation, deobfuscatorOptions)
    ]);

    const importObj = {};
    const defaultsResult = defaults(importObj);
    if (typeof options.installImports !== "undefined") {
        options.installImports(importObj);
    }
    //if (!options.noAutoImports) {
    //    await wrapImports(module, importObj);
    //}
    
    defaultsResult.supplyAsyncRunnableQueue(setupAsyncCallbackPoll(importObj));
    
    let instance = /** @type {!WebAssembly.Instance} */ (await WebAssembly.instantiate(module, importObj));

    let userExports = {};
    
    defaultsResult.supplyExports(instance.exports);
    if (deobfuscatorFactory) {
        let deobfuscator = createDeobfuscator(null, debugInfo, deobfuscatorFactory.instance);
        if (deobfuscator !== null) {
            defaultsResult.supplyStackDeobfuscator(deobfuscator);
            userExports["deobfuscator"] = deobfuscator;
        }
    }

    let teavm = {
        exports: userExports,
        instance: instance,
        modules: {
            classes: module,
            deobfuscator: (deobfuscatorFactory ? deobfuscatorFactory.module : null)
        }
    };
    for (let key in instance.exports) {
        let exportObj = instance.exports[key];
        if (exportObj instanceof WebAssembly.Global) {
            Object.defineProperty(userExports, key, {
                get: () => exportObj.value
            });
        }else if (typeof exportObj === "function") {
            userExports[key] = exportObj;
        }
    }
    userExports.memory = instance.exports["teavm.memory"];
	userExports.debugInfo = debugInfo;
    return teavm;
}

/**
 * @param {Object} options
 * @return {!Promise<?{module:!WebAssembly.Module,instance:!WebAssembly.Instance}>}
 */
async function getDeobfuscator(options) {
    try {
        const importObj = {};
        const defaultsResult = defaults(importObj);
        const module = (options.path instanceof WebAssembly.Module) ?
            options.path : await WebAssembly.compileStreaming(fetch(options.path));
        const instance = new WebAssembly.Instance(module, importObj);
        defaultsResult.supplyExports(instance.exports)
        return { module, instance };
    } catch (e) {
        console.warn("Could not load deobfuscator", e);
        return null;
    }
}

/**
 * @param {WebAssembly.Module} module
 * @param {Int8Array} externalData
 * @param {WebAssembly.Instance} deobfuscatorFactory
 * @return {function(number)}
 */
function createDeobfuscator(module, externalData, deobfuscatorFactory) {
    let deobfuscator = null;
    let deobfuscatorInitialized = false;
    function ensureDeobfuscator() {
        if (!deobfuscatorInitialized) {
            deobfuscatorInitialized = true;
            if (externalData !== null) {
                try {
                    deobfuscator = deobfuscatorFactory.exports["createFromExternalFile"].value(externalData);
                } catch (e) {
                    console.warn("Could not load create deobfuscator", e);
                }
            }
            if (deobfuscator == null && module !== null) {
                try {
                    deobfuscator = deobfuscatorFactory.exports["createForModule"].value(module);
                } catch (e) {
                    console.warn("Could not create deobfuscator from module data", e);
                }
            }
        }
    }
    return addresses => {
        ensureDeobfuscator();
        return deobfuscator !== null ? deobfuscator["deobfuscate"](addresses) : [];
    }
}

/**
 * @param {string} debugInfoLocation
 * @param {Object} options
 * @return {!Promise<Int8Array>}
 */
async function fetchExternalDebugInfo(debugInfoLocation, options) {
    if (!options.enabled) {
        return null;
    }
    if (debugInfoLocation !== "auto" && debugInfoLocation !== "external") {
        return null;
    }
	if(options.externalInfoPath instanceof ArrayBuffer) {
		return new Int8Array(options.externalInfoPath);
	}else {
		let response = await fetch(options.externalInfoPath);
		if (!response.ok) {
			return null;
		}
		return new Int8Array(await response.arrayBuffer());
	}
}

/**
 * @param {Object} importObj
 * @return {function(function())}
 */
function setupAsyncCallbackPoll(importObj) {
    const queueObj = new EaglerLinkedQueue();
    importObj["teavm"]["pollAsyncCallbacks"] = function() {
        var v;
        while(v = queueObj.shift()) {
            v["fn"]();
        }
    };
    return function(/** function() */ fn) {
        queueObj.push({
            "fn": fn,
            "_next": null
        });
    };
}

wasmGC = (function() {
    //include();
    return { load, defaults, wrapImport };
})();

})();
