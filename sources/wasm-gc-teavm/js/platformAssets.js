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

const platfAssetsName = "platformAssets";
	
/**
 * @param {number} idx
 * @return {Object}
 */
eagruntimeImpl.platformAssets["getEPKFileData"] = function(idx) {
	const tmp = epkFileList[idx];
	epkFileList[idx] = null;
	return tmp;
};

/**
 * @return {number}
 */
eagruntimeImpl.platformAssets["getEPKFileCount"] = function() {
	return epkFileList.length;
};

if(typeof window !== "undefined") {

	/**
	 * @param {Uint8Array} bufferData
	 * @param {string} mime
	 * @return {Promise}
	 */
	function loadImageFile0Impl(bufferData, mime) {
		return new Promise(function(resolve) {
			const loadURL = URL.createObjectURL(new Blob([bufferData], {type: mime}));
			if(loadURL) {
				const toLoad = document.createElement("img");
				toLoad.addEventListener("load", function(evt) {
					URL.revokeObjectURL(loadURL);
					resolve({
						"width": toLoad.width,
						"height": toLoad.height,
						"img": toLoad
					});
				});
				toLoad.addEventListener("error", function(evt) {
					URL.revokeObjectURL(loadURL);
					resolve(null);
				});
				toLoad.src = loadURL;
			}else {
				resolve(null);
			}
		});
	}

	eagruntimeImpl.platformAssets["loadImageFile0"] = new WebAssembly.Suspending(loadImageFile0Impl);

	/** @type {HTMLCanvasElement} */
	var imageLoadingCanvas = null;

	/** @type {CanvasRenderingContext2D} */
	var imageLoadingContext = null;

	/**
	 * @param {Object} imageLoadResult
	 * @param {Uint8ClampedArray} dataDest
	 */
	eagruntimeImpl.platformAssets["loadImageFile1"] = function(imageLoadResult, dataDest) {
		const width = imageLoadResult["width"];
		const height = imageLoadResult["height"];
		const img = imageLoadResult["img"];
		if(img) {
			if(!imageLoadingCanvas) {
				imageLoadingCanvas = /** @type {HTMLCanvasElement} */ (document.createElement("canvas"));
			}
			if(imageLoadingCanvas.width < width) {
				imageLoadingCanvas.width = width;
			}
			if(imageLoadingCanvas.height < height) {
				imageLoadingCanvas.height = height;
			}
			if(!imageLoadingContext) {
				imageLoadingContext = /** @type {CanvasRenderingContext2D} */ (imageLoadingCanvas.getContext("2d", { willReadFrequently: true }));
				imageLoadingContext.imageSmoothingEnabled = false;
			}
			imageLoadingContext.clearRect(0, 0, width, height);
			imageLoadingContext.drawImage(img, 0, 0, width, height);
			dataDest.set(imageLoadingContext.getImageData(0, 0, width, height).data, 0);
		}
	};

}else {
	setUnsupportedFunc(eagruntimeImpl.platformAssets, platfAssetsName, "loadImageFile0");
	setUnsupportedFunc(eagruntimeImpl.platformAssets, platfAssetsName, "loadImageFile1");
}
