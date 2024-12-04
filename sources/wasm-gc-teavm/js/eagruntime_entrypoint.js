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

async function entryPoint() {
	try {
		eaglercraftXOpts = self.__eaglercraftXLoaderContext.getEaglercraftXOpts();
		eagRuntimeJSURL = self.__eaglercraftXLoaderContext.getEagRuntimeJSURL();
		const classesWASM = self.__eaglercraftXLoaderContext.getClassesWASMURL();
		const classesDeobfWASM = self.__eaglercraftXLoaderContext.getClassesDeobfWASMURL();
		const classesTEADBGURL = self.__eaglercraftXLoaderContext.getClassesTEADBGURL();
		epkFileList = self.__eaglercraftXLoaderContext.getEPKFiles();
		rootElement = self.__eaglercraftXLoaderContext.getRootElement();
		splashURL = self.__eaglercraftXLoaderContext.getImageURL(0);
		pressAnyKeyURL = self.__eaglercraftXLoaderContext.getImageURL(1);
		crashURL = self.__eaglercraftXLoaderContext.getImageURL(2);
		faviconURL = self.__eaglercraftXLoaderContext.getImageURL(3);
		const args = self.__eaglercraftXLoaderContext.getMainArgs();
		const isWorker = args[0] === "_worker_process_";

		if(!isWorker) {
			if(!await initializeContext()) {
				return;
			}
		}else {
			setLoggerContextName("worker");
			await initializeContextWorker();
		}

		eagInfo("Loading EaglercraftX WASM GC binary...");

		const teavm = await wasmGC.load(classesWASM, {
			stackDeobfuscator: {
				enabled: true,
				path: classesDeobfWASM,
				infoLocation: "external",
				externalInfoPath: classesTEADBGURL
			},
			installImports: function(/** {Object} */ importObj) {
				importObj[WASMGCBufferAllocatorName] = eagruntimeImpl.WASMGCBufferAllocator;
				importObj[platfApplicationName] = eagruntimeImpl.platformApplication;
				importObj[platfAssetsName] = eagruntimeImpl.platformAssets;
				importObj[platfAudioName] = eagruntimeImpl.platformAudio;
				importObj[platfFilesystemName] = eagruntimeImpl.platformFilesystem;
				importObj[platfInputName] = eagruntimeImpl.platformInput;
				importObj[platfNetworkingName] = eagruntimeImpl.platformNetworking;
				importObj[platfOpenGLName] = eagruntimeImpl.platformOpenGL;
				importObj[platfRuntimeName] = eagruntimeImpl.platformRuntime;
				importObj[platfScreenRecordName] = eagruntimeImpl.platformScreenRecord;
				importObj[platfVoiceClientName] = eagruntimeImpl.platformVoiceClient;
				importObj[platfWebRTCName] = eagruntimeImpl.platformWebRTC;
				importObj[platfWebViewName] = eagruntimeImpl.platformWebView;
				importObj[clientPlatfSPName] = eagruntimeImpl.clientPlatformSingleplayer;
				importObj[serverPlatfSPName] = eagruntimeImpl.serverPlatformSingleplayer;
				importObj["teavm"]["notifyHeapResized"] = function() {
					handleMemoryResized(teavm.exports.memory);
				};
			}
		});

		classesWASMModule = teavm.modules.classes;
		classesDeobfWASMModule = teavm.modules.deobfuscator;
		classesTEADBG = teavm.exports.debugInfo;

		handleMemoryResized(teavm.exports.memory);
		deobfuscatorFunc = /** @type {function(Array<number>):Array<Object>|null} */ (teavm.exports["deobfuscator"]);

		eagInfo("Calling entry point with args: {}", JSON.stringify(args));

		try {
			await WebAssembly.promising(teavm.exports["main"]["__impl"])(args);
		}catch(ex) {
			teavm.exports["main"]["__rethrow"](ex);
		}finally {
			eagWarn("Main function has returned!");
		}
	}catch(ex) {
		displayUncaughtCrashReport(ex);
	}
}

if(typeof self.__eaglercraftXLoaderContext === "object") {
	self.__eaglercraftXLoaderContext.runMain(entryPoint);
}else {
	console.error("???");
}
