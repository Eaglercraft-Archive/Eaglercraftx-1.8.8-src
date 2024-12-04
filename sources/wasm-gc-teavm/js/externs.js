/**
 * @fileoverview eagruntime externs
 * @externs
 */
/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

self.__eaglercraftXLoaderContext = {};

/**
 * @return {Object}
 */
self.__eaglercraftXLoaderContext.getEaglercraftXOpts = function() {};

/**
 * @return {string}
 */
self.__eaglercraftXLoaderContext.getEagRuntimeJSURL = function() {};

/**
 * @return {string|WebAssembly.Module}
 */
self.__eaglercraftXLoaderContext.getClassesWASMURL = function() {};

/**
 * @return {string|WebAssembly.Module}
 */
self.__eaglercraftXLoaderContext.getClassesDeobfWASMURL = function() {};

/**
 * @return {string}
 */
self.__eaglercraftXLoaderContext.getClassesTEADBGURL = function() {};

/**
 * @return {Array}
 */
self.__eaglercraftXLoaderContext.getEPKFiles = function() {};

/**
 * @return {HTMLElement}
 */
self.__eaglercraftXLoaderContext.getRootElement = function() {};

/**
 * @return {Array}
 */
self.__eaglercraftXLoaderContext.getMainArgs = function() {};

/**
 * @param {number} img
 * @return {string}
 */
self.__eaglercraftXLoaderContext.getImageURL = function(img) {};

/**
 * @param {function(Array<string>)} fn
 */
self.__eaglercraftXLoaderContext.runMain = function(fn) {};

/**
 * @param {Object} o
 */
self.__eaglerXOnMessage = function(o) {};

window.__isEaglerX188UnloadListenerSet = "";

/** @type {function()|null} */ 
window.__curEaglerX188UnloadListenerCB = function() {};

/**
 * @return {Promise<Object>}
 */
window.navigator.keyboard.getLayoutMap = function() {};

/**
 * @param {*} fn
 * @return {function(...*)}
 */
WebAssembly.promising = function(fn) {};

WebAssembly.Suspending = class {
	/**
	 * @param {*} fn
	 */
	constructor(fn) {
		
	}
};

/**
 * @param {*} tag
 * @return {boolean}
 */
WebAssembly.Exception.prototype.is = function(tag) {}

/**
 * @param {*} tag
 * @param {number} idx
 * @return {*}
 */
WebAssembly.Exception.prototype.getArg = function(tag, idx) {}

WebAssembly.Global = class {
	/**
	 * @param {!Object} desc
	 * @param {*} initValue
	 */
	constructor(desc, initValue) {
		/** @type {*} */
		this.value = null;
	}
};

/** @type {string|null} */
HTMLIFrameElement.prototype.csp = "";
