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

Module["locateFile"] = function(path) {
	if(path === "loader.wasm") {
		return window.__eaglercraftXLoaderContextPre.loaderWASMURL;
	}else {
		return path;
	}
};

const rootElement = window.__eaglercraftXLoaderContextPre.rootElement;
const optsObj = window.__eaglercraftXLoaderContextPre.eaglercraftXOpts;
const epwFile = window.__eaglercraftXLoaderContextPre.theEPWFileBuffer;
const splashURL = window.__eaglercraftXLoaderContextPre.splashURL;

const results = [ null ];

function createCrashParentElement() {
	var oldSplash = null;
	
	var node;
	while(node = rootElement.lastChild) {
		if(!oldSplash) {
			oldSplash = node;
		}
		rootElement.removeChild(node);
	}
	
	const parentElement = document.createElement("div");
	parentElement.classList.add("_eaglercraftX_wrapper_element");
	parentElement.setAttribute("style", "position:relative;width:100%;height:100%;overflow:hidden;");
	
	if(oldSplash) {
		oldSplash.classList.add("_eaglercraftX_early_splash_element");
		oldSplash.style.position = "absolute";
		oldSplash.style.top = "0px";
		oldSplash.style.left = "0px";
		oldSplash.style.right = "0px";
		oldSplash.style.bottom = "0px";
		oldSplash.style.zIndex = "2";
		parentElement.appendChild(oldSplash);
	}
	
	rootElement.appendChild(parentElement);
	
	return parentElement;
}
