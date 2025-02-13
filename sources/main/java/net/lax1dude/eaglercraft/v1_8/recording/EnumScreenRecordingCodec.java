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

package net.lax1dude.eaglercraft.v1_8.recording;

public enum EnumScreenRecordingCodec {

	CODEC_MP4_H264_GENERIC_AAC("MP4 (video: H.264 Default, audio: AAC LC)", "mp4", "video/mp4", "avc1", "mp4a.40.2", false),
	CODEC_MP4_H264_GENERIC_OPUS("MP4 (video: H.264 Default, audio: Opus)", "mp4", "video/mp4", "avc1", "opus", false),
	CODEC_MP4_H264_L40_AAC("MP4 (video: H.264 CBP L4.0, audio: AAC LC)", "mp4", "video/mp4", "avc1.424028", "mp4a.40.2", true),
	CODEC_MP4_H264_L40_OPUS("MP4 (video: H.264 CBP L4.0, audio: Opus)", "mp4", "video/mp4", "avc1.424028", "opus", true),
	CODEC_MP4_H264_L42_AAC("MP4 (video: H.264 CBP L4.2, audio: AAC LC)", "mp4", "video/mp4", "avc1.42402A", "mp4a.40.2", true),
	CODEC_MP4_H264_L42_OPUS("MP4 (video: H.264 CBP L4.2, audio: Opus)", "mp4", "video/mp4", "avc1.42402A", "opus", true),
	CODEC_MP4_H264_L50_AAC("MP4 (video: H.264 CBP L5.0, audio: AAC LC)", "mp4", "video/mp4", "avc1.424032", "mp4a.40.2", true),
	CODEC_MP4_H264_L50_OPUS("MP4 (video: H.264 CBP L5.0, audio: Opus)", "mp4", "video/mp4", "avc1.424032", "opus", true),
	CODEC_MP4_H264_L52_AAC("MP4 (video: H.264 CBP L5.2, audio: AAC LC)", "mp4", "video/mp4", "avc1.424034", "mp4a.40.2", true),
	CODEC_MP4_H264_L52_OPUS("MP4 (video: H.264 CBP L5.2, audio: Opus)", "mp4", "video/mp4", "avc1.424034", "opus", true),

	CODEC_MP4_VP9_GENERIC_AAC("MP4 (video: VP9 Default, audio: AAC LC)", "mp4", "video/mp4", "vp9", "mp4a.40.2", false),
	CODEC_MP4_VP9_GENERIC_OPUS("MP4 (video: VP9 Default, audio: Opus)", "mp4", "video/mp4", "vp9", "opus", false),
	CODEC_MP4_VP9_L40_AAC("MP4 (video: VP9 8-bit L4.0, audio: AAC LC)", "mp4", "video/mp4", "vp9.00.40.08", "mp4a.40.2", true),
	CODEC_MP4_VP9_L40_OPUS("MP4 (video: VP9 8-bit L4.0, audio: Opus)", "mp4", "video/mp4", "vp9.00.40.08", "opus", true),
	CODEC_MP4_VP9_L41_AAC("MP4 (video: VP9 8-bit L4.1, audio: AAC LC)", "mp4", "video/mp4", "vp9.00.41.08", "mp4a.40.2", true),
	CODEC_MP4_VP9_L41_OPUS("MP4 (video: VP9 8-bit L4.1, audio: Opus)", "mp4", "video/mp4", "vp9.00.41.08", "opus", true),
	CODEC_MP4_VP9_L50_AAC("MP4 (video: VP9 8-bit L5.0, audio: AAC LC)", "mp4", "video/mp4", "vp9.00.50.08", "mp4a.40.2", true),
	CODEC_MP4_VP9_L50_OPUS("MP4 (video: VP9 8-bit L5.0, audio: Opus)", "mp4", "video/mp4", "vp9.00.50.08", "opus", true),
	CODEC_MP4_VP9_L51_AAC("MP4 (video: VP9 8-bit L5.1, audio: AAC LC)", "mp4", "video/mp4", "vp9.00.51.08", "mp4a.40.2", true),
	CODEC_MP4_VP9_L51_OPUS("MP4 (video: VP9 8-bit L5.1, audio: Opus)", "mp4", "video/mp4", "vp9.00.51.08", "opus", true),

	CODEC_MP4_GENERIC("MP4 (Default Codecs)", "mp4", "video/mp4", null, null, false),

	CODEC_WEBM_H264_GENERIC_OPUS("WEBM (video: H.264 Default, audio: Opus)", "webm", "video/webm", "avc1", "opus", false),
	CODEC_WEBM_H264_GENERIC_VORBIS("WEBM (video: H.264 Default, audio: Vorbis)", "webm", "video/webm", "avc1", "vorbis", false),
	CODEC_WEBM_H264_L40_OPUS("WEBM (video: H.264 CBP L4.0, audio: Opus)", "webm", "video/webm", "avc1.424028", "opus", true),
	CODEC_WEBM_H264_L40_VORBIS("WEBM (video: H.264 CBP L4.0, audio: Vorbis)", "webm", "video/webm", "avc1.424028", "vorbis", true),
	CODEC_WEBM_H264_L42_OPUS("WEBM (video: H.264 CBP L4.2, audio: Opus)", "webm", "video/webm", "avc1.42402A", "opus", true),
	CODEC_WEBM_H264_L42_VORBIS("WEBM (video: H.264 CBP L4.2, audio: Vorbis)", "webm", "video/webm", "avc1.42402A", "vorbis", true),
	CODEC_WEBM_H264_L50_OPUS("WEBM (video: H.264 CBP L5.0, audio: Opus)", "webm", "video/webm", "avc1.424032", "opus", true),
	CODEC_WEBM_H264_L50_VORBIS("WEBM (video: H.264 CBP L5.0, audio: Vorbis)", "webm", "video/webm", "avc1.424032", "vorbis", true),
	CODEC_WEBM_H264_L52_OPUS("WEBM (video: H.264 CBP L5.2, audio: Opus)", "webm", "video/webm", "avc1.424034", "opus", true),
	CODEC_WEBM_H264_L52_VORBIS("WEBM (video: H.264 CBP L5.2, audio: Vorbis)", "webm", "video/webm", "avc1.424034", "vorbis", true),

	CODEC_WEBM_VP9_GENERIC_OPUS("WEBM (video: VP9 Default, audio: Opus)", "webm", "video/webm", "vp9", "opus", false),
	CODEC_WEBM_VP9_GENERIC_VORBIS("WEBM (video: VP9 Default, audio: Vorbis)", "webm", "video/webm", "vp9", "vorbis", false),
	CODEC_WEBM_VP9_L40_OPUS("WEBM (video: VP9 8-bit L4.0, audio: Opus)", "webm", "video/webm", "vp9.00.40.08", "opus", true),
	CODEC_WEBM_VP9_L40_VORBIS("WEBM (video: VP9 8-bit L4.0, audio: Vorbis)", "webm", "video/webm", "vp9.00.40.08", "vorbis", true),
	CODEC_WEBM_VP9_L41_OPUS("WEBM (video: VP9 8-bit L4.1, audio: Opus)", "webm", "video/webm", "vp9.00.41.08", "opus", true),
	CODEC_WEBM_VP9_L41_VORBIS("WEBM (video: VP9 8-bit L4.1, audio: Vorbis)", "webm", "video/webm", "vp9.00.41.08", "vorbis", true),
	CODEC_WEBM_VP9_L50_OPUS("WEBM (video: VP9 8-bit L5.0, audio: Opus)", "webm", "video/webm", "vp9.00.50.08", "opus", true),
	CODEC_WEBM_VP9_L50_VORBIS("WEBM (video: VP9 8-bit L5.0, audio: Vorbis)", "webm", "video/webm", "vp9.00.50.08", "vorbis", true),
	CODEC_WEBM_VP9_L51_OPUS("WEBM (video: VP9 8-bit L5.1, audio: Opus)", "webm", "video/webm", "vp9.00.51.08", "opus", true),
	CODEC_WEBM_VP9_L51_VORBIS("WEBM (video: VP9 8-bit L5.1, audio: Vorbis)", "webm", "video/webm", "vp9.00.51.08", "vorbis", true),

	CODEC_WEBM_VP8_GENERIC_OPUS("WEBM (video: VP8 Default, audio: Opus)", "webm", "video/webm", "vp8", "opus", false),
	CODEC_WEBM_VP8_GENERIC_VORBIS("WEBM (video: VP8 Default, audio: Vorbis)", "webm", "video/webm", "vp8", "vorbis", false),

	CODEC_WEBM_GENERIC("WEBM (Default Codecs)", "webm", "video/webm", null, null, false);

	public static final EnumScreenRecordingCodec[] preferred_codec_order = new EnumScreenRecordingCodec[] {
			CODEC_MP4_H264_GENERIC_AAC,
			CODEC_MP4_H264_L52_AAC,
			CODEC_MP4_H264_L50_AAC,
			CODEC_MP4_H264_L42_AAC,
			CODEC_MP4_H264_L40_AAC,
			CODEC_MP4_H264_GENERIC_OPUS,
			CODEC_MP4_H264_L40_OPUS,
			CODEC_MP4_H264_L42_OPUS,
			CODEC_MP4_H264_L50_OPUS,
			CODEC_MP4_H264_L52_OPUS,
			CODEC_WEBM_H264_GENERIC_OPUS,
			CODEC_WEBM_H264_L52_OPUS,
			CODEC_WEBM_H264_L50_OPUS,
			CODEC_WEBM_H264_L42_OPUS,
			CODEC_WEBM_H264_L40_OPUS,
			CODEC_WEBM_H264_GENERIC_VORBIS,
			CODEC_WEBM_H264_L52_VORBIS,
			CODEC_WEBM_H264_L50_VORBIS,
			CODEC_WEBM_H264_L42_VORBIS,
			CODEC_WEBM_H264_L40_VORBIS,
			CODEC_MP4_VP9_GENERIC_AAC,
			CODEC_MP4_VP9_L51_AAC,
			CODEC_MP4_VP9_L50_AAC,
			CODEC_MP4_VP9_L41_AAC,
			CODEC_MP4_VP9_L40_AAC,
			CODEC_MP4_VP9_GENERIC_OPUS,
			CODEC_MP4_VP9_L51_OPUS,
			CODEC_MP4_VP9_L50_OPUS,
			CODEC_MP4_VP9_L41_OPUS,
			CODEC_MP4_VP9_L40_OPUS,
			CODEC_WEBM_VP9_GENERIC_OPUS,
			CODEC_WEBM_VP9_L51_OPUS,
			CODEC_WEBM_VP9_L50_OPUS,
			CODEC_WEBM_VP9_L41_OPUS,
			CODEC_WEBM_VP9_L40_OPUS,
			CODEC_WEBM_VP9_GENERIC_VORBIS,
			CODEC_WEBM_VP9_L51_VORBIS,
			CODEC_WEBM_VP9_L50_VORBIS,
			CODEC_WEBM_VP9_L41_VORBIS,
			CODEC_WEBM_VP9_L40_VORBIS,
			CODEC_WEBM_VP8_GENERIC_OPUS,
			CODEC_WEBM_VP8_GENERIC_VORBIS,
			CODEC_MP4_GENERIC,
			CODEC_WEBM_GENERIC
	};

	public final String name;
	public final String fileExt;
	public final String container;
	public final String videoCodec;
	public final String audioCodec;
	public final boolean advanced;
	public final String mimeType;
	
	private EnumScreenRecordingCodec(String name, String fileExt, String container, String videoCodec, String audioCodec, boolean advanced) {
		this.name = name;
		this.fileExt = fileExt;
		this.container = container;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
		this.advanced = advanced;
		if(videoCodec != null || audioCodec != null) {
			StringBuilder mimeBuilder = new StringBuilder(container);
			mimeBuilder.append(";codecs=\"");
			if(videoCodec != null) {
				mimeBuilder.append(videoCodec);
				if(audioCodec != null) {
					mimeBuilder.append(',');
				}
			}
			if(audioCodec != null) {
				mimeBuilder.append(audioCodec);
			}
			mimeBuilder.append("\"");
			this.mimeType = mimeBuilder.toString();
		}else {
			this.mimeType = container;
		}
	}

}