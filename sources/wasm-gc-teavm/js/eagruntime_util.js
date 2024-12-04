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

const DEBUG = 0;
const INFO = 1;
const WARN = 2;
const ERROR = 3;
const OFF = 4;

const levels = [
	"DEBUG",
	"INFO",
	"WARN",
	"ERROR"
];

var contextName = "main";
var currentLogLevel = INFO;

/** @type {function(string, boolean)|null} */
var currentRedirectorFunc = null;

/**
 * @param {string} msg
 * @param {Array} args
 */
function formatArgs(msg, args) {
	if(args.length > 0) {
		var retString = [];
		for(var i = 0; i < args.length; ++i) {
			var idx = msg.indexOf("{}");
			if(idx != -1) {
				retString.push(msg.substring(0, idx));
				retString.push(args[i]);
				msg = msg.substring(idx + 2);
			}else {
				break;
			}
		}
		if(retString.length > 0) {
			retString.push(msg);
			return retString.join("");
		}else {
			return msg;
		}
	}else {
		return msg;
	}
}

/**
 * @param {number} lvl
 * @param {string} msg
 * @param {Array} args
 */
function logImpl(lvl, msg, args) {
	if(lvl < currentLogLevel) {
		return;
	}
	msg = "EagRuntimeJS: [" + (new Date()).toLocaleTimeString() + "][" + contextName +"/" + (levels[lvl] || "UNKNOWN") + "] " + formatArgs(msg, args);
	if(lvl >= ERROR) {
		console.error(msg);
	}else {
		console.log(msg);
	}
	if(currentRedirectorFunc) {
		currentRedirectorFunc(msg, lvl >= ERROR);
	}
}

/**
 * @param {string} name
 */
function setLoggerContextName(name) {
	contextName = name;
}

/**
 * @param {string} msg
 * @param {...*} args
 */
function eagDebug(msg, ...args) {
	logImpl(DEBUG, msg, args);
}

/**
 * @param {string} msg
 * @param {...*} args
 */
function eagInfo(msg, ...args) {
	logImpl(INFO, msg, args);
}

/**
 * @param {string} msg
 * @param {...*} args
 */
function eagWarn(msg, ...args) {
	logImpl(WARN, msg, args);
}

/**
 * @param {string} msg
 * @param {...*} args
 */
function eagError(msg, ...args) {
	logImpl(ERROR, msg, args);
}

/**
 * @param {number} lvl
 * @param {string} msg
 * @param {...*} args
 */
function eagLog(lvl, msg, ...args) {
	logImpl(lvl, msg, args);
}

/**
 * @param {number} lvl
 * @param {string} msg
 * @param {Error} err
 */
function eagStackTrace(lvl, msg, err) {
	if(err) {
		if(err.message) {
			eagLog(lvl, "{}: {} - \"{}\"", msg, err.name, err.message);
		}else {
			eagLog(lvl, "{}: {}", msg, err.name);
		}
		if(typeof err.stack === "string") {
			const stackElements = deobfuscateStack(err.stack);
			for(var i = 0; i < stackElements.length; ++i) {
				eagLog(lvl, "    at " + stackElements[i]);
			}
		}
	}else {
		eagLog(lvl, "{}: <null>", msg);
	}
}

/**
 * @param {string} modName
 * @param {string} str
 * @return {function()}
 */
function unsupportedFunc(modName, str) {
	return function() {
		eagError("Unsupported function called: {}.{}", str);
		return 0;
	};
}

/**
 * @param {Object} importsObj
 * @param {string} modName
 * @param {string} str
 */
function setUnsupportedFunc(importsObj, modName, str) {
	importsObj[str] = unsupportedFunc(modName, str);
}

/**
 * @param {number} ms
 */
function promiseTimeout(ms) {
	return new Promise(function(resolve) {
		setTimeout(resolve, ms);
	});
}

class EaglerLinkedQueue {

	constructor() {
		this.firstElement = null;
		this.lastElement = null;
		this.queueLength = 0;
	}

	/**
	 * @return {number}
	 */
	getLength() {
		return this.queueLength;
	}

	/**
	 * @param {Object} obj
	 */
	push(obj) {
		if(this.lastElement) {
			this.lastElement["_next"] = obj;
		}
		this.lastElement = obj;
		if(!this.firstElement) {
			this.firstElement = obj;
		}
		++this.queueLength;
	}

	/**
	 * @return {Object}
	 */
	shift() {
		if(this.firstElement) {
			const ret = this.firstElement;
			this.firstElement = ret["_next"] || null;
			if(!this.firstElement) {
				this.lastElement = null;
			}else {
				ret["_next"] = null;
			}
			--this.queueLength;
			return ret;
		}else {
			return null;
		}
	}

}
