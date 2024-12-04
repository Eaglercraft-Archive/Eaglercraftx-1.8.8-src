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

#ifndef _INCLUDED_EPW_HEADER_H
#define _INCLUDED_EPW_HEADER_H

#include "stdint.h"

struct epw_slice {
	uint32_t sliceOffset;
	uint32_t sliceLength;
};

struct epw_slice_compressed {
	uint32_t sliceOffset;
	uint32_t sliceCompressedLength;
	uint32_t sliceDecompressedLength;
	uint32_t _reserved;
};

struct epw_assets_epk_file {
	struct epw_slice filePath;
	struct epw_slice loadPath;
	struct epw_slice_compressed fileData;
};

struct epw_header {
	uint8_t magic[8];
	
	uint32_t fileLength;
	uint32_t fileCRC32;
	uint16_t versionMajor;
	uint16_t versionMinor;
	
	uint32_t clientVersionInt;
	
	struct epw_slice clientPackageName;
	struct epw_slice clientOriginName;
	struct epw_slice clientOriginVersion;
	struct epw_slice clientOriginVendor;
	struct epw_slice clientForkName;
	struct epw_slice clientForkVersion;
	struct epw_slice clientForkVendor;
	struct epw_slice metadataSegment;
	
	uint64_t creationTime;
	uint32_t numEPKs;
	
	struct epw_slice splashImageData;
	struct epw_slice splashImageMIME;
	struct epw_slice pressAnyKeyImageData;
	struct epw_slice pressAnyKeyImageMIME;
	struct epw_slice crashImageData;
	struct epw_slice crashImageMIME;
	struct epw_slice faviconImageData;
	struct epw_slice faviconImageMIME;
	
	struct epw_slice loaderJSData;
	uint32_t _reserved_0;
	uint32_t _reserved_1;
	
	struct epw_slice loaderWASMData;
	uint32_t _reserved_2;
	uint32_t _reserved_3;
	
	struct epw_slice_compressed JSPIUnavailableData;
	struct epw_slice_compressed eagruntimeJSData;
	struct epw_slice_compressed classesWASMData;
	struct epw_slice_compressed classesDeobfTEADBGData;
	struct epw_slice_compressed classesDeobfWASMData;
	
	struct epw_assets_epk_file assetsEPKs[];
};

#endif
