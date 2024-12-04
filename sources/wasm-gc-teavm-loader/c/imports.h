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

#ifndef _INCLUDED_IMPORTS_H
#define _INCLUDED_IMPORTS_H

#include "stdint.h"

struct epw_load_result_epk {
	uint32_t epkData;
	uint32_t epkName;
	uint32_t epkPath;
};

struct epw_load_result {
	uint32_t eagruntimeJSData;
	uint32_t classesWASMData;
	uint32_t classesDeobfTEADBGData;
	uint32_t classesDeobfWASMData;
	uint32_t pressAnyKeyImgData;
	uint32_t pressAnyKeyImgMIME;
	uint32_t crashImgData;
	uint32_t crashImgMIME;
	uint32_t faviconImgData;
	uint32_t faviconImgMIME;
	uint32_t numEPKs;
	struct epw_load_result_epk epkData[];
};

struct jspi_unsupported_load_result {
	uint32_t crashImgData;
	uint32_t crashImgMIME;
	uint32_t markup;
};

#define LOAD_RESULT_SIZE(numEPKs) (sizeof(struct epw_load_result) + sizeof(struct epw_load_result_epk) * (numEPKs))

extern uint8_t getJSPISupported();

extern uint32_t getEPWLength();
extern void memcpyFromEPW(void* dest, uint32_t off, uint32_t len);
extern uint32_t initResult(uint32_t bufLen);
extern void memcpyToResult(uint32_t bufId, const void* src, uint32_t off, uint32_t len);
extern void memcpyFromEPWToResult(uint32_t bufId, uint32_t dest, uint32_t off, uint32_t len);
extern uint32_t initEPWStringResult(uint32_t off, uint32_t len);

extern void resultFailed(const char* msg);
extern void resultSuccess(const struct epw_load_result* result);
extern void resultJSPIUnsupported(struct jspi_unsupported_load_result* result);

extern void dbgLog(const char* msg);
extern void dbgErr(const char* msg);

#endif
