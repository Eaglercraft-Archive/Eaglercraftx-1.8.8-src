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

#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>

#include "epw_header.h"
#include "imports.h"
#include "xz/xz.h"

static uint32_t initEPWBinaryCompressedHelper(struct epw_slice_compressed* sliceIn, uint32_t epwLen);
static uint32_t initEPWBinaryHelper(struct epw_slice* sliceIn, uint32_t epwLen);
static uint32_t initEPWStringHelper(struct epw_slice* sliceIn, uint32_t epwLen);

#define SLICE_IN_BOUNDS(pSlice, epwLen) (((struct epw_slice*)(pSlice))->sliceOffset + ((struct epw_slice*)(pSlice))->sliceLength <= (epwLen))

// Note: Linux kernel uses 4096
#define DEC_CHUNK_SIZE 16384

static char sprintfBuffer[65];
static uint8_t inputBuffer[DEC_CHUNK_SIZE];
static uint8_t outputBuffer[DEC_CHUNK_SIZE];

const char *const BAD_ALLOC = "Memory allocation failed";
const char *const EPW_INCOMPLETE = "EPW file is incomplete";
const char *const EPW_INVALID = "EPW file is invalid";

int main(int argc, char** argv) {
	dbgLog("Executing loader WASM binary...");
	
	uint32_t epwLen = getEPWLength();
	
	snprintf(sprintfBuffer, sizeof(sprintfBuffer), "(Loading a %u byte EPW file)", epwLen);
	dbgLog(sprintfBuffer);
	
	if(epwLen < 384) {
		resultFailed(EPW_INCOMPLETE);
		return -1;
	}
	
	struct epw_header* headerPtr = (struct epw_header*)malloc(384);
	if(!headerPtr) {
		resultFailed(BAD_ALLOC);
		return -1;
	}
	
	memcpyFromEPW(headerPtr, 0, 384);
	
	// hehehe
	if(*(uint64_t*)&headerPtr->magic != *(const uint64_t*)"EAG$WASM") {
		resultFailed("The data provided is not an EPW file");
		return -1;
	}
	
	dbgLog("Checking primary CRC32 checksum...");
	
	uint32_t crc32Val = 0;
	uint32_t epwRem = epwLen - 16;
	uint32_t j;
	
	xz_crc32_init();
	
	while(epwRem > 0) {
		j = epwRem < DEC_CHUNK_SIZE ? epwRem : DEC_CHUNK_SIZE;
		memcpyFromEPW(inputBuffer, epwLen - epwRem, j);
		epwRem -= j;
		crc32Val = xz_crc32(inputBuffer, (size_t)j, crc32Val);
	}
	
	if(crc32Val != headerPtr->fileCRC32) {
		resultFailed("EPW file has an invalid checksum");
		return -1;
	}
	
	uint32_t numEPKs = headerPtr->numEPKs;
	uint32_t headerLen = ((276 + 32 * numEPKs) + 127) & ~127;
	
	if(headerLen > 384) {
		snprintf(sprintfBuffer, sizeof(sprintfBuffer), "Note: Has %u EPK files, extending header to %u bytes", numEPKs, headerLen);
		dbgLog(sprintfBuffer);
		free(headerPtr);
		if(headerLen > epwLen) {
			resultFailed(EPW_INCOMPLETE);
			return -1;
		}
		headerPtr = (struct epw_header*)malloc((size_t)headerLen);
		if(!headerPtr) {
			resultFailed("Memory allocation failed");
			return -1;
		}
		memcpyFromEPW(headerPtr, 0, headerLen);
	}
	
	if(!getJSPISupported()) {
		dbgErr("JSPI is not supported! The client cannot start");
		
		struct jspi_unsupported_load_result result;
		
		dbgLog("Copying crash image...");
		
		result.crashImgData = initEPWBinaryHelper(&headerPtr->crashImageData, epwLen);
		result.crashImgMIME = initEPWStringHelper(&headerPtr->crashImageMIME, epwLen);
		
		dbgLog("Decompressing error screen...");
		
		result.markup = initEPWBinaryCompressedHelper(&headerPtr->JSPIUnavailableData, epwLen);
		if(!result.markup) {
			resultFailed(EPW_INVALID);
			return -1;
		}
		
		dbgLog("Displaying error screen...");
		
		resultJSPIUnsupported(&result);
		
		return 0;
	}
	
	struct epw_load_result* result = (struct epw_load_result*)malloc(sizeof(struct epw_load_result) + sizeof(struct epw_load_result_epk) * numEPKs);
	
	dbgLog("Copying non-compressed segments...");
	
	result->pressAnyKeyImgData = initEPWBinaryHelper(&headerPtr->pressAnyKeyImageData, epwLen);
	if(!result->pressAnyKeyImgData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->pressAnyKeyImgMIME = initEPWStringHelper(&headerPtr->pressAnyKeyImageMIME, epwLen);
	if(!result->pressAnyKeyImgMIME) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->crashImgData = initEPWBinaryHelper(&headerPtr->crashImageData, epwLen);
	if(!result->crashImgData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->crashImgMIME = initEPWStringHelper(&headerPtr->crashImageMIME, epwLen);
	if(!result->crashImgMIME) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->faviconImgData = initEPWBinaryHelper(&headerPtr->faviconImageData, epwLen);
	if(!result->faviconImgData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->faviconImgMIME = initEPWStringHelper(&headerPtr->faviconImageMIME, epwLen);
	if(!result->faviconImgMIME) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	dbgLog("Decompressing eagruntime.js...");
	
	result->eagruntimeJSData = initEPWBinaryCompressedHelper(&headerPtr->eagruntimeJSData, epwLen);
	if(!result->eagruntimeJSData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	dbgLog("Decompressing classes.wasm...");
	
	result->classesWASMData = initEPWBinaryCompressedHelper(&headerPtr->classesWASMData, epwLen);
	if(!result->classesWASMData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	dbgLog("Decompressing classes.wasm.teadbg...");
	
	result->classesDeobfTEADBGData = initEPWBinaryCompressedHelper(&headerPtr->classesDeobfTEADBGData, epwLen);
	if(!result->classesDeobfTEADBGData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	dbgLog("Decompressing deobfuscator...");
	
	result->classesDeobfWASMData = initEPWBinaryCompressedHelper(&headerPtr->classesDeobfWASMData, epwLen);
	if(!result->classesDeobfWASMData) {
		resultFailed(EPW_INVALID);
		return -1;
	}
	
	result->numEPKs = numEPKs;
	
	for(uint32_t i = 0; i < numEPKs; ++i) {
		struct epw_assets_epk_file* epkFile = &headerPtr->assetsEPKs[i];
		
		if(!SLICE_IN_BOUNDS(&epkFile->filePath, epwLen)) {
			resultFailed("EPW file contains an invalid offset");
			return -1;
		}
		
		char nameBuffer[33];
		
		uint32_t nameStrLen = epkFile->filePath.sliceLength;
		if(nameStrLen > 32) {
			nameStrLen = 32;
		}
		
		memcpyFromEPW(nameBuffer, epkFile->filePath.sliceOffset, nameStrLen);
		nameBuffer[nameStrLen] = 0;
		
		snprintf(sprintfBuffer, sizeof(sprintfBuffer), "Decompressing assets EPK \"%s\"...", nameBuffer);
		dbgLog(sprintfBuffer);
		
		struct epw_load_result_epk* epkId = &result->epkData[i];
		
		epkId->epkData = initEPWBinaryCompressedHelper(&epkFile->fileData, epwLen);
		epkId->epkName = initEPWStringHelper(&epkFile->filePath, epwLen);
		epkId->epkPath = initEPWStringHelper(&epkFile->loadPath, epwLen);
		
		if(!epkId->epkData || !epkId->epkName || !epkId->epkPath) {
			resultFailed(EPW_INVALID);
			return -1;
		}
	}
	
	dbgLog("Loader WASM binary executed successfully!");
	
	resultSuccess(result);
	
	return 0;
}

static uint32_t initEPWBinaryCompressedHelper(struct epw_slice_compressed* sliceIn, uint32_t epwLen) {
	if(!SLICE_IN_BOUNDS(sliceIn, epwLen)) {
		dbgErr("EPW file contains an invalid compressed offset");
		return 0;
	}
	
	uint32_t bufId = initResult(sliceIn->sliceDecompressedLength);
	
	struct xz_buf b;
	
	b.in = inputBuffer;
	b.in_pos = 0;
	b.in_size = 0;
	b.out = outputBuffer;
	b.out_pos = 0;
	b.out_size = DEC_CHUNK_SIZE;
	
	struct xz_dec* s;
	enum xz_ret ret;
		
	s = xz_dec_init(XZ_DYNALLOC, (uint32_t)33554432);
	if(!s) {
		dbgErr("Failed to initialize XZ decompression stream");
		return 0;
	}
	
	uint32_t bufInPos = 0;
	uint32_t bufOutPos = 0;
	uint32_t remainingIn = sliceIn->sliceCompressedLength;
	uint32_t remainingOut = sliceIn->sliceDecompressedLength;
	uint32_t toRead = 0;
	uint32_t i;
	
	do {
		if(b.in_pos == b.in_size) {
			i = (uint32_t)b.in_pos;
			if(i > remainingIn) {
				dbgErr("Decompression input buffer overflowed");
				xz_dec_end(s);
				return 0;
			}
			remainingIn -= i;
			
			toRead = remainingIn < DEC_CHUNK_SIZE ? remainingIn : DEC_CHUNK_SIZE;
			b.in_pos = 0;
			b.in_size = (size_t)toRead;
			
			memcpyFromEPW(inputBuffer, sliceIn->sliceOffset + bufInPos, toRead);
			
			bufInPos += toRead;
		}
		
		ret = xz_dec_run(s, &b);
		
		if(b.out_pos == b.out_size || (ret == XZ_STREAM_END && b.out_pos > 0)) {
			i = (uint32_t)b.out_pos;
			if(i > remainingOut) {
				dbgErr("Decompression output buffer overflowed");
				xz_dec_end(s);
				return 0;
			}
			memcpyToResult(bufId, outputBuffer, bufOutPos, i);
			remainingOut -= i;
			bufOutPos += i;
			b.out_pos = 0;
		}
	}while(ret == XZ_OK);
	
	xz_dec_end(s);
	
	if(ret != XZ_STREAM_END) {
		snprintf(sprintfBuffer, sizeof(sprintfBuffer), "Decompression failed, code %u!", (uint32_t)ret);
		dbgErr(sprintfBuffer);
		return 0;
	}
	
	if(b.in_pos > remainingIn) {
		dbgErr("Decompression input buffer overflowed");
		return 0;
	}
	remainingIn -= (uint32_t)b.in_pos;
	
	if(remainingIn > 0) {
		dbgErr("Decompression completed, but there is still some input data remaining");
		return 0;
	}
	
	return bufId;
}

static uint32_t initEPWBinaryHelper(struct epw_slice* sliceIn, uint32_t epwLen) {
	if(!SLICE_IN_BOUNDS(sliceIn, epwLen)) {
		dbgErr("EPW file contains an invalid offset");
		return 0;
	}else {
		uint32_t ret = initResult(sliceIn->sliceLength);
		memcpyFromEPWToResult(ret, 0, sliceIn->sliceOffset, sliceIn->sliceLength);
		return ret;
	}
}

static uint32_t initEPWStringHelper(struct epw_slice* sliceIn, uint32_t epwLen) {
	if(!SLICE_IN_BOUNDS(sliceIn, epwLen)) {
		dbgErr("EPW file contains an invalid offset");
		return 0;
	}else {
		return initEPWStringResult(sliceIn->sliceOffset, sliceIn->sliceLength);
	}
}
