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

const platfFilesystemName = "platformFilesystem";

/**
 * @param {Object} k
 * @return {string}
 */
function readDBKey(k) {
	return ((typeof k) === "string") ? k : (((typeof k) === "undefined") ? null : (((typeof k[0]) === "string") ? k[0] : null));
}

/**
 * @param {Object} obj
 * @return {ArrayBuffer}
 */
function readDBRow(obj) {
	return (typeof obj === "undefined") ? null : ((typeof obj["data"] === "undefined") ? null : obj["data"]);
}

/**
 * @param {string} pat
 * @param {ArrayBuffer} dat
 * @return {Object}
 */
function writeDBRow(pat, dat) {
	return { "path": pat, "data": dat };
}

/**
 * @param {string} filesystemDB
 * @return {Promise}
 */
function openDBImpl(filesystemDB) {
	return new Promise(function(resolve) {
		if(typeof indexedDB === "undefined") {
			resolve({
				"failedInit": true,
				"failedLocked": false,
				"failedError": "IndexedDB not supported",
				"database": null
			});
			return;
		}
		let dbOpen;
		try {
			dbOpen = indexedDB.open(filesystemDB, 1);
		}catch(err) {
			resolve({
				"failedInit": true,
				"failedLocked": false,
				"failedError": "Exception opening database",
				"database": null
			});
			return;
		}
		let resultConsumer = resolve;
		dbOpen.addEventListener("success", function(evt) {
			if(resultConsumer) resultConsumer({
				"failedInit": false,
				"failedLocked": false,
				"failedError": null,
				"database": dbOpen.result
			});
			resultConsumer = null;
		});
		dbOpen.addEventListener("blocked", function(evt) {
			if(resultConsumer) resultConsumer({
				"failedInit": false,
				"failedLocked": true,
				"failedError": "Database is locked",
				"database": null
			});
			resultConsumer = null;
		});
		dbOpen.addEventListener("error", function(evt) {
			if(resultConsumer) resultConsumer({
				"failedInit": true,
				"failedLocked": false,
				"failedError": "Opening database failed",
				"database": null
			});
			resultConsumer = null;
		});
		dbOpen.addEventListener("upgradeneeded", function(evt) {
			dbOpen.result.createObjectStore("filesystem", { keyPath: ["path"] });
		});
	});
}

eagruntimeImpl.platformFilesystem["openDB"] = new WebAssembly.Suspending(openDBImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @return {Promise}
 */
function eaglerDeleteImpl(database, pathName) {
	return new Promise(function(resolve) {
		const tx = database.transaction("filesystem", "readwrite");
		const r = tx.objectStore("filesystem").delete([pathName]);
		r.addEventListener("success", function() {
			resolve(true);
		});
		r.addEventListener("error", function() {
			resolve(false);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerDelete"] = new WebAssembly.Suspending(eaglerDeleteImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @return {Promise}
 */
function eaglerReadImpl(database, pathName) {
	return new Promise(function(resolve) {
		const tx = database.transaction("filesystem", "readonly");
		const r = tx.objectStore("filesystem").get([pathName]);
		r.addEventListener("success", function() {
			resolve(readDBRow(r.result));
		});
		r.addEventListener("error", function() {
			resolve(null);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerRead"] = new WebAssembly.Suspending(eaglerReadImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @param {ArrayBuffer} arr
 * @return {Promise}
 */
function eaglerWriteImpl(database, pathName, arr) {
	return new Promise(function(resolve) {
		const tx = database.transaction("filesystem", "readwrite");
		const r = tx.objectStore("filesystem").put(writeDBRow(pathName, arr));
		r.addEventListener("success", function() {
			resolve(true);
		});
		r.addEventListener("error", function() {
			resolve(false);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerWrite"] = new WebAssembly.Suspending(eaglerWriteImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @return {Promise}
 */
function eaglerExistsImpl(database, pathName) {
	return new Promise(function(resolve) {
		const tx = database.transaction("filesystem", "readonly");
		const r = tx.objectStore("filesystem").count([pathName]);
		r.addEventListener("success", function() {
			resolve(r.result > 0);
		});
		r.addEventListener("error", function() {
			resolve(false);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerExists"] = new WebAssembly.Suspending(eaglerExistsImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathNameOld
 * @param {string} pathNameNew
 * @return {Promise<boolean>}
 */
async function eaglerMoveImpl(database, pathNameOld, pathNameNew) {
	const oldData = await eaglerReadImpl(database, pathNameOld);
	if(!oldData || !(await eaglerWriteImpl(database, pathNameNew, oldData))) {
		return false;
	}
	return await eaglerDeleteImpl(database, pathNameOld);
}

eagruntimeImpl.platformFilesystem["eaglerMove"] = new WebAssembly.Suspending(eaglerMoveImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathNameOld
 * @param {string} pathNameNew
 * @return {Promise<boolean>}
 */
async function eaglerCopyImpl(database, pathNameOld, pathNameNew) {
	const oldData = await eaglerReadImpl(database, pathNameOld);
	return oldData && (await eaglerWriteImpl(database, pathNameNew, oldData));
}

eagruntimeImpl.platformFilesystem["eaglerCopy"] = new WebAssembly.Suspending(eaglerCopyImpl);

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @return {Promise}
 */
function eaglerSizeImpl(database, pathName) {
	return new Promise(function(resolve) {
		const tx = database.transaction("filesystem", "readonly");
		const r = tx.objectStore("filesystem").get([pathName]);
		r.addEventListener("success", function() {
			const data = readDBRow(r.result);
			resolve(data ? data.byteLength : -1);
		});
		r.addEventListener("error", function() {
			resolve(-1);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerSize"] = new WebAssembly.Suspending(eaglerSizeImpl);

/**
 * @param {string} str
 * @return {number}
 */
function countSlashes(str) {
	if(str.length === 0) return -1;
	var j = 0;
	for(var i = 0, l = str.length; i < l; ++i) {
		if(str.charCodeAt(i) === 47) {
			++j;
		}
	}
	return j;
}

/**
 * @param {IDBDatabase} database
 * @param {string} pathName
 * @param {number} recursive
 * @return {Promise}
 */
function eaglerIterateImpl(database, pathName, recursive) {
	return new Promise(function(resolve) {
		const rows = [];
		const tx = database.transaction("filesystem", "readonly");
		const r = tx.objectStore("filesystem").openCursor();
		const b = pathName.length === 0;
		const pc = recursive ? -1 : countSlashes(pathName);
		r.addEventListener("success", function() {
			const c = r.result;
			if(c === null || c.key === null) {
				resolve({
					"length": rows.length,
					/**
					 * @param {number} idx
					 * @return {string}
					 */
					"getRow": function(idx) {
						return rows[idx];
					}
				});
				return;
			}
			const k = readDBKey(c.key);
			if(k != null) {
				if((b || k.startsWith(pathName)) && (recursive || countSlashes(k) === pc)) {
					rows.push(k);
				}
			}
			c.continue();
		});
		r.addEventListener("error", function() {
			resolve(null);
		});
	});
}

eagruntimeImpl.platformFilesystem["eaglerIterate"] = new WebAssembly.Suspending(eaglerIterateImpl);
